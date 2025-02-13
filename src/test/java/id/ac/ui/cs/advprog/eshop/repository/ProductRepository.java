package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testUpdateProductPositiveScenario() {
        Product product = new Product();
        product.setProductId("123");
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("123");
        updatedProduct.setProductName("New Name");
        updatedProduct.setProductQuantity(20);
        productRepository.update(updatedProduct);

        Product retrievedProduct = productRepository.findById("123");
        assertNotNull(retrievedProduct);
        assertEquals("New Name", retrievedProduct.getProductName());
        assertEquals(20, retrievedProduct.getProductQuantity());
    }

    @Test
    void testUpdateProductNegativeScenario() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("999"); // Non-existent ID
        updatedProduct.setProductName("Non Existent Product");
        updatedProduct.setProductQuantity(5);
        productRepository.update(updatedProduct);

        Product retrievedProduct = productRepository.findById("999");
        assertNull(retrievedProduct);
    }

    @Test
    void testDeleteProductPositiveScenario() {
        Product product = new Product();
        product.setProductId("456");
        product.setProductName("Test Product");
        product.setProductQuantity(30);
        productRepository.create(product);

        productRepository.deleteById("456");
        assertNull(productRepository.findById("456"));
    }

    @Test
    void testDeleteProductNegativeScenario() {
        productRepository.deleteById("999"); // Non-existent ID
        assertNull(productRepository.findById("999"));
    }
}
