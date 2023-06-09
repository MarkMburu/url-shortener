package dev.markmburu.urlshortner.controllers;

import dev.markmburu.urlshortner.dtos.UserDto;
import dev.markmburu.urlshortner.dtos.requests.PaginatedRequest;
import dev.markmburu.urlshortner.dtos.responses.PaginatedResponse;
import dev.markmburu.urlshortner.exceptios.EntityExists;
import dev.markmburu.urlshortner.services.interfaces.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto request) throws EntityExists, URISyntaxException {
        var user = userService.createUser(request);
        return ResponseEntity.created(new URI("")).body(user);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<PaginatedResponse<UserDto>> findAll(@Valid PaginatedRequest request) {
        var users = userService.getUsers(request);
        return ResponseEntity.ok().body(users);
    }
}
