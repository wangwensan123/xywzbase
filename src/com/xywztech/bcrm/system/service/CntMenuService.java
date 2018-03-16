package com.xywztech.bcrm.system.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.CntMenu;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;
/**
 * 菜单项管理
 * @author songxs
 * @since 2012-9-23
 * 
 */
@Service
public class CntMenuService extends CommonService{
	
	public CntMenuService(){
		JPABaseDAO<CntMenu, String>  baseDAO=new JPABaseDAO<CntMenu, String>(CntMenu.class);  
		super.setBaseDAO(baseDAO);
	}
	/**
	 * TODO 未添加逻辑系统 管理员授权信息
	 */
	@SuppressWarnings("unchecked")
	public void deleteMenu(String idStr){//菜单项管理删除方法同时删掉菜单表，授权表和常用功能表里面的记录
		JSONArray jarray = JSONArray.fromObject(idStr);
//		for (int i = 0; i < jarray.size(); ++i){
//			Long id = Long.parseLong(String.valueOf(jarray.get(i)));
//			String idSt = String.valueOf(id);
//			CntMenu cntMenu = (CntMenu)this.em.find(CntMenu.class, id);
//			if (cntMenu != null){
//				this.em.remove(cntMenu);}
//			String jql = "select c from AuthResAttrData c where c.resCode = ?1 ";
//			Query q = em.createQuery(jql.toString());
//			q.setParameter(1, idSt);
//			List<AuthResAttrData> list = q.getResultList();
//			Iterator<AuthResAttrData> itardadl = list.iterator();
//			while(itardadl.hasNext()){
//				em.remove(itardadl.next());
//			}
//			String sql = "select t from OcrmFWpModule t where t.moduleId = ?1";
//			Query s = em.createQuery(sql.toString());
//			s.setParameter(1,idSt);
//			List<OcrmFWpModule> listquery = s.getResultList();
//			Iterator<OcrmFWpModule> itter = listquery.iterator();
//			while(itter.hasNext()){
//				em.remove(itter.next());
//			}
//		}
		//   String idstr1="" ;
		   StringBuffer idstr1 = new StringBuffer();//用来存放要删除的菜单的主键ID
		   idstr1.append("") ;
		   for (int i = 0; i < jarray.size(); i++){//循环将前台穿过来的数组取出来，然后一个个用逗号隔开
			   idstr1.append(String.valueOf(jarray.get(i)));
			   if(i != jarray.size()-1){
				   idstr1.append(",");
			   }
		   }
		   
		   StringBuffer idstr2 = new StringBuffer();//用来存放模块ID
		   idstr2.append("\'");
		   for (int i = 0; i < jarray.size(); i++){//循环取出模块ID，然后将其转换为String类型，然后再一个个的用逗号隔开，放在一个对象里
			   idstr2.append(String.valueOf(jarray.get(i)));
			   idstr2.append("\'");
			   if(i != jarray.size()-1){
				   idstr2.append("\'");
				   idstr2.append(",");

			   }
		   }
		   
		String jql="delete from CntMenu c where c.id in ("+idstr1.toString()+")";
		Map<String,Object> values=new HashMap<String,Object>();
		this.batchUpdateByName(jql, values);
        String jql1 = "delete from AuthResAttrData t where t.resCode in ("+idstr2.toString()+")";
		this.batchUpdateByName(jql1, values);
		String jql2  = "delete from OcrmFWpModule m where m.moduleId in ("+idstr2.toString()+")";
		this.batchUpdateByName(jql2, values);		
		return ;
	}
}
