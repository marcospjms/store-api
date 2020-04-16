package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_discounts")
@Getter
@Setter
@NoArgsConstructor
public class ProductDiscount extends AbstractEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private Discount discount;
}
