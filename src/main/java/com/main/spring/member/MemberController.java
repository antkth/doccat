package com.main.spring.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	private ModelAndView mav = new ModelAndView();
	@RequestMapping(value = "/join.mem", method = RequestMethod.GET)
	public ModelAndView join() {
		return mav;
	}
	@RequestMapping(value = "/idCheck.mem", method = RequestMethod.GET)
	public void idCheck(String id,
								HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String out = "";
		if(id=="") {
			out="<span></span><input type='text' id ='idc' name='idc' value='1'>";
		}else if(id.length()<4 || id.length()>12) {
			out = "<span style='color:red;'><i class='fa fa-remove'></i>�ּ� 4�ڸ� �ִ� 12�ڸ��� ���� �ּ���.</span><input type ='text' id='idc' value='1'>";
		}else if(id.contains("admin")) {
			out="<span style='color:red;'><i class='fa fa-remove'></i>admin�� ���Եɼ� �����ϴ�.</span><input type ='text' id='idc' value='1'>";
		}else {
			if(memberService.idCheck(id)==1) {
				out="<span style='color:red;'><i class='fa fa-remove'></i>�̹� �����ϴ� ���̵� �Դϴ� .</span><input type ='text' id='idc' value='1'>";
			}else {
				out="<span style='color:blue;'><i class='fa fa-check'></i></span><input type ='text' id='idc' value='0'>";
			}
		}
		response.getWriter().print(out);

	}
	@RequestMapping(value = "/addMember.mem", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute MemberVO memberVO,
									HttpServletRequest request,
									HttpServletResponse response) {
		memberService.addMember(memberVO);
		mav.setViewName("redirect:/index.pro"); 
		request.getSession().setAttribute("id",memberVO.getId());
		return mav;
	}
	@RequestMapping(value = "/login.mem" , method = RequestMethod.GET)
	public ModelAndView login() {
		mav.clear();
		return mav;
	}
	@RequestMapping(value = "/loginCheck.mem" , method = RequestMethod.POST)
	public ModelAndView loginMember(@ModelAttribute MemberVO memberVO,
									HttpServletRequest request,
									HttpServletResponse response){
		String msg = memberService.loginMember(memberVO);

		if(msg.equals("login")) {
			request.getSession().setAttribute("id",memberVO.getId());
			mav.setViewName("redirect:/index.tem");
		}else {
			mav.addObject("msg",msg);
			mav.setViewName("login");
		}
		return mav;
	}
	@RequestMapping(value = "/logout.mem", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request,
							   HttpServletResponse response) {
		String inforpage = request.getHeader("referer").substring(29);
		String nextPage = inforpage.substring(0,inforpage.indexOf("."));
		request.getSession().invalidate();
		mav.setViewName(nextPage);
		return mav;
	}
	@RequestMapping(value = "/memMod.mem", method = RequestMethod.GET)
	public ModelAndView memMod(HttpServletRequest request) {
		String id = (String)request.getSession().getAttribute("id");
		mav.addObject("meminfo", memberService.memMod(id));
		mav.setViewName("memMod");
		return mav;
	}
	@RequestMapping(value = "/updateMem.mem", method = RequestMethod.POST)
	public ModelAndView updateMem(@ModelAttribute MemberVO memberVO,
								  HttpServletRequest request) {
		memberService.updateMem(memberVO);
		mav.setViewName("redirect:/index.tem");
		return mav;
	}
	@RequestMapping(value = "/findId.mem" , method = RequestMethod.GET)
	public ModelAndView findId() {
		mav.clear();
		return mav;
	}
	@RequestMapping(value = "/findIdCheck.mem" , method = RequestMethod.POST)
	public ModelAndView findIdCheck(@ModelAttribute MemberVO memberVO) {
		memberVO = memberService.findId(memberVO);
		String msg="";
		if(memberVO.getId().equals("admin")){
			msg = "�߸��� ���� �Դϴ�.";
			mav.addObject("msg",msg);
			mav.setViewName("findId");
		}else {
			String idMaking = memberVO.getId();
			String id = idMaking.substring(0,idMaking.length()-2)+"**";
			memberVO.setId(id);
			mav.addObject("meminfo",memberVO);
			mav.setViewName("findpwd");
		}
		return mav;
	}
	@RequestMapping(value = "/findpwdCheck.mem" , method = RequestMethod.POST)
	public ModelAndView finpwdCheck(@ModelAttribute MemberVO memberVO)throws Exception{
		if(memberService.idCheck(memberVO.getId())==0) {
			mav.addObject("msg2","�߸��� ���̵� �Դϴ� . ");
			mav.addObject("meminfo",memberVO);
			mav.setViewName("findpwd");
		}else {
			memberVO=memberService.memMod(memberVO.getId());
			memberService.SendEmail(memberVO);
			mav.addObject("msg","���� ������ �Ϸ� �Ǿ����ϴ� .");
			mav.setViewName("login");
		}
		return mav;
	}
}
