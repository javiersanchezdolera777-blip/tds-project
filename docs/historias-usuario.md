# Historias de Usuario — Gestión de Gastos

## Resumen de Historias de Usuario

| ID        | Historia de Usuario                                           | Épica                    | Prioridad |
| :-------- | :------------------------------------------------------------ | :----------------------- | :-------- |
| **HU-01** | Registrar gasto personal desde la interfaz gráfica            | Gastos y Categorías      | Alta      |
| **HU-02** | Editar y eliminar gastos personales desde la GUI              | Gastos y Categorías      | Alta      |
| **HU-03** | Gestionar categorías de gasto (predefinidas y personalizadas) | Gastos y Categorías      | Alta      |
| **HU-04** | Gestionar gastos desde línea de comandos (CLI)                | Gastos y Categorías      | Alta      |
| **HU-05** | Consultar gastos en formato tabla/lista                       | Consulta y Visualización | Alta      |
| **HU-06** | Filtrar gastos por fechas, meses y categorías                 | Consulta y Visualización | Alta      |
| **HU-07** | Visualizar distribución de gastos en gráficos                 | Consulta y Visualización | Media     |
| **HU-08** | Visualizar gastos en calendario                               | Consulta y Visualización | Media     |
| **HU-09** | Configurar y gestionar alertas de límite de gasto             | Alertas y Notificaciones | Alta      |
| **HU-10** | Recibir notificaciones y consultar historial                  | Alertas y Notificaciones | Alta      |
| **HU-11** | Crear cuenta de gasto compartida                              | Cuentas Compartidas      | Alta      |
| **HU-12** | Registrar gastos compartidos y consultar saldos               | Cuentas Compartidas      | Alta      |
| **HU-13** | Importar gastos desde ficheros externos                       | Importación              | Media     |

## Épica 1: Gestión de Gastos y Categorías

### HU-01: Registrar gasto personal (GUI)
**Como** usuario de la aplicación,  
**quiero** registrar un nuevo gasto indicando su importe, fecha y categoría (y opcionalmente una descripción),  
**para** llevar un control persistente de mis gastos personales.

**Criterios de aceptación:**
- El importe debe ser un valor numérico estrictamente mayor que 0.
- La fecha y la categoría son obligatorias (la categoría debe existir previamente en el sistema).
- Al confirmar, el gasto se persiste en la base de datos SQLite y queda disponible al reiniciar la aplicación.
- Tras guardar el gasto, el sistema evalúa automáticamente las alertas activas que puedan verse afectadas.
- Si algún campo obligatorio falta o tiene un formato inválido, el sistema rechaza el registro e informa al usuario.

---

### HU-02: Editar y eliminar gasto personal (GUI)
**Como** usuario,  
**quiero** modificar o eliminar cualquiera de mis gastos registrados en cualquier momento,  
**para** corregir errores de introducción o descartar movimientos incorrectos.

**Criterios de aceptación:**
- El usuario puede seleccionar un gasto existente y modificar su importe, fecha, categoría o descripción, manteniendo las mismas validaciones que en el alta (importe > 0, fecha y categoría válidas).
- El usuario puede eliminar un gasto existente previa confirmación.
- Tanto la modificación como el borrado se reflejan inmediatamente en las vistas activas y quedan persistidos en la base de datos.
- Tras modificar un gasto, el sistema reevalúa automáticamente las alertas activas que puedan verse afectadas por el cambio (y, si el gasto pertenece a una cuenta compartida, cualquier edición o borrado recalcula los saldos pendientes de sus miembros).

---

### HU-03: Gestionar categorías de gasto
**Como** usuario,  
**quiero** disponer de categorías predefinidas y poder crear nuevas categorías personalizadas,  
**para** organizar mis gastos según mis necesidades reales.

**Criterios de aceptación:**
- Al iniciar la aplicación por primera vez, existen categorías predefinidas disponibles (al menos: *Alimentación*, *Transporte* y *Entretenimiento*).
- El usuario puede crear una nueva categoría introduciendo un nombre no vacío.
- No se permite crear dos categorías con el mismo nombre (ignorando mayúsculas/minúsculas y espacios sobrantes).
- Las nuevas categorías creadas se persisten y aparecen inmediatamente en los selectores de alta, edición, filtrado y alertas.

---

### HU-04: Gestionar gastos desde línea de comandos (CLI)
**Como** usuario avanzado,  
**quiero** poder registrar, modificar y borrar gastos mediante una interfaz básica de línea de comandos,  
**para** gestionar mis gastos de forma rápida sin necesidad de abrir las pantallas gráficas.

**Criterios de aceptación:**
- La aplicación ofrece un modo o punto de entrada por consola que permite ejecutar operaciones de alta, edición y borrado de gastos.
- Las operaciones por CLI utilizan exactamente la misma lógica de negocio, validaciones y capa de persistencia que la interfaz gráfica.
- Cualquier cambio realizado desde la CLI queda persistido en SQLite y es visible al abrir la interfaz JavaFX (y viceversa).
- Ante comandos o parámetros erróneos, la CLI muestra un mensaje claro de error y ayuda de uso sin finalizar de forma abrupta.

---

## Épica 2: Consulta, Filtrado y Visualización

### HU-05: Consultar gastos en formato tabla/lista
**Como** usuario,  
**quiero** consultar mis gastos registrados en una vista de tabla o lista,  
**para** revisar de un vistazo cada movimiento con su fecha, categoría e importe.

**Criterios de aceptación:**
- Se muestran todos los gastos persistidos con columnas claras (fecha, categoría, importe y descripción si aplica).
- La vista se actualiza automáticamente tras registrar, editar, eliminar o importar gastos.
- Si no existen gastos que mostrar, la vista indica de forma clara que la lista está vacía.

---

### HU-06: Filtrar gastos
**Como** usuario,  
**quiero** filtrar mis gastos por una lista de meses, por un intervalo de fechas personalizado, por una lista de categorías o por cualquier combinación de estos criterios,  
**para** analizar únicamente los movimientos que me interesan.

**Criterios de aceptación:**
- Se puede filtrar por uno o varios meses seleccionados (por ejemplo, julio y agosto).
- Se puede filtrar por un intervalo de fechas (`fechaInicio` y `fechaFin`), validando que `fechaInicio <= fechaFin`.
- Se puede filtrar por una o varias categorías simultáneamente.
- Todos los filtros se pueden combinar libremente (por ejemplo: categoría *Transporte* en los meses de *julio* y *agosto*).
- Existe una opción para limpiar/restablecer todos los filtros y volver a mostrar el conjunto completo.
- El filtro activo afecta de forma coherente a las vistas de datos mostradas.

---

### HU-07: Visualizar distribución de gastos mediante gráficos
**Como** usuario,  
**quiero** visualizar mis gastos en diagramas de barras y/o circulares agrupados por categoría,  
**para** comprender visualmente en qué partidas gasto más dinero.

**Criterios de aceptación:**
- El sistema muestra al menos un gráfico (barras y/o circular) que agrupa el importe total gastado por cada categoría.
- Los gráficos respetan los filtros activos aplicados por el usuario (HU-06).
- Los gráficos se actualizan cuando cambian los gastos o los filtros, sin mostrar categorías con datos inconsistentes.

---

### HU-08: Visualizar gastos en formato calendario
**Como** usuario,  
**quiero** ver mis gastos distribuidos sobre un calendario visual (por ejemplo, mediante CalendarFX en vista *Full Day*),  
**para** identificar fácilmente en qué días del mes concentro mis gastos.

**Criterios de aceptación:**
- Cada gasto aparece ubicado en el día correspondiente a su fecha.
- Se muestra información resumida del gasto (importe y categoría) dentro de la entrada del calendario.
- La vista del calendario refleja los filtros activos y los cambios realizados sobre los gastos.

---

## Épica 3: Alertas y Notificaciones

### HU-09: Configurar y gestionar alertas de límite de gasto
**Como** usuario,  
**quiero** definir alertas con un importe límite semanal o mensual, de ámbito general o asociadas a una categoría concreta,  
**para** controlar no superar mi presupuesto.

**Criterios de aceptación:**
- Al crear una alerta se debe indicar: importe límite (> 0), periodicidad (*semanal* o *mensual*) y, opcionalmente, una categoría específica.
- Si no se especifica categoría, la alerta evalúa la suma de todos los gastos del periodo; si se especifica, solo evalúa los gastos de esa categoría.
- Las alertas configuradas se persisten en base de datos y pueden consultarse o eliminarse en cualquier momento.

---

### HU-10: Recibir notificaciones y consultar historial
**Como** usuario,  
**quiero** que el sistema genere una notificación cuando supere el límite de una alerta y poder revisar las notificaciones pasadas en cualquier momento,  
**para** ser consciente al instante de mis excesos de gasto y tener un registro histórico.

**Criterios de aceptación:**
- Cuando un nuevo gasto (o edición/importación) hace que el acumulado del periodo supere el límite de una alerta activa, el sistema genera automáticamente una notificación con fecha, detalle de la alerta superada e importe acumulado.
- El usuario puede visualizar las notificaciones nuevas y marcarlas como leídas.
- Todas las notificaciones generadas se persisten y pueden consultarse en una sección de historial de notificaciones en cualquier momento.

---

## Épica 4: Cuentas de Gasto Compartidas

### HU-11: Crear cuenta de gasto compartida
**Como** usuario,  
**quiero** crear cuentas compartidas con un grupo de personas eligiendo entre reparto equitativo o porcentajes personalizados,  
**para** gestionar gastos en común con amigos, pareja o compañeros de piso.

**Criterios de aceptación:**
- Una cuenta requiere un nombre y una lista de al menos 2 personas (identificadas por su nombre, sin duplicados dentro de la misma cuenta).
- Por defecto, la distribución es equitativa (`100% / N` para cada persona).
- Opcionalmente, el usuario puede asignar un porcentaje de participación (> 0) a cada persona; el sistema valida obligatoriamente que la suma total sea exactamente el 100%.
- Al crearse la cuenta, el saldo pendiente inicial de todas las personas es `0.00 €`.
- **Inmutabilidad:** una vez creada la cuenta, su lista de personas no puede modificarse (ni añadir ni eliminar miembros).
- La cuenta y sus miembros/porcentajes quedan persistidos en base de datos.

---

### HU-12: Registrar gastos en cuenta compartida y consultar saldos
**Como** usuario miembro de una cuenta compartida,  
**quiero** registrar gastos indicando qué persona del grupo los ha pagado y consultar el saldo pendiente actualizado de cada miembro,  
**para** saber exactamente cuánto dinero debe o le deben a cada persona.

**Criterios de aceptación:**
- Todo gasto registrado en una cuenta compartida debe estar asociado obligatoriamente a una persona perteneciente a dicha cuenta (el pagador), además de indicar importe (> 0), fecha y categoría.
- Al registrar un gasto de importe `I` pagado por la persona `P`:
  - A cada miembro `i` de la cuenta se le descuenta su cuota correspondiente (`I * porcentaje_i / 100`).
  - Al pagador `P` se le suma el importe total `I` pagado (variación neta: `+I * (1 - porcentaje_P / 100)`).
- La suma de los saldos de todas las personas de la cuenta se mantiene siempre igual a `0` (salvo redondeos de céntimo controlados).
- El usuario puede consultar en cualquier momento el saldo positivo (le deben) o negativo (debe al grupo) de cada persona y el listado de gastos de la cuenta.

---

## Épica 5: Importación de Datos Externos

### HU-13: Importar gastos desde ficheros externos
**Como** usuario,  
**quiero** importar gastos desde un fichero de texto plano externo (como el CSV exportado de una plataforma bancaria),  
**para** incorporar mis movimientos automáticamente sin introducirlos uno a uno.

**Criterios de aceptación:**
- El usuario puede seleccionar un fichero externo desde la aplicación e iniciar la importación.
- El sistema procesa el formato CSV bancario proporcionado (en codificación UTF-8, formato de fecha `M/d/yyyy H:mm`), mapeando `Subcategory` como categoría del gasto (creándola automáticamente si no existe), `Note` como descripción, `Amount` como importe, `Account` como cuenta y `Payer` como pagador.
- Los registros cuya cuenta sea `Personal` se incorporan como gastos personales; los pertenecientes a cuentas compartidas (`Compartida 1`, `Compartida 2`, etc.) se asocian a la cuenta correspondiente (creándola con reparto equitativo y todos los pagadores detectados en el fichero si aún no existe).
- Tras la importación, todos los gastos, categorías y cuentas generadas quedan persistidos y se evalúan las alertas activas.
- El diseño de importación está desacoplado del formato concreto (preparado para incorporar nuevos formatos en el futuro sin modificar la lógica de negocio).
- Si el fichero no existe, tiene un formato no soportado o contiene líneas mal formadas, el sistema informa del error sin corromper los datos existentes.
