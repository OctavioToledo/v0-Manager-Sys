package com.demoV1Project.util.enums;

public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
    FRIDAY, SATURDAY, SUNDAY ;

    public static DayOfWeek fromJavaDay(java.time.DayOfWeek javaDay) {
        return DayOfWeek.valueOf(javaDay.name());
    }
}
