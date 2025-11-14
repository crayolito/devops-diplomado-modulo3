# Usa la imagen oficial de Node.js versión 18 con Alpine Linux como sistema base
# Alpine Linux es una distribución ligera que reduce el tamaño de la imagen
FROM node:18-alpine

# Establece el directorio de trabajo donde se ejecutará la aplicación dentro del contenedor
# Todas las operaciones posteriores se realizarán desde esta carpeta
WORKDIR /app

# Copia el archivo de dependencias del proyecto al contenedor
# Se copia primero para aprovechar el cache de Docker en las capas
COPY package.json .

# Instala todas las dependencias especificadas en package.json
# npm install descarga e instala los paquetes necesarios para el proyecto
RUN npm install

# Copia todos los archivos del proyecto al contenedor
# Incluye el código fuente y archivos de configuración
COPY . .

# Expone el puerto 4000 para que sea accesible desde fuera del contenedor
# Permite que otros servicios o la máquina host se conecten a este puerto
EXPOSE 4000

# Define el comando que se ejecutará cuando inicie el contenedor
# npm start ejecutará el script definido en package.json
CMD ["npm", "start"]