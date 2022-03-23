package com.mavericsystems.commentservice.controller;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.model.Comment;
import com.mavericsystems.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment/posts/{postId}/comments")
public class CommentController {
    @Autowired
    CommentService commentService;
    @GetMapping()
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") String postId){
        return new ResponseEntity<>(commentService.getComments(postId), HttpStatus.OK);
    }


}
