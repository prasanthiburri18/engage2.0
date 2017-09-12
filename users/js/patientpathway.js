/**
 * 
 * @type String|response.data|accpeteddate
 * Patients Child Pathway Operations
 * Update Message
 * Set appointment dates
 * View message dates and delivered messages..etc
 * 
 * 
 * 
 */
var stdate = '';
function getObjects(obj, key, val) {
    var objects = [];
    for (var i in obj) {
        if (!obj.hasOwnProperty(i))
            continue;
        if (typeof obj[i] == 'object') {
            objects = objects.concat(getObjects(obj[i], key, val));
        } else if (i == key && obj[key] == val) {
            objects.push(obj);
        }
    }
    return objects;
}
if (typeof (Number.prototype.isBetween) === "undefined") {
    Number.prototype.isBetween = function (min, max, notBoundaries) {
        var between = false;
        if (notBoundaries) {
            if ((this < max) && (this > min))
                between = true;
        } else {
            if ((this <= max) && (this >= min))
                between = true;

        }

        return between;
    }
}
var patientevents = [];
var pagenos = [
    {startp: 0, endp: 0},
    {startp: 1, endp: 5},
    {startp: 6, endp: 10},
    {startp: 11, endp: 15},
    {startp: 16, endp: 20},
    {startp: 21, endp: 25},
    {startp: 26, endp: 30},
    {startp: 31, endp: 35},
    {startp: 36, endp: 40},
    {startp: 41, endp: 45},
    {startp: 46, endp: 50},
    {startp: 51, endp: 55},
    {startp: 56, endp: 60},
    {startp: 61, endp: 65},
    {startp: 66, endp: 70},
    {startp: 71, endp: 75},
    {startp: 76, endp: 80},
    {startp: 81, endp: 85},
    {startp: 86, endp: 90},
    {startp: 91, endp: 95},
    {startp: 96, endp: 100}

];


function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = hours + ':' + minutes + ' ' + ampm;
    return strTime;
}

function filluprows(trtdlast, twlen) {
    trtdlast.find("td").each(function (innerlen, val) {



        if ($(this).find("div").hasClass("addblock")) {

            var $this = $(this);
            var ii = $(this).index();

            for (var ks = 1; ks <= $(this).index(); ks++)
                var rid = twlen + '-' + ks + '-blockcell';
            var drid = twlen + '-' + ks;

            $(this).find('div.addblock').attr('id', rid);
            $(this).find('div.addblock').data('rowcol', drid);
            $(this).find('div.addblock').children().remove();
        }


    });
    return false;
}

(function ($) {
    $.rand = function (arg) {
        if ($.isArray(arg)) {
            return arg[$.rand(arg.length)];
        } else if (typeof arg === "number") {
            return Math.floor(Math.random() * arg);
        } else {
            return 'color1';
        }
    };
})(jQuery);

var cuurentfilledeventcolpos = 0;
var $saveblocks = [];
var $allvents = [];
var $eventcolorarr = ['color2', 'color3'];
var eventcolor = 'color2';
var $currentcolor = 'color2';
var currentblockforupdate = {};
var currentblockforupdateid = 0;
var $createdevents = [];
var createdblocks = [];
var $previoushtmlarr = ['nohtml'];
var $currentstoredhtml = '';
var tablecols = 0;
var $currentpage = 1;
var $perviouspage = 0;
var $next = false;
var otblethlength = 5;
var prhtml = [];
var $nextpage = 1;
var eventscolsarrhederlist = [1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100];
var pathswaysdata;

var blocktriggersoptions = [
    {
        "name": "No Trigger",
        "day": 0
    }

];


var securitytoken = '';
/**
 * Checking for token authentication on Page Load itself.
 * If not fouund then explicitly we are logging out
 * using logout functionality
 */

if (localStorage.getItem("authtoken") != null)
{
    var usertoken = localStorage.getItem("authtoken");
    var br = 'Bearer ';
    securitytoken = br.concat(usertoken);
} else {
    logout();

}

var retrievedObject = localStorage.getItem('userinfo');
var output = JSON.parse(retrievedObject);






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
var pid = getUrlParameter('pathwayid');
var patientid = getUrlParameter('patientid');

var pdata = {"id": pid};
var ptdata = {};
ptdata.id = patientid;

var maxRowNumber = 0;
var maxColNumber = 0;
var accpeteddate = '';
$blocktype = 'M';

var patientpathwayinput = {"patientid": patientid, "pathwayid": pid};
var patientacceptdate = false;

$('document').ready(function ()
{
    if (navigator.userAgent.indexOf('Mac') > 0)
    {
        $('body').addClass('mac-os');
    }
    $(".lguser").html(output.fullName);
    var lastmodifiedtime = formatAMPM(new Date());
    lastmodifiedtime = 'Last saved ' + lastmodifiedtime;
    $("#lastsavedtime").html(lastmodifiedtime);
    $("#exitpathway").click(function () {
        window.close();
    });
    $("#savepathway").click(function () {

        $.toast({
            heading: 'Add Pathway',
            text: 'Pathway created successfully.',
            textAlign: 'center',
            position: 'top-center',
            loader: false,
            icon: 'success',
            allowToastClose: false,
            hideAfter: 5000,
        });



        var lastmodifiedtime = formatAMPM(new Date());
        lastmodifiedtime = 'Last Save at ' + lastmodifiedtime;
        $("#lastsavedtime").html(lastmodifiedtime);
    });



    /**
     * Pagination logic flow start here
     * We are implementing pagination for Previous  and next buttons
     * The click event will check for display five cols at a time
     * and it includes the calculation like
     * page 1= 1- 5 cols
     * ppage 2= 6-10 cols....etc
     * Each and every time we are creating the virtual columns in case of next operations
     * the system will check for the event col position and row postions
     * and will get displayed according to that in the table.
     * In case of no events for next page operations then system will
     * get fillvent event 1,event 2....etc with repect to page no's.
     * The color schema is applied based on the columns 1 to 5
     * and this will get repeated page wise up to the lats  Pathway Event column Number.
     * 
     * 
     * 
     */
    $("#previouscols").click(function () {


        $currentpage = $currentpage - 1;
        if ($currentpage == 1)
        {

            $("#previouscols").prop('disabled', true);
            $("#previouscols").css('background-color', '#d5cece');
        }
        var displayrange = pagenos[$currentpage];


        otblethlength = otblethlength + 1;

        for (var kl = 2; kl <= otblethlength; kl++)
        {

            var myNumber = kl;
            try {

                if (myNumber.isBetween((1 + parseInt(displayrange.startp)), (1 + parseInt(displayrange.endp))))
                {
                    $("table tr th:nth-child(" + (kl) + "), table tr td:nth-child(" + (kl) + ")").show();
                } else
                {
                    $("table tr th:nth-child(" + (kl) + "), table tr td:nth-child(" + (kl) + ")").hide();
                }
            } catch (e) {

                $.toast({
                    heading: 'Add Pathway',
                    text: e.message(),
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'warning',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });
            }
        }
        return;
    });

    $("#previouscols").prop('disabled', true);
    $("#previouscols").css('background-color', '#d5cece');
    $("#nextcols").click(function () {


        $("#previouscols").prop('disabled', false);
        $("#previouscols").css('background-color', '');


        $next = true;

        var tlgth = $("table tr th").length;
        var previouspage = $currentpage;
        $currentpage = $currentpage + 1;

        var displayrange = pagenos[($currentpage)];

        if (tablecols == 0)
            tablecols = 5;


        if (tlgth < parseInt(displayrange.endp))
        {

            var renderupto = (parseInt($currentpage) * 5) + 1;

            for (var emptr = (tlgth); emptr < renderupto; emptr++)
            {
                $('table').find('tr').each(function () {

                    $(this).find('th').eq(-1).after('<th>Event ' + (emptr) + ' </th>');
                    $(this).find('td').eq(-1).after('<td></td>');
                });
                tablecols = tablecols + 1;
            }
            tlgth = parseInt(displayrange.endp);
            otblethlength = tlgth;
        }



        for (var kl = 2; kl <= (parseInt(displayrange.endp) + 1); kl++)
        {

            var myNumber = kl;
            try {

                if (myNumber.isBetween((1 + parseInt(displayrange.startp)), (1 + parseInt(displayrange.endp))))
                {

                    $("table tr th:nth-child(" + (kl) + "), table tr td:nth-child(" + (kl) + ")").show();
                } else
                {

                    $("table tr th:nth-child(" + (kl) + "), table tr td:nth-child(" + (kl) + ")").hide();
                }
            } catch (e) {
                $.toast({
                    heading: 'Add Pathway',
                    text: e.message(),
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'warning',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });
            }
        }

        return;


    });

    $("#pathwaydelivery button").attr('disabled', 'disabled');
    $("#blocktrigger").on('change', function () {

        var btrigger = $("#blocktrigger").val();
        if (btrigger == 0)
        {


            $("#pathwaydelivery button").attr('disabled', 'disabled');
        } else {

            $("#pathwaydelivery button").prop("disabled", false);
        }
    });

    $("#appointmentdate").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        minDate: '0',
        buttonText: "Select date",
        dateFormat: "yy-mm-dd"
    });



    var startdate = moment().format("DD-MM-YYYY");

    var new_date = moment(startdate, "DD-MM-YYYY").add(5, 'days');

    $.ajax({
        url: patientapibase + '/api/v1/view_Patient',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        Accept: "application/json",
        data: JSON.stringify(ptdata),
        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");
        },
        success: function (response)
        {

            $.LoadingOverlay("hide");

            if (response.data != null)
            {
                var results = response.data;
                var name = results.patient.firstName + ' ' + results.patient.lastName;
                $("#patientname").html(name);

                if (results.pathwayInfo != null)
                {
                    if (results.pathwayInfo.data.eventInfo.length > 0)
                    {
                        patientevents = results.pathwayInfo.data.eventInfo;

                        $.ajax({
                            url: pathwayapibase + '/api/v1/checkforpatientpathwayacceptance',
                            type: 'POST',
                            dataType: 'json',
                            headers: {
                                'Authorization': securitytoken,
                                'Content-Type': 'application/json'
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




                                }
                                viewPathway();


                            }
                        });



                    }

                }
            }


        }
    });


    $("#editevent").on('click', function () {

        var meventname = $("#editeventname").val();
        var evinputdata = {
            "id": $("#eventeditid").val(),
            "eventName": meventname,
            "pathwayId": pid,
            "teamId": 1,
            "eventPocRow": 1,
            "eventPocCol": $("#eventeditcolindex").val()
        };

        $.ajax({
            url: pathwayapibase + '/api/v1/updateEvents',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(evinputdata),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                $.LoadingOverlay("hide");

                $("#editeventModal").modal('hide');
                var colenindex = parseInt($("#eventeditcolindex").val()) + 1;
                $("#pathwaytble tr th:nth-child(" + colenindex + ") div").text(meventname);
                $("#editeventname").val('');

            }
        });

    });
    /**
     * Edit Block functionality
     * For Block Type B
     * While saving the block we are checking with the block type
     * to get the respective messages should be updated.
     * Note that we are only allowing user to update the 
     * message part.
     * 
     * For Block Type A "Appointment Block". In Patient Pathway we had an option
     * to provide the appointment dates. Based on appointment date reposition
     * the remainder message blocks and the followup message blocks .
     * 
     * In case of the appointment date is already specified and some of the remainder messages sent in 
     * that case  system is again creating the new message blocks based on date selection which follows the logic 
     * like .....
     * Case Block Execution day > 7
     *  Then the system will create the appointmnt blocks on executed day -7 =Remiander one
     *  executed day -1 =Remiander to message
     *  Executed day +1 =follow up message
     * 
     *  Case Block Execution day < 7
     *  Then the system will create the appointmnt blocks on executed day -3 =Remiander one
     *  executed day -1 =Remiander to message
     *  Executed day +1 =follow up message
     * 
     *  Case Block Execution day < 3
     * 
     *  executed day -1 =Remiander to message
     *  Executed day +1 =follow up message
     Case Block Execution day =1
     * Shows only followup message with toase warn message like no remainders ..etc
     * 
     * Please note that if the patient has got earlier remiander message those 
     * ones will get deleted while creating 
     * on new ones.
     * 
     */
    $("#editblocsumbtn").click(function () {

        var currenteditblock = $saveblocks[currentblockforupdateid];

        var editmessage = $("#editblockmessage").val();
        var editremindermessage = $("#editremindermessage").val();
        var editappointmentfollowup = $("#editappointmentfollowup").val();
        var updatemessage = '';
        var inblocktype = '';

        if (editmessage != '')
        {
            currenteditblock.bodyOfMessage = editmessage;
            updatemessage = editmessage;
            inblocktype = 'M';
        }
        if (editremindermessage != '')
        {

            currenteditblock.remainderOfMessage = editremindermessage;
            updatemessage = editremindermessage;
            inblocktype = 'A';
        }
        if (editappointmentfollowup != '')
        {
            currenteditblock.followupOfMessage = editappointmentfollowup;
            updatemessage = editappointmentfollowup;
            inblocktype = 'A';
        }

        if (updatemessage == '')
        {
            $.toast({
                heading: 'Add Block',
                text: 'Block name should not be empty.',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            return;
        }



        if (currenteditblock.blockType == "A" && currenteditblock.blockAppointmentParent == 0)
        {
            $.LoadingOverlay("show");
            var recentbm = currenteditblock.bodyOfMessage;
            var recentrm = currenteditblock.remainderOfMessage;
            var recentfm = currenteditblock.followupOfMessage;
            var bins = {"parentblockid": currenteditblock.blockId}
            var blockcurrentpid = currenteditblock.blockId;



            $.ajax({
                url: pathwayapibase + '/api/v1/listBlocksByParent',
                type: 'POST',
                async: false,
                dataType: 'json',
                headers: {
                    'Authorization': securitytoken,
                    'Content-Type': 'application/json'
                },
                Accept: "application/json",
                data: JSON.stringify(bins),
                beforeSend: function ()
                {
                    $("#error").fadeOut();
                    $.LoadingOverlay("show");
                },
                success: function (response)
                {
                    $.LoadingOverlay("hide");


                    if ($("#editappointmentdate").val() != '')
                    {
                        if (response.data.length > 0)
                        {
                            $.each(response.data, function (ch, childblock) {


                                patientChildblockdelete(childblock.id);
                            });
                        }
                        patientChildblockdelete(currenteditblock.id);
                    } else
                    {

                        $.toast({
                            heading: 'Patient pathway',
                            text: 'Please Enter Date.',
                            textAlign: 'center',
                            position: 'top-center',
                            icon: 'error',
                            loader: false,
                            allowToastClose: false,
                            hideAfter: 5000,
                        });
                        return;
                    }

                    var date2 = new Date(stdate);
                    var date1 = new Date($("#editappointmentdate").val());


                    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                    var dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));


                    dayDifference = dayDifference + 1;
                    var blockexecuteday = dayDifference;

                    var rownum = dayDifference;


                    //Imp The Parent Appoinmentblock

                    var inputBloc = {
                        "blockName": currenteditblock.blockName,
                        "blockType": "A",
                        "blockid": blockcurrentpid,
                        "pathwayId": pid,
                        "appointmentdate": $("#editappointmentdate").val(),
                        "messagesendat": $("#editappointmentdate").val(), //later calculation apply
                        "messeagesentstatus": "scheduled",
                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                        "patientId": patientid,
                        "phiSecured": currenteditblock.phiSecured,
                        "blockAppointmentParent": 0,
                        "eventId": currenteditblock.eventId,
                        "blockPocRow": rownum,
                        "blockPocCol": currenteditblock.blockPocCol,
                        "triggerId": currenteditblock.triggerId,
                        "triggername": 0,
                        "DeliveryDaysAfterTigger": 0,
                        "repeatForNoOfDays": 0,
                        "subjectOfMessage": 'Hi',
                        "remainderOfMessage": currenteditblock.remainderOfMessage,
                        "followupOfMessage": currenteditblock.followupOfMessage,
                        "bodyOfMessage": currenteditblock.bodyOfMessage,
                        "status": "Y",
                        "msenttime": 0

                    };

                    $.ajax({
                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                        type: 'POST',
                        async: false,
                        dataType: 'json',
                        headers: {
                            'Authorization': securitytoken,
                            'Content-Type': 'application/json'
                        },
                        Accept: "application/json",
                        data: JSON.stringify(inputBloc),
                        beforeSend: function ()
                        {
                            $("#error").fadeOut();
                            $.LoadingOverlay("show");
                        },
                        success: function (response)
                        {
                            $.LoadingOverlay("hide");

                            var bid = response.data;

                            var blkclass = 'newcreateblock';
                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                            $saveblocks[bid] = inputBloc;
                        }
                    });


                    //Get the Date Difference here

                    var caseex = '';
                    if (dayDifference > 7)
                    {
                        caseex = 'greaterthan7';
                    }
                    if (dayDifference < 7 && dayDifference > 3)
                    {
                        caseex = 'lessthan7';
                    }
                    if (dayDifference < 3 && dayDifference > 1)
                    {
                        caseex = 'lessthan3';
                    }
                    if (dayDifference == 1)
                    {
                        caseex = 'dayone';
                    }



                    //Update the Parent Block Dates and execute the child logics

                    //Swtich Login starts here

                    switch (caseex) {
                        case 'greaterthan7':


                            for (var a = 0; a < 3; a++)
                            {

                                if (a == 0)
                                {

                                    var ablockexecuteday = blockexecuteday - 7;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';

                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }
                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() - 7);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();


                                    var blockname = 'Appointment Reminder 1';

                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": recentrm,
                                        "followupOfMessage": 'no message',
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };

                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });



                                }
                                if (a == 1)
                                {

                                    var ablockexecuteday = blockexecuteday - 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';

                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }

                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() - 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();



                                    var blockname = 'Appointment Reminder 2';
                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": recentrm,
                                        "followupOfMessage": 'no message',
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };

                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;
                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });

                                }
                                if (a == 2)
                                {


                                    var ablockexecuteday = blockexecuteday + 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';

                                    //Width Calculation
                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }
                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() + 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();




                                    var blockname = 'Follow Up Message';
                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": 'no message',
                                        "followupOfMessage": recentfm,
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };

                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });


                                }


                            }


                            $.LoadingOverlay("hide");
                            $("#editeventblockModal").modal('hide');
                            window.location.reload();

                            return;



                            break;
                        case 'lessthan7':

                            for (var a = 0; a < 3; a++)
                            {

                                if (a == 0)
                                {


                                    var ablockexecuteday = 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }

                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() - dayDifference);
                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();

                                    var blockname = 'Appointment Reminder 1';

                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": recentrm,
                                        "followupOfMessage": 'no message',
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };


                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });

                                }
                                if (a == 1)
                                {


                                    var ablockexecuteday = blockexecuteday - 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }
                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() - 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();

                                    var blockname = 'Appointment Reminder 2';
                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": recentrm,
                                        "followupOfMessage": 'no message',
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };

                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });

                                }
                                if (a == 2)
                                {


                                    var ablockexecuteday = blockexecuteday + 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }

                                    var blockname = 'Followup Message';
                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() + 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();


                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": 'no message',
                                        "followupOfMessage": recentfm,
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };

                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });
                                }


                            }
                            $.LoadingOverlay("hide");
                            $("#editeventblockModal").modal('hide');
                            window.location.reload();

                            return;

                            break;

                        case 'lessthan3':



                            for (var a = 0; a < 2; a++)
                            {

                                if (a == 0)
                                {

                                    var ablockexecuteday = blockexecuteday - 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';


                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }

                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() - 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();


                                    var blockname = 'Appointment Reminder 1';
                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": recentrm,
                                        "followupOfMessage": 'no message',
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };
                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });



                                }
                                if (a == 1)
                                {

                                    var ablockexecuteday = blockexecuteday + 1;
                                    currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                                    var blockstylewidth = 100 / bwdivlength;
                                    blockstylewidth = blockstylewidth + '%';

                                    for (var bw = 1; bw <= bwdivlength; bw++)
                                    {

                                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                                    }

                                    var bsomeDate = new Date($("#editappointmentdate").val());

                                    bsomeDate.setDate(bsomeDate.getDate() + 1);

                                    var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();


                                    var blockname = 'Follow Up Message';
                                    var inputBloc = {
                                        "blockName": blockname,
                                        "blockType": "A",
                                        "blockid": 0,
                                        "pathwayId": pid,
                                        "appointmentdate": $("#editappointmentdate").val(),
                                        "messagesendat": messsentdate, //later calculation apply
                                        "messeagesentstatus": "scheduled",
                                        "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                        "patientId": patientid,
                                        "phiSecured": currenteditblock.phiSecured,
                                        "blockAppointmentParent": blockcurrentpid,
                                        "eventId": currenteditblock.eventId,
                                        "blockPocRow": ablockexecuteday,
                                        "blockPocCol": currenteditblock.blockPocCol,
                                        "triggerId": currenteditblock.triggerId,
                                        "triggername": 0,
                                        "DeliveryDaysAfterTigger": 0,
                                        "repeatForNoOfDays": 0,
                                        "subjectOfMessage": 'Hi',
                                        "remainderOfMessage": 'no message',
                                        "followupOfMessage": recentfm,
                                        "bodyOfMessage": 'no meessage',
                                        "status": "Y",
                                        "msenttime": 0

                                    };



                                    $.ajax({
                                        url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                        type: 'POST',
                                        async: false,
                                        dataType: 'json',
                                        headers: {
                                            'Authorization': securitytoken,
                                            'Content-Type': 'application/json'
                                        },
                                        Accept: "application/json",
                                        data: JSON.stringify(inputBloc),
                                        beforeSend: function ()
                                        {
                                            $("#error").fadeOut();
                                            $.LoadingOverlay("show");
                                        },
                                        success: function (response)
                                        {
                                            $.LoadingOverlay("hide");

                                            var bid = response.data;

                                            var blkclass = 'newcreateblock';
                                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                            $("#" + currentid).append(blockhtml);
                                            $saveblocks[bid] = inputBloc;
                                        }
                                    });
                                }



                            }
                            $.LoadingOverlay("hide");
                            $("#editeventblockModal").modal('hide');
                            window.location.reload();
                            break;
                        case 'dayone':


                            $.toast({
                                heading: 'No Reminder',
                                text: 'No reminder message can be sent.',
                                textAlign: 'center',
                                position: 'top-center',
                                icon: 'warning',
                                loader: false,
                                allowToastClose: false,
                                hideAfter: 5000,
                            });


                            var ablockexecuteday = blockexecuteday + 1;
                            currentid = ablockexecuteday + '-' + currenteditblock.blockPocCol + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }

                            var bsomeDate = new Date($("#editappointmentdate").val());

                            bsomeDate.setDate(bsomeDate.getDate() + 1);

                            var messsentdate = bsomeDate.getFullYear() + '-' + (bsomeDate.getMonth() + 1) + '-' + bsomeDate.getDate();


                            var blockname = 'Follow Up Message';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": "A",
                                "blockid": 0,
                                "pathwayId": pid,
                                "appointmentdate": $("#editappointmentdate").val(),
                                "messagesendat": messsentdate, //later calculation apply
                                "messeagesentstatus": "scheduled",
                                "patientaccepteddate": date2.getFullYear() + '-' + date2.getMonth() + '-' + date2.getDate(),
                                "patientId": patientid,
                                "phiSecured": currenteditblock.phiSecured,
                                "blockAppointmentParent": blockcurrentpid,
                                "eventId": currenteditblock.eventId,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": currenteditblock.blockPocCol,
                                "triggerId": currenteditblock.triggerId,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": 'no message',
                                "followupOfMessage": recentfm,
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "msenttime": 0

                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/createpatientpathwayblockrecord',
                                type: 'POST',
                                async: false,
                                dataType: 'json',
                                headers: {
                                    'Authorization': securitytoken,
                                    'Content-Type': 'application/json'
                                },
                                Accept: "application/json",
                                data: JSON.stringify(inputBloc),
                                beforeSend: function ()
                                {
                                    $("#error").fadeOut();
                                    $.LoadingOverlay("show");
                                },
                                success: function (response)
                                {
                                    $.LoadingOverlay("hide");

                                    var bid = response.data;

                                    var blkclass = 'newcreateblock';
                                    var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                                    $("#" + currentid).append(blockhtml);
                                    $saveblocks[bid] = inputBloc;
                                }
                            });
                            $.LoadingOverlay("hide");
                            $("#editeventblockModal").modal('hide');
                            window.location.reload();
                            break;
                        default:
                            console.log('Case:: Default');
                    }
                    $.LoadingOverlay("hide");

                    //End of Swtich Case logic Here
                    //End of getting the date difference
                    return;






                }
            });
        } else
        {

            var pathwayupdateinput = {"patientid": patientid, "blockid": currentblockforupdateid, "mtype": inblocktype = 'M', "messagetxt": updatemessage}


            $.ajax({
                url: pathwayapibase + '/api/v1/updatePatientPathwayblock',
                type: 'POST',
                async: false,
                dataType: 'json',
                headers: {
                    'Authorization': securitytoken,
                    'Content-Type': 'application/json'
                },
                Accept: "application/json",
                data: JSON.stringify(pathwayupdateinput),
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

                        $.toast({
                            heading: 'Block Update',
                            text: 'Message Updated Succesfully.',
                            textAlign: 'center',
                            position: 'top-center',
                            loader: false,
                            icon: 'success',
                            allowToastClose: false,
                            hideAfter: 5000,
                        });
                        $saveblocks[currentblockforupdateid] = currenteditblock;
                        $("#editeventblockModal").modal('hide');
                    }
                },
                error: function () {
                    $.LoadingOverlay("hide");
                    $.toast({
                        heading: 'Save Pathway',
                        text: 'Updation Failed.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'error',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });
                    console.log('IN error @ Mode');
                }
            });
        }

    });

    $("#cbblockuploadplus").click(function () {

        $("#cbblockupload").toggle();
    });
    $("#bblockuploadplus").click(function () {

        $("#bblockupload").toggle();
    });
    $("#reminderuploadplus").click(function () {
        $("#reminderupload").toggle();
    });
    $("#followuploadplus").click(function () {
        $("#followupload").toggle();
    });
    $("#editappointmentdate").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        minDate: '0',
        buttonText: "Select date",
        dateFormat: "yy-mm-dd"
    });
    var currentblockclick;
    var $currenteventid;
    var $blocktype;
    $('input[type=radio][name=blocktype]').change(function () {
        if (this.value == 'M') {
            $blocktype = 'M';
            $('#aptgrp').css('display', 'none');
            $("#bblockmessage").css('display', 'block');
            $("#remindermessage").css('display', 'none');
            $("#followupmessage").css('display', 'none');
        } else if (this.value == 'A') {
            $blocktype = 'A';
            $('#aptgrp').css('display', 'block');
            $("#remindermessage").css('display', 'block');
            $("#followupmessage").css('display', 'block');
            $("#bblockmessage").css('display', 'none');


        }
    });

    $('input[type=radio][name=editblocktype]').change(function () {

    });

    $(document).on('click', 'td .newcreateblock', function () {

        if ($(this).data('blockid'))
        {
            $('#editeventblockModal').modal('hide');
        }

        var currenteditblock = $saveblocks[$(this).data('blockid')];

        var btype = currenteditblock.blockType;
        currentblockforupdateid = $(this).data('blockid');





        $("#editdeliverytirgger").attr('disabled', 'disabled');
        $("#editdeliverytirgger").val(currenteditblock.triggername);
        $("#editblocktype").attr('disabled', 'disabled');
        $("#editoptionsRadios1").attr('disabled', 'disabled');
        $("#editoptionsRadios2").attr('disabled', 'disabled');



        $("#editblockname").val(currenteditblock.blockName);
        $("#editblockmessage").val(currenteditblock.bodyOfMessage);
        if (btype == 'A')
        {
            $("#editoptionsRadios1").prop('checked', false);
            $("#editoptionsRadios2").prop('checked', true);



            if (currenteditblock.remainderOfMessage != 'no message')
            {
                $("#editremindermessage").css('display', 'block');
                $("#editfollowupmessage").css('display', 'none');
            }
            if (currenteditblock.followupOfMessage != 'no message')
            {
                $("#editfollowupmessage").css('display', 'block');
                $("#editremindermessage").css('display', 'none');
            }

            $("#editdeliverydaydisplay").css('display', 'none');
            $("#editblockmessage").css('display', 'none');
            $("#bblockmessage").css('display', 'none');

            $("#editremainderblockmessage").val(currenteditblock.remainderOfMessage);
            $("#editappointmentfollowup").val(currenteditblock.followupOfMessage);
            $("#editphisecuredgrp").css('display', 'block');

            $("#aptgrp").css('display', 'none');
            if (currenteditblock.blockAppointmentParent == 0)
            {

                $("#editphisecuredgrp").css('display', 'none');
                $("#editremindermessage").css('display', 'none');

                $("#aptgrp").css('display', 'block');
                $("#aptgrp").attr('disabled', false);

            }

        }
        if (btype == 'M')
        {
            $("#editoptionsRadios1").prop('checked', true);
            $("#editoptionsRadios2").prop('checked', false);
            $("#aptgrp").css('display', 'none');
            $("#editphisecuredgrp").css('display', 'block');

            $("#editremindermessage").css('display', 'none');

            $("#editfollowupmessage").css('display', 'none');

            $("#editdeliverydaydisplay").css('display', 'block');
            $("#editblockmessage").css('display', 'block');
            $("#bblockmessage").css('display', 'block');


        }

        $("#editblocktrigger").val(currenteditblock.triggerId);
        $("#editblockname").attr('disabled', 'disabled');

        $("#editblocktrigger").attr('disabled', 'disabled');
        $("#editdeliveryday").attr('disabled', 'disabled');
        $("#editdeliveryday").val(currenteditblock.blockPocRow);

        $("#editphisecured").attr('disabled', 'disabled');
        $("#editphisecured").css('display', 'none');

        if (currenteditblock.blockAppointmentDate != '')
        {

            $("#editappointmentdate").datepicker("setDate", currenteditblock.blockAppointmentDate);
        }
        if (!patientacceptdate)
        {
            $("#aptgrp").css('display', 'none');
            $("#editappointmentdate").css('display', 'none');
        }

        $("#editeventblockModal").modal('show');
        return;



    });



});
function addemptydata(pathswaysdata, maxRowNumber)
{

    if (pathswaysdata.events.length < 5)
    {
        for (var emrw = 1; emrw <= maxRowNumber; emrw++)
        {

            var tddata = '';
            for (var evetds = ((pathswaysdata.events.length) + 1); evetds <= 5; evetds++)
            {
                tddata += '<td></td>';
            }

            $("#pathwaytble tr:nth-child(" + emrw + ")").find("td:eq(2)").html(tddata);
        }
    }

}
function splitforpagination()
{



    var numCols = $("#pathwaytble").find('tr')[0].cells.length;

    var $pages = Math.ceil(numCols / 5);
    for (var pg = 1; pg <= $pages; pg++)
    {
        if (pg > 1)
        {
            var pkl = 1 + eventscolsarrhederlist[pg - 1];
        } else
        {
            var pkl = 1;
        }
        var colhtml = '';
        for (var kl = pkl; kl <= eventscolsarrhederlist[pg]; kl++)
        {

            colhtml += $("table tr th:nth-child(" + kl + "), table tr td:nth-child(" + kl + ")").html();
            if (kl > 6)
            {

                $("table tr th:nth-child(" + kl + "), table tr td:nth-child(" + kl + ")").hide();
            }
        }
        prhtml[pg] = colhtml;
    }


}
/**
 * 
 * @returns {JsonObject}
 * View PatientPathway
 * Loading the Master Pathway which is associated with patient
 * After that checking for patient acceptance in case of patient
 * not accepted then showing the Maste Pathway Information only
 * If the Patient Get Accepted then loading the child pathway along with 
 * Patient Master pathway while showing the child pathway the master pathway days
 * are converting into child pathway dates based on patient accepted date.
 * Whenever the user performs edit operations on blocks these changes will get
 * affected only for child pathway not for master pathway.
 * 
 * 
 */
function viewPathway()
{

    $.ajax({
        url: pathwayapibase + '/api/v1/viewPathway',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
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


            pathswaysdata = response.data[0];
            var blocarrs = [];
            var blocarrscol = [];
            $('.patientpathwayfilename').html(pathswaysdata.pathwayName);


            var theaderht = '';
            theaderht += '<th class="tbheader" data-name="header1">Event 1</th>';

            $.each(pathswaysdata.events, function (index, pathwayeventinfo) {
                if (index != 0)
                {

                    theaderht += '<th class="tbheader" data-name="header1">Event ' + (index) + '</th>'
                    $.each(pathwayeventinfo.blocks, function (bindex, blockinfo) {

                        blocarrs.push(blockinfo.blockPocRow);
                        blocarrscol.push(blockinfo.blockPocCol);


                    });


                }


            });


            if (pathswaysdata.events.length < 5)
            {
                var theaderht = '';
                theaderht += '<th class="tbheader" data-name="header1">Event 1</th>';
                for (var eventhead = 1; eventhead < 5; eventhead++)
                {

                    theaderht += '<th class="tbheader" data-name="header1">Event ' + (eventhead + 1) + '</th>';
                }
                $("#headerrow").append(theaderht);
            } else
            {
                $("#headerrow").append(theaderht);
            }

            maxRowNumber = Math.max.apply(Math, blocarrs);
            maxColNumber = Math.max.apply(Math, blocarrscol);
            var bthtml = '';
            var cuut = false;
            var cuutpos = 0;
            if (maxRowNumber < 150)
            {
                maxRowNumber = 150;
            }
            for (var tbrw = 1; tbrw <= maxRowNumber; tbrw++)
            {
                bthtml += '<tr>';
                if (patientacceptdate)
                {

                    var someDate = new Date(accpeteddate);

                    var numberOfDaysToAdd = tbrw;
                    if (numberOfDaysToAdd == 1)
                    {
                        someDate.setDate(someDate.getDate());

                        stdate = accpeteddate;

                    } else
                    {

                        someDate.setDate(someDate.getDate() + (parseInt(numberOfDaysToAdd) - 1));
                    }

                    var dd = someDate.getDate();
                    var mm = someDate.getMonth() + 1;
                    var y = someDate.getFullYear();

                    var someFormattedDate = dd + '-' + mm + '-' + y;

                    var new_date = moment(someFormattedDate, "DD-MM-YYYY");
                    var labeldate = moment(new_date).format('ddd , MMM Do , YYYY');
                    var cusdate = mm + '/' + dd + '/' + y;
                    var inputDate = new Date(cusdate);
                    var todaysDate = new Date();

                    if (inputDate.setHours(0, 0, 0, 0) == todaysDate.setHours(0, 0, 0, 0)) {

                        cuut = true;
                        cuutpos = tbrw;
                    }
                    bthtml += '<td>' + labeldate + '</td>';
                } else
                {
                    bthtml += '<td> Day ' + tbrw + '</td>';
                }

                if (pathswaysdata.events.length < 5)
                {
                    var columnlength = 5;
                } else
                {
                    columnlength = pathswaysdata.events.length;
                }

                for (var column = 1; column <= columnlength; column++)
                {
                    bthtml += '<td></td>';
                }


                bthtml += '</tr>';
            }
            $("#pathwaytemplatemaincontent").html(bthtml);
            if (cuut)
            {

                $('#pathwaytemplatemaincontent tr:nth-child(' + cuutpos + ')').addClass('currentday');
            }

            renderevent(pathswaysdata, maxRowNumber);
            splitforpagination();
        }
    });

}
function renderevent(pathswaysdata, maxRowNumber) {

    var eventcolorarr = ['color2', 'color3'];
    pathswaysdata.events.sort(function (a, b) {
        return a.id - b.id;
    });
    $.each(pathswaysdata.events, function (index, pathwayeventinfo) {

        var checkeveexist = checkObjectArray(patientevents, pathwayeventinfo.eventName, true);


        if (checkeveexist)
        {

            if (index == 1)
            {
                eventcolor = 'color2';
            }
            if (index <= 0)
            {
                var eventbg = 'color1';
                eventcolor = 'color1';

            } else
            {



                if ($currentcolor == 'color2')
                {
                    eventcolor = 'color3';
                }
                if ($currentcolor == 'color3')
                {
                    eventcolor = 'color4';
                }
                if ($currentcolor == 'color4')
                {
                    eventcolor = 'color5';
                }
                if ($currentcolor == 'color5')
                {
                    eventcolor = 'color2';
                }






                var eventbg = eventcolor;
            }

            var eventheaderclass = 'header-' + eventcolor;
            var eventheader = '<div class="' + eventheaderclass + '">' + pathwayeventinfo.eventName + '</div>';

            $("#headerrow th:nth-child(" + (index + 2) + ")").html(eventheader);
            $allvents[pathwayeventinfo.id] = pathwayeventinfo.eventName;

            var eventobj = {};
            eventobj.id = pathwayeventinfo.id;
            eventobj.name = pathwayeventinfo.eventName;
            $createdevents.push(eventobj);

            var eventrow = parseInt(pathwayeventinfo.eventPocRow);

            cuurentfilledeventcolpos = pathwayeventinfo.eventPocCol;

            for (var emrw = 1; emrw <= maxRowNumber; emrw++)
            {
                var cellid = parseInt(emrw) + '-' + pathwayeventinfo.eventPocCol + '-blockcell';
                var datrow = parseInt(emrw) + '-' + pathwayeventinfo.eventPocCol;
                var eventid = pathwayeventinfo.id;
                var drrow = emrw;

                var erclass = "addblock " + eventbg
                $currentcolor = eventcolor;
                var erhtml = '<div class="' + erclass + '" id="' + cellid + '" data-eventid="' + eventid + '" data-rowcol="' + datrow + '"></div>';
                $("#pathwaytble tr:nth-child(" + drrow + ")").find("td:eq(" + pathwayeventinfo.eventPocCol + ")").html(erhtml);

            }

            if (patientacceptdate)
            {
                loadpatientblocks(pathwayeventinfo.id);
            } else
            {
                renderblock(pathwayeventinfo.blocks);
            }


        } else
        {

            if (index == 1)
            {
                eventcolor = 'color2';
                $currentcolor = 'color2';

            } else
            {
                if ($currentcolor == 'color2')
                {
                    eventcolor = 'color3';
                }
                if ($currentcolor == 'color3')
                {
                    eventcolor = 'color4';
                }
                if ($currentcolor == 'color4')
                {
                    eventcolor = 'color5';
                }
                if ($currentcolor == 'color5')
                {
                    eventcolor = 'color2';
                }
            }

        }
    });
}


function loadpatientblocks(eid)
{
    var eventcountinput = {"patientid": patientid, "pathwayid": pid, "eventid": eid};


    $.ajax({
        url: pathwayapibase + '/api/v1/getPatientpathwayblockinfobyevent',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
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

            var eventblocks = response.data;

            renderblock(eventblocks);
        }
    });


}






function renderblock(blocks) {


    $.each(blocks, function (blindex, block) {

        block.triggername = $allvents[block.eventId] + ' > ' + block.blockName;
        var blocop = {};
        blocop.name = block.triggername;
        blocop.day = block.blockPocRow;
        blocktriggersoptions.push(blocop);
        var blockoptionhtml = '';
        $.each(blocktriggersoptions, function (optionindex, optionval) {
            blockoptionhtml += '<option value="' + optionval.day + '">' + optionval.name + '</option>';
        });

        $('#blocktrigger').html(blockoptionhtml);

        $saveblocks[block.id] = block;

        var bcreatid = block.blockPocRow + '-' + block.blockPocCol + '-blockcell';
        var btype = block.blockType;
        var appmaster = block.blockAppointmentParent;
        if (btype == "M")
        {
            var bcellclass = "newcreateblock";
        }
        if (btype == "A" && appmaster != 0)
        {
            var bcellclass = "newcreateblock";
        }

        if (btype == "A" && appmaster == 0)
        {
            var bcellclass = "newcreateblock";
        }
        var bwdivlength = parseInt($("#" + bcreatid + " div").length) + 1;
        if (bwdivlength != 0)
        {
            var blockstylewidth = 100 / bwdivlength;
            blockstylewidth = blockstylewidth + '%';
        } else
        {
            var blockstylewidth = '100%';

        }
        for (var bw = 1; bw <= (bwdivlength + 1); bw++)
        {
            $("#" + bcreatid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
        }


        var painp = {"blockid": block.id}



        var bhtm = '<div id="newblock" style="float:left;width:' + blockstylewidth + ';" data-blockid="' + block.id + '" data-eventid="' + block.eventId + '" class="' + bcellclass + '"><h3>' + block.blockName + '</h3></div>';
        $("#" + bcreatid).append(bhtm);

        if (block.messageStatus == "sent")
        {

            $("#" + bcreatid + " div").addClass('greycolor');
            $("#" + bcreatid + " div").prop('disabled', true);
        }

    });
}


function renderblockOrgRemove(blocks) {


    $.each(blocks, function (blindex, block) {

        block.triggername = $allvents[block.eventId] + ' > ' + block.blockName;
        var blocop = {};
        blocop.name = block.triggername;
        blocop.day = block.blockPocRow;
        blocktriggersoptions.push(blocop);
        var blockoptionhtml = '';
        $.each(blocktriggersoptions, function (optionindex, optionval) {
            blockoptionhtml += '<option value="' + optionval.day + '">' + optionval.name + '</option>';
        });

        $('#blocktrigger').html(blockoptionhtml);

        $saveblocks[block.id] = block;

        var bcreatid = block.blockPocRow + '-' + block.blockPocCol + '-blockcell';
        var btype = block.blockType;
        var appmaster = block.blockAppointmentParent;
        if (btype == "M")
        {
            var bcellclass = "newcreateblock";
        }
        if (btype == "A" && appmaster != 0)
        {
            var bcellclass = "newcreateblock";
        }

        if (btype == "A" && appmaster == 0)
        {
            var bcellclass = "newcreateblock";
        }
        var bwdivlength = parseInt($("#" + bcreatid + " div").length) + 1;
        if (bwdivlength != 0)
        {
            var blockstylewidth = 100 / bwdivlength;
            blockstylewidth = blockstylewidth + '%';
        } else
        {
            var blockstylewidth = '100%';

        }
        for (var bw = 1; bw <= (bwdivlength + 1); bw++)
        {
            $("#" + bcreatid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
        }
        var mstat = 'scheduled';
        var painp = {"blockid": block.id}
        $.ajax({
            url: pathwayapibase + '/api/v1/getPatientblockinfo',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
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

                if (response.data.length > 0)
                {

                    if (response.data[0][17] != 'no message')
                        $saveblocks[block.id].bodyOfMessage = response.data[0][17];
                    if (response.data[0][18] != 'no message')
                        $saveblocks[block.id].remainderOfMessage = response.data[0][18];
                    if (response.data[0][19] != 'no message')
                        $saveblocks[block.id].followupOfMessage = response.data[0][19];

                    mstat = response.data[0][10];
                    if (mstat == "sent")
                    {
                        $("#" + bcreatid + " div").addClass('greycolor');
                        $("#" + bcreatid + " div").prop('disabled', true);
                    }


                    $.LoadingOverlay("hide");
                } else
                {

                    $.LoadingOverlay("hide");
                }
            }
        });


        var bhtm = '<div id="newblock" style="float:left;width:' + blockstylewidth + ';" data-blockid="' + block.id + '" data-eventid="' + block.eventId + '" class="' + bcellclass + '"><h3>' + block.blockName + '</h3></div>';
        $("#" + bcreatid).append(bhtm);

    });
}

function checkObjectArray(inArr, eventName, exists)
{
    for (var i = 0; i < inArr.length; i++)
    {
        if (inArr[i].eventName == eventName)
        {
            return (exists === true) ? true : false;
        }
    }
}

function patientChildblockdelete(id)
{
    var inpid = {"id": id};
    $.ajax({
        url: pathwayapibase + '/api/v1/deletepatientchildblockbyid',
        type: 'POST',
        async: false,
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        Accept: "application/json",
        data: JSON.stringify(inpid),
        beforeSend: function ()
        {
            $("#error").fadeOut();
            $.LoadingOverlay("show");
        },
        success: function (response)
        {
            $.LoadingOverlay("hide");

        }
    });
}
function logout() {


    localStorage.clear();
    window.location.href = "index.html";

}
