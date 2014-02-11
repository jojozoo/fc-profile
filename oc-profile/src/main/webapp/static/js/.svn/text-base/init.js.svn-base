jQuery(function($) {
	// search
	$('#header_search_button').click(function() {
		var searchInput = $.trim($('#header_search_input').val());
		if (!searchInput) {
			PU.alertDialog('请输入搜索内容');
			return;
		}
		$('#header_search_form').submit();
	});

	// ajax-form
	$('form[node-type="ajax-form"]').each(function() {
		var form = $(this);
		form.ajaxform();
	});
	
	// node-type="submit"
	$('[node-type="submit"]').click(function(){
		var btn = $(this);
		var form = btn.parents('form');
		if(!form.length){
			return;
		}
		var action = btn.attr('form-action');
		var method = btn.attr('form-method');
		if(action!=null){
			form.attr('action',action);
		}
		if(method!=null){
			form.attr('method',method);
		}
		if(!btn.is(":submit")){
			form.submit();
		}
		return true;
	});
});