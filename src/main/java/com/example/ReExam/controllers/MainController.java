package com.example.ReExam.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ReExam.models.Room;
import com.example.ReExam.models.User;
import com.example.ReExam.repo.RoomRepo;
import com.example.ReExam.repo.UserDetailsRepo;
import com.example.ReExam.service.RoomService;
import com.example.ReExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private RoomRepo roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsRepo userDetailsRepo;


    @GetMapping("/")
    public String index() {
        return "redirect:/rooms";
    }

    @PostMapping("/editNickname")
    public String editNickname(@RequestParam String name) {
//        User usr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        usr.setUsername(name);
//        userDetailsRepo.save(usr);
        userService.updateUsername(name);
        return "redirect:/rooms";
    }

    @GetMapping("/addRoom")
    public String createRoom() {
        return "addRoom";
    }

    @PostMapping("/addRoom")
    public String createPostRoom(@RequestParam String title) {
        User user = userService.getCurrentUser();

        Room room = new Room(title, user, new ArrayList<>());
        roomService.addUserAndRoom(room, user);

        return "redirect:/rooms";
    }

//    отображение информации (user, rooms)
    @GetMapping("/rooms")
    public String main(Model model) {
        //        текущий пользователь
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentUser();
        String username = user.getUsername();
        model.addAttribute("user", username);

        List<Room> closeRooms = user.getRooms().stream().filter(Room::getPrivate).collect(Collectors.toList());
        List<Room> openRooms = ((List<Room>) roomRepository.findAll()).stream().filter(rt -> !rt.getPrivate()).collect(
                Collectors.toList());

        model.addAttribute("openRooms", openRooms);
        model.addAttribute("closeRooms", closeRooms);

        return "main";
    }
}
