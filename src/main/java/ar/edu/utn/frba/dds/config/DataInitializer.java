package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.modelDomain.Entidad;
import ar.edu.utn.frba.dds.modelDomain.MedioDeContacto;
import ar.edu.utn.frba.dds.repository.EntidadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EntidadRepository entidadRepository;

    public DataInitializer(EntidadRepository entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Entidad entidad = new Entidad();
        entidad.setNombre("Facundo Ramos");

        MedioDeContacto medio = new MedioDeContacto("ramosfacu05@gmail.com");
        entidad.agregarMedioDeContacto(medio);

        entidadRepository.guardar(entidad);
        System.out.println("DataInitializer: Entidad inicializada con éxito (Facundo Ramos - ramosfacu05@gmail.com)");
    }
}
