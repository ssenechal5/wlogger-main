Ext.define('com.actility.specific.googlemaps', {
   statics: {
      _translatePosition: function (lat,lon) {
         //need to correct position in china zone to gcj coordinates
         if (GPS.isInChina(lat,lon) && com.actility.global.gmapsEcjEncryptMode) 
             return translatedPos=GPS.gcj_encrypt(lat,lon);
         else
             return {lat:lat,lon:lon}
      },
      _getPosition: function (lat,lon) {
        translatedPos=this._translatePosition(lat,lon);
        return new google.maps.LatLng(translatedPos.lat,translatedPos.lon);
      },
      _getInfoWindow: function(where) {
       //google maps : only one infowindow
       if (typeof where.infowindow == "undefined")
          //store infowindow reference in the context
    	    where.infowindow = new google.maps.InfoWindow();
       return where.infowindow;
      },
      _extendMap: function (where, pos) {
         if (typeof where.bounds == 'undefined') {      
           //-----------------------------------------------------------
           //Draw an invisible circle around the first point with distance 50m
           var circleOptions = {
    		       strokeColor: '#FF0000',
    		       strokeOpacity: 0,
    		       strokeWeight: 2,
    		       fillColor: '#FF0000',
    		       fillOpacity: 0,
    		       map: where,
    		       center: pos,
    		       radius: 50
    		   };
           // Add the circle for this map
	    	   lrrCircle = new google.maps.Circle(circleOptions);
	    	   //save bounds in the oject itself         
           where.bounds=lrrCircle.getBounds();
           // reduce size of the map to the bound of the circle
           where.fitBounds(lrrCircle.getBounds());
         } 
         else {
	         where.bounds.extend(pos);
	         where.fitBounds(where.bounds);
	       }
      },
      externalApiLoaded : function()
      {
         //this is the external callback when google maps is loaded, invoked by google
         var dependencies = [
           "resources/js/gmaps.markerclusterer/markerclusterer.js", 
           "resources/js/ux/GMapPanel.js",
           "resources/js/gps.js"];
         var scriptLoader = new ScriptLoader(dependencies, 
              com.actility.specific.googlemaps.callbackAfterApiLoaded, 
              com.actility.specific.googlemaps.callbackAfterApiLoadedParameters);
         scriptLoader.loadFiles();
      },
      loadMapDependencies: function(callbackAfterApiLoaded, parameters)
      {
         //ASYNCHROUNOUS loading of google maps api, with google callback
         //we first need to load the google maps api,
         //the externalApiLoaded callback will be raised by google as soon as the api is loaded
         //externalApiLoaded is in charge of loading dependencies and calling back the callbackAfterApiLoaded
         var dependencies = [com.actility.global.gmapsUrl];
         //save callback in the context
         com.actility.specific.googlemaps.callbackAfterApiLoaded=callbackAfterApiLoaded;
         com.actility.specific.googlemaps.callbackAfterApiLoadedParameters=parameters;
         //load google api
         var scriptLoader = new ScriptLoader(dependencies, function(){}, null);
         scriptLoader.loadFiles();
      },
      isLoaded: function() {
         return (typeof google == "object");
      },
      displayPolyline: function(where, path, color)
      {
    	  var polyline=[];
    	  for (var i=0; i < path.length; i++) {
   			  polyline.push(this._getPosition(path[i].lat,path[i].lon));
         }
         var tracePath = new google.maps.Polyline({
            path: polyline, //chemin du tracé
            strokeColor: color, //couleur du tracé
            strokeOpacity: 1.0, //opacité du tracé
            strokeWeight: 2 //grosseur du tracé
         });
         tracePath.setMap(where);   
      },
      displayBullet: function(where, lat, lon, content, image, index, color, letter, cluster) {    
    	   if (cluster==true) {
            if (typeof where.clusterMarker == "undefined") {
               //save clusterMarker in the oject itself
               var markers=[];
//             var options = { imagePath: '/wlogger/resources/images/gmaps.markerclusterer/m' };
               var options = { 
							   imagePath: 'resources/images/gmaps.markerclusterer/m',
                               maxZoom: 12 //declustering when reaching this level
               };				 
               where.clusterMarker = new MarkerClusterer(where,markers,options);
            }
         }
         var pinImage = new google.maps.MarkerImage("https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + letter + "|" + color,
            null,
            null,
            null,
            new google.maps.Size(15, 15));
         var pos=this._getPosition(lat, lon);
         var marker = new google.maps.Marker({
            position: pos,
            map: where,
            icon: pinImage
         });
         google.maps.event.addListener(marker, 'mouseover', (function(marker, i) {
           return function() {
             var infowindow=com.actility.specific.googlemaps._getInfoWindow(where);
             infowindow.setContent(content);
             infowindow.open(where, marker);
            }
         })(marker, index));
         if (cluster == true) {
            where.clusterMarker.addMarker(marker);
         }
 		     com.actility.specific.googlemaps._extendMap(where,pos);
         return marker;
       },
	     displayLRR: function(where, lat, lon, content, index, color) {
         var pos=new google.maps.LatLng(lat, lon);
         var marker = new google.maps.Marker({
            position: this._getPosition(lat, lon),
            map: where,
            icon: com.actility.global.buildUrl('/gui/resources/images/lrr_' + color + '.png')
         });
         google.maps.event.addListener(marker, 'mouseover', (function(marker, i) {
            return function() {
              var infowindow=com.actility.specific.googlemaps._getInfoWindow(where);
               infowindow.setContent(content);
               infowindow.open(where, marker);
            }
         })(marker, index));
         com.actility.specific.googlemaps._extendMap(where,pos);
      },
      buildMapPanel: function(config) {
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
               xtype: 'gmappanel',
               gmapType: 'map',
               zoomLevel: 14,
               zoom: 13,
               center: this._getPosition(0, 0),
               mapOptions: {
                  panControl: true,
                  zoonControl: true,
                  overviewMapControl: true,
                  streetViewControl: true,
                  scaleControl: true,
                  navigationControl: true,
                  navigationControlOptions: {
                     style: google.maps.NavigationControlStyle.SMALL,
                     position: google.maps.ControlPosition.TOP_RIGHT
                  },
                  mapTypeControl: true,
                  mapTypeControlOptions: {
                     style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
                  },
                  mapTypeId: google.maps.MapTypeId.ROADMAP
               },
               listeners: {
                  mapready: handleMapReady
               }
            } //items
         }); //mappanel
         function handleMapReady(ux, gmap) {
            api = gmap;
            config.onMapReady.call(this, gmap, config);
         }
         mappanel.show();
      }
 }});
