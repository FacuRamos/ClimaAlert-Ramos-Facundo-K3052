package ar.edu.utn.frba.dds.modelDomain;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MailSenderJava implements AdapterNotificadorMail {

  @Value("${sendgrid.api-key:}")
  private String sendGridApiKey;

  @Value("${sendgrid.from-email:test@example.com}")
  private String fromEmail;

  @Override
  public void enviar(String destinatario, String asunto, String cuerpo) throws IOException {
    Email from = new Email(fromEmail);
    Email to = new Email(destinatario);
    Content content = new Content("text/plain", cuerpo);
    Mail mail = new Mail(from, asunto, to, content);

    // Priorizes the configured API key, falls back to the environment variable
    String apiKey = (sendGridApiKey != null && !sendGridApiKey.isEmpty()) ? sendGridApiKey : System.getenv("SENDGRID_API_KEY");
    SendGrid sg = new SendGrid(apiKey);

    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);

      System.out.println("Status Code: " + response.getStatusCode());
      System.out.println("Body: " + response.getBody());
      System.out.println("Headers: " + response.getHeaders());

      if (response.getStatusCode() >= 400) {
        throw new IOException("Failed to send email via SendGrid, status code: " + response.getStatusCode() + ", body: " + response.getBody());
      }
    } catch (IOException ex) {
      throw ex;
    }
  }
}
