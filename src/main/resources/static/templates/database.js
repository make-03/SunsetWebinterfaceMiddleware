var sendFeedbackLocation        = "../resources/scripts/sendFeedback.php";
var logAccessLocation           = "../resources/scripts/logAccess.php";

// Improved Versions
function sendFeedback(msg, callbackFunctionSuccess, callbackFunctionFail){
    msg = escape(msg);
    $.ajax({
        url:        sendFeedbackLocation,
        type:       "POST",
        data:       {feedback: msg}
    }).done(function(data){
        callbackFunctionSuccess(data);
    }).fail(function(data){
        callbackFunctionFail(data);
    });
}

function logAccess(pageId){
    $.ajax({
        url:        logAccessLocation,
        type:       "POST",
        data:       {id: pageId}
    }).done(function(data){
        //console.log("Data logged. Response: " + data);
    }).fail(function(data){
        //console.log("Failed to log data. Response: " + data); 
    });
}
