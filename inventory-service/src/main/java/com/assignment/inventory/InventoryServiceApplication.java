package com.assignment.inventory;

import com.assignment.inventory.entity.InventoryBatch;
import com.assignment.inventory.entity.Product;
import com.assignment.inventory.repository.InventoryBatchRepository;
import com.assignment.inventory.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

/**
 * @author hemant-nitm
 */
@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    //  some sample products + batches for manual testing
    @Bean
    CommandLineRunner loadData(ProductRepository productRepo, InventoryBatchRepository batchRepo) {
        return args -> {
            if (productRepo.count() == 0) {
                Product p1 = productRepo.save(Product.builder().name("Paracetamol").build());
                Product p2 = productRepo.save(Product.builder().name("Aspirin").build());

                batchRepo.save(InventoryBatch.builder()
                        .product(p1)
                        .quantity(100)
                        .expiryDate(LocalDate.now().plusDays(30))
                        .build());

                batchRepo.save(InventoryBatch.builder()
                        .product(p1)
                        .quantity(50)
                        .expiryDate(LocalDate.now().plusDays(10))
                        .build());

                batchRepo.save(InventoryBatch.builder()
                        .product(p2)
                        .quantity(200)
                        .expiryDate(LocalDate.now().plusDays(90))
                        .build());
            }
        };
    }
}
