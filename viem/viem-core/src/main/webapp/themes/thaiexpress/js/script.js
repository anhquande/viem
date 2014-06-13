
jQuery(function($) {
	$('.tooltip-container').tooltip({
	        selector: "[data-toggle=tooltip]",
	        container: "body"
	    });
	// delegate calls to data-toggle="lightbox"
    $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
        event.preventDefault();
        return $(this).ekkoLightbox({
            onShown: function() {
                if (window.console) {
                    return console.log('Checking our the events huh?');
                }
            }
        });
    });

    //Programatically call
    $('#open-image').click(function (e) {
        e.preventDefault();
        $(this).ekkoLightbox();
    });
    $('#open-youtube').click(function (e) {
        e.preventDefault();
        $(this).ekkoLightbox();
    });
	
	$('select[rating]').barrating('show', {
		showSelectedRating:true
	});
	
	$('#slider').radiosToSlider();
	
    $( "input[spinner]" ).spinner({
      spin: function( event, ui ) {
        if ( ui.value < 1 ) {
          $( this ).spinner( "value", 1 );
          return false;
        }
      }
    });


});