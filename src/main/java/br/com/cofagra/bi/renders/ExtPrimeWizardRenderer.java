
package br.com.cofagra.bi.renders;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import org.primefaces.component.tabview.Tab;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.component.wizard.WizardRenderer;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.util.ComponentUtils;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:wizard/>
 * 
 * @author thiagosampaio
 */
public class ExtPrimeWizardRenderer extends WizardRenderer{

     @Override
     public void decode(FacesContext context, UIComponent component) {

          Wizard wizard = (Wizard) component;

          if (wizard.isWizardRequest(context)) {
               Map<String, String> params = context.getExternalContext().getRequestParameterMap();
               String clientId = wizard.getClientId(context);
               String stepToGo = params.get(clientId + "_stepToGo");
               String currentStep = wizard.getStep();

               FlowEvent event = new FlowEvent(wizard, currentStep, stepToGo);
               event.setPhaseId(PhaseId.INVOKE_APPLICATION);

               wizard.queueEvent(event);
          }
     }

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          Wizard wizard = (Wizard) component;

          if (wizard.isWizardRequest(context)) {
               encodeStep(context, wizard);
          } else {
               encodeMarkup(context, wizard);
               encodeScript(context, wizard);
          }
     }

     protected void encodeStep(FacesContext context, Wizard wizard) throws IOException {

          Map<String, String> params = context.getExternalContext().getRequestParameterMap();
          String stepToDisplay = wizard.getStep();
          UIComponent tabToDisplay = null;
          for (UIComponent child : wizard.getChildren()) {
               if (child.getId().equals(stepToDisplay)) {
                    tabToDisplay = child;
               }
          }

          tabToDisplay.encodeAll(context);

          RequestContext.getCurrentInstance().addCallbackParam("currentStep", wizard.getStep());
     }

     protected void encodeScript(FacesContext context, Wizard wizard) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = wizard.getClientId(context);

          UIComponent form = ComponentUtils.findParentForm(context, wizard);
          if (form == null) {
               throw new FacesException("Wizard : \"" + clientId + "\" must be inside a form element");
          }

          startScript(writer, clientId);

          writer.write("$(function() {");

          writer.write("PrimeFaces.cw('Wizard','" + wizard.resolveWidgetVar() + "',{");
          writer.write("id:'" + clientId + "'");
          writer.write(",showStepStatus:" + wizard.isShowStepStatus());
          writer.write(",showNavBar:" + wizard.isShowNavBar());

          if (wizard.getOnback() != null) {
               writer.write(",onback:function(){" + wizard.getOnback() + "}");
          }
          if (wizard.getOnnext() != null) {
               writer.write(",onnext:function(){" + wizard.getOnnext() + "}");
          }

          // all steps
          writer.write(",steps:[");
          boolean firstStep = true;
          String defaultStep = null;
          for (Iterator<UIComponent> children = wizard.getChildren().iterator(); children.hasNext();) {
               UIComponent child = children.next();

               if (child instanceof Tab && child.isRendered()) {
                    Tab tab = (Tab) child;
                    if (defaultStep == null) {
                         defaultStep = tab.getId();
                    }

                    if (!firstStep) {
                         writer.write(",");
                    } else {
                         firstStep = false;
                    }

                    writer.write("'" + tab.getId() + "'");
               }
          }
          writer.write("]");

          // current step
          if (wizard.getStep() == null) {
               wizard.setStep(defaultStep);
          }

          writer.write(",initialStep:'" + wizard.getStep() + "'");

          writer.write("});});");

          endScript(writer);
     }

     protected void encodeMarkup(FacesContext facesContext, Wizard wizard) throws IOException {

          ResponseWriter writer = facesContext.getResponseWriter();
          String clientId = wizard.getClientId(facesContext);
          String styleClass = wizard.getStyleClass() == null ? "ui-wizard ui-widget" : "ui-wizard ui-widget " + wizard.getStyleClass();

          writer.startElement("div", wizard);
          writer.writeAttribute("id", clientId, "id");
          writer.writeAttribute("class", styleClass, "styleClass");
          if (wizard.getStyle() != null) {
               writer.writeAttribute("style", wizard.getStyle(), "style");
          }

          if (wizard.isShowStepStatus()) {
               encodeStepStatus(facesContext, wizard);
          }

          encodeContent(facesContext, wizard);

          if (wizard.isShowNavBar()) {
               encodeNavigators(facesContext, wizard);
          }

          writer.endElement("div");
     }

     protected void encodeCurrentStep(FacesContext facesContext, Wizard wizard) throws IOException {

          for (UIComponent child : wizard.getChildren()) {
               if (child instanceof Tab && child.isRendered()) {
                    Tab tab = (Tab) child;

                    if ((wizard.getStep() == null || tab.getId().equals(wizard.getStep()))) {
                         tab.encodeAll(facesContext);

                         break;
                    }
               }
          }
     }

     protected void encodeNavigators(FacesContext facesContext, Wizard wizard) throws IOException {

          ResponseWriter writer = facesContext.getResponseWriter();
          String clientId = wizard.getClientId(facesContext);
          String widgetVar = wizard.resolveWidgetVar();

          writer.startElement("div", null);
          writer.writeAttribute("class", "ui-wizard-navbar ui-helper-clearfix", null);
          
               writer.startElement("ul", null);
               writer.writeAttribute("class", "pager", null);
               
                    writer.startElement("li", null);
                    writer.writeAttribute("class", "previous", null);
                         encodeNavigator(facesContext, wizard, clientId + "_back", wizard.getBackLabel(), Wizard.BACK_BUTTON_CLASS, "ui-icon-arrowthick-1-w");
                    writer.endElement("li");
                    
                    writer.startElement("li", null);
                    writer.writeAttribute("class", "next", null);
                         encodeNavigator(facesContext, wizard, clientId + "_next", wizard.getNextLabel(), Wizard.NEXT_BUTTON_CLASS, "ui-icon-arrowthick-1-e");
                    writer.endElement("li"); 
                    
               writer.endElement("ul");               
               
          writer.endElement("div");
     }

     protected void encodeContent(FacesContext facesContext, Wizard wizard) throws IOException {

          ResponseWriter writer = facesContext.getResponseWriter();
          String clientId = wizard.getClientId(facesContext);

          writer.startElement("div", null);
          writer.writeAttribute("id", clientId + "_content", "id");
          writer.writeAttribute("class", "ui-wizard-content", null);

          encodeCurrentStep(facesContext, wizard);

          writer.endElement("div");
     }

     protected void encodeStepStatus(FacesContext context, Wizard wizard) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String currentStep = wizard.getStep();
          boolean currentFound = false;
          
          writer.startElement("ul", null);
          writer.writeAttribute("class", "ui-wizard-step-titles nav nav-tabs", null);

          for (UIComponent child : wizard.getChildren()) {
               if (child instanceof Tab && child.isRendered()) {
                    
                    Tab tab = (Tab) child;
                    boolean active = (!currentFound) && (currentStep == null || tab.getId().equals(currentStep));
                    
                    String titleStyleClass = active ? "ui-wizard-step-title ui-state-highlight " : "ui-wizard-step-title ";
                    
                    if (tab.getTitleStyleClass() != null) {
                         titleStyleClass = titleStyleClass + " " + tab.getTitleStyleClass();
                    }

                    if (active) {
                         currentFound = true;
                    }

                    writer.startElement("li", null);
                    writer.writeAttribute("class", titleStyleClass, null);
                    if (tab.getTitleStyle() != null)
                         writer.writeAttribute("style", tab.getTitleStyle(), null);
                    if (tab.getTitletip() != null)
                         writer.writeAttribute("title", tab.getTitletip(), null);

                    writer.startElement("a", null);
                    writer.writeAttribute("href", "#", null);
                         writer.write(tab.getTitle());
                    writer.endElement("a");

                    writer.endElement("li");
               }
          }

          writer.endElement("ul");
     }

     protected void encodeNavigator(FacesContext facesContext, Wizard wizard, String id, String label, String buttonClass, String icon) throws IOException {

          ResponseWriter writer = facesContext.getResponseWriter();
          
          writer.startElement("button", null);
          writer.writeAttribute("id", id, null);
          writer.writeAttribute("name", id, null);
          writer.writeAttribute("type", "button", null);
          writer.writeAttribute("class", "ui-button ui-widget ui-corner-all ui-button-text-icon-left" + " " + buttonClass, null);

          // button icon
          if(Wizard.BACK_BUTTON_CLASS.equalsIgnoreCase(buttonClass)){
               
               writer.startElement("i", null);
               writer.writeAttribute("class", "icon-double-angle-left", null);
               writer.endElement("i");
               
               writer.writeText(label, "value");
               
          }else{
               
               writer.writeText(label, "value");
               
               writer.startElement("i", null);
               writer.writeAttribute("class", "icon-double-angle-right", null);
               writer.endElement("i");
          }          

          writer.endElement("button");
     }

     @Override
     public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {

          // Rendering happens on encodeEnd
     }

     @Override
     public boolean getRendersChildren() {

          return true;
     }
}