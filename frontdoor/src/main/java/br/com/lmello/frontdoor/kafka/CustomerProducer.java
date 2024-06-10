package br.com.lmello.frontdoor.kafka;

import br.com.lmello.common.customer.CustomerDTO;
import br.com.lmello.common.customer.UnprocessedCustomerDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CustomerProducer {
    private final Logger logger = Logger.getLogger(CustomerProducer.class.getName());

    private final KafkaTemplate<String, UnprocessedCustomerDTO> kafkaTemplate;

    public CustomerProducer(KafkaTemplate<String, UnprocessedCustomerDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CustomerDTO customer) {
        UnprocessedCustomerDTO unprocessedCustomerDTO = new UnprocessedCustomerDTO(customer);

        kafkaTemplate.send("unprocessed_customers", unprocessedCustomerDTO);
        logger.info("Sent customer " + unprocessedCustomerDTO);
    }
}
