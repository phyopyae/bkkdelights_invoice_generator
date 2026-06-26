package com.psychojunior.invoice.template.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InvoiceSequence {

	@Id
	private String invoiceMonth;
	private Long lastNumber;

	public String getInvoiceMonth() {
		return invoiceMonth;
	}

	public void setInvoiceMonth(String invoiceMonth) {
		this.invoiceMonth = invoiceMonth;
	}

	public Long getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(Long lastNumber) {
		this.lastNumber = lastNumber;
	}

}
