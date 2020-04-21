package br.com.store.model;

import br.com.store.util.DateTimeDeserializer;
import br.com.store.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
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
        if (discountRate < 0) {
            throw new RuntimeException("Taxa de desconto inválida: não pode ser menor que zero");
        }
        if (this.type == null) {
            throw new RuntimeException("Taxa de desconto inválida: sem nenhum type definido");
        }
        if (this.type == DiscountType.RELATIVE && discountRate > 1) {
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
        double totalPrice = Product.sumPrices(this.getValidProducts(products));
        double discount = this.type == DiscountType.RELATIVE ? totalPrice * (1 - this.discountRate) : discountRate;
        double totalCost= totalPrice - discount;

        return  totalCost > 0 ? discount : totalPrice;
    }

    private List<Product> getValidProducts(List<Product> products) {
        return products.stream().filter(product -> this.isValidProduct(product)).collect(Collectors.toList());
    }

    private boolean isValidProduct(Product product) {
        return this.category == null || this.category.equals(product.getCategory());
    }
}
