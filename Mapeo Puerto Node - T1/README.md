# Servidor Web Node.js con Mapeo de Puertos Docker - Synapsis Tech

Proyecto de servidor web Node.js con Express que demuestra el mapeo de puertos en contenedores Docker.

## Información del Proyecto

### Estructura del Proyecto

Mapeo Puerto Node - T1/
├── server.js # Código del servidor Express
├── package.json # Dependencias y configuración npm
├── Dockerfile # Configuración para contenedor Docker
├── .gitignore # Archivos excluidos de Git
└── README.md # Documentación del proyecto

## Instalación

### 1. Navegar al directorio del proyecto

cd "Mapeo Puerto Node - T1"

### 2. Instalar dependencias

npm install

Esto instalará Express y todas las dependencias necesarias especificadas en `package.json`.

## Ejecución del Proyecto

### Opción 1: Ejecución Local (sin Docker)

npm start### Opción 2: Ejecución con Docker

#### Paso 1: Construir la imagen Docker

docker build -t mapeo-puerto-node-t1 .

#### Paso 2: Verificar que la imagen se creó

docker images

Deberías ver `mapeo-puerto-node-t1` en la lista.

#### Paso 3: Ejecutar el contenedor

# docker run: Comando para crear y ejecutar un contenedor desde una imagen

# -d: Ejecuta el contenedor en modo "detached" (segundo plano/background)

# -p 8080:4000: Mapea puertos (PUERTO_MAQUINA:PUERTO_CONTENEDOR)

# - 8080 (izquierda): Puerto en tu máquina Windows donde accederás

# - 4000 (derecha): Puerto interno del contenedor donde corre Node.js

# --name contenedor-t1: Asigna un nombre al contenedor para identificarlo fácilmente

# mapeo-puerto-node-t1: Nombre de la imagen Docker a ejecutar

**Ejemplo 1 - Puerto estándar (4000:4000)**
docker run -d -p 4000:4000 --name contenedor-t1 mapeo-puerto-node-t1
Acceder en: `http://localhost:4000`

**Ejemplo 2 - Puerto 8080 en Windows → Puerto 4000 en contenedor**
docker run -d -p 8080:4000 --name contenedor-t1-8080 mapeo-puerto-node-t1
Acceder en: `http://localhost:8080`

**Ejemplo 3 - Puerto 3000 en Windows → Puerto 4000 en contenedor**
docker run -d -p 3000:4000 --name contenedor-t1-3000 mapeo-puerto-node-t1
Acceder en: `http://localhost:3000`

#### Paso 4: Verificar el contenedor

# Ver contenedores en ejecución

docker ps

# Ver logs del contenedor

docker logs contenedor-t1-8080## Verificación del Funcionamiento

### 1. Acceso desde navegador

Abre tu navegador y visita:

- **Página principal**: `http://localhost:8080`
- **Información técnica (JSON)**: `http://localhost:8080/info`

### 2. Verificación con comandos

# Verificar respuesta del servidor con curl

curl http://localhost:8080

# Verificar que el puerto está en uso

netstat -an | findstr :8080

# Verificar contenedor Docker (si aplica)

docker ps

2. Instalar dependencias
   npm install
   Esto instalará Express y todas las dependencias necesarias especificadas en package.json.

Ejecución del Proyecto

Opción 1: Ejecución Local (sin Docker)
npm start

Opción 2: Ejecución con Docker
Paso 1: Construir la imagen Docker
docker build -t mapeo-puerto-node-t1 .

Paso 2: Verificar que la imagen se creó
docker images
Deberías ver mapeo-puerto-node-t1 en la lista.

Paso 3: Ejecutar el contenedor

Ejemplo 1 - Puerto estándar (4000:4000)
docker run -d -p 4000:4000 --name contenedor-t1 mapeo-puerto-node-t1
Acceder en: http://localhost:4000

Ejemplo 2 - Puerto 8080 en Windows → Puerto 4000 en contenedor
docker run -d -p 8080:4000 --name contenedor-t1-8080 mapeo-puerto-node-t1
Acceder en: http://localhost:8080

Paso 4: Verificar el contenedor

# Ver contenedores en ejecución

docker ps

# Ver logs del contenedor

docker logs contenedor-t1-8080

Verificación del Funcionamiento

1. Acceso desde navegador
   Abre tu navegador y visita:
   Página principal: http://localhost:8080
   Información técnica (JSON): http://localhost:8080/info

2. Verificación con comandos

# Verificar respuesta del servidor con curl

curl http://localhost:8080

# Verificar que el puerto está en uso

netstat -an | findstr :8080

# Verificar contenedor Docker (si aplica)

docker ps
