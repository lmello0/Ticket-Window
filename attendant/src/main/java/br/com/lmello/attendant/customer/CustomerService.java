package br.com.lmello.attendant.customer;

import br.com.lmello.common.Constants;
import br.com.lmello.common.customer.ProcessedCustomerDTO;
import br.com.lmello.common.customer.UnprocessedCustomerDTO;
import br.com.lmello.common.workshift.WorkHourChecker;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class CustomerService {

    private final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private final KafkaTemplate<String, ProcessedCustomerDTO> kafkaTemplate;

    private final Random rand = new Random();

    public CustomerService(KafkaTemplate<String, ProcessedCustomerDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "unprocessed_customers", groupId = "attendant-group", containerFactory = "listenerContainerFactory")
    public void process(UnprocessedCustomerDTO customer) throws InterruptedException {
        ProcessedCustomerDTO processedCustomerDTO = processCustomer(customer);

        sendStatistic(processedCustomerDTO);
        logger.info("Finished processing " + processedCustomerDTO.name() + " - " + processedCustomerDTO.operation());
    }

    public ProcessedCustomerDTO processCustomer(UnprocessedCustomerDTO customer) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();

        int chosenNumber = rand.nextInt(0, 99);
        for (int i = 0; i < customer.operation().getOperationTime() / Constants.SECOND_IN_MS; i++) {
            int guess = rand.nextInt(0, 99);

            if (guess == chosenNumber) {
                break;
            }

            Thread.sleep(Constants.SECOND_IN_MS);
        }

        LocalDateTime end = LocalDateTime.now();

        return new ProcessedCustomerDTO(
                customer.name(),
                customer.operation(),
                customer.arrivedAt(),
                start,
                end,
                WorkHourChecker.isInWorkHour()
        );
    }

    public void sendStatistic(ProcessedCustomerDTO processedCustomerDTO) {
        this.kafkaTemplate.send("processed_customers", processedCustomerDTO);
    }
}
