package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.modelDomain.Entidad;
import java.util.List;
import java.util.Optional;

public interface EntidadRepository {
    void guardar(Entidad entidad);
    Optional<Entidad> buscarPorId(Long id);
    List<Entidad> buscarTodos();
}
