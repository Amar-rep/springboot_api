package com.codewithmosh.store.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class UserDto {
    @JsonIgnore
    private Long id;
    private String name;
    private String email;


    private LocalDateTime createdAt;
}
