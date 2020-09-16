package com.rapgru.ampel.service.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CoronaDataFetchSchedulerImpl implements CoronaDataFetchScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoronaDataFetchSchedulerImpl.class);
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final RefreshDataTask refreshDataTask;

    public CoronaDataFetchSchedulerImpl(RefreshDataTask refreshDataTask) {
        this.refreshDataTask = refreshDataTask;
    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(refreshDataTask, 0, 10, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduledExecutorService.shutdown();
        try {
            scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduledExecutorService.shutdownNow();
    }
}
