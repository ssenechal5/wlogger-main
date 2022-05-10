//http://www.mamicode.com/info-detail-411460.html
//http://www.yjs001.cn/web/extjs/67065759806799969950.html
Ext.define('Ext.ux.BaiduMapView', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.bmappanel',
	requires: ['Ext.window.MessageBox'],
	 initComponent: function () {
        this.callParent();
     },
     afterRender: function () {
         this.loaded = false;
         this.callParent();
         this.addEvents('mapready');   
         if (this.bmapType === 'map') {
             // console.log("building map");
             this.bmap = new BMap.Map(this.getId());
             this.bmap.centerAndZoom(new BMap.Point(this.lng,this.lat), this.zoomLevel);
             this.bmap.addEventListener("tilesloaded", function() {this.onLoaded(this);}.bind(this));
         }
     },
     onLoaded: function (me) {
        if (! me.loaded)  {
            me.addMapConfigs(); 
            me.addMapControls(); 
            me.addMapMarkers(me.markers);  
            me.fireEvent('mapready', me, me.bmap);
            me.loaded=true;
        }
           
     },
     getMap: function () {
         return this.bmap;
     },
     addMapMarkers: function (markerArray) {
         if (Ext.isArray(markerArray)) {
             for (var i = 0; i < markerArray.length; i++) {
                 this.addMapMarker(markerArray[i]);
             }
         }
     },
     addMapMarker: function (markerParam) {
         var point = new BMap.Point(markerParam.x, markerParam.y);
         var markerBase = new BMap.Marker(point); 
 
         if (markerParam.isDragging == true)
             markerBase.enableDragging();
         else
             markerBase.disableDragging();
 
         this.getMap().addOverlay(markerBase); 
     },
     addMapControls: function () {
         if (Ext.isArray(this.mapControls)) {
             for (var i = 0; i < this.mapControls.length; i++) {
                 this.addMapControl(this.mapControls[i]);
             }
         }
     },
     addMapControl: function (controlParam) {
          var controlParamName=controlParam.controlName;
          delete controlParam['controlName'];
          var controlBase = new BMap[controlParamName](controlParam);
          this.getMap().addControl(controlBase);
     },
     addMapConfigs: function () {
         if (Ext.isArray(this.mapConfigs)) {
             for (var i = 0; i < this.mapConfigs.length; i++) {
                 this.addMapConfig(this.mapConfigs[i]);
             }
         } else if (typeof this.mapConfigs === 'string') {
             this.addMapConfig(this.mapConfigs);
         }
 
     },
     addMapConfig: function (configParam) {
         this.getMap()[configParam]();
     }
 });
