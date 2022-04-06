package com.mavericsystems.commentservice.constant;

public final class CommentConstant {
    private CommentConstant() {
        // restrict instantiation
    }

    public static final String DELETEDCOMMENT = "Comment deleted successfully"  ;
    public static final String FEIGNEXCEPTON = "One of the service among user or like is unavailable";
    public static final String COMMENTNOTFOUND = "Comment not found for post id : ";
    public static final String COMMENTIDMISMATCH = "Comment Id passed in path variable and request Body does not match";
    public static final String COMMENTID = " & Comment id : ";
}
