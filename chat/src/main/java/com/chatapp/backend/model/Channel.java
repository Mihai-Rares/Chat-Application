package com.chatapp.backend.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Channel implements Comparable<Channel> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long channel_id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Memberships",
            joinColumns = {
                    @JoinColumn(name = "channel_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            }
    )
    private Set<User> members;

    @ManyToMany
    @JoinTable(name = "Admins",
            joinColumns = {
                    @JoinColumn(name = "channel_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            }
    )
    private Set<User> admins;

    public long getId() {
        return channel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public Set<User> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<User> admins) {
        this.admins = admins;
    }

    public boolean isAdmin(User user) {
        return admins.contains(user);
    }

    public boolean isMember(User user) {
        return members.contains(user);
    }

    public String toString() {
        return "{ \"id\" : \"" + channel_id + "\" , \"name\" : \"" + name + "\" }";
    }

    public void setId(long l) {
        channel_id = l;
    }

    @Override
    public int compareTo(Channel o) {
        long dif = channel_id - o.channel_id;
        if (dif == 0) {
            return 0;
        }
        if (dif < 0) {
            return -1;
        }
        return 1;
    }
}
