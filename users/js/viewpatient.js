var patientfname='';
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

 var pid = getUrlParameter('patientid');
   
    
    var pdata={};
       pdata.id=pid;
       var pathwayid=0;
       var pathwayevents=[];
/**
 * Checking for token authentication on Page Load itself.
 * If not fouund then explicitly we are logging out
 * using logout functionality
 */
if(localStorage.getItem("authtoken")!=null)
{
 var usertoken=localStorage.getItem("authtoken");
 var br='Bearer ';
  var securitytoken = br.concat(usertoken);
}
else{
    logout();
   return; 
}



var retrievedObject = localStorage.getItem('userinfo');
var output=JSON.parse(retrievedObject);


$(".lguser").html(output.fullName);
var dataSet=[];
/**
 * Load patient using patient Id
 * @Input JsonObject
 * @return JsonObjects
 * 
 */

 /**
     * 
     * @returns {JsonObject}
     * View PatientPathway
     * Loading the Master Pathway which is associated with patient
     * After that checking for patient acceptance in case of patient
     * not accepted then showing the Maste Pathway Information only
     * If the Patient Get Accepted then loading the child pathway information
     * And Displaying the message status along with delivery dates table.
     * 
     * 
     */
$.ajax({
            url: patientapibase+'/api/v1/view_Patient',
            type: 'POST',
            dataType: 'json',
            headers: {
        'Authorization':securitytoken,
        'Content-Type':'application/json'
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
                
                 $.LoadingOverlay("hide");
                 
                 if(response.data!=null)
                 {
                     var results=response.data;
                     

var name=results.patient.firstName+' '+results.patient.lastName;
patientfname=results.patient.firstName;
                     $("#pname").html(name);
                     
                     //Making Phone Substring
                     
                     var phinput =results.patient.phone;

                    var displayphinput=phinput.substring(0,3)+'-'+phinput.substring(3,6)+'-'+phinput.substring(6,10);
                     
                     $("#pphone").html(displayphinput);
                       var formatted = moment(results.patient.dob).format('L');
                     $('#pdob').html(formatted);
                     $("#pathwayname").html(results.pathwayInfo.data.pathwayInfo.name);
                     pathwayid=results.pathwayInfo.data.pathwayInfo.id;
                     
                     var ppin={"pathwayId":pathwayid};
               
                     $.ajax({
            url:pathwayapibase+'/api/v1/listEvents',
            type: 'POST',
            dataType: 'json',
            headers: {
        'Authorization':securitytoken,
        'Content-Type':'application/json'
    },

            Accept: "application/json",
            data: JSON.stringify(ppin),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");

            },
            success: function (presponse)
            {
                  $.LoadingOverlay("hide");
                
                pathwayevents=presponse.data;

            }
           });     

                     $.each(results.pathwayInfo.data.eventInfo,function(evetindex,evenval){
                        
                      
                                  var eventnameshtml='';
                                var totalcount=0;
                                var deliveredcount=0;
                                var eventcountinput={"patientid":pid,"pathwayid":pathwayid,"eventid":evenval.id};
                                
                         
                                
                                $.ajax({
            url: pathwayapibase+'/api/v1/getPatientpathwayblockbyevent',
            type: 'POST',
            dataType: 'json',
            headers: {
        'Authorization':securitytoken,
        'Content-Type':'application/json'
    },

            Accept: "application/json",
            data: JSON.stringify(eventcountinput),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
            $.LoadingOverlay("hide");
           
           var eventblocks=response.data;
          
           
           if(eventblocks.length > 0)
           {


                  
                  $.each(eventblocks,function(bi,be){
                 
                      if(be.block_type=='A' && be.block_parent_id==0)
                      {
                          //Ignore
                      }
                      else
                      {
                         

                          if(be.message_status=="sent")
                          {
                             deliveredcount=deliveredcount+1; 
                          }
                         
                          totalcount=totalcount+1;
                      }
                      
                  });
                  if(totalcount==deliveredcount)
                  eventnameshtml+='<div class="col-lg-4 col-md-4 col-sm-4 HF-list active">';
              else
                   eventnameshtml+='<div class="col-lg-4 col-md-4 col-sm-4 HF-list">';
                  eventnameshtml+='<h4>'+evenval.eventName+'</h4>';
                  eventnameshtml+='<p>Delivered : '+deliveredcount+' of '+totalcount+'</p>';
                  eventnameshtml+='</div>';
                  $("#eventslist").append(eventnameshtml);
           }
           
        }
        });

                                
                                
                           
                         
                     });
                     
                     
var phinput =results.patient.phone;
 phinput = phinput.replace(/\\D/g, " ").split("-");

 $("#editphone1").val(phinput[0]);
 $("#editphone2").val(phinput[1]);
 $("#editphone3").val(phinput[2]);

  
  var painp={"patientid":pid,"pathwayid":pathwayid}
        $.ajax({
            url: pathwayapibase+'/api/v1/getPatientpathway',
            type: 'POST',
            dataType: 'json',
            headers: {
        'Authorization':securitytoken,
        'Content-Type':'application/json'
    },

            Accept: "application/json",
            data: JSON.stringify(painp),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                 $.LoadingOverlay("hide");
                 var jsonlist=[];
                 $.each(response.data,function(i,row){
                  
                     if(row.message_status=='sent')
                     {
                       jsonlist.push(row);  
                     }
                 });
                 
                 dataSet=jsonlist;
               
                                 var myTable = $('#example').DataTable();
myTable.clear().rows.add(dataSet).draw();
        }
        });


                 }
                
               
            }
        });


 
  var SearcheventTag = function (eventid) {
            var i = null;
          
            for (i = 0; pathwayevents.length > i; i += 1) {
           
                if (pathwayevents[i].id === eventid) {
                    return pathwayevents[i];
                }
            }

            return null;
        };
        
$('#example').DataTable({
    
    "bProcessing": true,
    "order": [],
    "data": dataSet,
    "columns": 
    	[
    	{"data": "body_of_message"}, 
      	{"data": null},
        {"data": null},
        {"data": "message_status"},
        {"data": null},
        
    	],
         "columnDefs": [
                {
                    "render": function (data, type, row) {
                        var pname='No Message';
                        if(row.block_type=='M')
                        {
                         pname=row.body_of_message;
                    }
                    if(row.block_type=='A')
                        {
                    if(row.followup_of_message!='no message')
                         pname=row.followup_of_message;
                    if(row.remainder_of_message!='no message')
                         pname=row.remainder_of_message;
                    }
                        var plink='<a href="#" style="color:#ec843e;">More</a>';
                       

                        var dd =pname;
                        
                       if(row.block_name=="Sign Up")
                       {
                    dd = dd.replace("#FN", patientfname);
                        return dd;
                    }
                   return dd;
                    },
                    "targets":0
                },
                  {
                    "render": function (data, type, row) {
                        var date1 = new Date();
var date2 = new Date(row.patient_accepted_date);
var timeDiff = Math.abs(date2.getTime() - date1.getTime());
var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 

                        return diffDays;
                    },
                    "targets":1
                },
                 {
                    "render": function (data, type, row) {
                        var tag = SearcheventTag(row.event_id);
                      
        if (tag)
        {
            return tag.eventName;
        }
                        return 'No Event';
                    },
                    "targets":2
                },
                 {
                    "render": function (data, type, row) {
//                        var date = new Date(row.msenttime);
                        var formatted = moment.unix(row.msenttime/1000).format('LLL');
                        return formatted;
                        
                    },
                    "targets":3
                },
                {
                    "render": function (data, type, row) {
                        return "SMS";
                    },
                    "targets":4
                }
         ]
        
  });
  
  
  
  
} );