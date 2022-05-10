Ext.define('WirelessLogger.store.userStore', {
    extend: 'Ext.data.Store',

    requires: [
        'WirelessLogger.model.userModel',
        'Ext.data.proxy.Memory',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'WirelessLogger.model.userModel',
            storeId: 'userStore',
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});