
package br.com.cofagra.bi.facade;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import br.com.cofagra.bi.entidades.Metrica;
import br.com.twsoftware.alfred.object.Objeto;

@SuppressWarnings("unchecked")
@Service
public class MetricaFacadeImpl extends GenericFacadeImpl<Metrica> implements MetricaFacade, Serializable{

     private static final long serialVersionUID = -3297160495315553896L;
     
     public Number validarMetrica(Metrica m){
          EntityManager em = getGenericDao().getEntityManager();
          Query query = em.createNativeQuery(m.getQuery());
          BigDecimal result = (BigDecimal) query.getSingleResult();
          return result;
     }
     
     public Number consultarMetrica(Metrica m){
          Number result = null;
          if(Objeto.notBlank(m) && Objeto.notBlank(m.getQuery())){
               EntityManager em = getGenericDao().getEntityManager();
               Query query = em.createNativeQuery(m.getQuery());
               result = (Number) query.getSingleResult();
          }
          return result;
     }
     
     
}