# TP Integrador DAW1 - Sistema de Reservas de Canchas

**Alumna:** Cardozo, Sabrina  
**Enunciado elegido:** Canchas

## Descripción

Sistema web desarrollado con Java y Spring Boot para administrar
canchas deportivas, clientes y reservas.

Permite registrar canchas y clientes, realizar reservas,
consultar disponibilidad y gestionar estados.

## Cómo ejecutarlo

Requisitos:
- Java 21
- Maven Wrapper incluido en el proyecto

Desde la carpeta principal ejecutar:

```bash
./mvnw spring-boot:run
```

La API funciona en:

http://localhost:8080/api

Base de datos: H2.

## Modelo de datos

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| Cancha | id, nombre, deporte, precioHora | Tiene muchas reservas |
| Cliente | id, nombre, telefono | Tiene muchas reservas |
| Reserva | id, inicio, horas, estado, precioTotal | Pertenece a una cancha y un cliente |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /api/canchas | Listar canchas |
| GET | /api/canchas/{id} | Buscar cancha |
| POST | /api/canchas | Crear cancha |
| PUT | /api/canchas/{id} | Modificar cancha |
| DELETE | /api/canchas/{id} | Eliminar cancha |
| GET | /api/clientes | Listar clientes |
| GET | /api/clientes/{id} | Buscar cliente |
| POST | /api/clientes | Crear cliente |
| PUT | /api/clientes/{id} | Modificar cliente |
| DELETE | /api/clientes/{id} | Eliminar cliente |
| GET | /api/reservas | Listar reservas |
| GET | /api/reservas/{id} | Buscar reserva |
| POST | /api/reservas | Crear reserva |
| PATCH | /api/reservas/{id}/estado | Cambiar estado |
| GET | /api/canchas/{id}/reservas?fecha=AAAA-MM-DD | Reservas por fecha |
| GET | /api/clientes/{id}/reservas | Reservas de un cliente |

## Reglas de negocio

1. Una cancha no puede tener reservas superpuestas.
2. Las reservas canceladas liberan el horario.
3. La duración permitida es de 1 a 3 horas.
4. El precio total se calcula multiplicando el precio por hora por la duración.
5. Las reservas comienzan en estado PENDIENTE.
6. Los cambios de estado permitidos son:
   - PENDIENTE a CONFIRMADA o CANCELADA.
   - CONFIRMADA a FINALIZADA o CANCELADA.
7. Los cambios de estado inválidos devuelven HTTP 409.
8. Los registros inexistentes devuelven HTTP 404.
9. Los datos inválidos devuelven HTTP 400 utilizando ProblemDetail.

## Pruebas

Las peticiones de ejemplo se encuentran en pruebas.http.

El proyecto incluye pruebas automáticas que se ejecutan mediante:

```bash
bash .tp/correr.sh
```
