package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.dto.DatosClima;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ControlDelClimaRepositoryImpl implements ControlDelClimaRepository {
    private final List<DatosClima> datosClimas = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void guardar(DatosClima datosClima) {
        this.datosClimas.add(datosClima);
    }

    @Override
    public List<DatosClima> buscarTodos() {
        return new ArrayList<>(this.datosClimas);
    }

    @Override
    public Optional<DatosClima> buscarUltimo() {
        if (datosClimas.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(datosClimas.get(datosClimas.size() - 1));
    }
  
}
