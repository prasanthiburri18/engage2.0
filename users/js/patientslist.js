var practicenames = [];
var securitytoken = '';


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

		 $('#searchpatientname').keypress(function (event) {


		        var inputValue = (event.which) ? event.which : event.keyCode;
		        //event.preventDefault();
		        /*if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		        	event.preventDefault();
			    }*/
		        if(!(inputValue >= 65 && inputValue <= 120) && (inputValue != 32 && inputValue != 0)) { 
		            event.preventDefault(); 
		        }
		        
		 });
		
		 $('#praname').keypress(function (e) {

			 var valid = (e.which >= 48 && e.which <= 57) || (e.which >= 65 && e.which <= 90) || (e.which >= 97 && e.which <= 122 || e.which == 32|| e.which == 8);
		        if (!valid) {
		            e.preventDefault();
		        }
		      
		        
		 });
$(document).ready(function () {
	
	
    /**
     * Checking for token authentication on Page Load itself.
     * If not fouund then explicitly we are logging out
     * using logout functionality
     */
    var dataSet = [];
    $(".helpblock").css('display', 'none');
   /* if (sessionStorage.getItem("authtoken") != null)
    {
        var usertoken = sessionStorage.getItem("authtoken");
        var br = 'Bearer ';
        securitytoken = br.concat(usertoken);
    } else {
        logout();
        return;
    }*/

    var retrievedObject = sessionStorage.getItem('userinfo');
    var output = JSON.parse(retrievedObject);


    $(".lguser").html(output.fullName);
    var pdata = {"orgid": output.orgid};
    var plist = {"orgId": output.orgid};

/**
 * Loading the Pathway names for filter atuo select purpose
 * @input JsonObject
 * @return JsonObject
 *
 */

    $.ajax({
        url: pathwayapibase + '/api/v1/listPathway',
        type: 'POST',
        dataType: 'json',
        headers: {
          //  'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        xhrFields: {
            withCredentials: true
        },
        Accept: "application/json",
        data: JSON.stringify(plist),
        beforeSend: function (request)
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");
        },
        success: function (response)
        {

           if (response.statuscode == 200) {
               $.LoadingOverlay("hide");
                var practicenames = [];
                if (response.data.length > 0)
                {
                    $.each(response.data, function (pi, pd) {
                        practicenames.push(pd.pathwayName);
                    });
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

                }


            } else {

                $.LoadingOverlay("hide");
                $("#error").fadeIn(500, function () {


                });

            }
        },
        error: function(response, status){

          if(response.status==412) {
          $.LoadingOverlay("hide");
            logout();
          }

        }
    });
/**
 * Displaying the Patient list using organization
 * In this section we are calculating the patient pathway start days by
 * patient accepted date for particular Pathway
 *
 */
    $('#example').DataTable({
        "bProcessing": true,
        "order": [],
        "bSort": true,
        "data": dataSet,
        "columns":
                [
                    {"data": "patient.firstName"},
                    {"data": "eventInfo.createDate"},
                    {"data": "pathwayInfo.pathwayName"},
                    {"data": "eventInfo.eventName"},
                    {"data": "patient.updateDate"},
                    {"data": null},
                ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    var pname = row.firstName + " " + row.lastName;
                    var plink = '<p class="elementWordBreak2"><a href="viewpatient.html?patientid=' + row.id + '" style="color:#ec843e;">' + pname + '</a></p>';
                    return plink;
                },
                "targets": 0
            },
            {
                "render": function (data, type, row, meta) {
                	return row.daysSinceStart;
                  /*  var date1 = new Date();
                    if (row.pathwayInfo.data == null)
                    {
                        return '0';
                    }
                    var pthid = row.pathwayInfo.data.pathwayInfo.id;
                    var patientid = row.patient.id;



                    var patientpathwayinput = {"patientid": patientid, "pathwayid": pthid};
                    var patientacceptdate = false;
                    var currentCell = $("#example").DataTable().cells({"row": meta.row, "column": meta.col}).nodes(0);
                    $.ajax({
                        url: pathwayapibase + '/api/v1/checkforpatientpathwayacceptance',
                        type: 'POST',
                        dataType: 'json',
                        headers: {
                           // 'Authorization': securitytoken,
                            'Content-Type': 'application/json'
                        },
                        xhrFields: {
                            withCredentials: true
                        },
                        Accept: "application/json",
                        data: JSON.stringify(patientpathwayinput),
                        beforeSend: function ()
                        {
                            $("#error").fadeOut();
                            $.LoadingOverlay("show");
                        },
                        success: function (response)
                        {

                            $.LoadingOverlay("hide");

                            if (response.data != 'No Data')
                            {

                                var results = response.data;

                                patientacceptdate = true;
                                accpeteddate = response.data;

                                var date2 = new Date(accpeteddate);

                                var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                                var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

                                $(currentCell).html(diffDays);
                            } else
                            {

                                $(currentCell).html('0');
                            }


                        },
                        error: function(response, status){

                        	if(response.status==412 || response.status==401) {
                        	$.LoadingOverlay("hide");
                        		logout();
                        	}

                        }
                    });
*/


                   // return '0';
                },
                "targets": 1
            },
            {
                "render": function (data, type, row) {
                    if (row.pathwayName!= null||row.pathwayName!='')
                    {


                        var pathname = row.pathwayName;

                        var pthid = row.pathwayId;
                        var pathlink = '<a href="patientpathway.html?pathwayid=' + pthid + '&patientid=' + row.id + '" target="_blank" style="color:#ec843e;">' + pathname + '</a>';
                    } else
                    {
                        // var pathname = 'No Pathway';
                        var pathname = '-';
                        var pathlink = '<a href="#" style="color:#ec843e;">' + pathname + '</a>';
                    }

                    return pathlink;
                },
                "targets": 2
            },
            {
                "render": function (data, type, row) {
                    var ens = '';
                    if (row.eventNames!= null)
                    {
                        var events = row.eventNames;
/*                        $.each(evetns, function (evenindex, eninfo) {

                            ens += eninfo.eventName + ', ';

                        });*/
                        $.each(events,function(index, val){
                        	ens+= val+', ';
                        });

                    } else
                    {
                        var ens = 'No Events';
                    }
                    ens = ens.replace(/,\s*$/, "");
                    return ens;
                },
                "targets": 3
            },
            {
                "render": function (data, type, row) {

                    var actiondata = parseInt(row.updateDate) / 1000;
                    var formatted = moment.unix(actiondata).tz('America/New_York').format('LLL');

                    return formatted;
                },
                "targets": 4
            },
            {
                "render": function (data, type, row) {
                    var pedit = '<button class="btn btn-primary btn-xs" data-title="Edit"  onClick="patientEdit(\'' + row.id + '\')" ><span class="glyphicon glyphicon-pencil"></span></button>';
                    var pdelete = '<button class="btn btn-danger btn-xs" data-title="Delete" onclick="patientDelete(\'' + row.id + '\')";  ><span class="glyphicon glyphicon-trash"></span></button>';
                    var actiondata = pedit + ' ' + pdelete;
                    return actiondata;
                },
                "targets": 5
            }
        ]
    });



    $("div.dataTables_filter input").unbind();
    var oTable = $('#example').dataTable();

    $('#searchbtn').click(function (e) {

        var searchpatientname = $("#searchpatientname").val();
        var searchpathwayname = $("#praname").val();
        if (searchpatientname != '')
        {

            oTable.fnFilter(searchpatientname, 0);
        }
        if (searchpathwayname != '')
        {

            oTable.fnFilter(searchpathwayname, 2);
        }
        if (searchpathwayname == '' && searchpatientname == '')
        {

            oTable.fnFilterClear();


        }

    });

    $('#resetbtn').click(function (e) {
        $("#searchpatientname").val("");
        $("#praname").val("");

    });
/**
 * Loading the Patient List using OrganizationId
 * Rendering the list using Datatbales
 * @Input JsonObject
 * @return JsonObject
 *
 */
/*   $.ajax({
        url: patientapibase + '/api/v1/list_Patient',
        type: 'POST',
        dataType: 'json',
        headers: {
         //   'Authorization': securitytoken,
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

                dataSet = response.data;
                if (dataSet.length == 0)
                {
                    $(".helpblock").css('display', 'block');
                    $(".dashboardbox").css('display', 'none');

                }
                dataSet.sort(function (a, b) {
                    return (b.patient.id > a.patient.id) ? 1 : ((a.patient.id > b.patient.id) ? -1 : 0);
                });

                var myTable = $('#example').DataTable();
                myTable.clear().rows.add(dataSet).draw();


            } else {

                $.LoadingOverlay("hide");
                $("#error").fadeIn(500, function () {
                    $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                });

            }
        },
        error: function(response, status){

          if(response.status==412) {
          $.LoadingOverlay("hide");
            logout();
          }

        }
    });
*/


var orgid = output.orgid;

    $.ajax({
        url: patientapibase + '/api/v1/patients?orgId='+orgid,
        type: 'GET',
        //dataType: 'json',
        headers: {
         //   'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        xhrFields: {
            withCredentials: true
        },
        Accept: "application/json",

        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");

        },
        success: function (response,status)
        {
        	 $.LoadingOverlay("hide");

            if (status=="success"||status=="nocontent") {

                $.LoadingOverlay("hide");
             
                dataSet = response;
                if (dataSet==undefined || dataSet.length == 0)
                {
                    $(".helpblock").css('display', 'block');
                    $(".dashboardbox").css('display', 'none');
                    return;

                }
                dataSet.sort(function (a, b) {
                    return (b.id > a.id) ? 1 : ((a.id > b.id) ? -1 : 0);
                });

                var myTable = $('#example').DataTable();
                myTable.clear().rows.add(dataSet).draw();


            } else {

                $.LoadingOverlay("hide");
                $("#error").fadeIn(500, function () {
                    $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                });
	
            }
        },
        error: function(response, status){

          if(response.status==412) {
          $.LoadingOverlay("hide");
            logout();
          }

        }
    });


    

});



function patientEdit(id) {
    window.location.href = "editpatient.html?patientid=" + id;
}
function patientDelete(id) {
    sessionStorage.setItem("deletepatient", id);
    $("#psdelete").modal('show');
}
function patientdelete()
{
     $("#psdelete").modal('hide'); 
    var retrievedObject = sessionStorage.getItem('userinfo');
    var output = JSON.parse(retrievedObject);
    var priv = output.userType;
    if(priv!='A'){
         $.toast({
                    heading: 'Delete Patient',
                    text: 'You don\'t have enough privileges to delete a patient',
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });

        return false;
    }

   
    var pdid = sessionStorage.getItem("deletepatient");
    var data = {"id": pdid};

    $.ajax({
        url: patientapibase + '/api/v1/patient',
        type: 'DELETE',
        dataType: 'json',
        headers: {
         //   'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        xhrFields: {
            withCredentials: true
        },
        Accept: "application/json",
        data: JSON.stringify(data),
        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");

        },
        success: function (response)
        {

            if (response.statuscode == 200) {
                $.LoadingOverlay("hide");
                dataSet = response.data;
                window.location.reload();

            } else {

                $.LoadingOverlay("hide");
                $("#error").fadeIn(500, function () {
                    $("#error").html('<div class="alert alert-danger"> <span class="glyphicon glyphicon-info-sign"></span> &nbsp; ' + response.message + ' !</div>');

                });

            }
        },
        error: function(response, status){

          if(response.status==412) {
          $.LoadingOverlay("hide");
            logout();
          }

        }
    });
}


function logout() {
    sessionStorage.clear();

    var ajaxurl = baseurl+"/api/v1/logout";
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

    
    window.location.href = "index.html";
}


/*
function logout() {
    sessionStorage.clear();

    window.location.href = "index.html";
}*/
