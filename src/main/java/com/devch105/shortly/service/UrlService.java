package com.devch105.shortly.service;

import com.devch105.shortly.dto.ClickEventDTO;
import com.devch105.shortly.dto.UrlDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.entity.UrlEntity;
import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.ClickEventRepository;
import com.devch105.shortly.repository.UrlRepository;
import com.devch105.shortly.repository.UserRepository;
import com.devch105.shortly.utils.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {


    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ClickEventRepository clickEventRepository;

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

    private String generateUniqueShortCode() {
        String shortcode ;
        do {
            shortcode = shortCodeGenerator.generate();
        }while (urlRepository.existsByShortUrl(shortcode));
        return shortcode;
    }


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


    public List<UrlDTO> geUrlsOfCurrentUser(UserDTO user) {
       try{
           List<UrlEntity> currentUserUrls = urlRepository.findByUserId(user.getId());
           return currentUserUrls.stream().map(this::toUrlDto).toList();
       }catch (Exception e){
           throw new RuntimeException("Error getting urls for current user : " + user, e);
       }
    }

//    public List<ClickEventDTO> getClickEventsByDate(String shortcode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
//      try{
//          if(endDateTime.isBefore(startDateTime)) {
//              throw new RuntimeException("End date is before start date");
//          }
//          UrlEntity urlEntity = urlRepository.findByShortUrl(shortcode).orElseThrow(() -> new RuntimeException("Url does not exist"));
//          if(urlEntity != null) {
//              return clickEventRepository.findByShortUrlAndClickDateBetween(urlEntity,startDateTime,endDateTime).stream()
//                      .collect(Collectors.groupingBy(click -> click.getClickTime().toLocalDate(), Collection.counting)
//          }
//      }
//    }
}
