package com.example.ReExam.repo;

import com.example.ReExam.models.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface MessageChatRepo extends CrudRepository<ChatMessage, Long> {
}
