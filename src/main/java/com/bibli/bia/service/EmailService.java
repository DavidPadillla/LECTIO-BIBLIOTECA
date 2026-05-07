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

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Async
    public void enviarConfirmacionReservaHTML(String destinatario, String nombreUsuario, String libro, String fecha, String categoria) {
        try {
            Email from = new Email("lectio.biblio@gmail.com");
            String subject = "Confirmacion de Reserva - Lectio Biblioteca";
            Email to = new Email(destinatario);

            String htmlMsg = "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Confirmacion de Reserva</title></head><body style='background:#f6f6f6;font-family:Arial,sans-serif;color:#2c3e50;padding:0;margin:0;'><table width='100%' cellpadding='0' cellspacing='0' style='background:#f6f6f6;padding:20px 0;'><tr><td align='center'><table width='520' cellpadding='0' cellspacing='0' style='max-width:520px;background:#fff;border-radius:16px;box-shadow:0 6px 24px rgba(44,62,80,0.08);overflow:hidden;border:1px solid #e1e1e1;'><tr><td style='background:linear-gradient(90deg,#2c3e50 0%,#27ae60 100%);padding:24px 32px;'><table width='100%' cellpadding='0' cellspacing='0'><tr><td width='60' style='vertical-align:middle;'><div style='font-size:2rem;background:#fff;color:#27ae60;padding:8px 14px;border-radius:50%;font-weight:bold;box-shadow:0 2px 10px rgba(15,15,48,0.6);display:inline-block;margin-right:15px;'>📚</div></td><td style='vertical-align:middle;'><div style='font-size:1.6rem;font-weight:700;color:#fff;letter-spacing:0.5px;'>Lectio Biblioteca</div></td></tr></table></td></tr><tr><td style='padding:32px;'><div style='text-align:center;font-size:1.2rem;margin-bottom:24px;color:#27ae60;font-weight:600;'>Tu reserva fue registrada exitosamente</div><div style='color:#2c3e50;font-weight:500;margin-bottom:12px;font-size:1.08rem;'>Hola <strong>" + nombreUsuario + "</strong>,</div><p style='color:#2c3e50;margin:0 0 16px 0;line-height:1.5;'>Gracias por confiar en <strong>Lectio Biblioteca</strong>. Aqui tienes los detalles de tu reserva:</p><div style='background:#f4f8f6;border-radius:10px;padding:18px 24px;margin-bottom:10px;font-size:1.03rem;border-left:4px solid #27ae60;'><div style='margin-bottom:8px;'><strong style='color:#2c3e50;font-weight:600;display:inline-block;min-width:120px;'>Libro:</strong> " + libro + "</div><div style='margin-bottom:8px;'><strong style='color:#2c3e50;font-weight:600;display:inline-block;min-width:120px;'>Fecha de reserva:</strong> " + fecha + "</div><div><strong style='color:#2c3e50;font-weight:600;display:inline-block;min-width:120px;'>Categoria:</strong> " + categoria + "</div></div><div style='background:#eafbf3;color:#2c3e50;border-left:4px solid #27ae60;padding:14px 20px;border-radius:8px;font-size:0.97rem;margin-top:10px;line-height:1.6;'>Por favor, acude en la fecha reservada para reclamar tu ejemplar en el area de prestamos.<br/>Recuerda llevar tu documento de identificacion.</div></td></tr><tr><td style='background:#f1faf6;text-align:center;padding:20px 15px;font-size:1.03rem;border-top:1px solid #e1e1e1;border-radius:0 0 16px 16px;'><div style='font-size:1.13rem;color:#27ae60;font-weight:700;margin-bottom:8px;letter-spacing:0.1px;'>Mas que leer, es aprender, conectar y crecer.</div><div style='color:#7f8c8d;font-size:0.98rem;margin-bottom:3px;'>Este mensaje fue enviado automaticamente, por favor no respondas.</div><div style='color:#abb8c3;font-size:0.92em;margin-top:3px;'>&copy; 2025 Lectio. Todos los derechos reservados.</div></td></tr></table></td></tr></table></body></html>";

            Content content = new Content("text/html", htmlMsg);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email enviado exitosamente a: " + destinatario);
            } else {
                System.err.println("Error al enviar email. Codigo: " + response.getStatusCode());
            }

        } catch (IOException e) {
            System.err.println("Error enviando email HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
