var validatef;
var orgid = 0;
var currentpraticename = '';
var currentdate = new Date();
var currenttime = currentdate.getTime();
var currentusertype = 'A';

$(document).ready(function () {
    /**
     * Checking for token authentication on Page Load itself.
     * If not fouund then explicitly we are logging out
     * using logout functionality
     */
    if (localStorage.getItem("authtoken") != null)
    {
        var usertoken = localStorage.getItem("authtoken");
        var br = 'Bearer ';
        var securitytoken = br.concat(usertoken);
    } else {
        logout();
        return;
    }

    var retrievedObject = localStorage.getItem('userinfo');
    var output = JSON.parse(retrievedObject);
    orgid = output.orgid;
    currentpraticename = output.practiceName;

    if (output.userType == 'U')
    {

        $("#addTeamuser").css('display', 'none');
    }
    $(".lguser").html(output.fullName);
    var dataSet = [];
    //Add Teammeber DOB
    $("#teammembernamedob").datepicker({
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
    $('#example').DataTable({
        "bProcessing": true,
        "order": [],
        "bSort": true,
        "data": dataSet,
        "columns":
                [
                    {"data": "fullname"},
                    {"data": "email"},
                    {"data": "phone"},
                    {"data": "role"}

                ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    var uph = row.phone;
                    var ph1 = uph.substring(0, 3);
                    var ph2 = uph.substring(3, 6);
                    var ph3 = uph.substring(6, 10);





                    return ph1 + '-' + ph2 + '-' + ph3;
                },
                "targets": 2
            },
            {
                "render": function (data, type, row) {
                    var userrole = row.role;
                    var displayrole = '';
                    if (userrole == 'A')
                    {
                        displayrole = "Admin";
                    }
                    if (userrole == 'U')
                    {
                        displayrole = "User";
                    }


                    return displayrole;
                },
                "targets": 3
            },
        ]

    });
    $("#addTeamuser").click(function () {
        $('#addpatient-form')[0].reset();
        $("#addteammemModal").modal('show');
    });


    $('.phone').keypress(function (event) {

        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
        {
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
    /**
     * Showing the team members list by Organization
     * @Input JsonObject
     * @return Output JsonObject
     * List view is rendering using Datatable.
     *  
     */
    var listinpu = {"orgid": orgid};
    $.ajax({
        url: userapibase + '/api/v1/teammemberslist',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        Accept: "application/json",
        data: JSON.stringify(listinpu),
        beforeSend: function (request)
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");

        },
        success: function (response)
        {


            if (response.statuscode == 200) {


                $.LoadingOverlay("hide");

                dataSet = response.data;

                dataSet = dataSet.reverse();


                var myTable = $('#example').DataTable();
                myTable.clear().rows.add(dataSet).draw();


            } else {

                $.LoadingOverlay("hide");
                $("#error").fadeIn(500, function () {
                    $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                });

            }
        }
    });

    $('input[type=radio][name=roletype]').change(function () {

        currentusertype = this.value;
    });
    $(".cancel").click(function () {
        $("#addteammemModal").modal('hide');
        return;
    });
    validatef = $("#addpatient-form").validate({
        rules:
                {
                    teammembername: {
                        required: true,
                    },
                    teammemberemail: {
                        required: true,
                        email: true,
                    },
                    teammembernamedob: {
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
                        min: 001
                    },
                    phone3: {
                        required: true,
                        minlength: 4,
                        min: 0001
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
                    teammembername: {
                        required: "Please enter  name."
                    },
                    teammemberemail: {
                        required: "Please enter correct email address."
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
        submitHandler: addteamsubmitForm
    });

    $('#cancel').on('click', function (e) {
        e.preventDefault();
        validatef.resetForm();
        $('form').get(0).reset();
        $("#addteammemModal").modal('hide');
    });
    /**
     * Adding team memeber into the user logged organization
     * @Input JsonObject
     * @returns {JsonObject}
     * 
     * 
     */
    function addteamsubmitForm()
    {
        var pfname = $("#teammembername").val();
        var temail = $("#teammemberemail").val();
        var pdob = $("#teammembernamedob").val();
        var temamemcomments = $("#membercomments").val();

        var phone_aval = $('#phone1').val();
        var phone_bval = $('#phone2').val();
        var phone_cval = $('#phone3').val();

        var pnum = phone_aval.toString() + phone_bval.toString() + phone_cval.toString();
        var pn = pnum;

        var teaminputobj = {};
        teaminputobj.email = temail;
        teaminputobj.fullName = pfname;
        teaminputobj.phone = pn;
        teaminputobj.practiceName = currentpraticename;
        teaminputobj.orgid = orgid;
        teaminputobj.userType = currentusertype;
        teaminputobj.status = 'Y';
        teaminputobj.createDate = currenttime;
        teaminputobj.updateDate = currenttime;

        $.ajax({
            url: userapibase + '/api/v1/addteammember',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(teaminputobj),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");

            },
            success: function (response)
            {


                if (response.statuscode == 200) {
                    $.LoadingOverlay("hide");

                    //Send an email to team If user added successfully
                    var sendemailobj = {"useremail": temail, "mailcontent": temamemcomments}
                    $.ajax({
                        url: userapibase + '/api/v1/sendemailforteammember',
                        type: 'POST',
                        dataType: 'json',
                        headers: {
                            'Authorization': securitytoken,
                            'Content-Type': 'application/json'
                        },
                        Accept: "application/json",
                        data: JSON.stringify(sendemailobj),
                        beforeSend: function ()
                        {
                            $("#error").fadeOut();
                            $.LoadingOverlay("show");

                        },
                        success: function (response)
                        {
                            $("#addteammemModal").modal('hide');
                            $.toast({
                                heading: 'Add team',
                                text: 'Member created.',
                                textAlign: 'center',
                                position: 'top-center',
                                loader: false,
                                icon: 'success',
                                allowToastClose: false,
                                hideAfter: 5000,
                            });
                            location.reload();
                        }
                    });
                    //End of sending email

                }
                if (response.statuscode == 208) {
                    $.toast({
                        heading: 'Add team',
                        text: 'Email already taken.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'warning',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });
                }
                if (response.statuscode == 203) {
                    $("#addteammemModal").modal('hide');
                    $.toast({
                        heading: 'Add team',
                        text: 'Internal Server Error.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'warning',
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

});