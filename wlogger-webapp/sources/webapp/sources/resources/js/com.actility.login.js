Ext.define('com.actility.login',{

statics : {
     displaySSOLoginPopup : function(applicationId,hostname)
     {
//       var ssoCallTpl = new Ext.XTemplate("openSSOWindow('{applicationId}', {mobile}, '{gmaps}')");
//       var fnStr = ssoCallTpl.apply({
//                    applicationId: applicationId,
//                    mobile: false
//       });

       Ext.getBody().unmask();
       Ext.create('Ext.window.Window', {
          closable: false,
          title: '${TIT Sign in}',
          width: 400,
          height: 200,
          layout: 'border',
          id: 'login',
          items: [
/*          
             {
                xtype: 'container',
                padding: 20,
                region: 'center',
                width: 384,
                height: 144,
                items: [
                    {
                        itemId: 'operatorLogo',
                        xtype: 'image',
                        style:'display: block; margin: 0 auto; max-height: 100%; max-width: 100%',
                          src: com.actility.global.thingparkUrl+'/thingpark/smp/rest/resources/files/logo/operator?domain='+hostname
                    }
                ]
             },
*/             
             {
//                width: 400,
//                height: 180,
                id: 'loginSubForm',
//                region: 'south',
                region: 'center',
                layout: 'border',
                items: [
/*                
                   {
                      region: 'center',
                      html: '<div style="display: flex; padding:20px 25%;"><span class="tpkLoginBtn" onclick="'+fnStr+'" style="display: inline-block; width: 195px; height: 35px; background-image: url('+com.actility.global.thingparkUrl+'/thingpark/smp/rest/resources/files/signInButton/operator?domain='+hostname+'); background-color: transparent; background-position: 50% 50%; background-repeat: initial initial;">&nbsp;</span></div>'
                   },
*/                   
                   {
//                      region: 'south',
                      region: 'center',
                      style: "margin: 10 10 20 10",
                      items: [
/*                      
                         {
                             width: 370,
                             itemId: 'infoText',
                             html: '<p style="text-align:center; background-color: white">'+com.actility.locale.translateLogin('LBL LOGIN')+'</p>',
                             listeners: { afterrender: { fn: com.actility.login.onInfoTextAfterRender } },
                         },
*/
                         {
                             xtype: 'fieldset',
//                             hidden: true,
//                             width: 370,
//                             height: 100,
                             itemId: 'loginForm',
                             title: '${TIT Login}',
                             items: [
                                        {
                                            xtype: 'textfield',
                                            anchor: '100%',
                                            itemId: 'email',
                                            fieldLabel: '${LBL Email}',
                                            listeners: { specialkey: { fn: com.actility.login.onEmailSpecialkey } }
                                        },
                                        {
                                            xtype: 'textfield',
                                            anchor: '100%',
                                            itemId: 'password',
                                            fieldLabel: '${LBL Password}',
                                            inputType: 'password',
                                            listeners: { specialkey: { fn: com.actility.login.onPasswordSpecialkey } }
                                        },
                                        {
                                              xtype: 'button',
                                              text: '${BTN Connect}',
                                              textAlign: 'right',
                                              listeners: { click: { fn: com.actility.login.onLoginButtonClick } }
                                        }
                                   ]
                           }
                      ]

                   }
                ]
            }
         ]//items
       }).show();
    },
/*    
    onInfoTextAfterRender: function(component, eOpts) {
           var aEl = Ext.get('clickHere');
           if (aEl) { aEl.on('click', com.actility.specific.extjs.infoTextClick); }
    },
*/
    onEmailSpecialkey: function(field, e, eOpts) {
        com.actility.specific.extjs.manageLoginPasswordSpecialKeys(field,e);
    },

    onPasswordSpecialkey: function(field, e, eOpts) {
        com.actility.specific.extjs.manageLoginPasswordSpecialKeys(field,e);
    },

    onLoginButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageLoginButtonClick();
    },

    onLoginPanelAfterRender: function(component, eOpts) {
        com.actility.specific.extjs.manageLoginAfterRender();
    }

}});

