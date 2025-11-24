package com.example.crud_usuario.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// SQL Injection
// Ataque donde se inserta código SQL malicioso en los inputs
// para manipular o dañar la base de datos

// Cross-Site Scripting (XSS)
// Ataque donde se inyecta código JavaScript malicioso 
// que se ejecuta en el navegador de otros usuarios

// Command Injection
// Ataque donde se ejecutan comandos del sistema operativo
// a través de inputs no validados en la aplicación

@RestController
@RequestMapping("/vulnerable")
public class VulnerableController {
    // VULNERABILIDAD DE PRUEBA - SQL Injection
    @GetMapping("/search")
    public String search(@RequestParam String query) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb");
            // VULNERABLE: Concatenación directa sin prepared statements
            String sql = "SELECT * FROM clientes WHERE nombre = '" + query + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return "Results found";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // VULNERABILIDAD DE PRUEBA - XSS (Cross-Site Scripting)
    @GetMapping("/echo")
    public String echo(@RequestParam String input) {
        // VULNERABLE: Devuelve input sin sanitizar
        return "<html><body><h1>Result: " + input + "</h1></body></html>";
    }

    // VULNERABILIDAD DE PRUEBA - Command Injection
    @GetMapping("/execute")
    public String execute(@RequestParam String command) {
        try {
            // VULNERABLE: Ejecuta comandos del sistema sin validación
            Process p = Runtime.getRuntime().exec(command);
            return "Command executed";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
