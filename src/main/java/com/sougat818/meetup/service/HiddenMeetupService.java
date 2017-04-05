package com.sougat818.meetup.service;

import com.sougat818.meetup.domain.HiddenMeetup;
import com.sougat818.meetup.repository.HiddenMeetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing HiddenMeetup.
 */
@Service
@Transactional
public class HiddenMeetupService {

    private final Logger log = LoggerFactory.getLogger(HiddenMeetupService.class);

    private final HiddenMeetupRepository hiddenMeetupRepository;

    public HiddenMeetupService(HiddenMeetupRepository hiddenMeetupRepository) {
        this.hiddenMeetupRepository = hiddenMeetupRepository;
    }

    /**
     * Save a hiddenMeetup.
     *
     * @param hiddenMeetup the entity to save
     * @return the persisted entity
     */
    public HiddenMeetup save(HiddenMeetup hiddenMeetup) {
        log.debug("Request to save HiddenMeetup : {}", hiddenMeetup);
        HiddenMeetup result = hiddenMeetupRepository.save(hiddenMeetup);
        return result;
    }

    /**
     *  Get all the hiddenMeetups.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HiddenMeetup> findAll() {
        log.debug("Request to get all HiddenMeetups");
        List<HiddenMeetup> result = hiddenMeetupRepository.findAllByOrderByDateAsc();

        return result;
    }

    /**
     *  Get one hiddenMeetup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public HiddenMeetup findOne(Long id) {
        log.debug("Request to get HiddenMeetup : {}", id);
        HiddenMeetup hiddenMeetup = hiddenMeetupRepository.findOne(id);
        return hiddenMeetup;
    }

    /**
     *  Delete the  hiddenMeetup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HiddenMeetup : {}", id);
        hiddenMeetupRepository.delete(id);
    }
}
