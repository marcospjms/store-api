package br.com.store.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.store.model.auth.Role;
import br.com.store.model.auth.StoreUser;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.transaction.Transactional;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerPublicControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String serveUrl;
    private String usersUrl;
    private String createPublicUserUrl;
    private String createAdminUserUrl;

    @BeforeEach
    public void setup() {
        this.serveUrl = "http://localhost:" + port + "/api/";
        this.usersUrl = this.serveUrl + "public/users/";
        this.createPublicUserUrl = this.usersUrl;
        this.createAdminUserUrl = this.usersUrl + "admin";
    }

    @Test
    public void createValidPublicUserTest() throws Exception {
        StoreUser storeUser = new StoreUser();
        storeUser.setUsername("usuariopublico");
        storeUser.setPassword("123456");
        storeUser.setEmail("usuariopublico@gmail.com");
        storeUser.setName("Teste da Silva");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, storeUser, StoreUser.class))
                .hasFieldOrPropertyWithValue("username", storeUser.getUsername())
                .hasFieldOrPropertyWithValue("email", storeUser.getEmail())
                .hasFieldOrPropertyWithValue("name", storeUser.getName())
                .hasFieldOrPropertyWithValue("password", null)
                .extracting("roles")
                .asInstanceOf(InstanceOfAssertFactories.ITERABLE)
                .containsOnly(Role.CUSTOMER);
    }

    @Test
    public void createInvalidPublicUserTest() throws Exception {
        StoreUser storeUser = new StoreUser();
        storeUser.setUsername("usuariopublicoinvalido");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, storeUser, StoreUser.class)).hasAllNullFieldsOrProperties();
    }

    @Test
    public void createValidAdminUserTest() throws Exception {
        StoreUser storeUser = new StoreUser();
        storeUser.setUsername("usuarioadmin");
        storeUser.setPassword("123456");
        storeUser.setEmail("usuarioadmin@gmail.com");
        storeUser.setName("Teste da Silva");
        assertThat(this.restTemplate.postForObject(this.createAdminUserUrl, storeUser, StoreUser.class))
                .hasFieldOrPropertyWithValue("username", storeUser.getUsername())
                .hasFieldOrPropertyWithValue("email", storeUser.getEmail())
                .hasFieldOrPropertyWithValue("name", storeUser.getName())
                .hasFieldOrPropertyWithValue("password", null)
                .extracting("roles")
                .asInstanceOf(InstanceOfAssertFactories.ITERABLE)
                .containsOnly(Role.CUSTOMER, Role.ADMIN);
    }

    @Test
    public void createInvalidAdminUserTest() throws Exception {
        StoreUser storeUser = new StoreUser();
        storeUser.setUsername("usuarioadmininvalido");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, storeUser, StoreUser.class)).hasAllNullFieldsOrProperties();
    }
}
