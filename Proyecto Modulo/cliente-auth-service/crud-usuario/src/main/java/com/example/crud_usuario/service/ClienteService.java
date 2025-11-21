package com.example.crud_usuario.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.crud_usuario.entity.Cliente;

@Service
public class ClienteService {

    // Lista en memoria (volátil)
    private List<Cliente> clientes = new ArrayList<>();
    private AtomicLong contador = new AtomicLong(1);

    // Constructor con datos de ejemplo
    public ClienteService() {
        // Datos iniciales para prueba
        Cliente cliente1 = new Cliente("Juan", "Pérez", "juan@email.com", "123456789");
        cliente1.setId(1L);

        Cliente cliente2 = new Cliente("María", "García", "maria@email.com", "987654321");
        cliente2.setId(2L);

        clientes.add(cliente1);
        clientes.add(cliente2);

        contador.set(3); // Siguiente ID será 3
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }

    // Obtener cliente por ID
    public Optional<Cliente> obtenerPorId(Long id) {
        return clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    // Crear nuevo cliente
    public Cliente crear(Cliente cliente) {
        // Verificar email único
        boolean emailExiste = clientes.stream()
                .anyMatch(c -> c.getEmail().equals(cliente.getEmail()));

        if (emailExiste) {
            throw new RuntimeException("Ya existe un cliente con este email");
        }

        cliente.setId(contador.getAndIncrement());
        clientes.add(cliente);
        return cliente;
    }

    // Actualizar cliente
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        // Validar que el ID no sea null
        if (id == null) {
            throw new RuntimeException("El ID no puede ser nulo");
        }

        // Buscar el cliente
        Optional<Cliente> clienteOpt = obtenerPorId(id);

        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }

        Cliente cliente = clienteOpt.get();

        // Validar que el cliente actualizado no sea null
        if (clienteActualizado == null) {
            throw new RuntimeException("Los datos del cliente no pueden ser nulos");
        }

        // Verificar email único (si cambió)
        if (clienteActualizado.getEmail() != null &&
                !cliente.getEmail().equals(clienteActualizado.getEmail())) {
            boolean emailExiste = clientes.stream()
                    .anyMatch(c -> c.getId() != null &&
                            !c.getId().equals(id) &&
                            c.getEmail() != null &&
                            c.getEmail().equals(clienteActualizado.getEmail()));

            if (emailExiste) {
                throw new RuntimeException("Ya existe un cliente con este email");
            }
        }

        // Actualizar campos (solo si no son null)
        if (clienteActualizado.getNombre() != null) {
            cliente.setNombre(clienteActualizado.getNombre());
        }
        if (clienteActualizado.getApellido() != null) {
            cliente.setApellido(clienteActualizado.getApellido());
        }
        if (clienteActualizado.getEmail() != null) {
            cliente.setEmail(clienteActualizado.getEmail());
        }
        if (clienteActualizado.getTelefono() != null) {
            cliente.setTelefono(clienteActualizado.getTelefono());
        }

        return cliente;
    }

    // Eliminar cliente
    public void eliminar(Long id) {
        boolean eliminado = clientes.removeIf(c -> c.getId().equals(id));

        if (!eliminado) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
    }
}