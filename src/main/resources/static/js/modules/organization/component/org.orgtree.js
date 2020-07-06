define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('org-orgtree', {
        props: ['org-id','org-name', 'mode'],

        data : function(){
            return {
                sOrgId: this.orgId,
                sOrgName: this.orgName,
                datas:[],
                dialogVisible: false,
                showCheckbox: this.mode=='multi'?true:false,
                expandKey: [],
                defaultProps: {
                    children: 'children',
                    label: 'title'
                }
            }
        },

        template: `
            <div>
                <el-input v-model="sOrgName" placeholder="请选择部门" @focus="popupDialog"></el-input>
                <el-dialog title="选择部门" :visible.sync="dialogVisible" append-to-body height="300px">
                    <div style="height: 50vh;overflow: auto;">
                        <el-tree :data="datas" 
                            :props="defaultProps"
                            node-key="id"
                            ref="orgTree"
                            highlight-current
                            check-on-click-node
                            :show-checkbox="showCheckbox"
                            :default-expanded-keys="expandKey"
                            @node-click="handleNodeClick"></el-tree>
                    
                    </div>
                    <span slot="footer" class="dialog-footer" v-if="mode == 'multi'">
                        <el-button type="primary" @click="confirm">确定</el-button>
                    </span>
                    
                </el-dialog>
            </div>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/organization/org/listWithTree', null, function(data) {
                    that.datas = data;
                    if (data) {
                        data.forEach(temp=>{
                            that.expandKey.push(temp.id);
                        })

                    }
                })
            },

            popupDialog() {
                this.dialogVisible = true;
            },

            handleNodeClick(data) {
                if (this.mode == 'multi') {
                    return;
                }
                this.sOrgId = data.id;
                this.sOrgName = data.path;
                this.dialogVisible = false;
            },

            confirm() {
                let selectNodes = this.$refs.orgTree.getCheckedNodes();
                if (selectNodes) {
                    let orgIds = [];
                    let orgNames = [];

                    selectNodes.forEach(node => {
                        orgIds.push(node.id);
                        orgNames.push(node.path);
                    })

                    this.sOrgId = orgIds.join();
                    this.sOrgName = orgNames.join('/');
                }
                this.dialogVisible = false;
            }
        },

        watch: {
            orgId(val) {
                this.sOrgId = val;
            },

            sOrgId(val, oldValue) {
                if (val != oldValue) {
                    this.$emit("update:orgId", val);
                }
            },

            orgName(val){
                this.sOrgName = val;
            },

            sOrgName(val, oldValue) {
                if (val != oldValue) {
                    this.$emit('update:orgName', val)
                }
            }
        },

        mounted () {
            this.init();
        }
    });


})