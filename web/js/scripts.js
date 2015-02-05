$(document).ready( function() {
	//Draggable y Droppable
	$( "#policy-menu" ).draggable();
	
	$("#policy-menu p").draggable({
		helper: "clone"
	});
	
	$(".policy-statements div").droppable({
			drop: function( event, ui ) {
				var clase = ui.draggable.context.className;
				if (clase.contains("policy-option")) {					
					$( "<p></p>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Delete'/>" ).appendTo( this );
				}
		}
	});
	// buttons
	$("#exitButton").button({
		icons: { primary: "ui-icon-power", secondary: null }
	});
	
	$(".add-printer-button").button({
		icons: { primary: "ui-icon-plusthick", secondary: null }
	});
	$(".delete-printer-button").button({
		icons: { primary: "ui-icon-trash", secondary: null }
	});
	
	$(".show-hide-button").button();
	
	$(".policy-statements").tabs();
	//
});

function showHide(id) {
	$("#"+id).toggle("blind");
	$("#policy-menu").toggle("drop");
	$("html, body").animate({
		scrollTop: $("#"+id).offset().top/2
	}, 1000);
}

function deleteItem(item) {
		$( "#delete-printer-confirm" ).dialog({
			resizable: false,
			height:50,
			modal: true,
			buttons: {
				"Delete": function() {
					$(item).parent().remove();
					$( this ).dialog( "close" );
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
		});
}