
package br.com.cofagra.bi.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.Getter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.cofagra.bi.entidades.GenericEntity;
import br.com.cofagra.bi.util.SimpleQuery;
import br.com.twsoftware.alfred.object.Objeto;
import br.com.twsoftware.alfred.reflexao.Reflexao;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class GenericDaoJpa<T extends GenericEntity<? extends Serializable>> implements GenericDao<T>, Serializable{

     private static final long serialVersionUID = -8725186123628019702L;

     private Class<T> classe;
     
     @Getter
     @PersistenceContext
     private EntityManager entityManager;   
     
     @Autowired
     private SessionFactory sessionFactory;

     public GenericDaoJpa(Class<T> classe){

          this.classe = classe;

     }

     /**
      * Persiste um determinado objeto verificando antes se ele esta duplicado. Essa duplica��o � de acordo com uma namedQuery existente na Entidade com o nome 'verificaDuplicata'
      */
     @Override
     @Transactional
     public T adicionar(T t) {

          this.entityManager.persist(t);
          return t;
     }

     /**
      * Altera um determinado objeto verificando antes se ele esta duplicado. Essa duplica��o � de acordo com uma namedQuery existente na Entidade com o nome 'verificaDuplicata'
      */
     @Override
     @Transactional
     public T alterar(T t) {

          this.entityManager.merge(t);
          return t;
     }

     /**
      * Remove um determinado objeto do banco
      */
     @Override
     @Transactional
     public void remover(T t) {

          t = this.entityManager.find(classe, t.getId());
          this.entityManager.remove(t);
     }

     /**
      * Lista todos os objetos de uma determinada entidade ordenando pelos campos passados por par�metros
      */
     @SuppressWarnings("unchecked")
     @Override
     public List<T> listar(String... orderBy) {

          StringBuilder orderByClause = new StringBuilder();

          if (orderBy != null && orderBy.length > 0) {
               orderByClause.append(" ORDER BY ");
               for (int i = 0; i < orderBy.length; i++) {
                    if (i > 0) {
                         orderByClause.append(", ");
                    }

                    orderByClause.append("o." + orderBy[i]);
               }
          }

          return this.entityManager.createQuery("SELECT o FROM " + classe.getSimpleName() + " o" + orderByClause.toString()).getResultList();
     }

     /**
      * Retorna um determinado objeto utilizando a Primare Key que foi passada como par�metro
      */
     @Override
     public T obter(Serializable chave) {

          return this.entityManager.find(classe, chave);
     }

     /**
      * Lista todos os objetos de uma determinada entidade de acordo com os parametro da consulta
      */
     @SuppressWarnings("unchecked")
     @Override
     public List<T> listar(String jpql, Map<String, Object> parametros) {

          Query query = null;
          query = getEntityManager().createQuery(jpql);

          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          } else {

               query = getEntityManager().createQuery(jpql);

          }

          List<T> itens = query.getResultList();
          return itens;

     }

     @Override
     @SuppressWarnings("unchecked")
     public T listarUnico(String jpql, Map<String, Object> parametros, String clausulaWhere) {

          Query query = null;
          query = getEntityManager().createQuery(jpql);

          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          } else {

               query = getEntityManager().createQuery(jpql);

          }

          return (T) query.getSingleResult();

     }

     @Override
     public Long countPorNamedQuery(String jpql, Map<String, Object> parametros) {

          Query query = null;
          query = getEntityManager().createQuery(jpql);

          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          } else {

               query = getEntityManager().createQuery(jpql);

          }

          return (Long) query.getSingleResult();

     }

     @Override
     @SuppressWarnings("unchecked")
     public List<T> busca(SimpleQuery sq) {

          return createQuery(sq).getResultList();
     }

     @Override
     @SuppressWarnings("unchecked")
     public List<T> listarPorNamedQuery(String nomeQuery, Object... parametros) {

          String queryString = getNamedQuery(nomeQuery, classe);
          Query query = this.entityManager.createQuery(queryString);
          
          if(Objeto.notBlank(parametros)){
               for (int i = 0; i < parametros.length; i++) {
                    query.setParameter(i+1, parametros[i]);
               }
               
          }
          return query.getResultList();
     }

     /**
      * Lista os objetos utilizando de uma named query existente na Entidade, uma clausula where e seus par�metros
      */
     @Override
     @SuppressWarnings("unchecked")
     public List<T> listarPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros) {

          String queryString = getNamedQuery(namedQuery, classe);

          Query query = null;
          if (Objeto.notBlank(clausulaWhere)) {

               if (queryString.matches(".+\\s+order.+")) {
                    String querySelect = queryString.replaceAll("\\s+order.+", "");
                    queryString = querySelect + " where " + clausulaWhere + queryString.replace(querySelect, "");
               } else {
                    queryString = queryString + " where " + clausulaWhere;
               }

          }

          query = getEntityManager().createQuery(queryString);

          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          }
          List<T> itens = query.getResultList();
          return itens;
     }

     /**
      * Retorna a quantidade de objetos utilizando de uma named query existente na Entidade, uma clausula where e seus par�metros
      */
     @Override
     public Long countPorNamedQuery(String namedQuery, String clausulaWhere, Map<String, Object> parametros) {

          String queryString = getNamedQuery(namedQuery, classe);

          Query query = null;
          if (Objeto.notBlank(clausulaWhere)) {

               if (queryString.matches(".+\\s+order.+")) {
                    String querySelect = queryString.replaceAll("\\s+order.+", "");
                    queryString = querySelect + " where " + clausulaWhere + queryString.replace(querySelect, "");
               } else {
                    queryString = queryString + " where " + clausulaWhere;
               }

          }

          query = getEntityManager().createQuery(queryString);

          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          }
          return (Long) query.getSingleResult();
     }

     @Override
     @SuppressWarnings("unchecked")
     public T listarUnicoPorNamedQuery(String namedQuery, Map<String, Object> parametros, String clausulaWhere) {

          String queryString = getNamedQuery(namedQuery, classe);

          Query query = null;
          if (clausulaWhere != null) {

               if (queryString.matches(".+\\s+order.+")) {
                    String querySelect = queryString.replaceAll("\\s+order.+", "");
                    queryString = querySelect + " where " + clausulaWhere + queryString.replace(querySelect, "");
               } else {
                    queryString = queryString + " where " + clausulaWhere;
               }

          }

          query = getEntityManager().createQuery(queryString);
          if (Objeto.notBlank(parametros)) {

               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }

          } else {

               if (clausulaWhere == null) {
                    query = getEntityManager().createQuery(queryString);
               }

          }

          return (T) query.getSingleResult();
     }

     /**
      * Lista os objetos utilizando de uma named query existente na Entidade, uma clausula where e seus par�metros pagianando de acordo com os par�metros inicial e final.
      */
     @Override
     @SuppressWarnings("unchecked")
     public List<T> listarPorNamedQuery(String namedQuery, String clausulaWhere, int posInicial, int posFinal, Map<String, Object> parametros) {

          String queryString = getNamedQuery(namedQuery, classe);

          Query query = null;
          if (clausulaWhere != null) {

               if (queryString.matches(".+\\s+order.+")) {
                    String querySelect = queryString.replaceAll("\\s+order.+", "");
                    queryString = querySelect + " where " + clausulaWhere + queryString.replace(querySelect, "");
               } else {
                    queryString = queryString + " where " + clausulaWhere;
               }

          }

          query = getEntityManager().createQuery(queryString);

          if (parametros != null && parametros.size() > 0) {
               for (String chave : parametros.keySet()) {
                    query = query.setParameter(chave, parametros.get(chave));
               }
          }

          query.setFirstResult(posInicial).setMaxResults(posFinal);
          List<T> itens = query.getResultList();
          return itens;
     }

     /**
      * Gerar um Query de acordo com o Objeto SimpleQuery que foi passado
      * 
      * @param SimpleQuery
      * @return
      */
     private Query createQuery(SimpleQuery simpleQuery) {

          String queryStr = simpleQuery.getQuery();

          if (!simpleQuery.getClausulas().isEmpty()) {
               queryStr += " where " + Joiner.on(" and ").skipNulls().join(simpleQuery.getClausulas());
          }
          
          Query query = getEntityManager().createQuery(queryStr);

          Set<Map.Entry<String, Object>> set = simpleQuery.getParameters().entrySet();

          for (Map.Entry<String, Object> entry : set) {
               query.setParameter(entry.getKey(), entry.getValue());
          }
          return query;
     }

     /**
      * 
      * Retornar o campo que esteja representando a Primare Key de uma determinada Entidade que foi passada como par�metro
      * 
      * @param classe
      * @param t
      * @return
      */
     private Object verificaId(Class<? super T> classe, T t) {

          if (classe != Object.class) {
               Field[] campos = classe.getDeclaredFields();
               for (Field field : campos) {
                    if (field.isAnnotationPresent(Id.class)) {
                         field.setAccessible(true);
                         try {
                              return field.get(t);
                         } catch (Exception e) {
                              e.printStackTrace();
                         }
                    }
               }
          }
          if (classe.getSuperclass() != Object.class) {
               return verificaId(classe.getSuperclass(), t);
          }
          return null;
     }

     /**
      * 
      * Retorna uma determinada namedQuery que existir em uma Classe
      * 
      * @param namedQuery
      * @param class1
      * @return
      */
     @SuppressWarnings("unchecked")
     private String getNamedQuery(String namedQuery, Class class1) {

          try {
               NamedQueries namedQueries = (NamedQueries) class1.getAnnotation(NamedQueries.class);
               NamedQuery[] naQueries = namedQueries.value();
               String queryString = null;
               for (NamedQuery namedQuery2 : naQueries) {
                    if (namedQuery2.name().equals(namedQuery)) {
                         queryString = namedQuery2.query();
                         break;
                    }
               }
               return queryString;
          } catch (Exception e) {
               return null;
          }
     }

     /**
      * 
      * Busca um determinado campo de uma Classe por Relfection
      * 
      * @param classe
      * @param t
      * @param nomeCampo
      * @return
      */
     private Object buscaCampo(Class<? super T> classe, T t, String nomeCampo) {

          if (classe != Object.class) {
               Field[] campos = classe.getDeclaredFields();
               for (Field field : campos) {
                    if (field.getName().equals(nomeCampo)) {
                         field.setAccessible(true);
                         try {
                              return field.get(t);
                         } catch (Exception e) {
                              e.printStackTrace();
                         }
                    }
               }
          }
          if (classe.getSuperclass() != Object.class) {
               return buscaCampo(classe.getSuperclass(), t, nomeCampo);
          }
          return null;
     }
     
     /**
      * Retorna a sess�o atual do Hibernate
      */
     public Session getCurrentSession(){
          return getEntityManager().unwrap(Session.class);
     }
     
     public Object getValorAttributo(Object objeto, String coluna) {

          if (coluna.contains(".")) {

               for (String c : Splitter.on('.').omitEmptyStrings().trimResults().split(coluna)) {

                    if (Reflexao.existeGet(objeto, c)) {
                         objeto = Reflexao.getValorDoAtributo(objeto, c);
                    } else {
                         return null;
                    }

               }

               return objeto;

          } else {

               if (Reflexao.existeGet(objeto, coluna)) {
                    return Reflexao.getValorDoAtributo(objeto, coluna);
               }

          }

          return null;

     }
     
     /**
      * M�todo para executar procedures usando o JPA
      * @param nomeProcedure
      * @param parametros
      * @throws SQLException
      */
     @Transactional
     public void executeProcedure(String nomeProcedure, List<Object> parametros){

          EntityManager em = getEntityManager();
          String parametrosStr = "";
          if (Objeto.notBlank(parametros)) {
               for (int i = 0; i < parametros.size(); i++) {
                    parametrosStr += "?";
                    if (i < parametros.size() - 1) {
                         parametrosStr += ",";
                    }
               }
          }
          Query query = em.createNativeQuery("BEGIN " + nomeProcedure + "(" + parametrosStr + "); END;");
          if (parametros != null && parametros.size() > 0) {
               for (int i = 0; i < parametros.size(); i++) {
                    query = query.setParameter(i + 1, parametros.get(i));
               }
          }
          
          query.executeUpdate();
     }

     /**
      * Metodos para serem refatorados
      */
     @SuppressWarnings("unchecked")
     public T last() {
          
          Query query = getEntityManager().createQuery("select x from " + classe.getSimpleName() + " x order by id desc ");
          return (T) query.setMaxResults(1).getSingleResult();
     }    
     
     @SuppressWarnings("unchecked")
     public T first() {
          
          Query query = getEntityManager().createQuery("select x from " + classe.getSimpleName() + " x order by id asc ");
          return (T) query.setMaxResults(1).getSingleResult();
     }    
     
     @SuppressWarnings("unchecked")
     public List<T> obter(Integer... ids) {
          
          Query query = getEntityManager().createQuery("select x from " + classe.getSimpleName() + " x where x.id IN (:ids) ");
          query.setParameter("ids", Lists.newArrayList(ids));
          return (List<T>) query.getResultList();
     }    
     
     @SuppressWarnings("unchecked")
     public List<T> limit(Integer limit) {

          Query query = getEntityManager().createQuery("select x from " + classe.getSimpleName() + " x ");
          return (List<T>) query.setMaxResults(limit).getResultList();
     }    
     
     public Long count(){
          
          Query query = getEntityManager().createQuery("select count(x) from " + classe.getSimpleName() + " x ");
          return (Long) query.getSingleResult();
     }
     
     public boolean exist(Integer id){
          
          return Objeto.notBlank(obter(id));
     }
     
     public boolean exist(Integer... id){
          
          return Objeto.notBlank(obter(id));
     }
     
     public boolean exist(){
          
          return count() > 0;
     }

}
