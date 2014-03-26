package br.com.cofagra.bi.entidades;

import java.io.Serializable;
import java.util.Map;

public interface GenericEntity<K extends Serializable> {
     
     K getId();

     public abstract Map<String, Object> getParametros();

     StringBuffer clausulaWhere = new StringBuffer("");
}
