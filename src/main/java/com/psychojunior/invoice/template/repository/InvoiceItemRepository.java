package com.psychojunior.invoice.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psychojunior.invoice.template.entity.InvoiceItem;

public interface InvoiceItemRepository  extends JpaRepository<InvoiceItem, Long>  {

}
