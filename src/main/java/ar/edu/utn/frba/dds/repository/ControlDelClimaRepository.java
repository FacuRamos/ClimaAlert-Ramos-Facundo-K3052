package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.dto.DatosClima;
import java.util.List;
import java.util.Optional;

public interface ControlDelClimaRepository {
    void guardar(DatosClima datosClima);
    List<DatosClima> buscarTodos();
    Optional<DatosClima> buscarUltimo();
}
