Ext.define('com.actility.specific.extjs', {
   statics: {

      //*************************************
      // TOOLS
      //*************************************
      specificClearQueryParameters: function() {
         var isIE = false;
         if (navigator.appName.indexOf("Internet Explorer") != -1) { //yeah, he's using IE
            isIE = true;
            /*  var badBrowser=(
                navigator.appVersion.indexOf("MSIE 9")==-1 &&   //v9 is ok
                navigator.appVersion.indexOf("MSIE 1")==-1  //v10, 11, 12, etc. is fine too
             );
           */
         }
         if (isIE === false) {
            //we now clear the url parameters
            var uri = window.location.toString();
            if (uri.indexOf("?") > 0 && uri.indexOf("state=") < 0) {
               var clean_uri = uri.substring(0, uri.indexOf("?"));
               window.history.replaceState({}, document.title, clean_uri);
            }
         }
      },
      specificGetVendorId: function() {
         // eg : http://lablora.thingpark.actility.com/?vendor=actility-open1
         var vendor = this.vendorId;
         return vendor;
      },
      specificGetAdminAccessTokenQueryParameters: function() {
         // eg : http://lablora.thingpark.actility.com/?vendor=actility-open1
         var token = Ext.Object.fromQueryString(location.search.substring(1)).adminAccessToken;
         return token;
      },
      //*************************************
      // UTILS
      //*************************************
      //UNUSED new notification
      specificNotification: function (message, messageType, close) {
        var tpl = new Ext.XTemplate('<img src="{imgSrc}" class="simpleIcon {iconCls}"/> {message}');
        var html = tpl.apply({
           imgSrc: Ext.BLANK_IMAGE_URL,
           iconCls: messageType ? messageType : '',
           message: message //com.actility.specific.specificTranslate(message)
        });

        var s = Ext.create('widget.uxNotification', {
          closable: false,
          corner: 'bl',
          stickOnClick: false,
          autoCloseDelay: 8000,
          manager: 'footer',
          slideInDuration: 400,
          slideBackDuration: 1500,
          cls: "ux-notification-light",
          html: html,

          listeners:{
            close:function(close) {
              console.log("Close Notif !" + close);
              if (close) {
                if (!Ext.isEmpty(WirelessLogger) && !Ext.isEmpty(WirelessLogger.util) && !Ext.isEmpty(WirelessLogger.util.Keycloak) && WirelessLogger.util.Keycloak.isReady()) {
                  WirelessLogger.util.Keycloak.logout();
                }
              }
            }
          }
        });
        s.show();
      },

      specificInfo: function (where, message) {
        this.specificWarningStatusBar(where, message);
      },
      specificWarning: function (where, message) {
        this.specificWarningStatusBar(where, message);
      },
      specificError: function (where, message) {
        this.specificWarningStatusBar(where, message);
      },
      specificWarningStatusBar: function(where, message) {
		     var me=this;
         if (typeof where == 'undefined') return;
         me.specificWarningMessage = message;
         me.specificWarningMessageWhere = where;
         if (typeof(com.actility.specific.extjs.specificWarningSlide) == 'undefined') {
            com.actility.specific.extjs.specificWarningSlide = Ext.Function.createBuffered(function() {
               if ((typeof com.actility.specific.extjs.specificWarningMessageWhere.down('image[itemId=statusImage]') !== 'undefined')
               && (me.specificWarningMessageWhere.down('image[itemId=statusImage]') !== null))
                  me.specificWarningMessageWhere.down('image[itemId=statusImage]').setSrc('resources/images/icon-warning.png');
               if ((typeof com.actility.specific.extjs.specificWarningMessageWhere.down('label[itemId=statusMessage]') !== 'undefined')
               && (me.specificWarningMessageWhere.down('label[itemId=statusMessage]') !== null)) {
                  me.specificWarningMessageWhere.down('label[itemId=statusMessage]').setText('  ' + com.actility.specific.extjs.specificWarningMessage, false);
                  me.specificWarningMessageWhere.down('label[itemId=statusMessage]').setWidth(500);
               }
               if ((typeof com.actility.specific.extjs.specificWarningMessageWhere.down('container[itemId=status]') !== 'undefined')
               && (me.specificWarningMessageWhere.down('container[itemId=status]') !== null)) {
                  me.specificWarningMessageWhere.down('container[itemId=status]').setWidth(600);
                  me.specificWarningMessageWhere.down('container[itemId=status]').el.slideIn();
               }
               me.specificWarningSlideOut();
            }, 500);
         }
         if (typeof(com.actility.specific.extjs.specificWarningSlideOut) == 'undefined') {
            com.actility.specific.extjs.specificWarningSlideOut = Ext.Function.createBuffered(function() {
               if ((typeof com.actility.specific.extjs.specificWarningMessageWhere.down('image[itemId=statusImage]') !== 'undefined')
               && (com.actility.specific.extjs.specificWarningMessageWhere.down('image[itemId=statusImage]') !== null))
                  com.actility.specific.extjs.specificWarningMessageWhere.down('image[itemId=statusImage]').setSrc('resources/images/icon-none.png');
               if ((typeof com.actility.specific.extjs.specificWarningMessageWhere.down('label[itemId=statusMessage]') !== 'undefined')
               && (com.actility.specific.extjs.specificWarningMessageWhere.down('label[itemId=statusMessage]') !== null)) 
                  com.actility.specific.extjs.specificWarningMessageWhere.down('label[itemId=statusMessage]').setText('');

            }, 3000);
         }
         //we do not want to raise multiple messages, we buffer for 500ms then call the function
         com.actility.specific.extjs.specificWarningSlide();
      },
      //USED
      toolManageExpand: function(expand) {
         //expand : true -> expand all
         //       : false-> collapse all

         Ext.getCmp('rootViewport').suspendLayout = true;
         var grid = Ext.getCmp('rootViewport').down('gridpanel[itemId=dashboardResultGridPannel]');
         var expander = grid.getPlugin();
         var expandedList = expander.recordsExpanded;
         var store = grid.getStore();

         for (var i = 0; i < store.getCount(); i++) {
            var recordInternalId = store.getAt(i).internalId;
            if (expand === false) //collapse is asked
            {
               if (typeof(expandedList[recordInternalId]) == 'undefined' || expandedList[recordInternalId] === true)
                  expander.toggleRow(i, store.getAt(i));
            } else //expand is asked
            {
               if (typeof(expandedList[recordInternalId]) == 'undefined' || expandedList[recordInternalId] === false)
                  expander.toggleRow(i, store.getAt(i));
            }
         }
      },
      toolStopDashboardRefresh: function() {
         //1) arreter
         clearInterval(com.actility.global.dashboardTimer);
         com.actility.global.dashboardTimer = null;
         //2) prevenir
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate('WR Auto Reload deactivated'));
      },
      //USED
      toolStartDashboardRefresh: function(delay) {
         //1) arreter un timer deja en cours
         if (com.actility.global.dashboardTimer !== null)
            clearInterval(com.actility.global.dashboardTimer);

         // Control Timer
         console.log('Timer:'+ delay);
         if (delay == "" || delay == "no" || delay == "n" || delay == "null" ||  delay == null) {
            this.toolStopDashboardRefresh();
            return;
         }
         //2) demarrer un nouveau timer
         com.actility.global.dashboardTimer = setInterval("com.actility.specific.extjs.toolRefreshDashboard(1)", delay);

         //3) prevenir
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate('Auto Reload activated'));
      },
      //USED
      refreshCustomerList: function() {
         // Display the customerId list in the header
         var customerIds = "";

         for (var i = 0; i < com.actility.global.customerIdList.length; i++) {
            if (i > 0)
               customerIds += ", "
            customerIds += com.actility.global.customerIdList[i].customerId;
         }

         // NFR965
         if (customerIds == "*")
           com.actility.global.notNetworkPartnerAccess = false;

      },
      //USED
      toolRefreshCustomerList: function() {
         // Display the customerId list in the header
         var customerIds = "";

         for (var i = 0; i < com.actility.global.customerIdList.length; i++) {
            if (i > 0)
               customerIds += ", "
            customerIds += com.actility.global.customerIdList[i].customerId;
         }

         // NFR965
         if (customerIds == "*")
           com.actility.global.notNetworkPartnerAccess = false;


         Ext.getCmp('dashboardHeader').setTitle(com.actility.locale.translate("LBL Dashboard") + " [ " + customerIds + " ]");
      },
      //USED
      toolUTCtoLocal: function(value) {
         //0004201: [N3_QUEUE][9237] wlogger timestamps daylight-saving time
         //0004326: [N3_QUEUE][9779] Wireless logger does not show localtime

         //value:2016-10-07 08:21:44.232
         //1) add T separator and Z
         var Spos=value.indexOf(" ");
         if (Spos != -1) value=value.substr(0,Spos)+"T"+value.substr(Spos+1)+"Z";
         var dateUTC = new Date(value);
         //2) get timezone offset of the initial date
         var timezone = dateUTC.getTimezoneOffset();
         //3) calculate the new timestamp
         var ts=dateUTC.getTime()- timezone *60 * 1000;
         //4) recreate a localized date
         var date = new Date(ts);
         var localizedDate=date.toISOString();
         //suppress T and Z, we are not UTC but locale without timezone
         var Zpos=localizedDate.indexOf("Z");
         if (Zpos != -1) localizedDate=localizedDate.substr(0,Zpos);
         var Tpos=localizedDate.indexOf("T");
         if (Tpos != -1) localizedDate=localizedDate.substr(0,Tpos)+" "+localizedDate.substr(Tpos+1);
         return localizedDate;
	  },
      callbackStoreDataChanged: function(store, records, options) {
          //store.events["datachanged"].clearListeners();
          com.actility.specific.extjs.specificInfo(Ext.getCmp('footer'),
                                        com.actility.locale.translate('WR Data loaded')+"("+store.getCount()+")");
          if (Ext.getCmp('header') != undefined)
             Ext.getCmp('header').down('displayfield[itemId=timestamp]').setValue(Ext.Date.format(new Date(), 'Y-m-d H:i:s'));

          Ext.getCmp('rootViewport').suspendLayout = false;
          Ext.getCmp('rootViewport').doLayout();
          Ext.getCmp('dashboard').down('grid').setLoading(false);

      },
      cleanInputs: function(input) {
        if (input !== null)
          return input.replace(/[\s.\-:]/g, "");
        else 
          return input;
      },
      toolRefreshDashboard: function(ipage) {
         //console.log("toolRefreshDashboard ipage="+ipage);
		     var me=this;
         var userId = com.actility.global.userId;
         //var last = Ext.getCmp('dashboard').down('combobox[itemId=last]').getValue();
         var last = "";
         var decoder = Ext.getCmp('dashboard').down('combobox[itemId=decoder]').getValue();
         var subtype = Ext.getCmp('dashboard').down('combobox[itemId=subtype]').getValue();
         
         // control subtype
         if (subtype == null || subtype == "null") {
            subtype = "";
         }
         
         var filteringLRC = Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').getValue();
         var filteringASID = Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').getValue();
         var fromDate = Ext.getCmp('dashboard').down('datefield[itemId=fromDate]').getValue();
         var toDate = Ext.getCmp('dashboard').down('datefield[itemId=toDate]').getValue();
         if (fromDate !== null) {
            fromDate = fromDate.toISOString();
         } else fromDate = "";
         if (toDate !== null) {
            toDate = toDate.toISOString();
         } else toDate = "";
         if (com.actility.global.viewportType == "LORA")
         {
            var filteringDevAddr = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').getValue());
            var filteringDevEUI  = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').getValue());
            var filteringLRR     = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').getValue());
            var filteringSubscriberId = Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').getValue();
         }
         else
         {
            var filteringDevAddr =  Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').getValue();
            var filteringDevEUI  =  Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').getValue();
            var filteringLRR     =  Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').getValue();
            var filteringSubscriberId = '';
         }

         Ext.getCmp('dashboard').down('fieldset[itemId=resultList]').setTitle(" "+com.actility.locale.translate("LBL last packets"));

         var t1 = Ext.getStore('dataStore');

         //CLEAN THE STORE - no notification
         //if (t1.hasListener("datachanged")) t1.events["datachanged"].clearListeners();
         t1.loadData([], false);

         // 1) get protocolType
         var type=0;
         if (com.actility.global.viewportType == "LTE") type=1;


         // Warning message if decoder AUTOMATIC and more than 500 packets
         if (decoder == com.actility.global.AUTO_DECODER && com.actility.global.pageSize > com.actility.global.maxAutomaticDecodedPackets) {
           this.showDecodingWarningMessage("LBL Automatic decoding cannot be performed when more than 500 packets are displayed");
         }


         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + userId + "/data?type=" + type + "&last=" + last 
                     + "&decoder=" + decoder + "&devicesAddr=" + filteringDevAddr + "&devicesID=" + filteringDevEUI + "&LRRID=" + filteringLRR 
                     + "&LRCID=" + filteringLRC + "&fromDate=" + fromDate + "&toDate=" + toDate + "&pageIndex=" + ipage 
                     + "&f=utc&sessionToken=" + com.actility.global.sessionToken + "&subscriberID=" + filteringSubscriberId 
                     + "&ASID=" + filteringASID + "&subtype=" + subtype);

         // 3) Prepare the action on store loaded : calculate max and redraw the graph           
         var grid = Ext.getCmp('rootViewport').down('gridpanel[itemId=dashboardResultGridPannel]');
         var gridView = grid.getView();
         gridView.on('refresh', function(records, index, options) {
            //need to take care of Expand All status
            var expand = Ext.getCmp('dashboard').down('checkboxfield[itemId=expand]').getValue();
            if (expand === true)
               me.toolManageExpand(expand);

         });

         Ext.getCmp('dashboard').down('grid').setLoading(true);
         // 4) Start the loading                 
         com.actility.ajax.ajaxCall({
            method: "GET",
            url: url,
            callbackSuccess: function(json) {
               if ((json !== null) && (json.data != undefined)) {
                  for (var i = 0; i < json.data.length; i++) {
                     json.data[i]['timestampLocal'] = me.toolUTCtoLocal(json.data[i]['timestampUTC']);
                  }
                  //t1.addListener("datachanged", callbackStoreDataChanged);
                  t1.currentPage = ipage;
                  t1.more = json.more;
                  t1.loadData(json.data);
               } else
                  Ext.getCmp('dashboard').down('grid').setLoading(false);

            },
            callbackError: function(errorMessage, response) {
               t1.more = false;
               t1.currentPage = 1;
               t1.loadData([], false);
               me.specificError(Ext.getCmp('footer'), errorMessage);
               Ext.getCmp('dashboard').down('grid').setLoading(false);
            }
         });
      },
      //USED
      toolManageDisconnection: function() {
		     var me=this;			  
         //1) ne pas recreer si deja present a l'écran
         if (typeof(Ext.getCmp('message')) == 'undefined') {
            var overlay = Ext.create('WirelessLogger.view.messagePanel', {
               id: 'message',
               modal: true,
               floating: true,
               centered: true,
               listeners: {
                  close: function(){
                      //2) stop dashboard refreshing timer
                      me.toolStopDashboardRefresh();

                      //3) destroy the viewport
                      if (Ext.getCmp("rootViewport") != undefined) Ext.getCmp("rootViewport").destroy();
                     
                      //4) destroy all opened windows
                      Ext.WindowMgr.each(
                         function(win) {
                            var title = win.title;
                            if (title != undefined) {
                              if ((title.substr(0, 3) == "Map") || (title.substr(0, 4) == "Path")) win.destroy();
                            }
                         }
                      );
                      //5) suppress the window manager unload callback
                      com.actility.specific.utils.stopTrackingUnload();
                     
                      if (com.actility.global.guiMode == "standalone" || com.actility.global.guiMode == "portal")
                      {
                         //6) recreate login panel
                         com.actility.login.displaySSOLoginPopup(com.actility.global.thingparkApplicationID,location.hostname);
                      }
                  }
                }
            });
            overlay.down('label[itemId=labelMessage]').setText(com.actility.locale.translate("MSG Sorry, connexion refused or timeout (invalid credential)"));
            overlay.show('pop', true);
         }
      },
      //*************************************
      // BOOT
      //*************************************
      //USED
      manageBootAfterRender: function() {
         //do nothing         
      },
      //*************************************
      // LOGIN SCREEN
      //*************************************
      infoTextClick: function() {
         Ext.getCmp("login").setHeight(410);
         Ext.getCmp("loginSubForm").setHeight(230);
         Ext.getCmp("login").down("fieldset[itemId=loginForm]").setVisible(true);
      },
      //*************************************
      // MANAGEMENT
      //*************************************
      //USED
      updateAutologinURL: function() {
         var autologinURL = location.href;
         autologinURL += "?login=" + Ext.getCmp('management').down('fieldset[itemId=edit]').down('textfield[itemId=email]').getValue();
         autologinURL += "&pwd=" + Ext.getCmp('management').down('fieldset[itemId=edit]').down('textfield[itemId=passwordMd5]').getValue();

         Ext.getCmp('management').down('fieldset[itemId=edit]').down('displayfield[itemId=autologinURL]').setValue(autologinURL);
      },
      managementManageEmailChange: function(field, oldValue, newValue) {
         this.updateAutologinURL();
      },
      managementManagePasswordChange: function(field, oldValue, newValue) {
         this.updateAutologinURL();
      },
      managementManageEditClose: function() {
         Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);
      },
      managementManageCreateClose: function() {
         Ext.getCmp('management').down('fieldset[itemId=create]').setVisible(false);
      },
      managementManageEditSave: function() {
		     var me=this;
         //refresh the list
         this.managementManageSearch();
         //Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);

         var form = Ext.getCmp('rootViewport').down('fieldset[itemId=edit]').down('form[itemId=form]').getForm();
         var record = form.getRecord();
         record.set(form.getValues());

         var writer = Ext.create('Ext.data.writer.Json');
         var data = writer.getRecordData(record);

         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/admin/users/" + record.get('uid') + "?sessionToken=" + com.actility.global.sessionToken);

         // 4) Start the loading                 
         com.actility.ajax.ajaxCall({
            method: "PUT",
            url: url,
            params: data,
            callbackSuccess: function(json) {
               me.specificInfo(Ext.getCmp('footer'), "user edit saved");

               //refresh the list
               me.managementManageSearch();

               //hide the panel
               Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);
            },
            callbackError: function(errorMessage, response) {
               me.specificError(Ext.getCmp('footer'), errorMessage);
               Ext.getCmp('management').down('grid').setLoading(false);
            }
         });
      },
      managementManageCreateSave: function() {
		     var me=this;		 
         //refresh the list
         me.managementManageSearch();
         //Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);

         var form = Ext.getCmp('rootViewport').down('fieldset[itemId=create]').down('form[itemId=form]').getForm();
         //var record = form.getRecord();
         //record.set(form.getValues());

         //var writer = Ext.create('Ext.data.writer.Json');
         //var data = writer.getRecordData(record);

         var data = form.getValues();
         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/admin/users?sessionToken=" + com.actility.global.sessionToken);

         // 4) Start the loading                 

         com.actility.ajax.ajaxCall({
            method: "POST",
            url: url,
            params: data,
            callbackSuccess: function(json) {
               me.specificInfo(Ext.getCmp('footer'), "user creation done");

               //refresh the list
               me.managementManageSearch();

               //hide the panel
               Ext.getCmp('management').down('fieldset[itemId=create]').setVisible(false);
            },
            callbackError: function(errorMessage, response) {
               me.specificError(Ext.getCmp('footer'), errorMessage);
            }
         });
      },
      managementManageDeleteConfirm: function(btn) {
		     var me=this;		 
         if (btn == "yes") {
            var selection = Ext.getCmp('management').down('grid').selModel.selected.items;
            if (selection.length > 0) {
               var selected = selection[0];
               // 2) build url
               var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/admin/users/" + selected.get('uid') + "?sessionToken=" + com.actility.global.sessionToken);

               com.actility.ajax.ajaxCall({
                  method: "DELETE",
                  url: url,
                  callbackSuccess: function(json) {
                     me.specificInfo(Ext.getCmp('footer'), "user deletion done");

                     //refresh the list
                     me.managementManageSearch();

                  },
                  callbackError: function(errorMessage, response) {
                     me.specificError(Ext.getCmp('footer'), errorMessage);
                  }
               });
            } else
               me.specificWarning(Ext.getCmp('footer'), 'Please select an item from the list');
         }
      },
      managementManageDelete: function() {
         var selection = Ext.getCmp('management').down('grid').selModel.selected.items;
         if (selection.length > 0) {
            Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);
            Ext.getCmp('management').down('fieldset[itemId=create]').setVisible(false);
            var selected = selection[0];
            Ext.MessageBox.confirm('Confirm', 'Ok to delete ' + selected.get('username') + ' ?', com.actility.specific.extjs.managementManageDeleteConfirm);
         } else
            this.specificWarning(Ext.getCmp('footer'), 'Please select an item from the list');
      },
      managementManageEdit: function() {
         var selection = Ext.getCmp('management').down('grid').selModel.selected.items;
         if (selection.length > 0) {
            Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(true);
            Ext.getCmp('management').down('fieldset[itemId=create]').setVisible(false);
            var selected = selection[0];
            Ext.getCmp('rootViewport').down('form[itemId=form]').getForm().loadRecord(selected);

            var autologinURL = location.href;
            autologinURL += "?login=" + selected.get('username');
            autologinURL += "&pwd=" + selected.get('password');

            Ext.getCmp('management').down('fieldset[itemId=edit]').down('displayfield[itemId=autologinURL]').setValue(autologinURL);
         } else
            this.specificWarning(Ext.getCmp('footer'), 'Please select an item from the list');
      },
      managementManageAdd: function() {
         Ext.getCmp('management').down('fieldset[itemId=edit]').setVisible(false);
         Ext.getCmp('management').down('fieldset[itemId=create]').setVisible(true);
      },
      managementManageSearch: function() {
		     var me=this;		 
         var email = Ext.getCmp('management').down('fieldset[itemId=search]').down('textfield[itemId=email]').getValue();
         var customerId = Ext.getCmp('management').down('fieldset[itemId=search]').down('textfield[itemId=customerId]').getValue();

         //----FIRST GRAPH
         // 1) GET STORE reference and prepare URL
         var t1 = Ext.getStore('userStore');

         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/admin/users?email=" + email + "&customerId=" + customerId + "&sessionToken=" + com.actility.global.sessionToken);

         // 3) Prepare the action on store loaded : calculate max and redraw the graph            
         t1.on('datachanged', function(store, records, options) {
            me.specificInfo(Ext.getCmp('footer'), 'User list loaded');
            Ext.getCmp('management').down('grid').setLoading(false);
         });

         //Ext.getCmp('rootViewport').suspendLayout = true;
         Ext.getCmp('management').down('grid').setLoading(true);
         // 4) Start the loading                 
         com.actility.ajax.ajaxCall({
            method: "GET",
            url: url,
            callbackSuccess: function(json) {
               if (typeof(json.data) != 'undefined') {
                  t1.loadData(json.data);
                  if (typeof(json.message) != 'undefined')
                     me.specificInfo(Ext.getCmp('footer'), 'User list loaded ' + json.message);
               } else
                  t1.loadData([]);
            },
            callbackError: function(errorMessage, response) {
               me.specificError(Ext.getCmp('footer'), errorMessage);
               Ext.getCmp('management').down('grid').setLoading(false);
            }
         });
      },
      //*************************************
      // LOGIN
      //*************************************
      //USED
      manageLoginAfterRender: function() {},
      manageLoginPasswordSpecialKeys: function(field, event) {
         if (event.getKey() === event.ENTER) {
            var login = Ext.getCmp('login').down('textfield[itemId=email]').getValue();
            var password = Ext.getCmp('login').down('textfield[itemId=password]').getValue();
            this.manageLogin(login, password);
         }
      },
      manageLoginButtonClick: function() {
         var login = Ext.getCmp('login').down('textfield[itemId=email]').getValue();
         var password = Ext.getCmp('login').down('textfield[itemId=password]').getValue();
         this.manageLogin(login, password);
      },
      manageAuthenticate: function(uid, userAccount, sessionToken,type) {
	       var me=this;	 
         var toLocaleDate = function(d) {
            if (d == "unknown") return "";
            return (d) ? (new Date(d)).toLocaleString() : "";
         };

         console.warn("Thingpark WLogger manageAuthenticate type="+type);
         console.warn("Thingpark WLogger manageAuthenticate com.actility.global.guiMode="+com.actility.global.guiMode);
         
         // 3) Start the connexion
         com.actility.ajax.ajaxCall({
            method: "POST",
            url: com.actility.global.buildUrl("/rest/users/" + uid + "/authenticate?sessionToken=" + sessionToken+"&type="+type),
            //Disclaimer
            params: {
               userAccount: userAccount
            },
            callbackSuccess: function(json) {
               // 4.0) store the sessionToken in the context
               com.actility.global.sessionToken = sessionToken;
               // 4.1) Store the userId in the context
               com.actility.global.userId = json.data.ID;
               // 5) Store the customerId list in the context
               com.actility.global.customerIdList = json.data.customerIds;
               if (json.data.disclaimer !== null && json.data.disclaimer.disclaimerRequired && type != "module" && type != "portal") {
                  var config = {
                           disclaimerMessage : json.data.disclaimer.disclaimerMessage,
                           who : json.data.disclaimer.who,
                           previousConnection :toLocaleDate(json.data.disclaimer.previousConnection),
                           thingparkLastUnsuccessfulLogin :toLocaleDate(json.data.disclaimer.thingparkLastUnsuccessfulLogin),
                           thingparkPreviousBadConsecutivePwd :json.data.disclaimer.thingparkPreviousBadConsecutivePwd,
                           onButtonPress : me.toolStartGUI
                   };
                   com.actility.specific.utils.buildDisclaimer(config);
               } //If Disclaimer
               else
                  me.toolStartGUI()
            },
            callbackError: function(errorMessage, response) {
               me.specificNotification(errorMessage, 'error', false);
//               me.specificError(Ext.getCmp('login'), errorMessage);
            }
         });
      },
      manageAuthenticateByKeycloak: function(type) {
         var me=this;  
         var toLocaleDate = function(d) {
            if (d == "unknown") return "";
            return (d) ? (new Date(d)).toLocaleString() : "";
         };

         console.warn("Thingpark WLogger manageAuthenticateByKeycloak type="+type);
         console.warn("Thingpark WLogger manageAuthenticateByKeycloak com.actility.global.guiMode="+com.actility.global.guiMode);
         
         // 3) Start the connexion
         com.actility.ajax.ajaxCall({
            method: "GET",
            url: com.actility.global.buildUrl("/rest/users"),
            //Disclaimer
            callbackSuccess: function(json) {
               // 4.0) store the sessionToken in the context
               com.actility.global.sessionToken = json.data.sessionId;
               // 4.1) Store the userId in the context
               com.actility.global.userId = json.data.ID;
               // 5) Store the customerId list in the context
               com.actility.global.customerIdList = json.data.customerIds;
               if (json.data.disclaimer !== null && json.data.disclaimer.disclaimerRequired && type != "module" && type != "portal") {
                  var config = {
                           disclaimerMessage : json.data.disclaimer.disclaimerMessage,
                           who : json.data.disclaimer.who,
                           previousConnection :toLocaleDate(json.data.disclaimer.previousConnection),
                           thingparkLastUnsuccessfulLogin :toLocaleDate(json.data.disclaimer.thingparkLastUnsuccessfulLogin),
                           thingparkPreviousBadConsecutivePwd :json.data.disclaimer.thingparkPreviousBadConsecutivePwd,
                           onButtonPress : me.toolStartGUI
                   };
                   com.actility.specific.utils.buildDisclaimer(config);
               } //If Disclaimer
               else
                  me.toolStartGUI()
            },
            callbackError: function(errorMessage, response) {
               me.specificNotification(errorMessage, 'error', true);
//               me.specificError(Ext.getCmp('login'), errorMessage);
            }
         });
      },
      toolStartGUI: function() {
         if (Ext.getCmp("boot"))
            Ext.getCmp("boot").destroy();
         if (Ext.getCmp("login"))
            Ext.getCmp("login").destroy();
         if (com.actility.global.viewport != null)
            com.actility.global.viewport.destroy();

         //6) customer Id list
         com.actility.specific.extjs.refreshCustomerList();

         if (com.actility.global.viewportType == "LTE")
            com.actility.global.viewport = Ext.create('WirelessLogger.view.viewportLte');
         else
            com.actility.global.viewport = Ext.create('WirelessLogger.view.viewportLora');
         com.actility.global.viewport.show();
         //affiche t'on le menu de changement de protocol
         if (com.actility.global.lteModeActif === true)
             Ext.getCmp('protocolSwitch').setVisible(true);
         //on est integre en module --> cacher le header
         if (com.actility.global.guiMode === "module") {
            Ext.getCmp("north").destroy();
            Ext.getCmp('rootViewport').down('button[itemId=logout]').setVisible(false);
         }
         else
         {
            //patch
            //pas en module : bloquer le back
            com.actility.specific.utils.startTrackingUnload();
         }
         //6) update customer Id list
         com.actility.specific.extjs.toolRefreshCustomerList();
      },
      manageLogin: function(login, password) {
         var me=this;					 
         if (login == "" || password == "")
            this.specificNotification(com.actility.locale.translate("WR Please enter an Email and Password"), 'error', false);
            //this.specificWarning(Ext.getCmp('login'), com.actility.locale.translate("WR Please enter an Email and Password"));
         else {
            // 3) Start the connexion                 
            com.actility.ajax.ajaxCall({
               method: "POST",
//               url: com.actility.global.buildUrl("/rest/users/login"),
               url: com.actility.global.buildUrl("/rest/users/logingui"),
               params: {
                  'username': login,
                  'password': password
               },
               callbackSuccess: function(json) {
                  // 4.0) store the sessionToken in the context
                  com.actility.global.sessionToken = json.data.sessionId;

                  // 4.1) Store the userId in the context
                  com.actility.global.userId = json.data.ID;

                  // 5) Store the customerId list in the context
                  com.actility.global.customerIdList = json.data.customerIds;

                  //6) Start the GUI
                  me.toolStartGUI("unknown");

                  //7) update customer Id list
                  me.toolRefreshCustomerList();
               },
               callbackError: function(errorMessage, response) {
                  me.specificNotification(errorMessage, 'error', false);
                  //me.specificError(Ext.getCmp('login'), errorMessage);
               }
            });
         }
      },
      //*************************************
      // TREEVIEW
      //*************************************
      manageTreeViewClick: function(record, e) {
         var node = Ext.getCmp('navigationTreePanel').getStore().getNodeById(record.get('id'));
         if (node == undefined)
            return;
         Ext.getCmp('navigationTreePanel').getSelectionModel().select(record);

         if (node.data.text == "Dashboard")
            Ext.getCmp('rootViewport').down('container[itemId=container]').getLayout().setActiveItem(0);
         else if (node.data.text == "Management")
            Ext.getCmp('rootViewport').down('container[itemId=container]').getLayout().setActiveItem(1);
      },
      //*************************************
      //COMBO TOOL
      //*************************************
      manageToolCombo: function(selectedValue) {
        // console.log(selectedValue);
        if (selectedValue == 'LPWA-LoRa') {
            com.actility.specific.extjs.manageGearMenuLora(1,true);
        }
        if (selectedValue == 'Cellular') {
            com.actility.specific.extjs.manageGearMenuLTE(1,true);
        }
      },
      //*************************************
      //GEAR TOOL
      //*************************************
      manageToolGear: function(e,el,owner,tool) {
	       var me=this;		 
         //console.log("manageToolGear");
         var menu = Ext.getCmp('protocolMenu');
         if (!menu) {
            //console.log("creation");
            //get default viewport type
            var loraChecked=true;
            if (com.actility.global.viewportType !== "LORA")
            {
               loraChecked=false;
            }
            menu = Ext.create('Ext.menu.Menu', {
                                id: 'protocolMenu',
				items: [
                                '<b class="menu-title">'+com.actility.locale.translate("LBL Choose a Protocol")+'</b>',
                                {

					text: com.actility.locale.translate("LBL LPWA - LoRa"),
                                        group: 'protocol',
                                        checked: loraChecked,
                                        checkHandler: me.manageGearMenuLora
				},{
					text: com.actility.locale.translate("LBL Cellular"),
                                        group: 'protocol',
                                        checked: !loraChecked,
                                        checkHandler: me.manageGearMenuLTE
				}]

			});
         }
         menu.showBy(owner,'tr-br?');
      },
      manageGearMenuLora: function(item, checked)
      {
         var status="unchecked";
         if (checked)
         {
            status="checked";
            com.actility.global.viewportType="LORA";
            com.actility.specific.extjs.toolStartGUI();
         }
         //console.log("manageGearMenuLora"+status);
      },
      manageGearMenuLTE: function(item, checked)
      {
         var status="unchecked";
         if (checked)
         {
            status="checked";
            com.actility.global.viewportType="LTE";
            com.actility.specific.extjs.toolStartGUI();
         }
         //console.log("manageGearMenuLTE"+status);
      },

      //*************************************
      // DASHBOARD
      //*************************************
      manageResize: function(newWidth, newHeight) {
      },
      manageDashboardLogoutClick: function() {
        if (!Ext.isEmpty(WirelessLogger) && !Ext.isEmpty(WirelessLogger.util) && !Ext.isEmpty(WirelessLogger.util.Keycloak) && WirelessLogger.util.Keycloak.isReady()) {
          WirelessLogger.util.Keycloak.logout();
        }
        com.actility.specific.utils.logout(function(){},function(){});
      },
      manageDashboardMapPathClick: function(record) {
         com.actility.specific.utils.mapExecuteOrLoadMapAPI(this.executeDashboardMapPathClick, record);
      },
      executeDashboardMapPathClick: function(record) {

	       var lrrLat = record.get('LrrLAT');
         var lrrLon = record.get('LrrLON');
         var deviceUid = record.get('DevEUI');

         //1) retrieve current data uid (begin/end)
         var store = Ext.getStore('dataStore');
         var end = store.getAt(0).get('uid');
         var begin = store.getAt(0).get('uid');
         //var end = store.getAt(0).get('uid');
         //var begin = store.getAt(store.getCount() - 1).get('uid');
         store.each(function(record) {
            if (record.get('uid') > end) end = record.get('uid');
            if (record.get('uid') < begin) begin = record.get('uid');
         });

         var decoder = Ext.getCmp('dashboard').down('combobox[itemId=decoder]').getValue();
         var subtype = Ext.getCmp('dashboard').down('combobox[itemId=subtype]').getValue();

                  // control subtype
         if (subtype == null || subtype == "null") {
            subtype = "";
         }

         var filteringLRC = Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').getValue();
         var filteringASID = Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').getValue();
         if (com.actility.global.viewportType == "LORA")
         {
            var filteringDevAddr = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').getValue());
            var filteringDevEUI  = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').getValue());
            var filteringLRR     = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').getValue());
            var filteringSubscriberId = Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').getValue();
         }
         else
         {
            var filteringDevAddr =  Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').getValue();
            var filteringDevEUI  =  Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').getValue();
            var filteringLRR     =  Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').getValue();
            var filteringSubscriberId = '';
         }
//         var last = Ext.getCmp('dashboard').down('textfield[itemId=last]').getValue();
         var last = "";

         // 1) get protocolType
         var type=0;
         if (com.actility.global.viewportType == "LTE") type=1;

         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/devices/" + deviceUid + "/locations?last=" + last 
                    + "&type=" + type + "&startUid=" + begin + "&endUid=" + end + "&decoder=" + decoder + "&devicesAddr=" + filteringDevAddr 
                    + "&devicesID=" + filteringDevEUI + "&LRRID=" + filteringLRR + "&LRCID=" + filteringLRC 
                    + "&sessionToken=" + com.actility.global.sessionToken + "&subscriberID=" + filteringSubscriberId 
                    + "&ASID=" + filteringASID + "&subtype=" + subtype );

         //3) generate Ajax call to retrieve the positions
         com.actility.ajax.ajaxCall({
            method: "GET",
            url: url,
            callbackSuccess: function(json) {
               com.actility.specific.extjs.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Device path retrieved"));

			   var idistance = null;
			   var distance = record.get('distance');
			   if ((distance!==0)&&(distance!==null)&&(distance!="null")&&(distance!="")) {
			  	idistance = Number(distance);
			   }
			   
               var config={
                  cartoType : 'path',
                  title : 'Path [' + deviceUid + ']',
                  deviceUid : deviceUid,
//                  distance : record.get('distance'),
                  distance : idistance,
                  lrrLat : record.get('LrrLAT'),
                  lrrLon : record.get('LrrLON'),
                  lrrId : record.get('Lrrid'),
                  data : json.data,
                  onMapReady : com.actility.specific.utils.mapCallbackDisplayPath
               };
               com.actility.specific.utils.buildMapPanel(config); 
            },
            callbackError: function(errorMessage, response) {
               com.actility.specific.extjs.specificError(Ext.getCmp('footer'), com.actility.locale.translate("WR Device path cannot be retrieved"));
            }
         });
      },
      manageDashboardMapLocationClick: function(record) {
         com.actility.specific.utils.mapExecuteOrLoadMapAPI(this.executeDashboardMapLocationClick, record);
      },
      executeDashboardMapLocationClick: function(record) {

	       var lrrLat = record.get('LrrLAT');
         var lrrLon = record.get('LrrLON');
         var deviceUid = record.get('DevEUI');

         var idistance = null;
         var distance = record.get('distance');
         if ((distance!==0)&&(distance!==null)&&(distance!="null")&&(distance!="")) {
            idistance = Number(distance);
         }

         var config = {
            cartoType: 'loc',
            title : 'Map [' + deviceUid + ']',
//            distance : record.get('distance'),
            distance : idistance,
            timestampUTC : record.get("timestampUTC"),
            lrrLat : record.get('LrrLAT'),
            lrrLon : record.get('LrrLON'),
            lrrId : record.get('Lrrid'),
            devLat : record.get('DevLAT'),
            devLon : record.get('DevLON'),
            deviceUid : record.get('DevEUI'),
            sf : record.get('SpFact'),
            snr : record.get('LrrSNR'),
            rssi : record.get("LrrRSSI"),
            esp : record.get("LrrESP"),
            mtc : record.get("Lrrid"),
            imei : record.get("DevEUI")/*record.get("lteIMEI")*/,
            msiisdn : record.get("lteMSISDN"),
            onMapReady :  com.actility.specific.utils.mapCallbackDisplayLocalization
         }
         com.actility.specific.utils.buildMapPanel(config);

      },
      manageDashboardMapClick: function() {
         com.actility.specific.utils.mapExecuteOrLoadMapAPI(this.executeDashboardMapClick);
      },
      executeDashboardMapClick: function() {
         var userId = com.actility.global.userId;
         // var last = Ext.getCmp('dashboard').down('combobox[itemId=last]').getValue();
         var last = "";
         var decoder = Ext.getCmp('dashboard').down('combobox[itemId=decoder]').getValue();
         var subtype = Ext.getCmp('dashboard').down('combobox[itemId=subtype]').getValue();

                  // control subtype
         if (subtype == null || subtype == "null") {
            subtype = "";
         }

         if (com.actility.global.viewportType == "LORA")
         {
            var filteringDevAddr = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').getValue());
            var filteringDevEUI  = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').getValue());
            var filteringLRR     = com.actility.specific.extjs.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').getValue());
            var filteringSubscriberId = Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').getValue();
         }
         else
         {
            var filteringDevAddr =  Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').getValue();
            var filteringDevEUI  =  Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').getValue();
            var filteringLRR     =  Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').getValue();
            var filteringSubscriberId = '';
         }
         var filteringLRC = Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').getValue();
         var filteringASID = Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').getValue();
         var fromDate = Ext.getCmp('dashboard').down('datefield[itemId=fromDate]').getValue();
         var toDate = Ext.getCmp('dashboard').down('datefield[itemId=toDate]').getValue();

         if (fromDate !== null) {
//            fromDate.setHours(0, 0, 0, 0);
            fromDate = fromDate.toISOString();
         } else fromDate = "";
         if (toDate !== null) {
//            toDate.setHours(24, 0, 0, 0);
            toDate = toDate.toISOString();
         } else toDate = "";

         // 1) get protocolType
         var type=0;
         if (com.actility.global.viewportType == "LTE") type=1;

         // build url
         var url = com.actility.global.buildUrl("/rest/users/" + userId + "/data?type=" + type + "&last=" + last + "&decoder=" + decoder 
                     + "&devicesAddr=" + filteringDevAddr + "&devicesID=" + filteringDevEUI + "&LRRID=" + filteringLRR + "&LRCID=" + filteringLRC 
                     + "&fromDate=" + fromDate + "&toDate=" + toDate + "&f=utc&sessionToken=" + com.actility.global.sessionToken 
                     + "&subscriberID=" + filteringSubscriberId 
                     + "&ASID=" + filteringASID + "&subtype=" + subtype );

         //3) generate Ajax call to retrieve the positions
         com.actility.ajax.ajaxCall({
            method: "GET",
            url: url,
            callbackSuccess: function(json) {
               com.actility.specific.extjs.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Map retrieved"));
               var config = {
                  cartoType : 'history',
                  title : 'Map',
                  data : json.data,
                  onMapReady : com.actility.specific.utils.mapCallbackDisplayLastPositions
               } 
               com.actility.specific.utils.buildMapPanel(config);
            },
            callbackError: function(errorMessage, response) {
               com.actility.specific.extjs.specificError(Ext.getCmp('footer'), com.actility.locale.translate("WR Map cannot be retrieved"));
            }
         });
      },
      manageDashboardExportClick: function() {
         var me=this;
         //1) retrieve current data uid (begin/end)
         var store = Ext.getStore('dataStore');

         if (store.getCount() === 0) {
            this.specificWarning(Ext.getCmp('footer'), com.actility.locale.translate("WR Nothing to export"));
            return;
         }
         var end = store.getAt(0).get('uid');
         var begin = store.getAt(0).get('uid');
         store.each(function(record) {
            if (record.get('uid') > end) end = record.get('uid');
            if (record.get('uid') < begin) begin = record.get('uid');
         });

         var decoder = Ext.getCmp('dashboard').down('combobox[itemId=decoder]').getValue();
         var subtype = Ext.getCmp('dashboard').down('combobox[itemId=subtype]').getValue();

                  // control subtype
         if (subtype == null || subtype == "null") {
            subtype = "";
         }

         if (com.actility.global.viewportType == "LORA")
         {
            var filteringDevAddr      = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').getValue());
            var filteringDevEUI       = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').getValue());
            var filteringLRR          = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').getValue());
            var filteringSubscriberId = Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').getValue();
         }
         else
         {
            var filteringDevAddr      =  Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').getValue();
            var filteringDevEUI       =  Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').getValue();
            var filteringLRR          =  Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').getValue();
            var filteringSubscriberId = '';
         }
         var filteringLRC = Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').getValue();
         var filteringASID = Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').getValue();
         var last = Ext.getCmp('dashboard').down('textfield[itemId=last]').getValue();

         var fromDate = Ext.getCmp('dashboard').down('datefield[itemId=fromDate]').getValue();
         var toDate = Ext.getCmp('dashboard').down('datefield[itemId=toDate]').getValue();
         if (fromDate !== null) {
            fromDate = fromDate.toISOString();
         } else fromDate = "";
         if (toDate !== null) {
            toDate = toDate.toISOString();
         } else toDate = "";

         // 1) get protocolType
         var type=0;
         if (com.actility.global.viewportType == "LTE") type=1;


         var lastInt = parseInt(last);
         // Warning message if decoder AUTOMATIC and more than 500 packets
         if (decoder == com.actility.global.AUTO_DECODER && lastInt > com.actility.global.maxAutomaticDecodedPackets) {
           this.showDecodingWarningMessage("LBL Automatic decoding cannot be performed when more than 500 packets are exported");
         }


         // 2) build url
         var url = com.actility.global.buildUrl("/rest/users/" + com.actility.global.userId + "/export?type="+ type + "&last=" + last
             + "&startUid=" + begin + "&endUid=" + end + "&decoder=" + decoder + "&devicesAddr=" + filteringDevAddr + "&devicesID=" + filteringDevEUI 
             + "&LRRID=" + filteringLRR + "&LRCID=" + filteringLRC + "&fromDate=" + fromDate + "&toDate=" + toDate 
             + "&f=utc&sessionToken=" + com.actility.global.sessionToken + "&subscriberID=" + filteringSubscriberId 
             + "&ASID=" + filteringASID + "&subtype=" + subtype);

         // 3) open the content
         window.open(url, "download");
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Export done"));
      },
      showDecodingWarningMessage: function(messageToTranslate) {
           var message = com.actility.locale.translate(messageToTranslate);
           message = message.replace("%s",com.actility.global.maxAutomaticDecodedPackets);

           Ext.Msg.alert('Warning', message);
           //2) push error message
           com.actility.specific.extjs.specificWarning(Ext.getCmp('footer'), message);
      },
      manageDashboardDevAddrClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR DevAddr filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').setValue("");
      },
      manageDashboardDevEUIClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR DevEUI filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').setValue("");
      },
      manageDashboardLRRClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR LRR filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').setValue("");
      },
      manageDashboardLRCClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR LRC filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').setValue("");
      },
      manageDashboardASIDClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR ASID filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').setValue("");
      },
      manageDashboardSubIDClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR SubscriberID filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').setValue("");
      },
      manageDashboardSubTypeClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Packet type filtering deactivated"));
         Ext.getCmp('dashboard').down('combobox[itemId=subtype]').setValue("");
      },
      manageDashboardDeviceAddrFiltering: function(devAddr) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on DevAddr") + " " + devAddr + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').setValue(devAddr);
      },
      manageDashboardSubscriberFiltering: function(subscriberId) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on Subscriber") + " " + subscriberId + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').setValue(subscriberId);
      },
//LTE EXTENSION
      manageDashboardSensorIPV4Filtering: function(sensorIPV4) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on Sensor IPV4") + " " + sensorIPV4 + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').setValue(sensorIPV4);
      },
      manageDashboardSensorIPV4ClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR IPV4 filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').setValue("");
      },
      manageDashboardIMEIFiltering: function(imei) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on IMEI") + " " + imei + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').setValue(imei);
      },
      manageDashboardIMEIClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR IMEI filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').setValue("");
      },
      manageDashboardMTCFiltering: function(mtc) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on MTC") + " " + mtc + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').setValue(mtc);
      },
      manageDashboardMTCClearClick: function() {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR MTC filtering deactivated"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').setValue("");
      },

//END EXTENSION 
      manageDashboardDeviceUIDFiltering: function(devEUI) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on DevEUI") + " " + devEUI + " "+ com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').setValue(devEUI);
      },
      manageDashboardLRRIdFiltering: function(lrrUID) {
         this.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on LRR") + " "+ lrrUID + " " + com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').setValue(lrrUID);
      },
      manageDashboardLRCIdFiltering: function(lrcUID) {
         this.specificWarning(Ext.getCmp('footer'), com.actility.locale.translate("WR Filtering on LRC") + " "+ lrcUID + " " + com.actility.locale.translate("WR selected"));
         Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').setValue(lrcUID);
      },
      manageDashboardRefreshClick: function() {
         this.toolRefreshDashboard(1);
      },
      manageDashboardAfterRender: function() {
         this.toolRefreshCustomerList();
         this.toolRefreshDashboard(1);

         var t1 = Ext.getStore('dataStore');
         t1.addListener("datachanged", com.actility.specific.extjs.callbackStoreDataChanged);

      },
      manageDashboardAutoReloadChange: function(newValue) {
         if (newValue === 0)
            this.toolStopDashboardRefresh();
         else
            this.toolStartDashboardRefresh(newValue);
      },
      manageDashboardExpandAllChange: function(newValue) {
         this.toolManageExpand(newValue);
      },
      bottomToolbarInfoTextChange: function(f, new_val) {
         function syncValue(text, mask, object) {
            filteringBegin = text.indexOf(mask);
            if (filteringBegin <= 0) return;
            filteringBegin += mask.length;
            filteringPart = text.substring(filteringBegin);
            filteringEnd = filteringPart.indexOf("\n");
            filtering = filteringPart.substring(0, filteringEnd);
            object.setValue(filtering);
         }
         var me=this;    

         if (com.actility.global.viewportType == "LORA")
         {
           syncValue(new_val, "DevAddr(s) : ", Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]'));
           syncValue(new_val, "DevEUI(s)  : ", Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]'));
           syncValue(new_val, "LRR Ids    : ", Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]'));
           syncValue(new_val, "Sub Id     : ", Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]'));
         }
         else
         {
           syncValue(new_val, "DevAddr(s) : ", Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]'));
           syncValue(new_val, "DevEUI(s)  : ", Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]'));
           syncValue(new_val, "LRR Ids    : ", Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]'));
         }
         syncValue(new_val, "LRC Ids    : ", Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]'));
         syncValue(new_val, "From       : ", Ext.getCmp('dashboard').down('datefield[itemId=fromDate]'));
         syncValue(new_val, "To         : ", Ext.getCmp('dashboard').down('datefield[itemId=toDate]'));
         syncValue(new_val, "Decoder    : ", Ext.getCmp('dashboard').down('combobox[itemId=decoder]'));
         syncValue(new_val, "subtype    : ", Ext.getCmp('dashboard').down('combobox[itemId=subtype]'));
         syncValue(new_val, "Last       : ", Ext.getCmp('dashboard').down('combobox[itemId=last]'));
      },
      bottomToolbarInfoClick: function() {
		     var me=this;		 
         var last = Ext.getCmp('dashboard').down('combobox[itemId=last]').getValue();
         var decoder = Ext.getCmp('dashboard').down('combobox[itemId=decoder]').getValue();
         var subtype = Ext.getCmp('dashboard').down('combobox[itemId=subtype]').getValue();

                  // control subtype
         if (subtype == null || subtype == "null") {
            subtype = "";
         }

         if (com.actility.global.viewportType == "LORA")
         {
            var filteringDevAddr      = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevAddr]').getValue());
            var filteringDevEUI       = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterDevEUI]').getValue());
            var filteringLRR          = me.cleanInputs(Ext.getCmp('dashboard').down('textfield[itemId=filterLRR]').getValue());
            var filteringSubscriberId = Ext.getCmp('dashboard').down('textfield[itemId=filterSubscriberID]').getValue();
         }
         else
         {
            var filteringDevAddr      =  Ext.getCmp('dashboard').down('textfield[itemId=filterIPV4]').getValue();
            var filteringDevEUI       =  Ext.getCmp('dashboard').down('textfield[itemId=filterIMEI]').getValue();
            var filteringLRR          =  Ext.getCmp('dashboard').down('textfield[itemId=filterMTC]').getValue();
            var filteringSubscriberId = '';
         }
         var filteringLRC = Ext.getCmp('dashboard').down('textfield[itemId=filterLRC]').getValue();
         var filteringASID = Ext.getCmp('dashboard').down('textfield[itemId=filterASID]').getValue();
         var from = Ext.Date.format(Ext.getCmp('dashboard').down('datefield[itemId=fromDate]').getValue(), "Y-m-d");
         var to = Ext.Date.format(Ext.getCmp('dashboard').down('datefield[itemId=toDate]').getValue(), "Y-m-d");

         var message = "Current settings\n";
         message += "----------------\n";
         message += "Filtering\n";
         message += "DevAddr(s) : " + filteringDevAddr + "\n";
         message += "DevEUI(s)  : " + filteringDevEUI + "\n";
         message += "LRR Ids    : " + filteringLRR + "\n";
         message += "LRC Ids    : " + filteringLRC + "\n";
         message += "Sub Id     : " + filteringSubscriberId + "\n";
         message += "From       : " + from + "\n";
         message += "To         : " + to + "\n";
         message += "-----------------\n";
         message += "Decoder    : " + decoder + "\n";
         message += "subtype    : " + subtype + "\n";
         message += "Last       : " + last + "\n";
         var win = Ext.create('Ext.window.Window', {
            title: 'Settings',
            height: 280,
            width: 400,
            layout: 'fit',
            items: [{
               xtype: 'textareafield',
               anchor: '100%',
               value: message,
               fieldStyle: {
                  'fontFamily': 'courier new',
                  'fontSize': '12px'
               },
               listeners: {
                  change: me.bottomToolbarInfoTextChange
               }
            }],
            closable: true,
            resizable: true
         });
         win.show();
      }
   } //statics
});

