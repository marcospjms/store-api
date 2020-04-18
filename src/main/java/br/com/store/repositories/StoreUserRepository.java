package br.com.store.repositories;

import br.com.store.model.auth.StoreUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreUserRepository extends CrudRepository<StoreUser, Long>, QueryByExampleExecutor<StoreUser> {

    @Query("SELECT storeUsers FROM StoreUser storeUsers")
    List<StoreUser> findAll(Pageable pageable);

    StoreUser findByUsername(String username);
}
