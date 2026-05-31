# CRUD de Productos — Taller Universitario

Aplicación de consola en Java para gestionar un catálogo de productos con operaciones
**Create · Read · Update · Delete**, almacenamiento en memoria, pruebas unitarias con JUnit 5
y configuración lista para SonarQube.

---

## Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java (JDK)  | 25            |
| Maven       | 3.9+          |

Verificar instalación:
```bash
java -version
mvn -version
```

---

## Estructura del proyecto

```
CrudProductos_Maicol_Piedrahita/
├── pom.xml                          # Configuración Maven
├── sonar-project.properties         # Configuración SonarQube
├── .gitignore
├── README.md
└── src/
    ├── main/java/com/crudproductos/
    │   ├── model/
    │   │   └── Product.java              # Entidad Producto
    │   ├── exception/
    │   │   ├── ProductNotFoundException.java
    │   │   ├── DuplicateProductIdException.java
    │   │   └── InvalidProductException.java
    │   ├── repository/
    │   │   ├── ProductRepository.java         # Interfaz (contrato)
    │   │   └── InMemoryProductRepository.java # Implementación en memoria
    │   ├── service/
    │   │   └── ProductService.java       # Lógica de negocio y validaciones
    │   └── app/
    │       └── Main.java                 # Menú interactivo de consola
    └── test/java/com/crudproductos/
        ├── service/
        │   └── ProductServiceTest.java   # Pruebas de la lógica de negocio
        └── repository/
            └── InMemoryProductRepositoryTest.java
```

---

## Comandos principales

### Compilar el proyecto
```bash
mvn compile
```

### ▶ Ejecutar todas las pruebas (comando único)
```bash
mvn test
```

> **Windows con proxy corporativo / certificados SSL personalizados:**  
> Si Maven no puede conectar a Maven Central usa el almacén de Windows:
> ```powershell
> $env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
> $env:MAVEN_OPTS = "-Djavax.net.ssl.trustStoreType=Windows-ROOT"
> mvn clean test --no-transfer-progress
> ```
> O simplemente ejecuta el script incluido:
> ```powershell
> .\run-tests.ps1
> ```

Esto también genera el reporte de cobertura JaCoCo en:
```
target/site/jacoco/index.html
```

### Compilar y empaquetar en JAR
```bash
mvn package -DskipTests
```

### Ejecutar la aplicación (menú de consola)
```bash
java -jar target/crud-productos.jar
```

### Limpiar y ejecutar todo de una vez
```bash
mvn clean test
```

---

## Operaciones disponibles en el menú

| Opción | Operación             |
|--------|-----------------------|
| 1      | Crear producto        |
| 2      | Listar todos          |
| 3      | Buscar por ID         |
| 4      | Actualizar producto   |
| 5      | Eliminar producto     |
| 0      | Salir                 |

---

## Reglas de negocio

- El **id** es único y obligatorio (no puede estar vacío).
- El **nombre** no puede estar vacío.
- La **descripción** no puede estar vacía.
- El **precio** debe ser ≥ 0.
- La **cantidad** debe ser ≥ 0.
- Las operaciones sobre IDs inexistentes lanzan `ProductNotFoundException`.
- La creación con un ID ya existente lanza `DuplicateProductIdException`.

---

## Pruebas unitarias

Las pruebas están organizadas con `@Nested` por operación CRUD:

| Clase de prueba                  | Casos cubiertos |
|----------------------------------|-----------------|
| `ProductServiceTest`             | 20 pruebas      |
| `InMemoryProductRepositoryTest`  | 8 pruebas       |

Cada operación tiene **al menos** un caso exitoso y uno de error.

---

## Análisis con SonarQube

### Con servidor SonarQube local (puerto 9000)
```bash
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar "-Dsonar.projectKey=crud-productos" "-Dsonar.projectName='crud-productos'" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=tu_token"
```

El reporte de cobertura JaCoCo (XML) se genera automáticamente en `mvn test`
y es detectado por SonarQube según la ruta configurada en `sonar-project.properties`.

---

## Tecnologías

- **Java 25**
- **Maven 3.9+**
- **JUnit Jupiter 5.11.4** — pruebas unitarias
- **JaCoCo 0.8.12** — cobertura de código
- **SonarQube** — análisis estático de calidad

---

## Autor

**Maicol Piedrahita**  
Taller universitario — Aplicaciones y Servicios Web · 2026-1
