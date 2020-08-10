package com.main.spring.resale;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResaleService {
	
	@Autowired
	private ResaleDAO resaleDAO;
	
	public void insertResale(ResaleVO resaleVO) {
		resaleDAO.insertResale(resaleVO);
	}
	public void checkResale() {
//		  3. ��ڰ� ���ⱸ�� ������ Ȯ����(���� /���ó�¥)
		List checkResaleList = resaleDAO.checkResale();
//		  4. ���ⱸ�� ����Ʈ���� ���ⱸ�� ���� ��¥�� ������ ��û���� ������ ���Ǻ���(�� ��û�� id�� ����Ʈ�� �����ͼ� ���ݺ�)
//		  	for�� -> �� ���̵� �������� ����Ʈ ������
//		  	num / ���� ���ؼ� ���� ������
		for(int i=0; i < checkResaleList.size(); i++) {
			ResaleVO resaleVO = (ResaleVO)checkResaleList.get(i);
			int userPoint = resaleDAO.userPoint(resaleVO.getId());
			int productPrice = Math.round(resaleDAO.getPrice(resaleVO.getNum()) * resaleVO.getQty()/100*70/100)*100;
			
			Timestamp date = resaleVO.getRe_date();
			Date d = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			calendar.add(Calendar.DATE, resaleVO.getPeriod());
			int month = calendar.get(Calendar.MONTH)+1;
			String time = calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DATE)+" 00:00:00";
			Timestamp redate = Timestamp.valueOf(time);
			
			if(userPoint >= productPrice) {
				//1. ����� ����Ʈ�� ���
				HashMap map = new HashMap();
				map.put("total", productPrice);
				map.put("id", resaleVO.getId());
				resaleDAO.decrpoint(map);
				//2. ������ ������
				map.put("num", resaleVO.getNum());
				map.put("qty", resaleVO.getQty());
				map.put("price", productPrice);
				resaleDAO.re_addCart(map);
				
				int numcheck = resaleDAO.numcheck();
				//3. ���Ÿ�Ͽ� �ø�
				map.put("pur_num", numcheck);
				resaleDAO.re_intsertPurchase(map);
				//4. ī�� ����
				map.put("cart_num", numcheck);
				resaleDAO.deletecart(map);
			}
//		  	 	 �񱳸� �ؼ� ����(����Ʈ���) ���� �������ְ� ��¥ ���������ְ�
//		 		 5. ���ⱸ�� ���̺� ������Ʈ(�ֱ�� ���ó�¥+�ֱ�)
			HashMap map2 = new HashMap();
			map2.put("re_date", redate);
			map2.put("re_num", resaleVO.getRe_num());
			resaleDAO.updateResale(map2);
		}
	}
	public List resaleList() {
		return resaleDAO.resaleList();
	}
	public List myResaleList(String id) {
		return resaleDAO.myResaleList(id);
	}
	

}
