var linkToMinVersion = "sunset-min.html";
var linkToMaxVersion = "sunset-max.html";
// edited 2.4.2019 
var syssecLogoSource = "/img/syssec_logo.svg";
var navId, cme;

var code;

FFaplApiSideNav = {
    // @Param isMobile      Whether the navbar should be a slide-out or not
    init: function (isMobile) {
        navId = isMobile ? "slide-out" : "nav-mobile";
        $('body>main').append("<style>" +
            "#modalApiProgram .CodeMirror{" +
            "height: auto;" +
            "}" +
            +"</style>");

        $('body>main').append("<div id='modalApiDetails' class='modal'>" +
            "<div class='modal-content'>" +
            "<h5 id='modalApiDetailsHeader'>Header</h5>" +
            "<p id='api-description'>Beschreibung</p>" +
            "</div>" +
            "<div class='modal-footer'>" +
            "<a class='modal-action modal-close waves-effect btn-flat'>Close</a>" +
            "</div></div>");

        $('body>main').append("<div id='modalApiProgram' class='modal modal-fixed-footer'>" +
            "<div class='modal-content'>" +
            "<h5 id='modalApiProgramHeader'>Header</h5>" +
            "<textarea id='txtaProgramCode' style=''></textarea>" +
            "</div>" +
            "<div class='modal-footer'>" +
            "<ul class='collapsible' data-collapsible='accordion'>" + 
            "<li>" + 
            "<a class='modal-action modal-close waves-effect btn-flat'>Close</a>" +
            "</li>" +
            "<li>" +    
            "<a class='modal-action modal-copy waves-effect btn-flat' onclick='copyCodeToClipBoard()'>Copy Example to Clip Board</a>" +
            "</li>" +
            "</ul>" +
            "</div></div>");

        $('body>main').append(
            "<ul id='" + navId + "' class='side-nav " + (isMobile ? "" : "fixed") + "'>" +
            "<li class='no-item center brand-logo'><img src='" + syssecLogoSource + "'></li>" +
            "<li class='no-item header center'>FFapl API</li>" +
            "<li class='no-item'><div class='divider'></div></li>" +

            "<!--Spezifications-->" +
            "<li class='no-padding'>" +
            "<ul class='collapsible' data-collapsible='accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Spezifikationen</a>" +
            "<div class='collapsible-body'>" +
            "<ul class='second-layer'>" +
            "<!--Types-->" +
            " <li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            " <li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Typen</a>" +
            "<div class='collapsible-body'><ul class='typeContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "<!--Functions-->" +
            "<li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Funktionen</a>" +
            "<div class='collapsible-body'><ul class='functionContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "<!--Procedures-->" +
            "<li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Prozeduren</a>" +
            "<div class='collapsible-body'><ul class='procedureContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "</ul>" +
            "</div>" +
            "</li>" +
            "</ul>" +
            "</li>" +

            "<!--Examples-->" +
            "<li class='no-padding'>" +
            "<ul class='collapsible' data-collapsible='accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Beispiele</a>" +
            "<div class='collapsible-body'>" +
            "<ul class='second-layer'>" +
            "<li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Funktionen</a>" +
            "<div class='collapsible-body'><ul class='exFunctionContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "<li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Prozeduren</a>" +
            "<div class='collapsible-body'><ul class='exProcedureContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "<!--Program Examples-->" +
            "<li class='no-padding'>" +
            "<ul class='collapsible collapsible-accordion'>" +
            "<li>" +
            "<a class='collapsible-header waves-effect waves-teal bold'>Programmbeispiele</a>" +
            "<div class='collapsible-body'><ul class='exProgramContainer'></ul></div>" +
            "</li>" +
            "</ul>" +
            "</li>" +
            "</ul>" +
            "</div>" +
            "</li>" +
            "</ul>" +
            "</li>" +

            "<!--Snippets Code goes here!-->" +
            
            "</ul>");
       
        /* // code for snippets, if needed, append this to the html string directly above
        "<li class='no-padding'>" +
        "<ul class='collapsible' data-collapsible='accordion'>" +
        "<li class='bold'>" +
        "<a class='collapsible-header waves-effect waves-teal bold'>Snippets</a>" +
        "<div class='collapsible-body'><ul class='snippetContainer'></ul></div>" +
        "</li>" +
        "</ul>" +
        "</li>" +
		*/

        cme = CodeMirror.fromTextArea(document.getElementById("txtaProgramCode"), {
            theme: "eclipse",
            indentUnit: 2,
            smartIndent: true,
            tabSize: 4,
            lineNumbers: true,
            readOnly: true
        });
        if (!$('#modalApiProgram .CodeMirror')) {
            $('#modalApiProgram .CodeMirror').resizable({
                resize: function () {
                    editor.setSize($('#txtaProgramCode').width(), $('#txtaProgramCode').height());
                }
            });
        }
        /*Drag n' Drop is already implemented in CodeMirror Editor!!!*/


        this.fillApi();
        $('#' + navId + ' .modal-trigger').leanModal();
    },

    fillApi: function () {
        // Append API content to <ul>
        var objects = API.getAllObjects();
        var container, name, method;
        for (var i = 0, o; o = objects[i]; i++) {
            name = o.name;
            method = "FFaplApiSideNav.showModalApiDetails(this)";
            if (o.id.substring(0, API.ids.TYPE.length) === API.ids.TYPE) {
                container = $('.typeContainer');
            } else if (o.id.substring(0, API.ids.FUNCTION.length) === API.ids.FUNCTION) {
                container = $('.functionContainer');
                name = API.getFunctionNameByObject(o);
            } else if (o.id.substring(0, API.ids.PROCEDURE.length) === API.ids.PROCEDURE) {
                container = $('.procedureContainer');
                name = API.getProcedureNameByObject(o);
            } else if (o.id.substring(0, API.ids.PROGRAM.length) === API.ids.PROGRAM) {
                container = $('.exProgramContainer');
                method = "FFaplApiSideNav.showModalApiProgram(this)";
            } else if (o.id.substring(0, API.ids.EXAMPLE_F.length) === API.ids.EXAMPLE_F) {
                container = $('.exFunctionContainer');
            } else if (o.id.substring(0, API.ids.EXAMPLE_P.length) === API.ids.EXAMPLE_P) {
                container = $('.exProcedureContainer');
            } else if (o.id.substring(0, API.ids.SNIPPET.length) === API.ids.SNIPPET) {
                container = $('.snippetContainer');
            }
            if (container) {
                container.each(function () {
                    $(this).append("<li draggable='true' ondragstart='FFaplApiSideNav.dragApiElement(event, this)'>" +
                        "<a details='" + o.id + "' class='truncate' onclick='" + method + "'>" + name + "</a>" +
                        "</li>");
                });
            }
        }
    },

    /** @Param version      Is either 'min' or 'max'. Appends a Link to the designated html version of the interface.
     *                      If version === 'min', then it appends a link to the min version.
     */
    appendVersionLink: function (version) {
        var container = $("#" + navId);
        if (version == 'min') {
           /* container.append(
                "<li><div class='divider'></div></li>" +
                "<li><a href='" + linkToMinVersion + "'>Minimal Version</a></li>");
           */
        } else if (version == 'max') {
            container.append(
                "<li><div class='divider'></div></li>" +
                "<li><a href='" + linkToMaxVersion + "'>Full Version</a></li>");
        }
    },

    showModalApiDetails: function (obj) {
        var id = $(obj).attr('details');
        var object = API.getObjectById(id);
        if (object) {
            var header = object.fullname ? object.fullname : object.name;
            $('#modalApiDetailsHeader').html(header);
            $('#api-description').html(object.descr.replace(/\n/g, "<br>"));
            $('#modalApiDetails').openModal();
        } else {
            console.log("No object found with id: " + id);
        }
    },
    showModalApiProgram: function (obj) {
        var id = $(obj).attr('details');
        var object = API.getObjectById(id);
        if (object) {
            $('#modalApiProgramHeader').html(object.name);

            cme.getDoc().setValue(object.code);
            code = object.code;
            
            // Refresh CodeMirror
            $('#modalApiProgram').openModal();
            setTimeout(function () {
                cme.refresh()
            }, 320); // Timeout is required to get total size of codeMirror/modal
        } else {
            console.log("No object found with id: " + id);
        }
    },
    /*Define what content is transfered when dragging*/
    dragApiElement: function (ev, element) {
        var objID = element.getElementsByTagName('a')[0].getAttribute("details");
        var apiObj = API.getObjectById(objID); 
        ev.dataTransfer.setData("text", API.getName(apiObj));
    } 
}

function copyCodeToClipBoard() {
	var tempelement = document.createElement('textarea');
	tempelement.value = code;
	tempelement.setAttribute('readonly', '');
	tempelement.style = {position: 'absolute', left: '-9999px'};
	document.body.appendChild(tempelement);
	tempelement.select();
	document.execCommand('copy');
	document.body.removeChild(tempelement);
	
	console.log("Copied example to clip board!");
	alert("Copied example to clip board!");
	
	$('#modalApiProgram').closeModal();
}
