package com.minipos.pos.service;

import com.minipos.pos.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // НОВЫЙ МЕТОД: Чтобы AdminDashboardController не ругался
    public void generateZReport() {
        System.out.println(">>> Формирование Z-отчета...");
        // Здесь можно вызвать логику сохранения итогов дня
    }

    public double getDailyTotal(String cashierName) {
        return reportRepository.getDailyTotalByCashier(cashierName);
    }

    public double getTotalRevenue() {
        return reportRepository.getTotalRevenue();
    }

    public Map<String, Double> getSalesByCashier() {
        return reportRepository.getRevenueGroupedByCashier();
    }
}