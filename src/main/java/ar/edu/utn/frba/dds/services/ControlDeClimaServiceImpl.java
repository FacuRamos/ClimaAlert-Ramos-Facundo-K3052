package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.config.RestDatosClimasProperties;
import ar.edu.utn.frba.dds.dto.DatosClima;
import ar.edu.utn.frba.dds.modelDomain.Entidad;
import ar.edu.utn.frba.dds.modelDomain.MedioDeContacto;
import ar.edu.utn.frba.dds.modelDomain.Notificador;
import ar.edu.utn.frba.dds.repository.ControlDelClimaRepository;
import ar.edu.utn.frba.dds.repository.EntidadRepository;
import ar.edu.utn.frba.dds.repository.EntidadRepositoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ControlDeClimaServiceImpl implements ControlClimaService{
  private RestTemplate restTemplate;
  private RestDatosClimasProperties restDatosClimasProperties;
  private EntidadRepository entidadRepository;
  private ControlDelClimaRepository controlDelClimaRepository;
  private Notificador notificador;
  private final Set<String> alertasEnviadas = ConcurrentHashMap.newKeySet();

  public ControlDeClimaServiceImpl(EntidadRepository entidadRepository, 
                                   ControlDelClimaRepository controlDelClimaRepository, 
                                   RestDatosClimasProperties restDatosClimasProperties, 
                                   RestTemplate restTemplate,
                                   Notificador notificador) {
    this.entidadRepository = entidadRepository;
    this.controlDelClimaRepository = controlDelClimaRepository;
    this.restDatosClimasProperties = restDatosClimasProperties;
    this.restTemplate = restTemplate;
    this.notificador = notificador;
  }

  @Override
  public void obtenerDatosClima(){
    String apiKey = restDatosClimasProperties.getApiKey();
    if (apiKey == null || apiKey.isEmpty()) {
      apiKey = System.getenv("WEATHER_API_KEY");
    }

    URI uri = UriComponentsBuilder.fromUriString(restDatosClimasProperties.getBaseUrl())
        .queryParam("key", apiKey)
        .queryParam("q", "Buenos_Aires")
        .queryParam("aqi", "no")
        .build()
        .toUri();
    //Quiero hacer un get y que ese objeto le hagas match con la clase datosClima
    DatosClima datosClima = this.restTemplate.getForObject(uri,DatosClima.class);
    controlDelClimaRepository.guardar(datosClima);
  }

  @Override
  public void controlarAlertaDeClima(){
    Optional<DatosClima> datosClimaOpt = controlDelClimaRepository.buscarUltimo();
    if (datosClimaOpt.isEmpty()) {
      System.out.println("No hay datos de clima registrados aún para evaluar alertas.");
      return;
    }
    DatosClima datosClima = datosClimaOpt.get();

    // Bajamos los límites temporalmente para probar que el envío funcione con el clima actual de Buenos Aires
    if(datosClima.getDatosActualesClima().getTemperatura() > 5 && datosClima.getDatosActualesClima().getHumedad() > 10){
      List<Entidad> entidades = entidadRepository.buscarTodos();

      entidades.forEach(entidad -> {
        List<MedioDeContacto> mediosFiltrados = entidad.getMediosDeContactos().stream()
            .filter(medio -> {
              if (medio.getMail() == null || medio.getMail().isEmpty()) {
                return false;
              }
              String key = generarClaveAlerta(medio.getMail());
              if (alertasEnviadas.contains(key)) {
                System.out.println("El mail para " + medio.getMail() + " ya fue enviado previamente. Evitando duplicado en el servicio.");
                return false;
              }
              return true;
            })
            .collect(Collectors.toList());

        if (!mediosFiltrados.isEmpty()) {
          boolean success = notificador.notificar(mediosFiltrados);
          if (success) {
            mediosFiltrados.forEach(medio -> {
              alertasEnviadas.add(generarClaveAlerta(medio.getMail()));
            });
          }
        }
      });
    }
  }

  private String generarClaveAlerta(String email) {
    return email + "||Alerta de clima||Atención: Se han detectado condiciones climáticas extremas en tu zona.";
  }

  public void limpiarHistorial() {
    this.alertasEnviadas.clear();
  }
}
