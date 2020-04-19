package br.com.store.services;

import static org.junit.Assert.*;

import br.com.store.model.Category;
import br.com.store.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoppingCartDiscountRepository shoppingCartDiscountRepository;

    @Mock
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    public void sampleTeste() {
        Category category = new Category();
        category.setId(1L);
        when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertEquals(1L, 1L);
    }
}