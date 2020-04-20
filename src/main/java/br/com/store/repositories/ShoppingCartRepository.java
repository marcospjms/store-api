package br.com.store.repositories;

import br.com.store.model.ShoppingCart;
import br.com.store.model.ShoppingCartDiscount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>, QueryByExampleExecutor<ShoppingCart> {

    @Query("SELECT shoppingCart FROM ShoppingCart shoppingCart")
    List<ShoppingCart> findAll(Pageable pageable);

    @Query("SELECT shoppingCart FROM ShoppingCart shoppingCart " +
            "LEFT OUTER JOIN shoppingCart.storeUser " +
            "WHERE shoppingCart.storeUser.username= :username")
    List<ShoppingCart> findByUsername(@Param("username") String username);
}
