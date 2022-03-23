package com.mavericsystems.commentservice.service;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.feign.LikeFeign;
import com.mavericsystems.commentservice.feign.UserFeign;
import com.mavericsystems.commentservice.model.Comment;
import com.mavericsystems.commentservice.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<Comment> getComments(String postId,Integer page, Integer pageSize) {
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        List<Comment> comments = commentRepo.findByPostId(postId, PageRequest.of(page-1, pageSize));

        return comments;
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
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()).getEmail(),likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());
    }
    @Override
    public CommentDto getCommentDetails(String postId, String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId, commentId);
        return new CommentDto(commentId,comment.getComment(),userFeign.getUserById(comment.getCommentedBy()).getEmail(),likeFeign.getLikesCount(commentId),comment.getCreatedAt(),comment.getUpdatedAt());

    }

    @Override
    public CommentDto updateComment(String postId, CommentRequest commentRequest,String commentId) {
        Comment comment1 = commentRepo.findByPostIdAndId(postId,commentId);
        comment1.setUpdatedAt(LocalDate.now());
        comment1.setComment(commentRequest.getComment());
        commentRepo.save(comment1);
        return new CommentDto(commentId,comment1.getComment(),userFeign.getUserById(comment1.getCommentedBy()).getEmail(),likeFeign.getLikesCount(commentId),comment1.getCreatedAt(),LocalDate.now());
    }

    @Override
    public String deleteComment(String postId, String commentId) {
        commentRepo.deleteById(commentId);
        return deletedComment;
    }
    @Override
    public int getCommentsCount(String postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        return comments.size();
    }

}
