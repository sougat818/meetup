
package com.sougat818.meetup.pojo;

import java.util.List;

public class MeetupResult {

    private List<Result> results = null;

    private Meta meta;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
