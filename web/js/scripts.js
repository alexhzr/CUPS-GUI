$(document).ready( function() {
	$("#policy-menu li").draggable({
		helper: "clone"
	});
});
$(".policy-statements ul").droppable({
		drop: function( event, ui ) {
			$( "<li></li>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Borrar'/>" ).appendTo( this );
	}

});
/*Oculta el hide por el cual el id será igual que el que le pase por la funcion onclick*/
function hideDiv(id) {
	$("#"+id).hide("slow");
}

function showDiv(id) {
	$("#"+id).show("slow");
}

function deleteItem(item) {
	$(item).parent().remove();
}