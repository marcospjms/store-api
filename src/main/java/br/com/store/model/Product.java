package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractEntity {

    @ManyToOne
    private Category category;

    private double price;

    private double totalDiscount;
}
