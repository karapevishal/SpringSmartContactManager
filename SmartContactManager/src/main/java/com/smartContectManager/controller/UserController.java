package com.smartContectManager.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.smartContectManager.Entity.Contact;
import com.smartContectManager.Entity.User;
import com.smartContectManager.dao.ContactRepositiory;
import com.smartContectManager.dao.UserRepository;
import com.smartContectManager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository repository;
	@Autowired
	private ContactRepositiory contactRepositiory;
	
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model m,Principal principal) {
		
		String username = principal.getName();
		//get user using username
		User user = this.repository.getUserByUserName(username);
		m.addAttribute("user",user);
		System.out.println(user);
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model m,Principal principal) {
		
		m.addAttribute("title","User Dashboard");
		
		return "normal/user-dashboard";
	}
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title","Add Contact");
		Contact contact = new Contact();
		m.addAttribute("contact",contact);
		
		return "normal/add-contact-form";
	}
	
	@PostMapping("/process_contact")
	public String process_contact(Model m, @ModelAttribute Contact contact,Principal principal,@RequestParam("profileImage") MultipartFile file,HttpSession session) {
		
		try {
			
			System.out.println(file);
			String username = principal.getName();
			User user = this.repository.getUserByUserName(username);
			//processing and uploading file
			
			if(file.isEmpty()) {
				System.out.println("the file is emthy ");
				contact.setImage("contact.png");
				
			}else {
				//upload the file to the folder
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is uploaded");
				m.addAttribute("contact",new Contact());
				
			}
			
			user.getContact().add(contact);
			contact.setUser(user);
			this.repository.save(user);
			System.out.println(contact);
			//message success
			session.setAttribute("message", new Message("Contact Added Successfully !! add More", "success"));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message("Something is Went Wronge", "danger"));
			m.addAttribute("contact",contact);
		}
		
		
		System.out.println("added to database");
		return "normal/add-contact-form";
		
	}
	//per page=5[n]
	//current page=0[page]
	
	@GetMapping("show_contact/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m,HttpSession session,Principal principal) {
		
//		String username = principal.getName();
//		User user = this.repository.getUserByUserName(username);
//		List<Contact> list = user.getContact();
		
		m.addAttribute("title","Show Contacts");
		//send contact list
		String name = principal.getName();
		User user = this.repository.getUserByUserName(name);
		
		PageRequest pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepositiory.findContactByUser(user.getId(),pageable);
		
		m.addAttribute("contacts",contacts);
		
		m.addAttribute("currentPage",page);
		
		m.addAttribute("totalpages",contacts.getTotalPages());
		
		return "normal/show_contact";
	}
	
	
	//showing perticular contact details
	@RequestMapping("/{cId}/contact")
	public String showcontactdetails( @PathVariable("cId") Integer cId,Model m,Principal principal) {
		
		String name = principal.getName();
		User user = this.repository.getUserByUserName(name);
		
		Optional<Contact> id = this.contactRepositiory.findById(cId);
		Contact contact = id.get();
		
		if(user.getId()==contact.getUser().getId()) {
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getCname());
		}
		
		return "normal/contact_details";
	}
	
	@GetMapping("delete/{cId}")
	public String deletecontact(@PathVariable("cId") Integer cId,Model m,Principal principal,HttpSession session) {
		
		String name = principal.getName();
		User user = this.repository.getUserByUserName(name);
		
		Optional<Contact> contactOptional = this.contactRepositiory.findById(cId);
		Contact contact = contactOptional.get();
		
		
		
		session.setAttribute("message", new Message("Contact Deleted Succesfully", "success"));
		
		try {
			if(user.getId()==contact.getUser().getId()) {
				this.contactRepositiory.delete(contact);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("Something Went Wrong", "danger"));
		}
		
		
		return "redirect:/user/show_contact/0";
	}
	
	//open update form handler
	@PostMapping("/update_contact/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId,Model m,Principal principal) {
		
		Contact contact = this.contactRepositiory.findById(cId).get();
		m.addAttribute("contact",contact);
		
		return"normal/update_contact";
	}
	
//	update contact handler
	
	@PostMapping("/process_update_contact")
	public String updateHandler(@ModelAttribute Contact contact,Model m,@RequestParam("profileImage") MultipartFile file,HttpSession session,Principal prin) 
	{
		
		try {
			
			
			//old contact details
			Contact oldcontactDetails = this.contactRepositiory.findById(contact.getCid()).get();
			//is image selected 
			if(file.isEmpty()) {
				//file work 
				
				
				//delete old photo;
			 	
				
				//update new photo
				
			File save1 = new ClassPathResource("static/image").getFile();
			Path path = Paths.get(save1.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(file.getOriginalFilename());
				
			}else {
				
				//keep image as it is if no one has chnaged it
				contact.setImage(oldcontactDetails.getImage());
			
			}
			
			String name = prin.getName();
			User user = this.repository.getUserByUserName(name);
			contact.setUser(user);
			this.contactRepositiory.save(contact);
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		return "";
	}

}
