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
    {startp: 96, endp: 100},
    {startp: 101, endp: 105},
    {startp: 106, endp: 110},
    {startp: 111, endp: 115},
    {startp: 116, endp: 120},
    {startp: 121, endp: 125},
    {startp: 126, endp: 130},
    {startp: 131, endp: 135},
    {startp: 136, endp: 140},
    {startp: 141, endp: 145},
    {startp: 146, endp: 150},
    {startp: 151, endp: 155},
    {startp: 156, endp: 160},
    {startp: 161, endp: 165},
    {startp: 166, endp: 170},
    {startp: 171, endp: 175},
    {startp: 176, endp: 180},
    {startp: 181, endp: 185},
    {startp: 186, endp: 190},
    {startp: 191, endp: 195},
    {startp: 196, endp: 200},
    {startp: 101, endp: 105}


];

var currentdate = new Date();
var currenttime = currentdate.getTime();
var currentpathwayname = "";
var securitytoken = '';
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
var $currentcolor = 'color1';
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


$('document').ready(function ()
{
    if (navigator.userAgent.indexOf('Mac') > 0)
    {
        $('body').addClass('mac-os');
    }



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

    if (output.userType == 'U')
    {

        $("#addeventmodal").prop('disabled', true);
    }

    $(".lguser").html(output.fullName);

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
    var lastmodifiedtime = new Date();
    // lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
    // $("#lastsavedtime").html(lastmodifiedtime);
    lastmodifiedtime = moment(lastmodifiedtime).tz('America/New_York').format('LT');
    lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
    $("#lastsavedtime").html(lastmodifiedtime);
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

        var inputdata = {"id": pid, "pathwayName": currentpathwayname, "orgId": output.orgid, "teamId": 1, "status": "Y"};

        $.toast({
            heading: 'Save Pathway',
            text: 'Pathway has been saved successfully.',
            position: 'top-center',
            loader: false,
            icon: 'success',
            stack: true
        });
        var lastmodifiedtime = new Date();
        lastmodifiedtime = moment(lastmodifiedtime).tz('America/New_York').format('LT');
        lastmodifiedtime = 'Last saved at ' + lastmodifiedtime;
        $("#lastsavedtime").html(lastmodifiedtime);
        //Save Pathway Here


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
                    heading: 'Error',
                    textAlign: 'center',
                    text: e.message(),
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    stack: true
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
                $('table').find('tr').each(function (trindex, trvals) {

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
                    heading: 'Error',
                    textAlign: 'center',
                    text: e.message(),
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    stack: true
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
            $("#triggerdays").prop("disabled", true);

        } else {


            $("#pathwaydelivery button").prop("disabled", false);
            $("#triggerdays").prop("disabled", false);

        }
    });

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
    var pid = getUrlParameter('pathwayid');

    var pdata = {"id": pid};

    var maxRowNumber = 0;
    var maxColNumber = 0;
    $blocktype = 'M';
    /**
     * Load the Pathway Using QC API call
     * for Pathway Edit Operations
     *  After Loading the Pathway we are getting the information like
     *  Pathway Information, Events Information and Block Information
     *  Based on the Pathway Event Row and Col postions  the system will create 
     *  the Pathway Table by taking the Max row number from the Pathway Information
     *  Agaian checking the condition if the max pathway event block max row information
     *  is lessthat 16 then the max row informaiton is treated as 16 i.e by default the 
     *  system will render 16 rows in case less max row number.
     *   For Events and Block placement the system will use
     *   renderevent and render block functions which will the 
     *   event and blocks on respective places.
     *   While placing the events and blocks the system will check for the postions based on blocrow
     *   and block columns which are taken from API response. The Pathway system shows 
     *   5 cols for page i.e 5 factorial process in case of there is number which not fits
     *   into 5 factorial then the system will shows like event (number) (header name)....etc
     *   For Ex: I have 3 Events for Pathway then table event header looks like
     *   Welcome, Hyper, Heart, Event4, Event5....etc.
     *   
     *   In case of block rendering the system will check for the no of occurences 
     *   along the postion and display the block along with content.
     * 
     * 
     */
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
            currentpathwayname = pathswaysdata.pathwayName;
            var blocarrs = [];
            var blocarrscol = [];
            $('.pathwayfilename').html(pathswaysdata.pathwayName);


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

                    theaderht += '<th data-colst="' + eventhead + '" class="tbheader" data-name="header1">Event ' + (eventhead + 1) + '</th>';
                }
                $("#headerrow").append(theaderht);
            } else
            {
                $("#headerrow").append(theaderht);
            }

            maxRowNumber = Math.max.apply(Math, blocarrs);
            maxColNumber = Math.max.apply(Math, blocarrscol);



            var bthtml = '';
            if (maxRowNumber < 16)
            {
                maxRowNumber = 16;
            }
            for (var tbrw = 1; tbrw <= maxRowNumber; tbrw++)
            {
                bthtml += '<tr>';
                bthtml += '<td>Day ' + tbrw + '</td>';
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

            renderevent(pathswaysdata, maxRowNumber);


            splitforpagination();


        }
    });
    /**
     * Event Modal Popup 
     * By Clicking on the table header 
     * the event modal popup is getting showed.
     * Checking the If there is an already event existence of 
     * header clicked event
     * 
     */
    $("#headerrow").on('click', 'th', function () {
        if ($(this).children().length > 0) {

            var ename = $(this).children().text();

            var erw = $(this).index();
            var eveid = $("#pathwaytemplatemaincontent tr td:nth-child(2) div").data('eventid');

            $("#eventeditid").val(eveid);
            $("#eventeditcolindex").val(erw);

            $("#editeventname").val(ename);
            return;
        } else
        {
            if (output.userType == 'A')
            {
                if ($(this).index() != 0)
                {
                    $("#addeventmodal").trigger("click");
                    document.getElementById("eventname").focus();
//                $('#eventname').focus()
                }

            }

        }

    });

    $('#pathwaytemplatemaincontent').on('click', 'tr td', function () {
        var col = $(this).parent().children().index($(this));
        if ($(this).children().length == 0 && col > 0) {
            if (output.userType == 'A')
            {

                $("#addeventmodal").trigger("click");
                $('#eventname').focus()
            }


        }

    });
    /**
     * Edit Event Modal which check for event availability
     * If the event is available then dispalying the 
     * event popup with event name and allows user to change the event name.
     * 
     */
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
    $('.modal').on('shown.bs.modal', function () {
        $(this).find('input:text:visible:first').focus();
    });
    /**
     * Add Event Modal which checks for
     * pathway availability. Incase of non-availability
     * then it will return a toast warn message
     * If the Pathway is there then It will show up
     * the modal add event modal window.s
     * 
     */
    $("#addeventmodal").click(function () {

        $("#eventname").focus();
        $("#eventname").val('');

        $("#addeventModal").modal('show');
//         $('body').attr('style', 'padding-right: 0px !important;');
//        $("body").css("overflow","visible !important");
//        $('body').css('padding-right', 'auto');
//        $('#eventname').focus();
//        $('#eventname').get(0).focus();
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
                        heading: 'Block Edit',
                        text: 'Data has been saved successfully.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'success',
                        loader: false,
                        stack: true
                    });
                    $saveblocks[currentblockforupdateid] = currenteditblock;
                    $("#editeventblockModal").modal('hide');
                }
            },
            error: function () {
                $.LoadingOverlay("hide");
                $.toast({
                    heading: 'Edit Block',
                    text: 'Updation failed.',
                    textAlign: 'center',
                    position: 'top-center',
                    icon: 'error',
                    loader: false,
                    stack: true
                });

            }
        });


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
     * Event Creation functionality
     * Check point: Checking for the pathway creation or not
     * If there is not pathway while creating the event then 
     * the system will repond with some error taost message.
     * In case of the pathway existence then the system will create
     * an event using QC API call.
     * And alos the system will check for the dupliacte name with status code 208 which was getting
     * as API response.
     */
    $("#createevent").on('click', function () {

        var nexteventcol = cuurentfilledeventcolpos;

        var totalrws = $("#pathwaytble  tr").length;


        var eventstartrw = 1;
        nexteventcol = nexteventcol + 1;


        var eventname = $("#eventname").val();

        if (eventname == '')
        {
            alert('Please Enter the Event Name');
            $.toast({
                heading: 'Add Event',
                text: 'Please Enter the Event Name.',
                textAlign: 'center',
                position: 'top-center',
                icon: 'warning',
                loader: false,
                stack: true
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

        var createeventobj = {
            "eventName": eventname,
            "pathwayId": pid,
            "teamId": 1,
            "eventPocRow": eventstartrw,
            "eventPocCol": nexteventcol,
            "createDate": currenttime
        };

        if (eventcolor == 'color1')
        {
            eventcolor = 'color2';
        }
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

        $.ajax({
            url: pathwayapibase + '/api/v1/addEvent',
            type: 'POST',
            dataType: 'json',
            headers: {
                'Authorization': securitytoken,
                'Content-Type': 'application/json'
            },
            Accept: "application/json",
            data: JSON.stringify(createeventobj),
            beforeSend: function ()
            {
                $("#error").fadeOut();
                $.LoadingOverlay("show");
            },
            success: function (response)
            {
                $.LoadingOverlay("hide");

                var eventid = response.data;

                if (response.statuscode == 208)
                {



                    $.toast({
                        heading: 'Add Event',
                        text: 'Event name is already taken.',
                        textAlign: 'center',
                        position: 'top-center',
                        icon: 'warning',
                        loader: false,
                        allowToastClose: false,
                        hideAfter: 5000,
                    });
                    return false;
                }
                cuurentfilledeventcolpos = createeventobj.eventPocCol;
                var eventobj = {};
                eventobj.id = eventid;
                eventobj.name = createeventobj.eventName;
                $createdevents.push(eventobj);


                for (var k = eventstartrw; k <= totalrws; k++)
                {

                    var rowcolval = k + '-' + nexteventcol;
                    var idval = rowcolval + "-blockcell";

                    var eventclass = 'addblock' + ' ' + eventbg;
                    $currentcolor = eventcolor;
                    $('#pathwaytble tr:nth-child(' + k + ')').find('td:eq(' + nexteventcol + ')').html('<div class="' + eventclass + '" id="' + idval + '" data-eventid="' + eventid + '" data-rowcol="' + rowcolval + '"></div>');
                }



                var eventheaderclass = 'header-' + eventcolor;
                var eventheader = '<div class="' + eventheaderclass + '">' + eventname + '</div>';
                $('#pathwaytble tr').find('th:eq(' + nexteventcol + ')').html(eventheader);

                //current page redirection
//                if(nexteventcol <=5)
//                {
//                    
//                    //move to previous page
//                    $("#previouscols").trigger("click");
//                }
                updatePathwayInfo(pid, currentpathwayname);
            },
            error: function () {
                console.log('IN error @ Mode');
            }
        });

        $("#addeventModal").modal('hide');

    });


    var currentblockclick;
    var $currenteventid;
    var $blocktype;
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

    $(document).on('click', 'td .newcreateblock', function (e) {

        e.stopPropagation();
        $('#addeventblockModal').modal('hide');
        if ($(this).data('blockid'))
        {
//            $('#addeventblockModal').modal('hide');
        } else
        {
            $('#addeventblockModal').modal('show');
        }

        var currenteditblock = $saveblocks[$(this).data('blockid')];



        var btype = currenteditblock.blockType;
        var phisec = currenteditblock.phiSecured;
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
                if (phisec == 'yes')
                {
                    $("#reminderphisecured").prop('checked', true);

                } else
                {
                    $("#reminderphisecured").prop('checked', false);
                }
            }
            if (currenteditblock.followupOfMessage != 'no message')
            {
                $("#editfollowupmessage").css('display', 'block');
                $("#editremindermessage").css('display', 'none');
                if (phisec == 'yes')
                {

                    $("#followphisecured").prop('checked', true);

                } else
                {
                    $("#followphisecured").prop('checked', false);
                }
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
        $("body").addClass('modal-open');
        return;




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
        $blocktype = 'M';
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

    $("div").on('click', '#addblocsumbtn', function (ev) {


        $.LoadingOverlay("hide");
        ev.stopPropagation();

        var blockname = $("#blockname").val();
        if (blockname == '')
        {

            $.toast({
                heading: 'Add Block',
                text: 'Please enter a Block Name.',
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
                "pathwayId": pid,
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
                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                            var blockname = 'Follow Up Message';
                            var inputBloc = {
                                "blockName": blockname,
                                "blockType": $blocktype,
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                "remainderOfMessage": reminderphisecuredinput,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                                "pathwayId": pid,
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
                                    updatePathwayInfo(pid, currentpathwayname);
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
                        heading: 'Add Appointment',
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
                        "pathwayId": pid,
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
                            updatePathwayInfo(pid, currentpathwayname);
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
                    "pathwayId": pid,
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
                                var blkclass = 'newcreateblock';
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
//                            var blkclass = 'newcreateblock appointmentblock';
                                var blkclass = 'newcreateblock';

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



                        updatePathwayInfo(pid, currentpathwayname);

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
                heading: 'Edit Pathway',
                text: 'Sorry, the minimum value was reached.',
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
                $("#editpcreateblockrepeatpls").attr('disabled', true);
            }
            if (valueCurrent < 100 && name == "repeatdays")
            {
                $("#editpcreateblockrepeatpls").removeAttr('disabled');
            }
            if (valueCurrent >= 100 && name == "occurrences")
            {
                $("#editpcreateblockoccurrencespls").attr('disabled', true);
            }
            if (valueCurrent < 100 && name == "occurrences")
            {
                $("#editpcreateblockoccurrencespls").removeAttr('disabled');
            }


        } else {

            $.toast({
                heading: 'Edit Pathway',
                text: 'Sorry, the maximum value was reached.',
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

                    return;
                }

                if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                    e.preventDefault();
                }
                if ($("#repeatdays").val() >= 100)
                {
                    $("#editpcreateblockrepeatpls").attr('disabled', true);
                } else
                {
                    $("#editpcreateblockrepeatpls").removeAttr('disabled');

                }
            });


    $("#pathwaytemplatemaincontent").scroll(function (e) {
        var scrollHeight = $("#pathwaytemplatemaincontent").prop('scrollHeight');
        var divHeight = $("#pathwaytemplatemaincontent").height();
        var scrollerEndPoint = scrollHeight - divHeight;
        var divScrollerTop = $("#pathwaytemplatemaincontent").scrollTop();
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


function renderevent(pathswaysdata, maxRowNumber) {


    var eventcolorarr = ['color2', 'color3'];
    pathswaysdata.events.sort(function (a, b) {
        return a.id - b.id;
    });
    $.each(pathswaysdata.events, function (index, pathwayeventinfo) {
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
        var eventheader = '<div  class="' + eventheaderclass + '">' + pathwayeventinfo.eventName + '</div>';

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
        renderblock(pathwayeventinfo.blocks);
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
//            var bcellclass = "newcreateblock appointmentblock";
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
        if (block.noofOccurence > 0)
        {
            var ocblocknameht = '<p style="font-size:10px;color:#fff;">Every ' + block.repeatForNoOfDays + ' Days | End After ' + block.noofOccurence + ' Occurrences</p>';
        } else
        {
            var ocblocknameht = '<p style="font-size:10px;color:#fff;">Last Occurance</p>';

        }
        var bhtm = '<div id="newblock" style="float:left;width:' + blockstylewidth + ';" data-blockid="' + block.id + '" data-eventid="' + block.eventId + '" class="' + bcellclass + '"><h3>' + block.blockName + '</h3>' + ocblocknameht + '</div>';
        $("#" + bcreatid).append(bhtm);

    });
}
function logout() {


    localStorage.clear();

    window.location.href = "index.html";

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