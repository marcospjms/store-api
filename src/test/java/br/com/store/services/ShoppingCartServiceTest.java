package br.com.store.services;


import br.com.store.model.Category;
import br.com.store.model.Discount;
import br.com.store.model.Product;
import br.com.store.model.ShoppingCart;
import br.com.store.model.auth.StoreUser;
import br.com.store.util.tests.UtilTestService;
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
    private Discount absoluteDiscount1;
    private Discount absoluteDiscount2;

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
        this.absoluteDiscount1 = this.utilTestService.absoluteDiscount1;
        this.absoluteDiscount2 = this.utilTestService.absoluteDiscount2;
    }

    /**
     * Verifica se o product1 foi adicionado
     */
    @Test
    public void addProduct1Test() {
        ShoppingCart shoppingCart = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        assertEquals(product1.getPrice(), shoppingCart.getCost());
    }

    /**
     * Verifica se o product2 foi adicionado
     */
    @Test
    public void addProduct2Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        assertEquals(product2.getPrice(), shoppingCart.getCost());
    }

    /**
     * Verifica se os produtos foram adicionados
     */
    @Test
    public void addProductsTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        assertEquals(product1.getPrice() + product2.getPrice(), shoppingCart.getCost());
    }

    /**
     * Verifica se o desconto foi adicionado ao carrinho. Lembrando que o discount1 deve ser aplicado a aqueles com category1
     * definida
     */
    @Test
    public void addProductsAndAbsoluteDiscount1Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount1.getCode());
        assertEquals(product2.getPrice() + Math.max(0, product1.getPrice() - this.absoluteDiscount1.getDiscountRate()), shoppingCart.getComputedCost());
    }


    /**
     * Verifica se o desconto foi adicionado ao carrinho. Lembrando que o discount1 deve ser aplicado a aqueles com category1
     * definida
     */
    @Test
    public void addProductsAndAbsoluteDiscount2Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount2.getCode());
        assertEquals(product2.getPrice() + product1.getPrice(), shoppingCart.getCost());
        assertEquals(product2.getPrice() + product1.getPrice() - this.absoluteDiscount2.getDiscountRate(), shoppingCart.getComputedCost());
    }
}