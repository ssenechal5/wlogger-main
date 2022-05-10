Ext.define('WirelessLogger.model.dataModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'uid'
        },
        {
            name: 'direction'
        },
        {
            name: 'timestamp'
        },
        {
            name: 'timestampUTC',
            type: 'string'
        },
        {
            name: 'timestampLocal',
            type: 'string'
        },
        {
            name: 'DevEUI'
        },
        {
            name: 'FPort'
        },
        {
            name: 'FCnt'
        },
        {
            name: 'hasPayload'
        },
        {
            name: 'payload_hex'
        },
        {
            name: 'payload_decoded'
        },
        {
            name: 'rawMacCommands'
        },
        {
            name: 'mic_hex'
        },
        {
            name: 'LrrRSSI'
        },
        {
            name: 'LrrSNR'
        },
        {
            name: 'LrrESP'
        },
        {   
            name: 'LRRList'
        },
        {
            name: 'SpFact'
        },
        {
            name: 'SubBand'
        },
        {
            name: 'Channel'
        },
        {
            name: 'LrcId'
        },
        {
            name: 'Lrrid'
        },
        {
            name: 'LrrLAT'
        },
        {
            name: 'LrrLON'
        },
        {
            name: 'DevLrrCnt'
        },
        {
            name: 'DevLAT'
        },
        {
            name: 'DevLON'
        },
        {
            name: 'DevLocRadius'
        },
        {
            name: 'DevLocTime'
        },
        {
            name: 'DevAlt'
        },
        {
            name: 'DevAltRadius'
        },
        {
            name: 'DevAcc'
        },
        {
            name: 'customerId'
        },
        {
            name: 'lrcRequestedRxDelay'
        },
        {
            name: 'DevAddr'
        },
        {
            name: 'ADRbit'
        },
        {
            name: 'ADRAckReq'
        },
        {
            name: 'AckRequested'
        },
        {
            name: 'ACKbit'
        },
        {
            name: 'FPending'
        },
        {
            name: 'MType'
        },
        {
            name: 'MTypeText'
        },
        {
            name: 'hasMac'
        },
        {
            name: 'MACDecoded'
        },
        {
            name: 'AirTime'
        },
        {
            name: 'DLStatus'
        },
        {
            name: 'DLFailedCause1'
        },
        {
            name: 'DLFailedCause2'
        },
        {
            name: 'DLFailedCause3'
        },
        {
            name: 'DLStatusText'
        },
        {
            name: 'DLFailedCause1Text'
        },
        {
            name: 'DLFailedCause2Text'
        },
        {
            name: 'DLFailedCause3Text'
        },
        {
            name: 'distance'
        },
        {
            name: 'Late'
        },
        {
            name: 'lteIPPacketSize'
        },
        {
            name: 'lteCause'
        },
        {
            name: 'lteEBI'
        },
        {
            name: 'lteAPN'
        },
        {
            name: 'lteMSISDN'
        },
        {
            name: 'lteIMSI'
        },
        {
            name: 'lteRAT'
        },
        {
            name: 'lteCELLID'
        },
        {
            name: 'lteCELLTAC'
        },
        {
            name: 'lteSERVNET'
        },
        {
            name: 'lteMCCMNC'
        },
        {
            name: 'IPV4Decoded'
        },
        {
            name: 'ltePacketProtocol'
        },
        {
            name: 'SolvLAT'
        },
        {
            name: 'SolvLON'
        },
        {
            name: 'PayloadSize'
        },
        {
            name: 'gwOp'
        },
        {
            name: 'gwID'
        },
        {
            name: 'gwTk'
        },
        {
            name: 'ismb'
        },
        {
            name: 'mod'
        },
        {
            name: 'dr'
        },
        {
            name: 'DevNorthVelocity'
        },
        {
            name: 'DevEastVelocity'
        },
        {
            name: 'asID'
        },
        {
            name: 'mcc'
        },
        {
            name: 'tss'
        },
        {
            name: 'lcn'
        },
        {
            name: 'lsc'
        },
        {
            name: 'lfc'
        },
        {
            name: 'rt'
        },
        {
            name: 'rr'
        },
        {
            name: 'mfSzU'
        },
        {
            name: 'mfSzD'
        },
        {
            name: 'mfDur'
        },
        {
            name: 'aFCntDown'
        },
        {
            name: 'nFCntDown'
        },
        {
            name: 'confAFCntDown'
        },
        {
            name: 'confNFCntDown'
        },
        {
            name: 'confFCntUp'
        },
        {
            name: 'lfd'
        },
        {
            name: 'ts'
        },
        {
            name: 'rul'
        },
        {
            name: 'freq'
        },
        {
            name: 'class'
        },
        {
            name: 'bper'
        },
        {
            name: 'notifTp'
        },
        {
            name: 'resetTp'
        },
        {
            name: 'payload_driver_id'
        },
        {
            name: 'payload_driver_model'
        },
        {
            name: 'payload_driver_application'
        },
        {
            name: 'payload_decoding_error'
        },
        {
            name: 'lcMU'
        },
        {
            name: 'payload_encryption'
        },
        {
            name: 'customerData'
        },
        {
            name: 'tags'
        },
        {
            name: 'foreignOperatorNSID'
        },
        {
            name: 'rfRegion'
        },
        {
            name: 'asReportDeliveryID'
        },
        {
            name: 'asRecipients'
        },
        {
            name: 'asDeliveryStatus'
        }
    ]
});
