Ext.override(Ext.data.Connection, {

    extraParams: {},

    requires: [
        // 'WirelessLogger.util.Keycloak'
    ],

    request: function (options) {
        options = options || {};
        var me = this,
            scope = options.scope || window,
            username = options.username || me.username,
            password = options.password || me.password || '',
            async, requestOptions, xhr;

        if (me.fireEvent('beforerequest', me, options) !== false) {

            requestOptions = me.setOptions(options, scope);

            if (me.isFormUpload(options)) {
                me.upload(options.form, requestOptions.url, requestOptions.data, options);
                return null;
            }

            // if autoabort is set, cancel the current transactions
            if (options.autoAbort || me.autoAbort) {
                me.abort();
            }

            // create a connection object
            async = options.async !== false ? (options.async || me.async) : false;
            xhr = me.openRequest(options, requestOptions, async, username, password);
            if (!Ext.isEmpty(WirelessLogger) && !Ext.isEmpty(WirelessLogger.util) && !Ext.isEmpty(WirelessLogger.util.Keycloak) && WirelessLogger.util.Keycloak.isReady()) {
                WirelessLogger.util.Keycloak.updateToken(function () {
                    // start the request!
                    me.finishRequest(options, requestOptions, async, xhr)
                }, function (error) {
                    console.log('impossible to update token', error);
                });
                return null;
            }
            return me.finishRequest(options, requestOptions, async, xhr)
        } else {
            Ext.callback(options.callback, options.scope, [options, undefined, undefined]);
            return null;
        }
    },

    finishRequest: function (options, requestOptions, async, xhr) {
        var me = this, headers;

        // XDR doesn't support setting any headers
//        var xdr = me.getIsXdr();
        var xdr = false;
        if (!xdr) {
            headers = me.setupHeaders(xhr, options, requestOptions.data, requestOptions.params);
        }

        // create the transaction object
        var request = {
            id: ++Ext.data.Connection.requestId,
            xhr: xhr,
            headers: headers,
            options: options,
            async: async,
//            binary: options.binary || me.getBinary(),
            timeout: Ext.defer(function () {
                request.timedout = true;
                me.abort(request);
            }, options.timeout || me.timeout)
        };

        me.requests[request.id] = request;
        me.latestId = request.id;
        // bind our statechange listener
        if (async) {
            if (!xdr) {
                xhr.onreadystatechange = Ext.Function.bind(me.onStateChange, me, [request]);
            }
        }

        if (xdr) {
            me.processXdrRequest(request, xhr);
        }
        // start the request!
        xhr.send(requestOptions.data);
        if (!async) {
            return me.onComplete(request);
        }
        return request;
    },

    setupHeaders: function (xhr, options, data, params) {
        //console.log("connection override: extra params");
        var result = this.callParent(arguments);
        // console.log("setupHeaders.result:", result);
        if (!Ext.isEmpty(WirelessLogger) && !Ext.isEmpty(WirelessLogger.util) && !Ext.isEmpty(WirelessLogger.util.Keycloak)) {
            var token = WirelessLogger.util.Keycloak.getToken();
            if (!Ext.isEmpty(token)) {
                result['Authorization'] = 'Bearer ' + token;
                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                if (com.actility.global.modeDEV)
                  xhr.setRequestHeader('X-Realm-ID', WirelessLogger.util.Config.getKeyConfig('keycloak.dev.realm'));
            }
        }
        return result;
    },

    createResponse: function (request) {
        var response = this.callParent(arguments);
        var status = response.status;
        if (status >= 400) {
            if (Ext.String.startsWith(response.getResponseHeader('Content-Type'), 'application/json')) {
                var result = Ext.JSON.decode(response.responseText);
                console.error('[' + result.code + '] ' + result.message);
            }
        }
        return response;
    }
});