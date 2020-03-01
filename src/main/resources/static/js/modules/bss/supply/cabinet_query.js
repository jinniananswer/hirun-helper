require(['vue', 'ELEMENT', 'vxe-table', 'axios'], function (Vue, element, vxetable, axios) {
    // 橱柜查询
    Vue.use(vxetable)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                sexList: [
                    {'label': '女', 'value': '0'},
                    {'label': '男', 'value': '1'}
                ],
                statusList: [
                    {'label': '在职', 'value': '0'},
                    {'label': '离职', 'value': '1'}
                ],
                tableData: [
                    {
                        id: 1,
                        name: "小王",
                        age: 37,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/27",
                        salary: 123123.12,
                        idCard: '430103198207271000'
                    },
                    {
                        id: 2,
                        name: "小绿",
                        age: 36,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/26",
                        salary: 123321,
                        idCard: '430103198207261000'
                    },
                    {
                        id: 3,
                        name: "小红",
                        age: 35,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/25",
                        salary: 818212.32,
                        idCard: '430103198207251000'
                    },
                    {
                        id: 4,
                        name: "小花",
                        age: 34,
                        sex: '0',
                        status: '1',
                        birthday: "1982/7/24",
                        salary: 123123,
                        idCard: '430103198207241000'
                    },
                    {
                        id: 5,
                        name: "小橙",
                        age: 33,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 6,
                        name: "小黄",
                        age: 33,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 7,
                        name: "小青",
                        age: 33,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 8,
                        name: "小紫",
                        age: 33,
                        sex: '0',
                        status: '1',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 9,
                        name: "小赵",
                        age: 33,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 10,
                        name: "小钱",
                        age: 33,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 11,
                        name: "小孙",
                        age: 33,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 12,
                        name: "小李",
                        age: 33,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 13,
                        name: "小周",
                        age: 33,
                        sex: '1',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                    {
                        id: 14,
                        name: "小吴",
                        age: 33,
                        sex: '0',
                        status: '0',
                        birthday: "1982/7/23",
                        salary: 123123,
                        idCard: '430103198207231000'
                    },
                ],
                validRules: {
                    name: [
                        {required: true, message: '必填'},
                        {min: 2, max: 10, message: '名称长度在 2 到 10 个字符'}
                    ],
                    age: [
                        {required: true, message: '必填'},
                        {pattern: /^[1-9][0-9]?$/, message: '格式不正确'}
                    ],
                    idCard: [
                        {required: true, message: '必填'},
                        {pattern: /^(\d{15}$)|(^\d{18}$)/, message: '格式不正确'}
                    ]
                }
            }
        },

        methods: {
            changeEvent() {

            },
            save() {
                let updateRecords = vm.$refs.xTable.getUpdateRecords()
                console.log(updateRecords)
            },
            footerMethod({columns, data}) {
                return [
                    columns.map((column, columnIndex) => {
                        if (columnIndex === 0) {
                            return '汇总'
                        }
                        if (['age'].includes(column.property)) {
                            let total = 0;
                            data.forEach(function (v, k) {
                                total += parseInt(v.age);
                            })
                            return "平均: " + (total / data.length).toFixed(2)
                        }
                        if (['salary'].includes(column.property)) {
                            let total = 0;
                            data.forEach(function (v, k) {
                                total += parseInt(v.salary);
                            })
                            return "合计: " + total.toFixed(2)
                        }
                        return '-'
                    })
                ]
            },

        },
        created: function () {

        },
    });
})