layui.use(['form', 'jquery', 'laydate', 'layer'],function(){
    let form = layui.form;
    let layer = layui.layer;
    let $ = layui.jquery;
    let isOpen = false;

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

    $(".readonly").click(function() {
        // 获取msg
        let msg = $(this).attr("data-msg");
        layer.tips(msg, this, {
            tips: [3, '#2F4056'],
            time: 500
        })
    })

    $("input[name='totalNum']").change(function() {
        // 获取原来的总挂号数
        let oldTotalNum = $("input[name='oldTotalNum']").val();
        let inputNum = $(this).val();
        // 输入的数不能小于原来的数
        if(inputNum < oldTotalNum) {
            isOpen = true;
            layer.msg("总挂号数不能小于之前设置的数量！", {
                end: function() {
                    isOpen = false;
                }
            });
            $(this).val(oldTotalNum);
            return;
        }
    })

    form.on('submit(subBtn)', function(data){
        if(isOpen) {
            return false;
        }
        let paramObj = data.field;
        sendAjax("/admin/visit/updateInfo", "PUT", paramObj, true, true, function(result){
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
})