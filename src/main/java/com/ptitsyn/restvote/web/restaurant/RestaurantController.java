package com.ptitsyn.restvote.web.restaurant;

import com.ptitsyn.restvote.model.Restaurant;
import com.ptitsyn.restvote.service.RestaurantService;
import com.ptitsyn.restvote.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    RestaurantService service;

    @GetMapping
    public List<Restaurant> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll for user {}", authUser.id());
        return service.getAllWithMenu();
    }

    public Restaurant get(int id) {
        //TODO
        return null;
    }

    @PostMapping
    void create() {
        //TODO
    }

    @PutMapping
    void update() {
        //TODO
    }

    @DeleteMapping("{id}")
    void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        service.delete(id);
    }
}

