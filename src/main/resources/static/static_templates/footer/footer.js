Footer = {
    init:                   function(){
        $('footer').load(retrieveURL('footer')+'/footer.html', function(){
            // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
            $('footer .modal-trigger').leanModal();
            $('footer #btnSendFeedback').click(function(){Footer.onBtnSendFeedback()});
        });
    },
    txtaFeedback:           function(){return $('footer #textareaFeedback')},
    btnSendFeedback:        function(){return $('footer #btnSendFeedback')},
    modalFeedback:          function(){return $('footer #modalFeedback')},
    errorMessageContainer:           function(){return $('footer #errorMessage')},
    loadAnimation:          function(){return $('footer #animationProcessFeedback')},
    errorMessage:           function(){return "Ein Fehler ist aufgetreten. Bitte versuchen Sie es erneut."},
    onBtnSendFeedback:      function(){
        var txta = Footer.txtaFeedback();
        if(Footer.validateInput(txta.val()) == false){
            txta.val('').addClass('invalid').prop('placeholder', 'Bitte geben Sie eine Nachricht ein.');
        }else{
            txta.removeClass('invalid').removeProp('placeholder');
            Footer.toggleLoadAnimation(true);
            setTimeout(
                function(){sendFeedback(txta.val(),
                             function(data){Footer.sendFeedbackSuccess(data)},
                             function(data){footer.sendFeedbackFailed(data)}
                            )},
                1000);
        }
    },
    validateInput:          function(msg){
        if(msg.trim().length == 0){
            return false;
        }else{
            return true;
        }
    },
    toggleLoadAnimation:          function(toggle){
        if(toggle == true){
            Footer.loadAnimation().removeClass('hide');
            Footer.btnSendFeedback().addClass('disabled');
        }else{
            Footer.loadAnimation().addClass('hide');
            Footer.btnSendFeedback().removeClass('disabled');
        }
    },
    sendFeedbackSuccess:    function(data){
        if(data != "success"){
            Footer.sendFeedbackFailed(data);
        }else{
            Footer.toggleLoadAnimation(false);
            Footer.txtaFeedback().val('').removeClass('invalid');
            Footer.errorMessageContainer().text('').hide();
            Footer.btnSendFeedback().removeClass('disabled');
            Footer.modalFeedback().closeModal();
        }
    },
    sendFeedbackFailed:     function(data){
        console.log
        Footer.toggleLoadAnimation(false);
        Footer.txtaFeedback().addClass('invalid');
        Footer.errorMessageContainer().text(Footer.errorMessage()).show();
    }
}
var retrieveURL=function(filename) {
    var scripts = document.getElementsByTagName('script');
    if (scripts && scripts.length > 0) {
        for (var i in scripts) {
            if (scripts[i].src && scripts[i].src.match(new RegExp(filename+'\\.js$'))) {
                return scripts[i].src.replace(new RegExp('(.*)'+filename+'\\.js$'), '$1');
            }
        }
    }
};