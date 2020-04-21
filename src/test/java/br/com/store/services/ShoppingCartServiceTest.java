package br.com.store.services;


import br.com.store.model.Category;
import br.com.store.model.Product;
import br.com.store.model.ShoppingCart;
import br.com.store.model.ShoppingCartProduct;
import br.com.store.model.auth.StoreUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;


@SpringBootTest
@Transactional
public class ShoppingCartServiceTest {

    private StoreUser customer;
    private Category category;
    private Product product1;
    private Product product2;

    @Autowired
    private UtilTestService utilTestService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        this.customer = this.utilTestService.customer;
        this.category = this.utilTestService.category;
        this.product1 = this.utilTestService.product1;
        this.product2 = this.utilTestService.product2;
    }

    @Test
    public void addProduct1Test() {
        ShoppingCart shoppingCart = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        assertEquals(product1.getPrice(), shoppingCart.getCost());
    }

    @Test
    public void addProduct2Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        assertEquals(product2.getPrice(), shoppingCart.getCost());
    }

    @Test
    public void addProductsTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        assertEquals(product1.getPrice() + product2.getPrice(), shoppingCart.getCost());
    }
}