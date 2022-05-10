Ext.define('Ext.ux.LeafletMapView', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.leafletpanel',
	config:{
		map: null
	},
	redraw: function(){
	},
	afterRender: function(t, eOpts){
		var me = this;
		var loaded = false;
		me.addEvents('mapready');
		me.callParent(arguments);
		var leafletRef = window.L;
		if (leafletRef == null){
			me.update('No leaflet library loaded');
		} 
		else {
			me.map = L.map(this.getId());
			me.map.setView([0, 0], 13);
			me.setMap(this.map);
			var tileLayer=L.tileLayer(com.actility.global.osmUrl,
			{
				minZoom: 1,
				maxZoom: 19,
	            maxNativeZoom: 18,
			});
			tileLayer.addTo(me.map);
			tileLayer.on("load",function() { 
				if (! this.loaded) {
				   me.fireEvent('mapready', me, me.map);
				   this.loaded=true;
				}
		    });
			//all events
			/*
		    me.map.on('movestart',       function (e) { console.log('< movestart'); });
			me.map.on('moveend',         function (e) { console.log('> moveend'); });
			me.map.on('dragstart',       function (e) { console.log('    [ dragstart'); });
			me.map.on('dragend',         function (e) { console.log('    ] dragend'); });
			me.map.on('zoomstart',       function (e) { console.log('    ( zoomstart'); });
			me.map.on('zoomend',         function (e) { console.log('    ) zoomend'); });
			me.map.on('viewreset',       function (e) { console.log('      viewreset'); });
			me.map.on('autopanstart',    function (e) { console.log('      autopanstart'); });
			*/
		} //else
	}
});