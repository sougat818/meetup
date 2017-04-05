package com.sougat818.meetup.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A HiddenMeetup.
 */
@Entity
@Table(name = "hidden_meetup")
public class HiddenMeetup implements Serializable {

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

    @Column(name = "meetup_going_status")
    private String meetupGoingStatus;

    @Column(name = "date")
    private ZonedDateTime date;

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

    public HiddenMeetup meetupId(String meetupId) {
        this.meetupId = meetupId;
        return this;
    }

    public void setMeetupId(String meetupId) {
        this.meetupId = meetupId;
    }

    public String getMeetupName() {
        return meetupName;
    }

    public HiddenMeetup meetupName(String meetupName) {
        this.meetupName = meetupName;
        return this;
    }

    public void setMeetupName(String meetupName) {
        this.meetupName = meetupName;
    }

    public String getMeetupURL() {
        return meetupURL;
    }

    public HiddenMeetup meetupURL(String meetupURL) {
        this.meetupURL = meetupURL;
        return this;
    }

    public void setMeetupURL(String meetupURL) {
        this.meetupURL = meetupURL;
    }

    public String getMeetupGoingStatus() {
        return meetupGoingStatus;
    }

    public HiddenMeetup meetupGoingStatus(String meetupGoingStatus) {
        this.meetupGoingStatus = meetupGoingStatus;
        return this;
    }

    public void setMeetupGoingStatus(String meetupGoingStatus) {
        this.meetupGoingStatus = meetupGoingStatus;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HiddenMeetup date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public MeetupGroup getMeetupGroup() {
        return meetupGroup;
    }

    public HiddenMeetup meetupGroup(MeetupGroup meetupGroup) {
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
        HiddenMeetup hiddenMeetup = (HiddenMeetup) o;
        if (hiddenMeetup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hiddenMeetup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HiddenMeetup{" +
            "id=" + id +
            ", meetupId='" + meetupId + "'" +
            ", meetupName='" + meetupName + "'" +
            ", meetupURL='" + meetupURL + "'" +
            ", meetupGoingStatus='" + meetupGoingStatus + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
