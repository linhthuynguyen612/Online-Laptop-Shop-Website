package vn.hoidanit.laptopshop.service;


import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String handleHello() {
        return "Hello from UserService!";
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    public User handleSaveUser(User user) {
        User x = this.userRepository.save(user);
        System.out.println(x);
        return x;
    }

    public User getUserByID(long id){
        return this.userRepository.findById(id);
    }

    public void deleteUserByID(long id){
        this.userRepository.deleteById(id);
    }
}
