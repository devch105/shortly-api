package com.devch105.shortly.repository;

import com.devch105.shortly.entity.ClickEvent;
import com.devch105.shortly.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    Optional<ClickEvent> findByShortUrl (String shorturl);
    List<ClickEvent> findByUserId(Long userId);
    List<ClickEvent> findByShortUrlAndClickDateBetween(UrlEntity urlEntity, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<ClickEvent> findByShortUrlInAndClickDateBetween(List<UrlEntity> urlMappings, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
