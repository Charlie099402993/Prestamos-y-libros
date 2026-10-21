package com.example.Biblioteca.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Libro {
    @GeneratedValue
    @Id
    private long id;
    @Column(name = "titulo", nullable = false,length = 200)
    private String titulo;
    @Column(name = "ejemplaresDisponibles" ,nullable = false,length = 200)
    private int ejemplaresDisponibles;
}
