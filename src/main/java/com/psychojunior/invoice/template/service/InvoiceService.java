package com.psychojunior.invoice.template.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.psychojunior.invoice.template.model.Invoice;
import com.psychojunior.invoice.template.model.InvoiceItem;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class InvoiceService {

	@Value("${invoice.itemcount}")
	private Integer INITIAL_INVOICE_ITEM_COUNT;
	
	@Value("${invoice.filetype}")
	private String INVOICE_FILE_TYPE;
	
    @Autowired
    private ResourceLoader resourceLoader;
	
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

			File file = ResourceUtils.getFile("classpath:invoice_template.jrxml");
			jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			
			reportContent = printJasper(jasperReport, items);
			
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		}
		
		return reportContent;
	}

	public byte[] printJasper(JasperReport jasperReport, Invoice invoice) {
		byte[] reportContent = null;
		JasperPrint jasperPrint = null;
		
		try {
			Resource imageResource = resourceLoader.getResource("classpath:/static/images/logo.jpg");
	
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("title", "Invoice");
			parameters.put("customerName", invoice.getCustomerName());
			parameters.put("customerContact", invoice.getContactNumber());
			parameters.put("customerAddress", invoice.getCustomerAddress());
			parameters.put("invoiceNumber", invoice.getInvoiceNumber());
			parameters.put("invoiceDate", convertToDate(invoice.getInvoiceDate()));
			parameters.put("totalAmount", invoice.getTotalAmount());
			parameters.put("depositAmount", invoice.getDepositAmount());
			parameters.put("invoiceItems", invoice.getItemsList());
			parameters.put("logo", imageResource.getFile());

			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource(1));
			reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return reportContent;
	}
	
	public String getFileName(Invoice invoice) {
		return "Invoice_".concat(invoice.getInvoiceNumber().concat(".").concat(INVOICE_FILE_TYPE));
	}
	
	private Date convertToDate(LocalDate invoiceDate) {
		Date convertedDate = Date.from(invoiceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return convertedDate;
	}
}
