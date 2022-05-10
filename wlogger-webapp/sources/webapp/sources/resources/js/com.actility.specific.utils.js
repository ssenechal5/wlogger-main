Ext.define('com.actility.specific.utils', {
   statics: {
      windowUnload: function (e)
      {
         var message = "Want to exit ?";
         e = e || e.event;
         if (e) e.returnValue = message;
         return message;
      },
      startTrackingUnload: function()
      {
         //console.log("startTrackingUload");
         if (window.addEventListener) {
            window.addEventListener("beforeunload", com.actility.specific.utils.windowUnload, false);
         } else if (window.attachEvent) {
            window.attachEvent("onbeforeunload", com.actility.specific.utils.windowUnload);
         }
      },
      stopTrackingUnload: function()
      {
         //console.log("stopTrackingUload");
         if (window.removeEventListener) {
            window.removeEventListener("beforeunload", com.actility.specific.utils.windowUnload);
         } else if (window.detachEvent) {
            window.detachEvent("onbeforeunload", com.actility.specific.utils.windowUnload);
         }
      },
      logout: function (additionalCallbackOnSuccess, additionalCallbackOnError)
      {
         // 1) Start the disconnection
         var userId = com.actility.global.userId;

         com.actility.ajax.ajaxCall({
            method: "POST",
            url: com.actility.global.buildUrl("/rest/users/" + userId + "/logout?sessionToken=" + com.actility.global.sessionToken),


            callbackSuccess: function(json) {
               //1) call additional callback
               additionalCallbackOnSuccess.call(this);                   

               //2) stop dashboard refreshing timer
               com.actility.specific.extjs.toolStopDashboardRefresh();

               //3) destroy the viewport
               Ext.getCmp("rootViewport").destroy();

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

               //6) recreate login panel
               com.actility.login.displaySSOLoginPopup(com.actility.global.thingparkApplicationID,location.hostname);

            },
            callbackError: function(errorMessage, response) {
               //1) call additional callback
               additionalCallbackOnError.call(this);                   

               //2) push error message
               com.actility.specific.extjs.specificWarning(Ext.getCmp('footer'), com.actility.locale.translate("WR Unable to logout, please reload the page"));
            }
         });

         
      },
      colorRssi: function(value) {
         if (value >= -100)
            return "#18e314";
         else if (value >= -110)
            return "orange";
         else
            return "red";
      },
      colorSnr: function(value) {
         if (value >= -8)
            return "#18e314";
         else if (value >= -13)
            return "orange";
         else
            return "red";
      },
      colorSf: function(value) {
         if (value <= 10)
            return "#18e314";
         else if (value <= 11)
            return "orange";
         else
            return "red";
      },
      mapColor: function(index) {
         var colors = [
            "3300FF", //deep blue
            "37FF00", //light green
            "FFFF00", //yellow
            "FF0000", //red
            "00B6FF", //light blue
            "FF7B00", //orange
            "FFFFFF", //white
            "22491E", //deep green
            "49251E", //brown
            "FF00DD" //purple

         ];
         return colors[index % colors.length];
      },
      mapExecuteOrLoadMapAPI: function(callbackOk, parameters) {
        function callbackAfterApiLoaded(param)
        {
            if (com.actility.specific.utils.mapIsLoaded()) {
              com.actility.specific.extjs.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR MAP API dependencies loaded"));
              callbackOk(parameters);
            } 
            else
              com.actility.specific.extjs.specificError(Ext.getCmp('footer'), com.actility.locale.translate("WR MAP API not loaded"));
        };
        function callbackNormal(param)
        {
            if (com.actility.specific.utils.mapIsLoaded())
              callbackOk(parameters);
            else
              com.actility.specific.extjs.specificError(Ext.getCmp('footer'), com.actility.locale.translate("WR MAP API not loaded"));         
        }
        if (typeof com.actility.global.dependenciesLoadingPhase === "undefined") {
          com.actility.global.dependenciesLoadingPhase = "done";
          com.actility.specific.extjs.specificInfo(Ext.getCmp('footer'), com.actility.locale.translate("WR Trying to load MAP API dependencies"));
          com.actility.specific.utils.mapLoadMapDependencies(callbackAfterApiLoaded,null);
        }
        else
          callbackNormal();
      },
      mapIsLoaded: function() {
         if (com.actility.global.mapService == "OSM") 
            return com.actility.specific.openstreetmaps.isLoaded();
         else if (com.actility.global.mapService == "GMAPS")
            return com.actility.specific.googlemaps.isLoaded();
         else if (com.actility.global.mapService == "BMAPS")
            return com.actility.specific.baidumaps.isLoaded();
         else return false;
      },
      mapLoadMapDependencies: function(callbackOk, parameters) {
         if (com.actility.global.mapService == "OSM") 
            com.actility.specific.openstreetmaps.loadMapDependencies(callbackOk, parameters);
         else if (com.actility.global.mapService == "GMAPS")
            com.actility.specific.googlemaps.loadMapDependencies(callbackOk, parameters);
         else if (com.actility.global.mapService == "BMAPS")
            com.actility.specific.baidumaps.loadMapDependencies(callbackOk, parameters);
      },
      mapDrawPointLte: function(where, index, stepCount, colorIndex, lat, lon, timestamp, mtc, imei, msisdn) {
         legend = '<div class="infwin">';
         if (stepCount != -1)
            legend += '<div><b>Step [' + stepCount + ']</b></div>';
         legend += '<div><b>Place [' + lat + ' ' + lon + ']</b></div>';
         legend += '<div><b>' + timestamp + '</b></div>';
         if (imei != null && imei != "null" && imei != "" )
            legend += '<div><b>IMEI :' + imei + '</b></div>';
         if (msisdn != null && msisdn != "null" && msisdn != "")
            legend += '<div><b>MSISDN :' + msisdn + '</b></div>';
         legend += '<div><b>MTC :' + mtc + '</b></div>';
         legend += '</div>';

         var color = com.actility.specific.utils.mapColor(colorIndex);
         var image = "resources/images/white_bullet.png";

         if (com.actility.global.mapService == "OSM") 
            return com.actility.specific.openstreetmaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
         else if (com.actility.global.mapService == "GMAPS")
            return com.actility.specific.googlemaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
          else if (com.actility.global.mapService == "BMAPS")
            return com.actility.specific.baidumaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
      },
      mapDrawPointLora: function(where, index, stepCount, colorIndex, lat, lon, sf, snr, rssi, esp, timestamp, lrrId, distance, deviceId) {
         legend = '<div class="infwin">';
         if (stepCount != -1)
            legend += '<div><b>Step [' + stepCount + ']</b></div>';
         legend += '<div><b>Place [' + lat + ' ' + lon + ']</b></div>';
         legend += '<div><b>' + timestamp + '</b></div>';
         if (deviceId != -1)
            legend += '<div><b>DevEUI :' + deviceId + '</b></div>';
         // legend +='<div><b>'+substr(timestamp,0,19)+'</b></div>';
         legend += '<div><b>Distance :' + distance + '</b></div>';
         legend += '<div><b>LRR Id :' + lrrId + '</b></div>';
//         legend += '<div style="background-color:' + com.actility.specific.utils.colorRssi(rssi) + ';"><b>RSSI :' + rssi + '</b></div>';
         legend += '<div><b>RSSI :' + rssi + '</b></div>';
         legend += '<div style="background-color:' + com.actility.specific.utils.colorSnr(snr) + ';"><b>SNR :' + snr + '</b></div>';
         legend += '<div style="background-color:' + com.actility.specific.utils.colorRssi(esp) + ';"><b>ESP :' + esp + '</b></div>';
         legend += '<div style="background-color:' + com.actility.specific.utils.colorSf(sf) + ';"><b>SF :' + sf + '</b></div>';
         legend += '</div>';

         var color = com.actility.specific.utils.mapColor(colorIndex);
         var image = "resources/images/white_bullet.png";

         if (com.actility.global.mapService == "OSM") 
            return com.actility.specific.openstreetmaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
         else if (com.actility.global.mapService == "GMAPS")
            return com.actility.specific.googlemaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
          else if (com.actility.global.mapService == "BMAPS")
            return com.actility.specific.baidumaps.displayBullet(where, lat, lon, legend, image, index, color, ".",true);
         return marker;
      },
      mapDrawLRR: function(where, index, colorIndex, lat, lon, lrrId) {
         var legend = '<div class="infwin">';
         if (lat != -1 && lon != -1)
            legend += '<div><b>coord [' + lat + ' ' + lon + ']</b></div>';
         legend += '<div><b>LRRid :' + lrrId + '</b></div>';
         legend += '</div>';
         var color = com.actility.specific.utils.mapColor(colorIndex);
         if (com.actility.global.mapService == "OSM") 
            com.actility.specific.openstreetmaps.displayLRR(where, lat, lon, legend, index, color);
         else if (com.actility.global.mapService == "GMAPS")
            com.actility.specific.googlemaps.displayLRR(where, lat, lon, legend, index, color);
         else if (com.actility.global.mapService == "BMAPS")
            com.actility.specific.baidumaps.displayLRR(where, lat, lon, legend, index, color);
      },
      // mappanel ready : cartoType : loc draw device position and LRR position
      mapCallbackDisplayLocalization: function(where, config) {

         //0) retrieve data
         var distance=config.distance;
         var timestampUTC=config.timestampUTC;
         var lrrLat=config.lrrLat;
         var lrrLon=config.lrrLon;
         var lrrId=config.lrrId;
         var devLat=config.devLat;
         var devLon=config.devLon;
         var deviceUid=config.deviceUid;
         var sf=config.sf;
         var snr=config.snr;
         var rssi=config.rssi;
         var esp=config.esp;

         //2) pin the LRR
         //------------------------------------------------------------
         if ((lrrLat!==0)&&(lrrLat!==null)&&(lrrLat!="null")&&(lrrLat!="")&&(lrrLat!=undefined)&&(lrrLon!==0)&&(lrrLon!==null)&&(lrrLon!="null")&&(lrrLon!="")&&(lrrLon!=undefined))
            com.actility.specific.utils.mapDrawLRR(where, 1, 0, lrrLat, lrrLon, lrrId);

         //3) pin the Device
         //------------------------------------------------------------
         //protection against erroneous data
         if ((devLat!==0)&&(devLat!==null)&&(devLat!="null")&&(devLat!="")&&(devLat!=undefined)&&(devLon!==0)&&(devLon!==null)&&(devLon!="null")&&(devLon!="")&&(devLon!=undefined))
         {
            if (com.actility.global.viewportType == "LTE") 
               com.actility.specific.utils.mapDrawPointLte(where, 2, -1, 0, devLat, devLon,com.actility.specific.extjs.toolUTCtoLocal(timestampUTC), config.mtc, config.imei, config.msiisdn);
            else
               com.actility.specific.utils.mapDrawPointLora(where, 2, -1, 0, devLat, devLon, sf, snr, rssi, esp, com.actility.specific.extjs.toolUTCtoLocal(timestampUTC), lrrId, distance, deviceUid);
         }

      },
      // mappanel ready : cartoType : history draw all last device position and LRR position
      mapCallbackDisplayLastPositions : function(where, config) {
         //0) retrieve data
         var devicePositions=config.data;
         //protection against null data
         if ((devicePositions === null) || (typeof devicePositions == 'undefined')) devicePositions = new Array();

         //2) pin alls LRR
         var LRRArray = new Array();
         var LRRCount = 0;

         //clean the array of devices
         var newDevicePositions= new Array();
         var count=0;
         for (var i = 0; i < devicePositions.length ; i++) {
            var lat=devicePositions[i]['DevLAT'];
            var lon=devicePositions[i]['DevLON'];
            if ((lat!==0)&&(lat!==null)&&(lat!="null")&&(lat!="")&&(lat!=undefined)&&(lon!==0)&&(lon!==null)&&(lon!="null")&&(lon!="")&&(lon!=undefined))
            {
               newDevicePositions[count]=devicePositions[i];
               count ++;
            }
         }

         for (var i = 0; i < devicePositions.length; i++) {
            if (devicePositions[i]['direction'] == "0") {
              var lat=devicePositions[i]['DevLAT'];
              var lon=devicePositions[i]['DevLON'];
              if ((lat!==0)&&(lat!==null)&&(lat!="null")&&(lat!="")&&(lat!=undefined)&&(lon!==0)&&(lon!==null)&&(lon!="null")&&(lon!="")&&(lon!=undefined))
              {
               var found = false;
               for (var j in LRRArray) {
                  if (LRRArray[j]['Lrrid'] == devicePositions[i]['Lrrid']) found = true;
               }
               if (found === false) {
                  LRRArray[devicePositions[i]['Lrrid']] = devicePositions[i];
                  LRRArray[devicePositions[i]['Lrrid']]['color'] = LRRCount;
                  LRRCount++;
               }
              }
            }
         }

         var index = 0;
         for (var i in LRRArray) {
            // ajout du point dans les limites de la carte
            lat = LRRArray[i]['LrrLAT'];
            lon = LRRArray[i]['LrrLON'];
            if ((lat!==0)&&(lon!==0)&&(lat!==null)&&(lon!== null)&&(lat!="null")&&(lat!="")&&(lon!="null")&&(lon!="")&&(lat!=undefined)&&(lon!=undefined)) {
               //ajout du LRR
               com.actility.specific.utils.mapDrawLRR(where, index, LRRArray[i]['color'], LRRArray[i]['LrrLAT'], LRRArray[i]['LrrLON'], LRRArray[i]['Lrrid']);
               index++;
            }
         }

         devicePositions = newDevicePositions;

         //3) pin alls DEVICE
         var DeviceArray = new Array();
         var DeviceCount = 0;

         //protection against null data
         if (devicePositions === null) devicePositions = new Array();

         for (var i = 0; i < devicePositions.length; i++) {
            if (devicePositions[i]['direction'] == "0") {
               var found = false;
               for (var j in DeviceArray) {
                  if (DeviceArray[j]['DevEUI'] == devicePositions[i]['DevEUI']) found = true;
               }
               if (found === false) {
                  DeviceArray[devicePositions[i]['DevEUI']] = devicePositions[i];
                  DeviceCount++;
               }
            }
         }
         DeviceArray.sort();
         var markers = [];
         var index = 0;
         for (var i in DeviceArray) {
            //ajout du device
           if (com.actility.global.viewportType == "LTE")
               var marker=com.actility.specific.utils.mapDrawPointLte(where, i +10, -1, LRRArray[(DeviceArray[i]['Lrrid'])]['color'],
              DeviceArray[i]['DevLAT'], DeviceArray[i]['DevLON'], com.actility.specific.extjs.toolUTCtoLocal(DeviceArray[i]['timestampUTC']),
              DeviceArray[i]['Lrrid'], DeviceArray[i]['DevEUI']/*DeviceArray[i]['lteIMEI']*/,DeviceArray[i]['lteMSISDN']);
            else
               var marker = com.actility.specific.utils.mapDrawPointLora(where, i + 10, -1, LRRArray[(DeviceArray[i]['Lrrid'])]['color'],
                  DeviceArray[i]['DevLAT'], DeviceArray[i]['DevLON'], DeviceArray[i]['SpFact'], DeviceArray[i]['LrrSNR'],
                  DeviceArray[i]['LrrRSSI'], DeviceArray[i]['LrrESP'], com.actility.specific.extjs.toolUTCtoLocal(DeviceArray[i]['timestampUTC']), DeviceArray[i]['Lrrid'], DeviceArray[i]['distance'],
                  DeviceArray[i]['DevEUI']);

            markers.push(marker);
         }
         /*
         var mcOptions = {
            gridSize: 50,
            maxZoom: 20
         };
         var markerCluster = new MarkerClusterer(where, markers, mcOptions);
         */
      },
      // mappanel ready : config.cartoType :  path draw all positions of a uniq device
      mapCallbackDisplayPath: function(where, config) {
         //console.log("mapCallbackDisplayPath");

         //0) retrieve data
         var distance=config.distance;
         var lrrLat=config.lrrLat;
         var lrrLon=config.lrrLon;
         var lrrId=config.lrrId;
         var arrayDevicesInfo=config.data;
         //protection against null data
         if (arrayDevicesInfo === null || (typeof arrayDevicesInfo == 'undefined')) arrayDevicesInfo = new Array();

         var LRRArray = new Array();
         var LRRCount = 0;


         //clean the array of devices
         //clean the array of devices
         var newDevicePositions= new Array();
         var count=0;
         for (var i = 0; i < arrayDevicesInfo.length ; i++) {
            var lat=arrayDevicesInfo[i]['DevLAT'];
            var lon=arrayDevicesInfo[i]['DevLON'];


            if ((lat!==0)&&(lat!==null)&&(lat!="null")&&(lat!="")&&(lat!=undefined)&&(lon!==0)&&(lon!==null)&&(lon!="null")&&(lon!="")&&(lon!=undefined))
            {
               newDevicePositions[count]=arrayDevicesInfo[i];
               count ++;
            }
         }
         
         //2) pin alls LRR
         for (var i = 0; i < arrayDevicesInfo.length; i++) {
            var found = false;
            for (var j in LRRArray) {
               if (LRRArray[j]['Lrrid'] == arrayDevicesInfo[i]['Lrrid']) found = true;
            }
            if (found === false) {
               LRRArray[arrayDevicesInfo[i]['Lrrid']] = arrayDevicesInfo[i];
               LRRArray[arrayDevicesInfo[i]['Lrrid']]['color'] = LRRCount;
               LRRCount++;
            }
         }
         
         var index = 0;
         for (var i in LRRArray) {
            var lat=LRRArray[i]['LrrLAT'];
            var lon=LRRArray[i]['LrrLON'];
            if ((lat!==0)&&(lat!==null)&&(lat!="null")&&(lat!="")&&(lat!=undefined)&&(lon!==0)&&(lon!==null)&&(lon!="null")&&(lon!="")&&(lon!=undefined))
            {
               com.actility.specific.utils.mapDrawLRR(where, index, LRRArray[i]['color'], lat, lon, LRRArray[i]['Lrrid']);
               index++;
            }
         }

         arrayDevicesInfo=newDevicePositions;

         //chemin du tracé du futur polyline
         var path = [];
 
         var stepCount = 1;
         for (var i = arrayDevicesInfo.length-1 ; i >=0; i--) {
            if (com.actility.global.viewportType == "LTE")
               com.actility.specific.utils.mapDrawPointLte(where, i + 10, stepCount, LRRArray[(arrayDevicesInfo[i]['Lrrid'])]['color'],
                 arrayDevicesInfo[i]['DevLAT'], arrayDevicesInfo[i]['DevLON'], 
                 com.actility.specific.extjs.toolUTCtoLocal(arrayDevicesInfo[i]['timestampUTC']),
                 arrayDevicesInfo[i]['Lrrid'], arrayDevicesInfo[i]['DevEUI']/*arrayDevicesInfo[i]['lteIMEI']*/,arrayDevicesInfo[i]['lteMSISDN']);
            else
               com.actility.specific.utils.mapDrawPointLora(where, i + 10, stepCount, LRRArray[(arrayDevicesInfo[i]['Lrrid'])]['color'],
                  arrayDevicesInfo[i]['DevLAT'], arrayDevicesInfo[i]['DevLON'], arrayDevicesInfo[i]['SpFact'], arrayDevicesInfo[i]['LrrSNR'],
                  arrayDevicesInfo[i]['LrrRSSI'], arrayDevicesInfo[i]['LrrESP'],
                  com.actility.specific.extjs.toolUTCtoLocal(arrayDevicesInfo[i]['timestampUTC']), 
                  arrayDevicesInfo[i]['Lrrid'], arrayDevicesInfo[i]['distance'], -1);

            path.push({lat: arrayDevicesInfo[i]['DevLAT'], lon: arrayDevicesInfo[i]['DevLON']});
            stepCount++;
         }

         if (com.actility.global.mapService == "OSM") 
            com.actility.specific.openstreetmaps.displayPolyline(where,path,"#FF0000");
         else if (com.actility.global.mapService == "GMAPS") 
            com.actility.specific.googlemaps.displayPolyline(where,path,"#FF0000");
         else if (com.actility.global.mapService == "BMAPS")
            com.actility.specific.baidumaps.displayPolyline(where,path,"#FF0000");
      },
      buildMapPanel: function(config) {
         if (com.actility.global.mapService == "GMAPS")
            com.actility.specific.googlemaps.buildMapPanel(config);
         else if (com.actility.global.mapService == "OSM")
            com.actility.specific.openstreetmaps.buildMapPanel(config);
          else if (com.actility.global.mapService == "BMAPS")
            com.actility.specific.baidumaps.buildMapPanel(config);
      },
      buildDisclaimer : function(config) {
         var HiddenIfEmptyOrUndef = function($value) 
         {
            if (typeof $value == 'undefined')
               return true;
            if ($value.length == 0)
               return true;
            return false;
         };
         var win = new Ext.Window({
            width: 400,
            height: 500,
            modal: true,
            bodyStyle: "background-color: #c5ddf5",
            border: false,
            closable: true,
            overflowY: 'auto',
            layout: 'anchor',
            margin: 0,
         });
         win.add(
         {
            xtype: 'panel',
            width: '100%',
            height: '100%',
            bodyStyle: "background-color: #c5ddf5",
            overflowY: 'auto',
            layout: {
               type: 'vbox',
               align: 'center',
               pack: 'center'
            },
            items: [
            {
               xtype: 'container',
               width: '90%',
               left: 20,
               cls: 'disclaimer',
               itemId: 'msg',
               html: config.disclaimerMessage,
               hidden: HiddenIfEmptyOrUndef(config.disclaimerMessage),
            }, {
               margin: '20 0 15 0',
               xtype: 'label',
               itemId: 'userName',
               text: config.who,
               hidden: HiddenIfEmptyOrUndef(config.who),
               cls: 'disclaimerUserName'
            }, {
               xtype: 'displayfield',
               fieldLabel: '${LBL Last successful login}',
               labelWidth: 150,
               value: config.previousConnection,
               hidden: HiddenIfEmptyOrUndef(config.previousConnection),
            }, {
               xtype: 'displayfield',
               fieldLabel: '${LBL Last unsuccessful login}',
               labelWidth: 150,
               value: config.thingparkLastUnsuccessfulLogin,
               hidden: HiddenIfEmptyOrUndef(config.thingparkLastUnsuccessfulLogin),
            }, {
               xtype: 'displayfield',
               fieldLabel: '${LBL Number of failed logins since last successful login}',
               value: config.thingparkPreviousBadConsecutivePwd,
               labelWidth: 300,
               labelStyle: "width:300px!important",
               hidden: HiddenIfEmptyOrUndef(config.thingparkPreviousBadConsecutivePwd),
            }, {
               margin: '15 0 15 0',
               xtype: 'button',
               text: '${BTN Accept}',
               handler: function(btn) {
                  win.destroy();
                  config.onButtonPress.call(this,config.type);
               }
             }]
            });
         win.show();
         return win;
      },
      buildLFDTable : function(lfd)
      {
         if (lfd != null && lfd.length > 0) {
            var output = "<br><br><table class='chainList'>";
            output+="<thead><tr><td>"+com.actility.locale.translate('LBL Delivery Failed Cause')+"</td><td>"+com.actility.locale.translate('LBL Number of occurrences')+"</td></tr></thead>";
            output+="<tbody>";
            var lfdObj = eval(lfd);
            for(var i= 0; i < lfdObj.length; i++) {
              output+="<tr><td>"+com.actility.specific.utils.decodeTranslateDeliveryFailedCauseLfd(lfdObj[i].dfc)+"</td><td>"+lfdObj[i].cnt+"</td></tr>";
            }
            output+="</tbody></table>";
            return output;
         } else {
            return "";
         }
      },
      //******************************************************
      // SAME CODE ALSO IN Utils.php.class
      //******************************************************
      decodeTranslateDeliveryStatus : function(status)
      {
         var message;
         if (status.length == 0)     message=com.actility.locale.translate('TXT DS Unknown');
         else if (status == "null")  message=com.actility.locale.translate('TXT DS Unknown');
         else if (status == "0")     message=com.actility.locale.translate('TXT DS Failed');
         else if (status == "1")     message=com.actility.locale.translate('TXT DS Sent');
         else                  {
                                  message=com.actility.locale.translate('TXT DS Unknown status');
                                  message+='('+status+')';
                               }
         return message;
      },
      decodeTranslateDeliveryFailedCauseLfd : function(cause1)
      {
         var message;
         if (cause1 == null) return "";
         if (cause1 == "null")  return "";
         if (cause1.length == 0) return "";
         if (cause1 == "00") return message=com.actility.locale.translate('TXT DC1 Success');
         if (cause1.length != 2) return "";
         var causeClass=cause1.substr(0,1);
         if (causeClass == "A")      message=com.actility.locale.translate('TXT LFD: Transmission slot busy');
         else if (causeClass == "B") message=com.actility.locale.translate('TXT LFD: Too late');
         else if (causeClass == "D") message=com.actility.locale.translate('TXT LFD: DC constraint');
         else if (causeClass == "E") message=com.actility.locale.translate('TXT LFD: Frame expired');
         else if (causeClass == "F") message=com.actility.locale.translate('TXT LFD: Multicast failure');
         else                   message=com.actility.locale.translate('TXT DC1 Unknown cause');
         message+='('+cause1+')';
         return message;
      },
      decodeTranslateDeliveryFailedCause1 : function(cause1)
      {
         var message;
         if (cause1 == null) return "";
         if (cause1 == "null")  return "";
         if (cause1.length == 0) return "";
         if (cause1 == "00") return message=com.actility.locale.translate('TXT DC1 Success');
         if (cause1.length != 2) return "";
         var causeClass=cause1.substr(0,1);
         if (causeClass == "A")      message=com.actility.locale.translate('TXT DC1 Class A: Transmission slot busy on RX1');
         else if (causeClass == "B") message=com.actility.locale.translate('TXT DC1 Class A: Too late for RX1');
         else if (causeClass == "C") message=com.actility.locale.translate('TXT DC1 Class A: LRC selected RX2');
         else if (causeClass == "D") message=com.actility.locale.translate('TXT DC1 Class A: DC constraint on RX1');
         else if (causeClass == "E") message=com.actility.locale.translate('TXT DC1 Class A: Frame expired before transmission');
         else                   message=com.actility.locale.translate('TXT DC1 Unknown cause');
         message+='('+cause1+')';
         return message;
      },
      decodeTranslateDeliveryFailedCause2 : function(cause2)
      {
         var message;
         if (cause2 == null) return "";
         if (cause2 == "null")  return "";
         if (cause2.length == 0) return "";
         if (cause2 == "00") return message=com.actility.locale.translate('TXT DC2 Success');
         if (cause2.length != 2) return "";
         var causeClass=cause2.substr(0,1);
         if (causeClass == "A")      message=com.actility.locale.translate('TXT DC2 Class A: Transmission slot busy on RX2');
         else if (causeClass == "B") message=com.actility.locale.translate('TXT DC2 Class A: Too late for RX2');
         else if (causeClass == "D") message=com.actility.locale.translate('TXT DC2 Class A: DC constraint on RX2');
         else if (causeClass == "E") message=com.actility.locale.translate('TXT DC2 Class C: Frame expired before transmission');
         else if (causeClass == "F") message=com.actility.locale.translate('TXT DC2 Class C: Multicast failure');
         else                   message=com.actility.locale.translate('TXT DC2 Unknown cause');
         message+='('+cause2+')';
         return message;
      },
      decodeTranslateDeliveryFailedCause3 : function(cause3)
      {
         var message;
         if (cause3 == null) return "";
         if (cause3 == "null")  return "";
         if (cause3.length == 0) return "";
         if (cause3 == "00") return message=com.actility.locale.translate('TXT DC3 Success');
         if (cause3.length != 2) return "";
         var causeClass=cause3.substr(0,1);
         if (causeClass == "A")      message=com.actility.locale.translate('TXT DC3 Class B: Transmission slot busy on ping slot');
         else if (causeClass == "B") message=com.actility.locale.translate('TXT DC3 Class B: Too late for ping slot');
         else if (causeClass == "D") message=com.actility.locale.translate('TXT DC3 Class B: DC constraint on ping slot');
         else if (causeClass == "F") message=com.actility.locale.translate('TXT DC3 Class B: Multicast failure');
         else                   message=com.actility.locale.translate('TXT DC3 Unknown cause');
         message+='('+cause3+')';
         return message;
      }
   }
});
