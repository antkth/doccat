package com.main.spring.cart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CartController {
	private ModelAndView mav = new ModelAndView();
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(value = "/addCart.car", method = RequestMethod.POST)
	public ModelAndView addCart(@ModelAttribute CartVO cartVO,
								HttpServletRequest request,
								HttpServletResponse response) {
		cartService.addCart(cartVO);
		mav.setViewName("redirect:/index.pro");
		return mav;
	}
	
	@RequestMapping(value = "/cartList.car", method=RequestMethod.GET)
	public ModelAndView cartList(@RequestParam String id,
								HttpServletRequest request, 
								HttpServletResponse response) throws Exception {
		int total =0;
		List cartList = cartService.getCartList(id);
		if(cartService.totalCheck(id)!=0) total = cartService.getTotalPrice(id);
		mav.addObject("cartList", cartList);
		mav.addObject("total",total);
		mav.setViewName("cartList");
		return mav;
	}
	
	
	
	
}