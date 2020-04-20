package br.com.store.model;

import br.com.store.util.DateTimeDeserializer;
import br.com.store.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
public class Discount extends AbstractEntity {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String description;
    /**
     * Taxa de desconto. Se type for RELATIVE, deverá ser entre 0 e 1. Se for ABSOLUTE poderá ser qualquer valor.
     */
    private double discountRate;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Column(columnDefinition = "boolean default 'ALL'")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne
    private Category category;

    @Column(columnDefinition = "boolean default true")
    private boolean cumulative = true;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime start;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime end;

    private long counter;

    private long maxCounter;

    public void setDiscountRate(double discountRate) {
        if (this.type == null) {
            throw new RuntimeException("Taxa de desconto inválida: sem nenhum type definido");
        }
        if (this.type == DiscountType.RELATIVE && (discountRate < 0 || discountRate > 1)) {
            throw new RuntimeException("Taxa de desconto inválida: com type RELATIVE o valor deve ser entre 0 e 1");
        }
        if (!Arrays.asList(DiscountType.ABSOLUTE, DiscountType.RELATIVE).contains(this.type)) {
            throw new RuntimeException("Taxa de desconto inválida: type desconhecido");
        }
        this.discountRate = discountRate;
    }

    public double calculate(List<Product> products, PaymentType paymentType) {
        if (paymentType != this.paymentType && this.paymentType != PaymentType.ALL) {
            return 0.0;
        }
        return this.getValidProducts(products).stream().reduce(
                0.0,
                (subtotal, product) -> subtotal + product.getPrice(),
                Double::sum
        );
    }

    private List<Product> getValidProducts(List<Product> products) {
        return products.stream().filter(product -> this.isValidProduct(product)).collect(Collectors.toList());
    }

    private boolean isValidProduct(Product product) {
        return this.category == null || this.category.equals(product.getCategory());
    }

    public static List<Discount> getBestDiscountsByProducts(List<Discount> discounts, List<Product> products, PaymentType paymentType) {
        List<Discount> cumulativeDiscounts = getCumulativeDiscounts(discounts);
        List<Discount> nonCumulativeDiscounts = getNonCumulativeDiscounts(discounts);

        Double totalCumulativeDiscount = calcTotalDiscounts(cumulativeDiscounts, products, paymentType);
        Double totalNonCumulativeDiscount = calcTotalDiscounts(nonCumulativeDiscounts, products, paymentType);

        return totalCumulativeDiscount > totalNonCumulativeDiscount ? cumulativeDiscounts : nonCumulativeDiscounts;
    }

    private static List<Discount> getCumulativeDiscounts(List<Discount> discounts) {
        return discounts.stream().filter(discount -> discount.cumulative).collect(Collectors.toList());
    }

    private static List<Discount> getNonCumulativeDiscounts(List<Discount> discounts) {
        return discounts.stream().filter(discount -> !discount.cumulative).collect(Collectors.toList());
    }

    public static double calcTotalDiscounts(List<Discount> discounts, List<Product> products, PaymentType paymentType) {
        return discounts.stream().reduce(
                0.0,
                (subtotal, discount) -> subtotal + discount.calculate(products, paymentType),
                Double::sum
        );
    }
}
