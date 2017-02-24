
package com.sougat818.meetup.pojo;

public class Group {

    private String joinMode;

    private Long created;

    private String name;

    private Float groupLon;

    private Integer id;

    private String urlname;

    private Float groupLat;

    private String who;

    public String getJoinMode() {
        return joinMode;
    }

    public void setJoinMode(String joinMode) {
        this.joinMode = joinMode;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getGroupLon() {
        return groupLon;
    }

    public void setGroupLon(Float groupLon) {
        this.groupLon = groupLon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public Float getGroupLat() {
        return groupLat;
    }

    public void setGroupLat(Float groupLat) {
        this.groupLat = groupLat;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

}
