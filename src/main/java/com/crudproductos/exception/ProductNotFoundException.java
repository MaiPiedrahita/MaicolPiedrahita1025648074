package com.crudproductos.exception;

/**
 * Se lanza cuando se intenta acceder a un producto que no existe en el repositorio.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String id) {
        super("Producto no encontrado con id: " + id);
    }
}
