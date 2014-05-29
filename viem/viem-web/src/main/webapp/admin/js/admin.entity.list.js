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
	    	console.log("toolbar-async.onSubmit");
			var $form = $(this);
			var $bt = $( ":input[type=submit]:focus" ,$form);
			
			$('img[name=loading-indicator]',$form).show();
	        $('span[name=loading-msg]',$form).html("");
	        
	    	
	    	if ($bt.attr('name') == 'btClearAll'){
	    		$('input[name=action]',$form).val('clearall');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
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
				$('img[name=loading-indicator]',$form).hide();
			}	
			
	    	
	    	if ($bt.attr('name') == 'btSaveAll'){
	    		$('input[name=action]',$form).val('saveall');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Saving to cache successfully!").addClass("alert-success");
		            }
		        });
			}	
			
			if ($bt.attr('name') == 'btLoadAll'){
	    		$('input[name=action]',$form).val('loadall');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Restoring cache from database successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}	
			
			event.preventDefault();
	    });
    
	    $('form[data-async]').on('submit', function(event) {
			
			console.log("onSubmit");
			var $form = $(this);
			$('img[name=loading-indicator]',$form).show();
	        $('span[name=loading-msg]',$form).html("");
	        
	        var $bt = $( ":input[type=submit]:focus" ,$form);

	    	if ($bt.attr('name') == 'btDelete'){
	    		console.log("delete click");
	    		$('input[name=action]',$form).val('delete');
	    		$.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='glyphicon glyphicon-refresh'></i> Deleted successfully!");
		            	$('textarea[name=value]',$form).val(jsonObj.value);
		            	location.reload();
		            }
		        });
	    	}
	    	
	    	if ($bt.attr('name') == 'btUpdate'){
	    		$('input[name=action]',$form).val('update');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-check'></i> Updated successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}		 

	        event.preventDefault();
	    });
});