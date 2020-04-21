package br.com.store.services;

import br.com.store.model.Product;
import br.com.store.repositories.ProductRepository;
import br.com.store.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product save(Product product) {
        return this.repository.save(product);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Product> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new Product(), query), pageable).getContent();
    }


    public Product findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }

    public static double calcTotalCost(List<Product> products) {
        return products.stream().reduce(0.0, (subtotal, product) -> subtotal + product.getPrice(), Double::sum);
    }
}
