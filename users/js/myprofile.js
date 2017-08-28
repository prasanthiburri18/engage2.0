var output = '';
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
        var securitytoken = br.concat(usertoken);

        var retrievedObject = localStorage.getItem('userinfo');
        output = JSON.parse(retrievedObject);

        $("#fullname").val(output.fullName);
        $(".lguser").html(output.fullName);
        $("#email").val(output.email);
        $("#orgname").val(output.practiceName);

        var ph = output.phone;

        if (ph != "0000000000")
        {
            $("#phone1").val(ph.substring(0, 3));
            $("#phone2").val(ph.substring(3, 6));
            $("#phone3").val(ph.substring(6, 10));
        } else {
            $("#phone1").val("000");
            $("#phone2").val("000");
            $("#phone3").val("0000");
        }
    } else {
        logout();
        return;
    }

    $("#changepasswordmodalevt").click(function () {

        $("#changepasswordmodal").modal('show');
    });
// $(".cancel").click(function() {
//	 
//            return;
//		});
//Change Password frm submission process

    $.validator.addMethod("passcheck", function (value, element, regexpr) {
        return regexpr.test(value);
    }, "Password Criteria:<br/>8 to 15 characters<br/>At least one lowercase letter<br/>At least one uppercase letter<br/>At least one numeric digit<br/>At least one special character");
    $("#changepassword-form").validate({
        rules:
                {
                    newpassword: {
                        required: true,
                        passcheck: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,15}$/,
                    },
                    password_again: {
                        equalTo: "#newpassword"
                    }

                },
        messages:
                {
                    cfmpassword: "please enter correct text",
                    newpassword: "please enter correct text",
                },
        submitHandler: changepasswordsubmitForm
    });
    /**
     * 
     * Change Password for Logged in User
     * @Input Json Object
     * @return {Json Object} 
     * 
     */
    function changepasswordsubmitForm()
    {


        var cp = localStorage.getItem("changepasswordkey");

        var changepassstr = new Object();
        changepassstr.userid = output.id;

        changepassstr.password = $('#newpassword').val();
        var inputstring = changepassstr;


        $.ajax({
            url: userapibase + '/api/v1/change_password',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(inputstring),
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                $.LoadingOverlay("hide");

                if (response.statuscode == 200)
                {
                    $("#changepasswordmodal").modal('hide');
                    $.toast({
                        heading: 'Change Password',
                        text: 'Password changed successfully.',
                        textAlign: 'center',
                        position: 'top-center',
                        loader: false,
                        icon: 'success',
                        allowToastClose: false,
                        hideAfter: 5000,
                    });

                } else
                {
                    $("#changepasswordmodal").modal('hide');
                    $.toast({
                        heading: 'Change Password',
                        text: 'Password updation failed.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'error',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });

                }

            }
        });

        return false;
    }


//change Password frm submission process ends here




    $('.myphone').keypress(function (event) {



        var charCode = (event.which) ? event.which : event.keyCode;
//    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
//    {
//        event.preventDefault();
//    }
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

    /**
     * User Profile Update functionality
     * @Input JsonObject
     * @return {JsonObject}
     * After succesfull validation the user profile get updated
     * using backed QC API Call and return the JsonObject as Output.
     * 
     * 
     */


    $("#userprofile-form").validate({
        rules:
                {
                    fullname: {
                        required: true,
                    },
                    email: {
                        required: true,
                        email: true

                    },
                    orgname: {
                        required: true,
                    },
                },
        messages:
                {
                    fullname: {
                        required: "Please enter  name."
                    },
                    email: {
                        required: "Please enter email.",
                    },
                    orgname: {
                        required: "Please enter practice name."
                    },
                },
        submitHandler: updateuserprofilrsubmitForm
    });

    function updateuserprofilrsubmitForm()
    {
        var fullname = $("#fullname").val();
        var email = $("#email").val();
        var orgname = $("#orgname").val();


        var phone_aval = $('#phone1').val();
        var phone_bval = $('#phone2').val();
        var phone_cval = $('#phone3').val();

        var pnum = phone_aval.toString() + phone_bval.toString() + phone_cval.toString();
        var pn = pnum;

        var userObj = output;

        userObj.fullName = fullname;
        userObj.email = email;
        userObj.practiceName = orgname;
        userObj.phone = pn;




        $.ajax({
            url: userapibase + '/api/v1/updateprofile',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(userObj),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {

                if (response.statuscode == 200)
                {
                    $.LoadingOverlay("hide");
                    localStorage.setItem("userinfo", JSON.stringify(userObj));



                    $.toast({
                        heading: 'My Account',
                        text: 'Your profile has been updated successfully.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'success',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });


                }
            }
        });
    }


});