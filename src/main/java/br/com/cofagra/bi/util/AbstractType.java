
package br.com.cofagra.bi.util;

import java.lang.reflect.ParameterizedType;

/**
 * Classe que retorna o Type do gen�rico de uma determinada class.
 * Para isso basta extende-la e depois chamar o m�todo
 * 
 * @author thiago.sampaio
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class AbstractType<T> {

     public Class genericTypeClass() {

          ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
          return (Class) parameterizedType.getActualTypeArguments()[0];
     }

}
