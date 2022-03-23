package com.mavericsystems.commentservice.service;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.feign.LikeFeign;
import com.mavericsystems.commentservice.feign.UserFeign;
import com.mavericsystems.commentservice.model.Comment;
import com.mavericsystems.commentservice.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mavericsystems.commentservice.constant.CommentConstant.deletedComment;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    LikeFeign likeFeign;
    @Autowired
    UserFeign userFeign;
    @Override
    public List<Comment> getComments(String postId) {
        return commentRepo.findByPostId(postId);
    }
    @Override
    public CommentDto createComment(String postId, CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setComment(commentRequest.getComment());
        comment.setCommentedBy(commentRequest.getCommentedBy());
        comment.setCreatedAt(LocalDate.now());
        comment.setUpdatedAt(LocalDate.now());
        commentRepo.save(comment);
        return new CommentDto(comment.getId(),comment.getComment(),comment.getCommentedBy(),likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt(),comment.getPostId());
    }
    @Override
    public CommentDto getCommentDetails(String postId, String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId, commentId);
// String userName = userFeign.getUserById(comment.getCommentedBy()).getBody().getFirstName();
        return new CommentDto(commentId,comment.getComment(),comment.getCommentedBy(),likeFeign.getLikesCount(commentId),comment.getCreatedAt(),comment.getUpdatedAt(),postId);

    }

    @Override
    public Comment updateComment(String postId, CommentRequest commentRequest,String commentId) {
        Comment comment1 = commentRepo.findByPostIdAndId(postId,commentId);
        comment1.setComment(commentRequest.getComment());
        return commentRepo.save(comment1);
    }

}
