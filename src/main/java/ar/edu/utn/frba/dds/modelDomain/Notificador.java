package ar.edu.utn.frba.dds.modelDomain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Notificador {

  private final AdapterNotificadorMail adapter;

  @Autowired
  public Notificador(AdapterNotificadorMail adapter) {
    this.adapter = adapter;
  }

  public boolean notificar(List<MedioDeContacto> medioDeContactos) {
    boolean success = true;
    for (MedioDeContacto medio : medioDeContactos) {
      if (medio.getMail() != null && !medio.getMail().isEmpty()) {
        try {
          adapter.enviar(
              medio.getMail(), 
              "Alerta de clima", 
              "Atención: Se han detectado condiciones climáticas extremas en tu zona."
          );
        } catch (Exception e) {
          System.err.println("Error al enviar email a " + medio.getMail() + ": " + e.getMessage());
          success = false;
        }
      }
    }
    return success;
  }
}