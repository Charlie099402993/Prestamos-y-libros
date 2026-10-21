package com.example.Biblioteca.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prestamo {


    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Lector", nullable = false)
    private Lector lector;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "libro", nullable = false)
    private Libro libro;
    @Column(name = "fechaPrestamo",nullable = false,length = 255)
    private LocalDate fechaPrestamo;
    @Column(name = "estado",nullable = false)
    private EstadoPrestamo estado;
}
