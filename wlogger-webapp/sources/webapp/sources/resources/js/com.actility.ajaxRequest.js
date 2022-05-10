Ext.ns('com.actility');
com.actility.ajaxRequest = function(config) 
{
   com.actility.ajaxRequest.maxRequests = 10;
   Ext.apply(config.params,{queuePushTime : new Date().getTime()});
 
   if (typeof com.actility.ajaxRequest.Manager == 'undefined') 
   {
      com.actility.ajaxRequest.Manager = function() 
      {
         
         com.actility.ajaxRequest.Manager.process = function(request) 
         {
           
            var requestBefore = {};
            Ext.apply(requestBefore, request);
          
            var ajaxRequest = {
               success : function(response, options) {
               	   com.actility.ajaxRequest.Manager.nbConnectionAlive--;
               	   requestBefore.success(response, options);
               	   if(com.actility.ajaxRequest.queue.length > 0) {
                      com.actility.ajaxRequest.Manager();
                   }
               },
               failure : function(response, options) {
               	   com.actility.ajaxRequest.Manager.nbConnectionAlive--;
               	   requestBefore.failure(response, options);
               	   if(com.actility.ajaxRequest.queue.length > 0) {
                      com.actility.ajaxRequest.Manager();
                   }               
               } 
            };             

            Ext.apply(request.params,{traceid : "1111"});
            Ext.apply(request,ajaxRequest);
            Ext.apply(request.params,{queuePopTime : new Date().getTime()});
            Ext.Ajax.request(request);
         } //com.actility.ajaxRequest.Manager.process                       


         if(typeof com.actility.ajaxRequest.Manager.nbConnectionAlive == 'undefined') {
                 com.actility.ajaxRequest.Manager.nbConnectionAlive = 0;
         }
         var nextRequest = com.actility.ajaxRequest.queue[0];


         if (typeof nextRequest != 'undefined') {
                 if(com.actility.ajaxRequest.Manager.nbConnectionAlive < com.actility.ajaxRequest.maxRequests) {
                         com.actility.ajaxRequest.Manager.nbConnectionAlive++;
                         com.actility.ajaxRequest.queue.reverse();
                         var request = com.actility.ajaxRequest.queue.pop();
                         com.actility.ajaxRequest.queue.reverse();
                         com.actility.ajaxRequest.Manager.process(request);
                 }
         }         	

      } //com.actility.ajaxRequest.Manager
   	
   } //if    

   // first call
   if (typeof com.actility.ajaxRequest.queue == 'undefined') {
      com.actility.ajaxRequest.queue = [];
   }
   com.actility.ajaxRequest.queue.push(config);
   if(com.actility.ajaxRequest.queue.length == 1) {
      com.actility.ajaxRequest.Manager();           
   }
}
