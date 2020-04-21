package br.com.store.util.tests;


import br.com.store.model.*;
import br.com.store.model.auth.StoreUser;
import br.com.store.services.*;
import org.joda.time.DateTime;
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
    public Category category1;
    public Product product1;
    public Product product2;
    public Discount absoluteDiscount;
    public Discount bigAbsoluteDiscount;
    public Discount categorizedAbsoluteDiscount;

    public Discount relativeDiscount;
    public Discount otherRelativeDiscount;

    @PostConstruct
    public void setup() {
        this.customer = this.storeUserService.createUser(
                StoreUser.builder()
                        .name("Marcos Paulo")
                        .email("marcospjms@gmail.com")
                        .username("marcospjms")
                        .password("123456")
                        .build(),
                false
        );

        this.category1 = this.categoryService.save(
                Category.builder()
                        .name("Categoria 1")
                        .description("Descrição da categoria 1")
                        .build()
        );

        this.product1 = this.productService.save(
                Product.builder()
                        .name("Produto 1")
                        .description("Descrição do produto 1")
                        .price(50)
                        .category(this.category1)
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
                        .description("Desconto abosulto")
                        .code("cupomabsoluto")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(50)
                        .cumulative(true)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.categorizedAbsoluteDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto abosulto categorizado")
                        .code("cupomabsolutocategorizado")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(50)
                        .cumulative(true)
                        .category(this.category1)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.bigAbsoluteDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto abosulto com grande valor")
                        .code("cupomabsolutogrande")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(Double.MAX_VALUE)
                        .cumulative(true)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.relativeDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto relativo")
                        .code("cupomrelativo")
                        .type(DiscountType.RELATIVE)
                        .discountRate(0.5)
                        .cumulative(true)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.otherRelativeDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto relativo alternativo")
                        .code("cupomrelativooutro")
                        .type(DiscountType.RELATIVE)
                        .discountRate(0.2)
                        .cumulative(true)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );
    }
}