/*
使用方法：
	1. 当为需要显示时间的标签添加Class:timeselect,如果需要时间，添加Class:picktime
	2. 时间范围
		a. 为父容器添加Class：timerangeselect
		b. 显示时间的标签添加Class:timeselect,starttime/endtime
 */

DP = {
	defaultValue : {
		defaultDate : 0,
		changeMonth : true,
		changeYear : true,
		showButtonPanel : true,
		showOn : 'button',
		buttonImage : '/static/images/calendar.gif',
		buttonImageOnly : true,
		showOtherMonths : true,
		selectOtherMonths : true,
		dateFormat : 'yy-mm-dd',
		minDate : '2005-1-1'
	},
	picker : function(obj, a, b, c) {
		obj = jQuery(obj);
		obj.each(function(i) {
			var o = jQuery(obj[i]);
			if (o.hasClass('picktime')) {
				if (a == 'option') {
					o.datetimepicker(b, c);
				} else {
					o.datetimepicker(a, b, c);
				}
			} else {
				o.datepicker(a, b, c);
			}
		});
	},
	init : function(obj) {
		DP.picker(obj, DP.defaultValue);
	},
	setOption : function(obj, option, value) {
		obj.datepicker('option', option, value);
	},
	setOptions : function(obj, options) {
		obj.datepicker('option', options);
	},
	setTime : function(obj, time) {
		jQuery(obj).datetimepicker('setDate', time);
	},
	getTime : function(obj) {
		return jQuery(obj).datetimepicker('getDate');
	}
};

jQuery(function() {
	jQuery.datepicker.setDefaults(jQuery.datepicker.regional['zh-CN']);
	jQuery.timepicker.regional['zh-CN'] = {
		timeOnlyTitle : '选择时间',
		timeText : '时间',
		hourText : '时',
		minuteText : '分',
		secondText : '秒',
		currentText : '现在',
		closeText : '选择',
		ampm : false
	};
	jQuery.timepicker.setDefaults(jQuery.timepicker.regional['zh-CN']);
	// 设置时间选择器
	var times = jQuery('.timeselect,.timerangeselect .starttime,.timerangeselect .endtime');
	DP.init(times);

	times.each(function(i) {
		var dateObj = jQuery(times[i]);
		if (dateObj.hasClass('no-max-date')) {
			DP.setOption(dateObj, 'maxDate', '2030-01-01');
		}else if(dateObj.hasClass('post-time')){
			DP.setOption(dateObj, 'maxDate', 0);
		}
		if (dateObj.val() || dateObj.hasClass('allow-empty-date'))
			return;
		var dateVal = new Date();
		if (dateObj.is(".timerangeselect .starttime.picktime")) {
			dateVal.setHours(0, 0, 0, 0);
		} else if (dateObj.is(".timerangeselect .endtime.picktime")) {
			dateVal.setHours(23, 59, 59, 999); // 精确到天
		}
		DP.setTime(dateObj, dateVal);
	});

	// 设置时间区间选择器
	jQuery('.timerangeselect').each(
			function() {
				var dates = jQuery(this).find('.starttime, .endtime');
				var quickdates = jQuery(this).find('.quicktime');
				DP
						.setOptions(dates,
								{
									onSelect : function(selectedDate) {
										quickdates.removeClass('qtime-cur');
										var option = jQuery(this).hasClass(
												"starttime") ? "minDate"
												: "maxDate";
										var date = DP.getTime(jQuery(this));
										// var instance =
										// jQuery(this).data("datepicker");
										// var date =
										// jQuery.datepicker.parseDate(instance.settings.dateFormat
										// ||
										// jQuery.datepicker._defaults.dateFormat,
										// selectedDate, instance.settings);
										DP.setOption(dates.not(this), option,
												date);
									}
								});

				var stimeObj = jQuery(this).find('.starttime');
				var etimeObj = jQuery(this).find('.endtime');

				quickdates.each(function() {
					jQuery(this)
							.click(
									function() {
										var qDateObj = jQuery(this);
										if (qDateObj.hasClass('qtime-cur')) {
											return;
										}
										DP.setOption(dates, 'minDate',
												DP.defaultValue.minDate);
										DP.setOption(dates, 'maxDate',
												DP.defaultValue.maxDate);

										quickdates.not(qDateObj).removeClass(
												'qtime-cur');
										qDateObj.addClass('qtime-cur');
										var etime = new Date();
										etime.setHours(23, 59, 59, 999); // 精确到天
										var stime = new Date(etime);
										stime.setHours(0, 0, 0, 0); // 精确到天
										if (qDateObj.is("[day]")) {
											var day = parseInt(qDateObj
													.attr('day'));
											if (day <= 0) {
												return;
											}
											stime.setDate(stime.getDate() - day
													+ 1);
										} else if (qDateObj.is("[week]")) {
											var week = parseInt(qDateObj
													.attr('week'));
											if (week <= 0) {
												return;
											}
											var days = (week - 1) * 7
													+ stime.getDay();
											stime.setDate(stime.getDate()
													- days + 1);
										} else if (qDateObj.is("[month]")) {
											var month = parseInt(qDateObj
													.attr('month'));
											if (month <= 0) {
												return;
											}
											stime.setDate(1);
											stime.setMonth(stime.getMonth()
													- month + 1);
										}
										DP.setTime(stimeObj, stime);
										DP.setTime(etimeObj, etime);
									});
				});
			});
});