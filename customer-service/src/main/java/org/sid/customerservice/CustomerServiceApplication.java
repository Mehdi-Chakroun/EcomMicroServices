package org.sid.customerservice;

import lombok.Builder;
import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repo.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository) {
		return args -> {
			customerRepository.saveAll(List.of(
					Customer.builder().name("Customer 1").email("customer1@mail.com").build(),
					Customer.builder().name("Customer 2").email("customer2@mail.com").build(),
					Customer.builder().name("Customer 3").email("customer3@mail.com").build()
					));
			customerRepository.findAll().forEach(System.out::println);
		};
	}
}
