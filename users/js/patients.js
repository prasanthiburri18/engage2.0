var pathwayeventslist = [];
var pathwayinfo = [];
var currentlyselectedpathwayid = 0;
var currentlyselectedpathwayname = '';
var checkedevents = [];
var validatef;
var orgid = 0;
var currentdate = new Date();
var currenttime = currentdate.getTime();
/*if (sessionStorage.getItem("authtoken") != null)
{
    var usertoken = sessionStorage.getItem("authtoken");
    var br = 'Bearer ';
    var securitytoken = br.concat(usertoken);
} else {
    logout();
    return;
}*/
$.holdReady( true );


var ajaxurl = userapibase+"/api/v1/userbasicinfo"
$.ajax({
    url: ajaxurl,
    type: 'GET',
    dataType: 'json',
    "async": true,
    "crossDomain": true,
    contentType: 'application/json; charset=UTF-8',
    Accept: "application/json",
    xhrFields: {
        withCredentials: true
    },
   // headers:{ 'Authorization':securitytoken},
    
    //  data: JSON.stringify(datat),
    beforeSend: function ()
    {

        $("#error").fadeOut();
        $.LoadingOverlay("show");
    },
    success:function(response){
    	if(response.data!=null){
    	setUser(response.data.UserBacsicInfo);
    	  $.LoadingOverlay("hide");
    	 
    	  $.holdReady( false );
    	//  setTimeout(' window.location.href = "patientslist.html"; ', 1000);
    	}
    },
    error: function(response, status){

    	if(response.status==412) {
    	$.LoadingOverlay("hide");
    		logout();
    	}

    }

});

function setUser(userdata) {
	    sessionStorage.setItem("userinfo", JSON.stringify(userdata));
	}
	function getUser() {

	    var userdata = sessionStorage.getItem("userinfo");
	    return userdata;
	}
	$('document').ready(function ()
			{
var retrievedObject = sessionStorage.getItem('userinfo');
var output = JSON.parse(retrievedObject);
console.log(output);


    /**
     * Checking for token authentication on Page Load itself.
     * If not fouund then explicitly we are logging out
     * using logout functionality
     */
  /*  
*/

    orgid = output.orgid;

    if (output.userType == 'U')
    {
    	
        $("#patientaddpathwaybtn").css('display', 'none');
    }

    $(".lguser").html(output.fullName);

    $("#dialog").dialog({
        autoOpen: false,
        show: {
            effect: "blind",
            duration: 500
        },
        hide: {
            effect: "fade",
            duration: 500
        }
    });

    $("#importlist").on("click", function () {
//      $( "#dialog" ).dialog( "open" );

        $.toast({
            heading: 'Add Patient',
            text: 'Available only for  Premium users.',
            textAlign: 'center',
            position: 'top-center',
            icon: 'information',
            loader: false,
            allowToastClose: false,
            hideAfter: 5000,
        });
    });

    $("#emaipretext").click(function () {
        $("#emailpremier").css('display', 'block');
    })
    $("#logoutbtn").click(function () {
        sessionStorage.clear();

        window.location.href = "index.html";
    });
//    $("#patientdob").focusout(function () {
//        var d = new Date();
//
//        var month = d.getMonth() + 1;
//        var day = d.getDate();
//
//        var output = d.getFullYear();
//
//        var cdate = $(this).val();
//
//        var datearr = cdate.split('-');
//
//        if (parseInt(datearr[0]) > parseInt(output)) {
//            //CurrentDate is more than SelectedDate
//            $(".error").html("Invalid date of birth");
//        } else {
//            //SelectedDate is more than CurrentDate
//            //alert('success');
//        }
//    });
//
    $("#patientdob").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: '1700:' + new Date().getFullYear(),
        maxDate: '0',
        buttonText: "Select date",
        dateFormat: "mm/dd/yy",
        constrainInput: false
    });


    $('.phone').keypress(function (event) {


        var charCode = (event.which) ? event.which : event.keyCode;

        if (charCode != 8 && isNaN(String.fromCharCode(charCode))) {
            event.preventDefault();
        }

        var phone_aval = $('#phone1').val().length;
        var phone_bval = $('#phone2').val().length;
        var phone_cval = $('#phone3').val().length;

        if (phone_aval < 3) {
            $("#phone1").focus();
        } else if (phone_aval > 2 && phone_bval < 3) {
            $("#phone2").focus();
        } else if (phone_aval > 2 && phone_bval > 2) {
            $("#phone3").focus();
        } else if (phone_cval > 4) {
            return false;

        }

    });

    $('#mybutton').click(function () {
        var phone_aval = $('#phone1').val();
        var phone_bval = $('#phone2').val();
        var phone_cval = $('#phone3').val();

    });

    /**
     *
     * @returns {JsonObject}
     * View PatientPathway
     * Loading the Master Pathway which is associated with Organization
     * for user selction at the time of adding patient
     *
     */

    var pathwaysjson = [];
    var pdata = {"orgId": orgid};


    $.ajax({
        url: pathwayapibase + '/api/v1/listPathway',
        type: 'POST',
        dataType: 'json',
        headers: {
            //'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        xhrFields: {
               withCredentials: true
           },
        Accept: "application/json",
        data: JSON.stringify(pdata),
        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");
        },
        success: function (response)
        {
        	
            if (response.statuscode == 200) {


                $.LoadingOverlay("hide");

                pathwaysjson = response.data;
                pathwaysjson = pathwaysjson.reverse();



                $("#dpathways").empty();
                var firstpathwayid = 0;
                $.each(pathwaysjson, function (key, pathway) {


                    pathwayeventslist[pathway.id] = pathway.events;
                    pathwayinfo[pathway.id] = pathway.pathwayName;
                    if (key == 0)
                    {
                        currentlyselectedpathwayname = pathway.pathwayName;
                        firstpathwayid = pathway.id;
                    }
                    var rdshtml = '';
                    rdshtml = '<div class="col-md-4 col-sm-4">';

                    rdshtml += '<div class="radio">';

                    rdshtml += '<input type="radio" name="optionsRadios"  id="pathw-' + pathway.id + '" value="' + pathway.id + '" onclick="showevents(' + pathway.id + ');">';
                    rdshtml += '<label for="optionsRadios"><span></span>' + pathway.pathwayName + '</label>';
                    rdshtml += '</div></div>';

                    $(rdshtml).appendTo('#dpathways');


                });




                if (firstpathwayid != 0)
                {
//            showevents(firstpathwayid);
                }

            }
        },
        error: function(response, status){

          if(response.status==412) {
          $.LoadingOverlay("hide");
            logout();
          }

        }
    });
    
  
//End of getting the pathway list here

    var checknames_name = "Hyper"

    $("#eventschec").empty();

    $('#eventschec').on('change', 'input[name="eventpaths"]', function () {
        $.each($("input[name='eventpaths']:checked"), function () {


        });
    });

    validatef = $("#addpatient-form").validate({
        rules:
                {
                    patienfrmfirstname: {
                        required: true,
                    },
                    patienfrmlastname: {
                        required: true,
                    },
                    patientdob: {
                        required: true,
                    },
                    phone1: {
                        required: true,
                        minlength: 3,
                        min: 001
                    },
                    phone2: {
                        required: true,
                        minlength: 3,
                        min: 000
                    },
                    phone3: {
                        required: true,
                        minlength: 4,
                        min: 0000

                    },
                    groups: {
                        patientphone: "phone1 phone2 phone3"
                    },
                },
        errorPlacement: function (error, element) {

            if (element.attr("name") == "phone1" || $('#phone1').val() == "000" || element.attr("name") == "phone2" || element.attr("name") == "phone3")
            {

                $("#pherr").nextAll('label').remove();
                error.insertAfter("#pherr");
            } else
            {

                element.empty();
                error.insertAfter(element);
            }
        },
        messages:
                {
                    patienfrmfirstname: {
                        required: "Please enter patient first name."
                    },
                    patienfrmlastname: {
                        required: "Please enter patient last name."
                    },
                    patientdob: {
                        required: "Please enter patient date of birth."
                    },
                    phone1: {
                        required: "Please enter a valid Phone Number"
                    },
                    phone2: {
                        required: "Please enter a valid Phone Number"
                    },
                    phone3: {
                        required: "Please enter a valid Phone Number"
                    },
                },
        submitHandler: patientsubmitForm
    });


    $('#eventschec').on('change', 'input[name="eventpaths"]', function () {
        checkedevents = [];
        $.each($("input[name='eventpaths']:checked"), function () {

            checkedevents.push(parseInt($(this).val()));
        });

    });

    $("#devicepa").on("click", function () {
//      $( "#dialog" ).dialog( "open" );

        $.toast({
            heading: 'Device Token',
            text: 'Available only for  Premium users.',
            textAlign: 'center',
            position: 'top-center',
            icon: 'information',
            loader: false,
            allowToastClose: false,
            hideAfter: 5000,
        });
    });

    $("#emailpa").on("click", function () {

//      $( "#dialog" ).dialog( "open" );

        $.toast({
            heading: 'Email',
            text: 'Available only for  Premium users.',
            textAlign: 'center',
            position: 'top-center',
            icon: 'information',
            loader: false,
            allowToastClose: false,
            hideAfter: 5000,
        });
    });
    /**
     * @Input JsonObject
     * @returns {JsonObject}
     * Adding patient using QC API call.
     * Patient can be added with pathway assigned or
     * without pathway assigned.
     * The backed system will checks for patient pathway assinged . If there is
     * a pathway is assigned to patient then the system will send an sms
     * or else it will ignore. Same thing in Edit patient case well.
     */
    function patientsubmitForm()
    {
      //Clear server side error messages before submitting
      $('.fieldError').text('');


        var pfname = $("#patienfrmfirstname").val();
        var plname = $("#patienfrmlastname").val();

        var outDate = $("#patientdob").val();
      //   var date = new Date($('#patientdob').val());
      // day = date.getDate();
      // month = date.getMonth() + 1;
      // year = date.getFullYear();
      // var outDate=[month, day, year].join('/');
       //alert(outDate);
        //alert(outDate);
        //var ts = moment(outDate).valueOf();
        //alert(ts);
        //var alertdd =new Date(moment.unix(outDate));
       // alert(alertdd);

        var  pdob= convertDate(outDate); // 2013-12-06
        //alert(pdob)
        var phone_aval = $('#phone1').val();
        var phone_bval = $('#phone2').val();
        var phone_cval = $('#phone3').val();



        var pnum = phone_aval.toString() + phone_bval.toString() + phone_cval.toString();
        var pn = pnum;

        var pdata = {"clinicianId": output.id, "email": "", "firstName": pfname,
            "lastName": plname, "phone": pnum, "deviceToken": "6384632",
            "dob": pdob, "orgId": orgid, "pathwayId": currentlyselectedpathwayid,
            events: checkedevents, pathwayName: currentlyselectedpathwayname,
            createDate: currenttime, updateDate: currenttime}


        $.ajax({
            url: patientapibase + '/api/v1/addPatient',
            type: 'POST',
            dataType: 'json',
            headers: {
                //'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            xhrFields: {
               withCredentials: true
           },
            Accept: "application/json",
            data: JSON.stringify(pdata),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {


                if (response.statuscode == 200) {


                    $.LoadingOverlay("hide");
                    setTimeout('window.location.href = "patientslist.html";', 1000);

                }
                if (response.statuscode == 204) {


                    $.LoadingOverlay("hide");
                    $.toast({
                        heading: 'Add Patient',
                        text: response.message,
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'error',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });

                }

                if (response.statuscode == 400) {


                    $.LoadingOverlay("hide");
                    $("#error").fadeIn(500, function () {
                        $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                    });

                }else if (response.statuscode == 422) {
                    var resData = $.parseJSON(response.data);
                    if(typeof(resData.firstName) != "undefined" && resData.firstName !== null){
                        $('#error-firstName').text(resData.firstName);
                        $.LoadingOverlay("hide");
                    }

                    if(typeof(resData.lastName) != "undefined" && resData.lastName !== null){
                        $('#error-lastName').text(resData.lastName);
                    }

                    if(typeof(resData.dob) != "undefined" && resData.dob !== null){
                        $('#error-dob').text(resData.dob);
                    }

                    if(typeof(resData.phone) != "undefined" && resData.password !== null){
                        $('#error-phone').text(resData.phone);
                    }

                    $.LoadingOverlay("hide");
                }

                else {

                    $.LoadingOverlay("hide");
                    $("#error").fadeIn(500, function () {
                        $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                    });

                }
            },
            error: function (request, status, error) {
                $.LoadingOverlay("hide");
                $.toast({
                    heading: 'Add Patient',
                    text: 'Error occurred.',
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });


                  if(request.status==412) {
                  $.LoadingOverlay("hide");
                    logout();
                  }



            }
        });
        return false;
    }

});


function logout() {


    sessionStorage.clear();

    window.location.href = "index.html";

}

function pathwaychange() {

}

function showevents(pathwayid) {
    currentlyselectedpathwayid = pathwayid;
    currentlyselectedpathwayname = pathwayinfo[pathwayid];

    checkedevents = [];

    var eventdata = pathwayeventslist[pathwayid];

    eventdata.sort(function (a, b) {
        return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0);
    });
    $("#eventschec").empty();

    $('input:radio').parent().removeClass('active');
    $('#pathw-' + pathwayid).parent().addClass('active');
    $('#pathw-' + pathwayid).prop("checked", "checked");

    $.each(eventdata, function (index, eventval) {

        var chechtml = '';
        chechtml += '<div class="col-md-4 col-sm-4">';
        chechtml += '<div class="checkbox active">';

        if (eventval.eventName == "Welcome")
        {

            chechtml += '<input type="checkbox" checked ="true" disabled name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">'
            chechtml += '<label for="optionscheck' + eventval.id + '" > <span></span>' + eventval.eventName + '</label>';
        } else
        {
            chechtml += '<input type="checkbox" checked ="true" name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">'
            chechtml += '<label for="optionscheck' + eventval.id + '" > <span></span>' + eventval.eventName + '</label>';
        }



        chechtml += '</div>';
        chechtml += '</div>';
        checkedevents.push(eventval.id);

        $(chechtml).appendTo('#eventschec');

    });
}
//Cancel button operation for add patient.
$('#cancel').on('click', function (e) {
    e.preventDefault();
    validatef.resetForm();
    $('form').get(0).reset();
    window.location = "patientslist.html";
});
          var convertDate = function(usDate) {
  var dateParts = usDate.split(/(\d{1,2})\/(\d{1,2})\/(\d{4})/);
  return dateParts[3] + "-" + dateParts[1] + "-" + dateParts[2];
}
