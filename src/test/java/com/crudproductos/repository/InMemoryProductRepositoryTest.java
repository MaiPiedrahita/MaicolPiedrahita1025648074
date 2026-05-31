package com.crudproductos.repository;

import com.crudproductos.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link InMemoryProductRepository}.
 * Verifican que la implementación en memoria cumple el contrato de {@link ProductRepository}.
 */
@DisplayName("InMemoryProductRepository")
class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;
    private static final Product PRODUCTO_A = new Product("A1", "Producto A", "Desc A", 10.0, 5);

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    @DisplayName("✔ save() y findById() retornan el mismo producto")
    void deberiaGuardarYRecuperarProducto() {
        repository.save(PRODUCTO_A);
        Optional<Product> resultado = repository.findById("A1");

        assertTrue(resultado.isPresent());
        assertEquals("A1", resultado.get().getId());
    }

    @Test
    @DisplayName("✔ findById() retorna Optional vacío si no existe")
    void deberiaRetornarVacioSiNoExiste() {
        Optional<Product> resultado = repository.findById("NOPE");
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("✔ findAll() retorna todos los productos guardados")
    void deberiaRetornarTodosLosProductos() {
        repository.save(new Product("B1", "B", "Desc B", 20.0, 2));
        repository.save(new Product("B2", "C", "Desc C", 30.0, 3));
        List<Product> lista = repository.findAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("✔ update() reemplaza el producto existente")
    void deberiaActualizarProducto() {
        repository.save(PRODUCTO_A);
        Product actualizado = new Product("A1", "Producto Actualizado", "Desc Nueva", 99.0, 1);
        repository.update(actualizado);

        Product encontrado = repository.findById("A1").orElseThrow();
        assertEquals("Producto Actualizado", encontrado.getNombre());
    }

    @Test
    @DisplayName("✔ deleteById() elimina el producto y retorna true")
    void deberiaEliminarProductoYRetornarTrue() {
        repository.save(PRODUCTO_A);
        boolean eliminado = repository.deleteById("A1");

        assertTrue(eliminado);
        assertFalse(repository.existsById("A1"));
    }

    @Test
    @DisplayName("✔ deleteById() retorna false si el id no existe")
    void deberiaRetornarFalseAlEliminarIdInexistente() {
        boolean resultado = repository.deleteById("NO_EXISTE");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("✔ existsById() retorna true si el producto existe")
    void deberiaConfirmarExistencia() {
        repository.save(PRODUCTO_A);
        assertTrue(repository.existsById("A1"));
    }

    @Test
    @DisplayName("✔ existsById() retorna false si el producto no existe")
    void deberiaNegarExistencia() {
        assertFalse(repository.existsById("Z99"));
    }
}
