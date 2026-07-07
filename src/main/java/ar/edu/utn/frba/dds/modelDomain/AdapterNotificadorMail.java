package ar.edu.utn.frba.dds.modelDomain;

import java.io.IOException;

public interface AdapterNotificadorMail {
  void enviar(String destinatario, String asunto, String cuerpo) throws IOException;
}
