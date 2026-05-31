package com.crudproductos.exception;

/**
 * Se lanza cuando se intenta crear un producto con un id que ya existe.
 */
public class DuplicateProductIdException extends RuntimeException {

    public DuplicateProductIdException(String id) {
        super("Ya existe un producto con id: " + id);
    }
}
