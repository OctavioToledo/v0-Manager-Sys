package com.demoV1Project.util.enums;

public enum SubscriptionTier {
    BASIC(1, 0, "Basic"),
    PROFESSIONAL(5, 29, "Professional"),
    ENTERPRISE(999, 79, "Enterprise");

    private final int maxEmployees;
    private final int priceUsd;
    private final String displayName;

    SubscriptionTier(int maxEmployees, int priceUsd, String displayName) {
        this.maxEmployees = maxEmployees;
        this.priceUsd = priceUsd;
        this.displayName = displayName;
    }

    public int getMaxEmployees() {
        return maxEmployees;
    }

    public int getPriceUsd() {
        return priceUsd;
    }

    public String getDisplayName() {
        return displayName;
    }
}
