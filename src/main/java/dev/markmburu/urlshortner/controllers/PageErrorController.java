package dev.markmburu.urlshortner.controllers;

import dev.markmburu.urlshortner.dtos.ApiError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageErrorController implements ErrorController {

  @GetMapping(path = "/error")
  public ResponseEntity<ApiError<Object>> notFound() {
    final var apiError = new ApiError<>(HttpStatus.NOT_FOUND, "The requested path was not found.");
    return new ResponseEntity<>(apiError, apiError.getHttpStatus());
  }
}
