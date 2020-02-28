define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-search-employee', {
        props: ['employee-id','employee-name'],

        data : function(){
            return {
                sEmployeeId: this.employeeId,
                sEmployeeName: this.employeeName,
                datas:[],
                dialogVisible: false,
                searchText:''
            }
        },

        template: `
            <div>
                <el-input v-model="sEmployeeName" placeholder="请选择员工" @focus="popupDialog"></el-input>
                <el-dialog title="选择员工" :visible.sync="dialogVisible">
                    <el-input v-model="searchText" placeholder="请输入员工姓名和电话" @keyup.enter.native="search"><template slot="append"><el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button></template></el-input>
                    <el-table :data="datas" height="400">
                        <el-table-column property="name" label="姓名" width="80"></el-table-column>
                        <el-table-column property="orgPath" label="部门"></el-table-column>
                        <el-table-column property="jobRoleName" label="岗位" width="100"></el-table-column>
                        <el-table-column property="mobileNo" width="120"label="联系电话"></el-table-column>
                        <el-table-column property="mobileNo" width="100"label="选择员工" fixed="right">
                            <template slot-scope="scope">
                                <el-button type="primary" size="mini" @click="handle(scope.row)">选择</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-dialog>
            </div>
            `,

        methods: {
            init() {

            },

            search() {
                let that = this;
                ajax.get('api/organization/employee/searchEmployee', {searchText:this.searchText}, function(data) {
                    that.datas = data;
                })
            },

            popupDialog() {
                this.dialogVisible = true;
            },

            handle(row) {
                this.sEmployeeId = row.employeeId;
                this.sEmployeeName = row.name;
                this.dialogVisible = false;
            }
        },

        watch: {
            employeeId(val) {
                this.sEmployeeId = val;
            },

            sEmployeeId(val, oldValue) {
                if (val != oldValue) {
                    this.$emit("update:employeeId", val);
                }
            },

            employeeName(val){
                this.sEmployeeName = val;
            },

            sEmployeeName(val, oldValue) {
                if (val != oldValue) {
                    this.$emit('update:employeeName', val)
                }
            }
        },

        mounted () {

        }
    });


})