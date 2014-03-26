
package br.com.cofagra.bi.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.primefaces.model.chart.MeterGaugeChartModel;

import com.google.common.base.Splitter;

import br.com.cofagra.bi.util.Constantes;
import br.com.twsoftware.alfred.colecoes.Colecoes;
import br.com.twsoftware.alfred.object.Objeto;

@Entity
@Table(name = "T_METRICAS")
@SequenceGenerator(name = "GEN_METRICAS_ID", sequenceName = "SEQ_METRICAS_ID", allocationSize = 1)
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class Metrica implements GenericEntity<Integer>, Serializable{

     private static final long serialVersionUID = 6601582110804049841L;

     public Metrica(){

          setPainel(Constantes.PAINEL_I);
          setTipo(Constantes.GRAFICO_TIPO_VELOCIMETRO);
     }

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_METRICAS_ID")
     @Column(name = "ID")
     @Getter
     @Setter
     private Integer id;

     @Getter
     @Setter
     private String titulo;

     @Getter
     @Setter
     private String query;

     @Getter
     @Setter
     private String painel;

     @Getter
     @Setter
     private String tipo;

     @Getter
     @Setter
     private String estagios;

     @Getter
     @Setter
     private String cores;

     @Getter
     @Setter
     private String label;

     @Getter
     @Setter
     private String style;

     @Getter
     @Setter
     private String styleClass;

     @Getter
     @Setter
     private Boolean showTickLabels;

     @Getter
     @Setter
     private Integer labelHeightAdjust;

     @Getter
     @Setter
     private Integer intervalOuterRadius;

     @Getter
     @Setter
     private Double min;

     @Getter
     @Setter
     private Double max;

     @Transient
     @Setter
     @Getter
     private MeterGaugeChartModel model;

     public MeterGaugeChartModel criarModel(Number result) {

          if (Objeto.notBlank(getEstagios())) {

               Iterable<String> estagios = Splitter.on(";").omitEmptyStrings().split(getEstagios());
               if (Objeto.notBlank(estagios)) {

                    List<Number> intervals = new ArrayList<Number>();
                    for (String e : estagios) {
                         intervals.add(new BigDecimal(e));
                    }
//                    if(Objeto.notBlank(intervals) && !intervals.contains(new BigDecimal(result.toString()))){
//                         intervals.add(new BigDecimal(result.toString()));
//                    }
//                    
//                    Collections.sort(intervals, new Comparator<Number>(){
//
//                         @Override
//                         public int compare(Number o1, Number o2) {
//                              return new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString()));
//                         }
//                         
//                    });
                    return new MeterGaugeChartModel(result, intervals);
               }
          }
          return null;
     }

     @Override
     public Map<String, Object> getParametros() {

          return null;
     }

}
