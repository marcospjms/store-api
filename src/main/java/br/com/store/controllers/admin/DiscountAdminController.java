package br.com.store.controllers.admin;

import br.com.store.model.Discount;
import br.com.store.services.DiscountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/discounts")
public class DiscountAdminController {

    @Autowired
    private DiscountService discountService;

    @PostMapping(value = "")
    public ResponseEntity<Discount> save(@RequestBody Discount discount) {
        return new ResponseEntity<>(this.discountService.save(discount), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Discount> update(@RequestBody Discount discount) {
        return new ResponseEntity<>(this.discountService.save(discount), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.discountService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Discount> get(@PathVariable(value = "id") Long id) {
        Discount discount = this.discountService.findById(id);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/has")
    public ResponseEntity<Boolean> has(@PathVariable(value = "id") String id) {
        boolean response = false;
        if (StringUtils.isNumeric(id)) {
            response = this.discountService.has(Long.parseLong(id));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Discount>> listAll(Pageable pageable,
                                                  @RequestParam(value = "query", required = false) String query) {
        List<Discount> discounts = null;

        if (query != null && !query.trim().isEmpty()) {
            discounts = this.discountService.findByQuery(pageable, query);
        } else {
            discounts = this.discountService.findAll(pageable);
        }

        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

}
