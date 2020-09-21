require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee', 'order-payment'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee,payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                customerId: util.getRequest("custId"),
                openId: util.getRequest("openId"),
                partyId: util.getRequest("partyId"),
                customer:{},
                subActiveTab: 'baseInfo',
                activities: [],
                xqltyInfo:[],
                styleInfos:[],
                ltzdsInfo:[],
                activeNames:'',
                dialogTableVisible: false,
                funcDialogVisibleA:false,
                funcDialogVisibleB:false,
                funcDialogVisibleC:false,
                funcDialogVisibleAll:false,
                disabledA:true,
                disabledB:true,
                disabledC:true,
                disabledAll:true,
                urls:[],
                defaultProps: {
                    children: 'children',
                    label: 'title'
                },
                funcA:[],
                funcB:[],
                funcC:[],
            }
        },

        methods: {
            initCustomerInfo : function() {
                let that = this;
                let customerId=that.customerId;
                let partyId=that.partyId;

                if(customerId=='undefined'){
                    customerId=null;
                }
                if(partyId=='undefined'){
                    partyId=null;
                }
                ajax.get('api/bss.customer/customer/getCustomerDetailInfo', {customerId:customerId, openId:this.openId,partyId:partyId}, function(data) {
                    that.customer=data;
                })
            },

            initAction : function() {
                let that = this;
                if(this.customerId=='undefined'){
                    this.customerId=null;
                }
                if(this.partyId=='undefined'){
                    this.partyId=null;
                }

                ajax.get('api/bss.customer/customer/getActionInfo', {customerId:this.customerId, openId:this.openId,partyId:this.partyId}, function(actionData) {
                    that.activities=actionData;
                })
            },

            initXqlty : function() {
                let that = this;
                ajax.get('api/bss.customer/customer/getXQLTYInfo', {openId:this.openId}, function(xqltyData) {
                    that.xqltyInfo=xqltyData;
                })
            },

            initLtzds : function() {
                let that = this;
                ajax.get('api/bss.customer/customer/getLtzdsInfo', {openId:this.openId}, function(ltzdsData) {
                    that.ltzdsInfo=ltzdsData;
                })
            },

            initXqlte : function() {
                let that = this;
                ajax.get('api/bss.customer/customer/getXQLTEInfo', {openId:this.openId}, function(xqlteData) {
                    if(xqlteData!=null){
                        if(xqlteData.styleInfo!=null&&xqlteData.styleInfo.length>0){
                            that.styleInfos=xqlteData.styleInfo;
                        }
                        if(xqlteData.styleUrl!=null && xqlteData.styleUrl.length>0){
                            that.urls=xqlteData.styleUrl;
                        }
                        if(xqlteData.funcInfo!=null&&xqlteData.funcInfo.FUNC_A!=null){
                            that.funcA=xqlteData.funcInfo.FUNC_A;
                            that.disabledA=false;
                            that.disabledAll=false;
                        }
                        if(xqlteData.funcInfo!=null&&xqlteData.funcInfo.FUNC_B!=null){
                            that.funcB=xqlteData.funcInfo.FUNC_B;
                            that.disabledB=false;
                            that.disabledAll=false;
                        }

                        if(xqlteData.funcInfo!=null&&xqlteData.funcInfo.FUNC_C!=null){
                            that.funcC=xqlteData.funcInfo.FUNC_C;
                            that.disabledC=false;
                            that.disabledAll=false;
                        }
                    }



                })
            },

        },

        mounted () {
            this.initCustomerInfo();
            this.initAction();
            this.initXqlty();
            this.initXqlte();
            this.initLtzds();
        },

    });

    return vm;
})