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
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
public class Discount extends AbstractEntity {

    private String code;
    /**
     * Taxa de desconto. Se type for RELATIVE, deverá ser entre 0 e 1. Se for ABSOLUTE poderá ser qualquer valor.
     */
    private double discountRate;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentTypes;

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
            throw new RuntimeException("Impossível definir a taxa de desconto: sem nenhum type definido");
        }
        this.checkDiscountRate(discountRate);
        this.discountRate = discountRate;
    }

    public double calculate(List<Product> products, PaymentType paymentType) {
        return this.filterProducts(products).stream().reduce(
                0.0,
                (subtotal, product) -> subtotal + this.calcDiscountByProduct(product, paymentType),
                Double::sum
        );
    }

    private void checkDiscountRate(double discountRate) {
        switch (this.type) {
            case ABSOLUTE:
                break;
            case RELATIVE:
                if (discountRate < 0 || discountRate > 1) {
                    throw new RuntimeException("Impossível definir a taxa de desconto: type RELATIVE só " +
                            "permite valor entre 0 e 1");
                }
                break;
            default:
                throw new RuntimeException("Impossível definir a taxa de desconto: type desconhecido");
        }
    }

    private List<Product> filterProducts(List<Product> products) {
        if (this.category == null) {
            return products;
        }
        return products.stream().filter((product -> this.category.equals(product.getCategory()))).collect(Collectors.toList());
    }

    private double calcDiscountByProduct(Product product, PaymentType paymentType) {
        return product.getPrice();
    }
}
