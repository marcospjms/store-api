package br.com.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Category extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}
