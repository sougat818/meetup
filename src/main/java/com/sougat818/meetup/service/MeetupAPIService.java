package com.sougat818.meetup.service;

import com.sougat818.meetup.domain.*;
import com.sougat818.meetup.pojo.Group;
import com.sougat818.meetup.pojo.MeetupResult;
import com.sougat818.meetup.pojo.Result;
import com.sougat818.meetup.repository.*;
import com.sougat818.meetup.config.Constants;
import com.sougat818.meetup.security.AuthoritiesConstants;
import com.sougat818.meetup.security.SecurityUtils;
import com.sougat818.meetup.service.util.RandomUtil;
import com.sougat818.meetup.service.dto.UserDTO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
public class MeetupAPIService {

    private final Logger log = LoggerFactory.getLogger(MeetupAPIService.class);

    private final String URL = "https://api.meetup.com/2/open_events?country=au&city=sydney&radius=100&key=<KEY>";

    private final MeetupGroupRepository meetupGroupRepository;

    private final MeetupRepository meetupRepository;

    private final HiddenMeetupRepository hiddenMeetupRepository;


    public MeetupAPIService(MeetupRepository meetupRepository, MeetupGroupRepository meetupGroupRepository, HiddenMeetupRepository hiddenMeetupRepository) {
        this.meetupGroupRepository = meetupGroupRepository;
        this.meetupRepository = meetupRepository;
        this.hiddenMeetupRepository = hiddenMeetupRepository;
    }


    private void updateMeetup(Result result, Meetup meetup) {
        if (!Instant.ofEpochMilli(result.getTime()).atZone(ZoneId.of("Australia/Sydney")).equals(meetup.getDate())) {
            log.info("Updating Meetup");
            meetup.setDate(Instant.ofEpochMilli(result.getTime()).atZone(ZoneId.of("Australia/Sydney")));
            meetupRepository.save(meetup);
        }
    }

    /**
     *
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void updateFromMeetupAPI() {
        log.info("Updating from Meetup API at " + Instant.now());
        List<Result> results = getMeetupList(URL, null);

        for (Result result :
            results) {

            HiddenMeetup hiddenMeetup = hiddenMeetupRepository.findOneByMeetupId(result.getId());

            if (hiddenMeetup != null) {
                continue;
            }

            Meetup meetup = meetupRepository.findOneByMeetupId(result.getId());

            if (meetup != null) {
                updateMeetup(result, meetup);
                continue;
            }

            Group group = result.getGroup();
            MeetupGroup meetupGroup = meetupGroupRepository.findOneByGroupId(String.valueOf(group.getId()));

            if (meetupGroup == null) {
                meetupGroup = new MeetupGroup();
                meetupGroup.setBlocked(false);
                meetupGroup.setGroupId(result.getGroup().getId().toString());
                meetupGroup.setGroupName(result.getGroup().getName());
                meetupGroup.setGroupURL(result.getGroup().getUrlname());
                meetupGroup = meetupGroupRepository.save(meetupGroup);
            }


            if (meetupGroup.isBlocked()) {
                hiddenMeetup = new HiddenMeetup();
                hiddenMeetup.setMeetupId(result.getId());
                hiddenMeetup.setMeetupGoingStatus("hide");
                hiddenMeetup.setMeetupGroup(meetupGroup);
                hiddenMeetup.setDate(Instant.ofEpochMilli(result.getTime()).atZone(ZoneId.of("Australia/Sydney")));
                hiddenMeetup.setMeetupName(result.getName());
                hiddenMeetup.setMeetupURL(result.getEventUrl());
                hiddenMeetupRepository.save(hiddenMeetup);
                continue;
            }

            meetup = new Meetup();
            meetup.setMeetupId(result.getId());
            meetup.setMeetupGoingStatus("May Be");
            meetup.setMeetupGroup(meetupGroup);
            meetup.setDate(Instant.ofEpochMilli(result.getTime()).atZone(ZoneId.of("Australia/Sydney")));
            meetup.setMeetupName(result.getName());
            meetup.setMeetupURL(result.getEventUrl());
            meetupRepository.save(meetup);
        }
    }

    /**
     *
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void moveHiddenMeetups() {
        log.info("Moving Hidden Meetups " + Instant.now());
        List<Meetup> meetups = meetupRepository.findAllByMeetupGoingStatusEquals("hide");

        for (Meetup meetup :
            meetups) {
            HiddenMeetup hiddenMeetup = convertMeetupToHiddenMeetup(meetup);
            hiddenMeetupRepository.save(hiddenMeetup);
            meetupRepository.delete(meetup);
        }

    }

    /**
     *
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void moveUnhiddenMeetups() {
        log.info("Moving Unhidden Meetups " + Instant.now());
        List<HiddenMeetup> hiddenMeetups = hiddenMeetupRepository.findAllByMeetupGoingStatusNot("hide");

        for (HiddenMeetup hiddenMeetup :
            hiddenMeetups) {
            Meetup meetup = convertHiddenMeetupToMeetup(hiddenMeetup);
            meetupRepository.save(meetup);
            hiddenMeetupRepository.delete(hiddenMeetup);
        }

    }


    /**
     *
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void deleteOldMeetups() {
        log.info("Deleting Old Meetups " + Instant.now());
        List<Meetup> meetups = meetupRepository.findAllByDateBefore(ZonedDateTime.now());

        for (Meetup meetup :
            meetups) {
            meetupRepository.delete(meetup);
        }

    }

    /**
     *
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void deleteOldHiddenMeetups() {
        log.info("Deleting Old Hidden Meetups " + Instant.now());
        List<HiddenMeetup> hiddenMeetups = hiddenMeetupRepository.findAllByDateBefore(ZonedDateTime.now());

        for (HiddenMeetup hiddenMeetup :
            hiddenMeetups) {
            hiddenMeetupRepository.delete(hiddenMeetup);
        }

    }

    private HiddenMeetup convertMeetupToHiddenMeetup(Meetup meetup) {
        HiddenMeetup hiddenMeetup = new HiddenMeetup();
        hiddenMeetup.setDate(meetup.getDate());
        hiddenMeetup.setMeetupGoingStatus(meetup.getMeetupGoingStatus());
        hiddenMeetup.setMeetupGroup(meetup.getMeetupGroup());
        hiddenMeetup.setMeetupName(meetup.getMeetupName());
        hiddenMeetup.setMeetupURL(meetup.getMeetupURL());
        hiddenMeetup.setMeetupId(meetup.getMeetupId());
        return hiddenMeetup;
    }

    private Meetup convertHiddenMeetupToMeetup(HiddenMeetup hiddenMeetup) {
        Meetup meetup = new Meetup();
        meetup.setDate(hiddenMeetup.getDate());
        meetup.setMeetupGoingStatus(hiddenMeetup.getMeetupGoingStatus());
        meetup.setMeetupGroup(hiddenMeetup.getMeetupGroup());
        meetup.setMeetupName(hiddenMeetup.getMeetupName());
        meetup.setMeetupURL(hiddenMeetup.getMeetupURL());
        meetup.setMeetupId(hiddenMeetup.getMeetupId());
        return meetup;
    }


    private List<Result> getMeetupList(String url, List<Result> results) {
        if (results == null) {
            results = new ArrayList<>();
        }

        if (StringUtils.isEmpty(url)) {
            return results;
        }

        RestTemplate restTemplate = new RestTemplate();
        MeetupResult result = restTemplate.getForObject(url, MeetupResult.class);

        results.addAll(result.getResults());
        return getMeetupList(result.getMeta().getNext(), results);
    }
}
