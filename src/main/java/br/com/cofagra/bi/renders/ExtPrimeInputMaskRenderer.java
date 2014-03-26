
package br.com.cofagra.bi.renders;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import lombok.extern.java.Log;

import org.primefaces.component.inputmask.InputMask;
import org.primefaces.component.inputmask.InputMaskRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:inputMask/>
 * 
 * @author thiagosampaio
 */
@Log
public class ExtPrimeInputMaskRenderer extends InputMaskRenderer{

     @Override
     public void decode(FacesContext context, UIComponent component) {

          InputMask inputMask = (InputMask) component;

          if (inputMask.isDisabled() || inputMask.isReadonly()) {
               return;
          }

          decodeBehaviors(context, inputMask);

          String clientId = inputMask.getClientId(context);
          String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

          if (submittedValue != null) {
               inputMask.setSubmittedValue(submittedValue);
          }
     }

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          InputMask inputMask = (InputMask) component;

          encodeMarkup(context, inputMask);
          encodeScript(context, inputMask);
     }

     protected void encodeScript(FacesContext context, InputMask inputMask) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = inputMask.getClientId(context);
          String mask = inputMask.getMask();

          startScript(writer, clientId);

          writer.write("PrimeFaces.cw('InputMask','" + inputMask.resolveWidgetVar() + "',{");
          writer.write("id:'" + clientId + "'");

          if (mask != null) {
               writer.write(",mask:'" + inputMask.getMask() + "'");

               if (inputMask.getPlaceHolder() != null)
                    writer.write(",placeholder:'" + inputMask.getPlaceHolder() + "'");
          }

          encodeClientBehaviors(context, inputMask);

          writer.write("});");

          endScript(writer);
     }

     protected void encodeMarkup(FacesContext context, InputMask inputMask) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = inputMask.getClientId(context);
          String styleClass = inputMask.getStyleClass();
          String defaultClass = InputMask.STYLE_CLASS;
          defaultClass = !inputMask.isValid() ? defaultClass + " ui-state-error" : defaultClass;
          
//        Comentando pois quando o disable esta trus a classe 'ui-state-disabled' estava quabrando o twitter bootstrap
//          defaultClass = inputMask.isDisabled() ? defaultClass + " ui-state-disabled" : defaultClass;
          
          styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

          writer.startElement("input", null);
          writer.writeAttribute("id", clientId, null);
          writer.writeAttribute("name", clientId, null);
          writer.writeAttribute("type", "text", null);

          String valueToRender = ComponentUtils.getValueToRender(context, inputMask);
          if (valueToRender != null) {
               writer.writeAttribute("value", valueToRender, null);
          }

          renderPassThruAttributes(context, inputMask, HTML.INPUT_TEXT_ATTRS);

          if (inputMask.isDisabled())
               writer.writeAttribute("disabled", "disabled", "disabled");
          if (inputMask.isReadonly())
               writer.writeAttribute("readonly", "readonly", "readonly");
          if (inputMask.getStyle() != null)
               writer.writeAttribute("style", inputMask.getStyle(), "style");

          writer.writeAttribute("class", styleClass, "styleClass");

          writer.endElement("input");
     }
}