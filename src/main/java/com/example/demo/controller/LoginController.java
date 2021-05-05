package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/userLogin", method = RequestMethod.GET)
	public String userLogin(HttpServletRequest request, @ModelAttribute User user, Model model) {

		String viewPage = "userLogin", message = "";

		model.addAttribute("user", user);
		model.addAttribute("message", message);

		return viewPage;
	}

	// login user
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public String loginUser(HttpServletRequest request, @ModelAttribute User user, Model model) {

		String viewPage = "", message = "";

		String email = user.getEmail();
		String password = user.getPassword();

		HttpSession session = request.getSession(true);

		List<User> userList = new ArrayList<User>();
		userList = userService.getUserList();
		int userIndex = -1;
		Boolean found = false;

		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getEmail().equals(email)) {
				found = true;
				if (userList.get(i).getPassword().equals(password)) {
					session.setAttribute("email", email);
					userIndex = i;
					viewPage = "home";
				} else {
					message = "Incorrect password";
					viewPage = "userLogin";
					user = new User();
				}
			}
		}
		if (!found) {
			viewPage = "userLogin";
			message = "User not found";
			user = new User();
		}

		if (userIndex > -1) {
			model.addAttribute("user", userList.get(userIndex));
		} else {
			model.addAttribute("user", user);
		}
		model.addAttribute("message", message);

		return viewPage;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String userLogout(HttpServletRequest request, @ModelAttribute User user, Model model) {

		String viewPage = "userLogin", message = "";
		HttpSession session = request.getSession(true);
		session.invalidate();

		model.addAttribute("user", user);
		model.addAttribute("message", message);

		return viewPage;
	}

	// get user registration page
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String userRegistration(HttpServletRequest request, @ModelAttribute User user, Model model) {

		String viewPage = "register", message = "Register as a new user";

		model.addAttribute("user", user);
		model.addAttribute("message", message);

		return viewPage;
	}

	// register new user
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(HttpServletRequest request, @ModelAttribute User user, Model model) {

		System.out.println("I am in register inside login controller");
		String viewPage;

		try {
			Integer.parseInt(user.getStno());

			// check if user already exists
			List<User> userList = new ArrayList<User>();
			userList = userService.getUserList();
			Boolean found = false;
			if (user.getEmail() != "") {
				for (int i = 0; i < userList.size(); i++) {
					if (userList.get(i).getEmail().equals(user.getEmail())) {
						found = true;
					}
				}

				if (!found) {
					// register user
					userService.registerUser(user);

					HttpSession session = request.getSession(true);
					session.setAttribute("email", user.getEmail());

					viewPage = "home";
					String registrationMessage = "Registration Successful!";

					model.addAttribute("user", user);
					model.addAttribute("registrationMessage", registrationMessage);
				} else {
					viewPage = "register";
					String errorMsg = "Email is already associated with an account";
					model.addAttribute("errorMsg", errorMsg);
				}
			} else {
				viewPage = "register";
				String errorMsg = "Email field is required";
				model.addAttribute("errorMsg", errorMsg);
			}
		} catch (NumberFormatException e) {
			viewPage = "register";
			String errorMsg = "Street Number field must be a number";
			model.addAttribute("errorMsg", errorMsg);
		}
		viewPage = "register";
		return viewPage;
	}

}