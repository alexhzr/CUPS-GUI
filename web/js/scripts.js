$(document).ready( function() {
    $("#new-printer-dialog").hide();

    $("#error-alert").hide();

    //Draggable y Droppable
    $( "#policy-menu" ).draggable();

    $("#policy-menu p").draggable({
        helper: "clone"
    });
    // buttons
        $("#add-printer-button").button({
                icons: { primary: "ui-icon-plusthick", secondary: null }
        }).click(function() {
                $("#new-printer-dialog").show("blind");
        });

        preparePrinterList();
    //
});

function preparePrinterList() {

    $(".policy-statements div").droppable({
        drop: function( event, ui ) {
            var clase = ui.draggable.context.className;
            if (clase.search("policy-option") !== -1) {					
                $( "<p></p>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Delete'/>" ).appendTo( this );

            }
        }
    });
    
    $("#exitButton").button({
            icons: { primary: "ui-icon-power", secondary: null }
    });

    $("input[name='new-printer-submit']").button().click(function() {
        uploadPPD();
    });

    $("input[name='new-printer-cancel']").button().click(function() {
        $("#new-printer-dialog").hide("blind");
    });

    $(".delete-printer-button").button({
        icons: { primary: "ui-icon-trash", secondary: null }
    });

    $(".show-hide-button").button();

    $(".policy-statements").tabs();
}

function showHide(id) {
    $("#"+id).toggle("blind");
    $("#policy-menu").toggle("drop");
    $("html, body").animate({
            scrollTop: $("#"+id).offset().top/2
    }, 1000);
}

function deleteItem(item) {
    $( "#delete-printer-confirm" ).dialog({
        modal: true,
        buttons: {
            "Accept": function() {
                deletePrinter($(item).parent().attr("id"));
                $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function uploadPPD() {
    var data = new FormData();
            jQuery.each(jQuery("input[name='new-priner-driver-file']")[0].files, function(i, file) {
            data.append('file-'+i, file);
        });

        jQuery.ajax({
            url: 'MainServlet',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(data){
                alert(data);
            },
            error: function() {
                $("#error-alert").show();
                $("#error-alert").fadeOut(4000);
            }
        });
}

function listPrinter() {
    $.ajax({
       type: "POST",
       url: "MainServlet",
       data: "op=hjfksdf7&fromAjax=yes",
       success: function(msg) {
            $("#printerList").html(msg);
            preparePrinterList();
       }
    });
}

function deletePrinter(pName) {
    $.ajax({
        type: "POST",
        url: "MainServlet",
        data: "op=2okx0wwx&pName="+pName,
        success: function(threadName){
            var interval = setInterval(function() {askFinished(threadName, interval);}, 1000);
        },
        error: function() {
            $("#error-alert").show();
            $("#error-alert").fadeOut(4000);
        }
    });
}
var pruebaJSON;
function askFinished(threadName, interval) {
    $.ajax({
        type: "POST",
        url: "MainServlet",
        data: "op=83gdhxi2&threadName="+threadName,
        success: function(data){
            if (data.isFinished) {
                clearInterval(interval);
                listPrinter();
            }
        }
    });    
}