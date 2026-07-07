package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Me pone los getters y setters
@NoArgsConstructor
@AllArgsConstructor
public class DatosClima {
  //Esta puesto como un objeto porque es lo que me devuelve la api
  @JsonProperty("current") //Lo hago que haga el match con el dato que me ofrece la api
  DatosActualesClima datosActualesClima;
}
