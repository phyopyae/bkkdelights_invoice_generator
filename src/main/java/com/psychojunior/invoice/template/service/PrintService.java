package com.psychojunior.invoice.template.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.psychojunior.invoice.template.entity.Invoice;
import com.psychojunior.invoice.template.entity.InvoiceItem;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class PrintService {

	private final ResourceLoader resourceLoader;

	public PrintService(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public byte[] getPrintedInvoiceReport(Invoice invoice, List<InvoiceItem> invoiceItemList) {
		
		byte[] reportContent = null;
		try {
			
			JasperReport jasperReport;

			File file = ResourceUtils.getFile("classpath:invoice_template.jrxml");
			jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

			reportContent = printJasper(jasperReport, invoice, invoiceItemList);

		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		}

		return reportContent;
	}
	
	public byte[] printJasper(JasperReport jasperReport, Invoice invoice, List<InvoiceItem> itemList) {
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
			parameters.put("invoiceItems", itemList);
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
	
	private Date convertToDate(LocalDate invoiceDate) {
		Date convertedDate = Date.from(invoiceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return convertedDate;
	}
}
