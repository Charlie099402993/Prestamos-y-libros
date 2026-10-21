package com.example.Biblioteca.Repository;

import com.example.Biblioteca.Model.EstadoPrestamo;
import com.example.Biblioteca.Model.Lector;
import com.example.Biblioteca.Model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo,Long> {
    Long countByLectorAndEstadoIn(Lector lector, List <EstadoPrestamo> estadoPrestamo);
    List <Prestamo> findByLectorAndEstadoIn(Lector lector1,List<EstadoPrestamo> estado);
}
