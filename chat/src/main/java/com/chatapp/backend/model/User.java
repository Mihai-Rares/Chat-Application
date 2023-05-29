package com.chatapp.backend.model;

import com.chatapp.backend.util.json.JSONSerializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Entity
public class User implements Comparable<User>, JSONSerializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public int user_id;
    @Column(unique = true, nullable = false)
    public String username;
    public String email;
    @JsonIgnore
    public String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Memberships",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "channel_id")
            }
    )
    @JsonIgnore
    private Set<Channel> memberOf;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String toJSON() {
        return "{ \"username\" : \"" + username + "\" , \"email\" : \"" + email + "\" }";
    }

    public Channel getGroup(String groupName) {
        Optional<Channel> optional = memberOf.stream().filter(c -> c.getName().equals(groupName)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addChannel(Channel channel) {
        memberOf.add(channel);
    }

    public Set<Channel> getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(Set<Channel> memberOf) {
        this.memberOf = memberOf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(User o) {
        long dif = user_id - o.user_id;
        if (dif == 0) {
            return 0;
        }
        if (dif < 0) {
            return -1;
        }
        return 1;
    }
}
