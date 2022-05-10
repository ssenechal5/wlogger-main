Ext.define('com.actility.global',{
    statics : {
        ajaxM2MOperationTimeout : 30000,  //(msec) : 30 seconds
        ajaxRequestTimeout : 60000,       //(msec) : 30 seconds
        dashboardTimer: null,             //dashboard refresh timer
        userId: "",                       //userId
        customerIdList: null,             //customer ID list
        thingparkUrl : "",                //thingpark url
        thingparkApplicationID : null,    //wlogger application ID
        sessionToken : null,              //wlogger session token
        viewportType : "LORA",            //LTE or LORA
        viewport : null,                  //extjs viewport panel object
        guiMode : "module",               //standalone or module
        lteModeActif : false,             //do we have the GEAR menu
        localizationMode : "LOCALE",      // LOCALE OR SMP or DISTANT
        adminLogin : false,
        operatorDomain : null,            //retrieve from boot phase
        lang : null,                      //retrieve from boot phase, and overrided by user settings after logon
		
        googleBrowserApiKey : null,       //google api key
        mapServiceModuleAccess : "gmaps", //map service by module  # Map : gmaps, osm ,none, bmaps
        mapServiceDirectAccess : "gmaps", //map service            # Map : gmaps, osm ,none, bmaps
		
        mapService  : "GMAPS",             // OSM   for open street maps
                                           // GMAPS for google maps
                                           // BMAPS for baidu maps
                                           // NONE
        bmapsApiKey : null,                //bmaps api key
        bmapsUrl : "https://api.map.baidu.com/api?callback=com.actility.specific.baidumaps.externalApiLoaded&v=2.0&ak=",
        osmUrl   : "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
        gmapsEcjEncryptMode : true,         //does google map need ecj encryption for china zone
        gmapsUrl : "https://maps.googleapis.com/maps/api/js?callback=com.actility.specific.googlemaps.externalApiLoaded&key=",  // + <your api key> = googleBrowserApiKey
		
        pageSize : 100,                     //retrieve from boot phase
        maxPages : 100,                     //retrieve from boot phase

        notNetworkPartnerAccess : true,

        CONFIG : null,

        adminAppliName : "SuperWLAdmin",

        modeDEV : false,

        maxAutomaticDecodedPackets : 500,   //retrieve from boot phase

        keycloakHost : "",

        AUTO_DECODER : "auto",

        buildUrl : function(path) {
           //console.log("buildUrl "+ path);
           if (path.substring(0,1) == "/")
              var ret='/thingpark/wlogger'+path;
           else
              var ret='/thingpark/wlogger/'+path;
           //console.log("buildUrl ret="+ ret);
           return ret;
        },

        buildKeyCloakUrl : function(path) {
           if (com.actility.global.modeDEV)
             return com.actility.global.keycloakHost+path;
           else
             return path;
        }
   }
    
});
