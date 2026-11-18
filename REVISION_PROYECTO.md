# Revisión inicial de Lectio — 12 de noviembre de 2026

## Alcance y diagnóstico

Aplicación Spring Boot 3.3.4 / Java 17, Thymeleaf, PostgreSQL y Spring Security.
Se inventariaron las 41 plantillas, recursos estáticos, rutas del controlador web,
configuración de acceso y lógica de cancelación de reservas. No es una certificación
de todos los servicios ni una prueba completa con datos reales.

La inconsistencia visual proviene de CSS y JavaScript duplicados dentro de cada
plantilla. La portada, acceso, catálogo y reservas usan el diseño editorial;
administración, optimizador, perfil y desafíos conservaban paletas y efectos diferentes.
Se mantiene como referencia el verde oscuro, fondo claro y combinación Inter / Instrument Serif.

## Correcciones aplicadas

- Animaciones compartidas en `static/js/lectio-ui.js` y `static/estilos/lectio-ui.css`.
  El contenido es visible por defecto. Cada tarjeta entra de forma independiente,
  sin esperar a que un porcentaje de una cuadrícula larga quepa en la pantalla.
  Hay alternativa sin IntersectionObserver, respeto a movimiento reducido y restauración
  al volver mediante el historial. Los videos de portada se pausan fuera de vista.
- Menú móvil: acceso a iniciar sesión, cierre visible, Escape, estado accesible,
  enlaces cerrados fuera del recorrido de teclado y limpieza al pasar a escritorio.
- Acceso: sin loader ni esperas artificiales, sin desenfoque entre formularios,
  campos identificados y formulario inactivo sin foco. Envío nativo para respetar
  errores y redirecciones de Spring; mensajes de URL mostrados como texto seguro.
- Spring Security usa `/api/login`; `/login` queda como alias válido.
- Buscador público funcional, normalización de acentos, contador y estado sin resultados.
- Base visual compartida en `static/estilos/lectio-theme.css`: administración,
  perfil, optimizador, inicio de desafíos, biblioteca de desafíos, historia y los tres desafíos.
  Menos brillos, saltos, burbujas, gradientes e iconos decorativos en movimiento.
- Portada sin cifras históricas inventariadas como constantes, redes sociales vacías
  ni suscripción simulada. La llamada final lleva al registro existente.
- Optimizador: explicación matemática disponible en un desplegable, con prioridad
  para los campos útiles al lector. Enlaces de navegación corregidos.
- Chat: menos etiquetas técnicas; las respuestas se insertan como texto escapado
  para no ejecutar HTML generado. Por ahora se sacrifica el formato Markdown enriquecido.
- Introducción: enlace de salida que funciona sin JavaScript, salida si se bloquea
  la reproducción automática y omisión con movimiento reducido.
- Vistas faltantes o con capitalización incorrecta: registro, configuración, pago,
  acceso restringido y catálogo por categoría. El antiguo carrito lleva a reservas.
- El pago anterior no tenía procesador ni método POST y podía enviar datos de tarjeta
  por URL. Se reemplazó por un estado explícito de servicio no disponible.
- Las rutas administrativas exigen ADMIN, incluso cuando se accede directamente por URL.
- Cancelación de reservas: autorización del propietario o administrador dentro de la
  operación transaccional, antes de modificar existencias.
- Registro: se rechazan nombres vacíos o de solo espacios. Se retiró la dependencia
  JPA duplicada del `pom.xml`.

## Validación realizada

- Compilación Maven correcta.
- 7 pruebas Java aprobadas: existencia exacta de plantillas, registro vacío, enlaces
  antiguos, redirección al login, permisos administrativos y propiedad de reservas.
- 4 pruebas Node aprobadas: entrada de tarjetas, ausencia/fallo del observador,
  movimiento reducido y restauración del historial.
- 28 scripts inline analizados sin errores de sintaxis; recursos locales referenciados encontrados.
- Revisión visual en móvil y escritorio mediante servidor de vista previa local:
  portada, menú, acceso/registro, catálogo, inicio de desafíos, desafío 1984,
  administración y optimizador. Se verificaron Escape y búsqueda vacía/recuperación.
- `git diff --check` sin errores.

Comandos reproducibles:

```powershell
.\mvnw.cmd -q '-Dtest=WebUiRegressionTests,SecurityNavigationTests,ReservaOwnershipTests' test
node --test src/test/frontend/motion.test.cjs
```

La prueba original `BiaApplicationTests.contextLoads` falla porque `DB_URL` no está
definida en la terminal. No se alteró ni deshabilitó esa prueba. La aplicación ya
abierta en el puerto 8080 no estaba sirviendo los recursos nuevos; la verificación
visual se hizo aparte, sin sustituir su proceso ni modificar registros reales.
Es necesario recompilar y reiniciar la ejecución habitual para ver estos cambios allí.

## Pendiente para la siguiente revisión

1. Configurar una base de pruebas y recorrer registro, login por rol, reservas,
   devolución, multas, progreso, optimización y chat con Thymeleaf y datos reales.
   La vista previa estática no valida expresiones Thymeleaf ni servicios externos.
2. Migrar encabezados y pies duplicados a fragmentos Thymeleaf y extraer el CSS
   inline restante. La capa compartida actual unifica la apariencia, pero no termina
   la migración estructural de cada pantalla.
3. Revisar CSRF (actualmente deshabilitado), validación de payloads de progreso y
   configuración duplicada de repositorios. No forman parte de la validación visual.
4. Decidir si se eliminan o migran las plantillas sin retorno literal desde los
   controladores: `addMulta`, `comfirmacionLibro`, `editarReserva`, `eliminarReserva`,
   `guardarReserva` y `no`. Se conservaron para no borrar flujos sin confirmar su uso.
5. Confirmar datos de contacto, catálogo de muestra, textos promocionales restantes
   y portadas externas. Todavía hay contenidos fijos que no proceden de PostgreSQL.
6. Recuperar Markdown del chat mediante un sanitizador mantenido, con pruebas de XSS;
   no volver a insertar directamente `marked.parse(...)` en `innerHTML`.


## Actualización visual — 13 de noviembre de 2026

Se conservaron los cambios recientes del usuario y se tomó como referencia de movimiento
el HTML de Madeagro facilitado en Downloads. Se añadieron capas específicas para la barra
editorial (lectio-experience.css), los mundos de lectura (lectio-adventure.css) y
las entradas e interacciones (lectio-motion.css y lectio-experience.js).

La barra se comparte visualmente en optimizador, resultado, inicio de desafíos, libros,
pensadores y los tres desafíos. El inicio incorpora una ilustración SVG animada y accesos
a cada libro. Los nodos conservan sus clases de bloqueo y progreso; cada mundo tiene
su paleta y sus fondos. Las animaciones no mantienen contenido oculto por defecto.
El chulo usa longitud de trazo normalizada para evitar el segmento residual inicial.

No se ejecutaron pruebas, compilaciones ni navegadores en esta actualización, por
petición del usuario. La revisión de esta tanda fue exclusivamente sobre los archivos.
No se modificaron controladores, servicios, consultas ni la persistencia de progreso.

### Cierre — 17 de noviembre de 2026

Se corrigió el cierre del menú móvil compartido para retirar también su capa
superpuesta al pulsar Escape o volver a escritorio. Se protegió la asociación
del buscador con su formulario cuando la plantilla no lo incluye, evitando que
interrumpa la inicialización posterior de las interacciones.
No se ejecutaron pruebas, compilaciones ni vistas en navegador, por petición
del usuario. La apariencia y los flujos quedan sin validación en ejecución.

## Ajustes a partir de las capturas

Se extendió la navegación de Optimizador a Inicio de usuario, Reserva y Libros virtuales. Se independizó el formulario de salida de los estilos de página y se añadió confirmación accesible a las salidas directas de la barra. Los diálogos de salida y capítulos tienen ancho limitado, capa completa y desplazamiento vertical interno; los capítulos incluyen cierre y control del foco.

Se añadieron fondos SVG locales para los tres libros, reutilizando la mascota como Lio en el chatbot. Se compactaron los mensajes y se renovó la tarjeta pública de lectura. Se retiró el zoom del fondo de bienvenida, se acortaron las entradas y se actualizaron los copyrights a 2026 sin alterar fechas de registros.

Trabajo realizado sobre archivos, sin pruebas, compilaciones ni navegador por indicación del usuario. No se modificó lógica de negocio en esta tanda. Pendiente comprobar el aspecto en ejecución.

## Identidad del árbol y corrección de lectura

Logo SVG recreado a partir de la referencia del usuario, aplicado a las marcas de cabecera, acceso, administración, pies y favicon. Se sustituyó el morado del búho, Desafío y chat por azules petróleo y tonos cálidos. Se retiró Biblioteca digital del login. El acceso a Lio incorpora etiqueta, movimiento vertical y acceso por teclado; respeta movimiento reducido. Las entradas comparten un único motor para evitar animaciones CSS simultáneas.

Los tres desafíos convierten los capítulos del servidor (base 1) a índices de interfaz (base 0). Cerrar ya no completa capítulos. Completar espera confirmación del POST antes de actualizar las marcas; muestra error y permite reintento si falla. Se evita doble envío y se conserva la petición al navegar mediante keepalive. No se alteraron los registros existentes.

Sin pruebas, compilaciones ni navegador por indicación del usuario; revisión exclusivamente de código.

## Intro móvil y biblioteca de demostración

La intro móvil sustituye el vídeo por una escena SVG/CSS de Lio volando con un mapa; no asigna una fuente de vídeo en móvil. Conserva el vídeo de escritorio, entrada manual, transición automática de 8,5 segundos y movimiento reducido. Referencia de composición: intro del index.html facilitado por el usuario; arte propio del proyecto.

El catálogo de usuario incorpora portadas tipográficas locales con título y autor, tarjetas y una nota de demostración. Todos sus botones Leer apuntan a /api/lector-demo, ruta autenticada con plantilla propia y cuento original de cuatro capítulos. El lector ofrece navegación, tamaño de texto, modo noche y punto de lectura local con fallback si el almacenamiento falla. Sin JavaScript muestra el cuento completo. No guarda progreso en los desafíos.

No se ejecutaron pruebas, compilaciones ni navegador, según la indicación vigente del usuario.

## Ajustes de duración, menú y reservas

Intro móvil reducida a cuatro segundos y temporización visual sincronizada. Menú móvil compartido mediante dialog nativo en páginas públicas, de usuario y administración; conserva enlaces de cada contexto, cierre con Escape, foco y bloqueo del desplazamiento del fondo. No reemplaza acciones del escritorio.

Se retiró el aviso superior solicitado del lector y se diseñó su enlace de regreso como botón. Las portadas se mantienen en un plano 2D, eliminando la combinación de perspectiva, rotación y recorte al pasar el cursor. Reserva utiliza las mismas portadas tipográficas y tarjetas del catálogo, manteniendo stock, datos y manejadores de reserva.

Sin navegador, compilación ni pruebas por petición del usuario.

## Lecturas, administración, registro y privacidad — 18 de noviembre de 2026

Las cuatro tarjetas de la portada enlazan a muestras originales con rutas públicas explícitas. La continuación está en rutas autenticadas y el acceso conserva el destino de lectura mediante una lista cerrada de rutas. Se crearon siete lecturas temáticas independientes, incluidas las tres del inicio de usuario, con contenido original y progreso local separado por lectura.

Administración comparte shell, tarjetas, formularios y tablas editoriales; una sola barra con Más conserva las herramientas adicionales y cierre de sesión con confirmación. Se retiró Weka del texto visible. No se modificó el modelo predictivo.

Las muestras reutilizan la navegación pública y el botón de regreso tiene contraste en fondos oscuros. El chat escapa HTML antes de aplicar formato limitado de negritas, énfasis, listas y párrafos, incluso durante la respuesta, con entrada animada.

Registro exige aceptación de términos en ambos formularios y en el controlador. Se añadieron términos y privacidad orientados al proyecto educativo. El panel guarda la elección en localStorage y bloquea Google Fonts e iframes externos hasta recibir permiso; permite cambiarla. Los demás recursos externos se explican separadamente en privacidad. No se declara una auditoría legal ni de cookies.

No se ejecutaron pruebas, compilaciones ni navegador según petición del usuario. Se ajustó la llamada del test existente al nuevo parámetro de aceptación para conservar su firma, sin ejecutarlo.

## Cookies compactas y seis desafíos

Aviso más pequeño en escritorio y móvil, blanco cálido y verde editorial. Retirado el botón flotante; preferencias accesibles desde Privacidad con enlaces discretos en los pies existentes. Se conserva bloqueo de recursos opcionales y elección guardada.

Tres nuevos desafíos de relatos originales: La llave de las estaciones (jardin), La habitación del último reloj (reloj), La isla que cambiaba de nombre (isla). Cada uno tiene tres capítulos, portada y fondo SVG propios, ruta autenticada e integración con guardar-capitulo. El servicio existente otorga 100 puntos al completar cada libro y evita repetir la recompensa por el mismo identificador. La tabla y el total del catálogo contemplan seis libros; actualización al volver mediante caché de navegación.

Tarjetas de Inicio y Libros convertidas en carrusel con flechas, deslizamiento táctil, avance automático de 5,5 segundos y control de pausa. Respeta movimiento reducido y detiene el avance con interacción. Sin navegador, compilación ni pruebas por instrucción del usuario.

## Continuidad del vídeo, carrusel y chat

El vídeo de portada ya no se pausa por las medidas de su posición durante la carga; solo por pestaña oculta o movimiento reducido. Se añade preload auto. El carrusel avanza cada 2,5 segundos sin control de pausa visible y reinicia su intervalo tras una acción manual; conserva movimiento reducido.

Optimizador usa /api/chat/rag, la ruta existente. La conexión SSE envía encabezados de no buffering y un comentario inicial, limita la espera a 60 segundos y permite el redispatch ASYNC tras autenticar la petición inicial. El servicio interpreta eventos SSE del proveedor con el decodificador correspondiente y completa el flujo también cuando el proveedor cierra sin marcador final. No reintenta un stream parcialmente recibido. Consultas locales con transacción de lectura para asociaciones JPA.

El campo del chat tiene un único borde y foco, y el área de mensajes absorbe el scroll. Se añade reintento de la pregunta sin duplicar mensajes. Sin ejecución, navegador ni pruebas por petición del usuario; disponibilidad y credenciales del proveedor no verificadas.

## Limpieza de logs y Lio en catálogo/reservas

Retiradas las impresiones rutinarias de ChatController, ChatbotService y WebController: no se imprimen preguntas, usuarios, contexto recuperado, fragmentos de respuestas, conteos de libros ni mensajes de finalización. Se mantienen los diagnósticos de error del servicio. Retirado también el conteo de documentos de RetrievalService.

Libros virtuales de usuario y Reserva incluyen el panel de Lio, estilos base y el mismo comportamiento compartido con Inicio mediante lectio-chat.js. Se conserva /api/chat/rag, sugerencias, Markdown seguro, respuesta progresiva, reintento y limpieza. Posición separada del botón de subir. No se ejecutaron pruebas ni navegador por petición del usuario.
