package com.sougat818.meetup.service;

import com.sougat818.meetup.domain.MeetupGroup;
import com.sougat818.meetup.repository.MeetupGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing MeetupGroup.
 */
@Service
@Transactional
public class MeetupGroupService {

    private final Logger log = LoggerFactory.getLogger(MeetupGroupService.class);
    
    private final MeetupGroupRepository meetupGroupRepository;

    public MeetupGroupService(MeetupGroupRepository meetupGroupRepository) {
        this.meetupGroupRepository = meetupGroupRepository;
    }

    /**
     * Save a meetupGroup.
     *
     * @param meetupGroup the entity to save
     * @return the persisted entity
     */
    public MeetupGroup save(MeetupGroup meetupGroup) {
        log.debug("Request to save MeetupGroup : {}", meetupGroup);
        MeetupGroup result = meetupGroupRepository.save(meetupGroup);
        return result;
    }

    /**
     *  Get all the meetupGroups.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MeetupGroup> findAll() {
        log.debug("Request to get all MeetupGroups");
        List<MeetupGroup> result = meetupGroupRepository.findAll();

        return result;
    }

    /**
     *  Get one meetupGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MeetupGroup findOne(Long id) {
        log.debug("Request to get MeetupGroup : {}", id);
        MeetupGroup meetupGroup = meetupGroupRepository.findOne(id);
        return meetupGroup;
    }

    /**
     *  Delete the  meetupGroup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeetupGroup : {}", id);
        meetupGroupRepository.delete(id);
    }
}
