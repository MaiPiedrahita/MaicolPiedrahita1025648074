package com.crudproductos.model;

import java.util.Objects;

/**
 * Entidad que representa un producto en el sistema.
 * El id es inmutable; los demás campos pueden actualizarse.
 */
public class Product {

    private final String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;

    /**
     * Crea un producto con todos sus atributos.
     *
     * @param id          identificador único del producto
     * @param nombre      nombre del producto
     * @param descripcion descripción del producto
     * @param precio      precio (debe ser >= 0)
     * @param cantidad    cantidad en stock (debe ser >= 0)
     */
    public Product(String id, String nombre, String descripcion, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    // ── Setters ─────────────────────────────────────────────────────────────

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // ── equals / hashCode: basados únicamente en el id ──────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Product{ id='%s', nombre='%s', descripcion='%s', precio=%.2f, cantidad=%d }",
            id, nombre, descripcion, precio, cantidad
        );
    }
}
