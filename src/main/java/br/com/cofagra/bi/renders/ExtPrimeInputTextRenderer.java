
package br.com.cofagra.bi.renders;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import lombok.extern.java.Log;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtext.InputTextRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:inputText/>
 * 
 * @author thiagosampaio
 */
@Log
public class ExtPrimeInputTextRenderer extends InputTextRenderer{

     @Override
     public void decode(FacesContext context, UIComponent component) {

          InputText inputText = (InputText) component;

          if (inputText.isDisabled() || inputText.isReadonly()) {
               return;
          }

          decodeBehaviors(context, inputText);

          String clientId = inputText.getClientId(context);
          String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

          if (submittedValue != null) {
               inputText.setSubmittedValue(submittedValue);
          }
     }

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          InputText inputText = (InputText) component;

          encodeMarkup(context, inputText);
          encodeScript(context, inputText);
     }

     protected void encodeScript(FacesContext context, InputText inputText) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = inputText.getClientId(context);

          startScript(writer, clientId);

          writer.write("PrimeFaces.cw('InputText','" + inputText.resolveWidgetVar() + "',{");
          writer.write("id:'" + clientId + "'");

          encodeClientBehaviors(context, inputText);

          writer.write("});");

          endScript(writer);
     }

     protected void encodeMarkup(FacesContext context, InputText inputText) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = inputText.getClientId(context);

          writer.startElement("input", null);
          writer.writeAttribute("id", clientId, null);
          writer.writeAttribute("name", clientId, null);
          writer.writeAttribute("type", inputText.getType(), null);

          String valueToRender = ComponentUtils.getValueToRender(context, inputText);
          if (valueToRender != null) {
               writer.writeAttribute("value", valueToRender, null);
          }

          renderPassThruAttributes(context, inputText, HTML.INPUT_TEXT_ATTRS);

          if (inputText.isDisabled())
               writer.writeAttribute("disabled", "disabled", null);
          if (inputText.isReadonly())
               writer.writeAttribute("readonly", "readonly", null);
          if (inputText.getStyle() != null)
               writer.writeAttribute("style", inputText.getStyle(), null);

          writer.writeAttribute("class", createStyleClass(inputText), "styleClass");

          writer.endElement("input");
     }

     protected String createStyleClass(InputText inputText) {

          String defaultClass = InputText.STYLE_CLASS;
          defaultClass = inputText.isValid() ? defaultClass : defaultClass + " ui-state-error";
          defaultClass = !inputText.isDisabled() ? defaultClass : defaultClass + " ";
          
//        Comentando pois quando o disable esta trus a classe 'ui-state-disabled' estava quabrando o twitter bootstrap
//          defaultClass = !inputText.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

          String styleClass = inputText.getStyleClass();
          styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

          return styleClass;
     }
}