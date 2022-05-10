Ext.define('com.actility.boot',{
   statics : {

      readBootConfig: function(response) {
            var jsonParameters = Ext.decode(response.responseText);
            jsonData = jsonParameters.data;

            com.actility.global.googleBrowserApiKey = "";

            com.actility.global.thingparkUrl = window.location.origin;

            com.actility.global.CONFIG = jsonData;

            if (jsonData.length > 0) {
               for (var i=0; i < jsonData.length; i++) {
                  switch (jsonData[i].name) {
                  case "THINGPARK_APPID" :
                     com.actility.global.thingparkApplicationID = jsonData[i].value;
                     if (this.debug) console.log("found THINGPARK_APPID:"+jsonData[i].value);
                     break;
//                  case "THINGPARK_URL" :
//                     com.actility.global.thingparkUrl = jsonData[i].value;
//                     if (this.debug) console.log("found THINGPARK_URL:"+jsonData[i].value);
//                     break;               
                  case "VERSION" :
                     com.actility.global.version = jsonData[i].value;
                     if (this.debug) console.log("found VERSION:"+jsonData[i].value);
                     break;               
                  case "GOOGLE_BROWSER_API_KEY" :
                     com.actility.global.googleBrowserApiKey = jsonData[i].value;
					           com.actility.global.gmapsUrl += com.actility.global.googleBrowserApiKey;
                     if (this.debug) console.log("found GOOGLE_BROWSER_API_KEY:"+jsonData[i].value);
                     break;
                  case "MAP_SERVICE_MODULE_ACCESS" :
                     com.actility.global.mapServiceModuleAccess = jsonData[i].value;
                     if (this.debug) console.log("found MAP_SERVICE_MODULE_ACCESS:"+jsonData[i].value);
                     break;
                  case "MAP_SERVICE_DIRECT_ACCESS" :
                     com.actility.global.mapServiceDirectAccess = jsonData[i].value;
                     if (this.debug) console.log("found MAP_SERVICE_DIRECT_ACCESS:"+jsonData[i].value);
                     break;
                  case "LEAFLET_URL_TEMPLATE" :
                     com.actility.global.osmUrl = jsonData[i].value;
                     if (this.debug) console.log("found LEAFLET_URL_TEMPLATE:"+jsonData[i].value);
                     break;
                  case "BMAPS_API_KEY" :
                     com.actility.global.bmapsApiKey = jsonData[i].value;
                     com.actility.global.bmapsUrl += com.actility.global.bmapsApiKey;
                     if (this.debug) console.log("found BMAPS_API_KEY:"+jsonData[i].value);
                     break;
                  case "GMAPS_ECJ_ENCRYPTION" :
                     com.actility.global.gmapsEcjEncryptMode = jsonData[i].value;
                     if (this.debug) console.log("found GMAPS_ECJ_ENCRYPTION:"+jsonData[i].value);
                     break;					 
                     /*
                  case "NOMINATIM_URL" :
                     com.actility.global.nominatimUrl = jsonData[i].value;
                     if (this.debug) console.log("found NOMINATIM_URL:"+jsonData[i].value);
                     break;
                     */
                  case "LOCALIZATION" :
                     com.actility.global.localizationMode = jsonData[i].value;
                     if (this.debug) console.log("found LOCALIZATION:"+jsonData[i].value);
                     break;
                  case "LTE_MODE" :
                     if (jsonData[i].value == "1")
                       com.actility.global.lteModeActif = true;
                     else
                       com.actility.global.lteModeActif = false;
                     if (this.debug) console.log("found LTE_MODE:"+jsonData[i].value);
                     break;           
                  case "ADMIN_LOGIN" :
                    if (jsonData[i].value == "1")
                       com.actility.global.adminLogin = true;
                    else
                       com.actility.global.adminLogin = false;
                    if (this.debug) console.log("found ADMIN_LOGIN:"+jsonData[i].value);
                    break;                
                  case "PAGE_SIZE" :
                     com.actility.global.pageSize = jsonData[i].value;
                     if (this.debug) console.log("found PAGE_SIZE:"+jsonData[i].value);
                     break;               
                  case "MAX_PAGES" :
                     com.actility.global.maxPages = jsonData[i].value;
                     if (this.debug) console.log("found MAX_PAGES:"+jsonData[i].value);
                     break;               
                  case "MAX_AUTOMATIC_DECODED_PACKETS" :
                     com.actility.global.maxAutomaticDecodedPackets = jsonData[i].value;
                     if (this.debug) console.log("found MAX_AUTOMATIC_DECODED_PACKETS:"+jsonData[i].value);
                     break;               
                  case "MODE_DEV" :
                     com.actility.global.modeDEV = (jsonData[i].value == "1");
                     if (this.debug) console.log("found MODE_DEV:"+(jsonData[i].value == "1"));
                     break;               

                  default:
                     break;
                  }
               }
            } else
               if (this.debug) console.log("jboss configuration parameters not found");

      },

      boot: function() {

        var onSuccess = function(response) {

            var ssoLoadingOnError = function() {
               console.warn("Thingpark SSO loading error");
            };
            var ssoLoadingOnSuccess = function() {
               //console.log("Thingpark SSO loaded");
            };

            //2) read config
            com.actility.boot.readBootConfig(response);

            var type = Ext.Object.fromQueryString(location.search.substring(1)).type;

            //console.log("BOOT boot");
            //console.warn("Thingpark WLogger BOOT type="+type);

            if (type == undefined)
               com.actility.global.guiMode = 'standalone';
            else
               com.actility.global.guiMode = type;
            if (type == "module") {
              var operatorDomain = Ext.Object.fromQueryString(location.search.substring(1)).operator;

              com.actility.global.mapService = com.actility.global.mapServiceModuleAccess.toUpperCase();

            } else {
              var operatorDomain = window.location.host;
              operatorDomain = operatorDomain.replace(window.location.port,"");
              operatorDomain = operatorDomain.replace(":","");

              com.actility.global.mapService = com.actility.global.mapServiceDirectAccess.toUpperCase();
              
            }
            console.warn("Thingpark WLogger boot type="+type);
            console.warn("Thingpark WLogger boot com.actility.global.guiMode="+com.actility.global.guiMode);
            com.actility.global.operatorDomain = operatorDomain;
            var defaultLanguage = 'en-US';

            //IMPORTANT----------------------------------------------------------------------------------------------------
            // language-tag :
            //   in module mode, language-tag is given in the URL by SMP (after manageRestThingparkModule)
            //   in portal mode SSO user, language-tag is given in the URL by WLOGGER (after manageRestThingparkPortal)
            //   in portal mode SSO subscriber, language-tag is given in the URL by WLOGGER (after manageRestThingparkPortal)
            //--------------------------------------------------------------------------------------------------------------
            var lang = Ext.Object.fromQueryString(location.search.substring(1))["language-tag"] || Ext.Object.fromQueryString(location.search.substring(1)).lang ||
                        navigator.language || window.navigator.languages[0] || window.navigator.languages[2] || defaultLanguage;
            lang = lang.replace("_", "-"); // use ISO instead
            com.actility.global.lang = lang;

            //3) load thingpark single sign on openSSOWindow script
//            var pathToSSOScript = com.actility.global.thingparkUrl + "/thingpark/smp/openSSOWindow.js";
//            Ext.Loader.injectScriptElement(pathToSSOScript, ssoLoadingOnSuccess, ssoLoadingOnError);

            var login = Ext.Object.fromQueryString(location.search.substring(1)).login;
            var password = Ext.Object.fromQueryString(location.search.substring(1)).pwd;
            var uid = Ext.Object.fromQueryString(location.search.substring(1)).uid;
            var userAccount = Ext.Object.fromQueryString(location.search.substring(1)).userAccount;
            var sessionToken = Ext.Object.fromQueryString(location.search.substring(1)).sessionToken;

            com.actility.specific.extjs.specificClearQueryParameters();

            //GET THE LOCALIZATIONS
            if (com.actility.global.localizationMode == 'LOCALE')
            {
                var localizationURL=com.actility.global.buildUrl("/rest/resources/files/translations");
                var localizationURLError=com.actility.global.buildUrl("/rest/resources/files/errors");
            }
            else
            {
                var localizationURL=com.actility.global.thingparkUrl+"/thingpark/smp/rest/resources/files/translations";
                var localizationURLError=com.actility.global.thingparkUrl+"/thingpark/smp/rest/resources/files/errors";
            }
            var params={
                'domain' : com.actility.global.operatorDomain,
                'language-tag': com.actility.global.lang,
                'application':'wlogger'
            };

            $summary ="lang="+lang+"\nuid="+uid+"\nlogin="+login+"\npassword="+password;
            $summary+="\nuserAccount="+userAccount+"\nsessionToken="+sessionToken;
            $summary+="\nlocalization URL:"+localizationURL;
            //console.log($summary);

            var localeLoadingOnError = function() {
               com.actility.locale.load({
                         urlPath: com.actility.global.buildUrl("/rest/resources/files/translations"),
                         params: params,
                         callback: localeLoadingOnSuccess,
                         failure: localeLoadingOnSuccess //degraded GUI
                        },{});
            };

            var localeLoadingOnSuccess = function() {

              com.actility.locale.loaderrors({
                           urlPath: localizationURLError,
                           params: params
                          },{});

              console.log('com.actility.global.guiMode:'+ com.actility.global.guiMode);
              displayLogin = false;
              if (!(com.actility.global.guiMode == "module" || com.actility.global.guiMode == com.actility.global.adminAppliName)) {

                WirelessLogger.util.Keycloak.onReady(function() {
                  com.actility.specific.extjs.manageAuthenticateByKeycloak(type);
                });

              } else {
                displayLogin = true;
              }

              Ext.create('WirelessLogger.view.bootPanel', {renderTo: Ext.getBody()});

              if (uid != undefined) {
                  //=========================================================
                  //MAIN ENTRY
                  //=========================================================
                  //CASE 1: impersonate, module access code, we have the uid, language-tag
                  //CASE 2: portal, subscription access code, we have the uid, language-tag
                  //CASE 3: user access code, we have the uid, language-tag
                  //=========================================================
                  //module mode : language retrieved from url (language-tag) field

                  com.actility.specific.extjs.manageAuthenticate(uid, userAccount, sessionToken,type);
                  //NOTA : GUI is created later on by com.actility.specific.extjs.toolStartGUI

              } else if (login != undefined && password != undefined) {
                  //----------------------------------------------
                  //CASE : OLD LOGIN screen
                  //----------------------------------------------

                  com.actility.specific.extjs.manageLogin(login, password);
                  //NOTA : GUI is created later on by com.actility.specific.extjs.toolStartGUI

              } else {
                  //----------------------------------------------
                  //CASE : direct connexion through SSO procedure
                  //----------------------------------------------
                  if (displayLogin)
                    com.actility.login.displaySSOLoginPopup(com.actility.global.thingparkApplicationID,location.hostname);
                  //THIS WILL LEAD TO CASE 3
              }
            };

            com.actility.locale.load({
                         urlPath: localizationURL,
                         params: params,
                         callback: localeLoadingOnSuccess,
                         failure: localeLoadingOnError
                        },{});
         };

         var onError = function(errorMessage, response) {
         };

         var type = Ext.Object.fromQueryString(location.search.substring(1)).type;

         if (type == "module") {
           var operatorDomain = Ext.Object.fromQueryString(location.search.substring(1)).operator;
         } else {
           var operatorDomain = window.location.host;
           operatorDomain = operatorDomain.replace(window.location.port,"");
           operatorDomain = operatorDomain.replace(":","");
         }

         Ext.Ajax.request({
            url: com.actility.global.buildUrl('/rest/boot?domain='+operatorDomain),
            success: onSuccess,
            failure: onError
         });

      },

   }
});
