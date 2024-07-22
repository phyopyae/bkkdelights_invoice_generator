package com.psychojunior.invoice.template.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.psychojunior.invoice.template.model.Invoice;
import com.psychojunior.invoice.template.model.InvoiceItem;

@Service
public class CalculateService {

	public void calculateAmount(Invoice invoice) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (InvoiceItem item : invoice.getItemsList()) {
			
			if (item.getItemCount() == null) {
				continue;
			}
			
			if (item.getItemPrice() == null) {
				continue;
			}
			
			BigDecimal itemAmount = item.getItemCount().multiply(item.getItemPrice());
			item.setItemTotalAmt(itemAmount);
			totalAmount = totalAmount.add(itemAmount);
		}
		
		if (invoice.getDepositAmount() != null && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
			totalAmount = totalAmount.subtract(invoice.getDepositAmount());
		}
		
		invoice.setTotalAmount(totalAmount);
	}
}
