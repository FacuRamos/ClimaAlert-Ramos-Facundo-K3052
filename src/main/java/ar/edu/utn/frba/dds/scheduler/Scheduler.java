package ar.edu.utn.frba.dds.scheduler;

import ar.edu.utn.frba.dds.services.ControlClimaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler{
  private final ControlClimaService controlClimaService;

  public Scheduler(ControlClimaService controlClimaService) {
    this.controlClimaService = controlClimaService;
  }

  @Scheduled(fixedDelay = 60000) // Se ejecuta inmediatamente al iniciar y luego cada 1 minuto
  public void constultarDatos(){
    this.controlClimaService.obtenerDatosClima();
  }

  @Scheduled(initialDelay = 10000, fixedDelay = 60000) // Inicia 10 segundos después del arranque y luego cada 1 minuto
  public void consultaDeAlerta(){
    this.controlClimaService.controlarAlertaDeClima();
  }
}
