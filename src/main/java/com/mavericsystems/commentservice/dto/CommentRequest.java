package com.mavericsystems.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private String id;
    @NotEmpty(message = "Comment should not be empty")
    private String comment;
    private String commentedBy;

}
