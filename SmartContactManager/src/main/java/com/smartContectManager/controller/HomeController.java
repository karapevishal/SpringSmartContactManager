package com.smartContectManager.controller;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContectManager.Entity.User;
import com.smartContectManager.dao.UserRepository;
import com.smartContectManager.helper.Message;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	@Autowired
//	private UserRepository repository;
//	
//	@GetMapping("/test")
//	@ResponseBody
//	private String home() {
//		
//		User user=new User();
//		user.setName("vishal karape");
//		user.setEmail("karapevishal@gmail.com");
//		
//		Contact contact=new Contact();
//		user.getContact().add(contact);
//		
//		repository.save(user);
//		
//		return "working";
//	}
//	
	@Autowired
	private UserRepository repository;
	
	
	
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title","home-smart contact manager");
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","about-smart contact manager");
		
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model m) {
		m.addAttribute("title","Register-smart contact manager");
		m.addAttribute("user",new User());
		
		return "signup";
	}
	
	@RequestMapping("/signin")
	public String customeLogIn(Model m) {
		
		m.addAttribute("title","Login-smart contact manager");
		
		return "login";
	}
	
	//this handler for registering user
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,@RequestParam(value="aggrement",defaultValue = "false") boolean aggrement,Model m,HttpSession session) {
		
		try {
			if(!aggrement) {
				System.out.println("you have not aggred terms and condition");
				throw new Exception("You Have not aggreed Terms and Condtions");
			}
			if(result1.hasErrors()) {
				System.out.println("error"+result1.toString());
				m.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			System.out.println("Aggrement:"+aggrement);
			System.out.println("USER"+user);
			User result = this.repository.save(user);
			m.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
			return "signup";
		} catch (Exception e) {
		
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message", new Message("Somthing went Wrong!!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
	}
	
	
	

}
