package br.com.store.controllers.admin;

import br.com.store.model.Category;
import br.com.store.util.tests.AdminRestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class CategoryControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AdminRestTemplate adminRestTemplate;

    private String serveUrl;
    private String categoriesUrl;

    @BeforeEach
    public void setup() {
        this.serveUrl = "http://localhost:" + port + "/api/";
        this.categoriesUrl = this.serveUrl + "admin/categories";
    }

    @Test
    public void createValidCategoryTest() throws Exception {
        Category category = new Category();
        category.setName("Teste da Silva");
        category.setDescription("Teste de categoria");
        assertThat(this.adminRestTemplate.postForObject(this.categoriesUrl, category, Category.class))
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrProperty("id");
    }

    @Test
    public void createInvalidCategoryTest() throws Exception {
        Category category = new Category();
        Assertions.assertThrows(Exception.class, () ->
                this.adminRestTemplate.postForObject(this.categoriesUrl, category, Category.class));
    }
}
