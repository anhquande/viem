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


    $('form[data-async]').on('submit', function(event) {
		var $form = $(this);
		$('img[name=loading-indicator]',$form).show();
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
	            	$('img[name=loading-indicator]').hide();
	            	$('span[name=loading-msg]').html("").show();
	            	
	            	if (jsonObj.errno==0){
	            		$('a[name=previewLink]',$form).val(jsonObj.pageName);
	            		$('input[name="name"]',$form).val(jsonObj.entityName);
	            			            		
	            		$('span[name=loading-msg]').html("<i class='fa fa-check'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            	else{
	            		$('span[name=loading-msg]').html("<i class='fa fa-warning'></i> "+jsonObj.errmsg).fadeOut(2000);
	            	}
	            }
	        });
		}
			
        event.preventDefault();
    });
});