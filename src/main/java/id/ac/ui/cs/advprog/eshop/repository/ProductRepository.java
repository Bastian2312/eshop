package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public void deleteById(String productId) {
        productData.removeIf(product -> product.getProductId().equals(productId));
    }

    public Optional<Product> findById(String productId) {
        return productData.stream().filter(p -> p.getProductId().equals(productId)).findFirst();
    }

    public void update(Product updatedProduct) {
        findById(updatedProduct.getProductId()).ifPresent(existingProduct -> {
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductQuantity(updatedProduct.getProductQuantity());
        });
    }
}