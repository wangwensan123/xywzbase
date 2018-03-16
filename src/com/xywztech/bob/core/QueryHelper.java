package com.xywztech.bob.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class QueryHelper {
	private static Logger log = Logger.getLogger(QueryHelper.class);
    /** 主键字段 */
    private String primaryKey = "ID";
    /** 原始SQL */
    private String originSQL;
    /** 加上分页的SQL */
    private String pagingSQL;
    /** 数据库连接 */
    private Connection connection;
    /** 分页信息类 */
    private PagingInfo paging;
    /** Oracle字典映射 */
    private ConcurrentHashMap<String, String> oracleMapping = new ConcurrentHashMap<String, String>();
   
    LookupManager manager = LookupManager.getInstance();
    
    protected String prepareSQL(String SQL) {

        StringBuilder builder = new StringBuilder(SQL);
        if(paging != null) {

          	builder.insert(0, "SELECT *  FROM (SELECT @rownum := @rownum + 1 AS RN, BUSINESS_QUERY.* FROM (");
            builder.append(") BUSINESS_QUERY,(SELECT @rownum := 0) r) SUB_QUERY WHERE RN BETWEEN " + paging.getBeginRowNumber());
            builder.append(" AND " + paging.getEndRowNumber());
            log.info("JDBC分页查询语句："+builder.toString());
        }
        return builder.toString();
    }
    
    protected void processLookup(HashMap<String, Object> map, String name, Object code) {
        if (oracleMapping.containsKey(name)) {
            String lookupName = oracleMapping.get(name);
            ConcurrentHashMap<String, String> lookup = manager.getOracleValues(lookupName);
            String key = code.toString();
            if (lookup != null && lookup.containsKey(key)) {
                map.put(name + "_ORA", lookup.get(key));
            } else {
                map.put(name + "_ORA", code);
            }                         
        }        
    }
    
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }
   
    public QueryHelper(String SQL, Connection connection) {
        this.originSQL = SQL;
        this.connection = connection;
    }

    public QueryHelper(String SQL, Connection connection, PagingInfo paging) {
        this.paging = paging;
        this.originSQL = SQL;
        this.connection = connection;
    }

    public ResultSet executeQuery() throws SQLException {
        pagingSQL = prepareSQL(originSQL);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(pagingSQL);
        return rs;
    }
    
    public Map<String, Object> getJSON() throws SQLException {
        pagingSQL = prepareSQL(originSQL);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(pagingSQL);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> result = new HashMap<String, Object>();
        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
        while (rs.next()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                if ("RN".equals(columnName.toUpperCase())) {
                    continue;
                }
                if(rs.getObject(columnName) != null) {
                    String value = rs.getObject(columnName).toString();
                    map.put(columnName, value);     
                    processLookup(map, columnName, rs.getObject(columnName));
                } else {
                    map.put(columnName, "");
                }
            }
            rowsList.add(map);
        }
        result.put("data", rowsList);
        
        if (paging != null) {
            StringBuilder builder = new StringBuilder(originSQL);
            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
            builder.append(") SUB_QUERY");
            rs = stmt.executeQuery(builder.toString());
            if(rs.next()) {
                result.put("count", rs.getInt("TOTAL"));
            }
        }
        rs.close();
        stmt.close();
        connection.close();
        return result;       
    }
    
    public Map<String, Object> getJSON(boolean needTrans) throws SQLException {
    	if(needTrans){
    		 pagingSQL = prepareSQL(originSQL);
    	        Statement stmt = connection.createStatement();
    	        ResultSet rs = stmt.executeQuery(pagingSQL);
    	        ResultSetMetaData metaData = rs.getMetaData();
    	        int columnCount = metaData.getColumnCount();

    	        Map<String, Object> result = new HashMap<String, Object>();
    	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
    	        while (rs.next()) {
    	            HashMap<String, Object> map = new HashMap<String, Object>();
    	            for (int i = 0; i < columnCount; i++) {
    	                String columnName = metaData.getColumnName(i + 1);
    	                if ("RN".equals(columnName.toUpperCase())) {
    	                    continue;
    	                }
    	                if(rs.getObject(columnName) != null) {
    	                    String value = rs.getObject(columnName).toString();
    	                    map.put(ColumnNameUtil.getModelField(columnName), value);     
    	                    processLookup(map, ColumnNameUtil.getModelField(columnName), rs.getObject(columnName));
    	                } else {
    	                    map.put(ColumnNameUtil.getModelField(columnName), "");
    	                }
    	            }
    	            rowsList.add(map);
    	        }
    	        result.put("data", rowsList);
    	        
    	        if (paging != null) {
    	            StringBuilder builder = new StringBuilder(originSQL);
    	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
    	            builder.append(") SUB_QUERY");
    	            rs = stmt.executeQuery(builder.toString());
    	            if(rs.next()) {
    	                result.put("count", rs.getInt("TOTAL"));
    	            }
    	        }
    	        rs.close();
    	        stmt.close();
    	        connection.close();
    	        return result;  
    	}else{
    		return this.getJSON();
    	}
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

}
