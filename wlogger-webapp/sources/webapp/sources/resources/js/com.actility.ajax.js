Ext.define('com.actility.ajax',{
    statics : {
 
    	statusCodeToString : function(statusCode) {
    		    var string = "OK";
    		    var code=200;
    		    if (typeof(statusCode)=="string") 
    		    	code = parseInt(statusCode);
    		    else
    		    	code = statusCode;
    		    
    			switch( code )
    			{
    				// 1xx Informational
    				case 100: string = 'Continue'; break;
    				case 101: string = 'Switching Protocols'; break;
    				case 102: string = 'Processing'; break; // WebDAV
    				case 122: string = 'Request-URI too long'; break; // Microsoft

    				// 2xx Success
    				case 200: string = 'OK'; break;
    				case 201: string = 'Created'; break;
    				case 202: string = 'Accepted'; break;
    				case 203: string = 'Non-Authoritative Information'; break; // HTTP/1.1
    				case 204: string = 'No Content'; break;
    				case 205: string = 'Reset Content'; break;
    				case 206: string = 'Partial Content'; break;
    				case 207: string = 'Multi-Status'; break; // WebDAV

    				// 3xx Redirection
    				case 300: string = 'Multiple Choices'; break;
    				case 301: string = 'Moved Permanently'; break;
    				case 302: string = 'Found'; break;
    				case 303: string = 'See Other'; break; //HTTP/1.1
    				case 304: string = 'Not Modified'; break;
    				case 305: string = 'Use Proxy'; break; // HTTP/1.1
    				case 306: string = 'Switch Proxy'; break; // Depreciated
    				case 307: string = 'Temporary Redirect'; break; // HTTP/1.1

    				// 4xx Client Error
    				case 400: string = 'Bad Request'; break;
    				case 401: string = 'Unauthorized'; break;
    				case 402: string = 'Payment Required'; break;
    				case 403: string = 'Forbidden'; break;
    				case 404: string = 'Not Found'; break;
    				case 405: string = 'Method Not Allowed'; break;
    				case 406: string = 'Not Acceptable'; break;
    				case 407: string = 'Proxy Authentication Required'; break;
    				case 408: string = 'Request Timeout'; break;
    				case 409: string = 'Conflict'; break;
    				case 410: string = 'Gone'; break;
    				case 411: string = 'Length Required'; break;
    				case 412: string = 'Precondition Failed'; break;
    				case 413: string = 'Request Entity Too Large'; break;
    				case 414: string = 'Request-URI Too Long'; break;
    				case 415: string = 'Unsupported Media Type'; break;
    				case 416: string = 'Requested Range Not Satisfiable'; break;
    				case 417: string = 'Expectation Failed'; break;
    				case 422: string = 'Unprocessable Entity'; break; // WebDAV
    				case 423: string = 'Locked'; break; // WebDAV
    				case 424: string = 'Failed Dependency'; break; // WebDAV
    				case 425: string = 'Unordered Collection'; break; // WebDAV
    				case 426: string = 'Upgrade Required'; break;
    				case 449: string = 'Retry With'; break; // Microsoft
    				case 450: string = 'Blocked'; break; // Microsoft

    				// 5xx Server Error
    				case 500: string = 'Internal Server Error'; break;
    				case 501: string = 'Not Implemented'; break;
    				case 502: string = 'Bad Gateway'; break;
    				case 503: string = 'Service Unavailable'; break;
    				case 504: string = 'Gateway Timeout'; break;
    				case 505: string = 'HTTP Version Not Supported'; break;
    				case 506: string = 'Variant Also Negotiates'; break;
    				case 507: string = 'Insufficient Storage'; break; // WebDAV
    				case 509: string = 'Bandwidth Limit Exceeded'; break; // Apache
    				case 510: string = 'Not Extended'; break;
    				// Unknown code:
    				default: string = 'Unknown';  break;
    			}
    			return string;
    	},
  
     request : function(config) {
		     //parameters:
		     // config.callback : callback in case of success
		     //config.errorMessage
     	
     	  //console.log('inside ajax.request, config.params :');
     	  //console.log(config.params);
		     var errorMessage = config.errorMessage;
         try {
		     			com.actility.ajaxRequest({
		     			//Ext.Ajax.request({
		     			    url: config.url,
		     			    method: config.method,
		     			    timeout: com.actility.global.ajaxRequestTimeout,
		     			    params: config.params,
		     			    success: function(response, options){
		     			       // console.log('DEBUG SPECIFIC...........');
		     			       // console.log(config.url);
		     			       // console.log(config.params);
		     			       // console.log(response.responseText);   
		     			       
		     			        if (response.responseText == "")
		     			        {
		     			        	   
		     			        	   //console.log('ajax response is empty');
		     			        	   //console.log(response);
		     			        	   if (response.status >= 200 && response.status < 300)
		     			        	   {
		     			        	   	   var status = {success: 'true'}; 
		     			        	       config.callbackSuccess.call(this,status,config.passthru);
		     			        	   }
		     			        	   else
		     			                 config.callbackError.call(this, 'ajax response is empty', response, config.passthru);
		     			             return;
		     			        }
		     			        try 
		     			        {
		     			           var status = Ext.decode(response.responseText);
		     			        } 
		     			        catch(err)
		     			        {
		     			        	 var status = {success: true, error : "NOT JSON", data:response.responseText}; 
		     			        }
		     			        if (status.success == true)
		     			        {
		     			        	  //c'est du JSON, on renvoi le decodage
		     			            if ((config != undefined) && (config.callbackSuccess !== undefined))
		     			            {
		     			                //console.log("success");
		     			                config.callbackSuccess.call(this,status,config.passthru);
		     			            }
		     			        }
		     			        else
		     			        {
		     			         	 //status.error = status.error + " ["+status.statusCode+"] : "+com.actility.ajax.statusCodeToString(status.statusCode);
  		     			         //status.error = status.message;
		     			         	 //console.log(status.error);
                         //console.log(status.errorCode);
                         var code=0;
                         if (typeof(status.errorCode)=="string") 
                           code = parseInt(status.errorCode);
                         else
                           code = status.errorCode;

                         if (code > 0) {
                           status.error = com.actility.locale.translateErrorMessage(code,status.error);
                         }
		     			         	 
		     			         	 config.callbackError.call(this, status.error, response, config.passthru);
		     			        }	        			
		     			    },
		     			    failure: function(response, options){
		     			    	 //console.log('ajax failure');
		     			    	 //console.log(response);
		     			    	 //console.log(options);
  		    			     // console.log('Request error '+ errorMessage +' : ' + response.statusText +' ('+response.status+')');
  		    			      
                      if (response.status == 401)                          
        		                com.actility.specific.extjs.toolManageDisconnection(); 
        		                
		     			        if ((config != undefined) && (config.callbackError !== undefined))
		     			        {
                        try 
                        {
                           var status = Ext.decode(response.responseText);
                        } 
                        catch(err)
                        {
                           var status = {success: true, error : "NOT JSON", data:response.responseText}; 
                        }
                        //status.error = status.error + " ["+status.statusCode+"] : "+com.actility.ajax.statusCodeToString(status.statusCode);
                        //status.error = status.message;
                        //console.log(status.error);
                        //console.log(status.errorCode);
                        var code=0;
                        if (typeof(status.errorCode)=="string") 
                          code = parseInt(status.errorCode);
                        else
                          code = status.errorCode;

                         if (code > 0) {
                           errorMessage = com.actility.locale.translateErrorMessage(code,status.error);
                         }

		     			           config.callbackError.call(this, errorMessage, response, config.passthru);
		     			        }
		     			
		     			    }
	       			});   //Ext.Ajax.request 
	       	} //try
		       catch(e)
		       {
		     	   console.log('Error: "' + e.message + '" at line: ' + e.lineNumber);

		       }
	    }, //request
        
 
      ajaxCall: function(config)
      {
      	     Ext.apply(config.params, {'none':'none'});
             
             //console.log('calling ajax login :');
             //console.log(config.params);
             com.actility.ajax.request({
                 url:config.url,
                 method:config.method,
                 params: config.params,
                 callbackSuccess: config.callbackSuccess,
                 callbackError: config.callbackError,
                 errorMessage: 'request error'
             });         	
      }
 }//statics
});
