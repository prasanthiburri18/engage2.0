$('document').ready(function ()
{

    $("#login-form").validate({
        rules:
                {
                    password: {
                        required: true,
                    },
                    user_email: {
                        required: true,
                        email: true
                    },
                },
        messages:
                {
                    password: {
                        required: "please enter your password."
                    },
                    user_email: "please enter your email address.",
                },
        submitHandler: submitForm
    });
    /**
     * @Input Json Object
     * @returns {Json Object}
     * User Login System
     * After user sucessfull login the 
     * we are storing the user details in local storage for
     * further operations and this local storgae will get
     * cleared for i5 mins in case of app inactivity.
     * 
     * 
     */
    function submitForm()
    {

        var useremail = $("#user_email").val();
        var pass = $("#password").val();
        var datat = {"email": useremail, "password": pass};

        var ajaxurl = userapibase + '/login';
        $.ajax({
            url: ajaxurl,
            type: 'POST',
            dataType: 'json',
            "async": true,
            "crossDomain": true,
            contentType: 'application/json; charset=UTF-8',
            Accept: "application/json",
            data: JSON.stringify(datat),
            beforeSend: function ()
            {

                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {



                if (response.data != null) {

                    localStorage.setItem('userinfo', JSON.stringify(response.data.UserBacsicInfo));

                    setUser(response.data.UserBacsicInfo);
                    setToken(response.data.token);

                    setTimeout(' window.location.href = "patientslist.html"; ', 1000);
                    $.LoadingOverlay("hide");

                } else {

                    $.LoadingOverlay("hide");
                    $("#error").fadeIn(500, function () {
                        $("#error").html('<div class="alert alert-danger">Incorrect Email/Pasword combination. Please try again</div>');

                    });

                }
            }
        });
        return false;
    }


    $("#forgotpassword-form").validate({
        rules:
                {
                    forgotpassemail: {
                        required: true,
                        email: true

                    }

                },
        messages:
                {
                    forgotpassemail: "please enter your email address",
                },
        submitHandler: forgotsubmitForm
    });
    function forgotsubmitForm()
    {


        var email = $("#forgotpassemail").val();

        var forgotpassfrmdata = {
            "emailid": email
        };

        /**
         * 
         * Forgot Password API call
         * 
         */

        $.ajax({
            url: userapibase + '/forget_password',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(forgotpassfrmdata),
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
                    $("#ajaxmessage").css('display', 'block');

                }
                if (response.statuscode == 208)
                {

                    $.LoadingOverlay("hide");
                    $("#ajaxerr").html(response.message);


                }

                if (response.statuscode == 204)
                {

                    $.LoadingOverlay("hide");
                    $("#ajaxerr").html(response.message);


                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.LoadingOverlay("hide");

            }
        });
        return false;
    }
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
//            cfmpassword: "please enter correct text",
//            newpassword: "please enter correct text",
//                },
                    cfmpassword: {
                        required: "Please enter your password."
                    },
                    newpassword: {
                        required: "Please enter your password."
                    },
                },
        submitHandler: changepasswordsubmitForm
    });
    function changepasswordsubmitForm()
    {

        var cp = localStorage.getItem("changepasswordkey");

        var changepassstr = new Object();
        changepassstr.emailid = cp;

        changepassstr.password = $('#newpassword').val();
        var inputstring = changepassstr;


        $.ajax({
            url: userapibase + '/verify_emailForgetpwd',
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

                if (response.statuscode == 200)
                {
                    $("#changepass").css('display', 'block');
                    setTimeout('window.location.href = "index.html"; ', 500);

                } else
                {
                    $("#changepasserr").css('display', 'block');

                }

            }
        });

        return false;
    }


});

function setToken(token) {


    localStorage.setItem("authtoken", token);
}
function getToken() {

    var authtoken = localStorage.getItem("authtoken");
    return authtoken;
}
function setUser(userdata) {
    localStorage.setItem("userdata", JSON.stringify(userdata));
}
function getUser() {

    var userdata = localStorage.getItem("userdata");
    return userdata;
}
