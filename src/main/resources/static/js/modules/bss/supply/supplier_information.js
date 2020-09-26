require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'house-select'], function(Vue, element, axios, ajax, vueselect, util, houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function() {
             var checkMobileNo = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('联系人号码为空'));
                }else {
                    if (!Number.isInteger(value)) {
                        if(value.length != 11){
                            callback(new Error('请输入11位正确的手机号码'));
                        }else if(value != undefined && value != 'undefined'){
                            if(!Number.isInteger(value.parseInt())){
                                callback(new Error('请输入11位正确的手机号码'));
                            }
                        }else {
                            callback(new Error('请输入11位正确的手机号码'));
                        }
                    } else {
                        if(value.toString().length != 11){
                            callback(new Error('请输入11位正确的手机号码'));
                        }else {
                            callback();
                        }
                    }
                }
            };
            return {
                queryCond: {
                    id: '',
                    name: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                editSupplierInfo: {
                    name: '',
                    abbreviation: '',
                    mobileNo: '',
                    landline: '',
                    mailbox: '',
                    address: '',
                    contacts: '',
                    standbyTelephone: '',
                },
                addSupplierInfo:{},
                supplierRules: {
                    name: [
                        { required: true, message: '供应商名称不能为空', trigger: 'change' }
                    ],
                    abbreviation: [
                        { required: true, message: '供应商简称不能为空', trigger: 'change' }
                    ],
                    landline: [
                        { required: true, message: '座机不能为空', trigger: 'change' }
                    ],
                    mobileNo: [
                        {required: true, message: '请填写联系人号码', trigger: 'blur'}
                    ],
                    mailbox: [
                        { required: true, message: '邮箱不能为空', trigger: 'change' },
                        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                    ],
                    address: [
                        { required: true, message: '地址不能为空', trigger: 'change' }
                    ],
                    contacts: [
                        { required: true, message: '联系人不能为空', trigger: 'change'}
                    ],
                    verifyPerson: [
                        { required: true, message: '对账人不能为空', trigger: 'change' }
                    ],
                    accountNumber1: [
                        { required: true, message: '银行账号1不能为空', trigger: 'change' }
                    ]
                },
                editSupplierDialogVisible: false,
                addSupplierDialogVisible: false,
                modifyTag: "",
                supplierInfo: []
            }
        },

        methods: {
            querySupplier: function() {
                let that = this;
                ajax.get('api/bss.supply/supplier/queryByNameAndId', this.queryCond, function(responseData){
                    vm.supplierInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            deleteSupplierBatch(){
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的供应商！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }
                let supplierIdList = [];
                val.forEach(v => {
                    supplierIdList.push({supplierId:v.id});
                });
                let that = this;
                ajax.post('api/bss.supply/supplier/deleteSupplierByIds', val, function(responseData){
                    that.querySupplier();
                },null, true);
            },
            deleteSupplierById(supplier){
                let that = this;
                ajax.post('api/bss.supply/supplier/deleteSupplierById', supplier, function(responseData){
                    that.querySupplier();
                },null, true);
            },
            editSupplierById(supplier){
                this.$nextTick(()=>{
                    this.$refs.editSupplierInfo.resetFields();
                });
                this.modifyTag = "2";
                this.editSupplierInfo = JSON.parse(JSON.stringify(supplier));
                this.editSupplierDialogVisible = true;
            },
            submitEdit(editSupplierInfo){
                this.$refs.editSupplierInfo.validate((valid) => {
                    if(valid){
                        let that = this;
                        this.$confirm('是否提交修改供应商数据?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss.supply/supplier/updateSupplierById', this.editSupplierInfo, function(responseData){
                                that.editSupplierDialogVisible = false;
                                let supplierSize = that.supplierInfo.length;
                                that.querySupplier();
                            },null, true);
                        })
                    }
                });
            },
            cancel(){
                let that = this;
                if (that.modifyTag == '2'){
                    that.editSupplierDialogVisible = false;
                }else if (that.modifyTag == '0'){
                    that.addSupplierDialogVisible = false;
                }
            },
            querySupplierBrandBySupplierId(supplierId){
                let that = this;
                ajax.get('api/SupplySupplierBrand/queryBySupplierId', {supplierId:supplierId}, function(responseData){
                    vm.supplierInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            addSupplier(){
                this.modifyTag = "0";
                this.addSupplierDialogVisible = true;
            },
            submitAdd(addSupplierInfo){
                let that = this;
                ajax.post('api/bss.supply/supplier/addSupplier', addSupplierInfo, function(responseData){
                    that.addSupplierDialogVisible = false;
                    that.querySupplier();
                },null, true);
            }
        }
    });

    return vm;
})