
package br.com.cofagra.bi.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.hibernate.annotations.Type;
import org.primefaces.model.chart.MeterGaugeChartModel;

import br.com.cofagra.bi.util.Constantes;
import br.com.twsoftware.alfred.object.Objeto;

import com.google.common.base.Splitter;

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
     @Column(name = "TITULO")
     private String titulo;

     @Getter
     @Setter
     @Column(name = "QUERY")
     private String query;

     @Getter
     @Setter
     @Column(name = "PAINEL")
     private String painel;

     @Getter
     @Setter
     @Column(name = "TIPO")
     private String tipo;

     @Getter
     @Setter
     @Column(name = "ESTAGIOS")
     private String estagios;

     @Getter
     @Setter
     @Column(name = "CORES")
     private String cores;

     @Getter
     @Setter
     @Column(name = "LABEL")
     private String label;

     @Getter
     @Setter
     @Column(name = "STYLE")
     private String style;

     @Getter
     @Setter
     @Column(name = "STYLECLASS")
     private String styleClass;

     @Getter
     @Setter
     @Type(type="yes_no")     
     @Column(name = "SHOWTICKLABELS", length = 1)
     private boolean showTickLabels;

     @Getter
     @Setter
     @Column(name = "LABELHEIGHTADJUST")
     private Integer labelHeightAdjust;

     @Getter
     @Setter
     @Column(name = "INTERVALOUTERRADIUS")
     private Integer intervalOuterRadius;

     @Getter
     @Setter
     @Column(name = "MIN")
     private Double min;

     @Getter
     @Setter
     @Column(name = "MAX")
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
