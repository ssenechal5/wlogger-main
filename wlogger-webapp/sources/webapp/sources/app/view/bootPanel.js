Ext.define('WirelessLogger.view.bootPanel', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Ext.toolbar.Toolbar',
        'Ext.toolbar.TextItem',
        'Ext.toolbar.Fill',
        'Ext.Img',
        'Ext.form.FieldSet',
        'Ext.form.Label'
    ],

    id: 'boot',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            listeners: {
                afterrender: {
                    fn: me.onBootAfterRender,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onBootAfterRender: function(component, eOpts) {
        com.actility.specific.extjs.manageBootAfterRender();
    }

});
