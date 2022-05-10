Ext.define('WirelessLogger.store.autoreloadComboStore', {
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
            storeId: 'autoreloadComboStore',
            proxy: {
                type: 'memory',
                data: [
                    {
                        text: 'no',
                        value: 0
                    },
                    {
                        text: '10s',
                        value: 10000
                    },
                    {
                        text: '20s',
                        value: 20000
                    },
                    {
                        text: '30s',
                        value: 30000
                    },
                    {
                        text: '45s',
                        value: 45000
                    },
                    {
                        text: '60s',
                        value: 60000
                    },
                    {
                        text: '120s',
                        value: 120000
                    }
                ],
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});