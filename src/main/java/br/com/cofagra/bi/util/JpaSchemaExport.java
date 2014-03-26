
package br.com.cofagra.bi.util;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.java.Log;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 
 * Classe que exporta o schema do banco da aplica��o escrevendo em um arquivo
 * 
 * @author thiago.sampaio
 * 
 */
@SuppressWarnings("deprecation")
@Log
public class JpaSchemaExport{

     public static void main(String[] args) throws IOException {

          // execute(NOME_DO_PERSISTENCE_UNIT, DIRETORIO_PARA_OUTPUT, CREATE, FORMAT);

          execute(args[0], args[1], Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]));
     }

     public static void execute(String persistenceUnitName, String destination, boolean create, boolean format) {

          log.info("Exportanto o schema do banco e salvando em:" + destination);

          Ejb3Configuration cfg = new Ejb3Configuration().configure(persistenceUnitName, new Properties());
          Configuration hbmcfg = cfg.getHibernateConfiguration();
          SchemaExport schemaExport = new SchemaExport(hbmcfg);
          schemaExport.setOutputFile(destination);
          schemaExport.setDelimiter(";");
          schemaExport.setFormat(format);
          schemaExport.execute(true, false, false, create);
          System.out.println("Schema exported to " + destination);
     }
}