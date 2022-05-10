Ext.define('WirelessLogger.store.treeStore', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'WirelessLogger.model.treeModel',
        'Ext.data.proxy.Memory',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'WirelessLogger.model.treeModel',
            storeId: 'MyJsonTreeStore',
            proxy: {
                type: 'memory',
                data: [
                    {
                        text: 'Dashboard',
                        id: 'dashboard',
                        leaf: true,
                        cls: 'file',
                        children: [
                            
                        ]
                    },
                    {
                        text: 'Management',
                        id: 'management',
                        leaf: true,
                        cls: 'file',
                        children: [
                            
                        ]
                    }
                ],
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});