$(document).ready( function() {
	$("#policy-menu p").draggable({
		helper: "clone"
	});
	
	$("#exitButton").button({
		icons: { primary: "ui-icon-power", secondary: null }
	});
	
	$(".show-hide").button();
	
	$(".policy-statements").tabs();
	
	$(".policy-statements div").droppable({
			drop: function( event, ui ) {
				$( "<p></p>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Borrar'/>" ).appendTo( this );
		}
	});
	
});

function showHide(id) {
	$("#"+id).toggle("slow");
	$("#policy-menu").toggle("slide");
}

function deleteItem(item) {
	$(item).parent().remove();
}