package br.com.lmello.dashboard.model;

import br.com.lmello.common.customer.Operation;
import br.com.lmello.common.customer.ProcessedCustomerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "customers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    private LocalDateTime arrivedAt;
    private LocalDateTime startProcessingAt;
    private LocalDateTime finishedProcessingAt;
    private boolean processedInWorkHour;

    @Override
    public String toString() {
        return "Customer[" +
                "name='" + name + '\'' +
                ", operation=" + operation +
                ']';
    }

    public Customer(ProcessedCustomerDTO customer) {
        this.id = UUID.randomUUID().toString();
        this.name = customer.name();
        this.operation = customer.operation();
        this.arrivedAt = customer.arrivedAt();
        this.startProcessingAt = customer.startProcessingAt();
        this.finishedProcessingAt = customer.finishedProcessingAt();
        this.processedInWorkHour = customer.isAfterWorkTime();
    }

    public Long getArrivedAtMilli() {
        ZonedDateTime zdt = arrivedAt.atZone(ZoneId.of("America/Sao_Paulo"));
        return zdt.toInstant().toEpochMilli();
    }

    public Long getStartProcessingAtMilli() {
        ZonedDateTime zdt = startProcessingAt.atZone(ZoneId.of("America/Sao_Paulo"));
        return zdt.toInstant().toEpochMilli();
    }

    public Long getFinishedProcessingAtMilli() {
        ZonedDateTime zdt = finishedProcessingAt.atZone(ZoneId.of("America/Sao_Paulo"));
        return zdt.toInstant().toEpochMilli();
    }
}
