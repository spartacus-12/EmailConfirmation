package org.sesame.controller;

import java.util.Map;
import java.util.UUID;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.sesame.entities.AppUser;
import org.sesame.service.AccountService;
import org.sesame.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;



@RestController
public class UserController {
	@Autowired
private AccountService accountService ;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
@Autowired
private EmailService emailService ;
	/*@PostMapping("/register")
	public AppUser register(@RequestBody RegisterForm userForm) {
		if(!userForm.getPassword().equals(userForm.getRepassword()))
				throw new RuntimeException("You must confirm your password");
		AppUser user=accountService.findUserByUsername(userForm.getUsername());
		if(user!=null)throw new RuntimeException("This user already exists");
		AppUser appuser=new AppUser();
		appuser.setUsername(userForm.getUsername());
		appuser.setPassword(userForm.getPassword());
		appuser.setEmail(userForm.getEmail());
		accountService.saveUser(appuser);
		accountService.addRoleToUser(userForm.getUsername(), "ROLE_USER");
		return appuser ;
	*/
@RequestMapping(value = "/register", method = RequestMethod.POST)
public String processRegistrationForm(@RequestBody AppUser user, BindingResult bindingResult, HttpServletRequest request) {
			
	// Lookup user in database by e-mail
	//AppUser userExists = accountService.findByEmail(user.getEmail());
	
	//System.out.println(userExists);
	
	//if (userExists != null) {
		//System.setOut("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
	
//		bindingResult.reject("email");
	//}
		
//	if (bindingResult.hasErrors()) { 
	//	modelAndView.setViewName("register");		
	//} else { // new user so we create user and send confirmation e-mail
				
		// Disable user until they click on confirmation link in email
	    user.setEnabled(false);
	      
	    // Generate random 36-character string token for confirmation link
	    user.setConfirmationToken(UUID.randomUUID().toString());
	        
	    accountService.saveUser(user);
		accountService.addRoleToUser(user.getUsername(), "ROLE_USER");

			
		String appUrl = request.getScheme() + "://" + request.getServerName();
		
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
				+ appUrl + "/confirm?token=" + user.getConfirmationToken());
		registrationEmail.setFrom("noreply@domain.com");
		
		emailService.sendEmail(registrationEmail);
		
	
	
		
	return "ok";
}
@RequestMapping(value="/confirm", method = RequestMethod.GET)
public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {
		
	AppUser user = accountService.findByConfirmationToken(token);
		
	if (user == null) { // No token found in DB
		modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
	} else { // Token found
		modelAndView.addObject("confirmationToken", user.getConfirmationToken());
	}
		
	modelAndView.setViewName("confirm");
	return modelAndView;		
}
//Process confirmation link
	@RequestMapping(value="/confirm", method = RequestMethod.POST)
	public String confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {
				
		modelAndView.setViewName("confirm");
		
		Zxcvbn passwordCheck = new Zxcvbn();
		
		Strength strength = passwordCheck.measure(requestParams.get("password"));
		
		if (strength.getScore() < 3) {
			//modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
			bindingResult.reject("password");
			
			redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

			modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
			System.out.println(requestParams.get("token"));
			return "Registration";
		}
	
		// Find the user associated with the reset token
		AppUser user = accountService.findByConfirmationToken(requestParams.get("token"));

		// Set new password
		user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

		// Set user to enabled
		user.setEnabled(true);
		
		// Save user
		accountService.saveUser(user);
		
		modelAndView.addObject("successMessage", "Your password has been set!");
		return "Success";		
	}
}
