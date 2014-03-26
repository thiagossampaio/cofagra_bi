
package br.com.cofagra.bi.util;

import lombok.Getter;

public class Condicao{

     @Getter
     private Boolean condicao;

     @Getter
     private int tipoCondicaoEnum;

     @Getter
     private String msg;

     public static final int ERRO = 0;

     public static final int INFO = 1;

     public static final int WARN = 2;

     public static final int SUCESSO = 3;

     /**
      * 
      * @param condicao ao qual será validado caso negativo
      * @param msg que será exibido. Pode ser uma chave do properties ou apenas um texto qualquer
      * @param tipoCondicao da msg ex: ERRO, INFO, WARN E SUCESSO. Todos constantes dessa mesma classe.
      */
     public Condicao(Boolean condicao, String msg, int tipoCondicao){

          this.tipoCondicaoEnum = tipoCondicao;
          this.condicao = condicao;
          this.msg = msg;

     }

}
