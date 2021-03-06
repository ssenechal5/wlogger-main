Ext.define('com.actility.specific.openstreetmaps', {
   statics: {
      _translatePosition: function (lat,lon) {
        //no need to correct anything
        return  {lat:lat,lon:lon};
      },
      _getPosition: function (lat,lon) {
         translatedPos=this._translatePosition(lat,lon);
         return new L.LatLng(translatedPos.lat,translatedPos.lon);
      },
      _extendMap: function (where, pos) {
         if (typeof where.bounds == 'undefined') {      
           //-----------------------------------------------------------
           //First call : draw an invisible circle around the first point with distance 50m
           var circleOptions = {
                stroke: false,
                strokeOpacity: 0,
                weight: 0,
                color: '#FF0000',
                opacity: 0,
                fill: false,
                radius: 50
           };
           // Add the circle for this map
           lrrCircle = new L.circle(pos,circleOptions);
           lrrCircle.addTo(where);
           //save bounds in the oject itself
           where.bounds=lrrCircle.getBounds();
           // reduce size of the map to the bound of the circle
           where.fitBounds(lrrCircle.getBounds());
         }
         else {
            //just extend
            where.bounds.extend(pos);
            where.fitBounds(where.bounds);
         }
      },
     loadMapDependencies: function(callbackAfterApiLoaded, parameters)
     {
         var dependencies = [
          "resources/js/leaflet/leaflet-src.js",
          "resources/js/leaflet.markercluster/leaflet.markercluster-src.js",
          "resources/css/leaflet/leaflet.css",
          "resources/css/leaflet/addOn.css",
          "resources/css/leaflet.markercluster/MarkerCluster.css",
          "resources/css/leaflet.markercluster/MarkerCluster.Default.css",
          "resources/js/ux/LeafletPanel.js",
         ];
         var scriptLoader = new ScriptLoader(dependencies, callbackAfterApiLoaded, parameters);
         scriptLoader.loadFiles();
      },
      isLoaded: function() {
         return (typeof L == "object");
      },
      displayBullet: function(where, lat, lon, content, image, index, color, letter, cluster) {
         if (cluster==true) {
            if (typeof where.clusterMarker == "undefined") {
               //save clusterMarker in the oject itself
               where.clusterMarker = L.markerClusterGroup({
                   spiderfyOnMaxZoom: false,     //spiderfy when clicking on cluster
                   disableClusteringAtZoom: 12, //we do not cluster after zoom 12
                   showCoverageOnHover: false,
                   zoomToBoundsOnClick: true,
                   animate: true,
                   chunkedLoading: true, 
                   chunkProgress: function(){},
                });
            }
         }
         var pos=this._getPosition(lat, lon);
         var markerIcon = L.divIcon({
            className: 'leaflet-div-icon',
            iconSize: [20,20],
            iconAnchor: [10,10],
            popupAnchor: [0,-10]
         });
         var marker=L.marker(pos, {icon: markerIcon});      
         marker.bindPopup(content);
         marker.on('add', function() {this.valueOf()._icon.style.backgroundColor = color;});
         marker.on('mouseover', function (e) {this.openPopup();});
         if (cluster == true) {
            where.clusterMarker.addLayer(marker);
            where.addLayer(where.clusterMarker);
         }
         else {
            marker.addTo(where);
         }
         this._extendMap(where,pos);
      },
      displayPolyline: function(where, path, color)  {
         var polylinePoints=[];
         for (var i=0; i < path.length; i++) {
               polylinePoints.push(this._getPosition(path[i].lat,path[i].lon));
         }
         var polyline = new L.Polyline(polylinePoints,
              { //chemin du trac??
               weight: 2,
               opacity: 0.9,
               color: color
         });
         where.addLayer(polyline);   
      },
      displayLRR: function(where, lat, lon, content, index, color) {
         var lrrIcon = L.icon({
            iconUrl: com.actility.global.buildUrl('/gui/resources/images/lrr_' + color + '.png'),
            iconSize: [62,90],
            iconAnchor: [31,45],
            popupAnchor: [0,-40],
         });
         var pos=this._getPosition(lat, lon);
         var marker=L.marker(pos,{icon: lrrIcon});
         marker.addTo(where);
         marker.bindPopup(content);
         marker.on('mouseover', function (e) {this.openPopup();});
         this._extendMap(where,pos);
      },
      buildMapPanel: function(config) {
//         L.Icon.Default.imagePath = "/wlogger/resources/images/leaflet";
         L.Icon.Default.imagePath = "resources/images/leaflet";
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
                     xtype: 'leafletpanel',
                     width: '100%',
                     height: '100%',
                     listeners: {
                        mapready: handleMapReady,
                        resize: handleResizePanel,
                     }
                  }, //items
                  
            }); //mappanel
         function handleMapReady(ux, map) {
               api = map;
               config.onMapReady.call(this, map, config);
         };
         function handleResizePanel(who,width,height) {
            //do not manage it the first time !!
            if (typeof who.firstResize == "undefined") {
               who.firstResize=true;
            }
            else {
               if (api != null)  api.invalidateSize();
            }
         };
         mappanel.show();    
      }   
 }});
