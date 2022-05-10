Ext.define('WirelessLogger.model.userModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'username'
        },
        {
            name: 'password'
        },
        {
            name: 'description'
        },
        {
            name: 'isThingparkSubscriber'
        },
        {
            name: 'thingparkSubscriberID'
        },
        {
            name: 'thingparkSubscriptionHref'
        },
        {
            name: 'customerIdList'
        },
        {
            name: 'uid'
        }
    ]
});