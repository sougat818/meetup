package com.sougat818.meetup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sougat818.meetup.domain.Meetup;
import com.sougat818.meetup.service.MeetupService;
import com.sougat818.meetup.web.rest.util.HeaderUtil;
import com.sougat818.meetup.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Meetup.
 */
@RestController
@RequestMapping("/api")
public class MeetupResource {

    private final Logger log = LoggerFactory.getLogger(MeetupResource.class);

    private static final String ENTITY_NAME = "meetup";
        
    private final MeetupService meetupService;

    public MeetupResource(MeetupService meetupService) {
        this.meetupService = meetupService;
    }

    /**
     * POST  /meetups : Create a new meetup.
     *
     * @param meetup the meetup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meetup, or with status 400 (Bad Request) if the meetup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meetups")
    @Timed
    public ResponseEntity<Meetup> createMeetup(@RequestBody Meetup meetup) throws URISyntaxException {
        log.debug("REST request to save Meetup : {}", meetup);
        if (meetup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new meetup cannot already have an ID")).body(null);
        }
        Meetup result = meetupService.save(meetup);
        return ResponseEntity.created(new URI("/api/meetups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetups : Updates an existing meetup.
     *
     * @param meetup the meetup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meetup,
     * or with status 400 (Bad Request) if the meetup is not valid,
     * or with status 500 (Internal Server Error) if the meetup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meetups")
    @Timed
    public ResponseEntity<Meetup> updateMeetup(@RequestBody Meetup meetup) throws URISyntaxException {
        log.debug("REST request to update Meetup : {}", meetup);
        if (meetup.getId() == null) {
            return createMeetup(meetup);
        }
        Meetup result = meetupService.save(meetup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meetup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetups : get all the meetups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of meetups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/meetups")
    @Timed
    public ResponseEntity<List<Meetup>> getAllMeetups(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Meetups");
        Page<Meetup> page = meetupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meetups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meetups/:id : get the "id" meetup.
     *
     * @param id the id of the meetup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meetup, or with status 404 (Not Found)
     */
    @GetMapping("/meetups/{id}")
    @Timed
    public ResponseEntity<Meetup> getMeetup(@PathVariable Long id) {
        log.debug("REST request to get Meetup : {}", id);
        Meetup meetup = meetupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meetup));
    }

    /**
     * DELETE  /meetups/:id : delete the "id" meetup.
     *
     * @param id the id of the meetup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meetups/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeetup(@PathVariable Long id) {
        log.debug("REST request to delete Meetup : {}", id);
        meetupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
