jQuery(function($) {
		
	    // tooltip demo
	    $('.tooltip-container').tooltip({
	        selector: "[data-toggle=tooltip]",
	        container: "body"
	    });
	    
	    	$('#uploadForm').hide();
	    	var bar = $('.bar');
			var percent = $('.percent');
			var status = $('#status');
			   
			$('form[fileupload-async]').ajaxForm({
			    beforeSend: function() {
			        status.empty();
			        var percentVal = '0%';
			        bar.width(percentVal)
			        percent.html(percentVal);
			    },
			    uploadProgress: function(event, position, total, percentComplete) {
			        var percentVal = percentComplete + '%';
			        bar.width(percentVal)
			        percent.html(percentVal);
			    },
			    success: function() {
			        var percentVal = '100%';
			        bar.width(percentVal)
			        percent.html(percentVal);
			        location.reload();
			    },
				complete: function(xhr) {
					status.html(xhr.responseText);
				}
			}); 

	    
	    $('form[toolbar-async]').on('submit', function(event) {
			var $form = $(this);
			var $bt = $( ":input[type=submit]:focus" ,$form);
			
			$('span[name=loading-indicator]',$form).show();
	        $('span[name=loading-msg]',$form).html("");
	        
	    	
	    	if ($bt.attr('name') == 'btClearAll'){
	    		$('input[name=action]',$form).val('clearAll');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Clear successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}	
			
			if ($bt.attr('name') == 'btImport'){
				if ($('#uploadForm').is(':visible'))
	    			$('#uploadForm').hide();
	    		else
	    			$('#uploadForm').show();
				$('span[name=loading-indicator]',$form).hide();
			}	
			
			event.preventDefault();
	    });
    
	    $('form[data-async]').on('submit', function(event) {
			
			var $form = $(this);
			$('span[name=loading-indicator]',$form).show();
	        $('span[name=loading-msg]',$form).html("");
	        
	        var $bt = $( ":input[type=submit]:focus" ,$form);

	    	if ($bt.attr('name') == 'btDelete'){
	    		$('input[name=action]',$form).val('delete');
	    		$.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Deleted successfully!");
		            	$('textarea[name=value]',$form).val(jsonObj.value);
		            	$form.parent().remove();
		            }
		        });
	    	}						
	    	else if ($bt.attr('name') == 'btDuplicate'){
	    		$('input[name=action]',$form).val('duplicate');
	    		$.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("");
						$form.parent().parent().append(jsonObj.newItem);
		            }
		        });
	    	}
	    	else if ($bt.attr('name') == 'btVisible'){
	    		$('input[name=action]',$form).val('toggleVisibility');
	    		$.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("");
						if (jsonObj.visible) {
							$bt.html('<i class="fa fa-volume-up" style="color:black"></i>');
						}
						else
							$bt.html('<i class="fa fa-volume-off" style="color:gray"></i>');
						
		            }
		        });
	    	}
			else if ($bt.attr('name') == 'btUpdate'){
	    		$('input[name=action]',$form).val('update');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Updated successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}		
			else if ($bt.attr('name') == 'btRestoreKey'){
	    		$('input[name=action]',$form).val('restoreKey');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Updated successfully!").addClass("alert-success");
		            }
		        });
			}	
			else if ($bt.attr('name') == 'btSetKey'){
	    		$('input[name=action]',$form).val('setKey');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('span[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Updated successfully!").addClass("alert-success");
		            }
		        });
			}				

	        event.preventDefault();
	    });
});