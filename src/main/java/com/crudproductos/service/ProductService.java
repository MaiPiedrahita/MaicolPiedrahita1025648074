package com.crudproductos.service;

import com.crudproductos.exception.DuplicateProductIdException;
import com.crudproductos.exception.InvalidProductException;
import com.crudproductos.exception.ProductNotFoundException;
import com.crudproductos.model.Product;
import com.crudproductos.repository.ProductRepository;

import java.util.List;

/**
 * Servicio que contiene la lógica de negocio y las validaciones del CRUD de productos.
 * Delega el acceso a datos al {@link ProductRepository}.
 */
public class ProductService {

    private final ProductRepository repository;

    /**
     * Crea el servicio inyectando el repositorio (permite pruebas con distintas implementaciones).
     *
     * @param repository implementación del repositorio a utilizar
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Crea y persiste un nuevo producto tras validar todos sus campos.
     *
     * @param id          identificador único
     * @param nombre      nombre del producto (no puede estar vacío)
     * @param descripcion descripción del producto (no puede estar vacía)
     * @param precio      precio del producto (no puede ser negativo)
     * @param cantidad    cantidad en stock (no puede ser negativa)
     * @return el producto creado
     * @throws InvalidProductException      si algún campo no cumple las reglas
     * @throws DuplicateProductIdException  si ya existe un producto con ese id
     */
    public Product createProduct(String id, String nombre, String descripcion,
                                 double precio, int cantidad) {
        validateId(id);
        validateNombre(nombre);
        validateDescripcion(descripcion);
        validatePrecio(precio);
        validateCantidad(cantidad);

        if (repository.existsById(id)) {
            throw new DuplicateProductIdException(id);
        }

        Product product = new Product(id, nombre, descripcion, precio, cantidad);
        repository.save(product);
        return product;
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    /**
     * Busca un producto por su id.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     * @throws InvalidProductException   si el id es nulo o vacío
     * @throws ProductNotFoundException  si no existe un producto con ese id
     */
    public Product getProductById(String id) {
        validateId(id);
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /**
     * Retorna la lista de todos los productos almacenados.
     *
     * @return lista (posiblemente vacía) de productos
     */
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Actualiza todos los campos de un producto existente.
     *
     * @param id          identificador del producto a actualizar
     * @param nombre      nuevo nombre
     * @param descripcion nueva descripción
     * @param precio      nuevo precio
     * @param cantidad    nueva cantidad
     * @return el producto actualizado
     * @throws InvalidProductException  si algún campo no cumple las reglas
     * @throws ProductNotFoundException si no existe un producto con ese id
     */
    public Product updateProduct(String id, String nombre, String descripcion,
                                 double precio, int cantidad) {
        validateId(id);
        validateNombre(nombre);
        validateDescripcion(descripcion);
        validatePrecio(precio);
        validateCantidad(cantidad);

        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        Product updated = new Product(id, nombre, descripcion, precio, cantidad);
        repository.update(updated);
        return updated;
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Elimina el producto con el id indicado.
     *
     * @param id identificador del producto a eliminar
     * @throws InvalidProductException  si el id es nulo o vacío
     * @throws ProductNotFoundException si no existe un producto con ese id
     */
    public void deleteProduct(String id) {
        validateId(id);
        if (!repository.deleteById(id)) {
            throw new ProductNotFoundException(id);
        }
    }

    // ── Validaciones privadas ─────────────────────────────────────────────────

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidProductException("El id del producto no puede estar vacío.");
        }
    }

    private void validateNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new InvalidProductException("El nombre del producto no puede estar vacío.");
        }
    }

    private void validateDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new InvalidProductException("La descripción del producto no puede estar vacía.");
        }
    }

    private void validatePrecio(double precio) {
        if (precio < 0) {
            throw new InvalidProductException("El precio no puede ser negativo.");
        }
    }

    private void validateCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new InvalidProductException("La cantidad no puede ser negativa.");
        }
    }
}
