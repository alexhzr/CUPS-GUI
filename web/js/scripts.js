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
					$( "<p></p>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Borrar'/>" ).appendTo( this );
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
}

function deleteItem(item) {
	$(item).parent().remove();
}