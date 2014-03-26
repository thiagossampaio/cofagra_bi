
package br.com.cofagra.bi.facade;

/**
 * Interface para todos os controller que desejam implementar um CRUD gen�rico
 * 
 * @author thiago.sampaio
 * 
 */
public interface CrudFacade<T> {

     /**
      * Este m�todo dever� retornar um instancia da fachada gen�rica <code>br.com.neus.kernel.facade.GenericFacade<T></code>
      * @return
      */
     public GenericFacade<T> getGenericFacade();
}
