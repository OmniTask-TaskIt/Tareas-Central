# Prompts de Creación del Proyecto - Microservicio `mstareas`

Este documento detalla la secuencia de prompts sugerida para la reconstrucción del microservicio de gestión de tareas utilizando una arquitectura hexagonal.

## 1. Inicialización y Arquitectura Base
**Prompt:**
> "Genera la estructura base de un microservicio de Spring Boot llamado `mstareas` usando Java 21. El proyecto debe seguir una **arquitectura hexagonal**. Crea los paquetes principales: `Domain` (Model, Repository, Service/Ports), `Application` (Services, Ports), e `Infrastructure` (Inbound/REST, Outbound/Persistence/Messaging). Incluye las dependencias necesarias en el `pom.xml`: Spring Web, Spring Data MongoDB, Spring AMQP (RabbitMQ), Lombok y Springdoc OpenAPI."

## 2. Modelo de Dominio y Repositorio
**Prompt:**
> "Define la entidad de dominio `Task` en `com.taskit.mstareas.Domain.Model`. La tarea debe tener campos como `id`, `ownerId`, `assignedUserId`, `description`, `price`, `category`, `status` y marcas de tiempo. Crea enums para `TaskStatus` (ACTIVE, IN_PROGRESS, COMPLETED, DISPUTED, etc.) y `TaskCategory`. Define la interfaz `TaskRepository` en el puerto de salida del dominio."

## 3. Implementación de la Persistencia (MongoDB)
**Prompt:**
> "Implementa el adaptador de persistencia para MongoDB. Crea un `TaskMongoDTO` en `Infrastructure.Outbound.Persistence.DTO` y una interfaz `TaskRepositoryMongo` que extienda `MongoRepository`. Implementa `TaskRepositoryMongoAdapter` para mapear entre la entidad de dominio y el DTO de MongoDB, cumpliendo con la interfaz del puerto de salida."

## 4. Casos de Uso y Lógica de Negocio (CRUD y Estados)
**Prompt:**
> "Implementa los casos de uso para la gestión de tareas. Crea interfaces (Ports Inbound) y sus implementaciones (Application Services) para:
> - `CreateTask`: Validar y guardar una nueva tarea.
> - `EditTask` / `DeleteTask`: Solo si el estado es `ACTIVE`.
> - `AcceptTask`: Asignar un usuario a la tarea y cambiar estado a `IN_PROGRESS`.
> - `CompleteTask`: Lógica coordinada entre dueño y asignado.
> - `ReportTask` / `DisputeTask`: Para manejo de conflictos.
> - `SearchTasks` y `GetTaskFeed`: Filtros por categoría, precio y texto."

## 5. Integración con Mensajería (RabbitMQ)
**Prompt:**
> "Configura la integración con otros microservicios usando RabbitMQ. Crea `RabbitConfig` en `Infrastructure.Outbound.Config` definiendo exchanges y colas para:
> - Notificar la creación de una tarea.
> - Solicitar la creación/eliminación de un chat cuando una tarea es aceptada o terminada.
> - Notificar al servicio de pagos cuando una tarea ha sido aceptada.
> Implementa `TaskRabbitAdapter` para publicar estos eventos."

## 6. Exposición de API REST
**Prompt:**
> "Crea el `TaskController` en `Infrastructure.Inbound`. Define los endpoints REST para todas las funcionalidades mencionadas, utilizando DTOs para las peticiones (`CreateTaskRequestDTO`, `AcceptTaskRequestDTO`, etc.). Implementa un `GlobalExceptionHandler` para manejar errores de negocio y validaciones, devolviendo códigos HTTP adecuados."

## 7. Configuración y Documentación
**Prompt:**
> "Configura el archivo `application.properties` para soportar variables de entorno para la conexión a MongoDB y RabbitMQ. Asegúrate de que Swagger UI esté habilitado para documentar la API. Crea un archivo `README.md` con instrucciones de ejecución y despliegue usando Docker para las dependencias."
