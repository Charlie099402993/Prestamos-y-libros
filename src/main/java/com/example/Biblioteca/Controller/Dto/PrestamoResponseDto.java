package com.example.Biblioteca.Controller.Dto;

import com.example.Biblioteca.Model.EstadoPrestamo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrestamoResponseDto {
    private Long id;
    private Long lectorId;
    private String nombreLector;
    private Long libroId;
    private String tituloLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}