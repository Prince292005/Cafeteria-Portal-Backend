package com.dau.cafeteria_portal.enums;

public enum FeedbackOption {
    VERY_GOOD(5),
    GOOD(4),
    AVERAGE(3),
    POOR(2),
    VERY_POOR(1);

    private final int rating;
    FeedbackOption(int rating) {
        this.rating = rating;
    }
    public int getRating() { return rating; }
}
