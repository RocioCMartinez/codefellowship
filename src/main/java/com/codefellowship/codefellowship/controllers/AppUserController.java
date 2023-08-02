package com.codefellowship.codefellowship.controllers;

import com.codefellowship.codefellowship.models.AppUser;
import com.codefellowship.codefellowship.models.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public RedirectView postSignUp(String username, String password, LocalDate dateOfBirth, String bio){
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setDateOfBirth(dateOfBirth);
        user.setBio(bio);


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

//        throw new ResourceNotFoundException("404 Error");

        return "index.html";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{
        ResourceNotFoundException(String message){
            super(message);
        }
    }

    @GetMapping("/test")
    public String getTestPage(Principal p, Model m){
        if(p != null){
            String username = p.getName();
            AppUser appUser = appUserRepository.findByUsername(username);

            m.addAttribute("username", username);
        }
        return "test.html";
    }

    @GetMapping("/user/{id}")
    public String getUserInfo(Model m, Principal p, @PathVariable Long id){
        if(p != null){
            String username = p.getName();
            AppUser appUser = appUserRepository.findByUsername(username);

            m.addAttribute("username", username);
        }

        AppUser idAppUser = appUserRepository.findById(id).orElseThrow();
        m.addAttribute("username", idAppUser.getUsername());
        m.addAttribute("appUserId", idAppUser.getId());
        m.addAttribute("dateOfBirth", idAppUser.getDateOfBirth());
        m.addAttribute("bio", idAppUser.getBio());

        m.addAttribute("currentDate", LocalDateTime.now());

        return "/user-info.html";
    }

    @PutMapping("/user/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String username){
        if(p != null && (p.getName().equals(username))){
            AppUser appUser = appUserRepository.findById(id).orElseThrow();

            appUser.setUsername(username);
            appUserRepository.save(appUser);
        }
        return new RedirectView("/user/" + id);
    }
}
