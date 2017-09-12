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
 function logout(){
 localStorage.clear();
 window.location.href = "index.html";

                }
