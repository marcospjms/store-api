package br.com.store.repositories;

import br.com.store.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, QueryByExampleExecutor<Product> {

    @Query("SELECT product FROM Product product")
    List<Product> findAll(Pageable pageable);
}
