package br.com.lmello.common.customer;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UnprocessedCustomerDTO(
        @NotNull
        String name,
        @NotNull
        Operation operation,
        LocalDateTime arrivedAt
) {
        public UnprocessedCustomerDTO(CustomerDTO customer) {
                this(customer.name(), customer.operation(), LocalDateTime.now());
        }
}
