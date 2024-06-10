package br.com.lmello.dashboard.repository;

import br.com.lmello.dashboard.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE date_trunc('day', c.finishedProcessingAt) = :date")
    List<Customer> findCustomersByDate(LocalDateTime date);
}
