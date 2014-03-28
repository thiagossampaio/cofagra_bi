
package br.com.cofagra.bi.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGroup;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.component.chart.metergauge.MeterGaugeChart;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.cofagra.bi.entidades.Metrica;
import br.com.cofagra.bi.facade.MetricaFacade;
import br.com.cofagra.bi.util.FacesUtil;
import br.com.twsoftware.alfred.object.Objeto;

@Controller
@Scope("session")
public class Painel01Controlador implements Serializable{

     private static final long serialVersionUID = -7558111491015996987L;

     @Getter
     @Setter
     private Integer sliderNumber;
     
     @Getter
     @Setter
     public HtmlPanelGroup panelGroupMeters;     

     @Autowired
     private MetricaFacade metricaFacade;
     
     public Painel01Controlador(){
          panelGroupMeters = (HtmlPanelGroup) FacesUtil.getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
          panelGroupMeters.setId(criarId());
          sliderNumber = 60;
     }

     @PostConstruct
     public void init() {
     }
     
     public void onSlideEnd(SlideEndEvent event){
          panelGroupMeters.clearInitialState();
          panelGroupMeters.getChildren().clear();
          setSliderNumber(event.getValue());
     }
     
     public String criarId(){
          return FacesUtil.getContexto().getViewRoot().createUniqueId();
     }

     public void gerarMeters() {

          panelGroupMeters.getChildren().clear();
          
          List<Metrica> metricas = metricaFacade.listar("titulo ASC");
          if (Objeto.notBlank(metricas)) {
               for (Metrica metrica : metricas) {
                    if (Objeto.notBlank(metrica)) {

                         MeterGaugeChart m = (MeterGaugeChart) FacesUtil.getApplication().createComponent(MeterGaugeChart.COMPONENT_TYPE);
                         
                         m.setId(criarId());
                         
                         if (Objeto.notBlank(metrica.getIntervalOuterRadius()) && metrica.getIntervalOuterRadius() != 0) {
                              m.setIntervalOuterRadius(metrica.getIntervalOuterRadius());
                         }

                         if (Objeto.notBlank(metrica.getLabel())) {
                              m.setLabel(metrica.getLabel());
                         }

                         if (Objeto.notBlank(metrica.getLabelHeightAdjust()) && metrica.getLabelHeightAdjust() != 0) {
                              m.setLabelHeightAdjust(metrica.getLabelHeightAdjust());
                         }

                         if (Objeto.notBlank(metrica.getMax()) && metrica.getMax() != 0.0) {
                              m.setMax(metrica.getMax());
                         }

                         if (Objeto.notBlank(metrica.getMin()) && metrica.getMin() != 0.0) {
                              m.setMin(metrica.getMin());
                         }

                         if (Objeto.notBlank(metrica.getCores())) {
                              m.setSeriesColors(metrica.getCores());
                         }

                         if (Objeto.notBlank(metrica.getStyle())) {
                              m.setStyle(metrica.getStyle());
                         }

                         if (Objeto.notBlank(metrica.getStyleClass())) {
                              m.setStyleClass(metrica.getStyleClass());
                         }

                         m.setShowTickLabels(metrica.isShowTickLabels());

                         if (Objeto.notBlank(metrica.getTitulo())) {
                              m.setTitle(metrica.getTitulo());
                         }
                         
                         Number result = metricaFacade.consultarMetrica(metrica);
                         if (Objeto.notBlank(result)) {
                              MeterGaugeChartModel model = metrica.criarModel(result);
                              if (Objeto.notBlank(model)) {
                                   m.setValue(model);
                                   
                                   HtmlPanelGroup p = (HtmlPanelGroup) FacesUtil.getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
                                   p.setId(criarId());
                                   p.setLayout("block");
                                   p.setStyleClass("col-sm-3");
                                   p.getChildren().add(m);
                                   
                                   panelGroupMeters.getChildren().add(p);
                              }
                         }

                    }
               }
               
          }

     }
}