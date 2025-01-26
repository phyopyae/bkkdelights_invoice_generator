package com.psychojunior.invoice.template.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.psychojunior.invoice.template.model.InvoiceDto;
import com.psychojunior.invoice.template.model.InvoiceItemDto;

@Service
public class CalculateService {

	public void calculateAmount(InvoiceDto invoice) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for (InvoiceItemDto item : invoice.getItemsList()) {
			
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
