package com.sougat818.meetup.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Meetup.
 */
@Entity
@Table(name = "meetup")
public class Meetup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "meetup_id")
    private String meetupId;

    @Column(name = "meetup_name")
    private String meetupName;

    @Column(name = "meetup_url")
    private String meetupURL;

    @Column(name = "meetup_description")
    private String meetupDescription;

    @Column(name = "meetup_going_status")
    private String meetupGoingStatus;

    @ManyToOne
    private MeetupGroup meetupGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeetupId() {
        return meetupId;
    }

    public Meetup meetupId(String meetupId) {
        this.meetupId = meetupId;
        return this;
    }

    public void setMeetupId(String meetupId) {
        this.meetupId = meetupId;
    }

    public String getMeetupName() {
        return meetupName;
    }

    public Meetup meetupName(String meetupName) {
        this.meetupName = meetupName;
        return this;
    }

    public void setMeetupName(String meetupName) {
        this.meetupName = meetupName;
    }

    public String getMeetupURL() {
        return meetupURL;
    }

    public Meetup meetupURL(String meetupURL) {
        this.meetupURL = meetupURL;
        return this;
    }

    public void setMeetupURL(String meetupURL) {
        this.meetupURL = meetupURL;
    }

    public String getMeetupDescription() {
        return meetupDescription;
    }

    public Meetup meetupDescription(String meetupDescription) {
        this.meetupDescription = meetupDescription;
        return this;
    }

    public void setMeetupDescription(String meetupDescription) {
        this.meetupDescription = meetupDescription;
    }

    public String getMeetupGoingStatus() {
        return meetupGoingStatus;
    }

    public Meetup meetupGoingStatus(String meetupGoingStatus) {
        this.meetupGoingStatus = meetupGoingStatus;
        return this;
    }

    public void setMeetupGoingStatus(String meetupGoingStatus) {
        this.meetupGoingStatus = meetupGoingStatus;
    }

    public MeetupGroup getMeetupGroup() {
        return meetupGroup;
    }

    public Meetup meetupGroup(MeetupGroup meetupGroup) {
        this.meetupGroup = meetupGroup;
        return this;
    }

    public void setMeetupGroup(MeetupGroup meetupGroup) {
        this.meetupGroup = meetupGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meetup meetup = (Meetup) o;
        if (meetup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, meetup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Meetup{" +
            "id=" + id +
            ", meetupId='" + meetupId + "'" +
            ", meetupName='" + meetupName + "'" +
            ", meetupURL='" + meetupURL + "'" +
            ", meetupDescription='" + meetupDescription + "'" +
            ", meetupGoingStatus='" + meetupGoingStatus + "'" +
            '}';
    }
}
