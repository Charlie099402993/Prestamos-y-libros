package com.example.Biblioteca.Service;

import com.example.Biblioteca.Controller.Dto.PrestamoResponseDto;
import com.example.Biblioteca.Controller.Dto.SolicitarPrestamoDto;
import com.example.Biblioteca.Excpetion.RecursoException;
import com.example.Biblioteca.Model.EstadoPrestamo;
import com.example.Biblioteca.Model.Lector;
import com.example.Biblioteca.Model.Libro;
import com.example.Biblioteca.Model.Prestamo;
import com.example.Biblioteca.Repository.LectorRepository;
import com.example.Biblioteca.Repository.LibroRepository;
import com.example.Biblioteca.Repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(LectorRepository lectorRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

             @Transactional
    public PrestamoResponseDto solicitarPrestamo(SolicitarPrestamoDto dto) {
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new RecursoException("El lector no existe"));

        if (!lector.isActivo()) {
            throw new RecursoException("El lector no está activo para realizar préstamos");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new RecursoException("El libro no existe"));

        if (libro.getEjemplaresDisponibles() <= 0) {
            throw new RecursoException("No hay ejemplares disponibles para este libro");
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.prestado, EstadoPrestamo.vencido);
        long prestamosActivos = prestamoRepository.countByLectorAndEstadoIn(lector, estadosActivos);

        int limiteMaximo = lector.isMembresiaPremium() ? 5 : 3;
        if (prestamosActivos >= limiteMaximo) {
            throw new RecursoException("El lector ha alcanzado el límite máximo de préstamos activos (" + limiteMaximo + ")");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(hoy);
        prestamo.setFechaPrestamo(hoy.plusDays(14));
        prestamo.setEstado(EstadoPrestamo.prestado);

        return mapToResponseDto(prestamoRepository.save(prestamo));
    }

    @Transactional
    public PrestamoResponseDto devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RecursoException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.devuelto) {
            throw new RecursoException("El libro ya ha sido devuelto anteriormente");
        }

        prestamo.setEstado(EstadoPrestamo.devuelto);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaPrestamo())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        return mapToResponseDto(prestamoRepository.save(prestamo));
    }

    @Transactional
    public PrestamoResponseDto renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RecursoException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.vencido || prestamo.getEstado() == EstadoPrestamo.devuelto) {
            throw new RecursoException("No se puede renovar un préstamo en estado " + prestamo.getEstado());
        }

        prestamo.setFechaPrestamo(prestamo.getFechaPrestamo().plusDays(7));

        return mapToResponseDto(prestamoRepository.save(prestamo));
    }

    @Transactional
    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new RecursoException("El lector no existe"));

        lector.setActivo(true);

        return lectorRepository.save(lector);
    }

    public List<PrestamoResponseDto> listarPrestamosActivosLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new RecursoException("El lector no existe"));

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.prestado, EstadoPrestamo.vencido);
        return prestamoRepository.findByLectorAndEstadoIn(lector, estadosActivos)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private PrestamoResponseDto mapToResponseDto(Prestamo prestamo) {
        PrestamoResponseDto dto = new PrestamoResponseDto();
        dto.setId(prestamo.getId());
        dto.setLectorId(prestamo.getLector().getId());
        dto.setNombreLector(prestamo.getLector().getNombre());
        dto.setLibroId(prestamo.getLibro().getId());
        dto.setTituloLibro(prestamo.getLibro().getTitulo());
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucionMaxima(prestamo.getFechaPrestamo());
        dto.setEstado(prestamo.getEstado());
        return dto;
    }
}