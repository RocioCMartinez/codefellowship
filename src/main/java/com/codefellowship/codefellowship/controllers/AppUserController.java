package com.codefellowship.codefellowship.controllers;

import com.codefellowship.codefellowship.models.AppUser;
import com.codefellowship.codefellowship.models.Posts;
import com.codefellowship.codefellowship.repos.AppUserRepository;
import com.codefellowship.codefellowship.repos.PostRepository;
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
import java.util.List;

@Controller
public class AppUserController {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }



    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView postSignUp(String username, String firstName, String lastName, String password, LocalDate dateOfBirth, String bio){
        AppUser user = new AppUser(username, firstName, lastName, dateOfBirth, bio);
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
        m.addAttribute("userName", idAppUser.getUsername());
        m.addAttribute("firstName", idAppUser.getFirstName());
        m.addAttribute("lastName", idAppUser.getLastName());
        m.addAttribute("appUserId", idAppUser.getId());
        m.addAttribute("dateOfBirth", idAppUser.getDateOfBirth());
        m.addAttribute("bio", idAppUser.getBio());

        m.addAttribute("currentDate", LocalDateTime.now());

        m.addAttribute("usersIFollow", idAppUser.getUsersIFollow());
        m.addAttribute("userFollowingMe", idAppUser.getUsersWhoFollowMe());

        List<Posts> posts = idAppUser.getPosts();
        m.addAttribute("posts", posts);

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

    @GetMapping("/myprofile")
    public String getProfile(Model m , Principal p) {
        if (p != null){
            String username = p.getName();
            AppUser homeUser = appUserRepository.findByUsername(username);
            m.addAttribute("homeUser", homeUser);
            m.addAttribute("username", username);

            List<Posts> posts = homeUser.getPosts();
            m.addAttribute("posts", posts);
        }
        return "user-profile.html";
    }

    @PostMapping("/create-post")
    public RedirectView createPost(Principal p, String body){
        if(p != null){
            String username = p.getName();
            AppUser homeUser = appUserRepository.findByUsername(username);

            Posts newPost = new Posts(body, homeUser);
            newPost.setCreatedAt(LocalDate.now());
            postRepository.save(newPost);

            Long userId = appUserRepository.findByUsername(p.getName()).getId();
            return new RedirectView("/user/" + userId);
        }
        return new RedirectView("/");
    }

    @PutMapping("/follow-user/{id}")
    public RedirectView followUser(Principal p, @PathVariable Long id){
        //Followed user
        AppUser userToFollow = appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find user id " + id));
        //Current logged in user
        AppUser browsingUser = appUserRepository.findByUsername(p.getName());

        //check
        if(browsingUser.getUsername().equals((userToFollow.getUsername()))){
            throw new IllegalArgumentException("Following yourself is not permitted");
        }

        //access followers from browsingUser and update with new userToFollow
        browsingUser.getUsersIFollow().add(userToFollow);

        //save info on the database

        appUserRepository.save(browsingUser);

        return new RedirectView("/user/" + id);
    }

    @GetMapping("/allusers")
    public String getAllUsers(Model m, Principal p){
        if(p != null){
            String username = p.getName();
            AppUser appUser = appUserRepository.findByUsername(username);


            m.addAttribute("username", username);
        }

        List<AppUser> userCollection = appUserRepository.findAll();
       m.addAttribute("userCollection", userCollection);
        

        return "/user-index.html";
    }
}
