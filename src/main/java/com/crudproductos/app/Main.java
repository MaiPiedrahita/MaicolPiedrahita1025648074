package com.crudproductos.app;

import com.crudproductos.exception.DuplicateProductIdException;
import com.crudproductos.exception.InvalidProductException;
import com.crudproductos.exception.ProductNotFoundException;
import com.crudproductos.model.Product;
import com.crudproductos.repository.InMemoryProductRepository;
import com.crudproductos.service.ProductService;

import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación. Presenta un menú interactivo por consola.
 */
public class Main {

    // Constante de menú
    private static final String SEPARADOR = "─────────────────────────────────────";

    private final ProductService service;
    private final Scanner scanner;

    /**
     * Constructor que permite inyectar dependencias (facilita pruebas).
     */
    public Main(ProductService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    /** Punto de entrada del programa. */
    public static void main(String[] args) {
        ProductService service = new ProductService(new InMemoryProductRepository());
        try (Scanner scanner = new Scanner(System.in)) {
            new Main(service, scanner).run();
        }
    }

    /** Ciclo principal del menú. */
    public void run() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         CRUD de Productos            ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean activo = true;
        while (activo) {
            imprimirMenu();
            String opcion = scanner.nextLine().trim();
            activo = procesarOpcion(opcion);
        }
        System.out.println("\n¡Hasta luego!");
    }

    private void imprimirMenu() {
        System.out.println("\n" + SEPARADOR);
        System.out.println("  1. Crear producto");
        System.out.println("  2. Listar todos los productos");
        System.out.println("  3. Buscar producto por ID");
        System.out.println("  4. Actualizar producto");
        System.out.println("  5. Eliminar producto");
        System.out.println("  0. Salir");
        System.out.println(SEPARADOR);
        System.out.print("  Selecciona una opción: ");
    }

    private boolean procesarOpcion(String opcion) {
        switch (opcion) {
            case "1" -> crearProducto();
            case "2" -> listarProductos();
            case "3" -> buscarProducto();
            case "4" -> actualizarProducto();
            case "5" -> eliminarProducto();
            case "0" -> { return false; }
            default -> System.out.println("  ⚠ Opción no válida. Intenta de nuevo.");
        }
        return true;
    }

    // ── Opciones del menú ──────────────────────────────────────────────────────

    private void crearProducto() {
        System.out.println("\n── Crear Producto ──");
        String id = leerTexto("ID");
        String nombre = leerTexto("Nombre");
        String descripcion = leerTexto("Descripción");
        double precio = leerDouble("Precio");
        int cantidad = leerInt("Cantidad");

        try {
            Product product = service.createProduct(id, nombre, descripcion, precio, cantidad);
            System.out.println("✔ Producto creado: " + product);
        } catch (DuplicateProductIdException | InvalidProductException e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    private void listarProductos() {
        System.out.println("\n── Lista de Productos ──");
        List<Product> productos = service.getAllProducts();
        if (productos.isEmpty()) {
            System.out.println("  (No hay productos registrados.)");
        } else {
            productos.forEach(p -> System.out.println("  • " + p));
        }
    }

    private void buscarProducto() {
        System.out.println("\n── Buscar Producto ──");
        String id = leerTexto("ID del producto");
        try {
            Product product = service.getProductById(id);
            System.out.println("✔ Producto encontrado: " + product);
        } catch (ProductNotFoundException | InvalidProductException e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    private void actualizarProducto() {
        System.out.println("\n── Actualizar Producto ──");
        String id = leerTexto("ID del producto a actualizar");
        String nombre = leerTexto("Nuevo nombre");
        String descripcion = leerTexto("Nueva descripción");
        double precio = leerDouble("Nuevo precio");
        int cantidad = leerInt("Nueva cantidad");

        try {
            Product product = service.updateProduct(id, nombre, descripcion, precio, cantidad);
            System.out.println("✔ Producto actualizado: " + product);
        } catch (ProductNotFoundException | InvalidProductException e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        System.out.println("\n── Eliminar Producto ──");
        String id = leerTexto("ID del producto a eliminar");
        try {
            service.deleteProduct(id);
            System.out.println("✔ Producto eliminado correctamente.");
        } catch (ProductNotFoundException | InvalidProductException e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── Utilidades de entrada ──────────────────────────────────────────────────

    private String leerTexto(String campo) {
        System.out.print("  " + campo + ": ");
        return scanner.nextLine().trim();
    }

    private double leerDouble(String campo) {
        System.out.print("  " + campo + ": ");
        String valor = scanner.nextLine().trim().replace(",", ".");
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            System.out.println("  ⚠ Valor inválido (" + e.getMessage() + "). Se usará 0.0.");
            return 0.0;
        }
    }

    private int leerInt(String campo) {
        System.out.print("  " + campo + ": ");
        String valor = scanner.nextLine().trim();
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            System.out.println("  ⚠ Valor inválido (" + e.getMessage() + "). Se usará 0.");
            return 0;
        }
    }
}
