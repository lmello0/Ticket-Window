package br.com.lmello.dashboard.controller;

import br.com.lmello.dashboard.dto.Statistic;
import br.com.lmello.dashboard.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class DashboardController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getStatistics(@RequestParam("date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        LocalDateTime l = date.atTime(0,0);

        Statistic s = customerService.getStatistics(l);

        return ResponseEntity.ok(s);
    }
}
