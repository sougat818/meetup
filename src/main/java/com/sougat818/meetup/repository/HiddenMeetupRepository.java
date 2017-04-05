package com.sougat818.meetup.repository;

import com.sougat818.meetup.domain.HiddenMeetup;

import com.sougat818.meetup.domain.Meetup;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the HiddenMeetup entity.
 */
@SuppressWarnings("unused")
public interface HiddenMeetupRepository extends JpaRepository<HiddenMeetup,Long> {

    HiddenMeetup findOneByMeetupId(String id);

    List<HiddenMeetup> findAllByMeetupGoingStatusNot(String status);

    List<HiddenMeetup> findAllByDateBefore(ZonedDateTime date);

    List<HiddenMeetup> findAllByOrderByDateAsc();

}
