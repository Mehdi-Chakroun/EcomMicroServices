package org.sid.orderservice;

import org.sid.orderservice.entities.Order;
import org.sid.orderservice.entities.ProductItem;
import org.sid.orderservice.enums.OrderStatus;
import org.sid.orderservice.model.Customer;
import org.sid.orderservice.model.Product;
import org.sid.orderservice.repository.OrderRepository;
import org.sid.orderservice.repository.ProductItemRepository;
import org.sid.orderservice.services.CustomerRestClientService;
import org.sid.orderservice.services.InventoryRestClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(OrderRepository orderRepository,
							ProductItemRepository productItemRepository,
							CustomerRestClientService customerRestClientService,
							InventoryRestClientService inventoryRestClientService) {
		return args -> {
			List<Customer> customers = customerRestClientService.findAllCustomers().getContent().stream().toList();
			List<Product> products = inventoryRestClientService.findAllProducts().getContent().stream().toList();
			Long customerId = 1L;
			Random random = new Random();
			Customer customer = customerRestClientService.findCustomerById(customerId);
			for (int i = 0; i < 20; i++) {
				Order order = Order.builder()
						.customerId(customers.get(random.nextInt(customers.size())).getId())
						.customer(customer)
						.status(Math.random() > 0.5 ? OrderStatus.PENDING : OrderStatus.CREATED)
						.date(new Date())
						.build();
				Order savedOrder = orderRepository.save(order);
				for(int j = 0; j < products.size(); j++) {
					if(Math.random() > 0.7) {
						ProductItem productItem = ProductItem.builder()
								.order(savedOrder)
								.price(products.get(j).getPrice())
								.quantity(random.nextInt(3) + 1)
								.productId(products.get(j).getId())
								.build();
						productItemRepository.save(productItem);
					}
				}
			}
		};
	}

}
