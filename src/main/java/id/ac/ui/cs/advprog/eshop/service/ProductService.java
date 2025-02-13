package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    void deleteById(String productId);
    Product create(Product product);
    List<Product> findAll();
    Optional<Product> findById(String productId);
    void update(Product product);
}