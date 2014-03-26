
package br.com.cofagra.bi.renders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import lombok.extern.java.Log;

import org.primefaces.component.messages.Messages;
import org.primefaces.component.messages.MessagesRenderer;

import br.com.twsoftware.alfred.object.Objeto;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:messages/>
 * 
 * @author thiagosampaio
 */
@Log
public class ExtMessageRenderer extends MessagesRenderer{

     private boolean hasMsg = false;

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          Messages uiMessages = (Messages) component;
          ResponseWriter writer = context.getResponseWriter();
          String clientId = uiMessages.getClientId(context);
          Map<String, List<FacesMessage>> messagesMap = new HashMap<String, List<FacesMessage>>();

          String _for = uiMessages.getFor();
          Iterator<FacesMessage> messages;
          if (_for != null) {
               messages = context.getMessages(_for);
          } else {
               messages = uiMessages.isGlobalOnly() ? context.getMessages(null) : context.getMessages();
          }

          while (messages.hasNext()) {
               hasMsg = true;
               FacesMessage message = messages.next();
               FacesMessage.Severity severity = message.getSeverity();

               if (severity.equals(FacesMessage.SEVERITY_INFO))
                    addMessage(uiMessages, message, messagesMap, "info");
               else if (severity.equals(FacesMessage.SEVERITY_WARN))
                    addMessage(uiMessages, message, messagesMap, "warning");
               else if (severity.equals(FacesMessage.SEVERITY_ERROR))
                    addMessage(uiMessages, message, messagesMap, "danger");
               else if (severity.equals(FacesMessage.SEVERITY_FATAL))
                    addMessage(uiMessages, message, messagesMap, "success");
          }

          writer.startElement("div", uiMessages);
          writer.writeAttribute("id", clientId, "id");
          writer.writeAttribute("class", " ", null);

          for (String severity : messagesMap.keySet()) {
               List<FacesMessage> severityMessages = messagesMap.get(severity);

               if (severityMessages.size() > 0) {
                    encodeSeverityMessages(context, uiMessages, severity, severityMessages);
               }
          }

          writer.endElement("div");
     }

     protected void addMessage(Messages uiMessages, FacesMessage message, Map<String, List<FacesMessage>> messagesMap, String severity) {

          if (shouldRender(uiMessages, message, severity)) {
               List<FacesMessage> severityMessages = messagesMap.get(severity);

               if (severityMessages == null) {
                    severityMessages = new ArrayList<FacesMessage>();
                    messagesMap.put(severity, severityMessages);
               }

               severityMessages.add(message);
          }
     }

     protected void encodeSeverityMessages(FacesContext context, Messages uiMessages, String severity, List<FacesMessage> messages) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String styleClassPrefix = "alert alert-" + severity;
          boolean escape = uiMessages.isEscape();

          writer.startElement("div", null);
          writer.writeAttribute("class", styleClassPrefix, null);

          boolean close = false;
          //Funcionada para o PrimeFaces 3.3.1
          try{
               
               String closable = (String) uiMessages.getAttributes().get("closable");
               if(Objeto.notBlank(closable) && new Boolean(closable)){
                    close = true;  
               }
               
          }catch(Exception e){
          }
          
          //Funcionada para o PrimeFaces 3.5+
          try{
               
               Boolean closable = (Boolean) uiMessages.getAttributes().get("closable");
               if(Objeto.notBlank(closable) && closable){
                  close = true;  
               }
               
          }catch(Exception e){
          }
          
          if (hasMsg && close) {
               
               writer.startElement("button", null);
               writer.writeAttribute("class", "close", null);
               writer.writeAttribute("data-dismiss", "alert", null);
               writer.writeAttribute("type", "button", null);
               writer.writeText("×", null);
               writer.endElement("button");
          }

          //Escondendo o icone. Depois adicionar um icone do bootstrap caso venha
          // writer.startElement("span", null);
          // writer.writeAttribute("class", styleClassPrefix + "-icon", null);
          // writer.endElement("span");

          if (Objeto.notBlank(messages) && messages.size() == 1) {

               String summary = messages.get(0).getSummary() != null ? messages.get(0).getSummary() : "";
               String detail = messages.get(0).getDetail() != null ? messages.get(0).getDetail() : summary;

               if (uiMessages.isShowSummary()) {
                    writer.startElement("strong", null);

                    if (escape) {
                         writer.writeText(summary, null);
                    } else {
                         writer.write(summary);
                    }

                    writer.endElement("strong");
               }

               if (uiMessages.isShowDetail()) {

                    if (escape) {
                         writer.writeText(detail, null);
                    } else {
                         writer.write(detail);
                    }

               }

               messages.get(0).rendered();

          } else {

               writer.startElement("ul", null);
               
               String tipoLista = "unstyled";
               if (Objeto.notBlank(messages) && messages.size() > 1) {
                    tipoLista = "";
               }
               
               writer.writeAttribute("class", tipoLista, null);

               for (FacesMessage msg : messages) {

                    writer.startElement("li", null);

                    String summary = msg.getSummary() != null ? msg.getSummary() : "";
                    String detail = msg.getDetail() != null ? msg.getDetail() : summary;

                    if (uiMessages.isShowSummary()) {
                         writer.startElement("strong", null);

                         if (escape) {
                              writer.writeText(summary, null);
                         } else {
                              writer.write(summary);
                         }

                         writer.endElement("strong");
                    }

                    if (uiMessages.isShowDetail()) {

                         if (escape) {
                              writer.writeText(detail, null);
                         } else {
                              writer.write(detail);
                         }

                    }

                    writer.endElement("li");

                    msg.rendered();
               }

               writer.endElement("ul");

          }

          writer.endElement("div");
     }

}