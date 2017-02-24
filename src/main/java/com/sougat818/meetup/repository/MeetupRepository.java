package com.sougat818.meetup.repository;

import com.sougat818.meetup.domain.Meetup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Meetup entity.
 */
@SuppressWarnings("unused")
public interface MeetupRepository extends JpaRepository<Meetup,Long> {

    Meetup findOneByMeetupId(String id);
}
