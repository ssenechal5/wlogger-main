/**
 * This class implements the translation and languages localization features and functions used by the Actility's UI applications.
 * We use this class also to add text to error messages as we receive only error codes for them. We simply use the error code as translation id and look in the dictionary.
 * Also this translation mechanism is used to set more correct names to currencies, values, country names, timezones, etc
 * @namespace com.actility
 * @class locale
 * @static
 */
Ext.define('com.actility.locale',{

	statics : {
		/**
		 * Look for translation of the specified ID in the dictionary and return the translation
		 * @method translate
		 * @param {String} id A word or sentence for translations
		 * @return {String} translation
		 */
		translate: function(id) {

			var reg = /^\$\{(.+)\}$/gm;
			var nId = reg.exec(id);

			if(nId && nId[1])
				id = nId[1];

			var translation = com.actility.locale.translator[id];
			if (translation !== undefined)
				return translation;
			return id;
		},
		translateLogin: function(id) {
			if (com.actility.global.adminLogin)
				return com.actility.locale.translate('LBL LOGIN ADMIN');
			else
				return com.actility.locale.translate('LBL LOGIN');
		},
		translateErrorMessage: function(id,defaultMessage) {

			var translation = "";
			for (var i = 0; i < com.actility.locale.errorMessages.length; i++) {
			    var obj = com.actility.locale.errorMessages[i];
			    if (obj.code == id) {
			    	translation = obj.msg;
			    }
			}				
			if (translation.length > 0)
				return translation;
			return defaultMessage;
		},

		/**
		 * This is a simple object defining a path to functions who translates specific extJs object properties
		 * @config {Object} toTranslate
		 */
		toTranslate: {
			'title': function(obj,msg) { if (obj.setTitle) obj.setTitle(msg); },
			'html': function(obj,msg) { },
			'value': function(obj,msg) { if (obj.setValue) obj.setValue(msg); },
			'text': function(obj,msg) { if (obj.setText) obj.setText(msg); },
			'fieldLabel': function(obj,msg) { if (obj.labelEl) obj.labelEl.update(msg+':'); },
			'tooltip': function(obj,msg) { if (obj.setTooltip) obj.setTooltip(msg); },
			'blankText': function(obj,msg) { },
			'emptyText': function(obj,msg) { },
			'boxLabel': function(obj,msg) { if (obj.boxLabelEl) obj.boxLabel.update(msg); }
		},

		/**
		 * This function will translate the properties of an ExtJS object according to the rules of toTranslate
		 * @method applytranslate
		 * @param {Extjs object} obj extjs object
		 */
		applytranslate: function (obj) {
			for (var k in com.actility.locale.toTranslate)
				if(obj[k]) {
					if (typeof obj.beforeTranslate == 'undefined') {
						obj.beforeTranslate=Object();
						obj['beforeTranslate'][k] = obj[k]; // Store the original values
					}
					obj[k] = com.actility.locale.localize(obj[k]);
				}
		},

		/**
		 * This function retranslates automatically the stores (reloads them)
		 * @method storeretranslate
		 */
		storeretranslate: function() {
			Ext.each(Ext.StoreManager.items, function(n) {
				try {
					if (n.load && n.retranslate) n.load();
				} catch(e) {
					console.trace();
					console.warn(e); 
				}
			}); 
			/*            Ext.each(Ext.ComponentQuery.query('[store]'),function (n) {
                if (n.getStore) {
                    try {
                        var s = n.getStore();
                        if (s.load && s.retransate) s.load();
                    } catch(e) {
                        console.warn(e);
                    }
                }
            }); */
		},

		/**
		 * This funcrion is similar to translate, but re-translate already translated object in case we need to change the language
		 * @method applyretranslate
		 * @param {Object} obj
		 * @return {Boolean}
		 */
		applyretranslate: function (obj) {
			if (typeof obj == 'undefined') {
//				console.warn('applyretranslate called with undefined');
				return false;
			}
			if ((!obj.xtype)||(typeof obj.beforeTranslate == 'undefined'))
				return false;
			// return com.actility.locale.applytranslate(obj);

			// Translate tree node
			if (obj.childNodes && obj.data && obj.commit) {  // Assume it is a node
				obj.set('text',com.actility.locale.localize(obj.beforeTranslate['text'])); // Only text for now
				obj.commit();
				return;
			}

			// Translate common components
			for (var k in com.actility.locale.toTranslate)
				if(obj.beforeTranslate[k]) {
					obj[k] =  com.actility.locale.localize(obj.beforeTranslate[k]);
					com.actility.locale.toTranslate[k].apply(this,[obj,obj[k]]);
				}
		},

		/**
		 * This function retranslate (or translate) all the objects that belong to a specified root object
		 * @method retranslate
		 * @param {Object} obj
		 * @return {Boolean}
		 */
		retranslate: function(obj) {
			// This does not retranslate the treestores and the toolbar yet! This should be reviewed and fixed

			if (typeof obj == 'undefined') {
//				console.warn('retranslate called with undefined');
				return false;
			}

			if (typeof obj.beforeTranslate != 'undefined') this.applyretranslate(obj);

			if (obj.items) {
				// Go trough the childs with recursion
				if (obj.items instanceof Array)
					for (var i in obj.items) this.retranslate(obj.items[i]);
				else this.retranslate(obj.items);
			}

			if (obj.dockedItems) {
				// Lets verify the dockedItems
				if (obj.dockedItems instanceof Array)
					for (var i in obj.dockedItems) this.retranslate(obj.dockedItems[i]);
				else this.retranslate(obj.dockedItems);
			}

			if (obj.childNodes) {
				// Lets verify the dockedItems
				if (obj.childNodes instanceof Array)
					for (var i in obj.childNodes) this.retranslate(obj.childNodes[i]);
				else this.retranslate(obj.childNodes);
			}

			if (obj.xtype && obj.xtype == 'treepanel') {
				// Here we are translating tree panel
				if (obj.store && obj.store.tree)
					this.retranslate(obj.store.tree.getRootNode());  // Now we have access to all the nodes
			}
		},

		/**
		 * This function is used by other functions, translating a text if it is in the correct form suggesting translation is needed aka ${...}.
		 * A single string can contain multiple ${} statements and only the text within ${} is translated
		 * @method localize
		 * @param {Object} obj
		 * @return {String} translation
		 */
		localize: function(obj) {
			if (!Ext.isString(obj))
				return obj;

			if (obj.indexOf("${") < 0)
				return obj;

			var r = [];
			while (r = obj.match(/\$\{(.+?)\}/))
				obj = obj.replace(r[0], this.translate(r[1]));
			return obj;
		},

		/**
		 * In this array the translation will be stored
		 * @config {Array} translator
		 */
		translator : [],
		errorMessages : [],

		loaderrors : function(config,scope) {

			if(config.urlPath) { // e.g /some/url/containing/{{lang}}/template/to/replace

	            //var callBack = config.callback;
				var success = config.success || function(response) {
		            var _json = Ext.decode(response.responseText);
		            com.actility.locale.errorMessages = _json; // Seems to work better
		            //callBack.call(scope);
				};
				var failure = config.failure || function() {       
	                console.error('Unable to load default language for errors: '+ config.lang);
		            com.actility.locale.loaderrors({
		                           urlPath: com.actility.global.buildUrl("/rest/resources/files/errors"),
		                           params: config.params
		                          },{});
				};

                var reg = new RegExp("(\{\{lang\}\})", "g");
                var url = config.urlPath.replace(reg, config.lang);

				// WS not loaded yet
	            var ajaxConfig = {
					method : 'GET',
		            url: url,
                    failure: Ext.emptyFn,//failure,
                    params: config.params,
	                continueOnFailure: true,
	                callback : function(_opts, _success, _response) {
	                    if(_success)
	                        success(_response);
	                    else
	                        failure(_response);
	                }
	            };

	            Ext.Ajax.request(ajaxConfig);
	        }
		},

		/**
		 * With this function/method we are loading an external JSON dictionary with translations
		 * @method load
		 * @param {Object} config Config object, for example config.url points to the file with translation while config.callback to the function that will be called after then
		 * @param {Scope} scope 
		 */
		load : function(config,scope) {

			var defaultLanguage = 'en-US';

			if(config.urlPath) { // e.g /some/url/containing/{{lang}}/template/to/replace

	            //var lang = config.lang || Ext.Object.fromQueryString(location.search.substring(1)).lang || window.navigator.languages[0] || window.navigator.languages[2] || defaultLanguage;
	            var lang = config.lang || Ext.Object.fromQueryString(location.search.substring(1)).lang || 
	                                navigator.language || window.navigator.languages[0] || window.navigator.languages[2] || defaultLanguage;
	            lang = lang.replace("_", "-"); // use ISO instead

	            config.lang = lang; // update config

	            var callBack = config.callback;
				var success = config.success || function(response) {
		            var _json = Ext.decode(response.responseText);
		            com.actility.locale.translator = _json; // Seems to work better
		            callBack.call(scope, lang);
				};
				var failure = config.failure || function() {       
	                if(lang !== defaultLanguage) { // ensure to not use the default language to avoid infinite recursive function (e.g in case of network issue)
	                    config.lang = defaultLanguage; // update only language config
	                    com.actility.locale.load(config, scope);
	                } else {
	                  console.error('Unable to load default language: '+ defaultLanguage);
	                }
				};

                var reg = new RegExp("(\{\{lang\}\})", "g");
                var url = config.urlPath.replace(reg, lang);

				// WS not loaded yet
	            var ajaxConfig = {
					method : 'GET',
		            url: url,
                    failure: Ext.emptyFn,//failure,
                    params: config.params,
	                continueOnFailure: true,
	                callback : function(_opts, _success, _response) {
	                    if(_success)
	                        success(_response);
	                    else
	                        failure(_response);
	                }
	            };

	            Ext.Ajax.request(ajaxConfig);
			}
		}
	}
});

/**
 * @namespace Ext.data
 * @class TreeStore
 * @constructor
 */
Ext.override(Ext.data.TreeStore, {
	/**
	 * @method translateNode
	 * @param {Node} node
	 */
	translateNode : function(node) {
		if(node.raw && node.raw.text) {
			if (typeof node.beforeTranslate == 'undefined') {
				node.beforeTranslate = Object();
				node.beforeTranslate.text = node.get('text');
			}
			node.set('text',com.actility.locale.localize(node.raw.text));
			node.commit();
		}
		if(node.raw && node.raw.isLeaf) return;  // it is leaf, leave
		if(node.childNodes)
			for(var i=0;i<node.childNodes.length;i++) {
				this.translateNode(node.childNodes[i]);
			}
	},
	constructor : function() {
		if(arguments[0].root) {
			this.translateNode(arguments[0].root);
		}
		this.callOverridden(arguments);
		this.on('load',this.translateTree);
	},

	/**
	 * @method translateTree
	 */
	translateTree : function() {
		this.translateNode(this.tree.root);
	}
});

/**
 * @namespace Ext
 * @class AbstractComponent
 * @constructor
 */
Ext.override(Ext.AbstractComponent, {
	constructor : function() {
		for(var i in arguments) com.actility.locale.applytranslate(arguments[i]);
		this.callOverridden(arguments);
	}
});

//# sourceURL=com.actility.locale.js

