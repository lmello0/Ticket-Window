package br.com.lmello.common.customer;

import jakarta.validation.constraints.NotNull;

public record CustomerDTO(
        @NotNull
        String name,
        @NotNull
        Operation operation
) {
}
