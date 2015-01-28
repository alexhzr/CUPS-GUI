$("#policy-menu li").draggable({
	helper: "clone"
});
$(".policy-statements ul").droppable({
	drop: function( event, ui ) {
		$( "<li></li>" ).html( ui.draggable.text() + "<input type='button' onclick='deleteItem(this)' value='Borrar'/>" ).appendTo( this );
	}
});
function deleteItem(item) {
	$(item).parent().remove();
}


$( "button" ).click(function() {
 $( ".policy-statements" ).show( "slow" );
});