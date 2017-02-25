package com.sougat818.meetup.service;

import com.sougat818.meetup.domain.Authority;
import com.sougat818.meetup.domain.Meetup;
import com.sougat818.meetup.domain.MeetupGroup;
import com.sougat818.meetup.domain.User;
import com.sougat818.meetup.pojo.Group;
import com.sougat818.meetup.pojo.MeetupResult;
import com.sougat818.meetup.pojo.Result;
import com.sougat818.meetup.repository.AuthorityRepository;
import com.sougat818.meetup.repository.MeetupGroupRepository;
import com.sougat818.meetup.repository.MeetupRepository;
import com.sougat818.meetup.repository.PersistentTokenRepository;
import com.sougat818.meetup.config.Constants;
import com.sougat818.meetup.repository.UserRepository;
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


    public MeetupAPIService(MeetupRepository meetupRepository, MeetupGroupRepository meetupGroupRepository) {
        this.meetupGroupRepository = meetupGroupRepository;
        this.meetupRepository = meetupRepository;
    }

    /**
     *
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateFromMeetupAPI() {
        log.info("Updating from Meetup API at " + Instant.now());
        List<Result> results = getMeetupList(URL, null);

        for (Result result :
            results) {
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


            Meetup meetup = meetupRepository.findOneByMeetupId(result.getId());
            if (meetup == null) {
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
