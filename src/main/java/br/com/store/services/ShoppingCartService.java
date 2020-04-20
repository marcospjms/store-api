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
import java.util.stream.Collectors;


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

    public ShoppingCart findByStoreUser(StoreUser customer) {
        List<ShoppingCart> shoppingCarts = repository.findByStoreUserId(customer.getId());
        if (shoppingCarts.size() > 1) {
            throw new RuntimeException("Usuário com mais de um carinho de compra. Impossível saber o correto.");
        }
        if (!shoppingCarts.isEmpty()) {
            return shoppingCarts.get(0);
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setStoreUser(customer);
            return this.save(shoppingCart);
        }
    }

    public ShoppingCartProduct addProduct(String username, Long productId) {
        StoreUser customer = this.userService.findByUsername(username);
        Product product = this.productService.findById(productId);
        ShoppingCart shoppingCart = this.findByStoreUser(customer);
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setProduct(product);
        shoppingCartProduct.setShoppingCart(shoppingCart);
        return shoppingCartProductRepository.save(shoppingCartProduct);
    }

    public List<Product> findProductsByUsername(Pageable pageable, String username) {
        StoreUser customer = this.userService.findByUsername(username);
        return this.shoppingCartProductRepository.findByStoreUserId(pageable, customer.getId())
                .stream().map(scp -> scp.getProduct()).collect(Collectors.toList());
    }

    public ShoppingCartDiscount addDiscount(String username, String discountCode) {
        Discount discount = this.discountService.findValidByCode(discountCode);
        if (discount == null) {
            throw new RuntimeException("Código inválido");
        }
        StoreUser customer = this.userService.findByUsername(username);
        ShoppingCart shoppingCart = this.findByStoreUser(customer);
        ShoppingCartDiscount shoppingCartDiscount = new ShoppingCartDiscount();
        shoppingCartDiscount.setDiscount(discount);
        shoppingCartDiscount.setShoppingCart(shoppingCart);
        return shoppingCartDiscountRepository.save(shoppingCartDiscount);
    }

    public List<Discount> findDiscountByUsername(Pageable pageable, String username) {
        StoreUser customer = this.userService.findByUsername(username);
        return this.shoppingCartDiscountRepository.findByStoreUserId(pageable, customer.getId())
                .stream().map(scp -> scp.getDiscount()).collect(Collectors.toList());
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }
}
