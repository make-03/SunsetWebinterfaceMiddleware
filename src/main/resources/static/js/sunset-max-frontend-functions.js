logAccess("sunset-max");
var codeMirrorEditor; // Is initialized when calling initCodeMirror(...)
var consoles            =   $('#consoles');
var inputConsole        =   document.getElementById('inputTextArea');
var outputConsole       =   $('#outputTextArea');

// added 02.04.2019
document.getElementById("sendBtn").disabled = false;
document.getElementById("stopBtn").disabled = true;

$(document).ready(function(){
	console.log('Initializing page ...');
    /*Add the attributes 'data-tooltip' and 'data-delay' to each button of the function-panel*/
    var functions = $('#function-panel>ul>li>button');
    functions.each(function(){
        $(this).prop({
            "data-position":    "bottom",
            "data-delay":       500
        });
        $(this).addClass("btn tooltipped waves-effect");
    });
    $('.tooltipped').tooltip({delay: 500});

    // Initialize Splitter Layout
    {
        consoles.layout({
            closable:           true,
            resizable:		    true,
            slidable:		    true,
            livePaneResizing:	true,
            togglerLength_open: 0,
            minSize:            150
        });

        // Disable text-selection when dragging (or event trying to drag)

        $.layout.disableTextSelection = function(){
            var $d	= $(document),
                s = 'textSelectionDisabled',
                x='textSelectionInitialized';
            if ($.fn.disableSelection) {
                if (!$d.data(x)) // document hasn't been initialized yet
                    $d.on('mouseup', $.layout.enableTextSelection ).data(x, true);
                if (!$d.data(s))
                    $d.disableSelection().data(s, true);
            }
        };
        $.layout.enableTextSelection = function(){
            var $d	= $(document), s='textSelectionDisabled';
            if ($.fn.enableSelection && $d.data(s))
                $d.enableSelection().data(s, false);
        };
        $(".ui-layout-resizer").disableSelection() // affects only the resizer element
            .on('mousedown', $.layout.disableTextSelection ); // affects entire document
    }

    // Initialize CodeMirror
    {
        codeMirrorEditor = CodeMirror.fromTextArea(inputConsole, {
            theme: "eclipse",
            indentUnit: 2,
            smartIndent: true,
            tabSize: 5,
            lineNumbers: true
        });
        $('#inputTextArea .CodeMirror').resizable({
            resize: function() {
                editor.setSize($('#input-console').width(), $('#input-console').height());
            }
        });

        /*Drag n' Drop is already implemented in CodeMirror Editor!!!*/

        /* REMOVED 03.04.2019 Set default value of the codeMirror editor*/
        // codeMirrorEditor.getDoc().setValue('program calculate {\n\tprintln("Hello World!");\n}');
        //codeMirrorEditor.getDoc().setValue('');
        
    }

    FFaplApiSideNav.init(false);
    FFaplApiSideNav.appendVersionLink('min');
    Footer.init();

    // Initialize Plugins
    $(".button-collapse").sideNav({
        menuWidth: 300, // Default is 240
        edge: 'left', // Choose the horizontal origin
        closeOnClick: false // Closes the side-nav on <a> clicks
    });
    $('.collapsible').collapsible({
        accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });
    $('main .modal-trigger').leanModal();
    
    // ADDED 03.04.2019 - Scroll to bottom of output text field
    outputConsole.scrollTop(outputConsole[0].scrollHeight);
    var objDiv = document.getElementById("output-console");
    objDiv.scrollTop = objDiv.scrollHeight;
    
    // focus cursor inside input textarea
    //document.getElementById('inputTextArea').focus();
    
    console.log('Finished initiaizing page!');
});

// Button-functions for function panel
{
    //Transmitts the code to a server, and executes it there. Needs a callback-function for server-response.
    function onBtnExecute(){
    	// added 02.04.2019
    	console.log('Clicked execution button!');
        document.getElementById("sendBtn").disabled = true;
        document.getElementById("stopBtn").disabled = false;
        
        var code = codeMirrorEditor.getValue();
        code = code.replace(/(\r\n|\n|\r)/g,"\n");
        if(code.length>0){

            var hash = calcMD5(code);
            // added 03.04.2019
            console.log('MD5 Hash of Code: '+hash);
            //processCode(code, function(data){processCodeSucceeded(data)}, function(data){processCodeFailed(data)});
        }
    }
    
    /* 03.04.2019
    //Represent the solution of the transmitted Code!!!
    function processCodeSucceeded(data){
        if(data){
            data = htmlEntities(data);
            if(outputConsole.html()){
                outputConsole.append("<div class='divider'></div>")
            }
            outputConsole.append((new Date()).toLocaleTimeString());
            outputConsole.append("<pre style='padding-left:20px; line-height:20px'>" + data + "</pre>");
            // Scroll to bottom
            var objDiv = document.getElementById("output-console");
            objDiv.scrollTop = objDiv.scrollHeight;
            document.getElementById("exeButton").disabled = false;
        }
    }
    function processCodeFailed(data){
        if(outputConsole.html()){
            outputConsole.append("<br>")
        }
        outputConsole.append("An error occured: <br>" + data);
    }
    */

    function onBtnStopExecution(){
        /*killProgram(
            function(data){
                terminateProcessCallback(data);
            }, function(data){
                terminateProcessCallback(data);
            }
        );
        */
    	
    	// added 02.04.2019
    	console.log('Clicked cancel button!');
        document.getElementById("sendBtn").disabled = false;
        document.getElementById("stopBtn").disabled = true;
    }

    /* 03.04.2019
    function terminateProcessCallback(data){
        if(data){
            data = htmlEntities(data);
            if(outputConsole.html()){
                outputConsole.append("<div class='divider'></div>")
            }
            outputConsole.append((new Date()).toLocaleTimeString());
            outputConsole.append("<pre style='padding-left:20px; line-height:20px'>" + data + "</pre>");
            // Scroll to bottom
            var objDiv = document.getElementById("output-console");
            objDiv.scrollTop = objDiv.scrollHeight;
        }
    }
	*/

    /* 03.04.2019 - these functions (undo+redo) currently do not work properly */
    function undo(){
    	console.log('Clicked undo button!');
        codeMirrorEditor.execCommand('undo', true, null);
    }
    function redo(){
    	console.log('Clicked redo button!');
        codeMirrorEditor.execCommand('redo', true, null);
    }

    /*Fetches the text of the CodeMirror and creates a file to store the code on local devices*/
    // UPDATED 03.04.2019
    function save(){
    	console.log('Clicked the save button!');
        var textToWrite = codeMirrorEditor.getValue();
        
        // filename starts with code_ + current time (hh_mm_ss)
        var date = new Date();
        Date.prototype.timeNow = function () {
            return ((this.getHours() < 10)?"0":"") + this.getHours() +":"+ ((this.getMinutes() < 10)?"0":"") + 
            	this.getMinutes() +":"+ ((this.getSeconds() < 10)?"0":"") + this.getSeconds();
        }
        
        var fileName = 'code_'+date.timeNow()+'.ffapl';
        
        if(!textToWrite.length>0){
        	console.log('No code entered! Cannot save empty file!');
        	alert("Please write something before saving.");
        	return;
        }
        
        var a = document.createElement("a");
        document.body.appendChild(a);
        a.style = "display: none";
        
        blob = new Blob([textToWrite], {type: "plain/text;charset=utf-8"}),
        url = window.URL.createObjectURL(blob);
        
        console.group( "Object URL" );
        console.log( "Text:", textToWrite );
        console.log( "URL:", url );
        console.groupEnd();
        
        a.href = url;
        a.download = fileName;
        
        console.log('Prepared download link, starting download ...');
        
        a.click();
        window.URL.revokeObjectURL(url);
        
        // OLD CODE - DEPRECATED 03.04.2019
        /*
        var downloadLink = document.createElement("a");
        downloadLink.download = fileNameToSaveAs;
        downloadLink.innerHTML = "Download File";
        if (window.URL != null)
        {
            // Chrome allows the link to be clicked
            // without actually adding it to the DOM.
            downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
        }
        else
        {
            // Firefox requires the link to be added to the DOM
            // before it can be clicked.
            downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
            //downloadLink.onclick = destroyClickedElement();
            downloadLink.style.display = "none";
            document.body.appendChild(downloadLink);
        }
        console.log('Created download element!');
        console.log('[DOWNLOAD LINK]: '+downloadLink);

        downloadLink.click();
        */
        
    }

    /*Fetches the text(innerHTML) of the CodeMirror and prints it*/
    function print(){
    	console.log('Clicked the print button!');
        var textToWrite = codeMirrorEditor.getValue();
        if(!textToWrite.length>0){alert("Please write something before printing.");return;}

        var childWindow = window.open('','childWindow','location=yes, menubar=yes, toolbar=yes');
        childWindow.document.open();
        childWindow.document.write('<html><head></head><body>');
        childWindow.document.write(textToWrite.replace(/\n/gi,'<br>'));
        childWindow.document.write('</body></html>');
        childWindow.print();
        childWindow.document.close();
        childWindow.close();
    }

    var fileContainer = $('#fileContainer');
    var filePathField = $('#filePathField');
    function uploadFile(){
    	console.log('Clicked the upload button!');
        if (window.File && window.FileReader && window.FileList && window.Blob) {
            // Great success! All the File APIs are supported - Show the dialog.
            var files = fileContainer.prop('files');

            var reader = new FileReader();
            // Callback for FileReader when he finished reading a file
            reader.onload = function(e){
                codeMirrorEditor.getDoc().setValue(reader.result);
                $('#modalUpload').closeModal();
            }
            for(var i=0, f; f=files[i]; i++){
                var fileReader = reader.readAsText(f);
            }
        } else {
            alert('Can\' upload file. The File APIs are not fully supported in this browser.');
        }
    }

    function cancelUpload()
    {
        $('#modalUpload').closeModal();
    }
}
