package com.xywztech.bob.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class CorebankingQuery implements Runnable {
    
    private static final String sql = "select t1.aa20dpid, t1.aa20acno, t1.aa20acna," +
    		" t1.aa20sjno, t2.ab10sjna, t1.aa20bldr, t1.aa20cu, t1.aa20acblt," +
            " case when aa20bldr=aa20bldry then aa20acbli-aa20acbly" +
            " when aa20bldr<> aa20bldry then aa20acbli+aa20acbly end as aa20change" +
            " from testk.aapf20 t1, testk.abpf10 t2 where t1.aa20sjno = t2.ab10sjno" +
            " and (substr(t2.ab10sjco,1,1) = '1' or substr(t2.ab10sjco,2,1) <> ''" +
            " and substr(t2.ab10sjco,1,1) = '') and t1.aa20acsts <> '*' and t1.aa20acno = ?";
    
    private static Logger log = Logger.getLogger(CorebankingQuery.class);
    
    private boolean running;
       
    private CorebankingQueue queue;
    
    private DataSource dsOracle;
    
    private CorebankingQueueItem queueItem;
       
    public CorebankingQuery() {
        running = true;
        queue = CorebankingQueue.getInstance();
        ApplicationContext applicationContext = LookupManager.getInstance().getApplicationContext();
        dsOracle = (DataSource) applicationContext.getBean("dsOracle");
    }
    
    protected String processBranchId(int branchId) {
        String str = "00000" + branchId;
        int length = str.length();
        return str.substring(length - 5, length);
    }

    public void stop() {
        running = false;
        queue.notify();
    }
    
    public void pause() {
        try {
            synchronized (queue) {
                queue.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("挂起核心异步查询线程出错:", e);
        }
    }
    
    public void resume() {
        queue.notify();
    }

    public void run() {
        while(running) {
            queueItem = queue.getFirstQueueItem();
            if (queueItem != null) {
                try {
                    executeQuery(queueItem);
                } catch (Exception e) {
                    log.error("核心异步查询出错:", e);
                    queue.moveQueueItem(queueItem);
                }
                queue.removeQueueItem(queueItem);
            } else {
                pause();
            }
        }
    }
    
    public void executeQuery(CorebankingQueueItem queueItem) throws Exception {
        //2012-06-26 km modify;dsCorebanking  ->  dsOracle
    	Connection connCorebanking = dsOracle.getConnection();
        Connection connOracle = dsOracle.getConnection();
        try {
            //System.out.println("[coreBanking query SQL]: "+sql);
            //System.out.println("[Parameter]: "+queueItem.getAccount());
            PreparedStatement pstmtCorebanking = connCorebanking.prepareStatement(sql);
            pstmtCorebanking.setString(1, queueItem.getAccount());
            //System.out.println("Query from Core database start...");
            ResultSet rsCorebanking = pstmtCorebanking.executeQuery();
            //System.out.println("Query successfully!");
            Long id = queueItem.getId();
            if (rsCorebanking.next()) {
                String authUnits = queueItem.getAuthUnits();
                //System.out.println("Query result has been calling: there is one record returned at least!");
                String branchId = processBranchId(rsCorebanking.getInt("AA20DPID"));
                //System.out.println("Get branch Id:["+branchId+"]");
                String sql = "SELECT CASE WHEN ? IN (SELECT UNITID FROM SYS_UNITS " +
                		"START WITH ID = ? CONNECT BY PRIOR ID = SUPERUNITID ) " +
                		"THEN 1 ELSE 0 END AS MATCHED FROM DUAL"; 
                //System.out.println("Check the grant of the user!");
                //System.out.println("[SQL]:"+sql);
                //System.out.println("[Parameter1]:"+branchId);
                //System.out.println("[Parameter2]:"+authUnits);
                PreparedStatement pstmtOracle = connOracle.prepareStatement(sql);
                pstmtOracle.setString(1, branchId);
                pstmtOracle.setString(2, authUnits);
                ResultSet rs = pstmtOracle.executeQuery();
                rs.next();
                if (rs.getInt("MATCHED") > 0) {
                    //System.out.println("Grant check result is:"+rs.getInt("MATCHED"));
                    sql = "UPDATE OCRM_TEMP_CORE_BANKING SET BRANCH_ID = ?, CLIENT_NAME = ?, " +
                    "SUBJECT_ID = ?, SUBJECT_INFO = ?, CURRENCY_TYPE = ?, INDICATE = ?, " +
                    "BALANCE = ?, CHANGED = ?, FINISH_TIME = SYSDATE WHERE ID = ?";
                    //System.out.println("Update the query record.");
                    //System.out.println("[SQL]:"+sql);
                    //System.out.println("[parameter1]:"+branchId);
                    //System.out.println("[parameter2]:"+rsCorebanking.getString("AA20ACNA"));
                    //System.out.println("[parameter3]:"+rsCorebanking.getString("AA20SJNO"));
                    //System.out.println("[parameter4]:"+rsCorebanking.getString("AB10SJNA"));
                    //System.out.println("[parameter5]:"+rsCorebanking.getString("AA20CU"));
                    //System.out.println("[parameter6]:"+rsCorebanking.getString("AA20BLDR"));
                    //System.out.println("[parameter7]:"+rsCorebanking.getDouble("AA20ACBLT"));
                    //System.out.println("[parameter8]:"+rsCorebanking.getDouble("AA20CHANGE"));
                    //System.out.println("[parameter9]:"+id);
                    pstmtOracle = connOracle.prepareStatement(sql);
                    pstmtOracle.setString(1, branchId);
                    pstmtOracle.setString(2, rsCorebanking.getString("AA20ACNA"));
                    pstmtOracle.setString(3, rsCorebanking.getString("AA20SJNO"));
                    pstmtOracle.setString(4, rsCorebanking.getString("AB10SJNA"));
                    pstmtOracle.setString(5, rsCorebanking.getString("AA20CU"));
                    pstmtOracle.setString(6, rsCorebanking.getString("AA20BLDR"));
                    pstmtOracle.setDouble(7, rsCorebanking.getDouble("AA20ACBLT"));
                    pstmtOracle.setDouble(8, rsCorebanking.getDouble("AA20CHANGE"));
                    pstmtOracle.setLong(9, id);
                    pstmtOracle.execute();                    
                } else {
                    
                    sql = "UPDATE OCRM_TEMP_CORE_BANKING SET FINISH_TIME = SYSDATE, " +
                    "COMMENTS = '无权根访问此账号' " + "WHERE ID = ?";
                    //System.out.println("Grant check result:无权根访问此账号");
                    //System.out.println("Update the query record.");
                    //System.out.println("[SQL]:"+sql);
                    //System.out.println("[parameter1]:"+id);
                    pstmtOracle = connOracle.prepareStatement(sql);
                    pstmtOracle.clearParameters();
                    pstmtOracle.setLong(1, id);
                    pstmtOracle.execute();                   
                }
            } else {
                String sql = "UPDATE OCRM_TEMP_CORE_BANKING SET FINISH_TIME = SYSDATE, " +
                        "COMMENTS = '核心中未找到此账号' " + 
                		"WHERE ID = ?";
                ////System.out.println("Grant check result:核心中未找到此账号");
                ////System.out.println("Update the query record.");
                ////System.out.println("[SQL]:"+sql);
                ////System.out.println("[parameter1]:"+id);
                PreparedStatement pstmtOracle = connOracle.prepareStatement(sql);
                pstmtOracle.setLong(1, id);
                pstmtOracle.execute();
            }            
        } finally {
            connCorebanking.close();
            connOracle.close();
            //System.out.println("Connection has been closed, the query is finished!");
        }
    }

}