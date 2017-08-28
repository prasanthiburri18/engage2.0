//var baseurl='http://35.166.195.23:8080/ApiGateway/';
////var baseurl='https://engage-staging.quantifiedcare.com/ApiGateway/';
//var microservice='users';
//var userapibase=baseurl+microservice;

        var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};
        $('document').ready(function ()
{
   
    var tech = getUrlParameter('keyconfirm');
 
    var k=new Object();
    k.emailid=tech;
    var inputstring=k;
  
    
     $.ajax({
            url: userapibase+'/verify_email',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(inputstring),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                $.LoadingOverlay("hide");
               
                if(response.statuscode==200)
                {
                     $('#signupconfirmation').css('display','block');
                    
                }
                else
                {
                    $('#signupconfirmationerr').css('display','block');
                 
                }
              
            }
        });
});
   
    