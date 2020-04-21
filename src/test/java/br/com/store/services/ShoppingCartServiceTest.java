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
    private Discount absoluteDiscount2ofCategory1;
    private Discount absoluteDiscount3;
    private Discount relativeDiscount1;

    @Autowired
    private UtilTestService utilTestService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        this.customer = this.utilTestService.customer;
        this.category = this.utilTestService.category1;
        this.product1 = this.utilTestService.product1;
        this.product2 = this.utilTestService.product2;
        this.absoluteDiscount1 = this.utilTestService.absoluteDiscount1;
        this.absoluteDiscount2ofCategory1 = this.utilTestService.absoluteDiscount2ofCategory1;
        this.absoluteDiscount3 = this.utilTestService.absoluteDiscount3;
        this.relativeDiscount1 = this.utilTestService.relativeDiscount1;
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
     * Verifica se o desconto foi aplicado a todos produtos.
     */
    @Test
    public void addProductsAndAbsoluteDiscount1Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount1.getCode());
        assertEquals(product2.getPrice() + product1.getPrice(), shoppingCart.getCost());
        assertEquals(product2.getPrice() + product1.getPrice() - this.absoluteDiscount1.getDiscountRate(), shoppingCart.getComputedCost());
    }

    /**
     * Verifica se o desconto foi aplicado apenas aos produtos com a categoria "Categoria 1".
     */
    @Test
    public void addProductsAndAbsoluteDiscount2ofCategory1Test() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount2ofCategory1.getCode());
        assertEquals(product2.getPrice() + Math.max(0, product1.getPrice() - this.absoluteDiscount2ofCategory1.getDiscountRate()), shoppingCart.getComputedCost());
    }

    /**
     * Verifica se o desconto foi aplicado aos produtos e o valor do carrinho não ficou menor que zero.
     */
    @Test
    public void addProductsAndAbsoluteDiscount3() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount3.getCode());
        assertEquals(0, shoppingCart.getComputedCost());
    }

    @Test
    public void addProductsAndRelativeDiscount1() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product1.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product2.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.relativeDiscount1.getCode());
        assertEquals((product2.getPrice() + product1.getPrice()) * this.relativeDiscount1.getDiscountRate(), shoppingCart.getComputedCost());
    }
}