package com.psychojunior.invoice.template.model;

import java.math.BigDecimal;

public class InvoiceItem {

	private String itemNumber;
	private String itemDescription;
	private BigDecimal itemPrice;
	private BigDecimal itemCount;
	private BigDecimal itemTotalAmt;

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemCount() {
		return itemCount;
	}

	public void setItemCount(BigDecimal itemCount) {
		this.itemCount = itemCount;
	}

	public BigDecimal getItemTotalAmt() {
		return itemTotalAmt;
	}

	public void setItemTotalAmt(BigDecimal itemTotalAmt) {
		this.itemTotalAmt = itemTotalAmt;
	}

	@Override
	public String toString() {
		return "InvoiceItem [itemNumber=" + itemNumber + ", itemDescription=" + itemDescription + ", itemPrice="
				+ itemPrice + ", itemCount=" + itemCount + ", itemTotalAmt=" + itemTotalAmt + "]";
	}
}
