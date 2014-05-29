jQuery(function($) {
		
	    // tooltip demo
	    $('.tooltip-container').tooltip({
	        selector: "[data-toggle=tooltip]",
	        container: "body"
	    })
    
		    $('form[data-async]').on('submit', function(event) {
				
				var $form = $(this);
 				$('img[name=loading-indicator]',$form).show();
		        $('span[name=loading-msg]',$form).html("");
		        
		        var $bt = $( ":input[type=submit]:focus" ,$form);

		    	if ($bt.attr('name') == 'btSave'){
		    		$('input[name=action]',$form).val('update');
		    		$.ajax({
			        	dataType: "json",
			            type: $form.attr('method'),
			            url: $form.attr('action'),
			            data: $form.serialize(),
			            success: function(jsonObj, status) {
			            	$('img[name=loading-indicator]',$form).hide();
			            	
			            	if (jsonObj.errno==0){
			            		$('a[name=previewLink]',$form).val(jsonObj.pageName);
			            		$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> "+jsonObj.errmsg).removeClass().addClass("alert alert-success");
			            	}
			            	else{
			            		$('span[name=loading-msg]',$form).html("<i class='fa fa-warning'></i> "+jsonObj.errmsg).removeClass().addClass("alert alert-danger");
			            	}
			            }
			        });
		    	}
				if ($bt.attr('name') == 'btCancel'){
					window.history.go(-1);
				}	
		        event.preventDefault();
		    });
		});