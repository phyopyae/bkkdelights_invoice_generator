package com.psychojunior.invoice.template.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.psychojunior.invoice.template.entity.Invoice;
import com.psychojunior.invoice.template.entity.InvoiceItem;
import com.psychojunior.invoice.template.model.InvoiceDto;
import com.psychojunior.invoice.template.service.InvoiceService;
import com.psychojunior.invoice.template.service.PrintService;

@Controller
public class InvoiceController {

	private final InvoiceService invoiceService;
	private final PrintService printService;

	public InvoiceController(InvoiceService invoiceService, PrintService printService) {
		this.invoiceService = invoiceService;
		this.printService = printService;
	}

	@GetMapping("/")
	private String getHomepage() {
		return "redirect:/invoices";
	}

	@GetMapping("/test")
	private String getTestPage(Model model) {
		InvoiceDto invoice = invoiceService.getInitialInvoice();
		model.addAttribute("invoice", invoice);
		return "index";
	}

	@GetMapping("/invoices")
	public String getInvoiceList(Model model) {
		List<InvoiceDto> invoiceList = invoiceService.getAllInvoiceList();
		model.addAttribute("invoices", invoiceList);
		return "invoice_list";
	}

	@GetMapping("/invoice/{invoiceNumber}")
	public String getInvoiceDetails(@PathVariable String invoiceNumber, Model model) {
		InvoiceDto invoice = invoiceService.findInvoiceByNumber(invoiceNumber);

		if (invoice != null) {
			model.addAttribute("invoice", invoice);
		} else {
			model.addAttribute("message", "Invoice not found.");
		}

		return "invoice_detail";
	}

	@GetMapping("/invoice/add")
	public String showAddInvoiceForm(Model model) {
		InvoiceDto invoice = invoiceService.getInitialInvoice();
		model.addAttribute("invoice", invoice);
		return "invoice_add";
	}

	@PostMapping(value = "/invoice/add", params = "save")
	public String addInvoice(@ModelAttribute InvoiceDto invoiceDto, RedirectAttributes redirectAttributes) {
		invoiceService.save(invoiceDto);
		redirectAttributes.addFlashAttribute("message", "Invoice Added.");
		return "redirect:/invoices";
	}

	@PostMapping(value = "/invoice/add", params = "print")
	public String printInvoice(@ModelAttribute InvoiceDto invoiceDto) {
		// TODO to call print function
		return "redirect:/invoices";
	}

	@PostMapping("/invoice/print/{invoiceNumber}")
	public ResponseEntity<ByteArrayResource> printInvoice(@PathVariable String invoiceNumber, Model model) {

		Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
		List<InvoiceItem> invoiceItemList = invoiceService.getInvoiceItemByInvoiceNumber(invoiceNumber);

		if (null == invoice && invoiceItemList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		byte[] report = printService.getPrintedInvoiceReport(invoice, invoiceItemList);
		String fileName = invoiceService.getFileName(invoiceNumber);

		ByteArrayResource resource = new ByteArrayResource(report);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(resource.contentLength()).header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.attachment().filename(fileName).build().toString())
				.body(resource);
	}

	@Transactional
	@PostMapping("/invoice/delete/{invoiceNumber}")
	public String deleteInvoice(@PathVariable String invoiceNumber, RedirectAttributes redirectAttributes) {
		Boolean invoice = invoiceService.deleteInvoice(invoiceNumber);
		if (!invoice) {
			redirectAttributes.addFlashAttribute("message", "Invoice not found.");
		} else {
			redirectAttributes.addFlashAttribute("message", "Invoice deleted.");
		}
		return "redirect:/invoices";
	}

	@PostMapping(value = "/saveInvoice", params = "save")
	public String saveInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto, Model model) {
		invoiceService.save(invoiceDto);
		model.addAttribute("invoice", invoiceDto);
		model.addAttribute("isSave", true);
		return "index";
	}

	@GetMapping("/invoice/edit/{invoiceNumber}")
	public String editInvoice(@PathVariable String invoiceNumber, Model model) {

		InvoiceDto invoice = invoiceService.findInvoiceByNumber(invoiceNumber);

		if (invoice != null) {
			model.addAttribute("invoice", invoice);
			return "invoice_edit"; // Return the edit view
		} else {
			model.addAttribute("message", "Invoice not found.");
			return "redirect:/invoices"; // Redirect to the invoices list if not found
		}
	}

	@Transactional
	@PostMapping("/invoice/edit/{invoiceNumber}")
	public String updateInvoice(@PathVariable String invoiceNumber, @ModelAttribute InvoiceDto updatedInvoice,
			RedirectAttributes redirectAttributes) {
		boolean isUpdated = invoiceService.update(invoiceNumber, updatedInvoice);

		if (isUpdated) {
			redirectAttributes.addFlashAttribute("message", "Invoice updated successfully.");
			return "redirect:/invoice/" + invoiceNumber; // Redirect to the updated invoice's details page
		} else {
			redirectAttributes.addFlashAttribute("message", "Failed to update invoice.");
			return "redirect:/invoice/edit/" + invoiceNumber; // Stay on the edit page if update fails
		}
	}
}
