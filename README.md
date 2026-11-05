# Mstareas

Microservicio de gestión de tareas para **TaskIt**. Permite crear, editar, eliminar, buscar y asignar tareas, consultar historiales y coordinar la finalización entre el dueño y la persona asignada.

El servicio expone una API REST, guarda las tareas en MongoDB y publica eventos en Kafka para integrar los servicios de chat y pagos.

## Funcionalidades

- Crear tareas con dueño, descripción, precio y categoría.
- Editar o eliminar tareas que todavía están `ACTIVE`.
- Aceptar una tarea y asignarla a una persona.
- Reportar o disputar tareas.
- Aprobar la finalización desde el dueño y desde la persona asignada.
- Buscar por categoría, rango de precio o texto.
- Consultar feed por categorías e historiales por usuario.
- Publicar eventos Kafka para crear/eliminar chats y notificar pagos.
- Documentación interactiva mediante Swagger/OpenAPI.

## Tecnologías

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data MongoDB
- Spring Kafka
- Lombok
- Maven Wrapper
- Springdoc OpenAPI

## Requisitos

Instala o ten disponible:

- JDK 21 (`java -version`)
- Docker, si vas a ejecutar MongoDB y Kafka en contenedores
- Git

El `pom.xml` define Java 21. Usa esa versión para compilar y ejecutar el servicio.

## Configuración

La aplicación lee variables de entorno y también carga automáticamente un archivo `.env` en la raíz del proyecto. Crea `.env` con este contenido:

```properties
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/taskit
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
PORT=8080
```

`.env` está excluido del repositorio. No guardes credenciales ni secretos en Git.

| Variable | Obligatoria | Valor de ejemplo | Uso |
|---|---:|---|---|
| `SPRING_DATA_MONGODB_URI` | Sí | `mongodb://localhost:27017/taskit` | Conexión a MongoDB |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Sí | `localhost:9092` | Servidores Kafka |
| `PORT` | No | `8080` | Puerto HTTP; por defecto `8080` |

## Levantar dependencias localmente con Docker

### MongoDB

```bash
docker run -d \
  --name taskit-mongo \
  -p 27017:27017 \
  mongo:7
```

### Kafka

El siguiente comando inicia Kafka en modo KRaft, sin ZooKeeper, y expone el broker en `localhost:9092`:

```bash
docker run -d \
  --name taskit-kafka \
  --hostname kafka \
  -p 9092:9092 \
  -e KAFKA_CFG_NODE_ID=0 \
  -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093 \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  bitnami/kafka:3.8
```

Comprueba que ambos contenedores estén activos:

```bash
docker ps --filter name=taskit-mongo --filter name=taskit-kafka
```

Para detenerlos:

```bash
docker stop taskit-mongo taskit-kafka
```

Para eliminarlos después de detenerlos:

```bash
docker rm taskit-mongo taskit-kafka
```

Si ya tienes MongoDB o Kafka instalados, omite estos pasos y ajusta `.env`.

## Ejecutar la aplicación

Desde la raíz del repositorio:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Si el sistema no permite ejecutar el wrapper directamente:

```bash
bash mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

También puedes generar el artefacto y ejecutarlo:

```bash
./mvnw clean package
java -jar target/mstareas-0.0.1-SNAPSHOT.jar
```

## Verificar que está funcionando

En otra terminal:

```bash
curl http://localhost:8080/api/tasks/health
```

Respuesta esperada:

```json
{
  "status": "UP",
  "message": "Mstareas is running"
}
```

La documentación OpenAPI está disponible en:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Especificación OpenAPI: `http://localhost:8080/api-docs`

## API REST

Todos los endpoints empiezan con `/api/tasks`.

### Crear una tarea

```http
POST /api/tasks/create
Content-Type: application/json
```

```json
{
  "taskOwner": "user-owner-1",
  "taskName": "Reparar una bicicleta",
  "taskDescription": "Ajustar frenos y cambiar la cadena.",
  "price": 75000,
  "category": "ARREGLOS"
}
```

Ejemplo:

```bash
curl -X POST http://localhost:8080/api/tasks/create \
  -H 'Content-Type: application/json' \
  -d '{
    "taskOwner": "user-owner-1",
    "taskName": "Reparar una bicicleta",
    "taskDescription": "Ajustar frenos y cambiar la cadena.",
    "price": 75000,
    "category": "ARREGLOS"
  }'
```

La respuesta es `201 Created` e incluye un `taskId`. Guárdalo para las siguientes operaciones.

### Operaciones sobre una tarea

| Método | Ruta | Body | Resultado |
|---|---|---|---|
| `PUT` | `/api/tasks/{taskId}` | `taskName`, `taskDescription`, `price`, `category` | Edita una tarea `ACTIVE` |
| `DELETE` | `/api/tasks/{taskId}` | — | Elimina una tarea `ACTIVE`; responde `204` |
| `PATCH` | `/api/tasks/{taskId}/accept` | `taskAssignee` | Asigna la tarea y la cambia a `ASSIGNED` |
| `PATCH` | `/api/tasks/{taskId}/report` | `reason` | Cambia el estado a `REPORTED` |
| `PATCH` | `/api/tasks/{taskId}/dispute` | `reason` | Cambia el estado a `DISPUTED` |
| `PATCH` | `/api/tasks/{taskId}/complete` | `userId` | Registra una aprobación |

Ejemplos de bodies:

```json
// PUT /api/tasks/{taskId}
{
  "taskName": "Reparar bicicleta y luces",
  "taskDescription": "Ajustar frenos, cadena y luces.",
  "price": 85000,
  "category": "ARREGLOS"
}
```

```json
// PATCH /api/tasks/{taskId}/accept
{
  "taskAssignee": "user-worker-1"
}
```

```json
// PATCH /api/tasks/{taskId}/report
{
  "reason": "La tarea no coincide con la descripción publicada."
}
```

```json
// PATCH /api/tasks/{taskId}/dispute
{
  "reason": "El trabajo entregado requiere revisión."
}
```

```json
// PATCH /api/tasks/{taskId}/complete
{
  "userId": "user-owner-1"
}
```

Para marcar una tarea como `COMPLETED`, deben aprobarla ambos usuarios: el dueño y la persona asignada. Ejecuta el endpoint de finalización dos veces, una con cada `userId`.

### Búsqueda, feed e historiales

| Método | Ruta | Parámetros |
|---|---|---|
| `GET` | `/api/tasks/search` | `category`, `minPrice`, `maxPrice`, `keyword` opcionales |
| `GET` | `/api/tasks/feed` | `categories` obligatorio; puede repetirse |
| `GET` | `/api/tasks/history/created/{userId}` | Usuario dueño |
| `GET` | `/api/tasks/history/assigned/{userId}` | Usuario asignado |
| `GET` | `/api/tasks/history/completed/{userId}` | Usuario asignado con estado `COMPLETED` |

Ejemplos:

```bash
curl 'http://localhost:8080/api/tasks/search?category=ARREGLOS&minPrice=10000&maxPrice=100000&keyword=bici'
curl 'http://localhost:8080/api/tasks/feed?categories=ARREGLOS&categories=EDUCACION'
curl http://localhost:8080/api/tasks/history/created/user-owner-1
curl http://localhost:8080/api/tasks/history/assigned/user-worker-1
curl http://localhost:8080/api/tasks/history/completed/user-worker-1
```

Las categorías válidas son `ARREGLOS` y `EDUCACION`.

## Estados de una tarea

| Estado | Significado |
|---|---|
| `ACTIVE` | Publicada y disponible; se puede editar o eliminar |
| `ASSIGNED` | Tiene una persona asignada |
| `COMPLETED` | Dueño y persona asignada aprobaron la finalización |
| `REPORTED` | Fue reportada con un motivo |
| `DISPUTED` | Fue disputada con un motivo |
| `CANCELLED` | Estado reservado para cancelación |

## Eventos Kafka

El servicio publica eventos en estos topics:

| Topic | Cuándo se publica |
|---|---|
| `taskit.chat.events` | Al aceptar una tarea, para crear un chat; al completarla, para eliminarlo |
| `taskit.payment.events` | Al aceptar una tarea, para notificar el pago |
| `taskit.auth.dead-letter` | Topic configurado para mensajes que agoten reintentos |

La aceptación de una tarea requiere un Kafka disponible porque publica eventos de chat y pago. La persistencia requiere MongoDB.

## Verificación completa de un cambio

1. Levanta MongoDB y Kafka.
2. Crea `.env`.
3. Ejecuta la aplicación.
4. Comprueba `/api/tasks/health`.
5. Crea una tarea y copia el `taskId` de la respuesta.
6. Ejecuta una búsqueda para confirmar que quedó persistida.
7. Asígnala con `/accept` y comprueba que el estado sea `ASSIGNED`.
8. Aprueba la finalización con el dueño y luego con la persona asignada.
9. Comprueba que el estado final sea `COMPLETED`.
10. Consulta los historiales de ambos usuarios.

Para ejecutar las pruebas automatizadas:

```bash
./mvnw test
```

Si no puedes ejecutar `mvnw` directamente, usa `bash mvnw test`.

## Estructura del proyecto

```text
src/main/java/com/taskit/mstareas/
├── Application/
│   ├── Ports/Inbound/       Casos de uso de entrada
│   └── Services/            Implementación de casos de uso
├── Domain/
│   ├── Model/               Task, estados y categorías
│   └── Repository/          Puerto de persistencia
├── Infrastucture/Inbound/   Controller y DTOs REST
└── Infrastucture/Outbound/
    ├── Persistence/         Adaptador MongoDB
    └── Messaging/           Adaptador Kafka
```

La separación por puertos y adaptadores permite mantener la lógica de negocio independiente de HTTP, MongoDB y Kafka.

## Solución de problemas

- **`Connection refused` en MongoDB:** verifica que `taskit-mongo` esté activo y que `SPRING_DATA_MONGODB_URI` use el puerto correcto.
- **`Connection refused` en Kafka:** verifica que `taskit-kafka` esté activo y que `SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092`.
- **El puerto 8080 está ocupado:** define otro puerto en `.env`, por ejemplo `PORT=8081`, y usa ese puerto en los comandos `curl`.
- **`java version` no es 21:** instala JDK 21 y selecciona esa versión antes de ejecutar Maven.
- **Errores de compilación relacionados con Lombok:** confirma que Maven está ejecutándose con JDK 21 y vuelve a ejecutar `./mvnw clean test`.
