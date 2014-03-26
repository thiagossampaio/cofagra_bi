
package br.com.cofagra.bi.renders;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.util.ComponentUtils;

/**
 * Render respons�vel por sobrescrever o componente
 * <p:commandButton/>
 * 
 * @author thiagosampaio
 */
public class ExtPrimeBreadCrumbRenderer extends BaseMenuRenderer{

     @Override
     public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

          BreadCrumb breadCrumb = (BreadCrumb) component;

          if (breadCrumb.isDynamic()) {
               breadCrumb.buildMenuFromModel();
          }

          encodeMarkup(context, breadCrumb);
     }

     protected void encodeMarkup(FacesContext context, AbstractMenu menu) throws IOException {

          ResponseWriter writer = context.getResponseWriter();
          BreadCrumb breadCrumb = (BreadCrumb) menu;
          String clientId = breadCrumb.getClientId(context);
          String styleClass = breadCrumb.getStyleClass();
          styleClass = styleClass == null ? BreadCrumb.CONTAINER_CLASS : BreadCrumb.CONTAINER_CLASS + " " + styleClass;

          // home icon for first item
          if (breadCrumb.getChildCount() > 0) {
//               ((MenuItem) breadCrumb.getChildren().get(0)).setStyleClass("ui-icon ui-icon-home");
               ((MenuItem) breadCrumb.getChildren().get(0)).setStyleClass("ui-icon ui-icon-home");
          }

          writer.startElement("div", null);
          writer.writeAttribute("id", clientId, null);
//          writer.writeAttribute("class", styleClass, null);
//          writer.writeAttribute("role", "menu", null);
          if (breadCrumb.getStyle() != null) {
               writer.writeAttribute("style", breadCrumb.getStyle(), null);
          }
          
          writer.startElement("ul", null);
          writer.writeAttribute("class", "breadcrumb", null);

          for (int i = 0; i < breadCrumb.getChildCount(); i++) {
               UIComponent child = breadCrumb.getChildren().get(i);

               if (child.isRendered() && child instanceof MenuItem) {
                    MenuItem item = (MenuItem) child;

                    // dont render chevron before home icon
                    if (i != 0) {
                         
                         writer.startElement("li", null);
                         writer.startElement("span", null);
                         writer.writeAttribute("class", "divider", null);
                         writer.write("/");
                         writer.endElement("span");
                         
                         writer.endElement("li");
                         
                    }else{
                         
                    }

                    writer.startElement("li", null);
//                    writer.writeAttribute("role", "menuitem", null);

                    if (item.isDisabled())
                         encodeDisabledMenuItem(context, (MenuItem) child);
                    else
                         encodeMenuItem(context, (MenuItem) child);

                    writer.endElement("li");
               }
          }

          writer.endElement("ul");

          writer.endElement("div");
     }
     
     @Override
     protected void encodeMenuItem(FacesContext context, MenuItem menuItem) throws IOException {

          String clientId = menuItem.getClientId(context);
          ResponseWriter writer = context.getResponseWriter();
          String icon = menuItem.getIcon();
          String title = menuItem.getTitle();

          if (menuItem.shouldRenderChildren()) {
               
               renderChildren(context, menuItem);
               
          } else {
               
               boolean disabled = menuItem.isDisabled();
               String onclick = menuItem.getOnclick();

               writer.startElement("a", null);
               writer.writeAttribute("id", menuItem.getClientId(context), null);
               if (title != null) {
                    writer.writeAttribute("title", title, null);
               }

               String styleClass = menuItem.getStyleClass();
               styleClass = styleClass == null ? AbstractMenu.MENUITEM_LINK_CLASS : AbstractMenu.MENUITEM_LINK_CLASS + " " + styleClass;
               styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

               writer.writeAttribute("class", styleClass, null);

               if (menuItem.getStyle() != null) {
                    writer.writeAttribute("style", menuItem.getStyle(), null);
               }

               // GET
               if (menuItem.getUrl() != null || menuItem.getOutcome() != null) {
                    String targetURL = getTargetURL(context, menuItem);
                    String href = disabled ? "javascript:void(0)" : targetURL;
                    writer.writeAttribute("href", href, null);

                    if (menuItem.getTarget() != null) {
                         writer.writeAttribute("target", menuItem.getTarget(), null);
                    }
               }
               // POST
               else {
                    writer.writeAttribute("href", "javascript:void(0)", null);

                    UIComponent form = ComponentUtils.findParentForm(context, menuItem);
                    if (form == null) {
                         throw new FacesException("MenuItem must be inside a form element");
                    }

                    String command = menuItem.isAjax() ? buildAjaxRequest(context, menuItem, form) : buildNonAjaxRequest(context, menuItem, form, clientId, true);

                    onclick = onclick == null ? command : onclick + ";" + command;
               }

               if (onclick != null && !disabled) {
                    writer.writeAttribute("onclick", onclick, null);
               }

               if (icon != null) {
                    writer.startElement("span", null);
                    writer.writeAttribute("class", AbstractMenu.MENUITEM_ICON_CLASS + " " + icon, null);
                    writer.endElement("span");
               }

               if (menuItem.getValue() != null) {
                    writer.startElement("span", null);
                    writer.writeAttribute("class", AbstractMenu.MENUITEM_TEXT_CLASS, null);
                    writer.writeText((String) menuItem.getValue(), "value");
                    writer.endElement("span");
               }

               writer.endElement("a");
          }
     }     

     @Override
     public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

          // Do nothing
     }

     @Override
     public boolean getRendersChildren() {

          return true;
     }

     @Override
     protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {

          // Do nothing
     }

     private void encodeDisabledMenuItem(FacesContext context, MenuItem menuItem) throws IOException {

          ResponseWriter writer = context.getResponseWriter();

          String style = menuItem.getStyle();
          String styleClass = menuItem.getStyleClass();
          styleClass = styleClass == null ? BreadCrumb.MENUITEM_LINK_CLASS : BreadCrumb.MENUITEM_LINK_CLASS + " " + styleClass;
          styleClass += " ui-state-disabled";

          writer.startElement("span", null);
          writer.writeAttribute("class", styleClass, null);
          if (menuItem.getStyle() != null) {
               writer.writeAttribute("style", menuItem.getStyle(), null);
          }

          writer.startElement("span", null);
          writer.writeAttribute("class", BreadCrumb.MENUITEM_TEXT_CLASS, null);
          writer.writeText((String) menuItem.getValue(), "value");
          writer.endElement("span");

          writer.endElement("span");
     }
}