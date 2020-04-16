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

    @OneToOne
    private StoreUser storeUser;

    private double totalValue;

    private double totalDiscount;
}
