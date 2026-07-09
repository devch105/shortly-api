package com.devch105.shortly.repository;

import com.devch105.shortly.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    List<UrlEntity> findByUserId(Long userId);
    Optional<UrlEntity> findByShortUrl (String shorturl);
    boolean existsByShortUrl (String shorturl);
}
