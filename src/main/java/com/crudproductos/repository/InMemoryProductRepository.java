package com.crudproductos.repository;

import com.crudproductos.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria de {@link ProductRepository}.
 * Usa un {@link LinkedHashMap} para mantener el orden de inserción.
 * Ideal para pruebas y entornos donde no se requiere persistencia permanente.
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> storage = new LinkedHashMap<>();

    @Override
    public void save(Product product) {
        storage.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    @Override
    public void update(Product product) {
        storage.put(product.getId(), product);
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }
}
