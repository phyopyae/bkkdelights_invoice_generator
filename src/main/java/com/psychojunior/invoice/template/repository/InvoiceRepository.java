package com.psychojunior.invoice.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psychojunior.invoice.template.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>  {

	Invoice save(Invoice invoice);

}
