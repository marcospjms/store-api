package br.com.store.repositories;

import br.com.store.model.ShoppingCartDiscount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartDiscountRepository extends CrudRepository<ShoppingCartDiscount, Long>,
        QueryByExampleExecutor<ShoppingCartDiscount> {

    @Query("SELECT shoppingCartDiscount FROM ShoppingCart shoppingCartDiscount")
    List<ShoppingCartDiscount> findAll(Pageable pageable);
}
