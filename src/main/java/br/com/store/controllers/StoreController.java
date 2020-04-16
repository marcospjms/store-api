package br.com.store.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/store/")
public class StoreController {

    @GetMapping(value = "")
    public ResponseEntity<String> listAll(Pageable pageable) {
        return new ResponseEntity<>("oi", HttpStatus.OK);
    }

}
