package br.com.lmello.frontdoor.controller;

import br.com.lmello.common.customer.CustomerDTO;
import br.com.lmello.common.workshift.WorkHourChecker;
import br.com.lmello.frontdoor.dto.MessageDTO;
import br.com.lmello.frontdoor.kafka.CustomerProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CustomerController {

    private final CustomerProducer customerProducer;

    public CustomerController(CustomerProducer customerProducer) {
        this.customerProducer = customerProducer;
    }

    @PostMapping
    public ResponseEntity<MessageDTO> addToQueue(@RequestBody @Valid CustomerDTO customer) {
        if (!WorkHourChecker.isInWorkHour()) {
            return ResponseEntity.badRequest().body(new MessageDTO("You are not in work hour!"));
        }

        customerProducer.sendMessage(customer);

        return ResponseEntity.ok(new MessageDTO("You are in the queue, and must be attended in a few moments"));
    }
}
