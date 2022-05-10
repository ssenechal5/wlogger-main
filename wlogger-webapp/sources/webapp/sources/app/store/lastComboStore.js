Ext.define('WirelessLogger.store.lastComboStore', {
    extend: 'Ext.data.Store',

    requires: [
        'WirelessLogger.model.comboModel',
        'Ext.data.proxy.Memory',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'WirelessLogger.model.comboModel',
            storeId: 'lastComboStore',
            proxy: {
                type: 'memory',
                data: [
                    {
                        text: '50',
                        value: 50
                    },
                    {
                        text: '100',
                        value: 100
                    },
                    {
                        text: '200',
                        value: 200
                    },
                    {
                        text: '500',
                        value: 500
                    }
                ],
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});