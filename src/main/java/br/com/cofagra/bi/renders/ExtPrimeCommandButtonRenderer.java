
package br.com.cofagra.bi.renders;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import lombok.extern.java.Log;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandbutton.CommandButtonRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

import br.com.twsoftware.alfred.object.Objeto;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:commandButton/>
 * 
 * @author thiagosampaio
 */
@Log
public class ExtPrimeCommandButtonRenderer extends CommandButtonRenderer{

     @Override
     public void decode(FacesContext context, UIComponent component) {

          CommandButton button = (CommandButton) component;
          if (button.isDisabled()) {
               return;
          }

          String param = component.getClientId(context);
          if (context.getExternalContext().getRequestParameterMap().containsKey(param)) {
               component.queueEvent(new ActionEvent(component));
          }
     }

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          CommandButton button = (CommandButton) component;

          encodeMarkup(context, button);
          encodeScript(context, button);
     }

     protected void encodeMarkup(FacesContext context, CommandButton button) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = button.getClientId(context);
          String type = button.getType();
          String value = (String) button.getValue();
          String icon = button.resolveIcon();

          StringBuilder onclick = new StringBuilder();
          if (button.getOnclick() != null) {
               onclick.append(button.getOnclick()).append(";");
          }

          writer.startElement("button", button);
          writer.writeAttribute("id", clientId, "id");
          writer.writeAttribute("name", clientId, "name");
          
          //Comentando as classes do bot�o do prime para colocar panas do twitter bootstrap
//          writer.writeAttribute("class", button.resolveStyleClass(), "styleClass");
          
          String classesBtn = button.resolveStyleClass();
          String[] removerClasses = {"ui-button","ui-widget","ui-state-default","ui-corner-all","ui-button-text-only"};
          for(String klass : removerClasses){
               if(Objeto.notBlank(classesBtn) && classesBtn.contains(klass)){
                    
                    classesBtn = classesBtn.replaceAll(klass + "[\\w|-]*", "");
               }
          }
          writer.writeAttribute("class", classesBtn, "styleClass");

          if (!type.equals("reset") && !type.equals("button")) {
               String request;

               if (button.isAjax()) {
                    request = buildAjaxRequest(context, button, null);
               } else {
                    UIComponent form = ComponentUtils.findParentForm(context, button);
                    if (form == null) {
                         throw new FacesException("CommandButton : \"" + clientId + "\" must be inside a form element");
                    }

                    request = buildNonAjaxRequest(context, button, form, null, false);
               }

               onclick.append(request);
          }

          String onclickBehaviors = getOnclickBehaviors(context, button);
          if (onclickBehaviors != null) {
               onclick.append(onclickBehaviors).append(";");
          }

          if (onclick.length() > 0) {
               writer.writeAttribute("onclick", onclick.toString(), "onclick");
          }

          renderPassThruAttributes(context, button, HTML.BUTTON_ATTRS, HTML.CLICK_EVENT);

          if (button.isDisabled())
               writer.writeAttribute("disabled", "disabled", "disabled");
          if (button.isReadonly())
               writer.writeAttribute("readonly", "readonly", "readonly");

          // icon
          if (icon != null) {
               
//               String defaultIconClass = button.getIconPos().equals("left") ? HTML.BUTTON_LEFT_ICON_CLASS : HTML.BUTTON_RIGHT_ICON_CLASS;
               String iconClass = " " + icon;
               if(button.getIconPos().equals("right")){
                    
                    if (value == null) {
                         writer.write("ui-button");
                    } else {
                         if (button.isEscape())
                              writer.writeText(value, "value");
                         else
                              writer.write(value);
                    }    
                    
                    writer.startElement("i", null);
                    writer.writeAttribute("class", iconClass, null);
                    writer.writeAttribute("style", "padding-left:4px", null);
                    writer.endElement("i");
                    
               }else{
                    
                    writer.startElement("i", null);
                    writer.writeAttribute("class", iconClass, null);
                    writer.writeAttribute("style", "padding-right:4px", null);
                    writer.endElement("i");
                    
                    if (value == null) {
                         writer.write("ui-button");
                    } else {
                         if (button.isEscape())
                              writer.writeText(value, "value");
                         else
                              writer.write(value);
                    }    
               }

          }else{
               
               if (value == null) {
                    writer.write("ui-button");
               } else {
                    if (button.isEscape())
                         writer.writeText(value, "value");
                    else
                         writer.write(value);
               }    
               
          }

          writer.endElement("button");
     }

     protected void encodeScript(FacesContext context, CommandButton button) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = button.getClientId(context);

          startScript(writer, clientId);

          writer.write("PrimeFaces.cw('CommandButton','" + button.resolveWidgetVar() + "',{");
          writer.write("id:'" + clientId + "'");
          writer.write("});");

          endScript(writer);
     }
     
     public static void main(String[] args) {

          String classesBtn = "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only btn-primary";
          
          String[] removerClasses = {"ui-button","ui-widget","ui-state-default","ui-corner-all","ui-button-text-only"};
          
          for(String klass : removerClasses){
               String regx = klass + "[\\w|-]*";
               if(Objeto.notBlank(classesBtn) && classesBtn.contains(klass)){
                    
                    classesBtn = classesBtn.replaceAll(klass + "[\\w|-]*", "");
               }
          }
          
          System.out.println(classesBtn);
          
     }
}