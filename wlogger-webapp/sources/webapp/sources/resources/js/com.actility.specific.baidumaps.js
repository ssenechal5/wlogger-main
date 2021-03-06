Ext.define('com.actility.specific.baidumaps', {
   statics: {
      _translatePosition: function (lat,lon) {
         //need to correct position in china zone to baidu coordinates
         if (GPS.isInChina(lat,lon) && com.actility.global.gmapsEcjEncryptMode) {
//             console.log('_translatePosition  lat :'+lat + '  lon :'+lon);
             var translatedInGcjPos = GPS.gcj_encrypt(lat,lon);
//             console.log('translatedInGcjPos  lat :'+translatedInGcjPos.lat + '  lon :'+translatedInGcjPos.lon);
             var translatedInBaidu = GPS.bd_encrypt(translatedInGcjPos.lat,translatedInGcjPos.lon);
//             console.log('translatedInBaidu  lat :'+translatedInBaidu.lat + '  lon :'+translatedInBaidu.lon);
             return translatedInBaidu;
         }
         else
             return {lat:lat,lon:lon}
      },
      _getPosition: function (lon,lat) {
         var translatedPos=this._translatePosition(lat,lon);
         return new BMap.Point(translatedPos.lon, translatedPos.lat);
      },
      _extendMap: function (where, pos) {
        if (typeof where.arrayOfPoints == "undefined") {
          where.arrayOfPoints=[];
        }
        where.arrayOfPoints.push(pos);
        var map_center = where.getViewport(where.arrayOfPoints);
        where.centerAndZoom( map_center.center, map_center.zoom);
        //where.panTo(map_center.center);
      },
      externalApiLoaded : function()
      {
         //this is the external callback when baidu maps is loaded, invoked by baidu
         var dependencies = [
          "resources/js/ux/BMapPanel.js",
          "resources/js/bmaps.markercluster/MarkerClusterer.js",
          "resources/js/bmaps.texticonoverlay/TextIconOverlay.js",
          "resources/js/gps.js"];
         var scriptLoader = new ScriptLoader(dependencies, 
            com.actility.specific.baidumaps.callbackAfterApiLoaded, 
            com.actility.specific.baidumaps.callbackAfterApiLoadedParameters);
         scriptLoader.loadFiles();
      },
      loadMapDependencies: function(callbackAfterApiLoaded, parameters)
      {
         //ASYNCHROUNOUS loading of baidu maps api, with baidu callback
         //we first need to load the baidu maps api,
         //the externalApiLoaded callback will be raised by baidu as soon as the api is loaded
         //externalApiLoaded is in charge of loading dependencies and calling back the callbackAfterApiLoaded
         var dependencies = [com.actility.global.bmapsUrl];
         com.actility.specific.baidumaps.callbackAfterApiLoaded=callbackAfterApiLoaded;
         com.actility.specific.baidumaps.callbackAfterApiLoadedParameters=parameters;
         var scriptLoader = new ScriptLoader(dependencies, function(){}, null);
         scriptLoader.loadFiles();
      },
      isLoaded: function() {
        return (typeof BMap == "object");
      },
      displayBullet: function(where, lat, lon, content, image, index, color, letter, cluster) {
         if (cluster==true) {
            if (typeof where.clusterMarker == "undefined") {
               //save clusterMarker in the oject itself
               where.clusterMarker = new BMapLib.MarkerClusterer(where);
            }
         }
         var bulletIcon =  new BMap.Icon(
            com.actility.global.buildUrl('/gui/resources/images/marker_' + color + '.png'),
            new BMap.Size(32,37),
            {
              anchor: new BMap.Size(16, 37),
              infoWindowAnchor: new BMap.Size(16, 0)
            });
         var pos = this._getPosition(lon, lat);
         var marker = new BMap.Marker(pos, {icon: bulletIcon});
         //where.addOverlay(marker);  
         var infowindow = new BMap.InfoWindow(content);
         marker.addEventListener('mouseover', function (e) { this.openInfoWindow(infowindow);});
         if (cluster == true) {
            where.clusterMarker.addMarker(marker);
         }
         else {
            where.addOverlay(marker); 
         }
         com.actility.specific.baidumaps._extendMap(where,pos);
      },
      displayPolyline: function(where, path, color)  {
        var polyline=[];
        for (var i=0; i < path.length; i++) {
          polyline.push(this._getPosition(path[i].lon,path[i].lat));
        }
        var tracePath = new BMap.Polyline(polyline, {
              strokeStyle: "solid",
              strokeColor: color, //couleur du trac??
              strokeOpacity: 1.0, //opacit?? du trac??
              strokeWeight: 2 //grosseur du trac??
        });
        where.addOverlay(tracePath);   
      },
      displayLRR: function(where, lat, lon, content, index, color) {
         var lrrIcon =  new BMap.Icon(
            com.actility.global.buildUrl('/gui/resources/images/lrr_' + color + '.png'),
            new BMap.Size(62,90),
            {
              anchor: new BMap.Size(31, 45),
              infoWindowAnchor: new BMap.Size(31, 0)
            });
         var pos = this._getPosition(lon, lat);
         var marker = new BMap.Marker(pos, {icon: lrrIcon});
         where.addOverlay(marker);  
         var infowindow = new BMap.InfoWindow(content);
         marker.addEventListener('mouseover', function (e) { this.openInfoWindow(infowindow);});
         com.actility.specific.baidumaps._extendMap(where,pos);
      },
      buildMapPanel: function(config) {
         var api=null;
         var mappanel = Ext.create('Ext.window.Window', {
                  title: config.title,
                  layout: 'fit',
                  width: 450,
                  height: 450,
                  x: 10,
                  y: 10,
                  border: true,
                  //centered: true,
                  maximizable: true,
                  items: {
                     xtype: 'bmappanel',                    
                     width: '100%',
                     height: '100%',
                     bmapType: 'map',
                     border: true,
                     zoomLevel: 15,
                     lng: 0,
                     lat: 0,
                     mapConfigs: ['enableScrollWheelZoom','enableAutoResize'],
                     mapControls: [{
                          controlName: 'ScaleControl',
                          anchor: 'BMAP_ANCHOR_TOP_LEFT'
                     }, {
                          controlName: 'MapTypeControl',
                          mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP] 
                     }, {
                          controlName: 'NavigationControl',
                          anchor: 'BMAP_ANCHOR_TOP_RIGHT',
                          type: 'BMAP_NAVIGATION_CONTROL_SMALL'
                     }],
                     listeners: {
                          mapready: handleMapReady,
                          resize: handleResizePanel,
                     }
                  }, //items
                  
            }); //mappanel
         function handleMapReady(who, map) {
            api = map;
            config.onMapReady.call(this, map, config);
         };
         function handleResizePanel(who,width,height) {
            //no need to handle this callback
         };
         mappanel.show();    
      }   
 }});
