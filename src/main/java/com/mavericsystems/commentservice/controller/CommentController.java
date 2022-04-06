package com.mavericsystems.commentservice.controller;

import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.exception.CommentIdMismatchException;
import com.mavericsystems.commentservice.model.Comment;
import com.mavericsystems.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

import static com.mavericsystems.commentservice.constant.CommentConstant.COMMENTIDMISMATCH;

@CrossOrigin (origins = "http://localhost:8080")
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    @Autowired
    CommentService commentService;
    @GetMapping()
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable("postId") String postId, @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize){
        return new ResponseEntity<>(commentService.getComments(postId,page,pageSize), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") String postId,@Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentService.createComment(postId,commentRequest), HttpStatus.CREATED);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId,@Valid @RequestBody CommentRequest commentRequest){
        if(commentRequest.getId().equals(commentId))
            return new ResponseEntity<>(commentService.updateComment(postId,commentRequest,commentId), HttpStatus.OK);
        else
            throw new CommentIdMismatchException(COMMENTIDMISMATCH);

    }
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentDetails(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId){
        return new ResponseEntity<>(commentService.getCommentDetails(postId,commentId), HttpStatus.OK);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") String postId,@PathVariable("commentId") String commentId){
        return new ResponseEntity<>(commentService.deleteComment(postId,commentId), HttpStatus.OK);
    }
    @GetMapping("/count")
    public Integer getCommentsCount(@PathVariable("postId") String postId){
        return commentService.getCommentsCount(postId);
    }

}
