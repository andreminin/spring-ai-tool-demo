package org.lucentrix.demo.tool;

import org.lucentrix.demo.config.ProductDetails;
import org.lucentrix.demo.persistence.Product;
import org.lucentrix.demo.service.ProductService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lucentrix.demo.persistence.Product.NOT_FOUND_NAME;

public class AvailableProductsTool {
    private final ProductService productService;

    @Autowired
    public AvailableProductsTool(ProductService productService) {
        this.productService = productService;
    }

    @Tool(description = "Get available top products details ")
    public List<ProductDetails> getAvailableProductDetails() {
        List<Product> products = productService.getAvailableProductNames();
        if (products != null) {
            return products.stream().filter(Objects::nonNull).map(
                    product -> new ProductDetails(product.getName(), product.getPrice(), product.getQuantity())).collect(Collectors.toList());
        } else {
            return Collections.singletonList(new ProductDetails(NOT_FOUND_NAME, 0, 0));
        }
    }
}
