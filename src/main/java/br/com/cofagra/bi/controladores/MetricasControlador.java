
package br.com.cofagra.bi.controladores;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.model.chart.MeterGaugeChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.cofagra.bi.entidades.Metrica;
import br.com.cofagra.bi.facade.MetricaFacade;
import br.com.cofagra.bi.util.Condicao;
import br.com.cofagra.bi.util.FacesUtil;
import br.com.cofagra.bi.util.PrimeFacesJs;
import br.com.cofagra.bi.util.Validar;
import br.com.twsoftware.alfred.object.Objeto;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Controller
@Scope("session")
public class MetricasControlador implements Serializable{

     private static final long serialVersionUID = -2170211689217951275L;
     
     @Setter
     private List<Metrica> metricas;
     
     @Getter @Setter
     private Metrica entidade;
     
     @Getter @Setter
     private String cor;
     
     @Autowired
     private MetricaFacade metricaFacade;
     
     @PostConstruct
     public void init(){
          metricas = Lists.newArrayList();
          entidade = new Metrica();
          setCor("");
     }
     
//     public MeterGaugeChartModel createMeterGaugeModel() {  
//          
//          List<Number> intervals = new ArrayList<Number>();
//          intervals.add(20);
//          intervals.add(50);
//          intervals.add(120);
//          intervals.add(200);
//          
//          return new MeterGaugeChartModel(140, intervals);  
//      }      
     
     public boolean validar(boolean apenasValidar){
          
          
          if(Validar.invalid(
                    
               new Condicao(Objeto.isBlank(getEntidade()), "Preencha os campos obrigatórios", Condicao.ERRO), 
               new Condicao(Objeto.isBlank(getEntidade().getTitulo()), "Titulo é obrigatório", Condicao.ERRO), 
               new Condicao(Objeto.isBlank(getEntidade().getQuery()), "A query é obrigatório", Condicao.ERRO), 
               new Condicao(Objeto.isBlank(getEntidade().getPainel()), "Painel é obrigatório", Condicao.ERRO), 
               new Condicao(Objeto.isBlank(getEntidade().getEstagios()), "Estágios é obrigatório", Condicao.ERRO)
               )){
               
               if(!apenasValidar){
                    PrimeFacesJs.scrollToMessages();
               }
               return false;
          }
          
          try {
               
               Number result = metricaFacade.consultarMetrica(getEntidade());
               if(Objeto.isBlank(result)){
                    
                    FacesUtil.adicionarErro("Nenhum resultado foi retornado");
                    if(!apenasValidar){
                         PrimeFacesJs.scrollToMessages();
                    }
                    return false;
                    
               }else{
                    
                    MeterGaugeChartModel model = getEntidade().criarModel(result);
                    if(Objeto.notBlank(model)){
                         getEntidade().setModel(model);
                    }
                    
                    if(!apenasValidar){
                         PrimeFacesJs.execute("dialogMetrica.show()");
                         PrimeFacesJs.update("graficoMetricaValidar");
                    }
                    
               }
               
          } catch (Exception e) {
               e.printStackTrace();
               FacesUtil.adicionarErro("Métrica inválida. Erro: " + e.getMessage());      
               if(!apenasValidar){
                    PrimeFacesJs.scrollToMessages();
               }
               return false;
          }
          
          return true;
     }
     
     public List<Metrica> getMetricas(){
          
          if(Objeto.isBlank(metricas)){
               metricas = Lists.newArrayList();
               metricas.addAll(metricaFacade.listar("titulo ASC"));
          }
          
          return metricas;
     }
     
     public void salvar(){
          if(Objeto.notBlank(getEntidade())){
               
               if(validar(true)){
                    
                    if(Objeto.isBlank(getEntidade().getId())){
                         metricaFacade.adicionar(getEntidade());
                    }else{
                         metricaFacade.alterar(getEntidade());
                    }
                    init();
                    
               }
               
          }
     }
     
     public void remover(Metrica m){
          if(Objeto.notBlank(m)){
               metricaFacade.deletarFisicamente(m);
               init();
          }
     }
     
     public void coresChangeListener(ValueChangeEvent event){
          
          if(event != null && event.getNewValue() != null && !"".equals(event.getNewValue()) && entidade != null && entidade.getCores() != null){
               getEntidade().setCores(getEntidade().getCores() + event.getNewValue() + "; ");
          }
     }
     
     public String linkCadastroMetricas(){
          
          init();
          return "metricas";
          
     }
     
}