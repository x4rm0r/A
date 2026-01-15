# Patrones de Diseño Aplicados

En el desarrollo de la aplicación **GestorGastos** se han aplicado diversos patrones de diseño para resolver problemas recurrentes de arquitectura, mejorar la cohesión y reducir el acoplamiento. A continuación se detallan los patrones utilizados clasificados por su tipología:

## 1. Patrones de Creación (Creational Patterns)
*Se ocupan de la instanciación de objetos, delegando la creación para desacoplar al cliente de los objetos que necesita.*

### 1.1. Patrón Método Factoría (Factory Method)
**Contexto de uso:**
Se emplea en el módulo de **Importación de Datos** para la creación de los objetos encargados de leer ficheros externos.

* **Clases implicadas:**
    * `FactoriaImportadoresImpl`: Clase responsable de instanciar el importador adecuado.
    * `ImportadorGastos`: Interfaz común del producto.
    * `CSVImportadorGastos`: Producto concreto.

**Justificación:**
Permite desacoplar el controlador de la lógica de creación de los importadores. Si en el futuro se desea añadir soporte para importar desde XML o JSON, solo habrá que añadir una nueva clase y modificar la factoría, cumpliendo con el principio de Abierto/Cerrado (OCP).

### 1.2. Patrón Singleton
**Contexto de uso:**
Utilizado para gestionar clases que deben tener una única instancia accesible globalmente en todo el ciclo de vida de la aplicación.

* **Clases implicadas:**
    * `Configuracion`: Mantiene el estado global de la sesión, como el `SceneManager` y la instancia principal del controlador `GestionGastos`.
    * Repositorios (`RepositorioCuentas`, etc.): Para asegurar que solo hay una fuente de verdad en el acceso a datos.

**Justificación:**
Es necesario garantizar que todos los controladores de vista accedan a la misma instancia del modelo de negocio y de los servicios de navegación. El Singleton centraliza este acceso y asegura la consistencia de los datos en memoria.

---

## 2. Patrones Estructurales (Structural Patterns)
*Tratan sobre cómo se componen las clases y objetos para formar estructuras mayores, facilitando el diseño y la eficiencia.*

### 2.1. Patrón Adaptador (Adapter)
**Contexto de uso:**
Se utiliza conjuntamente con la importación para transformar datos de fuentes externas (ficheros CSV de bancos) al modelo de dominio de la aplicación.

* **Clases implicadas:**
    * `ImportadorGastos` (Target): La interfaz que espera nuestra aplicación (`List<Gasto> importarGastos(File f)`).
    * `CSVImportadorGastos` (Adapter): Implementa la interfaz y "adapta" el contenido del fichero de texto plano, parseando las líneas y convirtiéndolas en objetos `Gasto` comprensibles por el sistema.

**Justificación:**
El sistema necesita trabajar con objetos `Gasto`, pero la información llega en formato de texto separado por comas. El adaptador encapsula toda la complejidad del parsing y manejo de flujos de entrada (Streams), protegiendo al resto de la aplicación de los detalles del formato externo.

### 2.2. Patrón Fachada (Facade)
**Contexto de uso:**
Se aplica en la clase principal de lógica de negocio para simplificar la interacción entre la interfaz de usuario y el complejo subsistema de persistencia y reglas de dominio.

* **Clases implicadas:**
    * `GestionGastos` (Fachada): Proporciona una interfaz unificada y simplificada (`registrarGasto`, `checkAlertas`, `filtrarGastos`) para los controladores de la vista.
    * Subsistema de Repositorios (`RepositorioCuentas`, `RepositorioGastos`...): Clases de acceso a datos que quedan ocultas.
    * Controladores de Vista (`GastoVistaControlador`, etc.): Clientes que interactúan solo con la fachada.

**Justificación:**
Reduce el acoplamiento entre la vista y el modelo. Los controladores de JavaFX no necesitan gestionar múltiples repositorios ni conocer la lógica interna de validación; simplemente delegan en `GestionGastos`, haciendo el código más limpio.

### 2.3. Patrón Composite (Composite)
**Contexto de uso:**
Utilizado en la construcción dinámica de interfaces gráficas (JavaFX), tratando de manera uniforme a los contenedores y a los elementos individuales.

* **Clases implicadas:**
    * `Component`: La clase base `Node`.
    * `Composite`: Contenedores como `VBox` o `HBox` (ej. `containerParticipantes` en `CrearCuentaCompartidaControlador`).
    * `Leaf`: Elementos simples como `TextField`.

**Justificación:**
Fundamental para la creación dinámica de formularios, como la lista de participantes. Permite añadir filas complejas (contenedores) a un contenedor padre tratándolos como un único nodo, simplificando la jerarquía visual.

---

## 3. Patrones de Comportamiento (Behavioral Patterns)
*Se centran en la comunicación entre objetos, distribuyendo responsabilidades y algoritmos.*

### 3.1. Patrón Estrategia (Strategy)
**Contexto de uso:**
Se ha utilizado en el sistema de **Alertas** para definir cómo se comprueba si un gasto pertenece al periodo de tiempo controlado (semanal o mensual).

* **Clases implicadas:**
    * `EstrategiaTemporal` (Interfaz): Define el contrato común `esMismoPeriodo(...)`.
    * `EstrategiaSemanal` y `EstrategiaMensual` (Implementaciones): Lógica específica para calcular fechas.
    * `AlertaImpl` (Contexto): Delega en la estrategia el cálculo temporal.

**Justificación:**
Permite cambiar el comportamiento de una alerta en tiempo de ejecución sin modificar el código de la clase `Alerta`. Elimina la necesidad de sentencias condicionales complejas dentro de la lógica de comprobación.

### 3.2. Patrón Plantilla (Template Method)
**Contexto de uso:**
Aplicado en la capa de Vista para gestionar el comportamiento común del **Menú Lateral** de navegación.

* **Clases implicadas:**
    * `MenuLateralControladorPlantilla` (Clase Abstracta): Define la estructura y métodos de navegación comunes.
    * Controladores de vista (`CuentaPersonalVistaControlador`, etc.): Heredan de la plantilla y reutilizan la lógica.

**Justificación:**
Evita la duplicación de código (DRY). La clase padre define el comportamiento estándar de los botones de navegación, permitiendo que todas las vistas compartan la misma lógica de transición.

### 3.3. Patrón Observador (Observer)
**Contexto de uso:**
Implementado mediante los *Listeners* de JavaFX para que la interfaz reaccione automáticamente a cambios de estado.

* **Clases implicadas:**
    * **Sujeto:** Propiedades de los controles de UI (ej. `comboFiltroPeriodo.valueProperty()` en `LimitesVistaControlador`).
    * **Observador:** El controlador que registra un `addListener(...)`.

**Justificación:**
Permite una interfaz reactiva. Cuando el usuario cambia un filtro, la vista se actualiza inmediatamente por sí sola sin necesidad de un botón de refresco manual, desacoplando la vista de la lógica de actualización.

---

## 4. Patrones de Arquitectura
*Definen la estructura global y organización del sistema.*

### 4.1. Patrón Modelo-Vista-Controlador (MVC)
**Contexto de uso:**
Estructura global de la aplicación.

* **Clases implicadas:**
    * **Modelo:** Clases de dominio (`Gasto`, `Alerta`) y lógica (`GestionGastos`).
    * **Vista:** Archivos `.fxml`.
    * **Controlador:** Clases `...VistaControlador`.

**Justificación:**
Separa la interfaz de usuario de la lógica de negocio, facilitando el mantenimiento y permitiendo modificar la vista sin afectar al procesamiento de datos.

### 4.2. Patrón Repositorio (Repository)
**Contexto de uso:**
Abstrae y encapsula el acceso a los datos.

* **Clases implicadas:**
    * Interfaces: `RepositorioCuentas`, `RepositorioGastos`.
    * Implementaciones: `RepositorioCuentasImpl`, etc.
    * Cliente: `GestionGastos`.

**Justificación:**
Desacopla la lógica de negocio de los detalles de infraestructura (persistencia). Permite cambiar el mecanismo de almacenamiento (memoria, BD, ficheros) sin tocar la lógica de negocio.