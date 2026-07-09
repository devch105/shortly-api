package com.devch105.shortly.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthDTO {
    private String email;
    private String password;
    private String token;
}
