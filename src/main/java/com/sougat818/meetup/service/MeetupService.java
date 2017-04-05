package com.sougat818.meetup.service;

import com.sougat818.meetup.domain.Meetup;
import com.sougat818.meetup.repository.MeetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Meetup.
 */
@Service
@Transactional
public class MeetupService {

    private final Logger log = LoggerFactory.getLogger(MeetupService.class);

    private final MeetupRepository meetupRepository;

    public MeetupService(MeetupRepository meetupRepository) {
        this.meetupRepository = meetupRepository;
    }

    /**
     * Save a meetup.
     *
     * @param meetup the entity to save
     * @return the persisted entity
     */
    public Meetup save(Meetup meetup) {
        log.debug("Request to save Meetup : {}", meetup);
        Meetup result = meetupRepository.save(meetup);
        return result;
    }

    /**
     *  Get all the meetups.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Meetup> findAll() {
        log.debug("Request to get all Meetups");
        List<Meetup> result = meetupRepository.findAllByOrderByDateAsc();

        return result;
    }

    /**
     *  Get one meetup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Meetup findOne(Long id) {
        log.debug("Request to get Meetup : {}", id);
        Meetup meetup = meetupRepository.findOne(id);
        return meetup;
    }

    /**
     *  Delete the  meetup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Meetup : {}", id);
        meetupRepository.delete(id);
    }
}
