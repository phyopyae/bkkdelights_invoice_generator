package com.psychojunior.invoice.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.psychojunior.invoice.template.entity.InvoiceSequence;

import jakarta.persistence.LockModeType;

public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM InvoiceSequence s WHERE s.invoiceMonth = :month")
	InvoiceSequence findByInvoiceMonthForUpdate(@Param("month") String month);
}
