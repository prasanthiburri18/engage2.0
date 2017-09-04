/**
 * 
 * @type String|response
 * User Registration Js file
 * In this file we are making the user registration
 * Loading the all organization names which are getting displayed
 * in registration form.
 * After making user successful registration the system will send 
 * an email to user then the user  need to click on verify email
 * for user active. Otherwise the user would be in inactive mode only.
 * 
 */
var correctCaptchares = '';
var correctCaptcha = function (response) {
    if (response.length == 0)
    {
        alert('Recaptcha error');
        return false;
    } else
    {
        correctCaptchares = response;
        return true;
    }
};

var currentdate = new Date();
var currenttime = currentdate.getTime();
$('document').ready(function ()
{


    var datat = {};



    $.ajax({
        url: userapibase + '/getAllPraticeNames',
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
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
                var practicenames = response.data.praticenames;


                var practicenames = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.whitespace,
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: practicenames
                });


                $('.typeahead').typeahead({
                    hint: true,
                    highlight: true,
                    minLength: 1
                },
                        {
                            name: 'practicenames',
                            source: practicenames
                        });

                $.LoadingOverlay("hide");

            } else {

                $.LoadingOverlay("hide");


            }
        }
    });


    $.validator.addMethod("passcheck", function (value, element, regexpr) {
        return regexpr.test(value);
    }, "Password Criteria:<br/>8 to 15 characters<br/>At least one lowercase letter<br/>At least one uppercase letter<br/>At least one numeric digit<br/>At least one special character");

    $("#signupform").validate({
        ignore: ".ignore",
        rules:
                {
                    practicename: {
                        required: true,
                    },
                    username: {
                        required: true,
                    },
                    useremail: {
                        required: true,
                        email: true

                    },
                    userpassword: {
                        required: true,
                        passcheck: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,15}$/,
                    },
                    "hiddenRecaptcha": {
                        required: function () {
                            if (grecaptcha.getResponse() == '') {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }

                },
        messages:
                {
                    username: {
                        required: "Please enter your name."
                    },
                    practicename: {
                        required: "Please enter your parctice name."
                    },
                    hiddenRecaptcha: {
                        required: 'Please check the Captcha.'
                    },
                    userpassword: {
                        required: "Please enter your password."
                    },
                    useremail: "Please enter your email address.",
                },
        submitHandler: SignupsubmitForm
    });
    function SignupsubmitForm()
    {


        var practicename = $("#praname").val();
        var username = $("#username").val();
        var useremail = $("#useremail").val();
        var userpassword = $("#userpassword").val();



        var singupfrmdata = {
            "password": userpassword,
            "fullName": username,
            "practiceName": practicename,
            "email": useremail,
            "phone": "00000000",
            "createDate": currenttime,
            "updateDate": currenttime}



        $.ajax({
            url: userapibase + '/registration',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json",
            Accept: "application/json",
            data: JSON.stringify(singupfrmdata),
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
                    setTimeout('window.location.href = "signthankyou.html"; ', 2000);

                }
                if (response.statuscode == 208)
                {
                    $.LoadingOverlay("hide");

                    $("#emailexistswarn").css('display', 'block');
                    $.toast({
                        heading: 'Registration',
                        text: response.message,
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'error',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });


                }
                if (response.statuscode == 203)
                {
                    $.LoadingOverlay("hide");
                    $.toast({
                        heading: 'Registration',
                        text: response.message,
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

});

