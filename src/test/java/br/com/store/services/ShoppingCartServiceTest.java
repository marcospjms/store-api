package br.com.store.services;


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
    private Product categorizedProduct;
    private Product product;
    private Discount absoluteDiscount;
    private Discount categorizedAbsoluteDiscount;
    private Discount bigAbsoluteDiscount;
    private Discount relativeDiscount;
    private Discount otherRelativeDiscount;
    private Discount categorizedRelativeDiscount;

    @Autowired
    private UtilTestService utilTestService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        this.customer = this.utilTestService.customer;
        this.categorizedProduct = this.utilTestService.categorizedProduct;
        this.product = this.utilTestService.product;
        this.absoluteDiscount = this.utilTestService.absoluteDiscount;
        this.bigAbsoluteDiscount = this.utilTestService.bigAbsoluteDiscount;
        this.categorizedAbsoluteDiscount = this.utilTestService.categorizedAbsoluteDiscount;
        this.relativeDiscount = this.utilTestService.relativeDiscount;
        this.otherRelativeDiscount = this.utilTestService.otherRelativeDiscount;
        this.categorizedRelativeDiscount = this.utilTestService.categorizedRelativeDiscount;
    }

    /**
     * Verifica se o categorizedProduct foi adicionado
     */
    @Test
    public void addCategorizedProductTest() {
        ShoppingCart shoppingCart = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        assertEquals(categorizedProduct.getPrice(), shoppingCart.getCost());
    }

    /**
     * Verifica se o product foi adicionado
     */
    @Test
    public void addProductTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        assertEquals(product.getPrice(), shoppingCart.getCost());
        assertEquals(product.getPrice(), shoppingCart.getComputedCost());
    }

    /**
     * Verifica se os produtos foram adicionados
     */
    @Test
    public void addProductsTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        assertEquals(categorizedProduct.getPrice() + product.getPrice(), shoppingCart.getCost());
        assertEquals(categorizedProduct.getPrice() + product.getPrice(), shoppingCart.getComputedCost());
    }


    /**
     * Verifica se o desconto foi aplicado a todos produtos.
     */
    @Test
    public void addAbsoluteDiscountTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount.getCode());
        assertEquals(product.getPrice() + categorizedProduct.getPrice() - this.absoluteDiscount.getDiscountRate(), shoppingCart.getComputedCost());
    }

    /**
     * Verifica se o desconto foi aplicado aos produtos e o valor do carrinho não ficou menor que zero.
     */
    @Test
    public void addBigAbsoluteDiscountTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.bigAbsoluteDiscount.getCode());
        assertEquals(0, shoppingCart.getComputedCost());
    }

    /**
     * Verifica se o desconto foi aplicado apenas aos produtos com a categoria "Categoria 1".
     */
    @Test
    public void addCategorizedAbsoluteDiscountTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.categorizedAbsoluteDiscount.getCode());
        double expected = product.getPrice() + Math.max(0, categorizedProduct.getPrice() - this.categorizedAbsoluteDiscount.getDiscountRate());
        assertEquals(expected, shoppingCart.getComputedCost());
    }

    @Test
    public void addRelativeDiscountTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.relativeDiscount.getCode());
        double expected = (product.getPrice() + categorizedProduct.getPrice()) * (1 - this.relativeDiscount.getDiscountRate());
        assertEquals(expected, shoppingCart.getComputedCost());
    }

    @Test
    public void addRelativeDiscountsTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.relativeDiscount.getCode());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.otherRelativeDiscount.getCode());
        double expected = (product.getPrice() + categorizedProduct.getPrice()) * (1 - (this.otherRelativeDiscount.getDiscountRate() + this.relativeDiscount.getDiscountRate()));
        assertEquals(expected, shoppingCart.getComputedCost(), 0.00000001);
    }

    @Test
    public void addRelativeAndAbsoluteDiscountsTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.relativeDiscount.getCode());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.absoluteDiscount.getCode());
        double expected = (product.getPrice() + categorizedProduct.getPrice()) * this.relativeDiscount.getDiscountRate() - this.absoluteDiscount.getDiscountRate();
        assertEquals(expected, shoppingCart.getComputedCost());
    }

    @Test
    public void addCategorizedRelativeDiscountTest() {
        ShoppingCart shoppingCart  = this.shoppingCartService.addProduct(this.customer.getUsername(), this.categorizedProduct.getId());
        this.shoppingCartService.addProduct(this.customer.getUsername(), this.product.getId());
        this.shoppingCartService.addDiscount(this.customer.getUsername(), this.categorizedRelativeDiscount.getCode());
        double expected = product.getPrice() + categorizedProduct.getPrice() * (1 - this.categorizedRelativeDiscount.getDiscountRate());
        assertEquals(expected, shoppingCart.getComputedCost());
    }
}