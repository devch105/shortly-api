package com.devch105.shortly.dto;

import com.devch105.shortly.entity.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
   private  Long id;
   private String fullName;
   private   String email;
   private Role role;

}
