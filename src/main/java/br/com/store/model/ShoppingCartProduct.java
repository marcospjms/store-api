package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shopping_cart_products")
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartProduct extends AbstractEntity {

    @ManyToOne
    private ShoppingCart shoppingCart;

    @ManyToOne
    private Product product;
}
