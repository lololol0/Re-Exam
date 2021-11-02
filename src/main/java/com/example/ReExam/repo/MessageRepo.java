package com.example.ReExam.repo;

import java.util.List;

import com.example.ReExam.models.Message;
import com.example.ReExam.models.Room;
import com.example.ReExam.models.MessageApi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepo extends CrudRepository<Message, Long> {
    @Query(value = "SELECT new com.example.ReExam.models.MessageApi(m)" +
            "FROM Message m where m.room=:room")
    List<MessageApi> findAllMessagesByRoom(@Param("room") Room room);
}
