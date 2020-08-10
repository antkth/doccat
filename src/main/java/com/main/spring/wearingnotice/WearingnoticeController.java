package com.main.spring.wearingnotice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WearingnoticeController {
	@Autowired
	private WearingnoticeService wearingnoticeService;
	private ModelAndView mav = new ModelAndView();
	@RequestMapping(value = "/insertwearing.wea", method = RequestMethod.GET)
	public ModelAndView insertwearing(@ModelAttribute WearingnoticeVO wearingnoticeVO,
								HttpServletRequest request){
		String id = (String)request.getSession().getAttribute("id");
		wearingnoticeVO.setId(id);
		wearingnoticeService.insertwearing(wearingnoticeVO);
		request.getSession().setAttribute("msg","������ �Ϸ�Ǿ����ϴ� \\n �̸��Ϸ� ���۵˴ϴ�");
		mav.setViewName("redirect:/productInfo.pro?num="+wearingnoticeVO.getNum());
		return mav;
	}
	
}
