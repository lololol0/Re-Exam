package com.example.ReExam.controllers;

import java.util.List;

import com.example.ReExam.models.Room;
import com.example.ReExam.models.User;
import com.example.ReExam.models.MessageApi;
import com.example.ReExam.repo.MessageRepo;
import com.example.ReExam.repo.RoomRepo;
import com.example.ReExam.service.RoomService;
import com.example.ReExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomController {
    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomRepo roomRepository;

    @Autowired
    private RoomService roomService;

    @GetMapping("/enter_room/{room}")
    public String enterRoom(@PathVariable String room) {
        Room currentRoom = roomService.getRoomById(room);
        if (!currentRoom.getPrivate() || roomService.isActionByMemberOfChat(currentRoom)) {
            return "redirect:/room/{room}";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/room/{id}")
    public String getRoom(
            Model model,
            @PathVariable(value = "id") String id,
            @ModelAttribute("error") String error)
    {
        Room room = roomService.getRoomById(id);
        List<MessageApi> messagesByRoom = messageRepository.findAllMessagesByRoom(room);

        model.addAttribute("messages", messagesByRoom);
        model.addAttribute("roomId", id);
        model.addAttribute("admin", roomService.getRoomAdmin(room).getUsername());
        model.addAttribute("is_admin",
                roomService.getRoomAdmin(room).getId().equals(userService.getCurrentUser().getId()));
        model.addAttribute("users", roomService.getUsers(room));
        model.addAttribute("switched_type", room.getPrivate() ? "Публичная" : "Приватная");
        model.addAttribute("type", room.getPrivate() ? "Приватная" : "Публичная");
        model.addAttribute("room_name", room.getTitle());
        return "room";
    }

    @PostMapping("/{room}/send_message")
    public String addMessage(
            @RequestParam String text,
            @PathVariable String room)
    {
        User user = userService.getCurrentUser();
        Room currentRoom = roomService.getRoomById(room);
        if (currentRoom.getPrivate() && !roomService.isActionByMemberOfChat(currentRoom)) {

            return "redirect:/error";
        }
        roomService.addMessage(currentRoom, user, text);

        return "redirect:/room/{room}";
    }

    @PostMapping("/{id}/updateType")
    public String editType(
            @PathVariable(value = "id") String id)
    {
        Room room = roomService.getRoomById(id);
        room.setPrivate(!room.getPrivate());
        roomRepository.save(room);
        return "redirect:/room/{id}";
    }

    @PostMapping("/{id}/add_user")
    public String addUser(
            @PathVariable(value = "id") String id,
            @RequestParam String username)
    {
        try {
            User user = (User) userService.loadUserByUsername(username);
            Room room = roomService.getRoomById(id);
            roomService.addUser(room, user);
        } catch (Exception e) {
            return "redirect:/error";

        }
        return "redirect:/room/{id}";
    }

    @PostMapping("/{id}/updateNameRoom")
    public String changeName(
            @PathVariable(value = "id") String id,
            @RequestParam String title)
    {
        roomService.changeRoomName(id, title);
        return "redirect:/room/{id}";
    }
}
