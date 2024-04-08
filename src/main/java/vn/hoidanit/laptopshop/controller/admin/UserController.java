package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

import java.util.*;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @RequestMapping("/")
    public String getHomePage(Model model) {
        // String test = this.userService.handleHello();
        List<User> arrUsers = this.userService.getAllUsersByEmail("linhnguyenthuy612@gmail.com");
        System.out.println(arrUsers);

        model.addAttribute("Linh", "test");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("user1", users);
        return "admin/user/show";
    }


    @RequestMapping("/admin/user/{id}")
    public String getUserDetail(Model model, @PathVariable long id) {
        User user = this.userService.getUserByID(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(Model model, 
                                @ModelAttribute("newUser") @Valid User user, 
                                BindingResult newUserBindingResult,
                                @RequestParam("anhFile") MultipartFile file){
       
                        
        // List<FieldError> errors = newUserBindingResult.getFieldErrors();
        // for (FieldError error : errors ) {
        //     System.out.println (error.getField() + " - " + error.getDefaultMessage());
        //     error.getDefaultMessage()
        //     }              
        
        // Validate
        if(newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        //
        String avatar= this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setAvatar(avatar);
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName(user.getRole().getName()));
        this.userService.handleSaveUser(user);

        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}") //GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserByID(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    // @PostMapping("/admin/user/update")
    @RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
    public String PostUpdateUser(Model model,  @ModelAttribute("newUser") User x) {
        User currentUser = this.userService.getUserByID(x.getId());
        if(currentUser != null) {
            currentUser.setFullName(x.getFullName());
            currentUser.setAddress(x.getAddress());
            currentUser.setPhone(x.getPhone());
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}") //GET
    public String DeleteUserPage(Model model, @PathVariable long id) {
        // User currentUser = this.userService.getUserByID(id);
        
        User user = new User();
        user.setId(id);
        model.addAttribute("newUser", user);
        model.addAttribute("id", id);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete") //GET
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User x) {
        this.userService.deleteUserByID(x.getId());
        return "redirect:/admin/user";
    }



}


