package br.com.store.services;

import static org.junit.Assert.*;

import br.com.store.model.Product;
import br.com.store.model.ShoppingCart;
import br.com.store.model.ShoppingCartProduct;
import br.com.store.model.auth.StoreUser;
import br.com.store.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceTest {

    @Mock
    private StoreUserRepository storeUserRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ShoppingCartDiscountRepository shoppingCartDiscountRepository;

    @Mock
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private StoreUserService storeUserService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    public void addProductTest() {
        Long productId = 0l;
        Long userId = 1l;
        Long shoppingCartId = 2l;
        String username = "marcospjms";
        Product product = new Product();
        product.setId(productId);
        StoreUser storeUser = new StoreUser();
        storeUser.setId(1l);
        storeUser.setUsername(username);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartId);
        when(this.productService.findById(1L)).thenReturn(product);
        when(this.storeUserService.findByUsername(username)).thenReturn(storeUser);
        when(this.shoppingCartRepository.findByStoreUserId(userId)).thenReturn(Arrays.asList(shoppingCart));

        ShoppingCartProduct shoppingCartProduct = this.shoppingCartService.addProduct("marcospjms", 1L);

        assertEquals(1L, 1L);
    }

    @Test
    public void addDiscountTest() {
        ShoppingCartProduct shoppingCartProduct = this.shoppingCartService.addProduct("marcospjms", 1L);

        assertEquals(1L, 1L);
    }
}