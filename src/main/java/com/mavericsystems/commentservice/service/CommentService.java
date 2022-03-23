package com.mavericsystems.commentservice.service;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.model.Comment;

import javax.ws.rs.QueryParam;
import java.util.List;

public interface CommentService {
    List<Comment> getComments(String postId, Integer page, Integer pageSize);
    CommentDto createComment(String postId, CommentRequest commentRequest);
    CommentDto getCommentDetails(String postId,String commentId);
    CommentDto updateComment(String postId, CommentRequest commentRequest,String commentId);
    String deleteComment(String postId, String commentId);
    int getCommentsCount(String postId);


}
