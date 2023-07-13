package com.hepsi.todoapp.todoapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
public class Utility {

    public static boolean did24HoursPassed(Long timeStamp)    {
        LocalDateTime zdt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.UTC);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return now.minusHours(24).isAfter(zdt);
    }
}
