package com.gestra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception untuk resource yang tidak ditemukan (HTTP 404).
 *
 * Pilar PBO — Inheritance:
 * Extends RuntimeException, class kustom untuk domain Gestra.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " dengan id " + id + " tidak ditemukan");
    }
}
