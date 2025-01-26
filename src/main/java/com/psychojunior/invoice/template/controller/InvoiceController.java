package com.psychojunior.invoice.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.psychojunior.invoice.template.model.InvoiceDto;
import com.psychojunior.invoice.template.service.CalculateService;
import com.psychojunior.invoice.template.service.InvoiceService;

@Controller
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	CalculateService calculateService;
	
	@GetMapping("/")
	private String getHomepage(Model model) {
		InvoiceDto invoice = invoiceService.getInitialInvoice();
		model.addAttribute("invoice", invoice);
        return "index";
	}
	
	@GetMapping("/invoice")
    public String showInvoiceForm(Model model) {
        model.addAttribute("invoice", new InvoiceDto());
        return "invoice";
    }

    @PostMapping(value = "/saveInvoice", params = "calculate")
    public String calculateInvoice(@ModelAttribute("invoice") InvoiceDto invoice, Model model) {
    	calculateService.calculateAmount(invoice);
    	model.addAttribute("invoice", invoice);
    	System.out.println(invoice.toString());
        return "index";
    }
    
    @PostMapping(value = "/saveInvoice", params = "save")
    public String saveInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto, Model model) {
    	invoiceService.save(invoiceDto);
    	model.addAttribute("invoice", invoiceDto);
    	model.addAttribute("isSave", true);
        return "index";
    }
    
    @PostMapping(value = "/saveInvoice", params = "print")
    public ResponseEntity<ByteArrayResource> printInvoice(@ModelAttribute("invoice") InvoiceDto invoice, Model model) {
    	byte[] report = invoiceService.getInvoiceReport(invoice);
    	
    	String fileName = invoiceService.getFileName(invoice);
    	
        ByteArrayResource resource = new ByteArrayResource(report);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                            .filename(fileName)
                            .build().toString())
                .body(resource);
    }
}
