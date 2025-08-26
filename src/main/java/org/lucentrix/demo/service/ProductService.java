package org.lucentrix.demo.service;

import org.lucentrix.demo.persistence.Product;
import org.lucentrix.demo.persistence.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findProductByName(String name) {

        List<Product> exact = productRepository.findByNameIgnoringCase(name);
        if (!exact.isEmpty()) {
            return exact; // exact match found
        }

        // fallback to partial match ignoring case
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getAvailableProductNames() {
        return productRepository.findTop10ByNameIsNotNull();
    }

}
