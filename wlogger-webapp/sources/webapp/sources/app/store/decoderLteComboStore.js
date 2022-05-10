Ext.define('WirelessLogger.store.decoderLteComboStore', {
    extend: 'Ext.data.Store',

    requires: [
        'WirelessLogger.model.comboModel',
        'Ext.data.proxy.Memory',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
		
        var defaultLanguage = 'en-US';

        var lang = cfg.lang || Ext.Object.fromQueryString(location.search.substring(1)).lang || navigator.language || window.navigator.languages[0] || window.navigator.languages[2] || defaultLanguage;
        lang = lang.replace("_", "-"); // use ISO instead

        cfg.lang = lang; // update config

        var type = Ext.Object.fromQueryString(location.search.substring(1)).type;
        var operatorDomain = window.location.host;
        if (type == "module") {
          operatorDomain = Ext.Object.fromQueryString(location.search.substring(1)).operator;
        } else {
          operatorDomain = window.location.host;
          operatorDomain = operatorDomain.replace(window.location.port,"");
          operatorDomain = operatorDomain.replace(":","");              
        }

        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'WirelessLogger.model.comboModel',
            storeId: 'decoderLteComboStore',
            proxy: {
                type: 'ajax',
//                url: '/thingpark/wlogger/rest/stores/decoder?type=1',
                url: '/thingpark/wlogger/rest/stores/decoder?domain='+operatorDomain+"&type="+type+"&locale="+lang+"&decType=1",
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});
