package com.example.ReExam.service;


import com.example.ReExam.models.User;
import com.example.ReExam.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDetailsRepo userDetailsRepository;

    public User getCurrentUser() {
        User usr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = usr.getId();
        return userDetailsRepository.findById(userId).orElse(usr);
    }

    public void updateUsername(String newUsername) {
        User usr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usr.setUsername(newUsername);
        userDetailsRepository.save(usr);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User usr = userDetailsRepository.findByUsername(s);

        if (usr == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return usr;
    }
}
