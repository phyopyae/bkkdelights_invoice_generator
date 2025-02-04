package com.psychojunior.invoice.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.psychojunior.invoice.template.model.UserForm;
import com.psychojunior.invoice.template.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new UserForm());
		return "user_register";
	}

	@PostMapping("/register")
	public String registerUser(UserForm userForm, Model model) {
		if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
			model.addAttribute("message", "Passwords do not match.");
			return "register"; // Return back to the registration page with an error message
		}

		// Encrypt the password and save the user to the database
		boolean isRegistered = userService.registerNewUser(userForm.getUsername(), userForm.getPassword(),
				userForm.getEmail());

		if (isRegistered) {
			model.addAttribute("message", "Registration successful. Please login.");
			return "login"; // Redirect to the login page after successful registration
		} else {
			model.addAttribute("message", "Username or email already exists.");
			return "register"; // Show error if the registration failed
		}
	}
}
