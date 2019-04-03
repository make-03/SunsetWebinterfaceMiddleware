console.log('Script loaded successfully!');

function loadPage() {
	document.getElementById("sendBtn").disabled = false;
	document.getElementById("resetBtn").disabled = false;
	document.getElementById("infoText").innerHTML = '';
	document.getElementById("stopBtn").disabled = true;
}

function calcResult() {
	console.log('Button <Send Code> clicked, deactivating elements ...');
	document.getElementById("sendBtn").disabled = true;
	document.getElementById("resetBtn").disabled = true;
	document.getElementById("infoText").innerHTML = 'calculating result, please wait ...';
	document.getElementById("stopBtn").disabled = false;
}