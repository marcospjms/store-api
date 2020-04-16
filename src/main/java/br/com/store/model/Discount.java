package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
public class Discount extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private PaymentType paymentTypes;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @ManyToOne
    private Category category;

    private boolean cumulative;

    private double discountRate;


    public double calculate(List<Product> products, PaymentType paymentType) {
        return this.filterProducts(products).stream().reduce(
                0.0,
                (subtotal, product) -> subtotal + this.getDiscount(product, paymentType),
                Double::sum
        );
    }

    private List<Product> filterProducts(List<Product> products) {
        if (this.category == null) {
            return products;
        }
        return products.stream().filter((product -> this.category.equals(product.getCategory()))).collect(Collectors.toList());
    }

    private double getDiscount(Product product, PaymentType paymentType) {
        return product.getPrice();
    }
}
