package com.devch105.shortly.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {
    private static  final String Base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    private static final int Length = 7;

    private static final SecureRandom random = new SecureRandom();

    public  String generate(){
        StringBuilder sb = new StringBuilder(Length);
        for(int i=0; i<Length; i++){
            sb.append(Base62.charAt(random.nextInt(Base62.length())));
        }
        return sb.toString();
    }


}
