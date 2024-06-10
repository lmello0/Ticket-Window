package br.com.lmello.common.customer;

import java.time.LocalDateTime;

public record ProcessedCustomerDTO(
        String name,
        Operation operation,
        LocalDateTime arrivedAt,
        LocalDateTime startProcessingAt,
        LocalDateTime finishedProcessingAt,
        Boolean isAfterWorkTime
) {
}
