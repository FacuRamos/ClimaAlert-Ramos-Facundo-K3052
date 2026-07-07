package ar.edu.utn.frba.dds.modelDomain;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class Example {
  public static void main(String[] args) throws IOException {
    // IMPORTANTE: Este mail de origen DEBE estar verificado en tu cuenta de SendGrid
    Email from = new Email("ramosfacundo01@gmail.com");
    String subject = "Sending with SendGrid is Fun";
    Email to = new Email("ramosfacu05@gmail.com");
    Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
    Mail mail = new Mail(from, subject, to, content);

    // Reemplaza esto con tu API Key real de SendGrid (ej: "SG.xxxx") en tu .env o variables de entorno
    String apiKey = System.getenv("SENDGRID_API_KEY");
    SendGrid sg = new SendGrid(apiKey);
    // sg.setDataResidency("eu");
    // uncomment the above line if you are sending mail using a regional EU subuser
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
  }
}
