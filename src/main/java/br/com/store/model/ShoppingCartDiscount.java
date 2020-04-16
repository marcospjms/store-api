package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shopping_cart_discounts")
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartDiscount extends AbstractEntity {

    @ManyToOne
    private ShoppingCart shoppingCart;

    @ManyToOne
    private Discount discount;
}
