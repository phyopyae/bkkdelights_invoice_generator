package com.psychojunior.invoice.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psychojunior.invoice.template.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>  {

	@SuppressWarnings("unchecked")
	Invoice save(Invoice invoice);
	
	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
