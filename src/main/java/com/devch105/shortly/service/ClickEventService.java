package com.devch105.shortly.service;

import com.devch105.shortly.entity.ClickEvent;
import com.devch105.shortly.entity.UrlEntity;
import com.devch105.shortly.repository.ClickEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickEventService {

    private final ClickEventRepository clickEventRepository;
    public void createEvent(UrlEntity urlEntity) {
        ClickEvent clickEvent = ClickEvent.builder()
                .urlMapping(urlEntity)
                .clickTime(LocalDateTime.now())
                .build();
        clickEventRepository.save(clickEvent);

    }
}
