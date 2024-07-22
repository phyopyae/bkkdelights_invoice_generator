package com.psychojunior.invoice.template.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

	private String invoiceNumber;
	private LocalDate invoiceDate;
	private String customerName;
	private String customerAddress;
	private String contactNumber;
	private BigDecimal depositAmount;
	private BigDecimal totalAmount;
	private List<InvoiceItem> itemsList = new ArrayList<InvoiceItem>();

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<InvoiceItem> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<InvoiceItem> itemsList) {
		this.itemsList = itemsList;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceNumber=" + invoiceNumber + ", invoiceDate=" + invoiceDate + ", customerName="
				+ customerName + ", customerAddress=" + customerAddress + ", contactNumber=" + contactNumber
				+ ", depositAmount=" + depositAmount + ", totalAmount=" + totalAmount + ", itemsList=" + itemsList
				+ "]";
	}
}
