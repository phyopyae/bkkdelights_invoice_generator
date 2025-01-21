// Function to calculate amount for each item and total amount
function calculateAmount() {
	let totalAmount = 0; // Variable to store the sum of all amounts

	// Get all rows of items with the class 'item-row'
	let rows = document.querySelectorAll(".item-row");  // Select all item rows

	rows.forEach(function(row) {
		// Get the quantity and unit price inputs for this row
		let quantityInput = row.querySelector("input[id$='itemCount']");
		let unitPriceInput = row.querySelector("input[id$='itemPrice']");
		let amountInput = row.querySelector("input[id$='itemTotalAmt']");

		// Parse the values as numbers
		let quantity = parseFloat(quantityInput.value) || 0; // Default to 0 if not a valid number
		let unitPrice = parseFloat(unitPriceInput.value) || 0; // Default to 0 if not a valid number

		// Calculate the amount
		let amount = quantity * unitPrice;

		// Set the amount in the corresponding input field
		amountInput.value = amount.toFixed(2); // Display with two decimal places

		// Add to the total amount
		totalAmount += amount;
	});

	// Update the total amount field
	document.getElementById("totalAmount").value = totalAmount.toFixed(2); // Display total with two decimal places
}

// Event listener to call calculateAmount when quantity or unit price changes
document.addEventListener("DOMContentLoaded", function() {
	// Add event listeners for item count and price inputs
	let quantityInputs = document.querySelectorAll("input[id$='itemCount']");
	let priceInputs = document.querySelectorAll("input[id$='itemPrice']");

	quantityInputs.forEach(input => input.addEventListener("input", calculateAmount));
	priceInputs.forEach(input => input.addEventListener("input", calculateAmount));
});