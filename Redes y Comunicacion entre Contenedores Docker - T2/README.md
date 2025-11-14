# Redes y Comunicacion entre Contenedores Docker

Este documento describe el proceso para crear una imagen Docker con Node.js, ejecutar contenedores en distintas redes, comprobar su comunicación y conectar redes entre sí.

---

## Instalación

### 1. Crear imagen Docker desde proyecto Node.js

1. **Navegar al directorio del proyecto**

   ```bash
   cd "Redes y Comunicacion entre Contenedores Docker - T2"
   ```

2. **Instalar dependencias del proyecto**

   ```bash
   npm install
   ```

3. **Probar que el proyecto funciona localmente**

   ```bash
   npm start
   ```

  Verifica que el servidor Node se ejecute correctamente.

4. **Construir la imagen Docker**

   ```bash
   docker build -t mi-web-node .
   ```

  Crea una imagen llamada `mi-web-node` a partir del Dockerfile del proyecto.

5. **Verificar que la imagen se creó correctamente**

   ```bash
   docker images
   ```


---

## Ejecución del proyecto

### 2. Creación de redes Docker

1. **Crear dos redes tipo bridge**

   ```bash
   docker network create -d bridge red01
   docker network create -d bridge red02
   ```

   Estas redes permitirán la comunicación interna entre contenedores.

### 3. Crear contenedores asignados a redes

Cada contenedor ejecuta la imagen `mi-web-node` y expone un puerto diferente:

```bash
docker run -d --network red01 -p 3000:4000 --name servidor01 mi-web-node
docker run -d --network red01 -p 3001:4000 --name servidor02 mi-web-node
docker run -d --network red01 -p 3002:4000 --name servidor03 mi-web-node

docker run -d --net=red02 -p 3003:4000 --name servidorweb01 mi-web-node
docker run -d --net=red02 -p 3004:4000 --name servidorweb02 mi-web-node
```

Cada contenedor levantará el servidor Node escuchando internamente en el puerto 4000, expuesto externamente en puertos **3000–3004**.

### 4. Verificación de redes

```bash
docker network inspect red01
docker network inspect red02
```

Permite ver qué contenedores están conectados a cada red y sus direcciones IP.

---

### 5. Comprobación de comunicación entre contenedores

1. **Ingresar a un contenedor**

   ```bash
   docker exec -it servidor01 /bin/sh
   ```

2. **Hacer ping a otro contenedor de la misma red**

   * Por IP:

     ```bash
     ping 172.19.0.4
     ```
   * Por nombre:

     ```bash
     ping servidor02
     ```

3. **Intentar ping a contenedor de otra red (debe fallar)**

   ```bash
   ping 172.20.0.2
   ```

4. **Salir del contenedor**

   ```bash
   exit
   ```

Se demuestra que contenedores de diferentes redes no pueden comunicarse.

---

### 6. Conectar dos redes a un mismo contenedor

1. **Agregar `servidor01` a la red `red02`**

   ```bash
   docker network connect red02 servidor01
   ```

2. **Comprobar comunicación con la nueva red**

   ```bash
   docker exec -it servidor01 /bin/sh
   ping 172.20.0.2
   ```

3. **Verificar que ahora pertenece a ambas redes**

   ```bash
   docker network inspect red01
   docker network inspect red02
   ```

Ahora `servidor01` aparece listado en red01 y red02, permitiendo comunicación entre ambas redes a través de él.

---

## Solución de problemas comunes

### No puedo acceder a `localhost:3000`, `3001`, etc.

**Solución:** Verifica que el contenedor esté corriendo:

```bash
docker ps
```

Si no está arriba, reinícialo:

```bash
docker start servidor01
```

### El ping por nombre no funciona

**Solución:** Asegúrate de que ambos contenedores están en la misma red:

```bash
docker network inspect red01
```

### No se crea la imagen Docker

**Solución:** Revisa que tu Dockerfile esté en la misma carpeta donde ejecutas:

```bash
docker build -t mi-web-node .
```

---

## Equipo Synapsis Tech

- **Team Lead**: Baptista Gonzales Gino
- **Desarrolladores**:
  - Moises Reinaldo Reyes Flores
  - Miguel Ángel Ballivian Ocampo
  - Wilson Montaño Claros
  - Melissa Fisher Vargas
- **Scrum Master**: Alejandro Sahonero

---

**Desarrollado por Synapsis Tech**
