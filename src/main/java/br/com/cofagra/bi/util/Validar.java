
package br.com.cofagra.bi.util;

import br.com.twsoftware.alfred.object.Objeto;

public class Validar{

     public static boolean invalid(Condicao condicao) {

          return check(condicao);
     }

     public static boolean invalid(Condicao... condicoes) {

          Boolean result = false;

          for (Condicao condicao : condicoes) {
               boolean invalid = check(condicao);
               if (invalid) {
                    result = invalid;
                    break;
               }
          }

          return result;
     }

     private static Boolean check(Condicao condicao) {

          Boolean result = false;

          if (Objeto.notBlank(condicao) && condicao.getCondicao()) {
               result = true;
               switch (condicao.getTipoCondicaoEnum()) {

                    case 0:
                         FacesUtil.adicionarErro(condicao.getMsg());
                         PrimeFacesJs.scrollToMessages();
                         break;
                    case 1:
                         FacesUtil.adicionarInfo(condicao.getMsg());
                         PrimeFacesJs.scrollToMessages();
                         break;
                    case 2:
                         FacesUtil.adicionarWarn(condicao.getMsg());
                         PrimeFacesJs.scrollToMessages();
                         break;
                    case 3:
                         FacesUtil.adicionarWarn(condicao.getMsg());
                         PrimeFacesJs.scrollToMessages();
                         break;
                    default:
                         break;
               }
          }

          return result;
     }
     
}
