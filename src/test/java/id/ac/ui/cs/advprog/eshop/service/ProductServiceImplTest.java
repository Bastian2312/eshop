package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setProductId("123");
        product1.setProductName("Laptop");
        product1.setProductQuantity(10);

        product2 = new Product();
        product2.setProductId("456");
        product2.setProductName("Phone");
        product2.setProductQuantity(5);
    }

    @Test
    void testCreate() {
        when(productRepository.create(product1)).thenReturn(product1);
        Product createdProduct = productService.create(product1);
        assertNotNull(createdProduct);
        assertEquals("123", createdProduct.getProductId());
        verify(productRepository, times(1)).create(product1);
    }

    @Test
    void testFindAll() {
        Iterator<Product> productIterator = Arrays.asList(product1, product2).iterator();
        when(productRepository.findAll()).thenReturn(productIterator);

        List<Product> products = productService.findAll();
        assertEquals(2, products.size());
        assertEquals("123", products.get(0).getProductId());
        assertEquals("456", products.get(1).getProductId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExisting() {
        when(productRepository.findById("123")).thenReturn(product1);
        Product foundProduct = productService.findById("123");
        assertNotNull(foundProduct);
        assertEquals("123", foundProduct.getProductId());
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void testFindByIdNonExisting() {
        when(productRepository.findById("999")).thenReturn(null);
        Product foundProduct = productService.findById("999");
        assertNull(foundProduct);
        verify(productRepository, times(1)).findById("999");
    }

    @Test
    void testUpdate() {
        doNothing().when(productRepository).update(product1);
        productService.update(product1);
        verify(productRepository, times(1)).update(product1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(productRepository).deleteById("123");
        productService.deleteById("123");
        verify(productRepository, times(1)).deleteById("123");
    }
}