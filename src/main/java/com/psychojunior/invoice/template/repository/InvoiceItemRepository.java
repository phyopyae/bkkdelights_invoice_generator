package com.psychojunior.invoice.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.psychojunior.invoice.template.entity.InvoiceItem;

public interface InvoiceItemRepository  extends JpaRepository<InvoiceItem, Long>  {

	List<InvoiceItem> findByInvoiceNumber(String invoiceNumber);
	
	@Transactional
	void deleteByInvoiceNumber(String invoiceNumber);
}
