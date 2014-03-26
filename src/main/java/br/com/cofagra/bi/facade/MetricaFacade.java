package br.com.cofagra.bi.facade;

import java.math.BigDecimal;

import br.com.cofagra.bi.entidades.Metrica;


public interface MetricaFacade extends GenericFacade<Metrica> {

     Number consultarMetrica(Metrica m);

}
