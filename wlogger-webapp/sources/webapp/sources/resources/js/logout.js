Ext.onReady(function(){
   Ext.define("com.actility.LogOutMessage", function(a) {
      return {
         statics: {
            LogOutSuccessResponse: function(logOutId) {
                return Ext.encode({
                    messageType: a.MessageType.LogOutResponse,
                    success: true,
                    identifier: logOutId
                });
            },
            LogOutFailureResponse: function(logOutId) {
                console.log("LogOutFailureResponse");
                return Ext.encode({
                    messageType: a.MessageType.LogOutResponse,
                    success: false,
                    identifier: logOutId
                });
            },

            MessageType: {
                LogOutResponse: "LOG_OUT_RESPONSE"
            }
         }
      }
   });

    /**
     * Extract the logOutID parameter from the current document URL
     * empty string if not present
     */
    var logOutId = function(queryString) {
        if(queryString !== undefined) {
            return Ext.Object.fromQueryString(queryString).logOutID || "";
        } else {
            return "";
        }
    }(document.URL.split("?")[1]); // get the query string

   Ext.Ajax.request({
            url: "/thingpark/wlogger/rest/users/logout",
            method: "GET",
            params: {},
            success: function(a) {
                if (window.parent) {
                    window.parent.postMessage(com.actility.LogOutMessage.LogOutSuccessResponse(logOutId), "*")
                }
            },
            failure: function(a, b) {
                if (window.parent) {
                    window.parent.postMessage(com.actility.LogOutMessage.LogOutFailureResponse(logOutId), "*")
                }
            }
  });
});
