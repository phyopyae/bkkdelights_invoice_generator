var index = 0;
let invoiceItemMemo = [];

function addField() {
    var dynamicFieldsDiv = document.getElementById('inputFields');
    var newIndex = dynamicFieldsDiv.children.length; // Index for the new field

    // Create new input field
    var newFieldDiv = document.createElement('div');
    newFieldDiv.classList.add('item_'+newIndex);
    //newFieldDiv.innerHTML = '<input type="text" th:field="*{fields[__' + newIndex + '__].value}" >';
    var itemNum = '<input type="text" class="form-control-sm" th:field="*{itemNumber}" id="itemNumber' + newIndex + '" placeholder="Item#">';
    var itemDesc = '<input type="text" class="form-control-lg" th:field="*{itemDescription}" id="itemDescription' + newIndex + '" placeholder="Description">';
    var itemCount = '<input type="number" class="form-control-sm" th:field="*{itemCount}" id="itemCount' + newIndex + '" placeholder="Quantity">';
    var itemPrice = '<input type="number" class="form-control-sm" th:field="*{itemPrice}" id="itemPrice' + newIndex + '" placeholder="Unit Price">';
    var totalAmt = '<input type="number" class="form-control-sm" th:field="*{itemTotalAmt}" id="itemTotalAmt' + newIndex + '" placeholder="Amount">';
    var buttons = '<button type="button" class="btn btn-danger" onclick="saveField(this,' + newIndex + ')">Save</button><button type="button" class="btn btn-danger" onclick="removeField(this)">Remove</button>';
    
    newFieldDiv.innerHTML = itemNum + itemDesc + itemCount + itemPrice + totalAmt + buttons;
    
    //newFieldDiv.innerHTML = `
    //    <input type="text" class="form-control-sm" th:field="*{itemNumber}" id="itemNumber' + newIndex + '" placeholder="Item#">
	//	<input type="text" class="form-control" th:field="*{itemDescription}" id="itemDescription" placeholder="Description">
	//	<input type="number" class="form-control-sm" th:field="*{itemCount}" id="itemCount" placeholder="Quantity">
	//	<input type="number" class="form-control-sm" th:field="*{itemPrice}" id="itemPrice" placeholder="Unit Price">
	//	<input type="number" class="form-control-sm" th:field="*{itemTotalAmt}" id="itemTotalAmt" placeholder="Amount">
    //    <button type="button" class="btn btn-danger" onclick="saveField(this)">Save</button>
    //    <button type="button" class="btn btn-danger" onclick="removeField(this)">Remove</button>
    //`;
    
    // Append new field to dynamicFieldsDiv
    dynamicFieldsDiv.appendChild(newFieldDiv);
}

// Function to remove the input field
function removeField(button) {
    button.parentNode.remove();
}

function calculate() {
	
}

function saveField(button, index) {
	console.log(index);
	
	var itemNum = $("#itemNumber"+index).val();
    var itemDesc = $("#itemDescription"+index).val();
    var itemCount = $("#itemCount"+index).val();
    var itemPrice = $("#itemPrice"+index).val();
    var totalAmt = $("#itemTotalAmt"+index).val();
    
    var invoiceItem = {
			'itemNumber' : itemNum,
			'itemDescription' : itemDesc,
			'itemPrice' : itemCount,
			'itemCount' : itemPrice,
			'itemTotalAmt' : totalAmt
	}
	
	invoiceItemMemo.push(invoiceItem);
    
    console.log(invoiceItemMemo);
}