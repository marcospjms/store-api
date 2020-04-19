package br.com.store.services;

import br.com.store.model.Discount;
import br.com.store.repositories.DiscountRepository;
import br.com.store.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DiscountService {

    @Autowired
    private DiscountRepository repository;

    public Discount save(Discount discount) {
        return AbstractEntityUtil.save(this.repository, discount);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<Discount> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Discount> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new Discount(), query), pageable).getContent();
    }


    public Discount findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }
}
