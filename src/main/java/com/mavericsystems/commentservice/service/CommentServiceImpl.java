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
import java.util.ArrayList;
import java.util.List;

import static com.mavericsystems.commentservice.constant.CommentConstant.DELETEDCOMMENT;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    LikeFeign likeFeign;
    @Autowired
    UserFeign userFeign;
    @Override
    public List<CommentDto> getComments(String postId,Integer page, Integer pageSize) {
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        List<Comment> comments = commentRepo.findByPostId(postId, PageRequest.of(page-1, pageSize));
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments){
            commentDtoList.add(new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                    likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt()));
        }

        return commentDtoList;
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
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }
    @Override
    public CommentDto getCommentDetails(String postId, String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId, commentId);
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }

    @Override
    public CommentDto updateComment(String postId, CommentRequest commentRequest,String commentId) {
        Comment comment = commentRepo.findByPostIdAndId(postId,commentId);
        comment.setComment(commentRequest.getComment());
        comment.setUpdatedAt(LocalDate.now());
        commentRepo.save(comment);
        return new CommentDto(comment.getId(),comment.getComment(),userFeign.getUserById(comment.getCommentedBy()),
                likeFeign.getLikesCount(comment.getId()),comment.getCreatedAt(),comment.getUpdatedAt());

    }

    @Override
    public String deleteComment(String postId, String commentId) {
        commentRepo.deleteById(commentId);
        return DELETEDCOMMENT;
    }
    @Override
    public Integer getCommentsCount(String postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        return comments.size();
    }

}
