package br.com.cofagra.bi.util;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;

import org.springframework.stereotype.Service;

/**
 * Bean de configura��o geral para o Core.
 */
@Service
@ApplicationScoped
public class CoreConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Properties coreProperties;

     @PostConstruct
     public void init() {
          coreProperties = new PropertiesUtil().obterProperties("META-INF/core.properties");
     }

     /**
      * Obtem o valor de uma propriedade
      * 
      * @param nomePropriedade
      * 
      * @return conte�do da propriedade.
      */
     public String obterPropriedade(String nomePropriedade) {
          return coreProperties.getProperty(nomePropriedade);
     }

}
