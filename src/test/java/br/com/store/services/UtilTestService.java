package br.com.store.services;


import br.com.store.model.*;
import br.com.store.model.auth.StoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UtilTestService {

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    public StoreUser customer;
    public Category category;
    public Product product1;
    public Product product2;
    public Discount absoluteDiscount;

    @PostConstruct
    public void setup() {
        System.out.println("executing...");
        this.customer = this.storeUserService.createUser(
                StoreUser.builder()
                        .name("Marcos Paulo")
                        .email("marcospjms@gmail.com")
                        .username("marcospjms")
                        .password("123456")
                        .build(),
                false
        );

        this.category = this.categoryService.save(
                Category.builder()
                        .name("Categoria 1")
                        .description("Descrição da categoria 1")
                        .build()
        );

        this.product1 = this.productService.save(
                Product.builder()
                        .name("Produto 1")
                        .description("Descrição do produto 1")
                        .price(20)
                        .category(this.category)
                        .build()
        );

        this.product2 = this.productService.save(
                Product.builder()
                        .name("Produto 2")
                        .description("Descrição do produto 2")
                        .price(100)
                        .build()
        );

        this.absoluteDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto cumulativo 1")
                        .code("cupomdoido")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(50)
                        .category(this.category)
                        .build()
        );
    }
}