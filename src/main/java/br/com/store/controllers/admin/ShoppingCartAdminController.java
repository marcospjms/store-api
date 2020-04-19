package br.com.store.controllers.admin;

import br.com.store.model.ShoppingCart;
import br.com.store.services.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/shoppingCarts")
public class ShoppingCartAdminController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping(value = "")
    public ResponseEntity<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart) {
        return new ResponseEntity<>(this.shoppingCartService.save(shoppingCart), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<ShoppingCart> update(@RequestBody ShoppingCart shoppingCart) {
        return new ResponseEntity<>(this.shoppingCartService.save(shoppingCart), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.shoppingCartService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ShoppingCart> get(@PathVariable(value = "id") Long id) {
        ShoppingCart shoppingCart = this.shoppingCartService.findById(id);
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/has")
    public ResponseEntity<Boolean> has(@PathVariable(value = "id") String id) {
        boolean response = false;
        if (StringUtils.isNumeric(id)) {
            response = this.shoppingCartService.has(Long.parseLong(id));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<ShoppingCart>> listAll(Pageable pageable,
                                                  @RequestParam(value = "query", required = false) String query) {
        List<ShoppingCart> shoppingCarts = null;

        if (query != null && !query.trim().isEmpty()) {
            shoppingCarts = this.shoppingCartService.findByQuery(pageable, query);
        } else {
            shoppingCarts = this.shoppingCartService.findAll(pageable);
        }

        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

}
