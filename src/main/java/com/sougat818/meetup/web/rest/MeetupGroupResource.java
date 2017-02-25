package com.sougat818.meetup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sougat818.meetup.domain.MeetupGroup;
import com.sougat818.meetup.service.MeetupGroupService;
import com.sougat818.meetup.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MeetupGroup.
 */
@RestController
@RequestMapping("/api")
public class MeetupGroupResource {

    private final Logger log = LoggerFactory.getLogger(MeetupGroupResource.class);

    private static final String ENTITY_NAME = "meetupGroup";
        
    private final MeetupGroupService meetupGroupService;

    public MeetupGroupResource(MeetupGroupService meetupGroupService) {
        this.meetupGroupService = meetupGroupService;
    }

    /**
     * POST  /meetup-groups : Create a new meetupGroup.
     *
     * @param meetupGroup the meetupGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meetupGroup, or with status 400 (Bad Request) if the meetupGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meetup-groups")
    @Timed
    public ResponseEntity<MeetupGroup> createMeetupGroup(@RequestBody MeetupGroup meetupGroup) throws URISyntaxException {
        log.debug("REST request to save MeetupGroup : {}", meetupGroup);
        if (meetupGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new meetupGroup cannot already have an ID")).body(null);
        }
        MeetupGroup result = meetupGroupService.save(meetupGroup);
        return ResponseEntity.created(new URI("/api/meetup-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetup-groups : Updates an existing meetupGroup.
     *
     * @param meetupGroup the meetupGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meetupGroup,
     * or with status 400 (Bad Request) if the meetupGroup is not valid,
     * or with status 500 (Internal Server Error) if the meetupGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meetup-groups")
    @Timed
    public ResponseEntity<MeetupGroup> updateMeetupGroup(@RequestBody MeetupGroup meetupGroup) throws URISyntaxException {
        log.debug("REST request to update MeetupGroup : {}", meetupGroup);
        if (meetupGroup.getId() == null) {
            return createMeetupGroup(meetupGroup);
        }
        MeetupGroup result = meetupGroupService.save(meetupGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meetupGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetup-groups : get all the meetupGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of meetupGroups in body
     */
    @GetMapping("/meetup-groups")
    @Timed
    public List<MeetupGroup> getAllMeetupGroups() {
        log.debug("REST request to get all MeetupGroups");
        return meetupGroupService.findAll();
    }

    /**
     * GET  /meetup-groups/:id : get the "id" meetupGroup.
     *
     * @param id the id of the meetupGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meetupGroup, or with status 404 (Not Found)
     */
    @GetMapping("/meetup-groups/{id}")
    @Timed
    public ResponseEntity<MeetupGroup> getMeetupGroup(@PathVariable Long id) {
        log.debug("REST request to get MeetupGroup : {}", id);
        MeetupGroup meetupGroup = meetupGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meetupGroup));
    }

    /**
     * DELETE  /meetup-groups/:id : delete the "id" meetupGroup.
     *
     * @param id the id of the meetupGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meetup-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeetupGroup(@PathVariable Long id) {
        log.debug("REST request to delete MeetupGroup : {}", id);
        meetupGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
