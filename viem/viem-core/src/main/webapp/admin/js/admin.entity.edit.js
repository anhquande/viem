jQuery(function($) {
		
	// Text Editor
	$(".editor").jqte();
	
    // tooltip demo
    $('.tooltip-container').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    })
    
    $('form[data-async] :input[change-listener]').change(function(){
    	var $form =  $('form[data-async]');
        $form.submit();  
		
    });


	$('form[product2category]').on('submit', function(event) {
		var $form = $(this);
		$('span[name=loading-indicator]',$form).show();
        $('span[name=loading-msg]',$form).html("");

        var $bt = $( ":input[type=submit]:focus" ,$form);
		
		$( "select" ,$form).find('option').each(function() {    		
			$(this).attr('selected', true);
    	});

		if ($bt.attr('name') == 'btSaveSelectedProducts'){
			$('input[name=action]',$form).val('saveSelectedProducts');
    		$.ajax({
	        	dataType: "json",
	            type: $form.attr('method'),
	            url: $form.attr('action'),
	            data: $form.serialize(),
	            success: function(jsonObj, status) {
	            	$('span[name=loading-indicator]', $form).hide();
	            	$('span[name=loading-msg]', $form).html("").show();
	            	
	            	if (jsonObj.errno==0){
	            		$('a[name=previewLink]',$form).val(jsonObj.pageName);
	            		$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            	else{
	            		$('span[name=loading-msg]',$form).html("<i class='fa fa-warning'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            }
	        });
		}
		
		event.preventDefault();
	});
	
    $('form[data-async]').on('submit', function(event) {
		var $form = $(this);
		$('span[name=loading-indicator]',$form).show();
        $('span[name=loading-msg]',$form).html("");

        var $bt = $( ":input[type=submit]:focus" ,$form);

		if ($bt.attr('name') == 'btCancel'){
			window.history.go(-1);
		}	
		else{
			$('input[name=action]',$form).val('update');
    		$.ajax({
	        	dataType: "json",
	            type: $form.attr('method'),
	            url: $form.attr('action'),
	            data: $form.serialize(),
	            success: function(jsonObj, status) {
	            	$('span[name=loading-indicator]', $form).hide();
	            	$('span[name=loading-msg]', $form).html("").show();
	            	
	            	if (jsonObj.errno==0){	            			            		
	            		$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            	else{
	            		$('span[name=loading-msg]',$form).html("<i class='fa fa-warning'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            }
	        });
		}
			
        event.preventDefault();
    });
});