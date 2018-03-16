package com.xywztech.bcrm.system.service;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xywztech.bcrm.system.model.DataSet;
import com.xywztech.bcrm.system.model.DataSetColumn;


	@Service
	@Transactional(value="postgreTransactionManager")
	public class DataSetService {
		private EntityManager em;

		@PersistenceContext
		public void setEntityManager(EntityManager em) {
		        this.em = em;
		    }	
	public List<DataSetColumn> query(Long Id) {
		StringBuffer querysql = new StringBuffer();
		querysql.append("select c from DataSetColumn c where  c.dbtableId=?1");
		Query q = em.createQuery(querysql.toString());
		q.setParameter(1, Id);
		q.setFirstResult(0);
		q.setMaxResults(100000);
		List<DataSetColumn> list = q.getResultList();
		return list;
	}

	public Long save(DataSet dataSet) throws Exception {
    
           dataSet.setAppId("62");
           if(1==dataSet.getType()){
        	   dataSet.setTypeName("数据库表");
           }
           else if(2==dataSet.getType()){
        	   dataSet.setTypeName("标准SQL");
           }
            //新增
           em.persist(dataSet);
           return dataSet.getId();
	}

	public void update(DataSet dataSet) throws Exception {
    
           dataSet.setAppId("62");
           if(1==dataSet.getType()){
        	   dataSet.setTypeName("数据库表");
           }
           else if(2==dataSet.getType()){
        	   dataSet.setTypeName("标准SQL");
           }
            //修改
           em.merge(dataSet);
	}
	public void batchSave(JSONArray jarray1,JSONArray jarray2,JSONArray jarray3,JSONArray jarray4,JSONArray jarray5,JSONArray jarray6,JSONArray jarray7,JSONArray jarray8,Long dataSetId) throws ParseException{
		
	     if(jarray1.size()>0)
	        {
	    	   
	        	for (int i=0;i<jarray1.size();i++)
	        	{   
	        		DataSetColumn model=new DataSetColumn();
	        		model.setAppId("62");
	        		model.setColNameE(jarray1.get(i).toString());
	        		if("".equals(jarray2.get(i).toString())||jarray2.get(i)==null){
	        			model.setColNameC(" ");
	        		}
	        		else{
	        		model.setColNameC(jarray2.get(i).toString());}
	        		model.setColType(jarray3.get(i).toString());
	        		model.setColSize(Long.parseLong(jarray4.get(i).toString()));
	        		model.setIsNull(jarray5.get(i).toString());
	        		model.setPrimaryKeyFlag(jarray6.get(i).toString());
	        		model.setNotes(jarray7.get(i).toString());
	        		model.setColSort(Long.parseLong(jarray8.get(i).toString()));
	        		model.setDbtableId(dataSetId);
	        		model.setIsEnable("true");
	        		
	        		em.persist(model);}
	        	}
	        }
	public void batchUpdate(JSONArray jarray2,JSONArray jarray7,JSONArray jarray9) throws ParseException{
		
	     if(jarray9.size()>0)
	        {
	        	for (int i=0;i<jarray9.size();i++)
	        	{   
	        		DataSetColumn model = em.find(DataSetColumn.class,Long.parseLong( jarray9.get(i).toString()));
	        		model.setColNameC(jarray2.get(i).toString());
	        		model.setNotes(jarray7.get(i).toString());
	        		em.merge(model);}
	        	}
	        }
 

	
    //删除
    public void removeAll(JSONArray jarray) {
      if(jarray.size()>0)
	     {
	   for (int i=0;i<jarray.size();i++)
   {
		     Long id=Long.parseLong(jarray.get(i).toString());
		     remove(id);
		     DataSet dataSet = em.find(DataSet.class, id);
	   if (dataSet != null) {
	       em.remove(dataSet);
	    }
       }    	
    }
}
	// 删除
	public void remove(Long rowId) {
		List<DataSetColumn> list = query(rowId);
		if (!list.isEmpty()) {

			for (int i = 0; i < list.size(); i++) {
				DataSetColumn dataSetColumn = list.get(i);
				if (dataSetColumn != null) {
					em.remove(dataSetColumn);
				}
			}
		}
	}
    
}
