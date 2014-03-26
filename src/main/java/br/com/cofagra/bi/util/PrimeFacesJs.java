
package br.com.cofagra.bi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.primefaces.context.RequestContext;

/**
 * 
 * Wrapper da classe or.primefaces.context.RequestContext para faciliar e tratar execu��es de javascript client-side.
 * 
 * @author thiago.sampaio
 * 
 */
public class PrimeFacesJs{

     /**
      * Dando scroll para componente de mensagens
      */
     public static void scrollToMessages() {

          try {

               if (RequestContext.getCurrentInstance().isAjaxRequest()) {
                    RequestContext.getCurrentInstance().execute("scrollToMessages();");
               }

          } catch (Exception e) {
          }
     }
     
     /**
      * Executa algum javascript no lado do cliente caso a request tenha sido em AJAX
      * 
      * @param script
      */
     public static void execute(String script) {
          
          try {
               
               if (RequestContext.getCurrentInstance().isAjaxRequest()) {
                    RequestContext.getCurrentInstance().execute(script);
               }
               
          } catch (Exception e) {
          }
     }
     
     /**
      * Update component
      * 
      * @param script
      */
     public static void update(String script) {
          
          try {
               
               if (RequestContext.getCurrentInstance().isAjaxRequest()) {
                    RequestContext.getCurrentInstance().update(script);
               }
               
          } catch (Exception e) {
          }
     }

     /**
      * Update components
      * 
      * @param script
      */
     public static void update(Collection<String> collections) {

          try {

               if (RequestContext.getCurrentInstance().isAjaxRequest()) {
                    RequestContext.getCurrentInstance().update(collections);
               }

          } catch (Exception e) {
          }
     }

     public static String getHostname(){
          try {
               return InetAddress.getLocalHost().getHostName();
          } catch (UnknownHostException e) {
               return "???unknown???";
          }
     }     
     
     
}
