
package br.com.cofagra.bi.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.cofagra.bi.dao.GenericDao;
import br.com.cofagra.bi.util.AbstractType;
import br.com.cofagra.bi.util.BeanManager;
import br.com.cofagra.bi.util.SimpleQuery;

@SuppressWarnings("unchecked")
public abstract class GenericFacadeImpl<E> extends AbstractType<E> implements GenericFacade<E>{

     public GenericDao<E> getGenericDao() {

          String genericDaoName = "dao" + this.genericTypeClass().getSimpleName();
          return (GenericDao<E>) BeanManager.obterBean(genericDaoName);
     }

     @Override
     public E adicionar(E t) {

          return getGenericDao().adicionar(t);
     }

     /**
      * Remove apenas logicamente o objeto do banco
      */
     @Override
     public void remover(E t) {

          getGenericDao().alterar(t);
     }

     /**
      * Remover fisicamente do banco o objeto t
      * 
      * @param t
      */
     @Override
     public void deletarFisicamente(E t) {

          getGenericDao().remover(t);
     }

     @Override
     public E alterar(E t) {

          return getGenericDao().alterar(t);
     }

     @Override
     public List<E> busca(SimpleQuery simpleQuery) {

          return getGenericDao().busca(simpleQuery);
     }

     @Override
     public E obter(Serializable chave) {

          return getGenericDao().obter(chave);
     }

     @Override
     public List<E> listar(String... orderBy) {

          return getGenericDao().listar(orderBy);
     }

     @Override
     public E listarUnico(String jpql, Map<String, Object> parametros, String clausulaWhere) {

          return getGenericDao().listarUnico(jpql, parametros, clausulaWhere);
     }

     @Override
     public List<E> listar(String jpql, Map<String, Object> parametros) {

          return getGenericDao().listar(jpql, parametros);
     }

     @Override
     public Long countPorNamedQuery(String namedQueryPesquisa, Map<String, Object> parametros) {

          return getGenericDao().countPorNamedQuery(namedQueryPesquisa, parametros);
     }

     @Override
     public Long countPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros) {

          return getGenericDao().countPorNamedQuery(namedQuery, clausulaWhere, parametros);
     }

     @Override
     public List<E> listarPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros) {

          return getGenericDao().listarPorNamedQuery(namedQuery, clausulaWhere, parametros);
     }

     @Override
     public List<E> listarPorNamedQuery(String namedQuery, String clausulaWhere, int posInicial, int posFinal, Map<String, Object> parametros) {

          return getGenericDao().listarPorNamedQuery(namedQuery, clausulaWhere, posInicial, posFinal, parametros);
     }

     @Override
     public E listarUnicoPorNamedQuery(String namedQuery, Map<String, Object> parametros, String clausulaWhere) {

          return getGenericDao().listarUnicoPorNamedQuery(namedQuery, parametros, clausulaWhere);
     }

     @Override
     public List<E> listarPorNamedQuery(String nomeQuery, Object... parametros) {

          return getGenericDao().listarPorNamedQuery(nomeQuery, parametros);
     }

     public void executeProcedure(String nomeProcedure, List<Object> parametros) {

          getGenericDao().executeProcedure(nomeProcedure, parametros);
     }

     public E last() {

          return getGenericDao().last();
     }

     public E first() {

          return getGenericDao().first();
     }

     public List<E> obter(Integer... ids) {

          return getGenericDao().obter(ids);
     }

     public List<E> limit(Integer limit) {

          return getGenericDao().limit(limit);
     }

     public Long count() {

          return getGenericDao().count();
     }

     public boolean exist(Integer id) {

          return getGenericDao().exist(id);
     }

     public boolean exist(Integer... ids) {

          return getGenericDao().exist(ids);
     }

     public boolean exist() {

          return getGenericDao().exist();
     }

}