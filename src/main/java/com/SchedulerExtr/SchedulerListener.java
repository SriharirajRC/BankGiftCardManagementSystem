package com.SchedulerExtr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.Repository.TransactionLogRepository;
import com.Service.TransactionReportScheduler;

public class SchedulerListener implements ServletContextListener {
    private TransactionReportScheduler scheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Starting Transaction Report Scheduler...");

        scheduler = new TransactionReportScheduler();
        scheduler.checkAndGenerateMissedReports();
        scheduler.startScheduler();
    }

	@Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Stopping Transaction Report Scheduler...");
        if (scheduler != null) {
            scheduler.stopScheduler();
        }
    }
}
