package com.codefellowship.codefellowship.controllers;

import com.codefellowship.codefellowship.models.AppUser;
import com.codefellowship.codefellowship.models.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class AppUserController {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }



    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView postSignUp(String username, String password){
        AppUser user = new AppUser();
        user.setUsername(username);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        appUserRepository.save(user);

        authWithHttpServletRequest(username, password);

        return new RedirectView("/");
    }


    public void authWithHttpServletRequest(String username, String password){
        try {
            request.login(username, password);
        } catch(ServletException e) {
            System.out.println("Error logging in");
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public String getIndexPage(Model model, Principal p){
        System.out.println("Principal " + p);

        if (p != null){
            String username = p.getName();
//            AppUser user = appUserRepository.findByUsername(username);

            model.addAttribute("username", username);
        }
        return "index.html";
    }
}
