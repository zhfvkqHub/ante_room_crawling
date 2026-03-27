package com.anteprj.notice.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PageViewService {

    private final ConcurrentHashMap<LocalDate, AtomicLong> dailyViews = new ConcurrentHashMap<>();

    public long recordAndGet() {
        LocalDate today = LocalDate.now();
        cleanup(today);
        return dailyViews.computeIfAbsent(today, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getTodayViews() {
        return dailyViews.getOrDefault(LocalDate.now(), new AtomicLong(0)).get();
    }

    private void cleanup(LocalDate today) {
        dailyViews.keySet().removeIf(date -> date.isBefore(today));
    }
}
