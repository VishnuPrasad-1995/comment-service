package com.mavericsystems.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;
    private String comment;
    private UserDto commentedBy;
    private int likesCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
