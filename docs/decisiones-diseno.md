# Decisiones de diseño

Aquí vamos apuntando las decisiones según las tomamos.

## Arquitectura

- Seguimos la arquitectura del TPV de la práctica 6 (MVC). Paquetes:
  - `negocio.modelo`: clases del dominio, sin JavaFX ni SQL.
  - `negocio.controladores`: operaciones que hace el usuario. Los usa tanto la interfaz gráfica como la línea de comandos.
  - `adapters.repository`: interfaces de los repositorios, y en `impl` sus implementaciones (memoria y SQLite).
  - `vista`: controladores de JavaFX y `SceneManager`.
- `Configuracion` es un Singleton que crea los repositorios y controladores. Hay una versión en memoria para probar y otra con SQLite. Para cambiar de una a otra solo se toca `App`.
- `SceneManager` se encarga de cargar los FXML y de cambiar de pantalla, para que las vistas no dependan unas de otras.
- Cada bloque tendrá su propio controlador (gastos, cuentas, alertas...) en vez de uno solo para todo.

## Tecnologías

- JavaFX 21 para la interfaz y SQLite con JDBC para guardar los datos.
- Guava para las precondiciones (diseño por contrato).
- log4j para los logs.

## Forma de trabajar

- Nadie sube directo a `main`. Cada tarea va en su rama (`feature/...` o `docs/...`) y se mergea con pull request.

## Pendiente de decidir

- Métodos de las interfaces de los repositorios.
- Si los gastos de una cuenta compartida cuentan también para las alertas y gráficos personales.
- Al importar una cuenta compartida que no existe, cómo saber quiénes son sus miembros.
- Dónde se crean las categorías predefinidas.
