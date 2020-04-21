package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Product extends AbstractEntity {

    @ManyToOne
    private Category category;

    private double price;

    private double totalDiscount;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String specification;

    public static double sumPrices(List<Product> products) {
        return products.stream().reduce(0.0, (subtotal, product) -> subtotal + product.getPrice(), Double::sum);
    }
}
