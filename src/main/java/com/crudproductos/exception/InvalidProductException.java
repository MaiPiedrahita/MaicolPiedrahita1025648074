package com.crudproductos.exception;

/**
 * Se lanza cuando los datos de un producto no cumplen las reglas de negocio
 * (nombre vacío, precio negativo, cantidad negativa, etc.).
 */
public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String message) {
        super(message);
    }
}
