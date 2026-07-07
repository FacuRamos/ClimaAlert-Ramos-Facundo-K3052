package ar.edu.utn.frba.dds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

//Lo que hago es traerme todo los datos de configuracion del application, en este caso lo que este bajo el pseudonimo de rest-climate
@ConfigurationProperties(prefix = "rest-climate")
@Data
public class RestDatosClimasProperties {

  private String baseUrl;
  private String apiKey;
}
