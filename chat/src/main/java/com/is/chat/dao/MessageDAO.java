package com.is.chat.dao;

import com.is.chat.model.Channel;
import com.is.chat.model.Message;
import com.is.chat.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageDAO extends CrudRepository<Message, Long> {
    @Query("select m from Message m where m.to = :to")
    List<Message> findMessagesByTo(@Param("to") Channel to);

    @Query("select m from Message m where m.to = ?1 and m.message_id > ?2")
    List<Message> findMessagesByToAndMessage_idGreaterThan(Channel to, Long begin);

    @Query(nativeQuery = true, value = "select * from Message ms join Memberships mm where" +
            " mm.user_id = ?1")
    List<Message> getMessages(User user);
}
