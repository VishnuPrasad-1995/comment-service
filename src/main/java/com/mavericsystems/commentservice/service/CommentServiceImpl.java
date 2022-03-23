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

}
