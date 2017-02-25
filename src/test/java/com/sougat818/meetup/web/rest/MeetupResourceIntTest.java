package com.sougat818.meetup.web.rest;

import com.sougat818.meetup.MeetupApp;

import com.sougat818.meetup.domain.Meetup;
import com.sougat818.meetup.repository.MeetupRepository;
import com.sougat818.meetup.service.MeetupService;
import com.sougat818.meetup.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.sougat818.meetup.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeetupResource REST controller.
 *
 * @see MeetupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetupApp.class)
public class MeetupResourceIntTest {

    private static final String DEFAULT_MEETUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_MEETUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MEETUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEETUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEETUP_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEETUP_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MEETUP_GOING_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_MEETUP_GOING_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeetupMockMvc;

    private Meetup meetup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetupResource meetupResource = new MeetupResource(meetupService);
        this.restMeetupMockMvc = MockMvcBuilders.standaloneSetup(meetupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meetup createEntity(EntityManager em) {
        Meetup meetup = new Meetup()
                .meetupId(DEFAULT_MEETUP_ID)
                .meetupName(DEFAULT_MEETUP_NAME)
                .meetupURL(DEFAULT_MEETUP_URL)
                .meetupGoingStatus(DEFAULT_MEETUP_GOING_STATUS)
                .date(DEFAULT_DATE);
        return meetup;
    }

    @Before
    public void initTest() {
        meetup = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeetup() throws Exception {
        int databaseSizeBeforeCreate = meetupRepository.findAll().size();

        // Create the Meetup

        restMeetupMockMvc.perform(post("/api/meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetup)))
            .andExpect(status().isCreated());

        // Validate the Meetup in the database
        List<Meetup> meetupList = meetupRepository.findAll();
        assertThat(meetupList).hasSize(databaseSizeBeforeCreate + 1);
        Meetup testMeetup = meetupList.get(meetupList.size() - 1);
        assertThat(testMeetup.getMeetupId()).isEqualTo(DEFAULT_MEETUP_ID);
        assertThat(testMeetup.getMeetupName()).isEqualTo(DEFAULT_MEETUP_NAME);
        assertThat(testMeetup.getMeetupURL()).isEqualTo(DEFAULT_MEETUP_URL);
        assertThat(testMeetup.getMeetupGoingStatus()).isEqualTo(DEFAULT_MEETUP_GOING_STATUS);
        assertThat(testMeetup.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createMeetupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meetupRepository.findAll().size();

        // Create the Meetup with an existing ID
        Meetup existingMeetup = new Meetup();
        existingMeetup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetupMockMvc.perform(post("/api/meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMeetup)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Meetup> meetupList = meetupRepository.findAll();
        assertThat(meetupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeetups() throws Exception {
        // Initialize the database
        meetupRepository.saveAndFlush(meetup);

        // Get all the meetupList
        restMeetupMockMvc.perform(get("/api/meetups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetup.getId().intValue())))
            .andExpect(jsonPath("$.[*].meetupId").value(hasItem(DEFAULT_MEETUP_ID.toString())))
            .andExpect(jsonPath("$.[*].meetupName").value(hasItem(DEFAULT_MEETUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].meetupURL").value(hasItem(DEFAULT_MEETUP_URL.toString())))
            .andExpect(jsonPath("$.[*].meetupGoingStatus").value(hasItem(DEFAULT_MEETUP_GOING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getMeetup() throws Exception {
        // Initialize the database
        meetupRepository.saveAndFlush(meetup);

        // Get the meetup
        restMeetupMockMvc.perform(get("/api/meetups/{id}", meetup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meetup.getId().intValue()))
            .andExpect(jsonPath("$.meetupId").value(DEFAULT_MEETUP_ID.toString()))
            .andExpect(jsonPath("$.meetupName").value(DEFAULT_MEETUP_NAME.toString()))
            .andExpect(jsonPath("$.meetupURL").value(DEFAULT_MEETUP_URL.toString()))
            .andExpect(jsonPath("$.meetupGoingStatus").value(DEFAULT_MEETUP_GOING_STATUS.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingMeetup() throws Exception {
        // Get the meetup
        restMeetupMockMvc.perform(get("/api/meetups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeetup() throws Exception {
        // Initialize the database
        meetupService.save(meetup);

        int databaseSizeBeforeUpdate = meetupRepository.findAll().size();

        // Update the meetup
        Meetup updatedMeetup = meetupRepository.findOne(meetup.getId());
        updatedMeetup
                .meetupId(UPDATED_MEETUP_ID)
                .meetupName(UPDATED_MEETUP_NAME)
                .meetupURL(UPDATED_MEETUP_URL)
                .meetupGoingStatus(UPDATED_MEETUP_GOING_STATUS)
                .date(UPDATED_DATE);

        restMeetupMockMvc.perform(put("/api/meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeetup)))
            .andExpect(status().isOk());

        // Validate the Meetup in the database
        List<Meetup> meetupList = meetupRepository.findAll();
        assertThat(meetupList).hasSize(databaseSizeBeforeUpdate);
        Meetup testMeetup = meetupList.get(meetupList.size() - 1);
        assertThat(testMeetup.getMeetupId()).isEqualTo(UPDATED_MEETUP_ID);
        assertThat(testMeetup.getMeetupName()).isEqualTo(UPDATED_MEETUP_NAME);
        assertThat(testMeetup.getMeetupURL()).isEqualTo(UPDATED_MEETUP_URL);
        assertThat(testMeetup.getMeetupGoingStatus()).isEqualTo(UPDATED_MEETUP_GOING_STATUS);
        assertThat(testMeetup.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMeetup() throws Exception {
        int databaseSizeBeforeUpdate = meetupRepository.findAll().size();

        // Create the Meetup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeetupMockMvc.perform(put("/api/meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetup)))
            .andExpect(status().isCreated());

        // Validate the Meetup in the database
        List<Meetup> meetupList = meetupRepository.findAll();
        assertThat(meetupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeetup() throws Exception {
        // Initialize the database
        meetupService.save(meetup);

        int databaseSizeBeforeDelete = meetupRepository.findAll().size();

        // Get the meetup
        restMeetupMockMvc.perform(delete("/api/meetups/{id}", meetup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Meetup> meetupList = meetupRepository.findAll();
        assertThat(meetupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meetup.class);
    }
}
