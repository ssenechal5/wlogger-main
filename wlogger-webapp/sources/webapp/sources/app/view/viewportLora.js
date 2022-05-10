Ext.define('WirelessLogger.view.viewportLora', {
    extend: 'Ext.container.Viewport',

    requires: [
        'Ext.toolbar.Toolbar',
        'Ext.toolbar.TextItem',
        'Ext.form.field.Display',
        'Ext.toolbar.Fill',
        'Ext.Img',
        'Ext.tree.Panel',
        'Ext.tree.View',
        'Ext.button.Button',
        'Ext.form.field.Date',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Checkbox',
        'Ext.form.FieldSet',
        'Ext.grid.Panel',
        'Ext.grid.View',
        'Ext.grid.column.Action',
        'Ext.form.Panel',
        'Ext.form.Label'
    ],

    id: 'rootViewport',
    layout: 'border',
    autoWidth: false,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'container',
                    region: 'north',
                    id: 'north',
                    height: 40,
                    autoScroll: true,
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'toolbar',
                            id: 'header',
                            width: 70,
                            items: [
                                {
                                    xtype: 'tbtext',
                                    text: '${LBL WIRELESS-LOGGER}'
                                },
                                {
                                    xtype: 'displayfield',
                                    itemId: 'timestamp',
                                    width: 300,
                                    fieldLabel: '${LBL Last Update}',
                                    labelWidth: 70,
                                    value: ''
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    xtype: 'image',
                                    height: 32,
                                    width: 32,
                                    src: 'resources/images/thingparkWireless.png'
                                },
                                {
                                    xtype: 'image',
                                    height: 32,
                                    width: 32,
                                    src: 'resources/images/graph-icon.png'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'treepanel',
                    region: 'west',
                    hidden: true,
                    id: 'navigationTreePanel',
                    itemId: 'treePanel',
                    width: 150,
                    collapsed: true,
                    collapsible: true,
                    store: 'treeStore',
                    rootVisible: false,
                    viewConfig: {
                        resizable: true
                    },
                    listeners: {
                        itemclick: {
                            fn: me.onTreePanelItemClick,
                            scope: me
                        }
                    }
                },
                {
                    xtype: 'container',
                    region: 'center',
                    itemId: 'container',
                    layout: 'card',
                    items: [
                        {
                            xtype: 'panel',
                            id: 'dashboard',
                            itemId: 'dashboard',
                            layout: 'fit',
                            dockedItems: [
                                {
                                    xtype: 'panel',
                                    dock: 'top',
                                    id: 'dashboardHeader',
                                    itemId: 'dashboardHeader',
                                    layout: 'fit',
                                    collapsible: true,
                                    title: '',

                                    header: {
                                        xtype: 'header',
                                        titlePosition: 0,
                                        defaults: {
                                             padding: '0 0 0 0',
                                        },
                                        items: [
                                        {
                                          xtype: 'combobox',
                                          itemId: 'protocol',
                                          id : 'protocolSwitch',
                                          fieldLabel: '${LBL Radio Access Network}',
                                          width: 200,
                                          height: 30,
                                          labelWidth: 150,
                                          hideLabel : true,
                                          dirtyCls : 'ranComboboxTrigger',
                                          value: 'LPWA-LoRa',
                                          store: 'protocolComboStore',
                                          queryMode:    'local',
                                          valueField: 'value',
                                          editable:false,
                                          hidden : true,
                                          listConfig: {
                                             minHeight:62,maxHeight:62,height:62}, //size of dropdown list
                                          triggerClass: 'ranComboboxTrigger',
                                          triggerCls: 'ranComboboxTrigger',
                                          tpl: Ext.create('Ext.XTemplate',
                                               '<tpl for=".">',
                                               '<div class="x-boundlist-item" style="height:30px">',
                                               '<img src="resources/images/{image}" align="left">{textTranslated}',
                                               '</div>',
                                               '</tpl>'),
                                          listeners: {
                                              render: function (comboBox) {
                                                   var comboStore = comboBox.getStore();
                                                   comboStore.each(function(record)
                                                   {
                                                      record.set("textTranslated",com.actility.locale.translate(record.get("text")));
                                                      record.set("image",record.get("value")+'.png');
                                                   });

                                                   var recIndex = comboStore.findExact('value',comboBox.value);
                                                   var rec =  comboStore.getAt(recIndex);
                                                   if (rec) {
                                                       comboBox.setRawValue(rec.get("textTranslated"));
                                                       comboBox.inputEl.setStyle({
                                                        // 'background-image':    'url(resources/images/'+ record.get("image") + ')',
                                                        'background-image':    'url(resources/images/'+ comboBox.value + '.png)',
                                                        'background-repeat':   'no-repeat',
                                                        'background-position': '3px center',
                                                        'padding-left':        '50px'
                                                       });
                                                   }
                                              },
                                              change: function(field, selectedValue) {
                                                setTimeout(function(){com.actility.specific.extjs.manageToolCombo(selectedValue);},30);
                                              },
                                          }
                                        }//item
                                       ] //items
                                    }, //header
/*
                                    tools : [{type : 'gear',
                                              id : 'protocolSwitch',
                                              hidden : true, 
                                              handler : function(e,el, ow, tool){com.actility.specific.extjs.manageToolGear(e,el,ow,tool);}}],
*/                                              
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            height: 30,
                                            itemId: 'toolbarTopTop',
                                            items: [
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterDevAddr',
                                                    fieldLabel: '${LBL DevAddr Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the DevAddr field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearDevAddrClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterDevEUI',
                                                    fieldLabel: '${LBL DevEUI Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the DevEUI field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearDevEUIClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterLRR',
                                                    width: 200,
                                                    fieldLabel: '${LBL LRR Id Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the LRR field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearLRRClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterLRC',
                                                    width: 180,
                                                    fieldLabel: '${LBL LRC Id Filtering}',
                                                    labelWidth: 90
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the LRC field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearLRCClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterASID',
                                                    width: 180,
                                                    fieldLabel: '${LBL ASID Filtering}',
                                                    labelWidth: 90
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the ASID field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearASIDClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterSubscriberID',
                                                    hidden: (com.actility.global.notNetworkPartnerAccess),
                                                    width: 180,
                                                    fieldLabel: '${LBL SubscriberID Filtering}',
                                                    labelWidth: 90
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    hidden: (com.actility.global.notNetworkPartnerAccess),
                                                    tooltip: '${TT Clear the SubscriberID field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearSubIDClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                   xtype: 'tbfill'
                                                },
                                                {
                                                    xtype: 'button',
                                                    itemId: 'logout',
                                                    iconCls: 'logout',
                                                    text: '${BTN Logout}',
                                                    tooltip: '${TT Logout}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onLogoutClick,
                                                            scope: me
                                                        }
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            height: 30,
                                            itemId: 'toolbarTopMiddle1',
                                            items: [
                                                {
                                                    xtype: 'datetimefield',
                                                    cls: 'datetimepicker',
                                                    itemId: 'fromDate',
                                                    fieldLabel: '${LBL From}',
                                                    format : 'Y-m-d H:i:s',
                                                    //value : new Date(),
                                                    hourText: 'H',
                                                    minuteText: 'Min',
                                                    secondText: 'Sec',
                                                    labelWidth: 110,
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    width: 40
                                                },
                                                {
                                                    xtype: 'datetimefield',
                                                    cls: 'datetimepicker',
                                                    itemId: 'toDate',
                                                    fieldLabel: '${LBL To}',
                                                    format : 'Y-m-d H:i:s',
                                                    //value : new Date(),
                                                    hourText: 'H',
                                                    minuteText: 'Min',
                                                    secondText: 'Sec',
                                                    labelWidth: 110,
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    width: 40
                                                },
                                                {
                                                    xtype: 'combo',
                                                    itemId: 'subtype',
                                                    fieldLabel: '${LBL Packet Type}',
                                                    store: 'stpComboStore',
                                                    displayField: 'text',
                                                    valueField: 'value',
                                                    queryMode: 'local',
                                                    multiSelect: true,
                                                    filterPickList: true,
                                                    width: 300,
                                                    labelWidth: 90
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear packet type}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearSubTypeClick,
                                                            scope: me
                                                        }
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            height: 30,
                                            itemId: 'toolbarTopMiddle2',
                                            hidden: ((!com.actility.global.notNetworkPartnerAccess) && !(com.actility.global.guiMode == com.actility.global.adminAppliName)),
                                            items: [
                                                {
                                                    xtype: 'combobox',
                                                    itemId: 'decoder',
                                                    fieldLabel: '${LBL Decoder}',
                                                    labelWidth: 110,
                                                    value: 'raw',
                                                    store: 'decoderLoraComboStore',
                                                    valueField: 'value'
                                                }/*,
                                                {
                                                    xtype: 'displayfield',
                                                    width: 50
                                                },
                                                {
                                                    xtype: 'combobox',
                                                    itemId: 'last',
                                                    fieldLabel: '${LBL Last}',
                                                    labelWidth: 110,
                                                    value: 50,
                                                    store: 'lastComboStore',
                                                    valueField: 'value'
                                                }*/
                                            ]
                                        },
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            height: 30,
                                            itemId: 'toolbarTopBottom',
                                            items: [
                                                {
                                                    xtype: 'combobox',
                                                    itemId: 'autoreload',
                                                    fieldLabel: '${LBL Auto Reload}',
                                                    labelWidth: 110,
                                                    value: 'no',
                                                    store: 'autoreloadComboStore',
                                                    valueField: 'value',
                                                    listeners: {
                                                        change: {
                                                            fn: me.onAutoreloadChange,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'checkboxfield',
                                                    itemId: 'expand',
                                                    width: 170,
                                                    fieldLabel: '${LBL Expand All}',
                                                    labelWidth: 110,
                                                    listeners: {
                                                        change: {
                                                            fn: me.onCheckboxfieldChange1,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    width: 20
                                                },
                                                {
                                                    xtype: 'button',
                                                    itemId: 'refresh',
                                                    iconCls: 'reload',
                                                    text: '${BTN Refresh}',
                                                    tooltip: '${TT Reload the grid}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onRefreshClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    width: 20
                                                },
                                                {
                                                    xtype: 'combobox',
                                                    itemId: 'last',
                                                    fieldLabel: '${LBL Last}',
                                                    labelWidth: 70,
                                                    value: com.actility.global.pageSize,
                                                    store: 'lastComboStore',
                                                    valueField: 'value'
                                                },                                                
                                                {
                                                    xtype: 'button',
                                                    itemId: 'export',
                                                    iconCls: 'export',
                                                    text: '${BTN Export}',
                                                    tooltip: '${TT Export in csv file}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onExportClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    width: 20
                                                },
                                                {
                                                    xtype: 'button',
                                                    iconCls: 'map',
                                                    hidden: (com.actility.global.mapService=="NONE"),
                                                    text: '${BTN Map}',
                                                    tooltip: '${TT Display last device position in a map}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onMapClick,
                                                            scope: me
                                                        }
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ],
                            items: [
                                {
                                    xtype: 'fieldset',
                                    itemId: 'resultList',
                                    margin: 10,
                                    padding: '10 0 0 0',
                                    layout: 'fit',
                                    title: 'List',
                                    items: [
                                        {
                                            xtype: 'gridpanel',
                                            data: {
                                                plugins: [
                                                    {
                                                        ptype: 'rowexpander',
                                                        expandOnDblClick : false,
                                                        rowBodyTpl: [],
                                                    }
                                                ]
                                            },
                                            height: 400,
                                            itemId: 'dashboardResultGridPannel',
                                            width: 100,
                                            autoScroll: true,
                                            resizable: true,
                                            forceFit: false,
                                            store: 'dataStore',
                                            viewConfig: {
                                                autoScroll: true,
                                                enableTextSelection: true,
                                                loadMask: true,
                                                listeners : {
                                                   expandbody : function (rowNode, record, expandRow, eOpts)
                                                   {
                                                      var minusIfNullOrEmpty=function($data)
                                                      {
                                                        if (($data == null) || ($data == "null") || ($data.length==0))
                                                           return '-';
                                                        else
                                                           return $data;
                                                      }
                                                      var output='<tr><td><div style="width:1800px;overflow-x: auto;font-size: 10px;padding: 0px 0px 0px 10px">';

                                                      // NFR 1036 : MultiCast
                                                      if (record.get('direction') == 5) {
                                                         output+='<p>----------------------------------------<br>';
                                                         output+=com.actility.locale.translate('LBL device reset report');
                                                         output+='<br><br>'+com.actility.locale.translate('LBL Reset type :')+com.actility.locale.translate(record.get('resetTp'));
                                                         output+='<br>----------------------------------------</p>';
                                                      }

                                                      // NFR 1036 : MultiCast
                                                      if (record.get('direction') == 3) {
                                                         output+='<p>----------------------------------------<br>';
                                                         output+=com.actility.locale.translate('LBL multicast');
                                                         output+='<br><br>'+com.actility.locale.translate('LBL transmission status :')+record.get('tss');
                                                         output+='<br>'+com.actility.locale.translate('LBL lrrs associated :')+record.get('lcn');
                                                         output+='<br>'+com.actility.locale.translate('LBL LRRs having successfully transmitted :')+record.get('lsc');
                                                         output+='<br>'+com.actility.locale.translate('LBL LRRs that failed in transmitting :')+record.get('lfc');
                                                         output+=com.actility.specific.utils.buildLFDTable(record.get('lfd'));
                                                         output+='<br>----------------------------------------</p>';
                                                      }

                                                      // NFR 809
                                                      var devEUI=record.get('DevEUI');
                                                      // NFR 312
                                                      var roamingType=record.get('rt');
                                                      var gwOp=record.get('gwOp');
                                                      if (gwOp != '' && gwOp != null && gwOp != "null") {
                                                         output+='<p>----------------------------------------<br>';

                                                         if (roamingType != '' && roamingType != null && roamingType != "null") {
                                                            // new algo
                                                            if (roamingType == 1) {
                                                               output+=com.actility.locale.translate('LBL sNS');
                                                            } else {
                                                               output+=com.actility.locale.translate('LBL fNS');
                                                            }
                                                         } else {
                                                            // old algo
                                                            if (devEUI != '' && devEUI != null && devEUI != "null") {
                                                               output+=com.actility.locale.translate('LBL sNS');
                                                            } else {
                                                               output+=com.actility.locale.translate('LBL fNS');
                                                            }
                                                         }
                                                         output+='<br><br>'+com.actility.locale.translate('LBL Best Gateway ID :')+record.get('gwID');
                                                         output+='<br>'+com.actility.locale.translate('LBL Gateway Token :')+record.get('gwTk');
                                                         output+='<br>'+com.actility.locale.translate('LBL Foreign Operator Net ID :')+record.get('gwOp');
                                                         output+='<br>'+com.actility.locale.translate('LBL Foreign Operator NS ID:')+record.get('foreignOperatorNSID');
                                                         output+='<br>'+com.actility.locale.translate('LBL Roaming type :')+record.get('rt');
                                                         output+='<br>'+com.actility.locale.translate('LBL Roaming result :')+record.get('rr');
                                                         output+='<br>----------------------------------------</p>';
                                                      }

                                                      if (record.get('direction') < 2) {
                                                         output+='<p>'+com.actility.locale.translate('LBL MType :')+record.get('MTypeText');
                                                         // RDTP 5787
                                                         if (record.get('rul') == 1) {
                                                           output+=com.actility.locale.translate('LBL (generated for a repeated UL)');
                                                         }
                                                         output+=' </p>';
                                                      }
                                                      if (record.get('direction') == 1)
                                                         output+='<p>'+com.actility.locale.translate('LBL RX1/RX2Delay :')+record.get('lrcRequestedRxDelay')+'</p>';
                                                      if (record.get('direction') != 2)
                                                      {
                                                         if (record.get('MType') > 1)
                                                         {
                                                            output+='<p>'+com.actility.locale.translate('LBL Flags :')+'ADR : '+record.get('ADRbit')+', ADRAckReq : '+record.get('ADRAckReq')+', ACK : '+record.get('ACKbit');
                                                            if (record.get('direction') == 1)
                                                               output+=', FPending : '+record.get('FPending');
                                                            output+='</p>';
                                                         }
                                                      }

                                                      if (record.get('direction') != 2)
                                                      {
                                                         if (record.get('direction') < 2) 
                                                         {
                                                           output+='<p>'+com.actility.locale.translate('LBL Mac (hex) :')+minusIfNullOrEmpty(record.get('rawMacCommands'))+'</p>';
                                                           output+='<p><pre>'+record.get('MACDecoded')+'</pre></p>';
                                                         } 
                                                         if (record.get('MType') > 1 || record.get('direction') == 3)
                                                         {
                                                            output+='<p style="overflow-wrap:break-word">'+com.actility.locale.translate('LBL Data (hex) :');
                                                            var payload=record.get('payload_hex');
                                                            if (payload == 'None')
                                                               output+=com.actility.locale.translate('LBL None');
                                                            else if (payload == 'Hidden') 
                                                               output+=com.actility.locale.translate('LBL Hidden');
                                                            else {
                                                               output+=payload;
                                                               // RDTP-10309
                                                               if (record.get('payload_encryption') == 1) {
                                                                 output+=" "+com.actility.locale.translate('LBL encrypted');
                                                               } else if (record.get('payload_encryption') == 0) {
                                                                 output+=" "+com.actility.locale.translate('LBL not encrypted');
                                                               }
                                                            }
                                                            output+='</p>';

                                                            output+='<p>'+com.actility.locale.translate('LBL driver metadata :');
                                                            if (record.get('payload_driver_model') != null && record.get('payload_driver_model') != "null" && record.get('payload_driver_model') != "") 
                                                              output+=com.actility.locale.translate('LBL model :')+record.get('payload_driver_model');
                                                            else 
                                                              output+=com.actility.locale.translate('LBL model :')+'-';
                                                            if (record.get('payload_driver_application') != null && record.get('payload_driver_application') != "null" && record.get('payload_driver_application') != "") 
                                                              output+=' , '+com.actility.locale.translate('LBL application :')+record.get('payload_driver_application');
                                                            else
                                                              output+=' , '+com.actility.locale.translate('LBL application :')+'-';
                                                            output+='</p>';

                                                            if (record.get('payload_decoding_error') != null && record.get('payload_decoding_error') != "null" && record.get('payload_decoding_error') != "") 
                                                              output+='<p>'+com.actility.locale.translate('LBL Payload decoding error :')+'</p><p><pre>'+JSON.stringify(JSON.parse(record.get('payload_decoding_error')),null,2)+'</pre></p>';

                                                            if (record.get('payload_driver_id') != null && record.get('payload_driver_id') != "null" && record.get('payload_driver_id') != "") 
                                                              output+='<p>'+com.actility.locale.translate('LBL Driver id :')+record.get('payload_driver_id')+'</p>';
                                                            if (record.get('payload_decoded') != null && record.get('payload_decoded') != "null" && record.get('payload_decoded') != "") {
                                                                if (record.get('payload_decoded').startsWith("{")) {
                                                                  output+='<p><pre>'+JSON.stringify(JSON.parse(record.get('payload_decoded')),null,2)+'</pre></p>';
                                                                } else {
                                                                    output += '<p>' + record.get('payload_decoded') + '</p>';
                                                                }
                                                            }

                                                             output += '<p>' + com.actility.locale.translate('LBL Data size (bytes) :');
                                                             if (record.get('PayloadSize') != null)
                                                                 output += record.get('PayloadSize') + '</p>';
                                                             else
                                                                 output += com.actility.locale.translate('LBL Unknown') + '</p>';
                                                         }
                                                          if (record.get('direction') < 2) {
                                                              output += '<p>' + com.actility.locale.translate('LBL AirTime (s) :') + record.get('AirTime') + '</p>';
                                                          }
                                                      }
                                                       if ((record.get('direction') == 0) || (record.get('direction') == 2))
                                                           output += record.get('LRRList');
                                                       if ((record.get('direction') == 1) && (record.get('DLStatus') != "null")) {
                                                           output += '<p>' + com.actility.locale.translate('LBL Delivery Status :');
                                                           output += com.actility.specific.utils.decodeTranslateDeliveryStatus(record.get('DLStatus'));
                                                           output += '</p>';
                                                           if (record.get('DLFailedCause1') != null && record.get('DLFailedCause1') != "null" && record.get('DLFailedCause1') != "00" && record.get('DLFailedCause1') != "") {
                                                               output += '<p>' + com.actility.locale.translate('LBL Delivery Failed Cause on RX1 :');
                                                               output += com.actility.specific.utils.decodeTranslateDeliveryFailedCause1(record.get('DLFailedCause1'));
                                                               output += '</p>';
                                                           }
                                                         if (record.get('DLFailedCause2') != null && record.get('DLFailedCause2') != "null" && record.get('DLFailedCause2') != "00" && record.get('DLFailedCause2') != "")
                                                         {
                                                            output+='<p>'+com.actility.locale.translate('LBL Delivery Failed Cause on RX2 :');
                                                            output+=com.actility.specific.utils.decodeTranslateDeliveryFailedCause2(record.get('DLFailedCause2'));
                                                            output+='</p>';
                                                         }
                                                         if (record.get('DLFailedCause3') != null && record.get('DLFailedCause3') != "null" && record.get('DLFailedCause3') != "00" && record.get('DLFailedCause3') != "")
                                                         {
                                                            output+='<p>'+com.actility.locale.translate('LBL Delivery Failed Cause on ping slot :');
                                                            output+=com.actility.specific.utils.decodeTranslateDeliveryFailedCause3(record.get('DLFailedCause3'));
                                                            output+='</p>';
                                                         }
                                                      }
                                                      if ((record.get('direction') == 0) || (record.get('direction') == 2))
                                                      {
                                                         output+='<p>';
                                                         output+=com.actility.locale.translate('LBL Device [');
                                                         output+=com.actility.locale.translate('LBL Lat (solv) :') + minusIfNullOrEmpty(record.get('SolvLAT')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Lat :') + minusIfNullOrEmpty(record.get('DevLAT')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Lon (solv) :') + minusIfNullOrEmpty(record.get('SolvLON')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Lon :') + minusIfNullOrEmpty(record.get('DevLON')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Loc radius :') + minusIfNullOrEmpty(record.get('DevLocRadius')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Loc time :') + minusIfNullOrEmpty(record.get('DevLocTime')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Alt :') + minusIfNullOrEmpty(record.get('DevAlt')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Alt radius :') + minusIfNullOrEmpty(record.get('DevAltRadius')) + ' ';
                                                         output+=com.actility.locale.translate('LBL Acc :') + minusIfNullOrEmpty(record.get('DevAcc')) + ' ';
                                                         // NFR 791
                                                         output+=com.actility.locale.translate('LBL North Velocity :') + minusIfNullOrEmpty(record.get('DevNorthVelocity')) + ' ';
                                                         output+=com.actility.locale.translate('LBL East Velocity :') + minusIfNullOrEmpty(record.get('DevEastVelocity')) + ' ';
                                                         // NFR 3156
                                                         if (record.get('lcMU') != null && record.get('lcMU') != "null" && record.get('lcMU') != "")
                                                         {
                                                            output+=com.actility.locale.translate('LBL Device location method used :') + minusIfNullOrEmpty(record.get('lcMU')) + ' ';
                                                         }
                                                         output+=com.actility.locale.translate('LBL ]')+'</p>';
                                                      }
                                                      if (record.get('direction') == 0)
                                                      {
                                                         output+='<p>'+com.actility.locale.translate('LBL Reporting Status :');
                                                         if (record.get('Late') == 1)
                                                            output+=com.actility.locale.translate('LBL Late')+'</p>';
                                                         else
                                                            output+=com.actility.locale.translate('LBL On time')+'</p>';
                                                      }
                                                      if (record.get('ismb') != null && record.get('ismb') != "null" && record.get('ismb') != "")
                                                      {
                                                        output+='<p>'+com.actility.locale.translate('LBL Ism :') + com.actility.locale.translate(minusIfNullOrEmpty(record.get('ismb')));
                                                        output+='</p>';
                                                      }
                                                      if (record.get('rfRegion') != null && record.get('rfRegion') != "null" && record.get('rfRegion') != "")
                                                      {
                                                        output+='<p>'+com.actility.locale.translate('LBL RF region:') + minusIfNullOrEmpty(record.get('rfRegion'));
                                                        output+='</p>';
                                                      }

                                                      // NFR724
                                                      if (record.get('direction') != 2 && record.get('direction') < 5)
                                                         output+='<p>'+com.actility.locale.translate('LBL ASID :')+record.get('asID')+'</p>';

                                                      // NFR 1036
                                                      if (record.get('direction') == 1 && record.get('mcc') != null && record.get('mcc') != "null" && record.get('mcc') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL multicast class :')+record.get('mcc')+'</p>';

                                                      // NFR 1061 1062
                                                      if (record.get('confAFCntDown') != null && record.get('confAFCntDown') != "null" && record.get('confAFCntDown') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL confAFCntDown :')+record.get('confAFCntDown')+'</p>';
                                                      if (record.get('confNFCntDown') != null && record.get('confNFCntDown') != "null" && record.get('confNFCntDown') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL confNFCntDown :')+record.get('confNFCntDown')+'</p>';
                                                      if (record.get('confFCntUp') != null && record.get('confFCntUp') != "null" && record.get('confFCntUp') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL confFCntUp :')+record.get('confFCntUp')+'</p>';

                                                      // RDTP 7009
                                                      if (record.get('freq') != null && record.get('freq') != "null" && record.get('freq') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL Frequency (MHz):')+record.get('freq')+'</p>';

                                                      // RDTP 2210
                                                      if (record.get('class') != null && record.get('class') != "null" && record.get('class') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL Current class:')+record.get('class')+'</p>';
                                                      if (record.get('bper') != null && record.get('bper') != "null" && record.get('bper') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL Ping-slot period (seconds):')+record.get('bper')+'</p>';

                                                      // RDTP 2274 
//                                                      if (record.get('customerData') != null && record.get('customerData') != "null" && record.get('customerData') != "")
//                                                         output+='<p>'+com.actility.locale.translate('LBL customerData:')+record.get('customerData')+'</p>';
                                                      if (record.get('tags') != null && record.get('tags') != "null" && record.get('tags') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL tags:')+record.get('tags')+'</p>';

//                                                      if (record.get('asReportDeliveryID') != null && record.get('asReportDeliveryID') != "null" && record.get('asReportDeliveryID') != "")
//                                                         output+='<p>'+com.actility.locale.translate('LBL asReportDeliveryID:')+record.get('asReportDeliveryID')+'</p>';

                                                      if (record.get('asRecipients') != null && record.get('asRecipients') != "null" && record.get('asRecipients') != "")
                                                         output+=record.get('asRecipients');

                                                      output+='<br> ';
                                                      output+='</div>';
                                                      output+='</td></tr>';
                                                      Ext.get(expandRow).setHTML(output);
                                                  },
                                               },
                                            },
                                            columns: [
                                            /*
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'uid',
                                                    fieldLabel: 'Uid',
                                                    labelWidth: 150,
                                                    maxWidth: 100,
                                                    minWidth: 100,
                                                    name: 'uid'
                                                },
                                             */
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 44,
                                                    minWidth: 44,
                                                    dataIndex: 'direction',
                                                    text: '${LBL Direction}',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        var text="";
                                                        if (value != '')
                                                        {
                                                            var val=parseInt(value);
                                                            if (val == 0) {
                                                               
                                                               var status = record.get('asDeliveryStatus');
                                                               if (status == "Error") {
                                                                  metaData.style = "font-weight: bold; font-size: 14pt; color: green;";
                                                                  text+="x";
                                                               } else {
                                                                   metaData.style = "font-weight: bold; font-size: 14pt; color: green;";
                                                                   text+="&uArr;";
                                                               }

                                                            } else if (val == 1) {
                                                              // RDTP 5787
                                                               var repetedUL=record.get('rul');
                                                               if (repetedUL == 1) {
                                                                  metaData.style = "font-weight: bold; font-size: 12pt; color: red;";
                                                                  text+="&darr;&darr;";
                                                                  return text;                                                                
                                                               }

                                                               var DLStatus=record.get('DLStatus');
                                                               if (DLStatus == "1" || DLStatus == "null")
                                                               {
                                                                  metaData.style = "font-weight: bold; font-size: 14pt; color: red;";
                                                                  text+="&dArr;";   
                                                               }
                                                               else
                                                               {
                                                                  metaData.style = "font-weight: bold; font-size: 14pt; color: red;";
                                                                  text+="x";   
                                                               }
                                                            } else if (val == 3) {
                                                               metaData.style = "font-weight: bold; font-size: 14pt; color: blue;";
                                                               text+="&#x2299;";
                                                            } else if (val == 5) {
                                                               metaData.style = "font-weight: bold; font-size: 14pt; color: blue;";
                                                               text+="&#x21BB;";
                                                            } else {
                                                               metaData.style = "font-weight: bold; font-size: 14pt; color: blue;";
                                                               text+="&loz;";
                                                            }

                                                            // NFR 809
                                                            var devEUI=record.get('DevEUI');
                                                            var gwOp=record.get('gwOp');
                                                            var roamingType=record.get('rt');
                                                            if (gwOp != '' && gwOp != null && gwOp != "null") {
                                                              if (roamingType != '' && roamingType != null && roamingType != "null") {
                                                                // new algo
                                                                if (roamingType == 1) { // sNS
                                                                  text+="<span style='font-size: 6pt; display: inline-block; position: relative; top: -3px; left: 4px;'>sPR</span>";
                                                                } else {                // fNS
                                                                  text+="<span style='font-size: 6pt; display: inline-block; position: relative; top: -3px; left: 4px;'>fPR</span>";
                                                                }
                                                              } else {
                                                                // old algo
                                                                if (devEUI != '' && devEUI != null && devEUI != "null") { // sNS
                                                                  text+="<span style='font-size: 6pt; display: inline-block; position: relative; top: -3px; left: 4px;'>sPR</span>";
                                                                } else {                                                  // fNS
                                                                  text+="<span style='font-size: 6pt; display: inline-block; position: relative; top: -3px; left: 4px;'>fPR</span>";
                                                                }
                                                              }
                                                            }

                                                            // NFR 1036
                                                            var mcc=record.get('mcc');
                                                            if (mcc != '' && mcc != null && mcc != "null") {
                                                                text+="<span style='font-size: 6pt; display: inline-block; position: relative; top: -3px; left: 4px;'>m</span>";
                                                            }

                                                            return text;
                                                        }
                                                        return "?"; //value;
                                                    },

                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 40,
                                                    minWidth: 40,
                                                    dataIndex: 'hasMac',
                                                    text: '',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        var text="";
                                                        var MType=record.get('MType');
                                                        if (MType != '' && (MType == 0 || MType == 1 || MType == 6)) {
                                                           metaData.style = "font-size: 7pt;";
                                                           text+=com.actility.locale.translate('LBL join');
                                                        } else {
                                                            if (value != '') {
                                                                var val=parseInt(value);
                                                                if (val == 1)
                                                                {
                                                                   metaData.style = "font-size: 7pt;";
                                                                   text+=com.actility.locale.translate('LBL mac');
                                                                }
                                                            }
                                                            var hasPayload=record.get('hasPayload');
                                                            if (hasPayload != '') {
                                                                var val=parseInt(hasPayload);
                                                                if (val == 1)
                                                                {
                                                                   metaData.style = "font-size: 7pt;";
                                                                   if (text.length > 0)
                                                                       text+="<br/>";
                                                                   text+=com.actility.locale.translate('LBL data');
                                                                }
                                                            }                                                            
                                                        }
                                                        return text;
                                                    },
                                                },
/*
                                                {
                                                    xtype: 'gridcolumn',
                                                    hidden: true,
                                                    maxWidth: 400,
                                                    minWidth: 130,
                                                    dataIndex: 'timestamp',
                                                    text: '${LBL Timestamp}'
                                                },
*/
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 420,
                                                    minWidth: 150,
                                                    dataIndex: 'timestampUTC',
                                                    text: '${LBL UTC Timestamp}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 420,
                                                    minWidth: 150,
                                                    dataIndex: 'timestampLocal',
                                                    text: '${LBL Local Timestamp}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    hidden: (com.actility.global.notNetworkPartnerAccess),
                                                    maxWidth: 400,
                                                    minWidth: 80,
                                                    dataIndex: 'customerId',
                                                    text: '${LBL SubscriberID}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    hidden: (com.actility.global.notNetworkPartnerAccess),
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardSubscriberFiltering(rec.get('customerId'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that subscriber')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 80,
                                                    dataIndex: 'DevAddr',
                                                    text: '${LBL DevAddr}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardDeviceAddrFiltering(rec.get('DevAddr'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that devAddr')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'DevEUI',
                                                    text: '${LBL DevEUI}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardDeviceUIDFiltering(rec.get('DevEUI'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that devEUI')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 100,
                                                    minWidth: 40,
                                                    width: 40,
                                                    defaultWidth: 40,
                                                    dataIndex: 'FPort',
                                                    text: '${LBL FPort}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                                        // NFR 791 UP or LOC
                                                        if (record.get('direction') == 1)
                                                            return "";
                                                        else
                                                            return value;
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'FCnt',
                                                    cls: 'cell-up',
                                                    text: '${LBL FCnt Up}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                       if (record.get('direction') == 1) {
                                                        if (value != '' && value != null && value != "null")
                                                            return value;
                                                        else {
                                                            // NFR 1061 1062
                                                            value = record.get('nFCntDown');
                                                            if (value != '' && value != null && value != "null")
                                                                return value;
                                                            else
                                                                return "";
                                                        }
                                                       } else
                                                           return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'FCnt',
                                                    cls: 'cell-down',
                                                    text: '${LBL FCnt Down}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (record.get('direction') == 1)
                                                            return value;
                                                        else
                                                            return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'aFCntDown',
                                                    cls: 'cell-down',
                                                    text: '${LBL AFCnt Down}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                        {
                                                            var val=parseFloat(value);
                                                            // NFR 887
                                                            //metaData.style = "background-color:"+com.actility.specific.utils.colorRssi(val)+";";
                                                            return value;
                                                        } else
                                                          return "";//value;
                                                    },
                                                    stateId: '50',
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'LrrRSSI',
                                                    text: '${LBL RSSI}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                        {
                                                            var val=parseFloat(value);
                                                            metaData.style = "background-color:"+com.actility.specific.utils.colorSnr(val)+";";
                                                            return value;
                                                        } else
                                                          return "";//value;
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'LrrSNR',
                                                    text: '${LBL SNR}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                        {
                                                            var val=parseFloat(value);
                                                            metaData.style = "background-color:"+com.actility.specific.utils.colorRssi(val)+";";
                                                            return value;
                                                        } else
                                                          return "";//value;
                                                    },
                                                    stateId: '50',
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'LrrESP',
                                                    text: '${LBL ESP}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                        {
                                                            var val=parseFloat(value);
                                                            if (val==0)
                                                            {
                                                               //very special FSK50 is reported as SF
                                                               metaData.style = "background-color: #c5ddf5;";
                                                               return "-";
                                                            }
                                                            metaData.style = "background-color:"+com.actility.specific.utils.colorSf(val)+";";
                                                            return com.actility.locale.translate('LBL SF data')+value;
                                                        } else {

                                                          // NFR 887
                                                          var modulation=record.get('mod');
                                                          var datarate=record.get('dr');
                                                          if (modulation == "1" && datarate != '' && datarate != null && datarate != "null") {
                                                             metaData.style = "background-color: #00FFFF;";                                                          		
                                                             return com.actility.locale.translate('LBL DR data')+datarate;
                                                          }

                                                          return "";//value;
                                                        }
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'SpFact',
                                                    text: '${LBL SF}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'SubBand',
                                                    text: '${LBL SubBand}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {

                                                        var transmissionSlot=record.get('ts');
                                                        // NFR 2543
                                                        if (transmissionSlot != '' && transmissionSlot != null && transmissionSlot != "null") {
                                                            // after 6.0 
                                                            if (value == 'LC255') {
                                                                //very special LC255 is dedicated RX2 logical Channel
                                                                metaData.style = "background-color: #f18b4d;";
                                                                return "RX2";
                                                            } else if (transmissionSlot == '2') {
                                                                metaData.style = "background-color: #f18b4d;";
                                                            } else if (transmissionSlot == '3') {
                                                                metaData.style = "background-color: #c5ddf5;";
                                                            }
                                                            return value;
                                                        } else {
                                                            // Before 6.0 
                                                            if (value == 'LC255') {
                                                                //very special LC255 is dedicated RX2 logical Channel
                                                                metaData.style = "background-color: #f18b4d;";
                                                                return "RX2";
                                                            }
                                                            return value;

                                                        }

                                                    },

                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'Channel',
                                                    text: '${LBL Channel}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    width: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'LrcId',
                                                    text: '${LBL LRC Id}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardLRCIdFiltering(rec.get('LrcId'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that LRC Id')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else  { // NFR 809
                                                          var gwOp=record.get('gwOp');
                                                          if (gwOp != '' && gwOp != null && gwOp != "null") {
                                                            return com.actility.locale.translate('LBL Visited BS');
                                                          } else {
                                                            return "";
                                                          }
                                                        }
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    width: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'Lrrid',
                                                    text: '${LBL LRR Id}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardLRRIdFiltering(rec.get('Lrrid'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that LRR Id')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else
                                                          return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'LrrLAT',
                                                    text: '${LBL LRR Lat}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else
                                                          return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'LrrLON',
                                                    text: '${LBL LRR Long}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else
                                                          return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'DevLrrCnt',
                                                    text: '${LBL LRR GWCnt}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else
                                                          return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    width: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'DevLAT',
                                                    text: '${LBL Device Lat}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        if (value != '' && value != null && value != "null")
                                                          return value;
                                                        else
                                                          return "";
                                                    },
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    width: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'DevLON',
                                                    text: '${LBL Device Lon}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 300,
                                                    minWidth: 60,
                                                    width: 60,
                                                    defaultWidth: 60,
                                                    dataIndex: 'distance',
                                                    text: '${LBL Distance}'
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    hidden: (com.actility.global.mapService=="NONE"),
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardMapLocationClick(rec);
                                                            },
                                                            icon: 'resources/images/map_icon.png',
                                                            tooltip: com.actility.locale.translate('TT Localize LRR and device on a map')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'actioncolumn',
                                                    maxWidth: 30,
                                                    minWidth: 30,
                                                    resizable: false,
                                                    defaultWidth: 30,
                                                    tdCls: 'actionButtonClass',
                                                    hidden: (com.actility.global.mapService=="NONE"),
                                                    items: [
                                                        {
                                                            handler: function(view, rowIndex, colIndex, item, e) {
                                                                var rec = view.getStore().getAt(rowIndex);
                                                                com.actility.specific.extjs.manageDashboardMapPathClick(rec);
                                                            },
                                                            icon: 'resources/images/map_path.png',
                                                            tooltip: com.actility.locale.translate('TT Draw location path')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    width: 70,
                                                    dataIndex: 'mic_hex',
                                                    text: '${LBL MIC}'
                                                }

                                            ],
                                            dockedItems: [
                                                {
                                                    xtype: 'tpkpagging',
                                                    dock: 'bottom',
                                                    width: 360,
                                                    displayInfo: true,
                                                    store: 'dataStore'
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ],
                            listeners: {
                                afterrender: {
                                    fn: me.onDashboardAfterRender,
                                    scope: me
                                }
                            }
                        },
                        {
                            xtype: 'panel',
                            id: 'management',
                            autoScroll: true,
                            items: [
                                {
                                    xtype: 'fieldset',
                                    itemId: 'search',
                                    margin: 10,
                                    padding: 10,
                                    title: 'Search',
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            itemId: 'email',
                                            width: 412,
                                            fieldLabel: 'Email'
                                        },
                                        {
                                            xtype: 'textfield',
                                            itemId: 'customerId',
                                            width: 409,
                                            fieldLabel: 'Customer ID'
                                        },
                                        {
                                            xtype: 'button',
                                            margin: 10,
                                            icon: '',
                                            iconCls: 'search',
                                            text: '${BTN Search}',
                                            listeners: {
                                                click: {
                                                    fn: me.onSearchButtonClick,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'gridpanel',
                                            height: 200,
                                            itemId: 'users',
                                            margin: '0 0 10 0',
                                            autoScroll: true,
                                            resizable: true,
                                            forceFit: true,
                                            store: 'userStore',
                                            viewConfig: {
                                                autoScroll: true
                                            },
                                            columns: [
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'username',
                                                    text: 'Username'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'password',
                                                    text: 'Password'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'description',
                                                    text: 'Description'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'isThingparkSubscriber',
                                                    text: 'IsThingparkSubscriber'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'thingparkSubscriberID',
                                                    text: 'ThingparkSubscriberID'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'thingparkSubscriptionHref',
                                                    text: 'ThingparkSubscriptionHref'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'customerIdList',
                                                    text: 'CustomerIdList'
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'edit',
                                            text: 'Edit',
                                            listeners: {
                                                click: {
                                                    fn: me.onButtonClick,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'add',
                                            text: 'Add',
                                            listeners: {
                                                click: {
                                                    fn: me.onButtonAddClick,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'delete',
                                            text: 'Delete',
                                            listeners: {
                                                click: {
                                                    fn: me.onButtonDeleteClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'fieldset',
                                    hidden: true,
                                    itemId: 'edit',
                                    margin: 10,
                                    padding: 10,
                                    title: 'Edit',
                                    items: [
                                        {
                                            xtype: 'form',
                                            itemId: 'form',
                                            bodyPadding: 10,
                                            layout: {
                                                type: 'vbox',
                                                align: 'stretch'
                                            },
                                            items: [
                                                {
                                                    xtype: 'textfield',
                                                    disabled: true,
                                                    itemId: 'uid',
                                                    fieldLabel: 'Uid',
                                                    labelWidth: 150,
                                                    name: 'uid'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'email',
                                                    fieldLabel: 'Email',
                                                    labelWidth: 150,
                                                    name: 'username',
                                                    listeners: {
                                                        change: {
                                                            fn: me.onEmailChange,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'password',
                                                    fieldLabel: 'Password',
                                                    labelWidth: 150,
                                                    listeners: {
                                                        change: {
                                                            fn: me.onEditPasswordChange,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'passwordEncoded',
                                                    fieldLabel: 'Password (encoded)',
                                                    labelWidth: 150,
                                                    name: 'password'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'description',
                                                    fieldLabel: 'Description',
                                                    hideEmptyLabel: false,
                                                    labelWidth: 150,
                                                    name: 'description'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'isThingparkSubscriber',
                                                    fieldLabel: 'IsThingparkSubscriber',
                                                    hideEmptyLabel: false,
                                                    labelWidth: 150,
                                                    name: 'isThingparkSubscriber'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'thingparkSubscriberID',
                                                    fieldLabel: 'ThingparkSubscriberID',
                                                    hideEmptyLabel: false,
                                                    labelWidth: 150,
                                                    name: 'thingparkSubscriberID'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'thingparkSubscriptionHref',
                                                    fieldLabel: 'ThingparkSubscriptionHref',
                                                    hideEmptyLabel: false,
                                                    labelWidth: 150,
                                                    name: 'thingparkSubscriptionHref'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'customerId',
                                                    fieldLabel: 'Customer Ids',
                                                    labelWidth: 150,
                                                    name: 'customerIdList'
                                                },
                                                {
                                                    xtype: 'displayfield',
                                                    itemId: 'autologinURL',
                                                    fieldLabel: 'Autologin URL',
                                                    labelWidth: 150
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'button',
                                            margin: 10,
                                            icon: '',
                                            iconCls: 'save',
                                            text: 'Save',
                                            listeners: {
                                                click: {
                                                    fn: me.onEditSaveButtonClick,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'button',
                                            margin: 10,
                                            icon: '',
                                            iconCls: 'close',
                                            text: 'Close',
                                            listeners: {
                                                click: {
                                                    fn: me.onEditCloseButtonClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'fieldset',
                                    hidden: true,
                                    itemId: 'create',
                                    margin: 10,
                                    padding: 10,
                                    title: 'Create',
                                    items: [
                                        {
                                            xtype: 'form',
                                            itemId: 'form',
                                            bodyPadding: 10,
                                            layout: {
                                                type: 'vbox',
                                                align: 'stretch'
                                            },
                                            items: [
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'email',
                                                    width: 412,
                                                    fieldLabel: 'Email',
                                                    labelWidth: 150,
                                                    name: 'username'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'password',
                                                    width: 412,
                                                    fieldLabel: 'Password',
                                                    labelWidth: 150,
                                                    listeners: {
                                                        change: {
                                                            fn: me.onCreatePasswordChange,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'passwordEncoded',
                                                    width: 412,
                                                    fieldLabel: 'Password (encoded)',
                                                    labelWidth: 150,
                                                    name: 'password'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'customerId',
                                                    width: 409,
                                                    fieldLabel: 'Customer Ids',
                                                    labelWidth: 150,
                                                    name: 'customerIdList'
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'description',
                                                    width: 409,
                                                    fieldLabel: 'Description',
                                                    hideEmptyLabel: false,
                                                    labelWidth: 150,
                                                    name: 'description'
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'button',
                                            margin: 10,
                                            icon: '',
                                            iconCls: 'save',
                                            text: 'Save',
                                            listeners: {
                                                click: {
                                                    fn: me.onCreateSaveButtonClick,
                                                    scope: me
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'button',
                                            margin: 10,
                                            icon: '',
                                            iconCls: 'close',
                                            text: 'Close',
                                            listeners: {
                                                click: {
                                                    fn: me.onCreateCloseButtonClick,
                                                    scope: me
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'container',
                    region: 'south',
                    height: 30,
                    id: 'footer',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'toolbar',
                            items: [
                                {
                                    xtype: 'container',
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
                                    xtype: 'tbfill'
                                },
                                {
                                    xtype: 'image',
                                    listeners: {
                                        el: {
                                            click: function(){com.actility.specific.extjs.bottomToolbarInfoClick();}
                                        }
                                    },
                                    height: 16,
                                    itemId: 'infoImage',
                                    width: 16,
                                    src: 'resources/images/icon-none.png'
                                },
                                {
                                    xtype: 'label',
//                                  text: '#VERSION#'
                                    text: com.actility.global.version
                                },
                                {
                                    xtype: 'tbtext',
                                    text: '${LBL Copyright}'
                                }
                            ]
                        }
                    ]
                }
            ],
            listeners: {
                resize: {
                    fn: me.onRootViewportResize,
                    scope: me
                }
            }
        });

        me.callParent(arguments);
    },

    onTreePanelItemClick: function(dataview, record, item, index, e, eOpts) {
        com.actility.specific.extjs.manageTreeViewClick(record, e);
    },

    onClearDevAddrClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardDevAddrClearClick();
    },

    onClearDevEUIClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardDevEUIClearClick();
    },

    onClearLRRClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardLRRClearClick();
    },

    onClearLRCClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardLRCClearClick();
    },

    onClearASIDClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardASIDClearClick();
    },

    onClearSubIDClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardSubIDClearClick();
    },

    onClearSubTypeClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardSubTypeClearClick();
    },

    onAutoreloadChange: function(field, newValue, oldValue, eOpts) {
        com.actility.specific.extjs.manageDashboardAutoReloadChange(newValue);
    },

    onCheckboxfieldChange1: function(field, newValue, oldValue, eOpts) {
        com.actility.specific.extjs.manageDashboardExpandAllChange(newValue);
    },

    onRefreshClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardRefreshClick();
    },

    onExportClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardExportClick();
    },

    onMapClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardMapClick();
    },

    onLogoutClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardLogoutClick();
    },

    onDashboardAfterRender: function(component, eOpts) {
        com.actility.specific.extjs.manageDashboardAfterRender();
    },

    onSearchButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageSearch();
    },

    onButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageEdit();
    },

    onButtonAddClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageAdd();
    },

    onButtonDeleteClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageDelete();
    },

    onEmailChange: function(field, newValue, oldValue, eOpts) {
        com.actility.specific.extjs.managementManageEmailChange();
    },

    onEditPasswordChange: function(field, newValue, oldValue, eOpts) {
        com.actility.specific.extjs.managementManagePasswordChange(field,oldValue,newValue);

    },

    onEditSaveButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageEditSave();
    },

    onEditCloseButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageEditClose();
    },

    onCreatePasswordChange: function(field, newValue, oldValue, eOpts) {
        com.actility.specific.extjs.managementManagePasswordChange(field,oldValue,newValue);

    },

    onCreateSaveButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageCreateSave();
    },

    onCreateCloseButtonClick: function(button, e, eOpts) {
        com.actility.specific.extjs.managementManageCreateClose();
    },

    onRootViewportResize: function(component, adjWidth, adjHeight, eOpts) {
        com.actility.specific.extjs.manageResize(adjWidth,adjHeight);
    }

});
