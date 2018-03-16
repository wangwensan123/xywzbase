package com.xywztech.bcrm.system.service;

import java.math.BigDecimal;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xywztech.bcrm.system.model.OcrmFSmPaper;
import com.xywztech.bcrm.system.model.OcrmFSmPapersQuestionRel;
import com.xywztech.bob.common.CommonService;
import com.xywztech.bob.common.JPABaseDAO;

/**
 *问卷管理
 * @author wangwan
 * @since 2012-12-07 
 */
@Service
public class PaperManageService extends CommonService {
   
	public PaperManageService(){
		JPABaseDAO<OcrmFSmPaper, Long>  baseDAO=new JPABaseDAO<OcrmFSmPaper, Long>(OcrmFSmPaper.class);  
		super.setBaseDAO(baseDAO);
	}
	/**
	 * 新增问卷
	 * @param paperName //问卷名称
	 * @param optionType//答题人类型
	 * @param remark//备注
	 * @param available//是否可用
	 * @param creator//创建人
	 * @param creator_org//创建机构
	 */
	public  void save(String paperName, String optionType,String remark, String available,String creator,String creator_org) {
		OcrmFSmPaper ws = new OcrmFSmPaper();
		ws.setAvailable(available);
		ws.setCreateDate(new Date(System.currentTimeMillis()));
		ws.setCreateOrg(creator_org);
		ws.setCreator(creator);
		ws.setOptionType(optionType);
		ws.setPaperName(paperName);
		ws.setRemark(remark);	
		em.persist(ws);
	}
	/**
	 * 新增试题
	 * @param jarray//新增试题的信息数组
	 */
	public void saveQ( JSONArray jarray){
		if (jarray.size() > 0){
			for (int i = 0; i < jarray.size(); ++i){
				JSONObject wa = (JSONObject)jarray.get(i);
				OcrmFSmPapersQuestionRel ws = new OcrmFSmPapersQuestionRel();
				ws.setPaperId(new BigDecimal((String)wa.get("paper_id")));
				ws.setQuestionId(new BigDecimal((String)wa.get("question_id")));
				ws.setQuestionOrder(new BigDecimal((String)wa.get("sort_id")));
				this.em.persist(ws);
			}
		}	
	}
	/**
	 * 删除试题
	 * @param jarray2//删除试题的信息数组
	 */
	public void removeQ(JSONArray jarray2){
		if (jarray2.size() > 0){
			for (int i = 0; i < jarray2.size(); ++i) {
				JSONObject wb = (JSONObject)jarray2.get(i);
				String t = (String)wb.get("id");
				OcrmFSmPapersQuestionRel ws2 = (OcrmFSmPapersQuestionRel)this.em.find(OcrmFSmPapersQuestionRel.class,Long.valueOf(t));
				if (ws2 != null){
					this.em.remove(ws2);
				}
			}
		}
	}
}
