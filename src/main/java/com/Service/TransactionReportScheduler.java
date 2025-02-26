package com.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.Model.TransactionLog;
import com.Repository.TransactionLogRepository;

public class TransactionReportScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startScheduler() {
        Runnable task = this::generateDailyTransactionReport;
        
        scheduler.scheduleAtFixedRate(task, 0, 24, TimeUnit.HOURS);
//        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

    }

    public void stopScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    private long getInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.toLocalDate().atStartOfDay().plusDays(1);
        return java.time.Duration.between(now, nextRun).toSeconds();
    }

    private void generateDailyTransactionReport() {
        try {
            System.err.println("Running Transaction Report Task...");

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(23, 59, 59);

            Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
            Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

            if(writeReport( startTimestamp,endTimestamp, today))
            	System.out.println("Transaction report for " + today + " completed successfully.");
            else
            	System.out.println("FAILED REPORTING");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAndGenerateMissedReports() {
        String lastReportFileName = TransactionLogRepository.getLastReportFileName();

        if (lastReportFileName == null) {
            System.out.println("No reports found in the database.");
            return;
        }

        String lastReportDateStr = lastReportFileName.split("_")[1].split("\\.")[0];
        LocalDate lastReportDate = LocalDate.parse(lastReportDateStr);

        LocalDate today = LocalDate.now();
        LocalDate startDate = lastReportDate.plusDays(1);
        LocalDate endDate = today.minusDays(1);

        while (!startDate.isAfter(endDate)) {
            generateReportForDate(startDate);
            startDate = startDate.plusDays(1);
        }
    }
    
    private void generateReportForDate(LocalDate reportDate) {
        try {
            System.out.println("Running Transaction Report Task for: " + reportDate);

            LocalDateTime startOfDay = reportDate.atStartOfDay();
            LocalDateTime endOfDay = reportDate.atTime(23, 59, 59);

            Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
            Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

            if(writeReport(startTimestamp,endTimestamp, reportDate))
            	System.out.println("Transaction report for " + reportDate + " completed successfully.");
            else
            	System.out.println("FAILED REPORTING");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean writeReport(Timestamp startTimestamp, Timestamp endTimestamp , LocalDate date) {
    	
    	List<TransactionLog> transactions = TransactionLogRepository.findByEventdateBetween(startTimestamp, endTimestamp);

    	
        String reportFilePath = "reports/TransactionReport_" + date + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFilePath))) {
        	if(transactions.size() == 0) {
        		writer.write("NO TRANSACTION TODAY - "+date);
        	}
        	else {
            writer.write("ID,User,Amount,Event ID,Date,Details");
            writer.newLine();

            for (TransactionLog transaction : transactions) {
                writer.write(transaction.getId() + "," +
                        transaction.getUserid() + "," +
                        transaction.getAmount() + "," +
                        transaction.getEventname() + "," +
                        transaction.getEventdate() + "," +
                        transaction.getDetails());
                writer.newLine();
            }
        	}

            System.out.println("CSV saved: " + reportFilePath);
            TransactionLogRepository.insertCSVnames(reportFilePath);
        }catch(Exception e) {
        	System.out.println("ERROR-"+e.getMessage());
        	return false;
        }
        return true;
    }



}
