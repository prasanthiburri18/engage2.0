$(document).ready(function() {
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


    var apibase = baseurl + 'users';
    var patientmicroservice = 'patient';

var patientapibase = baseurl + patientmicroservice;
var bid = getUrlParameter('bid');
$("#messwidget").css('display','none');
$( "#patientdob" ).datepicker({
			showOn: "button",
			buttonImage: "images/calendar-icon.png",
			buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true,
                        yearRange: "-100:+0",
                         maxDate: '0',
			buttonText: "Select date",
                        dateFormat:"yy-mm-dd"
		});
                /**
                 * Loading the Patient Information
                 * by patient dob.
                 * Once the patient found in db then the 
                 * phi message will get displayed based on Block record id.
                 * 
                 */
                $("#checkpatientdob").click(function(){
                      var dobinput={"dob":$("#patientdob").val()};
                    
                    $.ajax({
            url: userapibase+'/getPatientBydob',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(dobinput),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                 $.LoadingOverlay("hide");
                 
                 var result=parseInt(response.data);
                 if(result >0)
                 {
                   
var binput={"id":bid};
         $.ajax({
            url: userapibase+'/getPatientpathwayblockbyId',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(binput),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                 $.LoadingOverlay("hide");
                 
                 if(response.data.length >0)
                 {
                     $("#messwidget").css('display','block');
                     var bdata=response.data;
                     if(bdata[0].body_of_message!='no message')
                     {
                         
                         $("#patientblockmessage").html(bdata[0].body_of_message);
                     }
                      if(bdata[0].remainder_of_message!='no message')
                     {
                         
                         $("#patientblockmessage").html(bdata[0].remainder_of_message);
                     }
                       if(bdata[0].followup_of_message!='no message')
                     {
                         
                         $("#patientblockmessage").html(bdata[0].followup_of_message);
                     }
                     
                     $("#dobwidget").css('display','none');
                 }
                 
            }
        });
                 }
                 else
                 {
                     
            $.toast({
    heading: 'Error',
    text: 'Entered date of birth is not found in our database..',
    textAlign: 'center',
    position: 'top-center',
    icon: 'warning',
    loader:false,
    allowToastClose: false,
    hideAfter: 5000,
    
});          
                 }
                 
            }
        });
                    
                    
                    
                });
                
    });

