# Proyecto Final TDS 2025 - Gestión de Gastos

## 👥 Integrantes del Grupo
* **Javier Galindo Garre** - [javier.galindog@um.es] - Subgrupo: [2.1]
* **Jose Diego Segura Solano** - [jd.segurasolano@um.es] - Subgrupo: [2.1]
* **Hugo Egea Jara** - [h.egeajara@um.es] - Subgrupo: [2.1]

---

## 📝 Descripción del Proyecto
Este proyecto es una aplicación de escritorio desarrollada en **Java y JavaFX** para la gestión eficiente de finanzas personales y compartidas. Permite a los usuarios registrar y filtrar gastos, clasificar movimientos por categorías, establecer límites de presupuesto con alertas visuales y gestionar el balance de deudas en cuentas compartidas entre múltiples participantes.

---

## 🚀 Cómo ejecutar el proyecto
Desde la terminal, en la carpeta raíz del proyecto. Asegúrate de tener **Java JDK** y **Maven** instalados. Ejecuta lo siguiente:

### Opción A: Ejecución estándar

```bash
mvn clean javafx:run
```

### Opción B: Ejecución con modo Terminal
```bash
mvn clean javafx:run -Djavafx.args="--terminal"
```
---

## 📚 Referencias y Recursos Técnicos
El diseño de la interfaz y la implementación de componentes avanzados se han realizado siguiendo las especificaciones oficiales y guías de estilo:

* **[CalendarFX Developer Guide](https://dlsc-software-consulting-gmbh.github.io/CalendarFX/):** Documentación técnica utilizada para la integración y personalización del calendario.
* **[JavaFX CSS Reference Guide (v21)](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/doc-files/cssref.html):** Guía oficial de Oracle/OpenJFX usada para el diseño de los archivos (`.css`).
* **[Jenkov JavaFX Tutorial](https://jenkov.com/tutorials/javafx/css-styling.html):** Referencia consultada para la estructura de los archivos (`.css`).

---

## 🔗 Documentación relevante
La documentación detallada se encuentra organizada en la carpeta [`/docs`](./docs):

* [**Diagrama de Clases UML**](./docs/imagenes/UML_TDS.png) - Dominio del proyecto.
* [**Historias de Usuario**](./docs/historias_usuario.md) - Especificación de las historias de usuario.
* [**Diagrama de interacción**](./docs/imagenes/Diagrama_interacción.png) - Un diagrama de interacción.
* [**Arquitectura y Patrones**](./docs/patrones_usados.md) - Decisiones de diseño y patrones aplicados.
* [**Manual de Usuario**](./docs/manual_usuario.md) - Guía visual de uso.
