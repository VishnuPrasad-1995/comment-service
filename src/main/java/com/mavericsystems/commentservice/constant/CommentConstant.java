package com.mavericsystems.commentservice.constant;

public final class CommentConstant {
    private CommentConstant() {
        // restrict instantiation
    }

    public static final String DELETED_COMMENT = "Comment deleted successfully"  ;
    public static final String FEIGN_EXCEPTION = "One of the service among user or like is unavailable";
    public static final String COMMENT_NOT_FOUND = "Comment not found for post id : ";
    public static final String COMMENT_ID_MISMATCH = "Comment Id passed in path variable and request Body does not match";
    public static final String COMMENT_ID = " & Comment id : ";
}
