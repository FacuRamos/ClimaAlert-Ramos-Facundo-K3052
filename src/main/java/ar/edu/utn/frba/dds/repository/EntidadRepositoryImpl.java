package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.modelDomain.Entidad;
import ar.edu.utn.frba.dds.utils.GeneradorIdSecuencial;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class EntidadRepositoryImpl implements EntidadRepository {
    private final List<Entidad> entidades = Collections.synchronizedList(new ArrayList<>());
    private final GeneradorIdSecuencial idGenerator = new GeneradorIdSecuencial();

    @Override
    public void guardar(Entidad entidad) {
        if (entidad.getId() == null) {
            entidad.setId(idGenerator.siguiente());
        }
        // Evitamos guardar duplicados reemplazando por ID si ya existe
        entidades.removeIf(e -> e.getId().equals(entidad.getId()));
        this.entidades.add(entidad);
    }

    @Override
    public Optional<Entidad> buscarPorId(Long id) {
        return entidades.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Entidad> buscarTodos() {
        return new ArrayList<>(this.entidades);
    }
}
