jQuery(function($) {
	var defaultTinymceOptions = {
		language : 'zh-cn',
		script_url : 'http://cdnjs.cloudflare.com/ajax/libs/tinymce/4.0.21/tinymce.min.js',
		theme_advanced_buttons1:"",
		theme_advanced_buttons2:"",
		theme_advanced_buttons3:"",
		//
		__end__:true
		
	};
	var tinymceOptions = $.extend({}, defaultTinymceOptions, window.pageTinymceOptions || {});

	var simpleTinymceOptions = {
		theme : 'simple',
		//
		__end__:true
	};
	$('textarea.simple-tinymce').tinymce(
			$.extend({}, tinymceOptions, simpleTinymceOptions));

	var normalTinymceOptions = {
		theme : "advanced",
		plugins : "style,layer,table,template",
		// Theme options
		theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,formatselect,fontsizeselect,|,bullist,numlist,|,tablecontrols,|,template",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "none",
		theme_advanced_resizing : true,
		//
		__end__:true
	};
	$('textarea.tinymce').tinymce(
			$.extend({}, tinymceOptions, normalTinymceOptions));

	var richTinymceOptions = {
		// General options
		theme : "advanced",
		plugins : "pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",

		// Theme options
		theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
		theme_advanced_buttons2 : "search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
		theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
		theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : true,
		//
		__end__:true
	};
	$('textarea.rich-tinymce').tinymce(
			$.extend({}, tinymceOptions, richTinymceOptions));
});