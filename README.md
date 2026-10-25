# proyecto_evaluacion_APP

App Android para registrar y hacer seguimiento de solicitudes de clientes (caso DSY1105 – Gestión móvil y seguimiento de solicitudes de clientes). Todos los datos son ficticios.

## Integrantes

- Matias Acevedo
- Alexander Mejias

## Funcionalidades

- Login con validaciones y 3 perfiles: Cliente, Administrador y Técnico.
- Crear y editar solicitudes con código único, estado inicial "Recibida" y foto de evidencia desde la galería.
- Lista de solicitudes con búsqueda, filtros y aviso de solicitudes atrasadas.
- Detalle con historial de cambios y código QR propio de cada solicitud.
- Administrador: clasificar, priorizar, asignar técnico y ver el resumen general.
- Técnico: cambiar estado, registrar avances y resolver.
- Escanear el QR de una solicitud con la cámara.
- Datos guardados en el celular con Room.

## Tecnologías

Kotlin, Jetpack Compose (Material 3), MVVM, Navigation Compose, Room, CameraX + ML Kit (leer QR) y ZXing (generar QR).

## Cómo ejecutar

1. Abrir Android Studio y en la pantalla de inicio elegir **Clone Repository**. Si ya hay un proyecto con el mismo nombre, se tiene que borrar desde archivos
2. Pegar la URL `https://github.com/mati4769/proyectoAPP.git` y presionar **Clone**.
3. Esperar que termine de sincronizar Gradle.
4. Elegir un emulador y presionar ▶ Run.

## Cuentas de prueba

| Correo | Contraseña | Perfil |
|---|---|---|
| cliente@pymedemo.cl | cliente123 | Cliente |
| cliente2@comerciodemo.cl | cliente456 | Cliente |
| admin@techsoluciones.cl | admin123 | Administrador |
| tecnico@techsoluciones.cl | tecnico123 | Técnico |
| tecnico2@techsoluciones.cl | tecnico456 | Técnico |
