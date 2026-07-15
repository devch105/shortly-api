package com.devch105.shortly.repository;

import com.devch105.shortly.entity.ClickEvent;
import com.devch105.shortly.entity.UrlEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    Optional<ClickEvent> findByUrlMapping (UrlEntity
                                                   urlMapping);
    List<ClickEvent> findByUrlMappingAndClickTimeBetween(UrlEntity urlEntity, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<ClickEvent> findByUrlMappingInAndClickTimeBetween(List<UrlEntity> urlMappings, LocalDateTime startDateTime, LocalDateTime endDateTime);

   @Modifying
   @Transactional
   @Query("DELETE FROM ClickEvent c WHERE c.urlMapping =:url")
    void deleteAllByUrl ( @Param("url") UrlEntity url);
}
