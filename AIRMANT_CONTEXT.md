# AIRMANT - CONTEXTO DEL PROYECTO

## 1. DESCRIPCIÓN

AIRMANT es un sistema de software orientado a la
gestión del mantenimiento aeronáutico (MRO).

El sistema busca controlar y centralizar información
relacionada con aeronaves, mantenimiento, personal
técnico, inventario, órdenes de trabajo, documentación,
usuarios y trazabilidad.

---

## 2. OBJETIVO

Desarrollar un sistema que permita gestionar de forma
organizada y segura los procesos relacionados con el
mantenimiento aeronáutico.

---

## 3. TECNOLOGÍAS

Backend:
- Java
- Spring Boot

Base de datos:
- MySQL

Frontend:
- HTML
- CSS
- JavaScript

Control de versiones:
- Git
- GitHub

IDE:
- IntelliJ IDEA

---

## 4. MÓDULOS

Actualmente se contemplan:

- Usuarios
- Roles
- Funcionarios
- Aeronaves
- Mantenimiento
- Órdenes de trabajo
- Inventario
- Personal técnico
- Documentación
- Reportes
- Historial de mantenimiento

---

## 5. BACKEND

La arquitectura utiliza Spring Boot.

Se busca mantener separación entre:

- Controller
- Service
- Repository
- Model / Entity

---

## 6. BASE DE DATOS

Motor:

MySQL

La base de datos debe mantener relaciones
consistentes entre las entidades.

---

## 7. ESTADO ACTUAL

Proyecto en desarrollo.

Se ha trabajado principalmente en:

- Configuración inicial de Spring Boot
- Conexión con MySQL
- Funcionarios
- Controllers
- Modelos
- CRUD
- Git
- Autenticación
- Usuarios
- Roles

---

## 8. PROBLEMAS CONOCIDOS

Registrar aquí los errores encontrados,
su causa y solución.

---

## 9. ÚLTIMO CAMBIO

Fecha:

Descripción:

Archivos modificados:

---

## 10. PRÓXIMO PASO

plan planteado por claud

1 Confirmar y limpiar el proyecto base
Sube el proyecto como .zip para que pueda revisar la estructura real de carpetas frontend/backend/database. Verifica que exista el docker-compose.yml y que PostgreSQL levante correctamente en local antes de tocar código.
2 Modelar la base de datos en Prisma
Traduce la lista de entidades del documento (usuarios, roles, aeronaves, ordenes_mantenimiento, manuales_tecnicos, repuestos, insumos, inventario, auditoria) a un schema.prisma con snake_case en tablas/campos, tal como definiste en tus estándares. Corre la primera migración.
3 Construir el módulo de autenticación real (backend)
En NestJS crea el módulo auth con JWT: endpoints de login, registro y validación de token, más guards de roles/permisos. Este módulo reemplaza la lógica que falta detrás de tu login.html actual.
4 Conectar el login del frontend al backend
Reescribe el formLogin de tu HTML (o pásalo a un componente React Login.tsx) para que haga fetch/axios al endpoint /auth/login real, guarde el JWT y redirija al Dashboard según el rol del usuario.
5 Construir módulos backend uno por uno
Sigue el orden de dependencia: usuarios → aeronaves → mantenimientos → manuales → repuestos/insumos → inventario → reportes/auditoría. Cada módulo con su controller, service y DTOs, respetando camelCase/PascalCase de tus estándares.
6 Construir las vistas React en paralelo
Por cada módulo backend, crea su página en pages/ y sus componentes en components/<Módulo>/ (ya tienes la carpeta lista en tu estructura: Aeronaves, Mantenimientos, Manuales, Repuestos, Insumos). Consume la API mediante services/ con interfaces I+PascalCase.
7 Integrar almacenamiento de archivos
Configura MinIO local (o el bucket S3) para subir manuales técnicos y evidencias fotográficas; en la base de datos solo guarda ruta, nombre, versión y metadatos, como definiste en tu justificación.
8 Pruebas, documentación y entrega de evidencia
Prueba flujos completos (login → crear orden de mantenimiento → adjuntar manual), documenta en el README y prepara la evidencia siguiendo el formato SENA que ya usaste en GA6 y GA7.

ya se realizo 
fase 5: el módulo de Aeronaves, siguiendo el mismo patrón Model → Repository → Controller que ya usaste en Usuarios
continuar con el paso indicado 

---

## 11. REGLAS PARA LA IA

Antes de modificar código:

1. Analizar primero el código existente.
2. No eliminar funcionalidades sin autorización.
3. No cambiar la arquitectura sin explicar previamente.
4. Explicar la causa de los errores.
5. Indicar los archivos modificados.
6. Mantener compatibilidad con el proyecto.
7. Utilizar buenas prácticas de programación.
8. Mantener una estructura clara.
9. No inventar clases, tablas o archivos que no existan
   sin indicarlo.
10. Cuando una modificación afecte la base de datos,
    explicar primero las consecuencias.

---

## 12. INSTRUCCIÓN PARA CONTINUAR

Cuando un nuevo usuario trabaje en AIRMANT:

1. Leer este archivo.
2. Analizar la estructura del proyecto.
3. Revisar el estado actual.
4. Revisar los cambios recientes de Git.
5. Identificar las tareas pendientes.
6. Preguntar o explicar el siguiente paso antes
   de realizar cambios importantes.