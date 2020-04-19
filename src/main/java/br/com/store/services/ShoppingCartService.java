package br.com.store.services;

import br.com.store.model.*;
import br.com.store.model.auth.StoreUser;
import br.com.store.repositories.ShoppingCartDiscountRepository;
import br.com.store.repositories.ShoppingCartProductRepository;
import br.com.store.repositories.ShoppingCartRepository;
import br.com.store.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShoppingCartService {

    @Autowired
    private StoreUserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ShoppingCartRepository repository;

    @Autowired
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Autowired
    private ShoppingCartDiscountRepository shoppingCartDiscountRepository;

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

    public ShoppingCart findByStoreUserId(Long id) {
        List<ShoppingCart> shoppingCarts = repository.findByStoreUserId(id);
        return shoppingCarts != null && !shoppingCarts.isEmpty() ? shoppingCarts.get(0) : null;
    }

    public ShoppingCartProduct addProduct(String username, Long productId) {
        StoreUser customer = this.userService.findByUsername(username);
        Product product = this.productService.findById(productId);
        ShoppingCart shoppingCart = this.findByStoreUserId(customer.getId());
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setProduct(product);
        shoppingCartProduct.setShoppingCart(shoppingCart);
        return shoppingCartProductRepository.save(shoppingCartProduct);
    }

    public ShoppingCartDiscount addDiscount(String username, Long discountId) {
        StoreUser customer = this.userService.findByUsername(username);
        Discount discount = this.discountService.findById(discountId);
        ShoppingCart shoppingCart = this.findByStoreUserId(customer.getId());
        ShoppingCartDiscount shoppingCartDiscount = new ShoppingCartDiscount();
        shoppingCartDiscount.setDiscount(discount);
        shoppingCartDiscount.setShoppingCart(shoppingCart);
        return shoppingCartDiscountRepository.save(shoppingCartDiscount);
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }
}
