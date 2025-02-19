package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void testCreate() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        Product createdProduct = productRepository.create(product);

        assertEquals(product, createdProduct);
        assertEquals("1", productRepository.findAll().next().getProductId());
    }

    @Test
    void testFindAllEmpty() {
        Iterator<Product> iterator = productRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllWithProducts() {
        Product product1 = new Product();
        product1.setProductId("1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);
        productRepository.create(product2);

        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next().getProductId());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next().getProductId());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindByIdExisting() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Product 1");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product foundProduct = productRepository.findById("1");
        assertNotNull(foundProduct);
        assertEquals("1", foundProduct.getProductId());
    }

    @Test
    void testFindByIdNonExisting() {
        ProductRepository productRepository = new ProductRepository();

        // Ensure no products exist
        assertNull(productRepository.findById("999"));

        // Add a product and check for a non-existing ID
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Existing Product");
        productRepository.create(product);

        assertNull(productRepository.findById("999")); // This should now hit `return null;`
    }

    @Test
    void testUpdateExistingProduct() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("1");
        updatedProduct.setProductName("New Name");
        updatedProduct.setProductQuantity(20);

        productRepository.update(updatedProduct);

        Product retrievedProduct = productRepository.findById("1");
        assertNotNull(retrievedProduct);
        assertEquals("New Name", retrievedProduct.getProductName());
        assertEquals(20, retrievedProduct.getProductQuantity());
    }

    @Test
    void testUpdateNonExistingProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("999");
        updatedProduct.setProductName("Non Existent");
        updatedProduct.setProductQuantity(5);

        productRepository.update(updatedProduct);

        assertNull(productRepository.findById("999"));
    }

    @Test
    void testUpdateOnEmptyRepository() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("1");
        updatedProduct.setProductName("New Name");
        updatedProduct.setProductQuantity(20);

        productRepository.update(updatedProduct);
        assertNull(productRepository.findById("1")); // Ensure it wasn't added
    }

    @Test
    void testDeleteExistingProduct() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.deleteById("1");

        assertNull(productRepository.findById("1"));
    }

    @Test
    void testDeleteNonExistingProduct() {
        productRepository.deleteById("999");  // Should not throw an error
        assertNull(productRepository.findById("999"));
    }

    @Test
    void testFindAllAfterDelete() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Product 1");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.deleteById("1");

        assertFalse(productRepository.findAll().hasNext()); // Should be empty
    }
}
