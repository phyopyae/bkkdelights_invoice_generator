package com.psychojunior.invoice.template.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.psychojunior.invoice.template.entity.Invoice;
import com.psychojunior.invoice.template.entity.InvoiceItem;
import com.psychojunior.invoice.template.model.InvoiceDto;
import com.psychojunior.invoice.template.model.InvoiceItemDto;
import com.psychojunior.invoice.template.repository.InvoiceItemRepository;
import com.psychojunior.invoice.template.repository.InvoiceRepository;

@Service
public class InvoiceService {

	@Value("${invoice.itemcount}")
	private Integer INITIAL_INVOICE_ITEM_COUNT;

	@Value("${invoice.filetype}")
	private String INVOICE_FILE_TYPE;
	
	@Value("${invoice.filetype.excel}")
	private String INVOICE_FILE_TYPE_EXCEL;

	private final InvoiceRepository invoiceRepo;
	private final InvoiceItemRepository invoiceItemRepo;

	public InvoiceService(InvoiceRepository invoiceRepo, InvoiceItemRepository invoiceItemRepo) {
		this.invoiceRepo = invoiceRepo;
		this.invoiceItemRepo = invoiceItemRepo;
	}

	public InvoiceDto getInitialInvoice() {
		InvoiceDto invoice = new InvoiceDto();

		for (int i = 0; i < INITIAL_INVOICE_ITEM_COUNT; i++) {
			invoice.getItemsList().add(new InvoiceItemDto());
		}

		return invoice;
	}

	public List<InvoiceDto> getAllInvoiceList() {
		List<Invoice> invoiceList = invoiceRepo.findAll();
		List<InvoiceDto> invoiceDtoList = new ArrayList<InvoiceDto>();
		for (Invoice invoice : invoiceList) {
			invoiceDtoList.add(convertToInvoiceDto(invoice));
		}
		return invoiceDtoList;
	}

	public InvoiceDto save(InvoiceDto invoiceDto) {
		saveInvoice(invoiceDto);
		saveInvoiceItem(invoiceDto.getInvoiceNumber(), invoiceDto.getItemsList());
		return invoiceDto;
	}

	public void saveInvoice(InvoiceDto invoiceDto) {
		Invoice invoice = new Invoice();
		invoice.setCustomerName(invoiceDto.getCustomerName());
		invoice.setCustomerAddress(invoiceDto.getCustomerAddress());
		invoice.setContactNumber(invoiceDto.getContactNumber());
		invoice.setInvoiceNumber(invoiceDto.getInvoiceNumber());
		invoice.setDepositAmount(
				invoiceDto.getDepositAmount() != null ? invoiceDto.getDepositAmount() : BigDecimal.ZERO);
		invoice.setTotalAmount(invoiceDto.getTotalAmount());
		invoice.setInvoiceDate(invoiceDto.getInvoiceDate());
		invoice.setPrinted(false);
		invoiceRepo.save(invoice);
	}

	private InvoiceDto convertToInvoiceDto(Invoice invoice) {
		InvoiceDto dto = new InvoiceDto();
		dto.setContactNumber(invoice.getContactNumber());
		dto.setCustomerAddress(invoice.getCustomerAddress());
		dto.setCustomerName(invoice.getCustomerName());
		dto.setDepositAmount(invoice.getDepositAmount() != null ? invoice.getDepositAmount() : BigDecimal.ZERO);
		dto.setInvoiceDate(invoice.getInvoiceDate());
		dto.setInvoiceNumber(invoice.getInvoiceNumber());
		dto.setTotalAmount(invoice.getTotalAmount());
		return dto;
	}

	private List<InvoiceItemDto> convertToInvoiceItemDtoList(List<InvoiceItem> invoiceItemList) {
		List<InvoiceItemDto> invoiceItemDtoList = new ArrayList<InvoiceItemDto>();
		for (InvoiceItem item : invoiceItemList) {
			if (item.getItemNumber().isBlank()) {
				continue;
			}

			InvoiceItemDto itemDto = new InvoiceItemDto();
			itemDto.setItemNumber(item.getItemNumber());
			itemDto.setItemDescription(item.getItemDescription());
			itemDto.setItemPrice(item.getItemPrice());
			itemDto.setItemTotalAmt(item.getItemTotalAmt());
			itemDto.setItemCount(item.getItemCount());
			invoiceItemDtoList.add(itemDto);
		}
		return invoiceItemDtoList;
	}

	public void saveInvoiceItem(String invoiceNumber, List<InvoiceItemDto> itemDtoList) {

		List<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>();
		for (InvoiceItemDto itemDto : itemDtoList) {

			if (null == itemDto.getItemNumber() || itemDto.getItemNumber().isBlank()) {
				continue;
			}

			InvoiceItem invoiceItem = new InvoiceItem();
			invoiceItem.setItemNumber(itemDto.getItemNumber());
			invoiceItem.setItemDescription(itemDto.getItemDescription());
			invoiceItem.setItemPrice(itemDto.getItemPrice());
			invoiceItem.setItemTotalAmt(itemDto.getItemTotalAmt());
			invoiceItem.setItemCount(itemDto.getItemCount());
			invoiceItem.setInvoiceNumber(invoiceNumber);
			invoiceItemList.add(invoiceItem);
		}

		invoiceItemRepo.saveAll(invoiceItemList);
	}

	public String getFileName(String invoiceNumber) {
		return "Invoice_".concat(invoiceNumber.concat(".").concat(INVOICE_FILE_TYPE));
	}

	public InvoiceDto findInvoiceByNumber(String invoiceNumber) {
		Optional<Invoice> invoice = invoiceRepo.findByInvoiceNumber(invoiceNumber);
		if (invoice.isPresent()) {
			InvoiceDto invoiceDto = convertToInvoiceDto(invoice.get());
			List<InvoiceItem> invoiceItemList = invoiceItemRepo.findByInvoiceNumber(invoiceNumber);
			invoiceDto.setItemsList(convertToInvoiceItemDtoList(invoiceItemList));
			return invoiceDto;
		}

		return null;
	}

	public Boolean deleteInvoice(String invoiceNumber) {
		Optional<Invoice> invoice = invoiceRepo.findByInvoiceNumber(invoiceNumber);
		if (invoice.isPresent()) {
			invoiceItemRepo.deleteByInvoiceNumber(invoiceNumber);
			invoiceRepo.delete(invoice.get());
			return true;
		}

		return false;
	}

	public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
		Optional<Invoice> invoice = invoiceRepo.findByInvoiceNumber(invoiceNumber);
		if (invoice.isPresent()) {
			return invoice.get();
		}

		return null;
	}

	public List<InvoiceItem> getInvoiceItemByInvoiceNumber(String invoiceNumber) {
		return invoiceItemRepo.findByInvoiceNumber(invoiceNumber);
	}

	public boolean update(String invoiceNumber, InvoiceDto updatedInvoice) {
		boolean isUpdatedInvoice = updateInvoice(invoiceNumber, updatedInvoice);
		if (isUpdatedInvoice) {
			updateInvoiceItem(invoiceNumber, updatedInvoice.getItemsList());
			return true;
		}
		return false;
	}

	public boolean updateInvoice(String invoiceNumber, InvoiceDto updatedInvoice) {
		Optional<Invoice> existingInvoice = invoiceRepo.findByInvoiceNumber(invoiceNumber);

		if (existingInvoice.isPresent()) {
			Invoice invoice = existingInvoice.get();
			invoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
			invoice.setCustomerName(updatedInvoice.getCustomerName());
			invoice.setCustomerAddress(updatedInvoice.getCustomerAddress());
			invoice.setContactNumber(updatedInvoice.getContactNumber());
			invoice.setDepositAmount(updatedInvoice.getDepositAmount());
			invoice.setTotalAmount(updatedInvoice.getTotalAmount());
			invoiceRepo.save(invoice); // Save the updated invoice
			return true;
		}
		return false;
	}

	public void updateInvoiceItem(String invoiceNumber, List<InvoiceItemDto> itemDtoList) {

		invoiceItemRepo.deleteByInvoiceNumber(invoiceNumber);
		saveInvoiceItem(invoiceNumber, itemDtoList);
	}

	public String getExcelFileName(String invoiceNumber) {
		return "Invoice_".concat(invoiceNumber.concat(".").concat(INVOICE_FILE_TYPE_EXCEL));
	}

}
