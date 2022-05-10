Ext.define("tpkpagging", {
    alias: "widget.tpkpagging",
    extend: "Ext.toolbar.Paging",
    displayInfo: false,
    displayMsg: "",
    getPagingItems: function() {
        var a = this;
        return [{
            hidden: true,
            itemId: "first",
            tooltip: a.firstText,
            overflowText: a.firstText,
            iconCls: Ext.baseCSSPrefix + "tbar-page-first",
            disabled: true,
            handler: a.moveFirst,
            scope: a
        }, {
            itemId: "prev",
//            tooltip: a.prevText,
//            overflowText: a.prevText,
            tooltip: '${BTN Prev}',
            overflowText: '${BTN Prev}',
            iconCls: Ext.baseCSSPrefix + "tbar-page-prev",
            disabled: true,
            handler: a.movePrevious,
            scope: a
        }, "-", a.beforePageText, {
            xtype: "numberfield",
            itemId: "inputItem",
            name: "inputItem",
            readOnly: true,
            cls: Ext.baseCSSPrefix + "tbar-page-number",
            allowDecimals: false,
            minValue: 1,
            hideTrigger: true,
            enableKeyEvents: true,
            keyNavEnabled: false,
            selectOnFocus: true,
            submitValue: false,
            isFormField: false,
            width: a.inputItemWidth,
            margins: "-1 2 3 2",
            listeners: {
                scope: a,
                keydown: a.onPagingKeyDown,
                blur: a.onPagingBlur
            }
        }, {
            hidden: true,
            xtype: "tbtext",
            itemId: "afterTextItem",
            text: Ext.String.format(a.afterPageText, 1)
        }, "-", {
            itemId: "next",
//            tooltip: a.nextText,
//            overflowText: a.nextText,
            tooltip: '${BTN Next}',
            overflowText: '${BTN Next}',
            iconCls: Ext.baseCSSPrefix + "tbar-page-next",
            disabled: true,
            handler: a.moveNext,
            scope: a
        }, {
            hidden: true,
            itemId: "last",
            tooltip: a.lastText,
            overflowText: a.lastText,
            iconCls: Ext.baseCSSPrefix + "tbar-page-last",
            disabled: true,
            handler: a.moveLast,
            scope: a
        }, "-", {
            itemId: "refresh",
//            tooltip: a.refreshText,
//            overflowText: a.refreshText,
            tooltip: '${BTN Refresh}',
            overflowText: '${BTN Refresh}',
            iconCls: Ext.baseCSSPrefix + "tbar-loading",
            handler: a.doRefresh,
            scope: a
        }];
    },
    moveNext: function() {
        var c = this;
        if (c.store.isLoading()) {
            return;
        }
        var b = c.getPageData().pageCount;
        var a = c.store.currentPage + 1;
        if (a <= b) {
            if (c.fireEvent("beforechange", c, a) !== false) {
                c.store.nextPage();
            }
        }
    },
    getPageData: function() {
        var b = this.store,
            a = b.getCount(),
            reader = b.getProxy().getReader(),
            pageCount = b.currentPage;
        var d = b.getCount() > 0;
        if (d) {
            var c = function(e) {
                a = (b.currentPage - 1) * b.pageSize + b.getCount();
                var more = b.hasMore();
                if(more===true){
                    a = (b.currentPage - 1) * b.pageSize + b.getCount() + 1;
                    pageCount++;
                }
            }(b.getAt(0));
        }
        return {
            total: a,
            currentPage: b.currentPage,
            pageCount: pageCount,
            fromRecord: ((b.currentPage - 1) * b.pageSize) + 1,
            toRecord: Math.min(b.currentPage * b.pageSize, a)
        };
    },
    getStoreListeners: function() {
        return {
            beforeload: this.beforeLoad,
            datachanged: this.onLoad,
            exception: this.onLoadError
        };
    },
});
