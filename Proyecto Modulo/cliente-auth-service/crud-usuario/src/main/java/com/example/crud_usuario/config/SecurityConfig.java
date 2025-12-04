package com.example.crud_usuario.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    // Configuración de seguridad para el servicio de autenticación
    public static final String JWT_SECRET = "mi_clave_secreta_para_jwt_bolivia_2024";
    public static final long JWT_EXPIRATION = 86400000; // 24 horas en millisegundos

    // VULNERABILIDAD DE PRUEBA - API KEYs hardcodeadas
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    // VULNERABILIDAD DE PRUEBA - Github Token
    private static final String GITHUB_TOKEN = "ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
}