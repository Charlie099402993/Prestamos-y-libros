package com.example.Biblioteca.Config;


import com.example.Biblioteca.Model.Lector;
import com.example.Biblioteca.Model.Libro;
import com.example.Biblioteca.Repository.LectorRepository;
import com.example.Biblioteca.Repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInicializater implements CommandLineRunner {

    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public DataInicializater(LectorRepository lectorRepository, LibroRepository libroRepository) {
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public void run(String... args) {
        Lector l1 = new Lector();
        l1.setNombre("Carlos Pérez");
        l1.setActivo(true);
        l1.setMembresiaPremium(false);
        lectorRepository.save(l1);

        Lector l2 = new Lector();
        l2.setNombre("Ana Gómez");
        l2.setActivo(true);
        l2.setMembresiaPremium(true);
        lectorRepository.save(l2);

        Lector l3 = new Lector();
        l3.setNombre("Juan Inactivo");
        l3.setActivo(false);
        l3.setMembresiaPremium(false);
        lectorRepository.save(l3);

        Libro b1 = new Libro();
        b1.setTitulo("Cien Años de Soledad");
        b1.setEjemplaresDisponibles(5);
        libroRepository.save(b1);

        Libro b2 = new Libro();
        b2.setTitulo("Don Quijote de la Mancha");
        b2.setEjemplaresDisponibles(0);
        libroRepository.save(b2);
    }
}