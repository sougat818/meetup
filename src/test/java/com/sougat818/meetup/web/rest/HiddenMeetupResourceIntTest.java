package com.sougat818.meetup.web.rest;

import com.sougat818.meetup.MeetupApp;

import com.sougat818.meetup.domain.HiddenMeetup;
import com.sougat818.meetup.repository.HiddenMeetupRepository;
import com.sougat818.meetup.service.HiddenMeetupService;
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
 * Test class for the HiddenMeetupResource REST controller.
 *
 * @see HiddenMeetupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetupApp.class)
public class HiddenMeetupResourceIntTest {

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
    private HiddenMeetupRepository hiddenMeetupRepository;

    @Autowired
    private HiddenMeetupService hiddenMeetupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHiddenMeetupMockMvc;

    private HiddenMeetup hiddenMeetup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HiddenMeetupResource hiddenMeetupResource = new HiddenMeetupResource(hiddenMeetupService);
        this.restHiddenMeetupMockMvc = MockMvcBuilders.standaloneSetup(hiddenMeetupResource)
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
    public static HiddenMeetup createEntity(EntityManager em) {
        HiddenMeetup hiddenMeetup = new HiddenMeetup()
                .meetupId(DEFAULT_MEETUP_ID)
                .meetupName(DEFAULT_MEETUP_NAME)
                .meetupURL(DEFAULT_MEETUP_URL)
                .meetupGoingStatus(DEFAULT_MEETUP_GOING_STATUS)
                .date(DEFAULT_DATE);
        return hiddenMeetup;
    }

    @Before
    public void initTest() {
        hiddenMeetup = createEntity(em);
    }

    @Test
    @Transactional
    public void createHiddenMeetup() throws Exception {
        int databaseSizeBeforeCreate = hiddenMeetupRepository.findAll().size();

        // Create the HiddenMeetup

        restHiddenMeetupMockMvc.perform(post("/api/hidden-meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hiddenMeetup)))
            .andExpect(status().isCreated());

        // Validate the HiddenMeetup in the database
        List<HiddenMeetup> hiddenMeetupList = hiddenMeetupRepository.findAll();
        assertThat(hiddenMeetupList).hasSize(databaseSizeBeforeCreate + 1);
        HiddenMeetup testHiddenMeetup = hiddenMeetupList.get(hiddenMeetupList.size() - 1);
        assertThat(testHiddenMeetup.getMeetupId()).isEqualTo(DEFAULT_MEETUP_ID);
        assertThat(testHiddenMeetup.getMeetupName()).isEqualTo(DEFAULT_MEETUP_NAME);
        assertThat(testHiddenMeetup.getMeetupURL()).isEqualTo(DEFAULT_MEETUP_URL);
        assertThat(testHiddenMeetup.getMeetupGoingStatus()).isEqualTo(DEFAULT_MEETUP_GOING_STATUS);
        assertThat(testHiddenMeetup.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createHiddenMeetupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hiddenMeetupRepository.findAll().size();

        // Create the HiddenMeetup with an existing ID
        HiddenMeetup existingHiddenMeetup = new HiddenMeetup();
        existingHiddenMeetup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHiddenMeetupMockMvc.perform(post("/api/hidden-meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHiddenMeetup)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HiddenMeetup> hiddenMeetupList = hiddenMeetupRepository.findAll();
        assertThat(hiddenMeetupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHiddenMeetups() throws Exception {
        // Initialize the database
        hiddenMeetupRepository.saveAndFlush(hiddenMeetup);

        // Get all the hiddenMeetupList
        restHiddenMeetupMockMvc.perform(get("/api/hidden-meetups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hiddenMeetup.getId().intValue())))
            .andExpect(jsonPath("$.[*].meetupId").value(hasItem(DEFAULT_MEETUP_ID.toString())))
            .andExpect(jsonPath("$.[*].meetupName").value(hasItem(DEFAULT_MEETUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].meetupURL").value(hasItem(DEFAULT_MEETUP_URL.toString())))
            .andExpect(jsonPath("$.[*].meetupGoingStatus").value(hasItem(DEFAULT_MEETUP_GOING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getHiddenMeetup() throws Exception {
        // Initialize the database
        hiddenMeetupRepository.saveAndFlush(hiddenMeetup);

        // Get the hiddenMeetup
        restHiddenMeetupMockMvc.perform(get("/api/hidden-meetups/{id}", hiddenMeetup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hiddenMeetup.getId().intValue()))
            .andExpect(jsonPath("$.meetupId").value(DEFAULT_MEETUP_ID.toString()))
            .andExpect(jsonPath("$.meetupName").value(DEFAULT_MEETUP_NAME.toString()))
            .andExpect(jsonPath("$.meetupURL").value(DEFAULT_MEETUP_URL.toString()))
            .andExpect(jsonPath("$.meetupGoingStatus").value(DEFAULT_MEETUP_GOING_STATUS.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingHiddenMeetup() throws Exception {
        // Get the hiddenMeetup
        restHiddenMeetupMockMvc.perform(get("/api/hidden-meetups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHiddenMeetup() throws Exception {
        // Initialize the database
        hiddenMeetupService.save(hiddenMeetup);

        int databaseSizeBeforeUpdate = hiddenMeetupRepository.findAll().size();

        // Update the hiddenMeetup
        HiddenMeetup updatedHiddenMeetup = hiddenMeetupRepository.findOne(hiddenMeetup.getId());
        updatedHiddenMeetup
                .meetupId(UPDATED_MEETUP_ID)
                .meetupName(UPDATED_MEETUP_NAME)
                .meetupURL(UPDATED_MEETUP_URL)
                .meetupGoingStatus(UPDATED_MEETUP_GOING_STATUS)
                .date(UPDATED_DATE);

        restHiddenMeetupMockMvc.perform(put("/api/hidden-meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHiddenMeetup)))
            .andExpect(status().isOk());

        // Validate the HiddenMeetup in the database
        List<HiddenMeetup> hiddenMeetupList = hiddenMeetupRepository.findAll();
        assertThat(hiddenMeetupList).hasSize(databaseSizeBeforeUpdate);
        HiddenMeetup testHiddenMeetup = hiddenMeetupList.get(hiddenMeetupList.size() - 1);
        assertThat(testHiddenMeetup.getMeetupId()).isEqualTo(UPDATED_MEETUP_ID);
        assertThat(testHiddenMeetup.getMeetupName()).isEqualTo(UPDATED_MEETUP_NAME);
        assertThat(testHiddenMeetup.getMeetupURL()).isEqualTo(UPDATED_MEETUP_URL);
        assertThat(testHiddenMeetup.getMeetupGoingStatus()).isEqualTo(UPDATED_MEETUP_GOING_STATUS);
        assertThat(testHiddenMeetup.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHiddenMeetup() throws Exception {
        int databaseSizeBeforeUpdate = hiddenMeetupRepository.findAll().size();

        // Create the HiddenMeetup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHiddenMeetupMockMvc.perform(put("/api/hidden-meetups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hiddenMeetup)))
            .andExpect(status().isCreated());

        // Validate the HiddenMeetup in the database
        List<HiddenMeetup> hiddenMeetupList = hiddenMeetupRepository.findAll();
        assertThat(hiddenMeetupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHiddenMeetup() throws Exception {
        // Initialize the database
        hiddenMeetupService.save(hiddenMeetup);

        int databaseSizeBeforeDelete = hiddenMeetupRepository.findAll().size();

        // Get the hiddenMeetup
        restHiddenMeetupMockMvc.perform(delete("/api/hidden-meetups/{id}", hiddenMeetup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HiddenMeetup> hiddenMeetupList = hiddenMeetupRepository.findAll();
        assertThat(hiddenMeetupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiddenMeetup.class);
    }
}
