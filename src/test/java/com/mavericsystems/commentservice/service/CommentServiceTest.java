package com.mavericsystems.commentservice.service;

import com.mavericsystems.commentservice.constant.CommentConstant;
import com.mavericsystems.commentservice.dto.CommentDto;
import com.mavericsystems.commentservice.dto.CommentRequest;
import com.mavericsystems.commentservice.dto.UserDto;
import com.mavericsystems.commentservice.exception.CommentNotFoundException;
import com.mavericsystems.commentservice.exception.CustomFeignException;
import com.mavericsystems.commentservice.feign.LikeFeign;
import com.mavericsystems.commentservice.feign.UserFeign;
import com.mavericsystems.commentservice.model.Comment;
import com.mavericsystems.commentservice.repo.CommentRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentServiceImpl commentService;

    @MockBean
    CommentRepo commentRepo;

    @MockBean
    UserFeign userFeign;
    @MockBean
    private LikeFeign likeFeign;


    @Test
    void testDeleteCommentById() {
        commentService.deleteComment("1", "2");
        verify(commentRepo, times(1)).deleteById("2");
    }

    @Test
    void testExceptionThrownForCommentNotFoundWhenDeleteCommentById() {
        Mockito.doThrow(CommentNotFoundException.class).when(commentRepo).deleteById(any());
        Exception userNotFoundException = assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment("1", "2"));
        assertTrue(userNotFoundException.getMessage().contains(CommentConstant.COMMENT_NOT_FOUND));
    }

    @Test
    void testGetCommentsCountById() {
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId("1");
        comment1.setComment("good");
        Comment comment2 = new Comment();
        comment2.setId("2");
        comment2.setComment("nice");
        comments.add(comment1);
        comments.add(comment2);

        Mockito.when(commentRepo.findByPostId("1")).thenReturn(comments);
        assertThat(commentService.getCommentsCount("1"));
    }

    @Test
    void testExceptionThrownWhenGetCommentsCountNotFound() {
        Mockito.doThrow(CommentNotFoundException.class).when(commentRepo).findByPostId(any());
        Exception commentNotFoundException = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentsCount("1"));
        assertTrue(commentNotFoundException.getMessage().contains(CommentConstant.COMMENT_NOT_FOUND));
    }

    @Test
    void testGetAllComments() {
        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(this.commentRepo.findByPostId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(commentList);
        assertEquals(1, this.commentService.getComments("1", 1, 3).size());
        verify(this.userFeign).getUserById((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByPostId((String) any(), (org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testExceptionThrownWhenCommentNotFoundById() {
        when(this.commentRepo.findByPostId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(new ArrayList<>());
        assertThrows(CommentNotFoundException.class, () -> this.commentService.getComments("1", 1, 3));
        verify(this.commentRepo).findByPostId((String) any(), (org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForGetAllComments() {
        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenThrow(mock(FeignException.class));

        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());

        ArrayList<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(this.commentRepo.findByPostId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(commentList);
        assertThrows(CustomFeignException.class, () -> this.commentService.getComments("1", 1, 3));
        verify(this.userFeign).getUserById((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByPostId((String) any(), (org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testCreateComment() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserById((String) any())).thenReturn(userDto);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());
        when(this.commentRepo.save((Comment) any())).thenReturn(comment);
        CommentDto actualCreateCommentResult = this.commentService.createComment("1", new CommentRequest("1", "Comment", "Commented By"));
        assertEquals("Comment", actualCreateCommentResult.getComment());
        assertEquals(3, actualCreateCommentResult.getLikesCount());
        assertNull(actualCreateCommentResult.getId());
        assertSame(userDto, actualCreateCommentResult.getCommentedBy());
        verify(this.userFeign).getUserById((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).save((Comment) any());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForCreateComment() {
        CommentRequest commentRequest = new CommentRequest("1", "Comment", "Commented By");
        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
        when(this.commentRepo.save((Comment) any())).thenThrow(mock(FeignException.class));
        assertThrows(CustomFeignException.class, () -> this.commentService.createComment("1", commentRequest));
        verify(this.commentRepo).save((Comment) any());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForGetCommentDetailsById() {
        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenThrow(mock(FeignException.class));

        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());
        when(this.commentRepo.findByPostIdAndId((String) any(), (String) any())).thenReturn(comment);
        assertThrows(CustomFeignException.class, () -> this.commentService.getCommentDetails("1", "1"));
        verify(this.userFeign).getUserById((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByPostIdAndId((String) any(), (String) any());
    }

    @Test
    void testUpdateCommentById() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserById((String) any())).thenReturn(userDto);
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());

        Comment comment1 = new Comment();
        comment1.setComment("Comment");
        comment1.setCommentedBy("Commented By");
        comment1.setCreatedAt(LocalDate.now());
        comment1.setId("1");
        comment1.setPostId("1");
        comment1.setUpdatedAt(LocalDate.now());
        when(this.commentRepo.save((Comment) any())).thenReturn(comment1);
        when(this.commentRepo.findByPostIdAndId((String) any(), (String) any())).thenReturn(comment);
        CommentDto actualUpdateCommentResult = this.commentService.updateComment("1", new CommentRequest("1", "Comment", "Commented By"), "1");
        assertEquals("Comment", actualUpdateCommentResult.getComment());
        assertEquals(3, actualUpdateCommentResult.getLikesCount());
        assertEquals("1", actualUpdateCommentResult.getId());
        assertEquals(LocalDate.now().toString(), actualUpdateCommentResult.getCreatedAt().toString());
        assertSame(userDto, actualUpdateCommentResult.getCommentedBy());
        verify(this.userFeign).getUserById((String) any());
        verify(this.likeFeign).getLikesCount((String) any());
        verify(this.commentRepo).findByPostIdAndId((String) any(), (String) any());
        verify(this.commentRepo).save((Comment) any());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForUpdateCommentById() {
        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);

        CommentRequest commentRequest = new CommentRequest("1", "Comment", "Commented By");
        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setCommentedBy("Commented By");
        comment.setCreatedAt(LocalDate.now());
        comment.setId("1");
        comment.setPostId("1");
        comment.setUpdatedAt(LocalDate.now());
        when(this.commentRepo.save((Comment) any())).thenThrow(mock(FeignException.class));
        when(this.commentRepo.findByPostIdAndId((String) any(), (String) any())).thenReturn(comment);
        assertThrows(CustomFeignException.class, () -> this.commentService.updateComment("1", commentRequest, "1"));
        verify(this.commentRepo).findByPostIdAndId((String) any(), (String) any());
        verify(this.commentRepo).save((Comment) any());
    }

}
