package org.lucentrix.demo.config;

import org.lucentrix.demo.persistence.Product;
import org.lucentrix.demo.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@AllArgsConstructor
@Configuration
public class DemoConfiguration {

    private final ProductService productService;

    @Bean
    @Description("Get product details by name")
    public Function<ProductName, List<ProductDetails>> getProductDetails() {
        return productName -> {
            List<Product> products = productService.findProductByName(productName.name());

            if (!products.isEmpty()) {
                return products.stream().filter(Objects::nonNull)
                        .map(product -> new ProductDetails(product.getName(), product.getPrice(), product.getQuantity()))
                        .collect(Collectors.toList());
            } else {
                return Collections.singletonList(new ProductDetails( "NOT_FOUND", 0, 0));
            }
        };
    }


}
