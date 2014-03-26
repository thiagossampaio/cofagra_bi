package br.com.cofagra.bi.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleQuery {

     private String query;

     private List<String> clausulas;

     private Map<String, Object> parameters;

     public SimpleQuery() {
          this.clausulas = new LinkedList<String>();
          this.parameters = new HashMap<String, Object>();
     }

     public SimpleQuery(String query) {
          this.query = query;
          this.clausulas = new LinkedList<String>();
          this.parameters = new HashMap<String, Object>();
     }

     public void addClausula(String clausula) {
          this.clausulas.add(clausula);
     }

     public void putParameter(String key, Object value) {
          this.parameters.put(key, value);
     }

     public String getQuery() {
          return query;
     }

     public void setQuery(String query) {
          this.query = query;
     }

     public List<String> getClausulas() {
          return clausulas;
     }

     public void setClausulas(List<String> clausulas) {
          this.clausulas = clausulas;
     }

     public Map<String, Object> getParameters() {
          return parameters;
     }

     public void setParameters(Map<String, Object> parameters) {
          this.parameters = parameters;
     }

}
