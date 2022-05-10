Ext.define('WirelessLogger.util.Config', {
    singleton: true,

    getKeyConfig: function () {
        if (Ext.isEmpty(com.actility.global.CONFIG)) {
			return null;
        }
        var current = com.actility.global.CONFIG;
        var key = key = arguments[0];
//        console.log('key:'+ key);

        if (current.length > 0) {
            for (var i=0; i < current.length; i++) {
              if  (current[i].name == key) {
                return current[i].value;
              }
            }
        }
        return null;
    }
});