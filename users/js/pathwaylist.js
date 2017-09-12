var orgid = 0;
var usertype = '';
$(document).ready(function () {

    var dataSet = [];
    $(".helpblock").css('display', 'none');
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
    usertype = output.userType;
    if (usertype == 'U')
    {

    $('.addpathwaybtn').css('display', 'none');
    }


    $(".lguser").html(output.fullName);
    var pdata = {"orgId": orgid};
    var practicenames = ['Hyper tension', 'Myocardical Infraction', 'Heart Failure'];
    var practicenames = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        local: practicenames
    });
    $("#pathwaydatemodified").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        maxDate: '0',
        Clear: true,
        buttonText: "Select date",
        dateFormat: "yy-mm-dd",
        constrainInput: false
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



    jQuery.extend(jQuery.fn.dataTableExt.oSort, {
        "formatted-num-pre": function (a) {
            a = (a === "-" || a === "") ? 0 : a.replace(/[^\d\-\.]/g, "");
            return parseFloat(a);
        },
        "formatted-num-asc": function (a, b) {
            return a - b;
// return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },
        "formatted-num-desc": function (a, b) {
            return b - a;
//return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        }
    });
    /**
     *Rendering the PathwayList for orgganization
     *using Datatable.
     *@Input JsonObject
     *@return {JsonObject}
     * 
     * 
     * 
     */
    $('#example').DataTable({
        "bProcessing": true,
        "order": [],
        "data": dataSet,
        "columns":
                [
                    {"data": "pathwayName"},
                    {"data": "pathwayName"},
                    {"data": "pathwayName"},
                    {"data": "updateDate"},
                    {"data": null},
                ],
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    var pname = row.pathwayName;
                    if (usertype == "U")
                    {
                        var plink = pname;
                    }
                    if (usertype == "A")
                    {
                        var plink = '<a href="editpathway.html?pathwayid=' + row.id + '" target="_blank" style="color:#ec843e;">' + pname + '</a>';
                    }

                    return plink;
                },
                "targets": 0
            },
            {
                "render": function (data, type, row) {
                    var eventslistpaths = row.events;

                    eventslistpaths.sort(function (a, b) {
                        return a.id - b.id;
                    });
                    var eventnamehtml = '';
                    if (eventslistpaths.length > 0)
                    {
                        $.each(eventslistpaths, function (index, eventval) {
                            var eventnn = '<span style="font-weight:bold">' + eventval.eventName + '</span>';
                            eventnamehtml += eventnn + ', ';

                        });
                    } else
                    {
                        eventnamehtml = 'No Events';
                    }
                    eventnamehtml = eventnamehtml.replace(/,\s*$/, "");

                    return eventnamehtml;
                },
                "targets": 1
            },
            {
                "render": function (data, type, row, meta) {



                    var pinfo = {"pathwayId": row.id};

                    var num = 0;
                    var currentCell = $("#example").DataTable().cells({"row": meta.row, "column": meta.col}).nodes(0);
                    $.ajax({
                        url: patientapibase + '/api/v1/getPatientsCountByPathwayId',
                        type: 'POST',
                        dataType: 'json',
                        headers: {
                            'Authorization': securitytoken,
                            'Content-Type': 'application/json'
                        },
                        Accept: "application/json",
                        data: JSON.stringify(pinfo),
                        beforeSend: function (request)
                        {
                            $("#error").fadeOut();
                            $.LoadingOverlay("show");


                        },
                        success: function (response)
                        {

                            if (response.statuscode == 200) {
                                $.LoadingOverlay("hide");

                                num = response.data;
                                $(currentCell).html(num);
                            }
                        }
                    });
                    return 'Not Started';
                },
                "targets": 2
            },
            {type: 'formatted-num', targets: 2},
            {
                "render": function (data, type, row) {
                    var formatted = moment(row.updateDate).tz('America/New_York').format('LLL');
                    return formatted;
                },
                "targets": 3
            },
            {
                "render": function (data, type, row) {
                    if (usertype == "U")
                    {
                        var pedit = '<button class="btn btn-primary btn-xs" data-title="Edit" disabled  onClick="pathwaytEdit(\'' + row.id + '\')" ><span class="glyphicon glyphicon-pencil"></span></button>';
                        var pdelete = '<button class="btn btn-danger btn-xs" data-title="Delete" disabled onclick="pathwayDelete(\'' + row.id + '\')";  ><span class="glyphicon glyphicon-trash"></span></button>';
                    }
                    if (usertype == "A")
                    {

                        var pedit = '<button class="btn btn-primary btn-xs" data-title="Edit"   onClick="pathwaytEdit(\'' + row.id + '\')" ><span class="glyphicon glyphicon-pencil"></span></button>';
                        var pdelete = '<button class="btn btn-danger btn-xs" data-title="Delete"  onclick="pathwayDelete(\'' + row.id + '\')";  ><span class="glyphicon glyphicon-trash"></span></button>';
                    }

                    var actiondata = pedit + ' ' + pdelete;
                    return actiondata;
                },
                "targets": 4
            }
        ]
    });


    $("div.dataTables_filter input").unbind();
    var oTable = $('#example').dataTable();

    $('#resetbtn').click(function (e) {
        $("#searchpathwayname").val("");
        $("#pathwaydatemodified").val("");
    });

    $('#searchbtn').click(function (e) {
        var searchpathwayname = $("#searchpathwayname").val();
        var searchpathwaymodified = $("#pathwaydatemodified").val();
        if (searchpathwayname != '' && searchpathwaymodified == '')
        {
            oTable = $('#example').dataTable();
            oTable.fnFilter(searchpathwayname, 0);
        }
        if (searchpathwaymodified != '' && searchpathwayname == '')
        {
            oTable = $('#example').dataTable();
            var formatteds = moment(searchpathwaymodified).format('LL');
            //formatteds = moment(searchpathwaymodified).format('LL');
            oTable.fnFilter(formatteds, 3);
        }
        if (searchpathwaymodified != '' && searchpathwayname != '')
        {
            oTable = $('#example').dataTable();
            var formatteds = moment(searchpathwaymodified).format('LL');
            //formatteds = moment(searchpathwaymodified).format('LL');
            oTable.fnFilter(searchpathwayname, 0);
            oTable.fnFilter(formatteds, 3);
        }
        if (searchpathwayname == '' && searchpathwaymodified == '')
        {
            oTable.fnFilterClear();
        }

    });


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
                if (dataSet.length == 0)
                {
                    $(".helpblock").css('display', 'block');
                    $(".dashboardbox").css('display', 'none');

                }

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









});
function pathwaytEdit(id) {

    var hlink = "editpathway.html?pathwayid=" + id;
    window.open(hlink, '_blank');
}
function pathwaytDelete(id) {

    localStorage.setItem("deletepathwayid", id);

    $("#psdelete").modal('show');
}


function logout() {


    localStorage.clear();
    window.location.href = "index.html";

}