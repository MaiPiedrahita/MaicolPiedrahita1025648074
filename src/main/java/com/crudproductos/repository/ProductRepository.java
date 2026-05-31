package com.crudproductos.repository;

import com.crudproductos.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Contrato que define las operaciones de persistencia para {@link Product}.
 * Cualquier implementación (en memoria, archivo, base de datos) debe respetar esta interfaz.
 */
public interface ProductRepository {

    /**
     * Persiste un nuevo producto.
     *
     * @param product el producto a guardar
     */
    void save(Product product);

    /**
     * Busca un producto por su id.
     *
     * @param id identificador del producto
     * @return {@link Optional} con el producto si existe, vacío si no
     */
    Optional<Product> findById(String id);

    /**
     * Retorna todos los productos almacenados.
     *
     * @return lista inmutable de productos
     */
    List<Product> findAll();

    /**
     * Reemplaza un producto existente por su id.
     *
     * @param product el producto con los datos actualizados
     */
    void update(Product product);

    /**
     * Elimina el producto con el id dado.
     *
     * @param id identificador del producto a eliminar
     * @return {@code true} si se eliminó, {@code false} si no existía
     */
    boolean deleteById(String id);

    /**
     * Comprueba si existe un producto con el id dado.
     *
     * @param id identificador a comprobar
     * @return {@code true} si existe
     */
    boolean existsById(String id);
}
