

Ext.override(Ext.grid.Panel, {
    constructor : function() {
        for (var i = 0; i < arguments.length; i++) {
            if (arguments[i].data && arguments[i].data.plugins) {
                Ext.apply(arguments[i],{
                    plugins : arguments[i].data.plugins
                });
            }
        }
        this.callOverridden(arguments);
    }
});