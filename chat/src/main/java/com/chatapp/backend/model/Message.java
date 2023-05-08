package com.chatapp.backend.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long message_id;
    @NotNull
    private String text;
    @NotNull
    private long date;
    @ManyToOne
    private User from;
    @ManyToOne
    private Channel to;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Channel getTo() {
        return to;
    }

    public void setTo(Channel to) {
        this.to = to;
    }

    public String toString() {
        return "{ \"text\" : \"" + text + "\" , \"date\" : \"" + date
                + "\" , \"id\" : \"" + message_id + "\" , \"to\" : \"" + to.getId() + "\" , \"from\" : \"" + from.getUsername() + "\" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return message_id == message.message_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_id);
    }

    public long getId() {
        return message_id;
    }
}
