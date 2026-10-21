package com.example.Biblioteca.Controller;

import com.example.Biblioteca.Controller.Dto.PrestamoResponseDto;
import com.example.Biblioteca.Controller.Dto.SolicitarPrestamoDto;

import com.example.Biblioteca.Model.Lector;
import com.example.Biblioteca.Service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<PrestamoResponseDto> solicitarPrestamo(@Valid @RequestBody SolicitarPrestamoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.solicitarPrestamo(dto));
    }

    @PutMapping("/{prestamoId}/devolver")
    public ResponseEntity<PrestamoResponseDto> devolverLibro(@PathVariable Long prestamoId) {
        return ResponseEntity.ok(prestamoService.devolverLibro(prestamoId));
    }

    @PutMapping("/{prestamoId}/renovar")
    public ResponseEntity<PrestamoResponseDto> renovarPrestamo(@PathVariable Long prestamoId) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(prestamoId));
    }

    @PutMapping("/lectores/{lectorId}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.rehabilitarLector(lectorId));
    }

    @GetMapping("/lectores/{lectorId}/activos")
    public ResponseEntity<List<PrestamoResponseDto>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}