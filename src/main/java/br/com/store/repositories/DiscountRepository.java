package br.com.store.repositories;

import br.com.store.model.Discount;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long>, QueryByExampleExecutor<Discount> {

    @Query("SELECT discount FROM Discount discount")
    List<Discount> findAll(Pageable pageable);

    @Query("SELECT discount FROM Discount discount WHERE discount.code = :code " +
            "AND discount.start <= :data AND discount.end >= :data")
    List<Discount> findByCodeAndDate(@Param("code") String code, @Param("data") DateTime data);
}
