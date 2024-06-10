package br.com.lmello.dashboard.dto;

import java.time.LocalDateTime;

public record Statistic(
        LocalDateTime date,
        Integer totalCustomers,
        Integer totalWithdrawal,
        Integer totalDeposit,
        Integer totalPayment,
        Double meanAwaitTime,
        Double meanProcessingTime,
        Double extraWorkTime
) {
}
