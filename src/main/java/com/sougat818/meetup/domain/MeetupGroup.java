package com.sougat818.meetup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MeetupGroup.
 */
@Entity
@Table(name = "meetup_group")
public class MeetupGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_url")
    private String groupURL;

    @Column(name = "blocked")
    private Boolean blocked;

    @OneToMany(mappedBy = "meetupGroup")
    @JsonIgnore
    private Set<Meetup> meetups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public MeetupGroup groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public MeetupGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupURL() {
        return groupURL;
    }

    public MeetupGroup groupURL(String groupURL) {
        this.groupURL = groupURL;
        return this;
    }

    public void setGroupURL(String groupURL) {
        this.groupURL = groupURL;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public MeetupGroup blocked(Boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Set<Meetup> getMeetups() {
        return meetups;
    }

    public MeetupGroup meetups(Set<Meetup> meetups) {
        this.meetups = meetups;
        return this;
    }

    public MeetupGroup addMeetup(Meetup meetup) {
        this.meetups.add(meetup);
        meetup.setMeetupGroup(this);
        return this;
    }

    public MeetupGroup removeMeetup(Meetup meetup) {
        this.meetups.remove(meetup);
        meetup.setMeetupGroup(null);
        return this;
    }

    public void setMeetups(Set<Meetup> meetups) {
        this.meetups = meetups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeetupGroup meetupGroup = (MeetupGroup) o;
        if (meetupGroup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, meetupGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MeetupGroup{" +
            "id=" + id +
            ", groupId='" + groupId + "'" +
            ", groupName='" + groupName + "'" +
            ", groupURL='" + groupURL + "'" +
            ", blocked='" + blocked + "'" +
            '}';
    }
}
