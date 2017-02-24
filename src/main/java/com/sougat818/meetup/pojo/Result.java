
package com.sougat818.meetup.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

    private Integer utcOffset;

    private Venue venue;

    private Integer rsvpLimit;

    private Integer headcount;

    private Float distance;

    private String visibility;

    private Integer waitlistCount;

    private Long created;

    private Fee fee;

    private Integer maybeRsvpCount;

    private String description;

    private String howToFindUs;

    @JsonProperty("event_url")
    private String eventUrl;

    private Integer yesRsvpCount;

    private Integer duration;

    private String name;

    private String id;

    private Long time;

    private Long updated;

    private Group group;

    private String status;

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Integer getRsvpLimit() {
        return rsvpLimit;
    }

    public void setRsvpLimit(Integer rsvpLimit) {
        this.rsvpLimit = rsvpLimit;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Integer getWaitlistCount() {
        return waitlistCount;
    }

    public void setWaitlistCount(Integer waitlistCount) {
        this.waitlistCount = waitlistCount;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public Integer getMaybeRsvpCount() {
        return maybeRsvpCount;
    }

    public void setMaybeRsvpCount(Integer maybeRsvpCount) {
        this.maybeRsvpCount = maybeRsvpCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHowToFindUs() {
        return howToFindUs;
    }

    public void setHowToFindUs(String howToFindUs) {
        this.howToFindUs = howToFindUs;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public Integer getYesRsvpCount() {
        return yesRsvpCount;
    }

    public void setYesRsvpCount(Integer yesRsvpCount) {
        this.yesRsvpCount = yesRsvpCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
