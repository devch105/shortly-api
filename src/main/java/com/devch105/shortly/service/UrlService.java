package com.devch105.shortly.service;

import com.devch105.shortly.dto.ClickEventDTO;
import com.devch105.shortly.dto.ShortenUrlListDTO;
import com.devch105.shortly.dto.UrlDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.entity.ClickEvent;
import com.devch105.shortly.entity.UrlEntity;
import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.ClickEventRepository;
import com.devch105.shortly.repository.UrlRepository;
import com.devch105.shortly.repository.UserRepository;
import com.devch105.shortly.utils.ShortCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {


    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ClickEventRepository clickEventRepository;
    private final ClickEventService clickEventService;
    private final RedisService redisService;

    @Value("${app.backend.base.url}")
    private String backendUrl;


    /* Create shortUrl */
    public UrlDTO createShortUrl(String originalUrl, UserEntity user) {
        String shortcode = generateUniqueShortCode();
        UrlEntity urlEntity = UrlEntity.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortcode)
                .user(user)
                .build();
        UrlEntity result = urlRepository.save(urlEntity);
        return toUrlDto(result);
    }

    /* Generate UniqueShortCode */
    private String generateUniqueShortCode() {
        String shortcode;
        do {
            shortcode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortUrl(shortcode));
        return shortcode;
    }


    /* Get URLs of Current User */
    public List<UrlDTO> getUrlsOfCurrentUser(UserDTO user) {
        try {
            List<UrlEntity> currentUserUrls = urlRepository.findByUserId(user.getId());
            return currentUserUrls.stream().map(this::toUrlDto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error getting urls for current user : " + user, e);
        }
    }

    /* Get shorten Urls List */
    // updated
    public List<ShortenUrlListDTO> getShortenUrlsOfCurrentUser(UserDTO user) {
        try {
            List<UrlEntity> currentUserUrls = urlRepository.findByUserId(user.getId());
            return currentUserUrls.stream().map(this::toShortenUrlListDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error getting shoten urls list for current user : " + user, e);
        }
    }


    @Transactional
    public String deleteURL(String shortCode) {
        try {
            UrlEntity url = urlRepository.findByShortUrl(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
            clickEventRepository.deleteAllByUrl(url);
            urlRepository.delete(url);
            return "Successfully deleted";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting url : " + shortCode, e);
        }
    }


    /* getClickEventsByDate */
    public List<ClickEventDTO> getClickEventsByDate(String shortcode, LocalDateTime startDate, LocalDateTime endDate) {
        validationRange(startDate, endDate);
        UrlEntity urlEntity = getUrl(shortcode);
        List<ClickEvent> events = clickEventRepository.findByUrlMappingAndClickTimeBetween(urlEntity, startDate, endDate);
        return mapToDailyClicks(events);
    }

    /* Validate Date Range */
    private void validationRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time should be before start time");
        }
    }


    /* Get Url Mapping for shortcode */
    private UrlEntity getUrl(String shortCode) {
        return urlRepository.findByShortUrl(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
    }

    /* Get Clicked Data for Shortcode Url */
    private List<ClickEventDTO> mapToDailyClicks(List<ClickEvent> clickEvents) {
        return clickEvents.stream().collect(Collectors.groupingBy(
                event -> event.getClickTime().toLocalDate(), Collectors.counting()
        )).entrySet().stream().map(entry -> ClickEventDTO.builder()
                .clickDate(entry.getKey())
                .count(entry.getValue()).build()).collect(Collectors.toList());
    }


    /* Get total Clicks BY user and between dates*/
    public Map<LocalDate, Long> getTotalsClicksByUserAndDate(UserDTO user, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }
        List<UrlEntity> urlEntities = urlRepository.findByUserId(user.getId());
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickTimeBetween(urlEntities, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        return mapToLocalDateAndClickCount(clickEvents);
    }

    /* mapToLocalDateAndClickCount */
    private Map<LocalDate, Long> mapToLocalDateAndClickCount(List<ClickEvent> clickEvents) {
        return clickEvents.stream().collect(Collectors.groupingBy(click -> click.getClickTime().toLocalDate(), Collectors.counting()));
    }


    /* Get Original URL*/
//   public UrlDTO getOriginalUrl(String url) {
//       UrlEntity urlEntity =  urlRepository.findByShortUrl(url).orElseThrow(()->new RuntimeException("Url not found"));
//       urlEntity.setClickCount(urlEntity.getClickCount()+1);
//       UrlEntity saved =  urlRepository.save(urlEntity);
//       log.info("Original url found and Incremented Successfully : {}", url);
//       clickEventService.createEvent(saved);
//       return toUrlDto(saved);
//   }

    public String getOriginalUrl(String shortCode) {
        String chachedurl = redisService.getUrl(shortCode);
        if (chachedurl != null) {
            log.info("Redis Cache Hit : " + chachedurl);
            return chachedurl;
        }
        log.info("Redis Cache Miss : " + shortCode);
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
        if (urlEntity.getOriginalUrl() != null) {
            log.info("Found in DB : " + urlEntity.getOriginalUrl());
            redisService.saveUrl(shortCode, urlEntity.getOriginalUrl());
            urlEntity.setClickCount(urlEntity.getClickCount() + 1);
            UrlEntity saved = urlRepository.save(urlEntity);
            log.info("Original url found and Incremented Successfully : {}", shortCode);
            clickEventService.createEvent(saved);
            return urlEntity.getOriginalUrl();
        }else{
            throw new RuntimeException("Url not found");
        }
    }



   /*Shorten Url DTO*/
   public ShortenUrlListDTO  toShortenUrlListDTO(UrlEntity urlEntity){
          String shortURL = backendUrl+"/"+urlEntity.getShortUrl();
          return ShortenUrlListDTO.builder()
                  .id(urlEntity.getId())
                  .shortUrl(shortURL)
                  .longUrl(urlEntity.getOriginalUrl())
                  .shortCode(urlEntity.getShortUrl())
                  .clickCount(urlEntity.getClickCount())
                  .createdDate(urlEntity.getCreatedDate())
                  .build();
   }
   /* ToUrlDTO Method */
    public  UrlDTO toUrlDto(UrlEntity urlEntity) {
        return UrlDTO.builder()
                .id(urlEntity.getId())
                .shortUrl(urlEntity.getShortUrl())
                .originalUrl(urlEntity.getOriginalUrl())
                .email(urlEntity.getUser().getEmail())
                .clickCount(urlEntity.getClickCount())
                .createdDate(urlEntity.getCreatedDate())
                .build();
    }


}
