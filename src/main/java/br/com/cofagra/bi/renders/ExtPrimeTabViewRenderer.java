
package br.com.cofagra.bi.renders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.component.tabview.TabViewRenderer;

import br.com.twsoftware.alfred.object.Objeto;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:tabview/>
 * 
 * @author thiagosampaio
 */
public class ExtPrimeTabViewRenderer extends TabViewRenderer{

     @Override
     public void decode(FacesContext context, UIComponent component) {

          Map<String, String> params = context.getExternalContext().getRequestParameterMap();
          TabView tabView = (TabView) component;
          String activeIndexValue = params.get(tabView.getClientId(context) + "_activeIndex");

          if (!isValueEmpty(activeIndexValue)) {
               tabView.setActiveIndex(Integer.parseInt(activeIndexValue));
          }

          decodeBehaviors(context, component);
     }

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          Map<String, String> params = context.getExternalContext().getRequestParameterMap();
          TabView tabView = (TabView) component;
          String clientId = tabView.getClientId(context);
          String var = tabView.getVar();

          if (tabView.isContentLoadRequest(context)) {
               Tab tabToLoad = null;

               if (var == null) {
                    String tabClientId = params.get(clientId + "_newTab");
                    tabToLoad = (Tab) tabView.findTab(tabClientId);

                    tabToLoad.encodeAll(context);
                    tabToLoad.setLoaded(true);
               } else {
                    int tabindex = Integer.parseInt(params.get(clientId + "_tabindex"));
                    tabView.setRowIndex(tabindex);
                    tabToLoad = (Tab) tabView.getChildren().get(0);
                    tabToLoad.encodeAll(context);
                    tabView.setRowIndex(-1);
               }

          } else {
               encodeMarkup(context, tabView);
               encodeScript(context, tabView);
          }
     }

     protected void encodeScript(FacesContext context, TabView tabView) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = tabView.getClientId(context);
          boolean dynamic = tabView.isDynamic();

          startScript(writer, clientId);

          writer.write("PrimeFaces.cw('TabView','" + tabView.resolveWidgetVar() + "',{");
          writer.write("id:'" + clientId + "'");

          if (dynamic) {
               writer.write(",dynamic:true");
               writer.write(",cache:" + tabView.isCache());
          }

          if (tabView.getOnTabChange() != null)
               writer.write(",onTabChange: function(index) {" + tabView.getOnTabChange() + "}");
          if (tabView.getOnTabShow() != null)
               writer.write(",onTabShow:function(index) {" + tabView.getOnTabShow() + "}");

          if (tabView.getEffect() != null) {
               writer.write(",effect: {");
               writer.write("name:'" + tabView.getEffect() + "'");
               writer.write(",duration:'" + tabView.getEffectDuration() + "'");
               writer.write("}");
          }

          encodeClientBehaviors(context, tabView);

          writer.write("});");

          endScript(writer);
     }

     protected void encodeMarkup(FacesContext context, TabView tabView) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String clientId = tabView.getClientId(context);
          String orientation = tabView.getOrientation();
          String styleClass = tabView.getStyleClass();
          String defaultStyleClass = TabView.CONTAINER_CLASS + " ui-tabs-" + orientation;

          // Removendo a classe ui-widget-content
          defaultStyleClass = defaultStyleClass.replaceAll("ui-widget-content", "ui-widget-content-");

          styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

          writer.startElement("div", tabView);
          writer.writeAttribute("id", clientId, null);
          writer.writeAttribute("class", styleClass, "styleClass");
          if (tabView.getStyle() != null) {
               writer.writeAttribute("style", tabView.getStyle(), "style");
          }

          if (orientation.equals("bottom")) {
               encodeContents(context, tabView);
               encodeHeaders(context, tabView);

          } else {
               encodeHeaders(context, tabView);
               encodeContents(context, tabView);
          }

          encodeActiveIndexHolder(context, tabView);

          writer.endElement("div");
     }

     protected void encodeActiveIndexHolder(FacesContext facesContext, TabView tabView) throws IOException {

          ResponseWriter writer = facesContext.getResponseWriter();
          String paramName = tabView.getClientId(facesContext) + "_activeIndex";

          writer.startElement("input", null);
          writer.writeAttribute("type", "hidden", null);
          writer.writeAttribute("id", paramName, null);
          writer.writeAttribute("name", paramName, null);
          writer.writeAttribute("value", tabView.getActiveIndex(), null);
          writer.endElement("input");
     }

     protected void encodeHeaders(FacesContext context, TabView tabView) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String var = tabView.getVar();
          int activeIndex = tabView.getActiveIndex();

          writer.startElement("ul", null);
          writer.writeAttribute("class", TabView.NAVIGATOR_CLASS, null);
          writer.writeAttribute("role", "tablist", null);

          if (var == null) {
               int i = 0;
               for (UIComponent kid : tabView.getChildren()) {
                    if (kid.isRendered() && kid instanceof Tab) {
                         encodeTabHeader(context, tabView, (Tab) kid, (i == activeIndex));
                         i++;
                    }
               }
          } else {
               int dataCount = tabView.getRowCount();

               // boundary check
               activeIndex = activeIndex >= dataCount ? 0 : activeIndex;

               Tab tab = (Tab) tabView.getChildren().get(0);

               for (int i = 0; i < dataCount; i++) {
                    tabView.setRowIndex(i);

                    encodeTabHeader(context, tabView, tab, (i == activeIndex));
               }

               tabView.setRowIndex(-1);
          }

          writer.endElement("ul");
     }

     protected void encodeTabHeader(FacesContext context, TabView tabView, Tab tab, boolean active) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String defaultStyleClass = active ? TabView.ACTIVE_TAB_HEADER_CLASS : TabView.INACTIVE_TAB_HEADER_CLASS;
          defaultStyleClass = defaultStyleClass + " ui-corner-" + tabView.getOrientation();   // cornering
          String styleClass = tab.getTitleStyleClass();
          styleClass = tab.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;
          styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;
          UIComponent titleFacet = tab.getFacet("title");

          // Criando icones
          List<String> icones = new ArrayList<String>();
          if (Objeto.notBlank(styleClass)) {
               Matcher matcher = Pattern.compile("icon[\\-|\\w]+").matcher(styleClass);
               while (matcher.find()) {
                    icones.add(matcher.group());
                    styleClass = styleClass.replaceAll(matcher.group(), "");
               }
          }

          // header container
          writer.startElement("li", null);
          writer.writeAttribute("class", styleClass, null);
          writer.writeAttribute("role", "tab", null);
          writer.writeAttribute("aria-expanded", String.valueOf(active), null);
          if (tab.getTitleStyle() != null)
               writer.writeAttribute("style", tab.getTitleStyle(), null);
          if (tab.getTitletip() != null)
               writer.writeAttribute("title", tab.getTitletip(), null);

          // title
          writer.startElement("a", null);
          writer.writeAttribute("href", "#" + tab.getClientId(context), null);

          for (String icone : icones) {
               writer.startElement("i", null);
               writer.writeAttribute("class", icone, null);
               writer.writeAttribute("style", "margin-right: 3px;", null);
               writer.endElement("i");
          }

          if (titleFacet == null) {
               writer.write(tab.getTitle());
          } else {
               titleFacet.encodeAll(context);
          }
          writer.endElement("a");

          // closable
          if (tab.isClosable()) {
               writer.startElement("span", null);
               writer.writeAttribute("class", "ui-icon ui-icon-close", null);
               writer.endElement("span");
          }

          writer.endElement("li");
     }

     protected void encodeContents(FacesContext context, TabView tabView) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String var = tabView.getVar();
          int activeIndex = tabView.getActiveIndex();
          boolean dynamic = tabView.isDynamic();

          writer.startElement("div", null);
          writer.writeAttribute("class", TabView.PANELS_CLASS, null);

          if (var == null) {
               int i = 0;
               for (UIComponent kid : tabView.getChildren()) {
                    if (kid.isRendered() && kid instanceof Tab) {
                         encodeTabContent(context, (Tab) kid, (i == activeIndex), dynamic);
                         i++;
                    }
               }
          } else {
               int dataCount = tabView.getRowCount();

               // boundary check
               activeIndex = activeIndex >= dataCount ? 0 : activeIndex;

               Tab tab = (Tab) tabView.getChildren().get(0);

               for (int i = 0; i < dataCount; i++) {
                    tabView.setRowIndex(i);

                    encodeTabContent(context, tab, (i == activeIndex), dynamic);
               }

               tabView.setRowIndex(-1);
          }

          writer.endElement("div");
     }

     protected void encodeTabContent(FacesContext context, Tab tab, boolean active, boolean dynamic) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          String styleClass = active ? TabView.ACTIVE_TAB_CONTENT_CLASS : TabView.INACTIVE_TAB_CONTENT_CLASS;

          // Removendo a classe ui-widget-content
          styleClass = styleClass.replaceAll("ui-widget-content", "ui-widget-content-");

          writer.startElement("div", null);
          writer.writeAttribute("id", tab.getClientId(context), null);
          writer.writeAttribute("class", styleClass, null);
          writer.writeAttribute("role", "tabpanel", null);
          writer.writeAttribute("aria-hidden", String.valueOf(!active), null);

          if (dynamic) {
               if (active) {
                    tab.encodeAll(context);
                    tab.setLoaded(true);
               }
          } else {
               tab.encodeAll(context);
          }

          writer.endElement("div");
     }

     @Override
     public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

          // Rendering happens on encodeEnd
     }

     @Override
     public boolean getRendersChildren() {

          return true;
     }
}