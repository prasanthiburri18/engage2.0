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
var securitytoken = '';
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
var eventloadcolor = ["color1", "color2", "color3", "color4", "color5"];
var currentdate = new Date();
var currenttime = currentdate.getTime();
var pathwaycreatedts;
var orgid = 0;
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
var currentpathwayname = '';
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

$(document).ready(function () {
    if (navigator.userAgent.indexOf('Mac') > 0)
    {
        $('body').addClass('mac-os');
    }

    $("#exitpathway").click(function () {
        window.close();
    });

    /**
     * Save Pathway API Call:
     * @input params Json Object
     * PathwayId,PathwayName,OrgId,TeamId
     * @return {JsonObject} JsonObject with status Code
     */


    $("#savepathway").click(function () {

        var inputdata = {"id": $currentpthwayId, "pathwayName": currentpathwayname, "orgId": output.orgid, "teamId": 1, "status": "Y"};
        //Save Pathway Here
        $.toast({
            heading: 'Save Pathway',
            text: 'Pathway has been saved successfully.',
            textAlign: 'center',
            position: 'top-center',
            loader: false,
            icon: 'success',
            stack: true
        });
        var lastmodifiedtime = formatAMPM(new Date());
        lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
        $("#lastsavedtime").html(lastmodifiedtime);




    });


    //Appointment DatePicker
    $("#appointmentdate").datepicker({
        showOn: "button",
        buttonImage: "images/calendar-icon.png",
        buttonImageOnly: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-100:+0",
        maxDate: '0',
        buttonText: "Select date",
        dateFormat: "yy-mm-dd"
    });


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
        return;
    }


    var retrievedObject = localStorage.getItem('userinfo');
    var output = JSON.parse(retrievedObject);

    orgid = output.orgid;
    $(".lguser").html(output.fullName);

    var $pagnumber = 1;
    var $isnewpathway = false;
    var $currentpthwayId = 0;
    var $currenteventid = 0;
    var $createdevents = [];
    var createdblocks = [];


    var patwname = 'Untitled  Pathway';
    var $blocktype = 'M';

    var $filledeventcols = [1];
    var $previoushtmlarr = [];
    var totalrws = 1;
    var tbhtmlString = $("#pathwaytble").html();


    var blocktriggersoptions = [
        {
            "name": "No Trigger",
            "day": 0
        },
        {
            "name": "Welcome > Sign Up",
            "day": 1
        }
    ];

    var blockoptionhtml;
    $.each(blocktriggersoptions, function (optionindex, optionval) {
        blockoptionhtml += '<option value="' + optionval.day + '">' + optionval.name + '</option>';
    });

    $('#blocktrigger').html(blockoptionhtml);

    $("#pathwaydelivery button").attr('disabled', 'disabled');

    var $eventcolorarr = ['color1', 'color2', 'color3'];
    var $alternatecolor = ['color2', 'color3'];
    var eventcolor = 'color2';
    var $currentcolor = 'color1';
    var $saveblocks = [];
    var $currenteventid;
    var $blocktype;

    //Defualt settings for Modal Popup Blocks 
    $("#remindermessage").css('display', 'none');
    $("#followupmessage").css('display', 'none');
    $("#followupphisecuredgrp").css('display', 'none');
    $("#remainderphisecuredgrp").css('display', 'none');




    $('input[type=radio][name=blocktype]').change(function () {
        if (this.value == 'M') {
            $blocktype = 'M';
            $('#aptgrp').css('display', 'none');
            $("#cbblockmessage").css('display', 'block');
            $("#blockmessage").css('display', 'block');
            $("#blocktrigger").attr('disabled', false);
            $("#remindermessage").css('display', 'none');
            $("#followupmessage").css('display', 'none');
            $("#followupphisecuredgrp").css('display', 'none');
            $("#remainderphisecuredgrp").css('display', 'none');
            $("#pathwaydelivery").css('display', 'block');
            $("#pathwaydayrepeatevery").css('display', 'block');
            $("#pathwaydayoccurances").css('display', 'block');
            $("#phisecuredgrp").css('display', 'block');
        } else if (this.value == 'A') {
            $blocktype = 'A';
            $('#aptgrp').css('display', 'none');
            $("#blocktrigger").attr('disabled', true);
            $("#cbblockmessage").css('display', 'none');
            $("#blockmessage").css('display', 'none');


            $("#remindermessage").css('display', 'block');
            $("#followupmessage").css('display', 'block');
            $("#followupphisecuredgrp").css('display', 'block');
            $("#remainderphisecuredgrp").css('display', 'block');

            $("#pathwaydelivery").css('display', 'none');
            $("#phisecuredgrp").css('display', 'none');

            $("#pathwaydayrepeatevery").css('display', 'none');
            $("#pathwaydayoccurances").css('display', 'none');

        }
    });


    $('input[type=radio][name=editblocktype]').change(function () {

    });

    $(document).on("click", "div.changepathwayname", function () {
        var txt = $(".changepathwayname").text();
        $(".inpchangepathwayname").css("display", "block");
        $(".changepathwayname").css("display", "none");
        $(".inpchangepathwayname").val(txt);
    });

    /**
     * Pathway Update on name change .
     * @Input params Json Object
     * @return {JsonObject} JsonObject
     * 
     * 
     */
    $(document).on("blur", ".inpchangepathwayname", function () {
        var pname = $(this).val();
        currentpathwayname = pname;
        if ($currentpthwayId != 0)
        {
            updatePathwayInfo($currentpthwayId, currentpathwayname);


        } else
        {

            $('#addpathwayModal').modal({backdrop: 'static', keyboard: false})
            $("#addpathwayModal").modal('show');
            var pathn = currentpathwayname;
            $("#pathwayname").val(pathn);
            $(".changepathwayname").text(txt);
            $(".inpchangepathwayname").css("display", "none");
            $(".changepathwayname").css("display", "block");

        }



    });

    $('.modal').on('shown.bs.modal', function () {
        $(this).find('input:text:visible:first').focus();
    })
    /**
     * Add Event Modal which checks for
     * pathway availability. Incase of non-availability
     * then it will return a toast warn message
     * If the Pathway is there then It will show up
     * the modal add event modal window.s
     * 
     */
    $("#addeventmodal").click(function () {
        if ($currentpthwayId == 0)
        {

            $.toast({
                heading: 'Add Pathway',
                text: 'You need to create pathway before event creation..',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            return false;
        } else
        {

            $('#eventname').val('');
            $isnewpathway = false;
            $("#addeventModal").modal('show');
            $('#eventname').focus();
        }
    });



    /**
     * Checking for the clicked parent has
     * childs or not . If there are no childs
     * then allow add event modal popup
     * 
     * 
     * 
     */

    $("#pathwaytble tbody tr td").on('click', function () {
        var col = $(this).parent().children().index($(this));

        if ($(this).children().length == 0 && col > 0) {
            $("#addeventmodal").trigger("click");
            $('#eventname').focus()
        }
    });
    /**
     * Checking for the clicked parent has
     * childs or not . If there are  childs gt 0
     * then allow add block modal popup
     * 
     * 
     * 
     */
    $("#headerrow").on('click', 'th', function () {
        if ($(this).children().length > 0) {

            var ename = $(this).children().text();

            var erw = $(this).index();
            var eveid = $("#pathwaytemplatemaincontent tr td:nth-child(" + $(this).index() + ") div").data('eventid');


            var evinputdata = {
                "id": eveid,
                "eventName": ename,
                "pathwayId": $currentpthwayId,
                "teamId": 1,
                "eventPocRow": 1,
                "eventPocCol": erw
            };
//             $.ajax({
//            url: pathwayapibase + '/api/v1/updateEvent',
//            type: 'POST',
//            dataType: 'json',
//            contentType: "application/json",
//            Accept: "application/json",
//            data: JSON.stringify(evinputdata),
//            beforeSend: function ()
//            {
//                $("#error").fadeOut();
//                $.LoadingOverlay("show");
//            },
//            success: function (response)
//            {
//                $.LoadingOverlay("show");
//                console.log(response);
//            }
//        });

        } else
        {

            if ($(this).index() != 0)
                $("#addeventmodal").trigger("click");
            $('#eventname').focus()
//       $("#addeventmodal").trigger("click"); 
        }

    });


    var previuoshtml = '';

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
                    heading: 'Pathway',
                    text: e.message,
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });
            }


        }


        return;



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
                    heading: 'Pathway',
                    text: e.message,
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });
            }
        }

        return;


    });







    CountColumn = function (T) {
        return $(T).find('tr')[0].cells.length;
    }
    Removetablecols = function (str) {
        var target = $('table').find('th[data-name="' + str + '"]');
        var index = (target).index();
        $('table tr').find('th:eq(' + index + '),td:eq(' + index + ')').remove();
    }

    if (CountColumn("#pathwaytble") == 6)
    {



    }
    var colsarr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    var colsarrset = 1;
    /**
     * Event Creation functionality
     * Check point: Checking for the pathway creation or not
     * If there is not pathway while creating the event then 
     * the system will repond with some error taost message.
     * In case of the pathway existence then the system will create
     * an event using QC API call.
     * And alos the system will check for the dupliacte name with status code 208 which was getting
     * as API response.
     * 
     * 
     */
    $("#createevent").on('click', function () {


        $("#addeventModal").modal('hide');
        if ($currentcolor == 'color1')
        {
            eventcolor = 'color2';
        }
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

        var eventname = '';
        var eventid = 0;
        var eventdat = 1;
        if ($('#eventname').val() != '')
        {
            eventname = $('#eventname').val();
        }

        var nexteventcol = $filledeventcols.length;


        totalrws = $("#pathwaytble  tr").length;


        if ($currentpthwayId == 0)
        {

            $.toast({
                heading: 'Add Pathway',
                text: "No Pathway Id",
                textAlign: 'center',
                position: 'top-center',
                icon: 'error',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            return false;
        }
        if ($isnewpathway)
        {
            eventbg = 'color1';

            var inputdata = {
                "eventName": "Welcome",
                "pathwayId": $currentpthwayId,
                "teamId": 1,
                "eventPocRow": 2,
                "eventPocCol": 1,
                "createDate": currenttime
            };

            $.ajax({
                url: pathwayapibase + '/api/v1/addEvent',
                type: 'POST',
                dataType: 'json',
                headers: {
                    'Authorization': securitytoken,
                    'Content-Type': 'application/json'
                },
                Accept: "application/json",
                data: JSON.stringify(inputdata),
                beforeSend: function ()
                {
                    $("#error").fadeOut();
                    $.LoadingOverlay("show");
                },
                success: function (response)
                {

                    eventid = response.data;


                    var eventobj = {};
                    eventobj.id = eventid;
                    eventobj.name = inputdata.eventName;
                    $createdevents.push(eventobj);
                    $.LoadingOverlay("hide");

                    for (var k = 1; k <= totalrws; k++)
                    {


                        var rowcolval = k + '-' + nexteventcol;
                        var idval = rowcolval + "-blockcell";
                        var eventclass = 'addblock' + ' ' + eventbg;

                        var eventrowhtml = '<div class="' + eventclass + '" id="' + idval + '" data-eventid="' + eventid + '" data-rowcol="' + rowcolval + '"></div>';
                        $('#pathwaytble tr:nth-child(' + k + ')').find('td:eq(' + nexteventcol + ')').html(eventrowhtml);

                    }

                    $filledeventcols.push(nexteventcol);

                    var eventheaderclass = 'header-color1';
                    var eventheader = '<div class="' + eventheaderclass + '">' + inputdata.eventName + '</div>';
                    $('#pathwaytble tr').find('th:eq(' + nexteventcol + ')').html(eventheader);

                    var sinup = 'Hi #FN  you have been chosen to receive messages from the engage application.  To accept reply with Y, to decline reply with N';
                    var inputBloc = {
                        "blockName": "Sign Up",
                        "blockType": $blocktype,
                        "pathwayId": $currentpthwayId,
                        "eventId": eventid,
                        "blockPocRow": 1,
                        "blockPocCol": 1,
                        "triggerId": 1,
                        "triggername": 'Welcome > Sign Up',
                        "DeliveryDaysAfterTigger": 1,
                        "repeatForNoOfDays": 1,
                        "subjectOfMessage": "hi",
                        "bodyOfMessage": sinup,
                        "phiSecured": "no",
                        "blockAppointmentParent": 0,
                        "remainderOfMessage": "no message",
                        "followupOfMessage": "no message",
                        "status": "Y",
                        "createDate": currenttime,
                        "noofOccurence": 0
                    };

                    $.ajax({
                        url: pathwayapibase + '/api/v1/addBlock',
                        type: 'POST',
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

                            inputBloc.id = response.data;
                            $saveblocks[response.data] = inputBloc;
                            $.LoadingOverlay("hide");
                            var firstblck = $("#1-1-blockcell");
                            var blockname = '<h3>Sign Up</h3>';
                            var blockhtml = '<div id="newblock"  data-blockid="' + response.data + '" data-eventid="' + eventid + '" class="newcreateblock">' + blockname + '</div>';
                            firstblck.html(blockhtml);


                        },
                        error: function () {
                            console.log('erorr');
                        }
                    });


                },
                error: function () {
                    console.log('IN error @ Mode');
                }

            });
        } else
        {


            if (eventname == '')
            {

                $.toast({
                    heading: 'Pathway',
                    text: "Please Enter the Event Name",
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    allowToastClose: false,
                    hideAfter: 5000,
                });
                return false;
            }

            if (cuurentfilledeventcolpos >= 5)
            {
                var executepage = Math.ceil(cuurentfilledeventcolpos / 5);


                $currentpage = executepage - 1;
                if (cuurentfilledeventcolpos % 5 === 0)
                {
                    $currentpage = $currentpage + 1;
                }
                $("#nextcols").trigger("click");
            }
            var inputdata = {
                "eventName": eventname,
                "pathwayId": $currentpthwayId,
                "teamId": 1,
                "eventPocRow": eventdat,
                "eventPocCol": $filledeventcols.length,
                "createDate": currenttime
            };

            $.ajax({
                url: pathwayapibase + '/api/v1/addEvent',
                type: 'POST',
                dataType: 'json',
                headers: {
                    'Authorization': securitytoken,
                    'Content-Type': 'application/json'
                },
                Accept: "application/json",
                data: JSON.stringify(inputdata),
                beforeSend: function ()
                {
                    $("#error").fadeOut();
                    $.LoadingOverlay("show");
                },
                success: function (response)
                {
                    $.LoadingOverlay("hide");

                    eventid = response.data;
                    if (response.statuscode == 208)
                    {

                        $.toast({
                            heading: 'Pathway',
                            text: "Event name is already taken",
                            textAlign: 'center',
                            position: 'top-center',
                            icon: 'warning',
                            loader: false,
                            allowToastClose: false,
                            hideAfter: 5000,
                        });
                        return false;
                    }
                    cuurentfilledeventcolpos = $filledeventcols.length;

                    var eventobj = {};
                    eventobj.id = eventid;
                    eventobj.name = inputdata.eventName;
                    $createdevents.push(eventobj);




                    for (var k = eventdat; k <= totalrws; k++)
                    {


                        var rowcolval = k + '-' + nexteventcol;
                        var idval = rowcolval + "-blockcell";

                        var eventclass = 'addblock' + ' ' + eventbg;
                        $currentcolor = eventcolor;
                        $('#pathwaytble tr:nth-child(' + k + ')').find('td:eq(' + nexteventcol + ')').html('<div class="' + eventclass + '" id="' + idval + '" data-eventid="' + eventid + '" data-rowcol="' + rowcolval + '"></div>');

                    }

                    $filledeventcols.push(nexteventcol);

                    var eventheaderclass = 'header-' + eventcolor;
                    var eventheader = '<div class="' + eventheaderclass + '">' + eventname + '</div>';
                    $('#pathwaytble tr').find('th:eq(' + nexteventcol + ')').html(eventheader);
                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                },
                error: function () {
                    console.log('IN error @ Mode');
                }
            });

        }


    });


    var currentblockclick;
    /**
     * Add Block functionality
     * While clicking on the Table event filled row
     * the system will get displayed the modal popup
     * base the blocktype selection the fields off/onn
     * Note:: The below funciton which is used for creating block incase
     * of already a block is placed on clicked cell.
     * 
     */
    $(document).on('click', 'td .newcreateblock', function () {

        if ($(this).data('blockid'))
        {
            $('#addeventblockModal').modal('hide');
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
        }

        if (btype == 'M')
        {
            $("#editoptionsRadios1").prop('checked', true);
            $("#editoptionsRadios2").prop('checked', false);



            $("#editremindermessage").css('display', 'none');

            $("#editfollowupmessage").css('display', 'none');

            $("#editdeliverydaydisplay").css('display', 'block');
            $("#editblockmessage").css('display', 'block');
            $("#bblockmessage").css('display', 'block');


        }

        $("#editblocktrigger").val(currenteditblock.triggerId);
        $("#editblockname").attr('disabled', 'disabled');

        $("#editblocktrigger").attr('disabled', 'disabled');
        $("#editappointmentdate").attr('disabled', 'disabled');
        $("#editappointmentdate").css('display', 'none');
        $("#editdeliveryday").attr('disabled', 'disabled');
        $("#editdeliveryday").val(currenteditblock.blockPocRow);


        $("#editphisecured").attr('disabled', 'disabled');
        $("#editphisecured").css('display', 'none');






        $("#editeventblockModal").modal('show');
        return;



    });

    /**
     * Add Block functionality
     * While clicking on the Table event filled row
     * the system will get displayed the modal popup
     * base the blocktype selection the fields off/onn
     * 
     * 
     */
    $("#pathwaytble").on('click', '.addblock', function (e) {

        if (e.target !== e.currentTarget)
            return;
        var rownumber = $(this).find('input').val();



        var clickedcell = $(this).data('blockavail');


        currentblockclick = $(this);

        $currenteventid = $(this).data('eventid');
        $("#blockname").val('');


        $("#blockmessage").val('');
        $('#addblocfrm')[0].reset();
        $('#addblocfrm')[0].reset();
        $("#remindermessage").css('display', 'none');
        $("#followupmessage").css('display', 'none');

        $("#followupphisecuredgrp").css('display', 'none');
        $("#remainderphisecuredgrp").css('display', 'none');


        $("#blocktrigger").attr('disabled', false);
        $("#pathwaydelivery button").attr('disabled', 'disabled');

        $("#pathwaydelivery").css('display', 'block');
        $("#blockmessage").css('display', 'block');
        $("#cbblockmessage").css('display', 'block');

        $("#pathwaydayrepeatevery").css('display', 'block');
        $("#pathwaydayoccurances").css('display', 'block');
        $("#phisecuredgrp").css('display', 'block');

        $('#addeventblockModal').modal('show');


    });
    /**
     * Edit Block functionality
     * While saving the block we are checking with the block type
     * to get the respective messages should be updated.
     * Note that we are only allowing user to update the 
     * message part.
     * 
     */
    $("#editblocsumbtn").click(function () {



        var currenteditblock = $saveblocks[currentblockforupdateid];

        var editmessage = $("#editblockmessage").val();
        if (currenteditblock.blockType == 'A')
        {

            var editrmessage = $("#editremainderblockmessage").val();
            var editfmessage = $("#editappointmentfollowup").val();



            if (editrmessage != '')
                currenteditblock.remainderOfMessage = editrmessage;
            if (editfmessage != '')
                currenteditblock.followupOfMessage = editfmessage;
        }
        if (currenteditblock.blockType == 'M')
        {

            if (editmessage != '')
                currenteditblock.bodyOfMessage = editmessage;
        }

        currenteditblock.id = currentblockforupdateid
        $.ajax({
            url: pathwayapibase + '/api/v1/updateBlock',
            type: 'POST',
            async: false,
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(currenteditblock),
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
                        heading: 'Message Block',
                        text: 'Message Updated Succesfully.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'success',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });

                    $saveblocks[currentblockforupdateid] = currenteditblock;
                    $("#editeventblockModal").modal('hide');
                }
            },
            error: function (err, status) {
                $.LoadingOverlay("hide");
                $.toast({
                    heading: 'Updation',
                    text: 'Updation Failed',
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


    });
    /**
     * Block Trigger change functionality
     * In case of no trigger we are disabling
     * triggerdays.
     * 
     */
    $("#blocktrigger").on('change', function () {

        var btrigger = $("#blocktrigger").val();
        if (btrigger == 0)
        {


            $("#pathwaydelivery button").attr('disabled', 'disabled');
            $("#triggerdays").prop("disabled", true);






        } else {


            $("#pathwaydelivery button").prop("disabled", false);
            $("#triggerdays").prop("disabled", false);





        }
    });

    /**
     * Upload plus button toggle functionality.
     * 
     */

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

    /**
     * Add Block Submit functionality
     * Checking for the block name field
     * in case of it is empty then showing the toast warn message.
     * In case of every thing is fine then creating the block
     * using QC API Call.
     *  While creating the block we are checking with Block type
     *  If the block type is Message then  the calculations are made with
     *  trigger ,repeat and occurence input vals
     *  In case the block type is an appointment then we are applying the below logic
     *  
     *  Case Block Execution day > 7
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
     *  
     */
    $("div").on('click', '#addblocsumbtn', function (ev) {


        $.LoadingOverlay("hide");
        ev.stopPropagation();


        var blockname = $("#blockname").val();
        if (blockname == '')
        {

            $.toast({
                heading: 'Updation',
                text: 'Please enter a Block Name',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            $.LoadingOverlay("hide");
            return false;
        }
        var blocktirggerafter = $("#blocktrigger").val();
        var blockmessage = $("#blockmessage").val();
        var triggerdays = $("#triggerdays").val();
        var repeatdays = $("#repeatdays").val();
        var occurrences = $("#occurrences").val();
        var triggertext = $("#blocktrigger").find(":selected").text();


        var phisecuredinput = "no";
        if ($('#phisecured').is(':checked'))
        {

            phisecuredinput = "yes";
        } else
        {

            phisecuredinput = "no";
        }
        var reminderphisecuredinput = "no";
        if ($('#reminderphisecured').is(':checked'))
        {

            reminderphisecuredinput = "yes";
        } else
        {

            reminderphisecuredinput = "no";
        }
        var followphisecuredinput = "no";
        if ($('#followphisecured').is(':checked'))
        {

            followphisecuredinput = "yes";
        } else
        {

            followphisecuredinput = "no";
        }


        var res = currentblockclick.attr('id').split("-");


        if (blocktirggerafter == 0)
        {

            blocktirggerafter = res[0];
            var changedowid = res[0];

            var blockexecuteday = parseInt(blocktirggerafter);
            var currentid = blockexecuteday + '-' + res[1] + '-blockcell';
        } else
        {
            var changedowid = res[0];

            var blockexecuteday = parseInt(blocktirggerafter) + parseInt(triggerdays);
            var currentid = blockexecuteday + '-' + res[1] + '-blockcell';
        }

        if ($blocktype == 'A')
        {
            $.LoadingOverlay("hide");
            var appointmentparentid = 0;


            var remindermessage = $("#remainderblockmessage").val();
            var appointmentfollowup = $("#appointmentfollowup").val();

            var inputBloc = {
                "blockName": blockname,
                "blockType": $blocktype,
                "pathwayId": $currentpthwayId,
                "phiSecured": phisecuredinput,
                "blockAppointmentParent": appointmentparentid,
                "eventId": $currenteventid,
                "blockPocRow": blockexecuteday,
                "blockPocCol": res[1],
                "triggerId": blocktirggerafter,
                "triggername": 0,
                "DeliveryDaysAfterTigger": 0,
                "repeatForNoOfDays": 0,
                "subjectOfMessage": 'SUB',
                "remainderOfMessage": 'no message',
                "followupOfMessage": 'no message',
                "bodyOfMessage": 'no message',
                "status": "Y",
                "createDate": currenttime,
                "noofOccurence": 0,
            };


            $.ajax({
                url: pathwayapibase + '/api/v1/addBlock',
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

                    var bid = response.data;
                    appointmentparentid = bid;
//                    var blkclass = 'newcreateblock appointmentblock';
                    var blkclass = 'newcreateblock';
                    var blockhtml = '<div  id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blockname + '</div>'

                    $("#" + currentid).append(blockhtml);
                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                }
            });

            var caseex = '';

            if (blockexecuteday > 7)
            {
                caseex = 'greaterthan7';
            }
            if (blockexecuteday < 7)
            {
                caseex = 'lessthan7';
            }
            if (blockexecuteday < 3 && blockexecuteday > 1)
            {
                caseex = 'lessthan3';
            }
            if (blockexecuteday == 1)
            {
                caseex = 'dayone';
            }

            switch (caseex) {
                case 'greaterthan7':


                    for (var a = 0; a < 3; a++)
                    {

                        if (a == 0)
                        {


                            var ablockexecuteday = blockexecuteday - 7;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';

                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }

                            var blockname = 'Appointment Reminder 1';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": reminderphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": remindermessage,
                                "followupOfMessage": 'no message',
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });



                        }
                        if (a == 1)
                        {


                            var ablockexecuteday = blockexecuteday - 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';

                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }



                            var blockname = 'Appointment Reminder 2';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": reminderphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": remindermessage,
                                "followupOfMessage": 'no message',
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });

                        }
                        if (a == 2)
                        {


                            var ablockexecuteday = blockexecuteday + 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';

                            //Width Calculation
                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }

                            var blockname = 'Follow Up Message';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": followphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": 'no message',
                                "followupOfMessage": appointmentfollowup,
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });


                        }


                    }


                    $.LoadingOverlay("hide");
                    $("#addeventblockModal").modal('hide');
                    ev.stopPropagation();

                    return;



                    break;
                case 'lessthan7':

                    for (var a = 0; a < 3; a++)
                    {

                        if (a == 0)
                        {


                            var ablockexecuteday = 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }
                            var blockname = 'Appointment Reminder 1';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": reminderphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": remindermessage,
                                "followupOfMessage": 'no message',
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });

                        }
                        if (a == 1)
                        {

                            var ablockexecuteday = blockexecuteday - 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }
                            var blockname = 'Appointment Reminder 2';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": reminderphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": remindermessage,
                                "followupOfMessage": 'no message',
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });
                        }
                        if (a == 2)
                        {


                            var ablockexecuteday = blockexecuteday + 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }
                            var blockname = 'Followup Message';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": followphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": 'no message',
                                "followupOfMessage": appointmentfollowup,
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });
                        }


                    }
                    $.LoadingOverlay("hide");
                    $("#addeventblockModal").modal('hide');
                    ev.stopPropagation();

                    return;

                    break;

                case 'lessthan3':



                    for (var a = 0; a < 2; a++)
                    {

                        if (a == 0)
                        {


                            var ablockexecuteday = blockexecuteday - 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }



                            var blockname = 'Appointment Reminder 1';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": reminderphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": remindermessage,
                                "followupOfMessage": 'no message',
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });
                        }
                        if (a == 1)
                        {


                            var ablockexecuteday = blockexecuteday + 1;
                            currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';

                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }



                            var blockname = 'Follow Up Message';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": $currentpthwayId,
                                "phiSecured": followphisecuredinput,
                                "blockAppointmentParent": appointmentparentid,
                                "eventId": $currenteventid,
                                "blockPocRow": ablockexecuteday,
                                "blockPocCol": res[1],
                                "triggerId": blocktirggerafter,
                                "triggername": 0,
                                "DeliveryDaysAfterTigger": 0,
                                "repeatForNoOfDays": 0,
                                "subjectOfMessage": 'Hi',
                                "remainderOfMessage": 'no message',
                                "followupOfMessage": appointmentfollowup,
                                "bodyOfMessage": 'no meessage',
                                "status": "Y",
                                "createDate": currenttime,
                                "noofOccurence": 0,
                            };

                            $.ajax({
                                url: pathwayapibase + '/api/v1/addBlock',
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
                                    updatePathwayInfo($currentpthwayId, currentpathwayname);
                                }
                            });
                        }



                    }
                    $.LoadingOverlay("hide");
                    $("#addeventblockModal").modal('hide');
                    ev.stopPropagation();
                    break;
                case 'dayone':


                    $.toast({
                        heading: 'Add pathway',
                        text: 'No reminder message can be sent.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'warning',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });


                    var ablockexecuteday = blockexecuteday + 1;
                    currentid = ablockexecuteday + '-' + res[1] + '-blockcell';


                    var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                    var blockstylewidth = 100 / bwdivlength;
                    blockstylewidth = blockstylewidth + '%';


                    for (var bw = 1; bw <= bwdivlength; bw++)
                    {

                        $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                    }



                    var blockname = 'Follow Up Message';
                    var inputBloc = {
                        "blockName": blockname,
                        "blockType": $blocktype,
                        "pathwayId": $currentpthwayId,
                        "phiSecured": followphisecuredinput,
                        "blockAppointmentParent": appointmentparentid,
                        "eventId": $currenteventid,
                        "blockPocRow": ablockexecuteday,
                        "blockPocCol": res[1],
                        "triggerId": blocktirggerafter,
                        "triggername": 0,
                        "DeliveryDaysAfterTigger": 0,
                        "repeatForNoOfDays": 0,
                        "subjectOfMessage": 'Hi',
                        "remainderOfMessage": 'no message',
                        "followupOfMessage": appointmentfollowup,
                        "bodyOfMessage": 'no meessage',
                        "status": "Y",
                        "createDate": currenttime,
                        "noofOccurence": 0,
                    };

                    $.ajax({
                        url: pathwayapibase + '/api/v1/addBlock',
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
                            updatePathwayInfo($currentpthwayId, currentpathwayname);
                        }
                    });
                    $.LoadingOverlay("hide");
                    $("#addeventblockModal").modal('hide');
                    ev.stopPropagation();
                    break;
                default:
                    console.log('Case:: Default');
            }
            $.LoadingOverlay("hide");
            ev.stopPropagation();

            $.LoadingOverlay("hide");
            return;

        }
        if ($blocktype == 'M')
        {

            for (var bcell = 0; bcell < occurrences; bcell++)
            {

                var inputBloc = {
                    "blockName": blockname,
                    "blockType": $blocktype,
                    "pathwayId": $currentpthwayId,
                    "eventId": $currenteventid,
                    "blockPocRow": blockexecuteday,
                    "blockPocCol": res[1],
                    "triggerId": blocktirggerafter,
                    "triggername": triggertext,
                    "DeliveryDaysAfterTigger": triggerdays,
                    "repeatForNoOfDays": repeatdays,
                    "subjectOfMessage": "hi",
                    "bodyOfMessage": blockmessage,
                    "phiSecured": phisecuredinput,
                    "blockAppointmentParent": 0,
                    "remainderOfMessage": "no message",
                    "followupOfMessage": "no message",
                    "status": "Y",
                    "createDate": currenttime,
                    "noofOccurence": parseInt(occurrences) - (bcell + 1),
                };

                $.ajax({
                    url: pathwayapibase + '/api/v1/addBlock',
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

                        var bid = response.data;
                        inputBloc.id = response.data;
                        $saveblocks[bid] = inputBloc;



                        var eventdetailsforblock = getObjects($createdevents, 'id', $currenteventid);

                        var blockoptionname = eventdetailsforblock[0].name + '>' + blockname;
                        var blocop = {};
                        blocop.name = blockoptionname;
                        blocop.day = blockexecuteday;
                        if (bcell == (parseInt(occurrences) - 1))
                        {
                            blocktriggersoptions.push(blocop);
                        }


                        var blockoptionhtml = '';
                        $.each(blocktriggersoptions, function (optionindex, optionval) {
                            blockoptionhtml += '<option value="' + optionval.day + '">' + optionval.name + '</option>';
                        });

                        $('#blocktrigger').html(blockoptionhtml);


                        var tablerowlth = $('#pathwaytble tbody tr').length;

                        var curcol = res[1];
                        if (parseInt(tablerowlth) < parseInt(blockexecuteday))
                        {

                            var addrwsnumber = blockexecuteday - tablerowlth;

                            for (var adnr = 0; adnr < addrwsnumber; adnr++)
                            {

                                tablerowlth = tablerowlth + 1;


                                $('#pathwaytble tbody').append($("#pathwaytble tbody tr:last").clone());



                                var twlen = ($('#pathwaytble tbody tr').length);

                                var adr = 'Day ' + twlen;
                                $("#pathwaytble tbody tr:last td:first").html(adr);


                                var dh = 0;
                                var trtdlast = $("#pathwaytble tbody tr:last");
                                filluprows(trtdlast, twlen);






                            }
                            var bcuurentday = parseInt(blockexecuteday) + parseInt(repeatdays);

                            if (parseInt(occurrences) > 1)
                            {

                                var blocknameht = '<h3>' + blockname + '</h3>';
                                var enaft = parseInt(occurrences) - (bcell + 1);
                                if (enaft > 0)
                                    blocknameht += '<p style="font-size:10px;color:#fff;">Every ' + repeatdays + ' Days | End After ' + enaft + ' Occurance</p>';
                                if (enaft == 0)
                                    blocknameht += '<p style="font-size:10px;color:#fff;">Last Occurance</p>';

                            } else
                            {


                                var blocknameht = '<h3>' + blockname + '</h3>';
                            }
                            if ($blocktype == 'A')
                            {
                                var blkclass = 'newcreateblock appointmentblock';
                            }

                            if ($blocktype == 'M')
                            {
                                var blkclass = 'newcreateblock';
                            }

                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }
                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blocknameht + '</div>';
                            $("#" + currentid).append(blockhtml);
                            blockexecuteday = bcuurentday;
                            changedowid = blockexecuteday;
                            currentid = changedowid + '-' + res[1] + '-blockcell';

                        } else
                        {
                            var bcuurentday = parseInt(blockexecuteday) + parseInt(repeatdays);

                            if (parseInt(occurrences) > 1)
                            {

                                var blocknameht = '<h3>' + blockname + '</h3>';
                                var enaft = parseInt(occurrences) - (bcell + 1);
                                var enaft = parseInt(occurrences) - (bcell + 1);
                                if (enaft > 0)
                                    blocknameht += '<p style="font-size:10px;color:#fff;">Every ' + repeatdays + ' Days | End After ' + enaft + ' Occurrences</p>';
                                if (enaft == 0)
                                    blocknameht += '<p style="font-size:10px;color:#fff;">Last Occurance</p>';
                            } else
                            {


                                var blocknameht = '<h3>' + blockname + '</h3>';
                            }
                            if ($blocktype == 'A')
                            {
                                var blkclass = 'newcreateblock appointmentblock';

                            }
                            if ($blocktype == 'M')
                            {
                                var blkclass = 'newcreateblock';
                            }
                            var bwdivlength = (parseInt($("#" + currentid + " div").length) + 1);
                            var blockstylewidth = 100 / bwdivlength;
                            blockstylewidth = blockstylewidth + '%';


                            for (var bw = 1; bw <= bwdivlength; bw++)
                            {

                                $("#" + currentid + " div:nth-child(" + bw + ")").css({'width': blockstylewidth});
                            }


                            var blockhtml = '<div style="float:left;width:' + blockstylewidth + ';" id="newblock" data-blockavail="true" data-blockid="' + bid + '" class="' + blkclass + '">' + blocknameht + '</div>'

                            $("#" + currentid).append(blockhtml);
                            blockexecuteday = bcuurentday;
                            changedowid = blockexecuteday;
                            currentid = changedowid + '-' + res[1] + '-blockcell';

                        }



                        updatePathwayInfo($currentpthwayId, currentpathwayname);

                        $.LoadingOverlay("hide");
                    },
                    error: function (err, status)
                    {
                        $.LoadingOverlay("hide");
                    }
                });













            }
            $.LoadingOverlay("hide");
            $("#addeventblockModal").modal('hide');
            return false;
        }
        $.LoadingOverlay("hide");
        $("#addeventblockModal").modal('hide');
        return false;

    });


    $('#addpathwayModal').modal({backdrop: 'static', keyboard: false})
    $("#addpathwayModal").modal('show');

    $(".pathwayfilename").html(patwname);
    /**
     * Add Pathway Functionality Start here
     * Creating the Pathway from modal popup
     * Checking the pathway name field in case of 
     * empty then we are showing the toast warn message
     * Once after pathway creation using QC API Call
     * the we are creating the Default event calling as Welcome.
     * followed default block calling as Signup.
     * While creating the block we are appending the welcome message
     * with #FN place holder which waould be helpful for the backend system.
     * 
     * So Please maintain #FN Place holder in case of updating the signup block as well.
     * 
     * 
     */
    $("#createpathway").click(function () {


        patwname = $('#pathwayname').val();
        if (patwname == '')
        {

            $.toast({
                heading: 'Add pathway',
                text: 'Please enter the pathway name.',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            return false;
        }
        if (patwname != '')
        {
            var lastmodifiedtime = formatAMPM(new Date());
            lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
            $("#lastsavedtime").html(lastmodifiedtime);

            var inputdata = {"pathwayName": patwname, "orgId": orgid, "teamId": 1, "createDate": currenttime, "updateDate": currenttime};

            $.ajax({
                url: pathwayapibase + '/api/v1/addPathway',
                type: 'POST',
                dataType: 'json',
                headers: {
                    'Authorization': securitytoken,
                    'Content-Type': 'application/json'
                },
                Accept: "application/json",
                data: JSON.stringify(inputdata),
                beforeSend: function ()
                {
                    $("#error").fadeOut();
                    $.LoadingOverlay("show");
                },
                success: function (response)
                {
                    $.LoadingOverlay("hide");

                    currentpathwayname = patwname;
                    if (response.statuscode == 208)
                    {

                        $.toast({
                            heading: 'Add Pathway',
                            text: 'Pathway name is already taken.',
                            textAlign: 'center',
                            position: 'top-center',
                            icon: 'warning',
                            loader: false,
                            allowToastClose: false,
                            hideAfter: 5000,
                        });
                        return false;
                    }
                    $('#addeventmodal').prop("disabled", false);

                    var patwayinfo = [];
                    patwayinfo['id'] = response.data;
                    $currentpthwayId = response.data;
                    patwayinfo['name'] = patwname;
                    pathwaycreatedts = currenttime;
                    localStorage.setItem("pathwayinfo", patwayinfo);
                    var pathwind = localStorage.getItem("pathwayinfo");

                    $isnewpathway = true;
                    $(".pathwayfilename").html(patwname);
                    $("#addpathwayModal").modal('hide');
                    $("#createevent").trigger("click");




                }
            });
            return false;

        }
    });

    $("#addpathwaytemplatemaincontent").scroll(function (e) {


        var scrollHeight = $("#addpathwaytemplatemaincontent").prop('scrollHeight');
        var divHeight = $("#addpathwaytemplatemaincontent").height();
        var scrollerEndPoint = scrollHeight - divHeight;
        var divScrollerTop = $("#addpathwaytemplatemaincontent").scrollTop();
        if (divScrollerTop === scrollerEndPoint)
        {


            for (var rince = 0; rince < 50; rince++)
            {
                $('#pathwaytble tbody').append($("#pathwaytble tbody tr:last").clone());
                var twlen = ($('#pathwaytble tbody tr').length);
                var adr = 'Day ' + twlen;
                $("#pathwaytble tbody tr:last td:first").html(adr);
                var trtdlast = $("#pathwaytble tbody tr:last");
                filluprows(trtdlast, twlen);
            }
        }

    });

    var track_page = 1;
    $(window).scroll(function () {
        if ($(window).scrollTop() + $(window).height() >= $(document).height()) {


        }
    });


    $('.btn-number').click(function (e) {
        e.preventDefault();
        fieldName = $(this).attr('data-field');
        type = $(this).attr('data-type');
        var input = $("input[name='" + fieldName + "']");
        var currentVal = parseInt(input.val());
        if (!isNaN(currentVal)) {
            if (type == 'minus') {

                if (currentVal > input.attr('min')) {
                    input.val(currentVal - 1).change();
                }
                if (parseInt(input.val()) == input.attr('min')) {
                    $(this).attr('disabled', true);
                }
            } else if (type == 'plus') {
                if (currentVal < input.attr('max')) {
                    input.val(currentVal + 1).change();
                }
                if (parseInt(input.val()) == input.attr('max')) {
                    $(this).attr('disabled', true);
                }
            }
        } else {
            input.val(0);
        }
    });
    $('.input-number').focusin(function () {
        $(this).data('oldValue', $(this).val());
    });
    $('.input-number').change(function () {
        minValue = parseInt($(this).attr('min'));
        maxValue = parseInt($(this).attr('max'));
        valueCurrent = parseInt($(this).val());
        name = $(this).attr('name');
        if (valueCurrent >= minValue) {
            $(".btn-number[data-type='minus'][data-field='" + name + "']").removeAttr('disabled')
        } else {

            $.toast({
                heading: 'Add Pathway',
                text: 'Sorry, the minimum value was reached',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            $(this).val($(this).data('oldValue'));
        }
        if (valueCurrent <= maxValue) {
            $(".btn-number[data-type='plus'][data-field='" + name + "']").removeAttr('disabled');

            if (valueCurrent >= 100 && name == "repeatdays")
            {
                $("#createblockrepeatpls").attr('disabled', true);
            }
            if (valueCurrent < 100 && name == "repeatdays")
            {
                $("#createblockrepeatpls").removeAttr('disabled');
            }
            if (valueCurrent >= 100 && name == "occurrences")
            {
                $("#createblockoccurrencespls").attr('disabled', true);
            }
            if (valueCurrent < 100 && name == "occurrences")
            {
                $("#createblockoccurrencespls").removeAttr('disabled');
            }


        } else {
            $.toast({
                heading: 'Add Pathway',
                text: 'Sorry, the maximum value was reached',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                allowToastClose: false,
                hideAfter: 5000,
            });
            $(this).val($(this).data('oldValue'));
        }
    });
    $(".input-number").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 190]) !== -1 ||
                // Allow: Ctrl+A
                        (e.keyCode == 65 && e.ctrlKey === true) ||
                        // Allow: home, end, left, right
                                (e.keyCode >= 35 && e.keyCode <= 39)) {
                    // let it happen, don't do anything
                    return;
                }
                // Ensure that it is a number and stop the keypress
                if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                    e.preventDefault();
                }


            });
    //End of add block here
});


function filluprows(trtdlast, twlen) {
    trtdlast.find("td").each(function (innerlen, val) { //get all rows in table



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


function updatePathwayInfo(pid, pname)
{
    var udate = new Date();
    var dfort = udate.getFullYear() + '-' + udate.getMonth() + '-' + udate.getDate() + ' ' + udate.getHours() + ':' + udate.getMinutes() + ':' + udate.getSeconds();

    var inputdata = {"pid": pid, "utime": dfort, "pname": pname};

    $.ajax({
        url: pathwayapibase + '/api/v1/updatepathwayname',
        type: 'POST',
        dataType: 'json',
        headers: {
            'Authorization': securitytoken,
            'Content-Type': 'application/json'
        },
        Accept: "application/json",
        data: JSON.stringify(inputdata),
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

                $(".changepathwayname").text(pname);
                $(".inpchangepathwayname").css("display", "none");
                $(".changepathwayname").css("display", "block");
                var lastmodifiedtime = moment(udate).tz('America/New_York').format('LT');
                lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
                $("#lastsavedtime").html(lastmodifiedtime);
                $.toast({
                    heading: 'Add Pathway',
                    text: 'Pathway  updated.',
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