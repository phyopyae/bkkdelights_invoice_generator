package com.psychojunior.invoice.template.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.psychojunior.invoice.template.model.Invoice;
import com.psychojunior.invoice.template.model.InvoiceItem;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

@Service
public class InvoiceService {

	@Value("${invoice.itemcount}")
	private Integer INITIAL_INVOICE_ITEM_COUNT;
	
	public Invoice getInitialInvoice() {
		Invoice invoice = new Invoice();
		
		for (int i = 0; i < INITIAL_INVOICE_ITEM_COUNT; i++) {
			invoice.getItemsList().add(new InvoiceItem());
	    }
		
		return invoice;
	}
	
	public byte[] getInvoiceReport(Invoice items) {

		byte[] reportContent = null;
		try {
			JasperReport jasperReport;

			jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("invoice_template.jasper"));
			File file = ResourceUtils.getFile("classpath:invoice_template.jrxml");
			jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRSaver.saveObject(jasperReport, "invoice_template.jasper");

			reportContent = printJasper(jasperReport, items);
			
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		}
		
		return reportContent;
	}

	public byte[] printJasper(JasperReport jasperReport, Invoice invoice) {
		byte[] reportContent = null;
		JasperPrint jasperPrint = null;
		
		List<Invoice> itemsList = new ArrayList<Invoice>();
		itemsList.add(invoice);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemsList);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("title", "Invoice");

		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return reportContent;
	}
}
