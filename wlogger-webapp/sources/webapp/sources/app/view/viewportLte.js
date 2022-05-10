Ext.define('WirelessLogger.view.viewportLte', {
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
                                          value: 'Cellular',
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
                                                    itemId: 'filterIPV4',
                                                    fieldLabel: '${LBL IPV4 Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the IPV4 field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearIPV4Click,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterIMEI',
                                                    fieldLabel: '${LBL IMEI Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the IMEI field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearIMEIClick,
                                                            scope: me
                                                        }
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    itemId: 'filterMTC',
                                                    width: 200,
                                                    fieldLabel: '${LBL MTC Filtering}',
                                                    labelWidth: 110
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '${BTN Clear}',
                                                    tooltip: '${TT Clear the MTC field}',
                                                    listeners: {
                                                        click: {
                                                            fn: me.onClearMTCClick,
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
                                                    store: 'stpLteComboStore',
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
                                            hidden: (!com.actility.global.notNetworkPartnerAccess),
                                            items: [
                                                {
                                                    xtype: 'combobox',
                                                    itemId: 'decoder',
                                                    fieldLabel: '${LBL Decoder}',
                                                    labelWidth: 110,
                                                    value: 'raw',
                                                    store: 'decoderLteComboStore',
                                                    valueField: 'value',
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
                                                    text: '${BTN Map}',
                                                    hidden: (com.actility.global.mapService=="NONE"),
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

                                                      // NFR 1012
                                                      if (record.get('direction') == 4) {
                                                         output+='<p>----------------------------------------<br>';
                                                         output+=com.actility.locale.translate('LBL microflow');
                                                         output+='<br>----------------------------------------</p>';

                                                      }

                                                      if (record.get('mfSzU') != null && record.get('mfSzU') != "null" && record.get('mfSzU') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL uplink bytes :')+record.get('mfSzU')+'</p>';
                                                      if (record.get('mfSzD') != null && record.get('mfSzD') != "null" && record.get('mfSzD') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL downlink bytes :')+record.get('mfSzD')+'</p>';
                                                      if (record.get('mfDur') != null && record.get('mfDur') != "null" && record.get('mfDur') != "")
                                                         output+='<p>'+com.actility.locale.translate('LBL duration (seconds) :')+record.get('mfDur')+'</p>';

                                                      if (record.get('direction') != '4')
                                                        output+='<p>MType : '+record.get('MTypeText')+'</p>';
                                                      if (record.get('MType') == '2')
                                                         output+='<p>Cause : '+record.get('lteCause')+'</p>';
                                                      if (record.get('MType') == '0')
                                                      {
                                                         output+='<p>Data (hex) : '+record.get('payload_hex')+'</p>';
                                                         output+='<p><pre>'+record.get('IPV4Decoded')+'</pre></p>';
                                                         output+='<p>'+record.get('payload_decoded')+'</p>';
                                                      }
                                                      if (record.get('direction') == 0)
                                                      {
                                                         output+='<p>';
                                                         output+='Device [Lat : ' + minusIfNullOrEmpty(record.get('DevLAT'));
                                                         output+=' Lon : ' + minusIfNullOrEmpty(record.get('DevLON'));
                                                         output+=']</p>';
                                                      }
                                                      if (record.get('direction') == 0)
                                                      {
                                                         if (record.get('Late') == 1)
                                                            output+='<p>Reporting Status: Late</p>';
                                                         else
                                                            output+='<p>Reporting Status: On time</p>';
                                                      }
                                                      if (record.get('lteEBI') != null && record.get('lteEBI') != "null" && record.get('lteEBI') != "")
                                                         output+='<p>EBI : '+record.get('lteEBI')+'</p>';
                                                      if (record.get('lteAPN') != null && record.get('lteAPN') != "null" && record.get('lteAPN') != "")
                                                         output+='<p>APN : '+record.get('lteAPN')+'</p>';
                                                      if (record.get('lteMSISDN') != null && record.get('lteMSISDN') != "null" && record.get('lteMSISDN') != "")
                                                         output+='<p>MSISDN : '+record.get('lteMSISDN')+'</p>';
                                                      if (record.get('lteIMSI') != null && record.get('lteIMSI') != "null" && record.get('lteIMSI') != "")
                                                         output+='<p>IMSI : '+record.get('lteIMSI')+'</p>';
                                                      if (record.get('lteRAT') != null && record.get('lteRAT') != "null" && record.get('lteRAT') != "")
                                                         output+='<p>RAT : '+record.get('lteRAT')+'</p>';
                                                      if (record.get('lteCELLID') != null && record.get('lteCELLID') != "null" && record.get('lteCELLID') != "")
                                                         output+='<p>CELLID : '+record.get('lteCELLID')+'</p>';
                                                      if (record.get('lteCELLTAC') != null && record.get('lteCELLTAC') != "null" && record.get('lteCELLTAC') != "")
                                                         output+='<p>CELLTAC : '+record.get('lteCELLTAC')+'</p>';
                                                      if (record.get('lteSERVNET') != null && record.get('lteSERVNET') != "null" && record.get('lteSERVNET') != "")
                                                         output+='<p>SERVNET : '+record.get('lteSERVNET')+'</p>';
                                                      if (record.get('lteMCCMNC') != null && record.get('lteMCCMNC') != "null" && record.get('lteMCCMNC') != "")
                                                         output+='<p>MCCMNC : '+record.get('lteMCCMNC')+'</p>';

                                                      // NFR724
                                                      if (record.get('direction') != 2 && record.get('direction') != '4')
                                                         output+='<p>'+com.actility.locale.translate('LBL ASID :')+record.get('asID')+'</p>';

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
                                               /* {
                                                    xtype: 'gridcolumn',
                                                    dataIndex: 'uid',
                                                    fieldLabel: 'Uid',
                                                    labelWidth: 150,
                                                    maxWidth: 100,
                                                    minWidth: 100,
                                                    name: 'uid'
                                                },*/
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 24,
                                                    minWidth: 24,
                                                    dataIndex: 'direction',
                                                    text: '${LBL Direction}',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        var text="";
                                                        if (value != '')
                                                        {
                                                            var val=parseInt(value);
                                                            if (val == 0)
                                                            {

                                                               var status = record.get('asDeliveryStatus');
                                                               if (status == "Error") {
                                                                  metaData.style = "font-weight: bold; font-size: 14pt; color: green;";
                                                                  text+="x";
                                                               } else {
                                                                  metaData.style = "font-weight: bold; font-size: 14pt; color: green;";
                                                                  text+="&uArr;";
                                                               }
                                                               
                                                            } 
                                                            else if (val == 4)
                                                            {
                                                               metaData.style = "font-weight: bold; font-size: 14pt; color: blue;";
                                                               text+="&#126;";
                                                            } 
                                                            else 
                                                            {
                                                               metaData.style = "font-weight: bold; font-size: 14pt; color: red;";
                                                               text+="&dArr;";   
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
                                                    dataIndex: 'hasPayload',
                                                    text: '',
                                                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                                                        var text="";
                                                        var hasPayload=record.get('hasPayload');
                                                        if (hasPayload != '')
                                                        {
                                                            var val=parseInt(hasPayload);
                                                            if (val == 1)
                                                            {
                                                               metaData.style = "font-size: 7pt;";
                                                               text="data";
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
                                                    maxWidth: 400,
                                                    minWidth: 80,
                                                    dataIndex: 'DevAddr',
                                                    text: '${LBL Sensor IPV4}'
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
                                                                com.actility.specific.extjs.manageDashboardSensorIPV4Filtering(rec.get('DevAddr'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that IPV4')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'DevEUI',
                                                    text: '${LBL IMEI}'
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
                                                                com.actility.specific.extjs.manageDashboardIMEIFiltering(rec.get('DevEUI'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that IMEI')
                                                        }
                                                    ]
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'lteIMSI',
                                                    text: '${LBL IMSI}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'lteMSISDN',
                                                    text: '${LBL MSISDN}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'lteIPPacketSize',
                                                    text: '${LBL IPPacketSize}'
                                                },
                                                {
                                                    xtype: 'gridcolumn',
                                                    maxWidth: 400,
                                                    minWidth: 120,
                                                    dataIndex: 'ltePacketProtocol',
                                                    text: '${LBL Packet Protocol}'
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
                                                    maxWidth: 300,
                                                    minWidth: 70,
                                                    width: 70,
                                                    defaultWidth: 70,
                                                    dataIndex: 'Lrrid',
                                                    text: '${LBL MTC}'
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
                                                                com.actility.specific.extjs.manageDashboardMTCFiltering(rec.get('Lrrid'));
                                                            },
                                                            icon: 'resources/images/filter.png',
                                                            tooltip: com.actility.locale.translate('TT Filter that MTC')
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
//LTE
    onClearIPV4Click: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardSensorIPV4ClearClick();
    },
    onClearIMEIClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardIMEIClearClick();
    },
    onClearMTCClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardMTCClearClick();
    },
//LTE
    onClearLRRClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardLRRClearClick();
    },

    onClearLRCClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardLRCClearClick();
    },

    onClearASIDClick: function(button, e, eOpts) {
        com.actility.specific.extjs.manageDashboardASIDClearClick();
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
