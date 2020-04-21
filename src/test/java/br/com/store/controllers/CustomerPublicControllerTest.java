package br.com.store.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.store.model.auth.StoreUser;
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
        this.usersUrl = this.serveUrl + "auth/users/";
        this.createPublicUserUrl = this.usersUrl;
        this.createAdminUserUrl = this.usersUrl + "admin";
    }

    @Test
    public void createPublicUserTest() throws Exception {
        StoreUser storeUser = new StoreUser();
        storeUser.setUsername("teste1");
        storeUser.setPassword("123456");
        storeUser.setName("Teste da Silva");
        System.out.println(">>>>>>");
        assertThat(this.restTemplate.postForObject(this.createPublicUserUrl, storeUser, StoreUser.class))
                .hasFieldOrPropertyWithValue("username", storeUser.getUsername());
    }
}
