$(document).ready( function() {

	$( "#policy-menu" ).draggable();
	
	$("#policy-menu p").draggable({
		helper: "clone"
	});
	
	$("#exitButton").button({
		icons: { primary: "ui-icon-power", secondary: null }
	});
	
	$(".show-hide-button").button();
	
	$(".policy-statements").tabs();
	
	$(".policy-statements div").droppable({
			drop: function( event, ui ) {
				var clase = ui.draggable.context.className;
				if (clase.contains("policy-option")) {					
					$( "<p></p>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Borrar'/>" ).appendTo( this );
				}
		}
	});
	
});

function showHide(id) {
	$("#"+id).toggle("blind");
	$("#policy-menu").toggle("drop");
}

function deleteItem(item) {
	$(item).parent().remove();
}