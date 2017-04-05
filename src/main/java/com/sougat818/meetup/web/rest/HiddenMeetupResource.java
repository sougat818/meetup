package com.sougat818.meetup.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sougat818.meetup.domain.HiddenMeetup;
import com.sougat818.meetup.service.HiddenMeetupService;
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
 * REST controller for managing HiddenMeetup.
 */
@RestController
@RequestMapping("/api")
public class HiddenMeetupResource {

    private final Logger log = LoggerFactory.getLogger(HiddenMeetupResource.class);

    private static final String ENTITY_NAME = "hiddenMeetup";
        
    private final HiddenMeetupService hiddenMeetupService;

    public HiddenMeetupResource(HiddenMeetupService hiddenMeetupService) {
        this.hiddenMeetupService = hiddenMeetupService;
    }

    /**
     * POST  /hidden-meetups : Create a new hiddenMeetup.
     *
     * @param hiddenMeetup the hiddenMeetup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hiddenMeetup, or with status 400 (Bad Request) if the hiddenMeetup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hidden-meetups")
    @Timed
    public ResponseEntity<HiddenMeetup> createHiddenMeetup(@RequestBody HiddenMeetup hiddenMeetup) throws URISyntaxException {
        log.debug("REST request to save HiddenMeetup : {}", hiddenMeetup);
        if (hiddenMeetup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hiddenMeetup cannot already have an ID")).body(null);
        }
        HiddenMeetup result = hiddenMeetupService.save(hiddenMeetup);
        return ResponseEntity.created(new URI("/api/hidden-meetups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hidden-meetups : Updates an existing hiddenMeetup.
     *
     * @param hiddenMeetup the hiddenMeetup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hiddenMeetup,
     * or with status 400 (Bad Request) if the hiddenMeetup is not valid,
     * or with status 500 (Internal Server Error) if the hiddenMeetup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hidden-meetups")
    @Timed
    public ResponseEntity<HiddenMeetup> updateHiddenMeetup(@RequestBody HiddenMeetup hiddenMeetup) throws URISyntaxException {
        log.debug("REST request to update HiddenMeetup : {}", hiddenMeetup);
        if (hiddenMeetup.getId() == null) {
            return createHiddenMeetup(hiddenMeetup);
        }
        HiddenMeetup result = hiddenMeetupService.save(hiddenMeetup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hiddenMeetup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hidden-meetups : get all the hiddenMeetups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hiddenMeetups in body
     */
    @GetMapping("/hidden-meetups")
    @Timed
    public List<HiddenMeetup> getAllHiddenMeetups() {
        log.debug("REST request to get all HiddenMeetups");
        return hiddenMeetupService.findAll();
    }

    /**
     * GET  /hidden-meetups/:id : get the "id" hiddenMeetup.
     *
     * @param id the id of the hiddenMeetup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hiddenMeetup, or with status 404 (Not Found)
     */
    @GetMapping("/hidden-meetups/{id}")
    @Timed
    public ResponseEntity<HiddenMeetup> getHiddenMeetup(@PathVariable Long id) {
        log.debug("REST request to get HiddenMeetup : {}", id);
        HiddenMeetup hiddenMeetup = hiddenMeetupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hiddenMeetup));
    }

    /**
     * DELETE  /hidden-meetups/:id : delete the "id" hiddenMeetup.
     *
     * @param id the id of the hiddenMeetup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hidden-meetups/{id}")
    @Timed
    public ResponseEntity<Void> deleteHiddenMeetup(@PathVariable Long id) {
        log.debug("REST request to delete HiddenMeetup : {}", id);
        hiddenMeetupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
