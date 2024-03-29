package de.anhquan.ireport;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportGenerator {

	private static final char FINANCISTO_FIELD_DELIMITER = ';';
	private static final String FINANCISTO_RECORD_DELIMITER = "\n";
	private static final String EXPORT_FINANCISTO_CSV = "financisto.csv";
	private static final String INPUT_LOCALE_COUNTRY = "US";
	private static final String INPUT_LOCALE_LANGUAGE = "en";
	private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
	private static final String OUTPUT_JASPER_REPORT = "report.pdf";
	private static final String COMPILED_JASPER_REPORT = "report.jasper";

	public static void main(String[] args)  {

		try {
			new ReportGenerator().execute();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public void execute() throws FileNotFoundException,	JRException{
		compileReport();
		
		System.out.println("Filling report ...");
		fillReport(getDataSource(EXPORT_FINANCISTO_CSV));

		System.out.println("Done!");
	}

	public void compileReport(){
		try {
			System.out.println("Compiling file 'report.jrxml' to report.jasper ...");
			JasperCompileManager.compileReportToFile("report.jrxml",
			        "report.jasper");
		} catch (JRException e) {
			e.printStackTrace();
		}

	}
	public void fillReport(JRDataSource dataSource) throws JRException {
		JasperPrint jasperPrint = JasperFillManager.fillReport(COMPILED_JASPER_REPORT,
				new HashMap<String, Object>(), dataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, OUTPUT_JASPER_REPORT);
	}

	private static JRCsvDataSource getDataSource(String inputCSVFile) throws JRException {
		JRCsvDataSource ds = new JRCsvDataSource(
				JRLoader.getLocationInputStream(inputCSVFile));
		ds.setRecordDelimiter(FINANCISTO_RECORD_DELIMITER);
		ds.setFieldDelimiter(FINANCISTO_FIELD_DELIMITER);
		ds.setDateFormat(new SimpleDateFormat(INPUT_DATE_FORMAT));
		Locale locale = new Locale(INPUT_LOCALE_LANGUAGE, INPUT_LOCALE_COUNTRY);
		NumberFormat numberFormat = NumberFormat.getInstance(locale);

		ds.setNumberFormat(numberFormat);
		ds.setUseFirstRowAsHeader(true);
		return ds;
	}

}
