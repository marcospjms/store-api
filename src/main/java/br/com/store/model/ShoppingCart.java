package br.com.store.model;

import br.com.store.model.auth.StoreUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart extends AbstractEntity {

    private double totalValue;

    private double totalDiscount;

    @OneToOne(cascade = CascadeType.ALL)
    private StoreUser storeUser;
}
