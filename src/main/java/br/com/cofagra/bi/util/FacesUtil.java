package br.com.cofagra.bi.util;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.twsoftware.alfred.object.Objeto;

/**
 * @author thiago.sampaio
 */
public final class FacesUtil {

     /**
      * Retorna o contexto
      * 
      * @return: ServletContext
      */
     public static ServletContext getServletContext() {
          return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
     }

     /**
      * Retorna o ManagedBean
      * 
      * @return: Object
      * @param beanName
      */
     public static Object getManagedBean(String beanName) {
          Object o = getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());

          return o;
     }

     /**
      * Reseta o Managed Bean
      * 
      * @return: void
      * @param beanName
      */
     public static void resetManagedBean(String beanName) {
          getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
     }

     /**
      * Seta um ManagedBean na session
      * 
      * @return: void
      * @param beanName
      * @param managedBean
      */
     public static void setManagedBeanInSession(String beanName, Object managedBean) {
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
     }

     /**
      * Retorna um determinado parametro que esteja na request
      * 
      * @return: String
      * @param name
      */
     public static Object getRequestParameter(Object name) {
          return (Object) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
     }

     /**
      * Seta determinado parametro na request
      * 
      * @author: Thiago Sampaio
      * @return: void
      * @param key
      * @param value
      */
     public static void setRequestParameter(String key, Object value) {
          HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
          req.setAttribute(key, value);
     }

     /**
      * Seta um objeto na sessao
      * 
      * @return: String
      * @param name
      */
     public static Object setSessionAttribute(String key, Object value) {
          return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
     }

     /**
      * Retorna um atributo na sessao
      * 
      * @return: String
      * @param name
      */
     public static Object getSessionAttribute(String key) {
          return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
     }

     /**
      * @author: Thiago Sampaio
      * @return: Application
      */
     public static Application getApplication() {
          ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
          return appFactory.getApplication();
     }

     /**
      * @return: ValueBinding
      * @param el
      */
     public static ValueBinding getValueBinding(String el) {
          return getApplication().createValueBinding(el);
     }

     /**
      * Retorna a resquest
      * 
      * @return: HttpServletRequest
      */
     public static HttpServletRequest getServletRequest() {
          return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     }

     /**
      * Retorna o response
      * 
      * @return: HttpServletResponse
      */
     public static HttpServletResponse getServletResponse() {
          return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
     }

     /**
      * @return: Object
      * @param el
      */
     @SuppressWarnings("unused")
     public static Object getElValue(String el) {
          return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
     }

     /**
      * @return: String
      * @param value
      */
     public static String getJsfEl(String value) {
          return "#{" + value + "}";
     }

     /**
      * Retorna a raiz da aplica��o
      * 
      * @return: String
      */
     public static String getRealPath(String pasta) {
          return getServletContext().getRealPath(File.separator + pasta);
     }

     /**
      * Adiciona um atributo no escopo de flash.
      * 
      * @param nome
      * .
      * @param objeto
      * .
      */
     public static void adicionarAtributoFlash(String nome, Object objeto) {
          getContexto().getExternalContext().getFlash().put(nome, objeto);
     }

     /**
      * Obt�m o path real do contexto
      * 
      * @return
      */
     public String getContextPath() {
          return getServletContext().getContextPath();
     }

     /**
      * Obtem um atributo do escopo flash.
      * 
      * @param nome
      * .
      * @return object.
      */
     public static Object obterAtributoFlash(String nome) {
          return getContexto().getExternalContext().getFlash().get(nome);
     }

     /**
      * Obt�m o contexto do faces.
      * 
      * @return contexto.
      */
     public static FacesContext getContexto() {
          return FacesContext.getCurrentInstance();
     }
     
     /**
      * Adiciona uma mensagem no contexto do faces.
      * 
      * @param texto
      * - nome da chave.
      */
     public static void adicionarInfoGrowl(String texto) {
          registrarFacesMessageGrowl(obterTexto(texto), FacesMessage.SEVERITY_INFO);
     }

     /**
      * Adiciona uma mensagem no contexto do faces.
      * 
      * @param texto
      * - nome da chave.
      */
     public static void adicionarInfo(String texto) {
          registrarFacesMessage(obterTexto(texto), FacesMessage.SEVERITY_INFO);
     }
     
     /**
      * Adiciona uma mensagem no contexto do faces, formatada com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarInfoGrowl(String texto, Object... params) {
          registrarFacesMessageGrowl(obterTexto(texto, params), FacesMessage.SEVERITY_INFO);
     }

     /**
      * Adiciona uma mensagem no contexto do faces, formatada com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarInfo(String texto, Object... params) {
          registrarFacesMessage(obterTexto(texto, params), FacesMessage.SEVERITY_INFO);
     }
     
     /**
      * Adiciona um aviso no contexto do faces.
      * 
      * @param texto
      * - nome da chave.
      */
     public static void adicionarWarnGrowl(String texto) {
          registrarFacesMessageGrowl(obterTexto(texto), FacesMessage.SEVERITY_WARN);
     }

     /**
      * Adiciona um aviso no contexto do faces.
      * 
      * @param texto
      * - nome da chave.
      */
     public static void adicionarWarn(String texto) {
          registrarFacesMessage(obterTexto(texto), FacesMessage.SEVERITY_WARN);
     }
     
     /**
      * Adiciona um aviso no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarWarnGrowl(String texto, Object... params) {
          registrarFacesMessageGrowl(obterTexto(texto, params), FacesMessage.SEVERITY_WARN);
     }

     /**
      * Adiciona um aviso no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarWarn(String texto, Object... params) {
          registrarFacesMessage(obterTexto(texto, params), FacesMessage.SEVERITY_WARN);
     }
     
     /**
      * Adiciona um sucesso no contexto do faces.
      * 
      * @param texto
      * .
      * @return 
      */
     public static void adicionarSucesso(String texto) {
          registrarFacesMessage(obterTexto(texto), FacesMessage.SEVERITY_FATAL);
     }
     
     /**
      * Adiciona um sucesso no contexto do faces.
      * 
      * @param texto
      * .
      * @return 
      */
     public static void adicionarSucessoGrowl(String texto) {
          registrarFacesMessageGrowl(obterTexto(texto), FacesMessage.SEVERITY_FATAL);
     }
     
     /**
      * Adiciona um sucesso no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarSucesso(String texto, Object... params) {
          registrarFacesMessage(obterTexto(texto, params), FacesMessage.SEVERITY_FATAL);
     }
     
     /**
      * Adiciona um sucesso no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarSucessoGrowl(String texto, Object... params) {
          registrarFacesMessageGrowl(obterTexto(texto, params), FacesMessage.SEVERITY_FATAL);
     }
     
     /**
      * Adiciona um erro no contexto do faces.
      * 
      * @param texto
      * .
      */
     public static void adicionarErro(String texto) {
          registrarFacesMessage(obterTexto(texto), FacesMessage.SEVERITY_ERROR);
     }

     /**
      * Adiciona um erro no contexto do faces.
      * 
      * @param texto
      * .
      */
     public static void adicionarErroGrowl(String texto) {
          registrarFacesMessageGrowl(obterTexto(texto), FacesMessage.SEVERITY_ERROR);
     }
     
     /**
      * Adiciona um erro no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarErro(String texto, Object... params) {
          registrarFacesMessage(obterTexto(texto, params), FacesMessage.SEVERITY_ERROR);
     }

     /**
      * Adiciona um erro no contexto do faces, formatado com par�metros.
      * 
      * @param texto
      * - nome da chave.
      * @param params
      * .
      */
     public static void adicionarErroGrowl(String texto, Object... params) {
          registrarFacesMessageGrowl(obterTexto(texto, params), FacesMessage.SEVERITY_ERROR);
     }
     
     /**
      * Mantem as mensagens armazenadas no escopo de flash.
      */
     public static void clearMensagensNoFlash() {
          obterContexto().getExternalContext().getFlash().clear();
     }

     /**
      * Mantem as mensagens armazenadas no escopo de flash.
      */
     public static void manterMensagensNoFlash() {
          obterContexto().getExternalContext().getFlash().setKeepMessages(true);
     }

     /**
      * Obt�m o texto do resource a partir da chave.
      * 
      * @param chave
      * .
      * @param resource
      * .
      * @return texto.
      */
     public static String obterTexto(String chave) {
          ResourceBundle bundle = obterResourceBundle();

          try {
               
               String result = bundle.getString(chave);
               if(Objeto.isBlank(result)){
                    result = chave;
               }
               return result;

          } catch (Exception e) {
               return chave;
          }
     }

     /**
      * Obtem uma string a partir da chave informada, formatada com os argumentos.
      * 
      * @param chave
      * .
      * @param args
      * .
      * @return string formatada.
      */
     public static String obterTexto(String chave, Object... params) {

          String texto = obterTexto(chave);

          if (Objeto.isBlank(texto)) {
               return chave;
          }

          return MessageFormat.format(texto, params);
     }

     /**
      * Obtem o navigation handler.
      * 
      * @return navigationHandler.
      */
     public static NavigationHandler obterNavigationHandler() {
          return obterContexto().getApplication().getNavigationHandler();
     }

     /**
      * Obt�m o resource bundle configurado no faces.
      * 
      * @return resourceBundle.
      */
     public static ResourceBundle obterResourceBundle() {
          CoreConfig coreConfig = (CoreConfig) BeanManager.obterBean("coreConfig");
          return ResourceBundle.getBundle(coreConfig.obterPropriedade("caminho.arquivo.mensagens"));
     }
     
     /**
      * Adiciona uma mensagem no contexto do faces com texto descritivo e severidade.
      * 
      * @param texto
      * @param severidade
      */
     public static void registrarFacesMessageGrowl(String texto, FacesMessage.Severity severidade) {
          
          FacesMessage mensagem = new FacesMessage();
          
          mensagem.setSummary(null);
          mensagem.setDetail(texto);
          mensagem.setSeverity(severidade);
          
          obterContexto().addMessage("growlGlobal", mensagem);
          
     }

     /**
      * Adiciona uma mensagem no contexto do faces com texto descritivo e severidade.
      * 
      * @param texto
      * @param severidade
      */
     public static void registrarFacesMessage(String texto, FacesMessage.Severity severidade) {

          FacesMessage mensagem = new FacesMessage();

          mensagem.setSummary(null);
          mensagem.setDetail(texto);
          mensagem.setSeverity(severidade);

          obterContexto().addMessage("messagesGlobal", mensagem);
          
     }

     /**
      * Obt�m o contexto do faces.
      * 
      * @return contexto.
      */
     public static FacesContext obterContexto() {
          return FacesContext.getCurrentInstance();
     }

     /**
      * Procura no raiz de objetos do Faces o componente da view
      * 
      * @param id
      * @return
      */
     public static UIComponent findComponentInRoot(String id) {

          UIComponent component = null;

          FacesContext facesContext = FacesContext.getCurrentInstance();
          if (Objeto.notBlank(facesContext)) {
               UIComponent root = facesContext.getViewRoot();
               component = findComponent(root, id);
          }

          return component;
     }

     /**
      * Procura no Facelets um determinado componente da view
      * 
      * @param base
      * @param id
      * @return
      */
     public static UIComponent findComponent(UIComponent base, String id) {

          if (id.equals(base.getId())) {
               return base;
          }

          UIComponent kid = null;
          UIComponent result = null;
          Iterator kids = base.getFacetsAndChildren();

          while (kids.hasNext() && (result == null)) {
               kid = (UIComponent) kids.next();
               if (id.equals(kid.getId())) {
                    result = kid;
                    break;
               }
               result = findComponent(kid, id);
               if (result != null) {
                    break;
               }
          }

          return result;
     }
     
}