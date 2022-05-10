Ext.define('WirelessLogger.model.comboModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'text'
        },
        {
            name: 'value'
        }
    ]
});