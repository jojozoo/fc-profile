(function() {
  var ProfileBrowser, ProfilePage, ProfileUtil;
  ProfileUtil = (function() {
    function ProfileUtil() {}
    ProfileUtil.prototype.str2html = function(str) {
      if (!str) {
        return "";
      }
      return str.replace(/&/g, '&amp;').replace(/#/g, '&#35;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\(/g, '&#40;').replace(/\)/g, '&#41;').replace(/\"/g, '&#34;').replace(/\'/, '&#39;');
    };
    ProfileUtil.prototype.nl2br = function(str) {
      if (!str) {
        return "";
      }
      return str.replace(/\r?\n/g, '<br/>');
    };
    ProfileUtil.prototype.cloneObj = function(obj, onlyStrNumBool, excludePlainObj, excludeArray) {
      var key, res, val;
      res = {};
      for (key in obj) {
        val = obj[key];
        if (onlyStrNumBool && typeof val !== "string" && typeof val !== "number" && typeof val !== "boolean") {
          continue;
        }
        if (excludePlainObj && jQuery.isPlainObject(val)) {
          continue;
        }
        if (excludeArray && jQuery.isArray(val)) {
          continue;
        }
        res[key] = val;
      }
      return res;
    };
    ProfileUtil.prototype.filterMap = function(map, reservedKeys) {
      var key, result, _i, _len;
      if (!map || !reservedKeys || !jQuery.isArray(reservedKeys) || reservedKeys.length === 0) {
        return map;
      }
      result = {};
      for (_i = 0, _len = reservedKeys.length; _i < _len; _i++) {
        key = reservedKeys[_i];
        result[key] = map[key];
      }
      return result;
    };
    ProfileUtil.prototype.isNumber = function(str) {
      var num;
      num = Number(str);
      return !isNaN(num) && isFinite(num);
    };
    ProfileUtil.prototype.isInteger = function(str) {
      return /^\s*[0-9]+\s*$/.test(str);
    };
    ProfileUtil.prototype.toInteger = function(str, defVal) {
      if (defVal == null) {
        defVal = 0;
      }
      if (!str) {
        return defVal;
      }
      if (!this.isInteger(str)) {
        return defVal;
      }
      if (this.isNumber(str)) {
        return Number(str);
      }
      return defVal;
    };
    ProfileUtil.prototype.getValueFromArray = function(array, index, defVal) {
      if (!array || (index < 0 || index >= array.length)) {
        return defVal;
      }
      return array[index];
    };
    ProfileUtil.prototype.stopEvent = function(event) {
      event.preventDefault();
      return event.stopPropagation();
    };
    ProfileUtil.prototype.getElById = function(id) {
      return document.getElementById(id);
    };
    ProfileUtil.prototype._ajaxProxies = {};
    ProfileUtil.prototype.crossDomainXHR = function(url, callback) {
      var hostname, iframe, tmpA, xhr;
      tmpA = this.makeEl('a');
      tmpA.href = url;
      hostname = tmpA.hostname;
      if (hostname && hostname !== location.hostname) {
        xhr = null;
        if (this._ajaxProxies[hostname]) {
          return callback(this._ajaxProxies[hostname].getTransport());
        } else {
          iframe = this.makeEl('iframe');
          iframe.style.display = 'none';
          return jQuery(function() {
            document.body.insertBefore(iframe, document.body.firstChild);
            iframe.src = 'http://' + hostname + '/ajaxproxy.htm';
            if (iframe.attachEvent) {
              return iframe.attachEvent('onload', function() {
                callback(iframe.contentWindow.getTransport());
                return this._ajaxProxies[hostname] = iframe.contentWindow;
              });
            } else {
              return iframe.onload = function() {
                callback(iframe.contentWindow.getTransport());
                return this._ajaxProxies[hostname] = iframe.contentWindow;
              };
            }
          });
        }
      } else {
        if (window.ActiveXObject) {
          try {
            return callback(new ActiveXObject('Msxml2.XMLHTTP'));
          } catch (e) {
            return callback(new ActiveXObject('Microsoft.XMLHTTP'));
          }
        } else {
          return callback(new XMLHttpRequest());
        }
      }
    };
    ProfileUtil.prototype.ajax = function(url,options,_method) {
      if(jQuery.isPlainObject(url)){
    	  options = url;
    	  url = options.url;
      }
      
      if(!options.url){
    	  options.url = url || "";
      }
      
      if(_method){
    	  options.type = _method;
      }
    	
      if(!options.dataType){
	      var _url = options.url;
	      if (_url.indexOf('?') < 0) {
	        _url += "?";
	      }
	      if (_url[_url.length - 1] !== "?") {
	        _url += "&";
	      }
	      _url += "__ajax=json";
	      options.url = _url;
	      
	      options.dataType = 'json';  
      }
      return jQuery.ajax(options);
    };
    ProfileUtil.prototype.ajaxPost = function(url, options) {
      return this.ajax(url, options,"post");
    };
    ProfileUtil.prototype.ajaxGet = function(url, options) {
      return this.ajax(url, options,"get");
    };
    ProfileUtil.prototype.makeEl = function(tagName) {
      return document.createElement(tagName);
    };
    ProfileUtil.prototype.adjustIframeHeight = function(iframeObj) {
      if (iframeObj.contentDocument) {
        iframeObj.height = iframeObj.contentDocument.body.offsetHeight;
      } else if (iframeObj.Document) {
        iframeObj.height = iframeObj.Document.body.scrollHeight;
      }
      return iframeObj;
    };
    ProfileUtil.prototype.adjustIframeWidth = function(iframeObj) {
      if (iframeObj.contentDocument) {
        iframeObj.width = iframeObj.contentDocument.body.offsetWidth;
      } else if (iframeObj.Document) {
        iframeObj.width = iframeObj.Document.body.scrollWidth;
      }
      return iframeObj;
    };
    ProfileUtil.prototype.adjustIframeSize = function(iframeObj) {
      adjustIframeHeight(iframeObj);
      adjustIframeWidth(iframeObj);
      return iframeObj;
    };
    ProfileUtil.prototype.parseTime = function(string) {
      var dotPos, result;
      result = Date.parse(string);
      if (result !== null) {
        return result;
      }
      dotPos = string.lastIndexOf('.');
      if (dotPos < 0) {
        return result;
      }
      string = string.substring(0, dotPos);
      result = Date.parse(string);
      return result;
    };
    ProfileUtil.prototype.formatTime = function(input, defaultValue) {
      var time;
      if (defaultValue == null) {
        defaultValue = "-";
      }
      time = input;
      if (typeof input === "string") {
        time = this.parseTime(input);
      }
      if (!time || !time.format || !jQuery.isFunction(time.format)) {
        return defaultValue;
      }
      return time.format();
    };
    ProfileUtil.prototype._dialogTaskList = [];
    ProfileUtil.prototype._runDialogTask = function(dialogTask) {
      if (!jQuery('.ui-dialog:visible').size()) {
        return dialogTask();
      } else {
        return this._dialogTaskList.push(dialogTask);
      }
    };
    ProfileUtil.prototype.initDialog = function(jDialog) {
      var self;
      self = this;
      return jDialog.dialog({
        autoOpen: false,
        resizable: true,
        height: 'auto',
        width: 'auto',
        modal: true,
        close: function() {
          var task;
          task = self._dialogTaskList.shift();
          if (task) {
            return task();
          }
        }
      });
    };
    ProfileUtil.prototype.initConfirmDialog = function(jDialog) {
      this.initDialog(jDialog);
      return jDialog.dialog({
        buttons: {
          '提交': function() {
            var cb;
            cb = jQuery(this).dialog('option', 'cb');
            if (!cb || cb() !== false) {
              return jQuery(this).dialog('close');
            }
          },
          '取消': function() {
            return jQuery(this).dialog('close');
          }
        }
      });
    };
    ProfileUtil.prototype._showDialog = function(settings, divId) {
      var dialog, _showDialogTask;
      dialog = jQuery('#' + divId);
      if (!dialog.size()) {
        dialog = jQuery("<div id='" + divId + "' style='display:none'></div>");
        jQuery('body').append(dialog);
        this.initDialog(dialog);
      }
      _showDialogTask = function() {
        var buttons, _i, _len, _ref;
        buttons = {};
        if (settings.buttons) {
        	_ref = settings.buttons;
            _fn = function(_button, _buttons) {
              if (_button.name) {
                return _buttons[_button.name] = function() {
                  var callback, callback_result;
                  callback = _button.callback;
                  if (callback && jQuery.isFunction(callback)) {
                    callback_result = callback();
                  }
                  if (callback_result !== false) {
                    return jQuery(this).dialog('close');
                  }
                };
              }
            };
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              settings_button = _ref[_i];
              _fn(settings_button, buttons);
            }
        }
        dialog.dialog({
          buttons: buttons
        });
        dialog.dialog("option", "title", settings.title);
        dialog.html(settings.msg);
        return dialog.dialog('open');
      };
      this._runDialogTask(_showDialogTask);
      return dialog;
    };
    ProfileUtil.prototype.confirmDialog = function(msg, title, yesCallback) {
      var settings;

      if(jQuery.isFunction(title)){
    	  yesCallback = title;
    	  title = "";
      }
      
      settings = {
        msg: '',
        title: '警告？',
        buttons: [
          {
            name: '确定',
            callback: yesCallback || null
          }, {
            name: '取消',
            callback: null
          }
        ]
      };
      
      if (jQuery.isPlainObject(msg)) {
        jQuery.extend(settings, msg);
      } else {
        settings.msg = msg || "";
        settings.title = title || settings.title;
      }
      return this._showDialog(settings, 'profile-confirm-dialog');
    };
    ProfileUtil.prototype.alertDialog = function(msg, title, callback) {
      var settings;

      if(jQuery.isFunction(title)){
    	  callback = title;
    	  title = "";
      }
      
      settings = {
        msg: '',
        title: '提示',
        buttons: [
          {
            name: '确定',
            callback: callback || null
          }
        ]
      };
      
      if (jQuery.isPlainObject(msg)) {
        jQuery.extend(settings, msg);
      } else {
        settings.msg = msg || "";
        settings.title = title || settings.title;
      }
      return this._showDialog(settings, 'profile-alert-dialog');
    };
    ProfileUtil.prototype.errorDialog = function(msg, title, callback) {
      var settings;

      if(jQuery.isFunction(title)){
    	  callback = title;
    	  title = "";
      }
      settings = {
        msg: '',
        title: '错误',
        buttons: [
          {
            name: '确定',
            callback: callback || null
          }
        ]
      };

      if (jQuery.isPlainObject(msg)) {
        jQuery.extend(settings, msg);
      } else {
        settings.msg = msg || "";
        settings.title = title || settings.title;
      }
      return this._showDialog(settings, 'profile-error-dialog');
    };
    return ProfileUtil;
  })();
  window.PU = new ProfileUtil();
  ProfilePage = (function() {
    function ProfilePage() {}
    ProfilePage.params = null;
    ProfilePage.prototype.setParam = function(a, b) {
      if (!(this.params != null)) {
        this.params = typeof params !== "undefined" && params !== null ? params : {};
      }
      if (jQuery.isPlainObject(a)) {
        jQuery.extend(this.params, a);
      } else {
        if ((a != null) && (b != null)) {
          this.params[a] = b;
        }
      }
      return this.params;
    };
    ProfilePage.prototype.paramStr = function(__params) {
      var key, p, type, val, _params;
      _params = __params || this.params;
      if (!_params) {
        return "";
      }
      p = {};
      for (key in _params) {
        val = _params[key];
        if (val === null) {
          continue;
        }
        type = typeof val;
        switch (type) {
          case "undefined":
          case "object":
            continue;
          case "number":
            val = "" + val;
            break;
          case "string":
            val = jQuery.trim(val);
            if (!val) {
              continue;
            }
        }
        p[key] = val;
      }
      if (jQuery.isEmptyObject(p)) {
        return "";
      } else {
        return jQuery.param(p);
      }
    };
    ProfilePage.prototype.gotoUrl = function(url) {
      window.location.href = url;
      return false;
    };
    ProfilePage.prototype.gotoPageNumber = function(pageNumber, _pageKey, _baseUrl) {
      var url;
      if (pageNumber == null) {
        pageNumber = 0;
      }
      if (!(_pageKey != null)) {
        _pageKey = typeof pageKey !== "undefined" && pageKey !== null ? pageKey : "curpage";
      }
      if (!(_baseUrl != null)) {
        _baseUrl = typeof baseUrl !== "undefined" && baseUrl !== null ? baseUrl : "";
      }
      url = "" + _baseUrl + "?" + _pageKey + "=" + pageNumber + "&" + (this.paramStr());
      this.gotoUrl(url);
      return false;
    };
    ProfilePage.prototype.submitParams = function(_baseUrl) {
      var url;
      if (!(_baseUrl != null)) {
        _baseUrl = typeof baseUrl !== "undefined" && baseUrl !== null ? baseUrl : "";
      }
      url = "" + _baseUrl + "?" + (this.paramStr());
      this.gotoUrl(url);
      return false;
    };
    return ProfilePage;
  })();
  window.PP = new ProfilePage();
  ProfileBrowser = (function() {
    var _chrome, _chromeMatch, _clientPC, _ff2, _ff2Bugs, _konqueror, _mac, _opera, _opera7, _opera95, _operaPre7, _safari, _webkitMatch, _window, _x11;
    function ProfileBrowser() {}
    /* Client Info */
    ProfileBrowser.prototype.clientPC = _clientPC = navigator.userAgent.toLowerCase();
    /* System */
    ProfileBrowser.prototype.window = _window = _clientPC.indexOf('windows') !== -1;
    ProfileBrowser.prototype.x11 = _x11 = _clientPC.indexOf('x11') !== -1;
    ProfileBrowser.prototype.mac = _mac = _clientPC.indexOf('mac') !== -1;
    ProfileBrowser.prototype.konqueror = _konqueror = _clientPC.indexOf('konqueror') !== -1;
    /* Gecko */
    ProfileBrowser.prototype.gecko = /gecko/.test(_clientPC) && !/khtml|spoofer|netscape\/7\.0/.test(_clientPC);
    /* webkit */
    if (_webkitMatch = _clientPC.match(/applewebkit\/(\d+)/)) {
      if (_chromeMatch = _clientPC.match(/chrome\/(\d+)/)) {
        ProfileBrowser.prototype.chrome = _chrome = true;
        ProfileBrowser.prototype.chromeVersion = parseInt(_chromeMatch[1]);
      } else {
        ProfileBrowser.prototype.safari = _safari = _clientPC.indexOf('spoofer') === -1;
        ProfileBrowser.prototype.safariWin = _safari && _window;
      }
      ProfileBrowser.prototype.webkitVersion = parseInt(_webkitMatch[1]);
    }
    /* FF */
    ProfileBrowser.prototype.ff2 = _ff2 = /firefox\/[2-9]|minefield\/3/.test(_clientPC);
    ProfileBrowser.prototype.ff2Bugs = _ff2Bugs = /firefox\/2/.test(_clientPC);
    ProfileBrowser.prototype.ff2Win = _ff2 && _window;
    ProfileBrowser.prototype.ff2X11 = _ff2 && _x11;
    /* Opera */
    ProfileBrowser.prototype.opera = _opera = _clientPC.indexOf('opera') !== -1;
    if (_opera) {
      ProfileBrowser.prototype.operaPre7 = _operaPre7 = window.opera && !document.childNodes;
      ProfileBrowser.prototype.opera7 = _opera7 = window.opera && document.childNodes;
      ProfileBrowser.prototype.opera95 = _opera95 = /opera\/(9\.[5-9]|[1-9][0-9])/.test(_clientPC);
      ProfileBrowser.prototype.opera6Bugs = _operaPre7;
      ProfileBrowser.prototype.opera7Bugs = _opera7 && !_opera95;
      ProfileBrowser.prototype.opera95Bugs = /opera\/(9\.5)/.test(_clientPC);
    }
    /* IE */
    ProfileBrowser.prototype.ie6Bugs = /msie ([0-9]{1,}[\.0-9]{0,})/.exec(_clientPC) !== null && parseFloat(RegExp.$1) <= 6.0;
    return ProfileBrowser;
  })();
  window.PB = new ProfileBrowser();
}).call(this);
