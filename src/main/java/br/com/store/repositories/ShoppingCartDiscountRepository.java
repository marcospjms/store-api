package br.com.store.repositories;

import br.com.store.model.ShoppingCartDiscount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartDiscountRepository extends CrudRepository<ShoppingCartDiscount, Long>,
        QueryByExampleExecutor<ShoppingCartDiscount> {

    @Query("SELECT shoppingCartDiscount FROM ShoppingCartDiscount shoppingCartDiscount")
    List<ShoppingCartDiscount> findAll(Pageable pageable);

    @Query("SELECT shoppingCartDiscount FROM ShoppingCartDiscount shoppingCartDiscount " +
            "LEFT OUTER JOIN shoppingCartDiscount.shoppingCart " +
            "LEFT OUTER JOIN shoppingCartDiscount.shoppingCart.storeUser " +
            "WHERE shoppingCartDiscount.shoppingCart.storeUser.username = :username")
    List<ShoppingCartDiscount> findByUsername(Pageable pageable, @Param("username") String username);

    @Query("SELECT shoppingCartDiscount FROM ShoppingCartDiscount shoppingCartDiscount " +
            "LEFT OUTER JOIN shoppingCartDiscount.shoppingCart " +
            "LEFT OUTER JOIN shoppingCartDiscount.shoppingCart.storeUser " +
            "WHERE shoppingCartDiscount.shoppingCart.storeUser.username = :username")
    List<ShoppingCartDiscount> findByUsername(@Param("username") String username);
}
