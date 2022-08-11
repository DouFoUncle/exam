layui.use(['form', 'jquery', 'laydate', 'layer'],function(){
    let form = layui.form;
    let layer = layui.layer;
    let $ = layui.jquery;
    let laydate = layui.laydate;

    //时间选择器
    laydate.render({
        elem: '#visitDate'
        ,type: 'date'
    });

    //时间选择器
    laydate.render({
        elem: '.startDate'
        ,type: 'time'
        ,format: 'HH:mm'
    });

    //时间选择器
    laydate.render({
        elem: '.endDate'
        ,type: 'time'
        ,format: 'HH:mm'
    });

    // 绑定Layui的表单验证
    form.verify({
        // 验证数字是否合法
        num: [
            /^[1-9]\d*$/
            ,'请输入正确的不为0的正整数！'
        ],
        // 验证价格输入是否正确
        money: [
            /^\d+(\.\d+)?$/
            ,'请输入正确的价格！'
        ]
    })

    form.on('submit(subBtn)', function(data){
        let paramObj = data.field;
        sendAjax("/admin/visit/insertInfo", "POST", paramObj, true, false, function(result){
            if(result.code == 1){
                layer.msg(result.msg, {icon: 1, time: 1000}, function () {
                    parent.layer.close(parent.layer.getFrameIndex(window.name));//关闭当前页
                    parent.layui.table.reload("data-table");
                });
            }else{
                layer.msg(result.msg,{icon:2,time:1000});
            }
        })
        return false;
    });

    // 绑定新增排班详情按钮
    $("#addVisit").click(function() {
        let form = $("#form-detail");
        // 获取当前下标
        let index = $("#form-data").attr("data-index");
        // 获取医生ID
        let doctorId = $("input[name='visit[0].doctorId']").val();
        form.append("<div class='visit-detail-item'>\n" +
            "                    <input type='hidden' name='visit["+index+"].doctorId' value='"+doctorId+"'/>" +
            "                    <fieldset class=\"layui-elem-field\">\n" +
            "                        <legend></legend>\n" +
            "                        <div class=\"layui-form-item\">\n" +
            "                            <label class=\"layui-form-label\">\n" +
            "                                开始时间\n" +
            "                            </label>\n" +
            "                            <div class=\"layui-input-inline\">\n" +
            "                                <input type=\"text\" name=\"visit["+index+"].startTime\" lay-verify=\"required\" autocomplete=\"off\" placeholder=\"请选择开始时间\" class=\"layui-input startDate\">\n" +
            "                            </div>\n" +
            "                            <label class=\"layui-form-label\">\n" +
            "                                结束时间\n" +
            "                            </label>\n" +
            "                            <div class=\"layui-input-inline\">\n" +
            "                                <input type=\"text\" name=\"visit["+index+"].endTime\" lay-verify=\"required\" autocomplete=\"off\" placeholder=\"请选择结束时间\" class=\"layui-input endDate\">\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"layui-form-item\">\n" +
            "                            <label class=\"layui-form-label\">\n" +
            "                                可挂号数量\n" +
            "                            </label>\n" +
            "                            <div class=\"layui-input-inline\">\n" +
            "                                <input type=\"text\" name=\"visit["+index+"].totalNum\" lay-verify=\"required|num\" autocomplete=\"off\" placeholder=\"请输入可挂号数量\" class=\"layui-input\">\n" +
            "                            </div>\n" +
            "                            <label class=\"layui-form-label\">\n" +
            "                                挂号费\n" +
            "                            </label>\n" +
            "                            <div class=\"layui-input-inline\">\n" +
            "                                <input type=\"text\" name=\"visit["+index+"].registerPrice\" lay-verify=\"required|money\" autocomplete=\"off\" placeholder=\"请输入挂号费\" class=\"layui-input\">\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"layui-form-item\">\n" +
            "                            <div class=\"layui-input-block\" style=\"text-align: right\">\n" +
            "                                <button class=\"pear-btn pear-btn-sm pear-btn-danger deleteItem\" style=\"text-align: center; margin-right: 24px\">删除</button>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </fieldset>\n" +
            "                </div>");
        // 为新增的元素绑定时间控件
        form.find(".startDate").each(function() {
            //时间选择器
            laydate.render({
                elem: this
                ,type: 'time'
                ,format: 'HH:mm'
            });
        })
        // 为新增的元素绑定时间控件
        form.find(".endDate").each(function() {
            //时间选择器
            laydate.render({
                elem: this
                ,type: 'time'
                ,format: 'HH:mm'
            });
        })
        // 为新增的元素绑定删除事件
        form.find(".deleteItem").each(function() {
            $(this).unbind("click");
            let than = $(this);
            $(this).click(function() {
                deleteItem(than)
            });
        })
        // 在每次动态生成laydate组件时, laydate框架会给input输入框增加一个lay-key="1",
        // 这样就导致了多个laydate 的inpute框都有lay-key="1"这个属性。导致时间控件不起作用，
        // 需要把动态生成的lay-key属性删除
        form.find(".startDate").removeAttr("lay-key");
        form.find(".endDate").removeAttr("lay-key");
        // 设置新的index
        $("#form-data").attr("data-index", parseInt(index) + 1)
    })

    // 为删除按钮绑定删除事件
    $(".deleteItem").click(function () {
        deleteItem(this)
    })

    // 删除本组排班详情
    function deleteItem(elm) {
        // 最少要保留一个排班详情
        let visitDetailItem = $("#form-detail").find(".visit-detail-item");
        if(visitDetailItem.length == 1) {
            layer.msg("最少要保留一组排班详情！")
            return;
        }
        // 删除本组排班详情
        $(elm).parent().parent().parent().parent().remove()
    }
})