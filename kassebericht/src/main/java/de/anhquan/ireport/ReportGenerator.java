package de.anhquan.ireport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ReportGenerator {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			JRException {
		
		ReportGenerator reporter = new ReportGenerator();
		
		reporter.fillReport( getDataSource());
		
		System.out.println("Done!");
	}
	
	public void fillReport(JRDataSource dataSource) throws JRException{
	    JasperPrint jasperPrint = JasperFillManager.fillReport("reports/report7.jasper",  new HashMap<String, Object>(),
	    dataSource);
	    JasperExportManager.exportReportToPdfFile(jasperPrint, "report7.pdf");
	}
	
	public void testReport() throws JRException{
		fillReport(new JREmptyDataSource());
	}
	
	  private static JRCsvDataSource getDataSource() throws JRException
	  {
	    JRCsvDataSource ds = new JRCsvDataSource(JRLoader.getLocationInputStream("financisto.csv"));	   
	    ds.setRecordDelimiter("\n");
	    ds.setFieldDelimiter(';');
	    ds.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));    
	    Locale locale = new Locale("en", "US");
	    NumberFormat numberFormat = NumberFormat.getInstance(locale);

	    ds.setNumberFormat(numberFormat);
	    ds.setUseFirstRowAsHeader(true);
	    return ds;
	  }


}
