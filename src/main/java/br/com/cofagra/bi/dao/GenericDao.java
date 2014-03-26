
package br.com.cofagra.bi.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.cofagra.bi.util.SimpleQuery;

public interface GenericDao<T> {

     T adicionar(T t);

     void remover(T t);

     T alterar(T t);

     List<T> busca(SimpleQuery simpleQuery);

     T obter(Serializable chave);

     List<T> listar(String... orderBy);

     T listarUnico(String jpql, Map<String, Object> parametros, String clausulaWhere);

     List<T> listar(String jpql, Map<String, Object> parametros);

     Long countPorNamedQuery(String namedQueryPesquisa, Map<String, Object> parametros);

     Long countPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros);

     List<T> listarPorNamedQuery(String nomeQuery, Object... parametros);

     List<T> listarPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros);

     List<T> listarPorNamedQuery(String namedQuery, String clausulaWhere, int posInicial, int posFinal, Map<String, Object> parametros);

     T listarUnicoPorNamedQuery(String namedQuery, Map<String, Object> parametros, String clausulaWhere);

     EntityManager getEntityManager();
     
     void executeProcedure(String nomeProcedure, List<Object> parametros);

     Session getCurrentSession();

     Object getValorAttributo(Object objeto, String coluna);

     T last();

     T first();
     
     List<T> obter(Integer... ids);

     List<T> limit(Integer limit);

     boolean exist(Integer id);

     boolean exist(Integer... id);

     boolean exist();

     Long count();

}
