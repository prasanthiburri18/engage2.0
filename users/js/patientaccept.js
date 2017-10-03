//var baseurl='https://engage-staging.quantifiedcare.com/ApiGateway/';
//var baseurl='https://engage-staging.quantifiedcare.com:8443/';
var baseurl='http://35.166.195.23:8080/ApiGateway/';
var microservice='patient';
//var microservice='PatientMicroservice';
var apibase=baseurl+microservice;
var pathwaybasapi=baseurl+'pathway';
//var pathwaybasapi=baseurl+'PathwayMicroservice';

$('document').ready(function ()
{

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

 var pfrom = getUrlParameter('From');
 var pbody = getUrlParameter('Body');
 
 pfrom=pfrom.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
   
    var inputs={"phone":pfrom,"accepteddate":"2015-03-31","accept":pbody};
    
    
    });