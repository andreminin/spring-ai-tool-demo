package org.lucentrix.demo.config;

import org.lucentrix.demo.persistence.Product;
import org.lucentrix.demo.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Value("${app.init-sample-data:false}")
    private boolean initSampleData;

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (!initSampleData) return;

            // Only initialize if table is empty
            if (repository.count() == 0) {
                List<Product> sampleProducts = List.of(
                        createProduct("Apple iPhone 15", 1200, 5),
                        createProduct("Samsung Galaxy S23", 1000, 10),
                        createProduct("Google Pixel 8", 900, 7),
                        createProduct("LG UltraGear Monitor", 400, 12),
                        createProduct("Lenovo ThinkPad X1", 1500, 8),
                        createProduct("Dell XPS 13", 1300, 6),
                        createProduct("HP Spectre x360", 1250, 5),
                        createProduct("Asus ROG Strix", 1400, 9),
                        createProduct("Microsoft Surface Pro 9", 1100, 4),
                        createProduct("Sony WH-1000XM5", 350, 15),

                        // Short names
                        createProduct("LG TV 55\"", 700, 10),
                        createProduct("Samsung Fridge 300L", 1200, 6),
                        createProduct("Lenovo IdeaPad 5", 650, 7),
                        createProduct("LG Phone K51", 200, 20),
                        createProduct("Samsung Galaxy A14", 250, 18),
                        createProduct("Lenovo Tab M10", 180, 12),
                        createProduct("LG Soundbar SL5Y", 150, 8),
                        createProduct("Samsung Galaxy Buds", 130, 16),
                        createProduct("Lenovo Legion 5", 1200, 5),
                        createProduct("LG Refrigerator 400L", 1000, 3)
                );

                repository.saveAll(sampleProducts);
                System.out.println("Sample products initialized");
            } else {
                System.out.println("Products table already has data, skipping initialization");
            }
        };
    }

    private Product createProduct(String name, int price, int quantity) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        return p;
    }
}
