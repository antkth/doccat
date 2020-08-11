package com.main.spring.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.crypto.spec.IvParameterSpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.spring.member.MemberVO;
import com.main.spring.wearingnotice.WearingnoticeVO;

class MyAuthentication extends Authenticator { //���̵� �н����� �����ޱ� �Լ�
	  PasswordAuthentication pa;
	  public MyAuthentication(){
	    pa=new PasswordAuthentication("gimt94566@gmail.com","66549tmig!");        
	  }
	  @Override
	  protected PasswordAuthentication getPasswordAuthentication() {
	    return pa;
	  }
	}
@Service
public class InventoryService {
	
	@Autowired
	InventoryDAO inventoryDAO;

	public int getAllProducts(String searchField, String search) {
		HashMap map = new HashMap();
		map.put("searchField", searchField);
		map.put("search", '%'+search+'%');		
		return inventoryDAO.getAllProducts(map);
	}

	public List getInvList(String searchField, String search, int pageFirst, int pageSize) {
		HashMap map = new HashMap();
		map.put("searchField", searchField);
		map.put("search", '%'+search+'%');
		map.put("pageFirst", pageFirst);
		map.put("pageSize", pageSize);
		return inventoryDAO.getInvList(map);
	}

	public List getNoSearch(int pageFirst, int pageSize) {
		HashMap map = new HashMap();
		map.put("pageFirst", pageFirst);
		map.put("pageSize", pageSize);
		return inventoryDAO.getNoSearch(map);
	}

	public void getUpdateInv(int num) {	
		inventoryDAO.getUpdateInv(num);
	}
	public void getUpdateW_date(int num) {
		inventoryDAO.getUpdateW_date(num);
	}
	public void getSubInv(int num) {
		inventoryDAO.getSubInv(num);
	}
	public int getInventory(int num) {
		return inventoryDAO.getInventory(num);
	}
	public void setZero(int num) {
		inventoryDAO.setZero(num);		
	}
	public void getWearingList(int num) {
		//1. num������ id���ؿ���
		List list = inventoryDAO.getId(num);
		//2. num������ name���ؿ���
		String name = inventoryDAO.getName(num); 
		//3. id�� email���ؿ���
		for(int i=0; i<list.size(); i++) {
			WearingnoticeVO wearingnoticeVO = (WearingnoticeVO)list.get(i);
			String email = inventoryDAO.getEmail(wearingnoticeVO.getId());
			wearingnoticeVO.setName(name);
			wearingnoticeVO.setEmail(email);
			SendEmail(wearingnoticeVO);
		}
		inventoryDAO.delWearing(num);
	}
	public void SendEmail(WearingnoticeVO vo){
		try {
			  Properties props = new Properties();
			props.put("mail.smtp.user", "gimt94566@gmail.com");
			props.put("mail.smtp.host", "smtp.googlemail.com");
			props.put("mail.smtp.port", "465");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			  Authenticator myauth=new MyAuthentication(); 
			  Session sess=Session.getInstance(props, myauth);
			  InternetAddress addr = new InternetAddress();
			  addr.setPersonal("gimt94566","UTF-8");
			  addr.setAddress("gimt94566@gmail.com");
			  Message msg = new MimeMessage(sess);
			  msg.setFrom(addr);         
			  msg.setSubject(MimeUtility.encodeText("�԰�˶�����", "utf-8","B"));
			  String content = vo.getId() +"��! ��û�Ͻ� " + vo.getName() + " ��ǰ�� �԰�Ǿ����ϴ�.";
			  msg.setContent(content, "text/html;charset=utf-8");
			  msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(vo.getEmail()));
			  Transport.send(msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
