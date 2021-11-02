package com.example.ReExam.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.example.ReExam.models.Message;
import com.example.ReExam.models.Room;
import com.example.ReExam.models.User;
import com.example.ReExam.repo.MessageRepo;
import com.example.ReExam.repo.RoomRepo;
import com.example.ReExam.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepo roomRepository;

    @Autowired
    private UserDetailsRepo userDetailsRepository;

    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private UserService userService;

    public Room getRoomById(String roomName) {
        return roomRepository.findById(Long.valueOf(roomName)).orElseThrow(IllegalStateException::new);
    }

    public User getRoomAdmin(Room room) {
        return room.getUserAdmin();
    }

    public List<User> getUsers(Room room) {
        return room.getUsers();
    }

    public void addUser(Room room, User user) {
        room.getUsers().add(user);
        user.getRooms().add(room);

        roomRepository.save(room);
        userDetailsRepository.save(user);
    }

    public void changeRoomName(String roomId, String name) {
        Room room = getRoomById(roomId);
        room.setTitle(name);
        roomRepository.save(room);
    }

    public void addMessage(Room currentRoom, User user, String text) {
        Date date = new Date();
        Message message = new Message(text, user, currentRoom, date);
        messageRepository.save(message);
    }

    public boolean isActionByMemberOfChat(Room currentRoom) {
        return currentRoom.getUsers().stream().map(User::getId).collect(Collectors.toList())
                .contains(userService.getCurrentUser().getId());
    }
}
