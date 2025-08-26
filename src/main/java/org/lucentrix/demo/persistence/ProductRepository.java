package org.lucentrix.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Exact match
    List<Product> findByNameIgnoringCase(String name);

    // Partial match ignoring case
    List<Product> findByNameContainingIgnoreCase(String text);

    List<Product> findTop10ByNameIsNotNull();
}
