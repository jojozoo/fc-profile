(function($) {
	var formMessagePrefix = "fv_";

	var fillMessages = function(options, form, errors, warnings, infos) {
		var messages = {};// <prefix><field-name>:{type:'error|warning|info',message:'<message>'}
		var fillMessages = function(type, inputMessagesMap) {
			if (!inputMessagesMap) {
				return;
			}
			for ( var key in inputMessagesMap) {
				var inputMessages = inputMessagesMap[key];
				if (!inputMessages || !inputMessages.length || key in messages) {
					continue;
				}
				messages[key] = {
					type : type,
					content : inputMessages[0]
				};
			}
		}
		fillMessages('error', errors);
		fillMessages('warning', warnings);
		fillMessages('info', infos);
		return messages;
	};

	var showFieldMessages = function(options, form, messages) {
		if (!messages) {
			return;
		}
		var messageObjs = form.find('[node-type="field-message"][message-for]');
		if (!messageObjs.length) {
			return;
		}
		messageObjs.each(function() {
			var messageObj = $(this);
			var fieldName = messageObj.attr('message-for');
			if (fieldName == null) {
				return;
			}
			var message = messages[formMessagePrefix + fieldName];
			if (!message) {
				return;
			}
			var alertClass = {
				error : options.errorClass,
				warning : options.warningClass,
				info : options.infoClass
			}[message.type] || "";
			var alertContent = message.content || "";
			if (!messageObj.hasClass(alertClass)) {
				messageObj.addClass(alertClass).text(alertContent);
			}
		});
	}

	var resetFieldMessages = function(options, form) {
		// 遍历所有的消息体
		(function() {
			var messageObjs = form
					.find('[node-type="field-message"][message-for]');
			if (!messageObjs.length) {
				return;
			}
			messageObjs.removeClass(
					options.errorClass + " " + options.warningClass + " "
							+ options.infoClass).text("");
		})();
	};

	var showFormMessage = function(options, form, formMessageObj, type, content) {
		if (!formMessageObj.length) {
			return;
		}
		var classMap = {
			loading : options.formLoadingClass,
			error : options.formErrorClass,
			warning : options.formWarningClass,
			info : options.formInfoClass
		};
		formMessageObj.removeClass(
				options.formLoadingClass + " " + options.formErrorClass + " "
						+ options.formWarningClass + " "
						+ options.formInfoClass).text(content || "");
		if (type && classMap[type]) {
			formMessageObj.addClass(classMap[type]);
		}
	};

	$.fn.ajaxform = function(options) {
		var name = "__plugin_ajaxform";
		var isMethodCall = typeof options === "string",
		args = Array.prototype.slice.call( arguments, 1 ),
		returnValue = this;

		// allow multiple hashes to be passed on init
		options = !isMethodCall && args.length ?
			$.extend.apply( null, [ true, options ].concat(args) ) :
			options;
	
		// prevent calls to internal methods
		if ( isMethodCall && options.charAt( 0 ) === "_" ) {
			return returnValue;
		}
	
		if ( isMethodCall ) {
			this.each(function() {
				var instance = $.data( this, name ),
					methodValue = instance && $.isFunction( instance[options] ) ?
						instance[ options ].apply( instance, args ) :
						instance;
				if ( methodValue !== instance && methodValue !== undefined ) {
					returnValue = methodValue;
					return false;
				}
			});
		} else {
			this.each(function() {
				var instance = $.data( this, name );
				if ( instance ) {
					instance.option( options || {} )._init();
				} else {
					$.data( this, name, new $.AjaxForm( options, this ) );
				}
			});
		}
	
		return returnValue;
	};

	var submitForm = function(ajaxFormInstance){
		if(!ajaxFormInstance.element){
			return true;
		}
		var form = ajaxFormInstance.element;
		var options = ajaxFormInstance.options;
		var formMessageObj = form.find("[node-type='form-message']");
		// 阻止提交
		var action = form.attr('action') || "";
		var method = form.attr('method') || "POST";
		var param = form.serialize();
		PU.ajax({
			url : action,
			type : method,
			data : param,
			beforeSend : function(xhr) {
				resetFieldMessages(options, form);
				// loading
				showFormMessage(options, form, formMessageObj,
						"loading", "正在发送请求...");
			},
			success : function(json) {
				if (json == null) {
					showFormMessage(options, form, formMessageObj,
							"error", "服务端未正常响应");
					return;
				}
				var code = json.code;

				showFormMessage(options, form, formMessageObj);
				if (code === 0) {// NORMAL
					// data
					if (options.success) {
						options.success.call(form, json.data);
					}
					// info
					if (json.infos && json.infos[""]
							&& json.infos[""][0]) {
						showFormMessage(options, form, formMessageObj,
								"info", json.infos[""][0]);
					}
					return;
				}
				if (code == 1) {// NEED_LOGIN
					PU.confirmDialog('您长时间未登录，需要重新登陆', function() {
						PP.gotoUrl('/login?to='+encodeURIComponent(location.href));
					});
					return;
				}
				if (code == 2) {// NO_AUTH
					PU.alertDialog('您没有权限使用此功能，具体信息请与管理员联系');
					return;
				}
				if (code == 3) {// ERROR
					// 处理错误
					var messages = fillMessages(options, form,
							json.errors, json.warnings, json.infos);
					showFieldMessages(options, form, messages);
					// from message
					var formMessageType, formMessageContent;
					var formMessage = messages[""];// global
					if (formMessage) {
						formMessageType = formMessage.type;
						formMessageContent = formMessage.content;
					} else if (json.errors) {
						formMessageType = 'error';
						formMessageContent = "表单中存在多个错误";
					} else if (json.warnings) {
						formMessageType = 'warning';
						formMessageContent = "表单中存在多个警告";
					}

					formMessageType = formMessageType || 'error';
					formMessageContent = formMessageContent
							|| "服务端未处理请求或者未成功响应";

					showFormMessage(options, form, formMessageObj,
							formMessageType, formMessageContent);
					return;
				}
				if (code == 4) {// EXPIRED_PAGE
					PU.confirmDialog('页面内容已经过期，需要重新刷新', function() {
						window.location.reload();
					});
					return;
				}
				if (code == 5) {// REDIRECT
					if (json.url) {
						PP.gotoUrl(json.url);
					} else {
						window.location.reload();
					}
					return;
				}
				if (code == 6) {// ALERT
					PU.alertDialog(json.alert || '服务端未正常响应');
					return;
				}
				showFormMessage(options, form, formMessageObj, "error",
						"服务端未正常响应");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				// 通常 textStatus 和 errorThrown 之中
				// 只有一个会包含信息
				showFormMessage(options, form, formMessageObj, "error",
						"请求发送失败!");
			}
		});
		return false;
	};

	$.AjaxForm = function(options, element) {
		// allow instantiation without initializing for simple inheritance
		if (arguments.length) {
			this._create(options, element);
		}
	};

	$.AjaxForm.prototype = {
		element : null,
		options : {
			errorClass : 'field-error',
			warningClass : 'field-warning',
			infoClass : 'field-info',
			formLoadingClass : 'form-loading',
			formErrorClass : 'form-error',
			formWarningClass : 'form-warning',
			formInfoClass : 'form-info',
			success : null
		},
		_create : function(options, element) {
			// $.widget.bridge stores the plugin instance, but we do it anyway
			// so that it's stored even before the _create function runs
			$.data(element, "__plugin_ajaxform", this);
			this.element = $(element);
			this.options = $.extend(true, {}, this.options, options);
			var self = this;
			this.element.submit(function() {
				submitForm(self);
				return false;
			});
			this._init();
		},
		_init : function() {},
		option : function(key, value) {
			var options = key;

			if (arguments.length === 0) {
				// don't return a reference to the internal hash
				return $.extend({}, this.options);
			}

			if (typeof key === "string") {
				if (value === undefined) {
					return this.options[key];
				}
				options = {};
				options[key] = value;
			}

			this._setOptions(options);

			return this;
		},
		_setOptions : function(options) {
			var self = this;
			$.each(options, function(key, value) {
				self._setOption(key, value);
			});

			return this;
		},
		_setOption : function(key, value) {
			this.options[key] = value;
			return this;
		}
	};

})(jQuery);
