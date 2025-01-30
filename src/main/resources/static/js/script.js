// Function to calculate amount for each item and total amount
function calculateAmount() {
	let totalAmount = 0; // Variable to store the sum of all amounts
	let depositAmountInput = document.getElementById("depositAmount");
	let depositAmount = parseFloat(depositAmountInput.value);

	// Get all rows of items with the class 'item-row'
	let rows = document.querySelectorAll(".item-row");  // Select all item rows

	rows.forEach(function(row) {
		// Get the quantity and unit price inputs for this row
		let quantityInput = row.querySelector("input[id$='itemCount']");
		let unitPriceInput = row.querySelector("input[id$='itemPrice']");
		let amountInput = row.querySelector("input[id$='itemTotalAmt']");

		// Parse the values as numbers
		let quantity = parseFloat(quantityInput.value);
		let unitPrice = parseFloat(unitPriceInput.value);

		// Check if both values are valid numbers (not NaN)
		if (isNaN(quantity) || isNaN(unitPrice)) {
			return;
		}
		
		// Calculate the amount
		let amount = quantity * unitPrice;

		// Set the amount in the corresponding input field
		amountInput.value = amount.toFixed(2); // Display with two decimal places

		// Add to the total amount
		totalAmount += amount;
	});
	
	if (depositAmount > 0 && totalAmount > 0) {
		totalAmount -= depositAmount;
	}

	// Update the total amount field
	document.getElementById("totalAmount").value = totalAmount.toFixed(2); // Display total with two decimal places
}

function formatDate(dateStr) {
	let parts = dateStr.split('/');

	if (parts.length === 3) {
	    let month = parts[0].padStart(2, '0');
	    let day = parts[1].padStart(2, '0');
	    let year = '20' + parts[2];

	    return year + '-' + month + '-' + day;
	} else {
	    console.error("Invalid date format.");
	    return null;
	}
}

function addNewItem() {

	let tableBody = document.querySelector("table tbody");
	let newRow = document.createElement("tr");
	
	newRow.classList.add("item-row");
	newRow.innerHTML = `
			<td><input type="text" class="form-control-sm" id="itemsList${tableBody.children.length}.itemNumber" name="itemsList[${tableBody.children.length}].itemNumber" placeholder="Item#" required></td>
			<td><input type="text" class="form-control-sm" id="itemsList${tableBody.children.length}.itemDescription" name="itemsList[${tableBody.children.length}].itemDescription" placeholder="Description" required></td>
			<td><input type="number" class="form-control-sm" id="itemsList${tableBody.children.length}.itemCount" name="itemsList[${tableBody.children.length}].itemCount" placeholder="Quantity" oninput="calculateAmount()" required></td>
			<td><input type="number" class="form-control-sm" id="itemsList${tableBody.children.length}.itemPrice" name="itemsList[${tableBody.children.length}].itemPrice" placeholder="Unit Price" oninput="calculateAmount()" required></td>
			<td><input type="number" class="form-control-sm" id="itemsList${tableBody.children.length}.itemTotalAmt" name="itemsList[${tableBody.children.length}].itemTotalAmt" placeholder="Amount" readonly></td>
			<td><button type="button" class="btn btn-danger btn-sm" onclick="removeItem(this)">Remove</button></td>
		`;

	tableBody.appendChild(newRow);
	calculateAmount();
}

function removeItem(button) {
	
	let row = button.closest("tr");

	row.remove();
	calculateAmount();
}

document.addEventListener("DOMContentLoaded", function() {
	// Add event listeners for item count and price inputs
	let quantityInputs = document.querySelectorAll("input[id$='itemCount']");
	let priceInputs = document.querySelectorAll("input[id$='itemPrice']");

	quantityInputs.forEach(input => input.addEventListener("input", calculateAmount));
	priceInputs.forEach(input => input.addEventListener("input", calculateAmount));
	
	let invoiceDateInput = document.getElementById("invoiceDate");

	let dateValue = invoiceDateInput.defaultValue;

	if (dateValue) {
		let formattedDate = formatDate(dateValue);

		invoiceDateInput.value = formattedDate;
	}
});