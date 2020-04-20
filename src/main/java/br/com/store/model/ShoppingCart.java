package br.com.store.model;

import br.com.store.model.auth.StoreUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private StoreUser storeUser;

    public void calc(List<Product> products, List<Discount> discounts) {
        List<Discount> bestDiscounts = Discount.getBestDiscountsByProducts(discounts, products, this.paymentType);
        this.discount = Discount.calcTotalDiscounts(bestDiscounts, products, this.paymentType);
        this.cost = Product.calcTotalCost(products);
    }
}
