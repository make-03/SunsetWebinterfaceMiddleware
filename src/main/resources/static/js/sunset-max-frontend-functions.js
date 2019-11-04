// logAccess("sunset-max");
var codeMirrorEditor; // Is initialized when calling initCodeMirror(...)
var consoles = $('#consoles');
var inputConsole = document.getElementById('inputTextArea');
var outputConsole = $('#outputTextArea');

document.getElementById("btnSend").disabled = false;
document.getElementById("btnStop").disabled = true;
document.getElementById("btnUndo").disabled = false;
document.getElementById("btnRedo").disabled = false;
document.getElementById("btnUpload").disabled = false;
document.getElementById("btnSave").disabled = false;
document.getElementById("btnPrint").disabled = false;

$(document)
		.ready(
				function() {
					console.log('Initializing page ...');
					/*
					 * Add the attributes 'data-tooltip' and 'data-delay' to
					 * each button of the function-panel
					 */
					var functions = $('#function-panel>ul>li>button');
					functions.each(function() {
						$(this).prop({
							"data-position" : "bottom",
							"data-delay" : 500
						});
						$(this).addClass("btn tooltipped waves-effect");
					});
					$('.tooltipped').tooltip({
						delay : 500
					});

					// Initialize Splitter Layout
					{
						consoles.layout({
							closable : true,
							resizable : true,
							slidable : true,
							livePaneResizing : true,
							togglerLength_open : 0,
							minSize : 150
						});

						// Disable text-selection when dragging (or event trying
						// to drag)

						$.layout.disableTextSelection = function() {
							var $d = $(document), s = 'textSelectionDisabled', x = 'textSelectionInitialized';
							if ($.fn.disableSelection) {
								if (!$d.data(x)) // document hasn't been
													// initialized yet
									$d.on('mouseup',
											$.layout.enableTextSelection).data(
											x, true);
								if (!$d.data(s))
									$d.disableSelection().data(s, true);
							}
						};
						$.layout.enableTextSelection = function() {
							var $d = $(document), s = 'textSelectionDisabled';
							if ($.fn.enableSelection && $d.data(s))
								$d.enableSelection().data(s, false);
						};
						$(".ui-layout-resizer").disableSelection() // affects
																	// only the
																	// resizer
																	// element
						.on('mousedown', $.layout.disableTextSelection); // affects
																			// entire
																			// document
					}

					// Initialize CodeMirror
					{
						codeMirrorEditor = CodeMirror.fromTextArea(
								inputConsole, {
									theme : "eclipse",
									indentUnit : 2,
									smartIndent : true,
									tabSize : 5,
									lineNumbers : true
								});
						$('#inputTextArea .CodeMirror').resizable(
								{
									resize : function() {
										editor.setSize($('#input-console')
												.width(), $('#input-console')
												.height());
									}
								});

						/*
						 * Drag n' Drop is already implemented in CodeMirror
						 * Editor!!!
						 */
					}

					FFaplApiSideNav.init(false);
					FFaplApiSideNav.appendVersionLink('min');
					Footer.init();

					// Initialize Plugins
					$(".button-collapse").sideNav({
						menuWidth : 300, // Default is 240
						edge : 'left', // Choose the horizontal origin
						closeOnClick : false
					// Closes the side-nav on <a> clicks
					});
					$('.collapsible').collapsible({
						accordion : false
					// A setting that changes the collapsible behavior to
					// expandable instead of the default accordion style
					});
					$('main .modal-trigger').leanModal();

					// Scroll to bottom of output text field
					outputConsole.scrollTop(outputConsole[0].scrollHeight);
					var objDiv = document.getElementById("output-console");
					objDiv.scrollTop = objDiv.scrollHeight;

					// focus cursor inside input textarea
					// document.getElementById('inputTextArea').focus();

					// generate random ID for user (does not
					// need to be an UUID!)
					var uniqueId = Math.random().toString(36).substring(2, 15)
							+ Math.random().toString(36).substring(2, 15);
					console.log("Generated ID: " + uniqueId); // logging ID
																// for testing!
					document.getElementById("uniqueId").innerHTML = uniqueId; // used
																				// for
																				// "sendForm"
					// redundant element for storing ID because you cannot
					// define 2 forms in 1 html-attribute!
					document.getElementById("uniqueId2").innerHTML = uniqueId; // used
																				// for
																				// "cancelForm"

					console.log('Page successfully initialized!');
				});

function validateForm() {
	var code = codeMirrorEditor.getValue();
	if (!code.length > 0) {
		alert("Please enter some code before executing.");
	    return false;
	}
}

// Button-functions for function panel
{
	function onBtnExecute() {
		console.log('Clicked execution button!');
		
		var code = codeMirrorEditor.getValue();
		
		if (!code.length > 0) {
			console.log('No code entered! Cannot execute empty code!');
			return;
		}
		
		document.getElementById("btnSend").disabled = true;
		document.getElementById("btnStop").disabled = false;
		document.getElementById("btnUndo").disabled = true;
		document.getElementById("btnRedo").disabled = true;
		document.getElementById("btnUpload").disabled = true;
		document.getElementById("btnSave").disabled = true;
		document.getElementById("btnPrint").disabled = true;

		code = code.replace(/(\r\n|\n|\r)/g, "\n");
		if (code.length > 0) {
			var hash = calcMD5(code);
			console.log('MD5 Hash of Code: ' + hash);
		}
	}

	function onBtnStopExecution() {
		console.log('Clicked cancel button!');
		document.getElementById("btnSend").disabled = false;
		document.getElementById("btnStop").disabled = true;
		document.getElementById("btnUndo").disabled = false;
		document.getElementById("btnRedo").disabled = false;
		document.getElementById("btnUpload").disabled = false;
		document.getElementById("btnSave").disabled = false;
		document.getElementById("btnPrint").disabled = false;
	}

	function undo() {
		console.log('Clicked undo button!');
		codeMirrorEditor.execCommand('undo', true, null);
	}
	function redo() {
		console.log('Clicked redo button!');
		codeMirrorEditor.execCommand('redo', true, null);
	}

	/*
	 * Fetches the text of the CodeMirror and creates a file to store the code
	 * on local devices
	 */
	function save() {
		console.log('Clicked the save button!');

		var code = codeMirrorEditor.getValue();

		if (!code.length > 0) {
			console.log('No code entered! Cannot save empty file!');
			alert("Please write something before saving.");
			return;
		}

		var date = new Date();
		/* code for dd_mm_yyyy format (day of the year) */
		var dd = date.getDate();
		var mm = date.getMonth() + 1; // January is 0!
		var yyyy = date.getFullYear();
		if (dd < 10) {
			dd = '0' + dd;
		}
		if (mm < 10) {
			mm = '0' + mm;
		}
		var today = dd + '-' + mm + '-' + yyyy;
		/* END code for dd_mm_yyyy format */

		/* code for hh_mm_ss format (current timestamp) */
		Date.prototype.timeNow = function() {
			return ((this.getHours() < 10) ? "0" : "") + this.getHours() + "-"
					+ ((this.getMinutes() < 10) ? "0" : "") + this.getMinutes()
					+ "-" + ((this.getSeconds() < 10) ? "0" : "")
					+ this.getSeconds();
		}
		/* END code for hh_mm_ss format */

		var fileName = 'sunsetcode_' + today + '_' + date.timeNow() + '.ffapl';

		var a = document.createElement("a");
		document.body.appendChild(a);
		a.style = "display: none";

		blob = new Blob([ code ], {
			type : "plain/text;charset=utf-8"
		}), url = window.URL.createObjectURL(blob);

		console.group("Object URL");
		console.log("Text:", code);
		console.log("URL:", url);
		console.groupEnd();

		a.href = url;
		a.download = fileName;

		console.log('Prepared download link, starting download ...');

		a.click();
		window.URL.revokeObjectURL(url);
	}

	/* Fetches the text(innerHTML) of the CodeMirror and prints it */
	function print() {
		console.log('Clicked the print button!');
		var code = codeMirrorEditor.getValue();
		if (!code.length > 0) {
			alert("Please write something before printing.");
			return;
		}
		
		var childWindow = window.open('', 'childWindow',
				'location=yes, menubar=yes, toolbar=yes');
		childWindow.document.open();
		childWindow.document.write('<html><head></head><body>');
		childWindow.document.write(code.replace(/\n/gi, '<br>').replace(/\t/gi, '&emsp;'));
		childWindow.document.write('</body></html>');
		childWindow.print();
		childWindow.document.close();
		childWindow.close();
	}

	var fileContainer = $('#fileContainer');
	var filePathField = $('#filePathField');
	function uploadFile() {
		console.log('Clicked the upload button!');
		if (window.File && window.FileReader && window.FileList && window.Blob) {
			// Great success! All the File APIs are supported - Show the dialog.
			var files = fileContainer.prop('files');

			var reader = new FileReader();
			// Callback for FileReader when he finished reading a file
			reader.onload = function(e) {
				codeMirrorEditor.getDoc().setValue(reader.result);
				$('#modalUpload').closeModal();
			}
			for (var i = 0, f; f = files[i]; i++) {
				var fileReader = reader.readAsText(f);
			}
		} else {
			alert('Can\' upload file. The File APIs are not fully supported in this browser.');
		}
	}

	function cancelUpload() {
		$('#modalUpload').closeModal();
	}
}
