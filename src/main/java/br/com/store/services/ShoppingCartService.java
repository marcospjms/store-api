package br.com.store.services;

import br.com.store.model.ShoppingCart;
import br.com.store.repositories.ShoppingCartRepository;
import br.com.store.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository repository;

    public ShoppingCart save(ShoppingCart shoppingCart) {
        return AbstractEntityUtil.save(this.repository, shoppingCart);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<ShoppingCart> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<ShoppingCart> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new ShoppingCart(), query), pageable).getContent();
    }


    public ShoppingCart findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }
}
