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

    private double cost;

    private double discount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(optional = false)
    private StoreUser storeUser;
}
