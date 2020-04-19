package br.com.store.controllers.admin;

import br.com.store.model.Category;
import br.com.store.services.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/categories")
public class CategoryAdminController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "")
    public ResponseEntity<Category> save(@RequestBody Category category) {
        return new ResponseEntity<>(this.categoryService.save(category), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        return new ResponseEntity<>(this.categoryService.save(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.categoryService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> get(@PathVariable(value = "id") Long id) {
        Category category = this.categoryService.findById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/has")
    public ResponseEntity<Boolean> has(@PathVariable(value = "id") String id) {
        boolean response = false;
        if (StringUtils.isNumeric(id)) {
            response = this.categoryService.has(Long.parseLong(id));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Category>> listAll(Pageable pageable,
                                                  @RequestParam(value = "query", required = false) String query) {
        List<Category> categories = null;

        if (query != null && !query.trim().isEmpty()) {
            categories = this.categoryService.findByQuery(pageable, query);
        } else {
            categories = this.categoryService.findAll(pageable);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
