
Ext.define('WirelessLogger.store.protocolComboStore', {
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
            storeId: 'protocolComboStore',
            proxy: {
                type: 'memory',
                data: [
                    {
                        text: 'LBL LPWA - LoRa',
                        textTranslated: '',
                        image: '',
                        value: 'LPWA-LoRa'
                    },
                    {
                        text: 'LBL Cellular',
                        textTranslated: '',
                        image: '',
                        value: 'Cellular'
                    }
                ],
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});