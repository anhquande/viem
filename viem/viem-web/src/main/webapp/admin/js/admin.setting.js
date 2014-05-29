jQuery(function($) {
		
	    // tooltip demo
	    $('.tooltip-container').tooltip({
	        selector: "[data-toggle=tooltip]",
	        container: "body"
	    })
    
    	var bar = $('.bar');
		var percent = $('.percent');
		var status = $('#status');
		   
		$('form[fileupload-async]').ajaxForm({
		    beforeSend: function() {
		    	console.log("before send");
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
		    	console.log("success");
		        var percentVal = '100%';
		        bar.width(percentVal)
		        percent.html(percentVal);
		      //  location.reload();
		    },
			complete: function(xhr) {
		    	console.log("complete");

				status.html(xhr.responseText);
			}
		}); 
		
		$('form[toolbar-async]').on('submit', function(event) {
	    	console.log("toolbar-async.onSubmit");
			var $form = $(this);
			var $bt = $( ":input[type=submit]:focus" ,$form);
			
			$('img[name=toolbar-loading-indicator]',$form).show();
	        $('span[name=toolbar-loading-msg]',$form).html("");
	        
	        console.log($bt.attr('name'));
	    	if ($bt.attr('name') == 'btClearAll'){
	    		console.log("clear All ...");
	    		$('input[name=action]',$form).val('clearall');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=toolbar-loading-indicator]',$form).hide();
		            	$('span[name=toolbar-loading-msg]',$form).html("<i class='fa fa-check'></i> Clear Pages successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}	
			
			if ($bt.attr('name') == 'btImport'){
				console.log("btImport clicked");
				if ($('#uploadForm').is(':visible'))
	    			$('#uploadForm').hide();
	    		else
	    			$('#uploadForm').show();
				$('img[name=toolbar-loading-indicator]',$form).hide();
				console.log("done");
			}	
			
	    	
	    	if ($bt.attr('name') == 'btSaveAll'){
	    		$('input[name=action]',$form).val('saveall');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=toolbar-loading-indicator]',$form).hide();
		            	$('span[name=toolbar-loading-msg]',$form).html("<i class='fa fa-check'></i> Saving to cache successfully!").addClass("alert-success");
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
		            	$('img[name=toolbar-loading-indicator]',$form).hide();
		            	$('span[name=toolbar-loading-msg]',$form).html("<i class='fa fa-check'></i> Restoring cache from database successfully!").addClass("alert-success");
		            	location.reload();
		            }
		        });
			}	
			
			event.preventDefault();
	    });
	    		      
	    $('form[data-async]').on('submit', function(event) {
			
			var $form = $(this);
			$('img[name=loading-indicator]',$form).show();
	        $('span[name=loading-msg]',$form).html("");
	        
	        var $bt = $( ":input[type=submit]:focus" ,$form);

	    	if ($bt.attr('name') == 'btReset'){
	    		$('input[name=action]',$form).val('restore');
	    		$.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-refresh'></i> Restored to default value!");
		            	$('textarea[name=value]',$form).val(jsonObj.value);
		            }
		        });
	    	}
	    	if ($bt.attr('name') == 'btSave'){
	    		$('input[name=action]',$form).val('update');
		        $.ajax({
		        	dataType: "json",
		            type: $form.attr('method'),
		            url: $form.attr('action'),
		            data: $form.serialize(),
		            success: function(jsonObj, status) {
		            	$('img[name=loading-indicator]',$form).hide();
		            	$('span[name=loading-msg]',$form).html("<i class='fa fa-save'></i> Saved successfully!").addClass("alert-success");
		            }
		        });
			}		 
	        event.preventDefault();
	    });
	});