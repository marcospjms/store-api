package br.com.store.controllers;

import br.com.store.model.Product;
import br.com.store.model.auth.StoreUser;
import br.com.store.services.StoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("api/public/users")
public class CustomerPublicController {

    @Autowired
    private StoreUserService userService;

    @PostMapping(value = "")
    public ResponseEntity<StoreUser> save(@RequestBody StoreUser storeUser) {
        return new ResponseEntity<>(this.userService.createUser(storeUser), HttpStatus.OK);
    }

    public ResponseEntity<String> listAll(Pageable pageable) {
        return new ResponseEntity<>("oi", HttpStatus.OK);
    }

}
