Ext.define('WirelessLogger.util.Keycloak', {
    singleton: true,

    config: {
        keycloak: null,
        realm: null,
        ready: false
    },

    requires: [
        'Ext.Ajax',
        'WirelessLogger.util.Config'
    ],

    constructor: function (config) {
        this.initConfig(config);
        this.callParent(arguments);
    },

    initKeycloak: function (realm, callback) {

        var me = this;
        var config = {
            url: com.actility.global.buildKeyCloakUrl(WirelessLogger.util.Config.getKeyConfig('keycloak.uri.gui') || '/auth'),
            realm: realm,
            clientId: WirelessLogger.util.Config.getKeyConfig('keycloak.clientId') || 'wlogger',
            responseMode: WirelessLogger.util.Config.getKeyConfig('keycloak.response.mode') || 'query'
        };
        me.keycloak = Keycloak(config);
        me.keycloak.init(config).success(function (authenticated) {
            console.log('authenticated', authenticated);
            if (!authenticated) {
                me.keycloak.login().success(function () {
                    console.log('Login success');
                    me.ready = true;
                    callback();
                }).error(function (error) {
                    console.log('failed to login', error)
                });
            } else {
                me.ready = true;
                callback();
            }
        }).error(function (error) {
            console.log('failed to init keycloak', error);
        });
        me.keycloak.onAuthLogout = function () {
            if (window.removeEventListener) {
                window.removeEventListener("beforeunload", utils_windowUnload, false);
            } else if (window.detachEvent) {
                window.detachEvent("onbeforeunload", utils_windowUnload);
            }
            me.keycloak.login();
        }
    },

    getToken: function () {
        var me = this;
        if (Ext.isEmpty(me.keycloak)) {
            return null;
        }
        return me.keycloak.token;
    },

    updateToken: function (success, error) {
        var me = this;
        if (!Ext.isFunction(success) || Ext.isEmpty(success)) {
            success = function () {
            };
        }
        if (!Ext.isFunction(error) || Ext.isEmpty(error)) {
            error = function () {
            };
        }
        return me.keycloak.updateToken(10).success(function () {
            success(me.getToken());
        }).error(error);
    },

    isReady: function () {
        return this.ready;
    },

    logout: function () {
        var me = this;
        me.keycloak.logout();
    },

    onReady: function(callback) {
        var me = this;
        console.log('WirelessLogger.util.Keycloak.onReady url :', WirelessLogger.util.Config.getKeyConfig('keycloak.discovery.uri') || '/auth-realm');
        Ext.Ajax.request({
            url: com.actility.global.buildKeyCloakUrl(WirelessLogger.util.Config.getKeyConfig('keycloak.discovery.uri') || '/auth-realm'),
            method: 'GET',
            scope: this,
            failure: function (response, options) {
                console.log('WirelessLogger.util.Keycloak.onReady failure');
                if (!Ext.isEmpty(WirelessLogger.util.Config.getKeyConfig('keycloak.dev.realm'))) {
                    me.initKeycloak(WirelessLogger.util.Config.getKeyConfig('keycloak.dev.realm'), callback);
                }
            },
            success: function (response) {
                console.log('WirelessLogger.util.Keycloak.onReady success');
                console.log('response', response);
                if (!Ext.isEmpty(response.responseText)) {
                    var authRealm = Ext.JSON.decode(response.responseText.trim());
                    me.initKeycloak(authRealm.realm, callback);
                }
            }
        });
    }

});