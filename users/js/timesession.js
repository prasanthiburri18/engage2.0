localStorage.setItem("idtime", 0);
var idleTime = localStorage.getItem("idtime");
$(document).ready(function () {
    var idleInterval = setInterval(timerIncrement, 60000); // 1 minute 60000
    $(this).mousemove(function (e) {
    	localStorage.setItem("idtime", 0);
        idleTime = localStorage.getItem("idtime");
       
    });
    $(this).keypress(function (e) {
    	localStorage.setItem("idtime", 0);
        idleTime = localStorage.getItem("idtime");
        
    });
});

function timerIncrement() {
    
    
    idleTime = parseInt(localStorage.getItem("idtime")) + 1;
    localStorage.setItem("idtime", idleTime);
    console.log(idleTime);
    if (idleTime >= 15) { 
        logout();
    }
}
/* function logout(){
 sessionStorage.clear();
 window.location.href = "index.html";

                }*/


function logout() {
    sessionStorage.clear();
    localStorage.clear();
    var ajaxurl = baseurl+"userlogout";
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

            window.location.href = "index.html";
        },
        error: function(response, status){


            window.location.href = "index.html";
        }

    });

}


