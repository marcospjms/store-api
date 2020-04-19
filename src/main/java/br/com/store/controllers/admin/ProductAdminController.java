package br.com.store.controllers.admin;

import br.com.store.model.Product;
import br.com.store.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/products")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(this.productService.save(product), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        return new ResponseEntity<>(this.productService.save(product), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.productService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> get(@PathVariable(value = "id") Long id) {
        Product product = this.productService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/has")
    public ResponseEntity<Boolean> has(@PathVariable(value = "id") String id) {
        boolean response = false;
        if (StringUtils.isNumeric(id)) {
            response = this.productService.has(Long.parseLong(id));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Product>> listAll(Pageable pageable,
                                                  @RequestParam(value = "query", required = false) String query) {
        List<Product> products = null;

        if (query != null && !query.trim().isEmpty()) {
            products = this.productService.findByQuery(pageable, query);
        } else {
            products = this.productService.findAll(pageable);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
