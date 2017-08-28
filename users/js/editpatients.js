var pathwayeventslist = [];
var pathwayinfo = [];
var currentlyselectedpathwayid = 0;
var editpathwayid = 0;
var currentlyselectedpathwayname = '';
var checkedevents = [];
var patientcheckevents = [];
var pathwaysjson = [];
var orgid = 0;
var currentdate = new Date();
var currenttime = currentdate.getTime();
var patientcreateddate = '';

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

var pid = getUrlParameter('patientid');


var pvdata = {};
pvdata.id = pid;
var securitytoken = '';
$('document').ready(function ()
{
    /**
     * Checking for token authentication on Page Load itself.
     * If not fouund then explicitly we are logging out
     * using logout functionality
     */
    if (localStorage.getItem("authtoken") != null)
    {
        var usertoken = localStorage.getItem("authtoken");
        var br = 'Bearer ';
        securitytoken = br.concat(usertoken);
    } else {
        logout();
        return;
    }



    var retrievedObject = localStorage.getItem('userinfo');
    var output = JSON.parse(retrievedObject);
    orgid = output.orgid;




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
        $("#dialog").dialog("open");
    });

    $("#emaipretext").click(function () {
        $("#emailpremier").css('display', 'block');
    })
    $("#logoutbtn").click(function () {
        localStorage.clear();

        window.location.href = "index.html";
    });
//Patien DOB Date Picker
    $("#patientdob").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        maxDate: '0',
        buttonText: "Select date",
        dateFormat: "yy-mm-dd",
        constrainInput: false
    });



    /**
     * Patient Phone number Tab auto moved functionality
     * once the field length reached.
     */
    $('.phone').keypress(function (event) {


        if (event.which != 8 && isNaN(String.fromCharCode(event.which))) {
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




    var pdata = {"orgId": orgid};

    /**
     * Listing the Oragnization Pathways
     * which are getting displayed on the edit patient section
     * And comapring the pathway which is already assigned to patient and making
     * that pathway as default selection (disabling other Pathways). In case of no pathway is assigned then 
     * listing all pathways of that organization
     * 
     */

    $.ajax({
        url: pathwayapibase + '/api/v1/listPathway',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
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

                    var rdshtml = '';
                    rdshtml = '<div class="col-md-4 col-sm-4">';
                    rdshtml += '<div class="radio">';

                    rdshtml += '<input type="radio"  disabled name="optionsRadios"  id="pathw-' + pathway.id + '" value="' + pathway.id + '" onclick="showevents(' + pathway.id + ');">';
                    rdshtml += '<label for="optionsRadios"><span></span>' + pathway.pathwayName + '</label>';
                    rdshtml += '</div></div>';

                    $(rdshtml).appendTo('#dpathways');


                });




                if (firstpathwayid != 0)
                {
                    showevents(firstpathwayid);
                }
                ViewpatientDetails();
            } else
            {
                ViewpatientDetails();
            }
        }
    });

    $("input:radio:first").prop("checked", true).trigger("click");

    $('.phone').keypress(function (event) {


        if (event.which != 8 && isNaN(String.fromCharCode(event.which))) {
            event.preventDefault();
        }


        var phone_aval = $('#editphone1').val().length;
        var phone_bval = $('#editphone2').val().length;
        var phone_cval = $('#editphone3').val().length;



        if (phone_aval < 3) {
            $("#editphone1").focus();
        } else if (phone_aval > 2 && phone_bval < 3) {
            $("#editphone2").focus();
        } else if (phone_aval > 2 && phone_bval > 2) {
            $("#editphone3").focus();
        } else if (phone_cval > 4) {
            return false;

        }

    });


    $("#editpatientdob").focusout(function () {
        var d = new Date();

        var month = d.getMonth() + 1;
        var day = d.getDate();

        var output = d.getFullYear();

        var cdate = $(this).val();

        var datearr = cdate.split('-');

        if (parseInt(datearr[0]) > parseInt(output)) {
            //CurrentDate is more than SelectedDate
            $(".error").html("Invalid date of birth");
        } else {
            //SelectedDate is more than CurrentDate
            //alert('success');
        }
    });
    $("#editpatientdob").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        maxDate: '0',
        buttonText: "Select date",
        dateFormat: "yy-mm-dd"
    });

    /**
     * Patient Form Jquery Validation Process
     * 
     */

    $("#editpatient-form").validate({
        rules:
                {
                    editpatienfrmfirstname: {
                        required: true,
                    },
                    editpatienfrmlastname: {
                        required: true,
                    },
                    editpatientdob: {
                        required: true,
                    },
                    phone1: {
                        required: true,
                        min: 001,
                        minlength: 3
                    },
                    phone2: {
                        required: true,
                        min: 001,
                        minlength: 3
                    },
                    phone3: {
                        required: true,
                        min: 0001,
                        minlength: 4
                    },
                    groups: {
                        patientphone: "phone1 phone2 phone3"
                    },
                },
        errorPlacement: function (error, element) {

            if (element.attr("name") == "phone1" || element.attr("name") == "phone2" || element.attr("name") == "phone3")
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
                    editpatienfrmfirstname: {
                        required: "Please enter patient first name."
                    },
                    editpatienfrmlastname: {
                        required: "Please enter patient last name."
                    },
                    editpatientdob: {
                        required: "Please enter patient dob."
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
        submitHandler: editpatientsubmitForm
    });

    /**
     * Edit Patient Functionality
     * @Input Params:Json Object
     * @return { Json Object} description
     * Taking the Patient information and updating in the backend
     * using QC API Call.
     * 
     * 
     */
    function editpatientsubmitForm()
    {
        var pfname = $("#editpatienfrmfirstname").val();
        var plname = $("#editpatienfrmlastname").val();
        var pdob = $("#editpatientdob").val();
        var peid = $("#editpatienid").val();

        var phone_aval = $('#editphone1').val();
        var phone_bval = $('#editphone2').val();
        var phone_cval = $('#editphone3').val();
        if (patientcreateddate == '')
            patientcreateddate = currenttime;
        var pnum = phone_aval.toString() + phone_bval.toString() + phone_cval.toString();
        var pedidata = {id: peid, "orgId": orgid, email: "", "firstName": pfname,
            "lastName": plname, "phone": pnum, "deviceToken": "6384632", "dob": pdob, "status": "Y",
            "pathwayId": currentlyselectedpathwayid, events: checkedevents, pathwayName: currentlyselectedpathwayname, createDate: patientcreateddate, updateDate: currenttime}



        $.ajax({
            url: patientapibase + '/api/v1/editPatient',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(pedidata),
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

                } else {

                    $.LoadingOverlay("hide");
                    $("#error").fadeIn(500, function () {
                        $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                    });

                }
            }
        });
        return false;
    }

    $('#eventschec').on('change', 'input[name="eventpaths"]', function () {
        checkedevents = [];
        $.each($("input[name='eventpaths']:checked"), function () {

            checkedevents.push(parseInt($(this).val()));
        });

    });

});
function logout() {


    localStorage.clear();

    window.location.href = "index.html";

}
function pathwaychange() {

}
function ViewpatientDetails()
{
    $.ajax({
        url: patientapibase + '/api/v1/view_Patient',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        Accept: "application/json",
        data: JSON.stringify(pvdata),
        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");

        },
        success: function (response)
        {

            $.LoadingOverlay("hide");

            if (response.data != null)
            {

                var results = response.data;

                if (results.pathwayInfo.data != null)
                {

                    patientcheckevents = [];
                    $.each(results.pathwayInfo.data.eventInfo, function (assev, asevval) {

                        patientcheckevents.push(asevval.id);
                    });

                    currentlyselectedpathwayid = results.pathwayInfo.data.pathwayInfo.id;

                    editpathwayid = results.pathwayInfo.data.pathwayInfo.id;
                    currentlyselectedpathwayname = results.pathwayInfo.data.pathwayInfo.name;
                } else
                {
                    $('input[name="optionsRadios"]').each(function () {
                        $(this).removeAttr("disabled");

                    });
//                         $('input[name="eventpaths"]').each(function () { 
//                             $(this).removeAttr( "disabled" );
////                             alert($(this).attr('name'));
//                         });



                }

                $("#editpatienid").val(results.patient.id);
                $("#editpatienfrmfirstname").val(results.patient.firstName);
                $("#editpatienfrmlastname").val(results.patient.lastName);
                $('#editpatientdob').datepicker('setDate', new Date(results.patient.dob));
                patientcreateddate = results.patient.createDate;



                var phinput = results.patient.phone;





                $("#editphone1").val(phinput.substring(0, 3));
                $("#editphone2").val(phinput.substring(3, 6));
                $("#editphone3").val(phinput.substring(6, 10));

                if (currentlyselectedpathwayid != 0)
                    showevents(currentlyselectedpathwayid);
            }

        }
    });
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
        chechtml += '<div class="checkbox eventbox active">';
        if (patientcheckevents.length > 0 && editpathwayid == currentlyselectedpathwayid)
        {
            if (patientcheckevents.indexOf(eventval.id) == -1)
            {
                if (eventval.eventName == "Welcome")
                    chechtml += '<input type="checkbox" disabled name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">';
                else
                    chechtml += '<input type="checkbox"  name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">';

            } else
            {
                checkedevents.push(eventval.id);
                if (eventval.eventName == "Welcome")
                    chechtml += '<input type="checkbox" disabled checked ="true" name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">';
                else
                    chechtml += '<input type="checkbox"  checked ="true" name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">'

            }
        } else
        {
            checkedevents.push(eventval.id);
            if (eventval.eventName == "Welcome")
                chechtml += '<input type="checkbox" disabled  checked ="true" name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">';
            else
                chechtml += '<input type="checkbox"  checked ="true" name="eventpaths" value="' + eventval.id + '" id="optionscheck' + eventval.id + '">';
        }




        chechtml += '<label for="optionscheck' + eventval.id + '"> <span></span>' + eventval.eventName + '</label>';
        chechtml += '</div>';
        chechtml += '</div>';

        $(chechtml).appendTo('#eventschec');


    });
}
$("#cancel").click(function () {
    setTimeout('window.location.href = "patientslist.html";', 10);
    return false;
});