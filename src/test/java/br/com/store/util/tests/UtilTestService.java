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

    public Category category;

    public Product categorizedProduct;
    public Product product;

    public Discount absoluteDiscount;
    public Discount bigAbsoluteDiscount;
    public Discount categorizedAbsoluteDiscount;

    public Discount relativeDiscount;
    public Discount otherRelativeDiscount;
    public Discount categorizedRelativeDiscount;

    public Discount nonCumulativeDiscount;
    public Discount smallerNonCumulativeDiscount;

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

        this.category = this.categoryService.save(
                Category.builder()
                        .name("Categoria")
                        .description("Descrição da categoria")
                        .build()
        );

        this.categorizedProduct = this.productService.save(
                Product.builder()
                        .name("Produto 1")
                        .description("Descrição do produto 1")
                        .price(50)
                        .category(this.category)
                        .build()
        );

        this.product = this.productService.save(
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
                        .discountRate(10)
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
                        .discountRate(10)
                        .cumulative(true)
                        .category(this.category)
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

        this.categorizedRelativeDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto relativo categorizado")
                        .code("cupomrelativocategorizado")
                        .category(this.category)
                        .type(DiscountType.RELATIVE)
                        .discountRate(0.1)
                        .cumulative(true)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.nonCumulativeDiscount = this.discountService.save(
                Discount.builder()
                        .description("Desconto não cumulativo")
                        .code("cupomnaocumulativo")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(100)
                        .cumulative(false)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );

        this.smallerNonCumulativeDiscount = this.discountService.save(
                Discount.builder()
                        .description("Outro desconto não cumulativo")
                        .code("cupomnaocumulativooutro")
                        .type(DiscountType.ABSOLUTE)
                        .discountRate(20)
                        .cumulative(false)
                        .start(DateTime.now().minusDays(1))
                        .end(DateTime.now().plusDays(1))
                        .build()
        );
    }
}