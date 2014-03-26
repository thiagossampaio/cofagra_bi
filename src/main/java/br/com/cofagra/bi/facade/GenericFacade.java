package br.com.cofagra.bi.facade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.cofagra.bi.util.SimpleQuery;

/**
 * Interface para todos os controller que desejam implementar um CRUD gen�rico
 * 
 * @author thiago.sampaio
 * 
 * @param <K>
 * @param <T>
 */
public interface GenericFacade<T> {
     
     Class<?> genericTypeClass();
	
     T adicionar(T t);

     void remover(T t);
     
     void deletarFisicamente(T t);

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
     
     void executeProcedure(String nomeProcedure, List<Object> parametros);

     T last();

     T first();

     List<T> obter(Integer... ids);

     List<T> limit(Integer limit);

     Long count();

     boolean exist(Integer id);

     boolean exist(Integer... ids);

     boolean exist();
}
