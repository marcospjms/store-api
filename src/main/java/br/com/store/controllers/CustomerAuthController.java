package br.com.store.controllers;

import br.com.store.model.ShoppingCartDiscount;
import br.com.store.model.ShoppingCartProduct;
import br.com.store.model.auth.StoreUser;
import br.com.store.services.ShoppingCartService;
import br.com.store.services.StoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/users")
public class CustomerAuthController {

    @Autowired
    private StoreUserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PutMapping(value = "")
    public ResponseEntity<StoreUser> update(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody StoreUser storeUser) {
        return new ResponseEntity<>(this.userService.updateFromStoreUser(userDetails.getUsername(), storeUser), HttpStatus.OK);
    }

    @PutMapping(value = "password")
    public ResponseEntity<StoreUser> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody StoreUser storeUser) {
        return new ResponseEntity<>(this.userService.updatePassword(userDetails.getUsername(), storeUser.getPassword()), HttpStatus.OK);
    }

    @GetMapping(value = "current")
    public ResponseEntity<StoreUser> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        StoreUser customer = this.userService.findByUsername(userDetails.getUsername());
        customer.setPassword(null);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping(value = "products")
    public ResponseEntity<ShoppingCartProduct> addProduct(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam(value = "productId") Long productId) {
        return new ResponseEntity<>(this.shoppingCartService.addProduct(userDetails.getUsername(), productId), HttpStatus.OK);
    }

    @PostMapping(value = "products")
    public ResponseEntity<ShoppingCartDiscount> addDiscount(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam(value = "discountId") Long discountId) {
        return new ResponseEntity<>(this.shoppingCartService.addDiscount(userDetails.getUsername(), discountId), HttpStatus.OK);
    }
}
