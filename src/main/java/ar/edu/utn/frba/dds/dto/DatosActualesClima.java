package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data //Me pone los getters y setters
@NoArgsConstructor
@AllArgsConstructor
public class DatosActualesClima {
  @JsonProperty("temp_c")
  private Double temperatura;
  @JsonProperty("humidity")
  private Double humedad;
}
