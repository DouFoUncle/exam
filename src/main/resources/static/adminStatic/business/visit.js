layui.use(['table', 'form', 'jquery', 'drawer', 'dropdown', 'laydate'], function () {
    let table = layui.table;
    let form = layui.form;
    let $ = layui.jquery;
    let drawer = layui.drawer;
    let dropdown = layui.dropdown;
    let token = window.localStorage.token;
    let laydate = layui.laydate;
    // 该模块的请求路径前缀
    let MODULE_PATH = "/admin/visit/";
    let userJSON = localStorage.userInfo;
    let userInfo;
    let tableUrl = MODULE_PATH + '/getListByPage?token=' + token;
    let toolbar = true;

    //常规用法
    laydate.render({
        elem: '#visitDate'
        ,type: 'date'
    });

    // 创建表格中显示的列
    let cols = [
        [
            {
                type: 'checkbox'
            },
            {
                title: '医生名',
                field: 'doctorName',
                align: 'center',
                minWidth: 160
            },
            {
                title: '排班时间',
                field: 'visitDate',
                align: 'center',
                minWidth: 200
            },
            {
                title: '坐诊开始时间',
                align: 'center',
                minWidth: 200,
                field: 'startTime'
            },
            {
                title: '坐诊结束时间',
                align: 'center',
                minWidth: 200,
                field: 'endTime'
            },
            {
                title: '总挂号数',
                align: 'center',
                minWidth: 120,
                field: 'totalNum',
            },
            {
                title: '已挂号数',
                align: 'center',
                minWidth: 120,
                field: 'useNum',
            },
            {
                title: '剩余可挂',
                align: 'center',
                minWidth: 120,
                field: 'surplusNum',
            },
            {
                title: '挂号费',
                align: 'center',
                minWidth: 120,
                field: 'registerPrice',
                templet: '<div>¥{{d.registerPrice}}</div>'
            }
        ]
    ]

    // 调用初始化方法
    init();

    /**
     * 初始化页面的一些操作
     */
    function init() {
        if(!userJSON) {
            window.location.href = "/";
            return;
        } else {
            userInfo = JSON.parse(userJSON);
            if(userInfo.loginType == 1) {
                tableUrl += "&doctorId=" + userInfo.id
                // 隐藏根据医生名搜索功能
                $("#doctorNameDiv").hide();
                // 增加行内操作按钮
                let operation = {
                    title: '操作',
                    toolbar: '#table-bar',
                    align: 'left',
                    minWidth: 160,
                    fixed: 'right'
                }
                cols[0].push(operation)
                // 显示头部导航条
                toolbar = '#table-toolbar';
            }
        }
    }

    // 创建表格
    table.render({
        elem: '#data-table',
        url: tableUrl,
        page: true,
        limit: 10,
        cols: cols,		// 指定表格中显示的列
        skin: 'line',
        toolbar: toolbar,
        minWidth: 80,
        defaultToolbar: [{
            layEvent: 'refresh',
            title: '刷新表格',
            icon: 'layui-icon-refresh',
        }, 'filter']
    });

    // 控制头部工具条
    table.on('toolbar(data-table)', function (obj) {
        if (obj.event === 'add') {
            // 打开新增弹窗
            window.add();
        } else if (obj.event === 'batchRemove') {
            // 批量删除
            window.batchRemove(obj);
        } else if (obj.event === 'refresh') {
            // 刷新
            window.refresh();
        }
    });

    // 监听行内操作按钮事件
    table.on('tool(data-table)', function (obj) {
        if (obj.event === 'remove') {
            // 删除
            window.remove(obj);
        } else if (obj.event === 'edit') {
            // 修改数据
            window.edit(obj);
        }
    });

    // 查询提交事件
    form.on('submit(form-query)', function (data) {
        table.reload('data-table', {
            where: data.field
        })
        return false;
    });

    // 打开新增窗口
    window.add = function(){
        toWindow("/system" + MODULE_PATH + 'toAddPage?doctorId=' + userInfo.id, "新增排班计划", "750px", "700px", true, null);
    }

    // 打开修改窗口
    window.edit = function(obj){
        toWindow("/system" + MODULE_PATH + 'toEditPage?id=' + obj.data.id, "修改排班计划", "400px", "500px", true, null);
    }

    // 行内工具的冻结按钮绑定
    window.remove = function (obj) {
        layer.confirm('确定要删除数据吗？', {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            let loading = layer.load();
            // 调用工具类封装的请求方法
            sendAjax(MODULE_PATH + "/deleteInfo?token=" + token + "&ids=" + obj.data.id, "DELETE", null, true, false, function (result) {
                layer.close(loading);
                if (result.code == 1) {
                    layer.confirm(result.msg, {
                        btn: ['确定']  //按钮
                        , icon: 6
                    }, function (index) {
                        layer.close(index);
                        table.reload('data-table');    //重载表格
                    });
                } else {
                    layer.confirm(result.msg, {
                        btn: ['确定']  //按钮
                        , icon: 5
                        , anim: 6
                    }, function (index) {
                        layer.close(index);
                    });
                }
            })
        });
    }

    // 批量冻结方法
    window.batchRemove = function (obj) {
        let data = table.checkStatus(obj.config.id).data;
        if (data.length === 0) {
            layer.msg("请先选中要操作的数据！", {
                icon: 3,
                time: 1000
            });
            return false;
        }
        // 调用工具类封装的getIds方法来获取选中数据的ID
        // 最终返回的是 用逗号连接的所有ID
        let ids = getIds(data);
        layer.confirm('确定要删除这些数据吗？', {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            let loading = layer.load();
            // 调用工具类封装的请求方法
            sendAjax(MODULE_PATH + "/deleteInfo?token=" + token + "&ids=" + ids, "DELETE", null, true, false, function (result) {
                layer.close(loading);
                if (result.code == 1) {
                    layer.msg(result.msg, {
                        icon: 1,
                        time: 1000
                    }, function (index) {
                        layer.close(index);
                        table.reload('data-table');    //重载表格
                    });
                } else {
                    layer.msg(result.msg, {
                        icon: 2,
                        time: 1000
                    });
                }
            })
        });
    }

    // 刷新表格数据
    window.refresh = function (param) {
        table.reload('data-table');
    }
})