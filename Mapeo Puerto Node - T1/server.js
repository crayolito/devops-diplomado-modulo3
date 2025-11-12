// Importar la librería Express para crear el servidor web
const express = require('express');

// Crear instancia de la aplicación Express
const aplicacion = express();

// Definir el puerto donde escuchará el servidor
const PUERTO = 4000;

/**
 * Configurar ruta principal del servidor web
 * Responde con página HTML cuando se accede a la raíz
 * 
 * @ruta GET /
 * @respuesta Página HTML con información del equipo
 */
aplicacion.get('/', (peticion, respuesta) => {
    // PASO 1: Crear contenido HTML con información del proyecto
    const paginaPrincipal = `
        <html>
        <head>
            <title>Servidor Node.js - Synapsis Tech</title>
        </head>
        <body>
            <h1> Servidor Node.js - Synapsis Tech</h1>
            <h2>Información del Proyecto</h2>
            <p><strong>Puerto del servidor:</strong> ${PUERTO}</p>
            <p><strong>Estado del servicio:</strong> Funcionando correctamente</p>
            <p><strong>Nombre del equipo:</strong> Synapsis Tech</p>
            
            <h3>Integrantes del Equipo</h3>
            <ul>
                <li>Baptista Gonzales Gino (Team Lead)</li>
                <li>Moises Reinaldo Reyes Flores (Desarrollador -> LE TOCO HACER)</li>
                <li>Miguel Ángel Ballivian Ocampo (Desarrollador)</li>
                <li>Wilson Montaño Claros (Desarrollador)</li>
                <li>Melissa Fisher Vargas (Desarrolladora)</li>
                <li>PAJARITO NUEVO (Desarrollador)</li>
            </ul>
            
            <h3>Gestión del Proyecto</h3>
            <p><strong>Scrum Master:</strong> Alejandro Sahonero</p>
            <p><strong>Herramienta de gestión:</strong> JIRA</p>
            
            <hr>
            <p>Mapeo de puertos Docker implementado exitosamente</p>
        </body>
        </html>
    `;

    // PASO 2: Enviar la página HTML como respuesta
    respuesta.send(paginaPrincipal);
});

aplicacion.listen(PUERTO, () => {
    // MENSAJE 1: Confirmar que el servidor está ejecutándose
    console.log(`Servidor Node.js ejecutándose correctamente en puerto ${PUERTO}`);

    // MENSAJE 2: Mostrar URL de acceso local
    console.log(`Acceder desde navegador en: http://localhost:8080`);

    // MENSAJE 3: Mostrar información del equipo
    console.log(`Proyecto desarrollado por: Synapsis Tech`);
});