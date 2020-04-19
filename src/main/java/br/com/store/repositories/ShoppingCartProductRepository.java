package br.com.store.repositories;

import br.com.store.model.ShoppingCartProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartProductRepository extends CrudRepository<ShoppingCartProduct, Long>,
        QueryByExampleExecutor<ShoppingCartProduct> {

    @Query("SELECT shoppingCartProduct FROM ShoppingCartProduct shoppingCartProduct")
    List<ShoppingCartProduct> findAll(Pageable pageable);
}