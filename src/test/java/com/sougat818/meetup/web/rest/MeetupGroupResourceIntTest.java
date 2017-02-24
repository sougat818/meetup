package com.sougat818.meetup.web.rest;

import com.sougat818.meetup.MeetupApp;

import com.sougat818.meetup.domain.MeetupGroup;
import com.sougat818.meetup.repository.MeetupGroupRepository;
import com.sougat818.meetup.service.MeetupGroupService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeetupGroupResource REST controller.
 *
 * @see MeetupGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetupApp.class)
public class MeetupGroupResourceIntTest {

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_URL = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BLOCKED = false;
    private static final Boolean UPDATED_BLOCKED = true;

    @Autowired
    private MeetupGroupRepository meetupGroupRepository;

    @Autowired
    private MeetupGroupService meetupGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeetupGroupMockMvc;

    private MeetupGroup meetupGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetupGroupResource meetupGroupResource = new MeetupGroupResource(meetupGroupService);
        this.restMeetupGroupMockMvc = MockMvcBuilders.standaloneSetup(meetupGroupResource)
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
    public static MeetupGroup createEntity(EntityManager em) {
        MeetupGroup meetupGroup = new MeetupGroup()
                .groupId(DEFAULT_GROUP_ID)
                .groupName(DEFAULT_GROUP_NAME)
                .groupURL(DEFAULT_GROUP_URL)
                .blocked(DEFAULT_BLOCKED);
        return meetupGroup;
    }

    @Before
    public void initTest() {
        meetupGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeetupGroup() throws Exception {
        int databaseSizeBeforeCreate = meetupGroupRepository.findAll().size();

        // Create the MeetupGroup

        restMeetupGroupMockMvc.perform(post("/api/meetup-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetupGroup)))
            .andExpect(status().isCreated());

        // Validate the MeetupGroup in the database
        List<MeetupGroup> meetupGroupList = meetupGroupRepository.findAll();
        assertThat(meetupGroupList).hasSize(databaseSizeBeforeCreate + 1);
        MeetupGroup testMeetupGroup = meetupGroupList.get(meetupGroupList.size() - 1);
        assertThat(testMeetupGroup.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testMeetupGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testMeetupGroup.getGroupURL()).isEqualTo(DEFAULT_GROUP_URL);
        assertThat(testMeetupGroup.isBlocked()).isEqualTo(DEFAULT_BLOCKED);
    }

    @Test
    @Transactional
    public void createMeetupGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meetupGroupRepository.findAll().size();

        // Create the MeetupGroup with an existing ID
        MeetupGroup existingMeetupGroup = new MeetupGroup();
        existingMeetupGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetupGroupMockMvc.perform(post("/api/meetup-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMeetupGroup)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MeetupGroup> meetupGroupList = meetupGroupRepository.findAll();
        assertThat(meetupGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeetupGroups() throws Exception {
        // Initialize the database
        meetupGroupRepository.saveAndFlush(meetupGroup);

        // Get all the meetupGroupList
        restMeetupGroupMockMvc.perform(get("/api/meetup-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetupGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.toString())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupURL").value(hasItem(DEFAULT_GROUP_URL.toString())))
            .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMeetupGroup() throws Exception {
        // Initialize the database
        meetupGroupRepository.saveAndFlush(meetupGroup);

        // Get the meetupGroup
        restMeetupGroupMockMvc.perform(get("/api/meetup-groups/{id}", meetupGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meetupGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.toString()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.groupURL").value(DEFAULT_GROUP_URL.toString()))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMeetupGroup() throws Exception {
        // Get the meetupGroup
        restMeetupGroupMockMvc.perform(get("/api/meetup-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeetupGroup() throws Exception {
        // Initialize the database
        meetupGroupService.save(meetupGroup);

        int databaseSizeBeforeUpdate = meetupGroupRepository.findAll().size();

        // Update the meetupGroup
        MeetupGroup updatedMeetupGroup = meetupGroupRepository.findOne(meetupGroup.getId());
        updatedMeetupGroup
                .groupId(UPDATED_GROUP_ID)
                .groupName(UPDATED_GROUP_NAME)
                .groupURL(UPDATED_GROUP_URL)
                .blocked(UPDATED_BLOCKED);

        restMeetupGroupMockMvc.perform(put("/api/meetup-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeetupGroup)))
            .andExpect(status().isOk());

        // Validate the MeetupGroup in the database
        List<MeetupGroup> meetupGroupList = meetupGroupRepository.findAll();
        assertThat(meetupGroupList).hasSize(databaseSizeBeforeUpdate);
        MeetupGroup testMeetupGroup = meetupGroupList.get(meetupGroupList.size() - 1);
        assertThat(testMeetupGroup.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testMeetupGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testMeetupGroup.getGroupURL()).isEqualTo(UPDATED_GROUP_URL);
        assertThat(testMeetupGroup.isBlocked()).isEqualTo(UPDATED_BLOCKED);
    }

    @Test
    @Transactional
    public void updateNonExistingMeetupGroup() throws Exception {
        int databaseSizeBeforeUpdate = meetupGroupRepository.findAll().size();

        // Create the MeetupGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeetupGroupMockMvc.perform(put("/api/meetup-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetupGroup)))
            .andExpect(status().isCreated());

        // Validate the MeetupGroup in the database
        List<MeetupGroup> meetupGroupList = meetupGroupRepository.findAll();
        assertThat(meetupGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeetupGroup() throws Exception {
        // Initialize the database
        meetupGroupService.save(meetupGroup);

        int databaseSizeBeforeDelete = meetupGroupRepository.findAll().size();

        // Get the meetupGroup
        restMeetupGroupMockMvc.perform(delete("/api/meetup-groups/{id}", meetupGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MeetupGroup> meetupGroupList = meetupGroupRepository.findAll();
        assertThat(meetupGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeetupGroup.class);
    }
}
