package com.mavericsystems.commentservice.repo;

import com.mavericsystems.commentservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByPostId(String postId);


    Comment findByPostIdAndId(String postId, String commentId);
}
