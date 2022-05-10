Ext.define('WirelessLogger.store.dataStore', {
    extend: 'Ext.data.Store',

    requires: [
        'WirelessLogger.model.dataModel',
        'Ext.data.proxy.Memory',
        'Ext.data.reader.Json'
    ],
    more: false,

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'WirelessLogger.model.dataModel',
            storeId: 'dataStore',
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    },
    hasMore: function(){
        return this.more;
    },
    loadPage: function(page, options) {
        this.currentPage = page;
        //appel fonction de provisioning
        com.actility.specific.extjs.toolRefreshDashboard(page);
    }
});