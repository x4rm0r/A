# ESPECIFICACIÓN DE HISTORIAS DE USUARIO

A continuación se detallan las historias de usuario que definen la funcionalidad del proyecto, junto con sus criterios de aceptación.

## 1. Registro de gastos
**Como** usuario  
**Quiero** registrar mis gastos personales indicando cantidad, fecha y concepto  
**Para** organizar el gasto realizado y tener un registro diario.

**Criterios de Verificación:**
* Es obligatorio introducir cantidad, fecha y categoría.
* El sistema debe confirmar que el gasto se ha guardado correctamente.

---

## 2. Categorizar gastos
**Como** usuario  
**Quiero** asociar cada gasto a una categoría (como alimentación, transporte o entretenimiento) y poder crear nuevas  
**Para** poder analizar en qué áreas gasto más dinero.

**Criterios de Verificación:**
* Se pueden crear nuevas categorías personalizadas.
* No se pueden duplicar nombres de categorías.

---

## 3. Editar gastos
**Como** usuario  
**Quiero** editar los detalles de un gasto ya registrado  
**Para** poder corregir algún error humano o modificar el importe si cambia.

**Criterios de Verificación:**
* Se pueden modificar todos los campos (fecha, cantidad, descripción).
* Al guardar, el saldo total de la cuenta debe recalcularse automáticamente.

---

## 4. Eliminar gastos
**Como** usuario  
**Quiero** eliminar alguno de mis gastos registrados  
**Para** que deje de verse reflejado en mi contabilidad si me he equivocado o he cancelado el pago.

**Criterios de Verificación:**
* El sistema debe eliminar el gasto de la lista permanentemente.
* El saldo de la cuenta debe actualizarse (restarse/sumarse) automáticamente tras borrarlo.

---

## 5. Consultar los gastos
**Como** usuario  
**Quiero** consultar mis gastos en forma de tabla o lista ordenada  
**Para** revisar mis movimientos con mayor facilidad y rapidez.

**Criterios de Verificación:**
* La lista debe mostrar la fecha, categoría y cantidad de cada gasto.
* Se debe poder ordenar la lista por fecha (ascendente/descendente).

---

## 6. Comprensión de distribución por categoría
**Como** usuario  
**Quiero** ver una representación visual de mis gastos (barras o sectores)  
**Para** entender de un vistazo en qué categorías se va mi dinero.

**Criterios de Verificación:**
* El gráfico debe agrupar los gastos por categoría y sumar sus importes.
* Se debe utilizar la librería gráfica de JavaFX.

---

## 7. Filtrar datos personales
**Como** usuario  
**Quiero** filtrar mis gastos por meses, intervalos de fechas y categorías específicas  
**Para** encontrar fácilmente gastos antiguos o analizar periodos concretos.

**Criterios de Verificación:**
* El sistema debe permitir buscar filtrando por lista de meses o intervalos de fechas.
* El sistema debe permitir combinar varios criterios (ej: "Transporte" en "Agosto").

---

## 8. Configurar alertas de gastos
**Como** usuario  
**Quiero** establecer límites de gasto (semanales o mensuales) vinculados a una categoría  
**Para** recibir una alerta automática cuando supere el presupuesto fijado.

**Criterios de Verificación:**
* Poder crear una alerta indicando el tipo (semanal/mensual), el importe máximo y la categoría.
* El sistema debe comprobar cada gasto nuevo contra las alertas activas en tiempo real.

---

## 9. Revisar notificaciones
**Como** usuario  
**Quiero** consultar un historial de notificaciones  
**Para** saber cuántas veces y cuándo he superado mis límites de gasto.

**Criterios de Verificación:**
* Debe existir una pantalla o listado con todas las notificaciones generadas.
* Cada notificación debe mostrar la fecha de generación y el mensaje de aviso.

---

## 10. Crear cuenta de gastos compartida
**Como** usuario  
**Quiero** crear una cuenta de gasto compartida añadiendo participantes  
**Para** agrupar a varias personas y gestionar gastos comunes (ej. viajes, piso).

**Criterios de Verificación:**
* Debe tener un nombre único de cuenta.
* Se debe definir la lista inicial de participantes.
* Todos los saldos iniciales de los participantes deben ser 0.

---

## 11. Registro de gasto en cuenta compartida
**Como** usuario de cuenta compartida  
**Quiero** registrar un gasto indicando quién lo pagó y cómo se reparte el coste  
**Para** que el sistema calcule automáticamente las deudas de cada uno.

**Criterios de Verificación:**
* El sistema debe permitir el reparto equitativo (partes iguales).
* El sistema debe permitir el reparto por porcentajes (validando que sumen 100%).
* Se deben actualizar los saldos individuales ("quién debe a quién") tras el registro.

---

## 12. Importar datos desde un fichero
**Como** usuario  
**Quiero** importar un fichero con datos de gastos externos  
**Para** incorporar múltiples gastos automáticamente sin tener que introducirlos uno a uno.

**Criterios de Verificación:**
* El sistema debe leer ficheros de texto/CSV con el formato predefinido.
* Si el formato es incorrecto, debe mostrar un error y no importar nada.

<br>

# REQUISITOS NO FUNCIONALES (RNF)

* **RNF-1:** La interfaz gráfica debe estar implementada con **JavaFX**.
* **RNF-2:** La persistencia de datos debe realizarse en formato JSON utilizando la librería **Jackson**.
* **RNF-3:** El código debe seguir el patrón **MVC** y utilizar el patrón **Repositorio** para el acceso a datos.
* **RNF-4:** Se deben utilizar **Expresiones Lambda y Streams** de Java 8+ para el procesamiento de colecciones.
* **RNF-5:** El proyecto debe gestionarse con **Maven** y control de versiones con **Git**.
* **RNF-6:** Uso de librerías externas para la visualización avanzada (ej. CalendarFX o gráficos nativos de JavaFX).