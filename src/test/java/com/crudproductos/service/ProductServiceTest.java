package com.crudproductos.service;

import com.crudproductos.exception.DuplicateProductIdException;
import com.crudproductos.exception.InvalidProductException;
import com.crudproductos.exception.ProductNotFoundException;
import com.crudproductos.model.Product;
import com.crudproductos.repository.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link ProductService}.
 * Cada operación CRUD tiene al menos un caso exitoso y uno de error.
 */
@DisplayName("ProductService")
class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        // Repositorio limpio antes de cada prueba
        service = new ProductService(new InMemoryProductRepository());
    }

    // ════════════════════════════════════════════════════════════════════════
    // CREATE
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("createProduct()")
    class CreateProductTests {

        @Test
        @DisplayName("✔ Crea un producto válido y lo retorna con todos sus campos")
        void deberiaCrearProductoValido() {
            Product product = service.createProduct("P001", "Laptop", "Laptop gamer", 1500.0, 10);

            assertNotNull(product);
            assertEquals("P001", product.getId());
            assertEquals("Laptop", product.getNombre());
            assertEquals("Laptop gamer", product.getDescripcion());
            assertEquals(1500.0, product.getPrecio(), 0.001);
            assertEquals(10, product.getCantidad());
        }

        @Test
        @DisplayName("✔ Crea un producto con precio 0 (válido en el límite)")
        void deberiaPermitirPrecioCero() {
            Product product = service.createProduct("P002", "Muestra", "Producto gratis", 0.0, 5);
            assertEquals(0.0, product.getPrecio(), 0.001);
        }

        @Test
        @DisplayName("✔ Crea un producto con cantidad 0 (válido en el límite)")
        void deberiaPermitirCantidadCero() {
            Product product = service.createProduct("P003", "Agotado", "Sin stock", 50.0, 0);
            assertEquals(0, product.getCantidad());
        }

        @Test
        @DisplayName("✖ Lanza DuplicateProductIdException si el id ya existe")
        void deberiLanzarExcepcionConIdDuplicado() {
            service.createProduct("P001", "Laptop", "Laptop gamer", 1500.0, 10);

            assertThrows(DuplicateProductIdException.class, () ->
                service.createProduct("P001", "Mouse", "Mouse inalámbrico", 25.0, 50)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el id es nulo")
        void deberiLanzarExcepcionConIdNulo() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct(null, "Teclado", "Descripción", 80.0, 1)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el id está vacío")
        void deberiLanzarExcepcionConIdVacio() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct("   ", "Teclado", "Descripción", 80.0, 1)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el nombre está vacío")
        void deberiLanzarExcepcionConNombreVacio() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct("P004", "", "Descripción", 100.0, 5)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si la descripción está vacía")
        void deberiLanzarExcepcionConDescripcionVacia() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct("P005", "Monitor", "   ", 200.0, 3)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el precio es negativo")
        void deberiLanzarExcepcionConPrecioNegativo() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct("P006", "Monitor", "Monitor 4K", -1.0, 3)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si la cantidad es negativa")
        void deberiLanzarExcepcionConCantidadNegativa() {
            assertThrows(InvalidProductException.class, () ->
                service.createProduct("P007", "Audífonos", "Audífonos BT", 120.0, -5)
            );
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // READ
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("getProductById()")
    class GetProductByIdTests {

        @Test
        @DisplayName("✔ Retorna el producto cuando existe")
        void deberiaRetornarProductoExistente() {
            service.createProduct("P010", "Silla", "Silla ergonómica", 300.0, 5);

            Product found = service.getProductById("P010");

            assertNotNull(found);
            assertEquals("P010", found.getId());
            assertEquals("Silla", found.getNombre());
        }

        @Test
        @DisplayName("✖ Lanza ProductNotFoundException cuando el producto no existe")
        void deberiLanzarExcepcionProductoInexistente() {
            assertThrows(ProductNotFoundException.class, () ->
                service.getProductById("ID_INEXISTENTE")
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el id es nulo")
        void deberiLanzarExcepcionConIdNulo() {
            assertThrows(InvalidProductException.class, () ->
                service.getProductById(null)
            );
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // GET ALL
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("getAllProducts()")
    class GetAllProductsTests {

        @Test
        @DisplayName("✔ Retorna lista vacía cuando no hay productos")
        void deberiaRetornarListaVacia() {
            List<Product> products = service.getAllProducts();
            assertTrue(products.isEmpty());
        }

        @Test
        @DisplayName("✔ Retorna todos los productos creados")
        void deberiaRetornarTodosLosProductos() {
            service.createProduct("P020", "Mesa", "Mesa de madera", 200.0, 10);
            service.createProduct("P021", "Lámpara", "Lámpara LED", 50.0, 30);
            service.createProduct("P022", "Estante", "Estante metálico", 120.0, 7);

            List<Product> products = service.getAllProducts();

            assertEquals(3, products.size());
        }

        @Test
        @DisplayName("✔ La lista retornada no permite modificar el almacenamiento interno")
        void laListaDebeSerInmutable() {
            service.createProduct("P023", "Silla", "Silla de oficina", 180.0, 4);
            List<Product> products = service.getAllProducts();

            assertThrows(UnsupportedOperationException.class, () -> products.add(
                new Product("EXTRA", "Extra", "Producto extra", 10.0, 1)
            ));
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // UPDATE
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("updateProduct()")
    class UpdateProductTests {

        @Test
        @DisplayName("✔ Actualiza un producto existente con nuevos valores")
        void deberiaActualizarProductoExistente() {
            service.createProduct("P030", "Impresora", "Impresora láser", 400.0, 8);

            Product updated = service.updateProduct("P030", "Impresora Pro", "Impresora láser color", 550.0, 6);

            assertNotNull(updated);
            assertEquals("P030", updated.getId());
            assertEquals("Impresora Pro", updated.getNombre());
            assertEquals("Impresora láser color", updated.getDescripcion());
            assertEquals(550.0, updated.getPrecio(), 0.001);
            assertEquals(6, updated.getCantidad());
        }

        @Test
        @DisplayName("✔ El cambio es visible al consultar el producto después de actualizar")
        void elCambioDebeSerPersistente() {
            service.createProduct("P031", "Tablet", "Tablet Android", 350.0, 12);
            service.updateProduct("P031", "Tablet Pro", "Tablet Android Pro", 450.0, 10);

            Product consultado = service.getProductById("P031");
            assertEquals("Tablet Pro", consultado.getNombre());
            assertEquals(450.0, consultado.getPrecio(), 0.001);
        }

        @Test
        @DisplayName("✖ Lanza ProductNotFoundException cuando el producto no existe")
        void deberiLanzarExcepcionProductoInexistente() {
            assertThrows(ProductNotFoundException.class, () ->
                service.updateProduct("ID_INEXISTENTE", "Nombre", "Descripción", 100.0, 1)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el precio es negativo en actualización")
        void deberiLanzarExcepcionConPrecioNegativoEnActualizacion() {
            service.createProduct("P032", "Ventilador", "Ventilador USB", 30.0, 20);

            assertThrows(InvalidProductException.class, () ->
                service.updateProduct("P032", "Ventilador", "Ventilador USB", -5.0, 20)
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si la cantidad es negativa en actualización")
        void deberiLanzarExcepcionConCantidadNegativaEnActualizacion() {
            service.createProduct("P033", "Teclado", "Teclado mecánico", 85.0, 10);

            assertThrows(InvalidProductException.class, () ->
                service.updateProduct("P033", "Teclado", "Teclado mecánico", 85.0, -3)
            );
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DELETE
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProductTests {

        @Test
        @DisplayName("✔ Elimina un producto existente sin lanzar excepción")
        void deberiaEliminarProductoExistente() {
            service.createProduct("P040", "Webcam", "Webcam HD 1080p", 90.0, 15);

            assertDoesNotThrow(() -> service.deleteProduct("P040"));
        }

        @Test
        @DisplayName("✔ El producto ya no es encontrable tras eliminarse")
        void elProductoNoDebeExistirDespuesDeEliminar() {
            service.createProduct("P041", "Micrófono", "Micrófono USB", 75.0, 8);
            service.deleteProduct("P041");

            assertThrows(ProductNotFoundException.class, () ->
                service.getProductById("P041")
            );
        }

        @Test
        @DisplayName("✔ La lista disminuye en 1 tras eliminar un producto")
        void laListaDebeReducirseAlEliminar() {
            service.createProduct("P042", "Bocinas", "Bocinas Bluetooth", 60.0, 20);
            service.createProduct("P043", "Cable HDMI", "Cable HDMI 4K", 15.0, 50);

            service.deleteProduct("P042");

            List<Product> products = service.getAllProducts();
            assertEquals(1, products.size());
            assertFalse(products.stream().anyMatch(p -> p.getId().equals("P042")));
        }

        @Test
        @DisplayName("✖ Lanza ProductNotFoundException cuando el producto no existe")
        void deberiLanzarExcepcionProductoInexistente() {
            assertThrows(ProductNotFoundException.class, () ->
                service.deleteProduct("ID_INEXISTENTE")
            );
        }

        @Test
        @DisplayName("✖ Lanza InvalidProductException si el id está en blanco")
        void deberiLanzarExcepcionConIdEnBlanco() {
            assertThrows(InvalidProductException.class, () ->
                service.deleteProduct("   ")
            );
        }
    }
}
