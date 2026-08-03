Sistema de Gestión para Clínica Veterinaria

Descripción

El Sistema de Gestión para Clínica Veterinaria es una aplicación de escritorio desarrollada en Java, cuyo propósito es facilitar la administración de los principales procesos de una clínica veterinaria, incluyendo la gestión de clientes, mascotas, veterinarios, citas, consultas, servicios y facturación.

El sistema fue desarrollado aplicando principios de Programación Orientada a Objetos (POO), incluyendo encapsulamiento, herencia, abstracción y polimorfismo. Además, incorpora el uso de colecciones genéricas, manejo de excepciones, una interfaz gráfica desarrollada con Java Swing y persistencia de información mediante JDBC y MySQL.

El proyecto se encuentra organizado mediante una arquitectura por capas, separando las responsabilidades del modelo, acceso a datos, lógica de negocio y presentación.

El desarrollo se realizó de manera colaborativa utilizando Git y GitHub, mediante ramas independientes, commits y Pull Requests para facilitar la integración del trabajo realizado por cada integrante.

---

Objetivos

Objetivo General

Desarrollar una aplicación de escritorio que permita administrar los principales procesos de una clínica veterinaria, aplicando conceptos de Programación Orientada a Objetos, una arquitectura organizada por capas y persistencia de información mediante una base de datos relacional.

Objetivos Específicos

- Aplicar los principios de encapsulamiento, herencia, abstracción y polimorfismo.
- Implementar relaciones entre las diferentes entidades del sistema.
- Utilizar una arquitectura organizada por capas para separar las responsabilidades del proyecto.
- Implementar persistencia de datos mediante JDBC y una base de datos MySQL.
- Desarrollar una interfaz gráfica funcional utilizando Java Swing.
- Implementar validaciones y manejo de excepciones personalizadas.
- Utilizar colecciones genéricas para el manejo de información.
- Implementar una funcionalidad concurrente que permita ejecutar tareas sin bloquear la interfaz gráfica.
- Aplicar el uso de Git y GitHub como herramientas de control de versiones y desarrollo colaborativo.

---

Tecnologías utilizadas

Tecnología| Descripción
Java| Lenguaje de programación principal utilizado para desarrollar el sistema.
Java Swing| Biblioteca utilizada para desarrollar la interfaz gráfica de la aplicación de escritorio.
JDBC| Tecnología utilizada para conectar la aplicación Java con la base de datos.
MySQL| Sistema gestor de base de datos relacional utilizado para almacenar la información del sistema.
NetBeans| Entorno de desarrollo utilizado durante la implementación del proyecto.
GitHub| Plataforma utilizada para alojar el repositorio y facilitar el desarrollo colaborativo.

---

Arquitectura del proyecto

El sistema se encuentra organizado mediante una arquitectura por capas, con el objetivo de separar las responsabilidades y facilitar el mantenimiento y evolución del código.

                 ┌─────────────────────┐
                 │    Presentación     │
                 │     Java Swing      │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │      Negocio        │
                 │ Reglas y validación │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │       Datos         │
                 │    DAO + JDBC       │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │        MySQL        │
                 │ Base de datos       │
                 └─────────────────────┘

Cada capa posee una responsabilidad específica:

- Presentación: Gestiona la interfaz gráfica, los formularios, los eventos y la visualización de información.
- Negocio: Contiene las reglas del sistema, validaciones y lógica necesaria para procesar las operaciones.
- Datos: Se encarga del acceso y persistencia de la información mediante clases DAO y JDBC.
- MySQL: Almacena de manera persistente la información utilizada por la aplicación.

Esta separación permite mantener una estructura organizada y evita mezclar la lógica de presentación con las reglas del negocio y el acceso a la base de datos.

---

Estructura del proyecto

src
│
├── Datos
│   ├── ConexionBD.java
│   ├── ClienteDAO.java
│   ├── MascotaDAO.java
│   ├── VeterinarioDAO.java
│   ├── CitaDAO.java
│   ├── ConsultaDAO.java
│   ├── ServicioDAO.java
│   └── FacturaDAO.java
│
├── Exception
│   ├── CitaNoDisponibleException.java
│   └── ValidationException.java
│
├── Negocio
│   ├── ClienteServicio.java
│   ├── MascotaServicio.java
│   ├── VeterinarioServicio.java
│   ├── CitaServicio.java
│   ├── ConsultaServicio.java
│   ├── GestionServicio.java
│   └── FacturaServicio.java
│
├── modelo
│   ├── Persona.java
│   ├── Cliente.java
│   ├── Veterinario.java
│   ├── Mascota.java
│   ├── Consulta.java
│   ├── Servicio.java
│   ├── ConsultaGeneral.java
│   ├── Vacunacion.java
│   ├── Procedimiento.java
│   ├── Factura.java
│   ├── DetalleFactura.java
│   ├── EstadoCita.java
│   ├── EstadoFactura.java
│   ├── Especie.java
│   ├── Especialidad.java
│   └── TipoServicio.java
│
├── presentacion
│   ├── MainFrame.java
│   ├── PanelClientes.java
│   ├── PanelMascotas.java
│   ├── PanelVeterinarios.java
│   ├── PanelCitas.java
│   ├── PanelConsultas.java
│   ├── PanelServicios.java
│   └── PanelFacturacion.java
│
└── clinica.veterinaria
    └── Main.java

---

Responsabilidad de los paquetes

modelo

Contiene las entidades principales del sistema y los elementos relacionados con el modelo de dominio.

En este paquete se encuentran las clases utilizadas para representar clientes, mascotas, veterinarios, consultas, servicios y facturación. También incluye enumeraciones y la jerarquía de clases utilizada para demostrar conceptos de Programación Orientada a Objetos.

Entre los elementos principales se encuentran la clase abstracta "Servicio" y sus clases derivadas, utilizadas para aplicar herencia, abstracción y polimorfismo.

---

Datos

Contiene las clases encargadas del acceso a la base de datos.

En esta capa se encuentra la conexión mediante JDBC y las clases DAO, responsables de realizar las operaciones necesarias sobre la información almacenada en MySQL.

El acceso a los datos se realiza mediante consultas SQL y mecanismos como "PreparedStatement" y "ResultSet".

---

Negocio

Contiene la lógica y las reglas de negocio del sistema.

Esta capa se encarga de procesar las operaciones antes de interactuar con la base de datos, realizar validaciones y gestionar las reglas necesarias para el correcto funcionamiento de las funcionalidades.

También permite mantener separada la lógica del sistema de la interfaz gráfica.

---

presentacion

Contiene los componentes de la interfaz gráfica desarrollada utilizando Java Swing.

Incluye la ventana principal y los diferentes paneles utilizados para gestionar y consultar la información de las entidades del sistema.

Entre los componentes utilizados se encuentran formularios, botones, tablas y cuadros de diálogo para facilitar la interacción del usuario con la aplicación.

---

Exception

Contiene las excepciones personalizadas utilizadas para controlar situaciones específicas del sistema.

Entre ellas se encuentran excepciones relacionadas con la disponibilidad de citas y la validación de datos.

---

clinica.veterinaria

Contiene la clase principal "Main", encargada de iniciar la ejecución de la aplicación.

---

Distribución del trabajo

Integrante| Responsabilidad
Integrante 1: Juan| Clientes, Mascotas y relación de composición
Integrante 2: Daryelin| Veterinarios, Citas y diseño de la interfaz gráfica
Integrante 3: Fiorelha| Consultas, Servicios y jerarquía polimórfica
Integrante 4: Daniel| Facturación, JDBC e integración del multihilo

Integrante 1: Juan

Fue responsable del desarrollo de los módulos correspondientes a Clientes y Mascotas, incluyendo la implementación de la relación de composición entre ambas entidades.

También trabajó en los DAO, la lógica de negocio y los componentes de la interfaz gráfica relacionados con estas funcionalidades.

Integrante 2: Daryelin

Fue responsable del desarrollo de los módulos de Veterinarios y Citas.

Implementó las funcionalidades necesarias para registrar y consultar veterinarios, incluyendo la información relacionada con su especialidad.

Además, desarrolló la gestión de citas, permitiendo relacionar mascotas y veterinarios, registrar la fecha, hora y motivo de cada cita, así como consultar las citas registradas y actualizar su estado.

También participó en el diseño y organización visual de la interfaz gráfica, trabajando en la distribución de los componentes de Java Swing, la apariencia general de las ventanas y la experiencia visual de la aplicación.

Integrante 3: Fiorelha

Fue responsable del desarrollo del módulo de Consultas y Servicios, incluyendo la implementación de la jerarquía polimórfica del sistema.

Trabajó con la clase abstracta "Servicio" y sus respectivas subclases, aplicando conceptos de herencia, abstracción y polimorfismo para representar los diferentes tipos de servicios veterinarios.

También desarrolló las funcionalidades relacionadas con el registro y manejo de consultas veterinarias.

Integrante 4: Daniel

Fue responsable del desarrollo del módulo de Facturación, incluyendo la generación de facturas y el cálculo de los valores correspondientes.

Además, trabajó en la integración con la base de datos MySQL mediante JDBC y en la implementación de la funcionalidad concurrente utilizada por el sistema.

---

Funcionalidades principales

El sistema permite gestionar diferentes procesos relacionados con la administración de una clínica veterinaria.

Gestión de clientes y mascotas

- Registrar clientes.
- Consultar clientes.
- Modificar información de clientes.
- Eliminar clientes.
- Registrar mascotas.
- Asociar mascotas con sus respectivos clientes.
- Consultar información de mascotas.
- Actualizar información de mascotas.

Gestión de veterinarios

- Registrar veterinarios.
- Registrar la especialidad del veterinario.
- Consultar veterinarios registrados.
- Actualizar información de veterinarios.

Gestión de citas

- Registrar citas.
- Relacionar una mascota con un veterinario.
- Registrar fecha y hora de la cita.
- Registrar el motivo de la cita.
- Consultar citas registradas.
- Cambiar el estado de una cita.
- Validar la disponibilidad de las citas según las reglas definidas por el sistema.

Gestión de consultas

- Registrar información relacionada con las consultas veterinarias.
- Asociar las consultas con las mascotas correspondientes.
- Registrar diagnóstico, tratamiento y observaciones.

Gestión de servicios

- Registrar y administrar servicios veterinarios.
- Manejar diferentes tipos de servicios.
- Aplicar una jerarquía de clases mediante herencia.
- Utilizar polimorfismo para calcular el costo de los diferentes servicios.

Facturación

- Crear facturas.
- Asociar servicios a una factura.
- Calcular el subtotal.
- Calcular los impuestos correspondientes.
- Calcular el total de la factura.
- Generar reportes en formato PDF.

Base de datos

- Almacenar la información de forma persistente en MySQL.
- Utilizar JDBC para la comunicación entre Java y la base de datos.
- Utilizar llaves primarias y foráneas para establecer relaciones entre las tablas.
- Ejecutar operaciones de inserción, consulta, actualización y eliminación en las entidades seleccionadas.

Interfaz gráfica

La aplicación cuenta con una interfaz gráfica desarrollada mediante Java Swing.

Entre los componentes utilizados se encuentran:

- "JFrame" para la ventana principal.
- "JTable" para mostrar información.
- "JTextField" para introducir datos.
- "JComboBox" para seleccionar opciones.
- "JButton" para ejecutar acciones.
- "JTextArea" para introducir información extensa.
- "JOptionPane" para mostrar mensajes y confirmaciones.

Concurrencia

El sistema incorpora una funcionalidad concurrente con el objetivo de ejecutar determinadas operaciones sin bloquear la interfaz gráfica.

Esta funcionalidad permite realizar tareas como la carga o actualización de información de manera que la aplicación pueda continuar respondiendo a las acciones del usuario.

---

Programación Orientada a Objetos

Durante el desarrollo del proyecto se aplicaron diferentes conceptos de Programación Orientada a Objetos.

Encapsulamiento

Las clases del sistema utilizan atributos y métodos para controlar el acceso y modificación de la información de los objetos.

Herencia

Se implementan jerarquías de clases para reutilizar atributos y comportamientos comunes.

Abstracción

Se utiliza una clase abstracta "Servicio" como base para representar diferentes tipos de servicios veterinarios.

Polimorfismo

Las diferentes subclases de "Servicio" sobrescriben métodos relacionados con el cálculo de costos, permitiendo que una referencia de tipo "Servicio" pueda trabajar con diferentes implementaciones concretas.

Interfaces

Se utilizan interfaces cuando son necesarias para definir comportamientos que pueden ser implementados por diferentes clases del sistema.

Enumeraciones

Se utilizan "enum" para representar estados y clasificaciones del sistema, como "EstadoCita", "EstadoFactura", "Especie", "Especialidad" y "TipoServicio".

---

Base de datos

El sistema utiliza MySQL como sistema gestor de base de datos y JDBC como tecnología de conexión desde Java.

La base de datos permite almacenar información relacionada con:

- Clientes.
- Mascotas.
- Veterinarios.
- Citas.
- Consultas.
- Servicios.
- Facturas.
- Detalles de factura.

Las entidades se encuentran relacionadas mediante llaves primarias y llaves foráneas, permitiendo representar las relaciones existentes entre los diferentes elementos del sistema.

El proyecto incluye un archivo "database.sql", utilizado para crear la estructura necesaria de la base de datos.

---

Control de versiones

El desarrollo del proyecto se realizó utilizando Git y GitHub.

Se utilizaron ramas para organizar el trabajo de los integrantes y facilitar la integración de las diferentes funcionalidades desarrolladas.

La estructura de ramas utilizada contempla:

main
│
└── develop
      │
      ├── feature/juan-clientes-mascotas
      ├── feature/daryelin-veterinarios-citas
      ├── feature/fiorelha-consultas-servicios
      └── feature/daniel-facturacion-jdbc

Cada integrante trabajó en su respectiva rama de funcionalidad y posteriormente se realizaron integraciones mediante Pull Requests.

El historial de Git permite evidenciar el proceso de desarrollo y los aportes realizados por cada integrante del equipo.

---

Requisitos para ejecutar el proyecto

Para ejecutar el sistema se requiere contar con:

- Java JDK instalado.
- NetBeans u otro IDE compatible con proyectos Java.
- MySQL Server.
- MySQL Workbench, opcional para administrar la base de datos.
- Driver JDBC de MySQL.

---

Configuración de la base de datos

1. Instalar y ejecutar MySQL Server.
2. Crear la base de datos utilizando el archivo "database.sql".
3. Verificar las credenciales de acceso configuradas en "ConexionBD.java".
4. Confirmar que el servidor MySQL se encuentre activo.
5. Verificar que la aplicación pueda establecer correctamente la conexión mediante JDBC.

---

Ejecución del proyecto

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en NetBeans.
3. Configurar el acceso a la base de datos MySQL.
4. Ejecutar el archivo "database.sql" para crear las tablas necesarias.
5. Verificar la configuración de la conexión JDBC.
6. Ejecutar la clase principal "Main.java".
7. Utilizar la interfaz gráfica para acceder a las diferentes funcionalidades del sistema.

