package com.psychojunior.invoice.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@Autowired
	AuthenticationManager authenticationManager;

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {

		// Create the authentication token from the username and password
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		// Authenticate the user using the AuthenticationManager
		try {
			Authentication authentication = authenticationManager.authenticate(token);

			// If authentication is successful, set the authentication in the
			// SecurityContext
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Redirect the user to the home page or any page after successful login
			return "redirect:/"; // Change to your preferred page
		} catch (Exception e) {
			// If authentication fails, show an error message
			return "redirect:/login?error=true"; // Redirect back to login page with error message
		}
	}
}
