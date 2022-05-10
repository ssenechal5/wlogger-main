Ext.define('WirelessLogger.view.messagePanel', {
    extend: 'Ext.form.Panel',

    requires: [
        'Ext.form.FieldSet',
        'Ext.form.Label',
        'Ext.toolbar.Toolbar',
        'Ext.Img',
        'Ext.toolbar.TextItem',
        'Ext.toolbar.Fill'
    ],

    height: 230,
    id: 'message',
    width: 500,
    closable: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'fieldset',
                    height: 80,
                    margin: 10,
                    padding: '10 0 0 0',
                    title: '${TIT Message}',
                    items: [
                        {
                            xtype: 'label',
                            itemId: 'labelMessage',
                            margin: '0 0 0 40',
                            text: '${Please wait, we are trying to connect you...}'
                        }
                    ]
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    itemId: 'status',
                    items: [
                        {
                            xtype: 'image',
                            height: 16,
                            itemId: 'statusImage',
                            width: 16,
                            src: 'resources/images/icon-none.png'
                        },
                        {
                            xtype: 'label',
                            itemId: 'statusMessage'
                        }
                    ]
                },
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    itemId: 'toolbar1',
                    items: [
                        {
                            xtype: 'tbtext',
                            text: '${LBL WIRELESS-LOGGER}'
                        },
                        {
                            xtype: 'tbfill'
                        },
                        {
                            xtype: 'image',
                            height: 61,
                            width: 61,
                            src: 'resources/images/thingparkWireless.png'
                        },
                        {
                            xtype: 'image',
                            height: 61,
                            width: 61,
                            src: 'resources/images/graph-icon.png'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});
