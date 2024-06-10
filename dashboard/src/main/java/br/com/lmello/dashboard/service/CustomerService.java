package br.com.lmello.dashboard.service;

import br.com.lmello.common.Constants;
import br.com.lmello.common.customer.Operation;
import br.com.lmello.common.customer.ProcessedCustomerDTO;
import br.com.lmello.dashboard.dto.Statistic;
import br.com.lmello.dashboard.model.Customer;
import br.com.lmello.dashboard.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Service
@Transactional
public class CustomerService {

    private final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private final CustomerRepository customerRepository;

    private final DecimalFormat df;

    public CustomerService(CustomerRepository customerRepository) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

        this.df = new DecimalFormat("0.00", symbols);
        this.df.setRoundingMode(RoundingMode.UP);

        this.customerRepository = customerRepository;
    }

    @KafkaListener(topics = "processed_customers", groupId = "dashboard-group", containerFactory = "listenerContainerFactory")
    public void process(ProcessedCustomerDTO customer) {
        Customer c = new Customer(customer);

        customerRepository.save(c);
        logger.info("Customer " + c.getName() + " saved");
    }

    public Statistic getStatistics(LocalDateTime date) {
        List<Customer> customers = customerRepository.findCustomersByDate(date);

        int totalCustomers = customers.size();

        int totalWithdrawalsOp = getTotalCustomerByOperation(customers, Operation.WITHDRAW);
        int totalDepositOp = getTotalCustomerByOperation(customers, Operation.DEPOSIT);
        int totalPaymentOp = getTotalCustomerByOperation(customers, Operation.PAYMENT);

        double meanAwaitTime = getMeanAwaitTime(customers);
        double meanProcessingTime = getMeanProcessingTime(customers);

        double extraTimeProcess = getTotalExtraTime(customers);

        return new Statistic(
                date,
                totalCustomers,
                totalWithdrawalsOp,
                totalDepositOp,
                totalPaymentOp,
                Double.parseDouble(df.format(meanAwaitTime)),
                Double.parseDouble(df.format(meanProcessingTime)),
                Double.parseDouble(df.format(extraTimeProcess))
        );
    }

    private int getTotalCustomerByOperation(List<Customer> customers, Operation operation) {
        return (int) customers
                .stream()
                .filter(c -> c.getOperation() == operation)
                .count();
    }

    private Double getMeanAwaitTime(List<Customer> customers) {
        double totalAwaitTime = customers
                .stream()
                .mapToDouble(c -> c.getStartProcessingAtMilli() - c.getArrivedAtMilli())
                .sum();

        return (totalAwaitTime / customers.size()) / Constants.SECOND_IN_MS;
    }

    private Double getMeanProcessingTime(List<Customer> customers) {
        double totalProcessingTime = customers
                .stream()
                .mapToDouble(c -> c.getFinishedProcessingAtMilli() - c.getStartProcessingAtMilli())
                .sum();

        return (totalProcessingTime / customers.size()) / Constants.SECOND_IN_MS;
    }

    private Double getTotalExtraTime(List<Customer> customers) {
        double totalExtraTime = customers
                .stream()
                .filter(c -> !c.isProcessedInWorkHour())
                .mapToDouble(c -> c.getFinishedProcessingAtMilli() - c.getArrivedAtMilli())
                .sum();

        return totalExtraTime / Constants.SECOND_IN_MS;
    }
}
