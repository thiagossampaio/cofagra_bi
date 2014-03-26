package br.com.cofagra.bi.util;

import lombok.Getter;
import lombok.extern.java.Log;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * F�brica de suporte para acessar os beans gerenciados pelo Spring.
 * 
 * Objetos que estejam fora do contexto do Spring, podem utilizar a f�brica para acessar os beans (do Spring) manualmente.
 */
@Scope("singleton")
@Component
@Log
public class BeanManager implements ApplicationContextAware {
     
     @Getter
     private static ApplicationContext applicationContext;

     public void setApplicationContext(ApplicationContext actx) throws BeansException {
          BeanManager.inicializar(actx);
     }

     /**
      * Inicializa a fabrica com o aplication context.
      * 
      */
     private static void inicializar(ApplicationContext actx) {
          BeanManager.applicationContext = actx;
     }
     
     /**
      * Obtem o bean gerenciado pelo spring a partir do id name.
      * 
      * @return spring bean.
      * 
      */
     public static Object obterBean(String nomeBean) {
          Object bean = null;
          try {
               bean = BeanManager.applicationContext.getBean(nomeBean);
               
          } catch (Exception be) {
               log.info("O bean: '" + nomeBean + "' , n�o foi localizado no Application Context!");
               be.printStackTrace();
          }
          return bean;
     }

     /**
      * Obtem o bean gerenciado pelo spring a partir do id name.
      * 
      * @return spring bean.
      * 
      */
     public static Object obterBean(Class<?> klass) {
          Object bean = null;
          try {
               bean = BeanManager.applicationContext.getBean(klass);

          } catch (Exception be) {
               log.info("O bean: '" + klass + "' , n�o foi localizado no Application Context!");
               be.printStackTrace();
          }
          return bean;
     }
     
     /**
      * Wrapper do getApplicationContext() apenas para faciliar no console
      * @return
      */
     public static ApplicationContext getContexto(){
          
          return getApplicationContext();
     }
}
