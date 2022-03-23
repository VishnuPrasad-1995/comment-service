package com.mavericsystems.commentservice.service;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(String postId);
    CommentDto createComment(String postId, CommentRequest commentRequest);

}
