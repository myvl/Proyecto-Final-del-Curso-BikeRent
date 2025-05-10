🚴‍♂️ BikeRent Manager – Sistema Integral de Alquiler de Bicicletas
BikeRent Manager es una aplicación de escritorio desarrollada en Java Swing con conexión a PostgreSQL, diseñada para administrar de forma eficiente un negocio de alquiler de bicicletas. El sistema abarca la gestión de datos maestros, procesos operativos y generación de reportes, integrando toda la operación del negocio en una sola plataforma.

Características principales:
📦 Gestión de inventario de bicicletas por tienda.

👥 Administración de clientes y reservas.

🔄 Seguimiento de alquileres activos y devoluciones.

📊 Reportes de uso y rentabilidad.

💾 Funciones de respaldo y restauración de la base de datos.

🔐 Interfaz gráfica intuitiva con paneles modulares para cada proceso.

Procesos Operativos

Registro de Alquileres (Rentals): Asociación de bicicletas con clientes y fechas de alquiler.

Devoluciones: Cálculo automático del tiempo de uso y estado del equipo.

Validaciones: Disponibilidad, duplicados, restricciones de datos.

Transacciones seguras: Uso de bloques transaccionales para evitar inconsistencias.

Estructura del sistema:
Base de datos relacional normalizada con tablas como Multi_Shop, Bicycles, Rentals, Renters, entre otras.

Panel de inicio con resumen del sistema, accesos rápidos e indicadores clave.

Módulos CRUD completos para bicicletas, tiendas, clientes y alquileres.

Transacciones controladas para operaciones sensibles (devoluciones, reservas).

Generación de informes con estadísticas relevantes del negocio.
