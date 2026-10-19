package com.bibli.bia.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Async
    public void enviarConfirmacionReservaHTML(
            String destinatario,
            String nombreUsuario,
            String libro,
            String fecha,
            String categoria) {

        // Si no hay clave, se omite el envío sin bloquear la reserva.
        if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
            System.out.println(
                    "SendGrid no está configurado. Se omite el correo de confirmación."
            );
            return;
        }

        try {
            Email from = new Email("lectio.biblio@gmail.com");
            String subject = "Confirmacion de Reserva - Lectio Biblioteca";
            Email to = new Email(destinatario);

            String htmlMsg = "<!DOCTYPE html><html lang='es'><head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Confirmacion de Reserva</title>"
                    + "</head><body style='font-family:Arial,sans-serif;color:#2c3e50;padding:20px;'>"
                    + "<h2 style='color:#27ae60;'>Lectio Biblioteca</h2>"
                    + "<h3>Tu reserva fue registrada exitosamente</h3>"
                    + "<p>Hola <strong>" + escaparHtml(nombreUsuario) + "</strong>,</p>"
                    + "<p>Gracias por confiar en Lectio Biblioteca. Aquí tienes los detalles:</p>"
                    + "<p><strong>Libro:</strong> " + escaparHtml(libro) + "</p>"
                    + "<p><strong>Fecha de reserva:</strong> " + escaparHtml(fecha) + "</p>"
                    + "<p><strong>Categoría:</strong> " + escaparHtml(categoria) + "</p>"
                    + "<p>Recuerda llevar tu documento de identificación al reclamar el libro.</p>"
                    + "<p>Más que leer, es aprender, conectar y crecer.</p>"
                    + "</body></html>";

            Content content = new Content("text/html", htmlMsg);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200
                    && response.getStatusCode() < 300) {
                System.out.println("Correo enviado exitosamente a: " + destinatario);
            } else {
                System.err.println(
                        "Error al enviar email. Código: " + response.getStatusCode()
                );
            }

        } catch (IOException e) {
            System.err.println("Error enviando email HTML: " + e.getMessage());
        }
    }

    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
