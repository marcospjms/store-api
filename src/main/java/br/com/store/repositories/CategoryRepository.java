package br.com.store.repositories;

import br.com.store.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>, QueryByExampleExecutor<Category> {

    @Query("SELECT category FROM Category category")
    List<Category> findAll(Pageable pageable);
}
