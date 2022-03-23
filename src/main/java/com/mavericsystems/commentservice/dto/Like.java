package com.mavericsystems.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private String id;
    private String postOrCommentId;
    private String likedBy;
    private LocalDate localDate;
}
