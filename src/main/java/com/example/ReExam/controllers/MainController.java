package com.example.ReExam.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ReExam.models.Room;
import com.example.ReExam.models.User;
import com.example.ReExam.repo.RoomRepo;
import com.example.ReExam.service.RoomService;
import com.example.ReExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/")
    public String index() {
        return "redirect:/rooms";
    }

    @GetMapping("/rooms")
    public String main(Model model) {
//        текущий пользователь
        User user = userService.getCurrentUser();
        String username = user.getUsername();
        model.addAttribute("user",username);

        List<Room> closeRooms = user.getRooms().stream().filter(Room::getPrivate).collect(Collectors.toList());
        List<Room> openRooms = ((List<Room>) roomRepository.findAll()).stream().filter(rt -> !rt.getPrivate()).collect(
                Collectors.toList());

        model.addAttribute("openRooms", openRooms);
        model.addAttribute("closeRooms", closeRooms);

        return "main";
    }

    @PostMapping("/edit_nickname")
    public String edit_nickname(@RequestParam String name) {
        userService.updateUsername(name);
        return "redirect:/rooms";
    }

    @GetMapping("restricted")
    public String restricted() {
        return "";
    }


    @GetMapping("/create_room/send_message")
    public String createRoom(Model model) {
        return "addRoom";
    }

    @PostMapping("/addRoom")
    public String createPostRoom(@RequestParam String title) {
        Room room = new Room(title, userService.getCurrentUser(), new ArrayList<>());

        User user = userService.getCurrentUser();
        roomService.addUser(room, user);

        return "redirect:/rooms";
    }

    @GetMapping("/addRoom")
    public String createRoom() {
        return "addRoom";
    }
}
