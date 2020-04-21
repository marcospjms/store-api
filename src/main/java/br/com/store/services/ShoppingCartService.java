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
        return this.repository.save(shoppingCart);
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

    public ShoppingCart findByStoreUser(StoreUser customer) {
        List<ShoppingCart> shoppingCarts = repository.findByUsername(customer.getUsername());
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

    public ShoppingCart addProduct(String username, Long productId) {
        StoreUser customer = this.userService.findByUsername(username);
        Product product = this.productService.findById(productId);
        ShoppingCart shoppingCart = this.findByStoreUser(customer);
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setProduct(product);
        shoppingCartProduct.setShoppingCart(shoppingCart);
        shoppingCartProduct = shoppingCartProductRepository.save(shoppingCartProduct);
        return this.updateShoppingCart(username);
    }

    public ShoppingCart addDiscount(String username, String discountCode) {
        Discount discount = this.discountService.findValidByCode(discountCode);
        if (discount == null) {
            throw new RuntimeException("Código de cupom inválido: desconto não localizado");
        }
        StoreUser customer = this.userService.findByUsername(username);
        ShoppingCart shoppingCart = this.findByStoreUser(customer);
        ShoppingCartDiscount shoppingCartDiscount = new ShoppingCartDiscount();
        shoppingCartDiscount.setDiscount(discount);
        shoppingCartDiscount.setShoppingCart(shoppingCart);
        shoppingCartDiscountRepository.save(shoppingCartDiscount);
        return this.updateShoppingCart(username);
    }

    public ShoppingCart updateShoppingCart(String username) {
        StoreUser customer = this.userService.findByUsername(username);
        ShoppingCart shoppingCart = this.findByStoreUser(customer);
        List<Product> products = this.findAllProductsByUsername(username);
        List<Discount> discounts = this.findDiscountByUsername(username);;

        shoppingCart.setDiscount(this.discountService.calcDiscount(discounts, products, shoppingCart.getPaymentType()));
        shoppingCart.setCost(this.productService.calcTotalCost(products));

        return this.save(shoppingCart);
    }

    public List<Product> findProductsByUsername(Pageable pageable, String username) {
        return this.toProducts(this.shoppingCartProductRepository.findByUsername(pageable, username));
    }

    public List<Product> findAllProductsByUsername(String username) {
        return this.toProducts(this.shoppingCartProductRepository.findByUsername(username));
    }

    public List<Discount> findDiscountByUsername(Pageable pageable, String username) {
        return this.toDiscounts(this.shoppingCartDiscountRepository.findByUsername(pageable, username));
    }

    public List<Discount> findDiscountByUsername(String username) {
        return this.toDiscounts(this.shoppingCartDiscountRepository.findByUsername(username));
    }

    private List<Discount> toDiscounts(List<ShoppingCartDiscount> shoppingCartDiscounts) {
        return shoppingCartDiscounts.stream().map(scp -> scp.getDiscount()).collect(Collectors.toList());
    }

    private List<Product> toProducts(List<ShoppingCartProduct> shoppingCartProducts) {
        return shoppingCartProducts.stream().map(scp -> scp.getProduct()).collect(Collectors.toList());
    }
}
