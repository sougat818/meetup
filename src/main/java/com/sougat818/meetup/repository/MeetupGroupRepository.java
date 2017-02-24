package com.sougat818.meetup.repository;

import com.sougat818.meetup.domain.MeetupGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MeetupGroup entity.
 */
@SuppressWarnings("unused")
public interface MeetupGroupRepository extends JpaRepository<MeetupGroup,Long> {

    MeetupGroup findOneByGroupId(String s);
}
