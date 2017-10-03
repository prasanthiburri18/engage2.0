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
var pathwayid = getUrlParameter('pathwayid');
var pathwayinpt={"pathwayid":pathwayid};
// alert(apibase+'/getorgname');
// alert( JSON.stringify(pathwayinpt));
$.ajax({
            url: apibase+'/getorgname',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(pathwayinpt),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                if(response.data!="No Name")
                {
                    $("#orgname").html(response.data);
                    $.LoadingOverlay("hide");
                }
            },
            error: function(response, status){

              if(response.status==412) {
              $.LoadingOverlay("hide");
                logout();
              }

            }
            });


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

                      var inputdate=$("#patientdob").val();
                      var da=new Date(inputdate);
                      //alert(da);
                      // var inputarr=inputdate.split('-');
                      // var day= parseInt(inputarr[2])-1;
                      // var newdate=inputarr[0]+'-'+inputarr[1]+'-'+day;
                      // da.setDate(da.getDate()+1);
                      // alert(da);
                      // var day = da.getDate();
                      //   var month = da.getMonth() + 1;
                      //   var year = da.getFullYear();


                        var new_date=formatDate(da);
                        //alert(new_date);
                      var dobinput={"dob":new_date};

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

            },
            error: function(response, status){

              if(response.status==412) {
              $.LoadingOverlay("hide");
                logout();
              }

            }
        });
                 }
                 else
                 {

            $.toast({
    heading: 'Error',
    text: 'Your phone number and date of birth combination does not match. Please try again',
    textAlign: 'center',
    position: 'top-center',
    icon: 'error',
    loader:false,
    allowToastClose: false,
    hideAfter: 5000,

});
                 }

            },
            error: function(response, status){

              if(response.status==412) {
              $.LoadingOverlay("hide");
                logout();
              }

            }
        });



                });

    });

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
