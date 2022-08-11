/**
 * 删除单个信息
 * @param obj
 * @param path
 */
function deleteOne(obj, path) {
    layer.confirm('确定要删除该信息吗', {icon: 3, title:'提示'}, function(index){
        layer.close(index);
        let loading = layer.load();
        // 调用工具类封装的请求方法
        sendAjax(path + "/deleteInfo?ids=" + obj.data.id, "DELETE", null, false, false, function (result) {
            layer.close(loading);
            if (result.code == 0) {
                layer.confirm(result.msg, {
                    btn: ['确定']  //按钮
                    , icon: 6
                }, function (index) {
                    layer.close(index);
                    window.location.reload();
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

/**
 * 批量删除方法
 * @param table
 * @param obj
 * @param path
 * @returns {boolean}
 */
function deleteAll(table, obj, path) {
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
        sendAjax(path + "/deleteInfo?ids=" + ids, "DELETE", null, false, false, function (result) {
            layer.close(loading);
            if (result.code == 0) {
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

/**
 * 绑定回车事件
 */
$(document).keypress(function (e) {
    //如果当前有类似layer.alert的窗体，点击最上层的确定按钮，并且取消所有焦点
    if ($('.layui-layer-btn0').length > 0) {
        $('.layui-layer-btn0').eq($('.layui-layer-btn0').length - 1)[0].click();
        $("*").blur();
    }
});

/**
 * 按钮小tips提示
 * @param dom
 * @param msg
 */
function showTips(dom, msg) {
    layer.tips(msg, dom, {
        tips: [1, '#2F4056']
    });
}

/**
 * 关闭小tips提示
 * @param dom
 * @param msg
 */
function closeTips() {
    layer.closeAll('tips');
}

/**
 * 获取选中ID
 * 处理传入的所有数据的数组，获取数据中的ID
 * @param dataArray
 * @returns {*}
 */
function getIds(dataArray) {
    var resultIds = -1;
    if (dataArray.length == 1) {           //如果获取的选中行只有一行，直接获取数组第一个的ID
        resultIds = dataArray[0].id;
        return resultIds;
    } else if (dataArray.length == 0) {     //如果选中行为0，返回-1
        return resultIds;
    } else if (dataArray.length > 1) {      //如果选中行大于1行，可能是删除，循环处理选中ID
        $.each(dataArray, function () {
            var data = $(this).get(0);
            resultIds += data.id + ",";
        });
        //如果flag已经不再是-1，就将前面的-1截取掉，同时截掉最后一个逗号
        if (resultIds != -1) {
            resultIds = resultIds.substring(2, resultIds.length - 1);
        }
        return resultIds;
    }
}

/**
 * 渲染一个图片层
 */
function createPhotos(photosData) {
    layer.photos({
        photos: photosData
        ,anim: 5
    });
}

/**
 * 为按钮绑定单击事件，打开弹窗
 * @param url       跳转的控制器
 * @param title     窗口标题
 * @param width     窗口宽度(例如：200px)
 * @param height    窗口高度(例如：200px)
 * @param isClose   点击窗体外是否关闭 默认false
 * @param endFunc   弹窗关闭后的响应事件
 * @param closeBtn
 */
function toWindow(url, title, width, height, isClose, endFunc, closeBtn = '1') {
    let openObj = {
        type: 2,
        title: title,
        shadeClose: isClose,
        shade: 0.3,
        // maxmin: true, //开启最大化最小化按钮
        area: [width, height],
        content: url,
        closeBtn: closeBtn
        // skin: 'layui-layer-lan'
    };
    if(endFunc) {
        openObj.end = endFunc;
    }
    layer.open(openObj);
}

/**
 * 初始化一个Layui穿梭框组件
 * @param transfer              Layui的穿梭框对象(transfer模块)
 * @param elemId                要绑定生成穿梭框的DOM元素
 * @param title                 标题, 数组格式, 例如：['活动列表', '已配置的活动']
 * @param leftData              穿梭框左侧展示的数据
 * @param rightData             穿梭框右侧展示的数据
 * @param id                    索引ID
 * @param width                 宽度, 默认 200
 * @param search                是否显示搜索框 true OR false
 * @param height                高度, 默认 360
 */
function createTransfer(transfer, elemId, title, leftData, rightData, id, width, height, search) {
    width = !width ? 200 : width;
    height = !height ? 360 : height;
    transfer.render({
        elem: elemId    //绑定元素
        , title: title
        , data: leftData
        , id: id         //定义索引
        , width: width
        , value: rightData
        , showSearch: search
        , height: height
    });
}

/**
 * 常用的错误弹窗提示
 */
function errorAlert(content, shade, closeBtn) {
    closeBtn = !closeBtn ? 0 : closeBtn;
    shade = !shade ? 0.1 : shade;
    content = !content ? "出现错误啦! " : content
    layer.open({
        title: '错误消息'
        , content: content
        , shade: shade
        , icon: 5
        , anim: 6
        , closeBtn: closeBtn
    })
}

/**
 * 获取项目根目录
 * @returns {string}
 */
function getPath() {
    //获取项目路径
    var curRequestPath = window.document.location.href;
    //获取项目请求路径
    var pathName = window.document.location.pathname;
    var ipAndPort = curRequestPath.indexOf(pathName);
    var localhostPath = curRequestPath.substring(0,ipAndPort);
    var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    basePath = localhostPath + projectName;
    return basePath;
}
// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证身份证号 Start ------------------------------
// -----------------------------------------------------------------------------------------

/**
 * 验证身份证号
 * @param val
 * @returns {string}
 */
function checkID(val) {
    if(checkCode(val)) {
        var date = val.substring(6,14);
        if(checkDate(date)) {
            if(checkProvince(val.substring(0,2))) {
                return "";
            }
        }
    }
    return "身份证输入格式有误！";
}

/**
 * 验证校验码
 * @param val
 * @returns {boolean}
 */
function checkCode(val) {
    var p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
    var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
    var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
    var code = val.substring(17);
    if(p.test(val)) {
        var sum = 0;
        for(var i=0;i<17;i++) {
            sum += val[i]*factor[i];
        }
        if(parity[sum % 11] == code.toUpperCase()) {
            return true;
        }
    }
    return false;
}

/**
 * 验证日期是否合法
 * @param val
 * @returns {boolean}
 */
function checkDate(val) {
    var pattern = /^(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)$/;
    if(pattern.test(val)) {
        var year = val.substring(0, 4);
        var month = val.substring(4, 6);
        var date = val.substring(6, 8);
        var date2 = new Date(year+"-"+month+"-"+date);
        if(date2 && date2.getMonth() == (parseInt(month) - 1)) {
            return true;
        }
    }
    return false;
}

/**
 * 验证省份编号是否正确
 * @param val
 * @returns {boolean}
 */
function checkProvince(val) {
    var pattern = /^[1-9][0-9]/;
    var provs = {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门"};
    if(pattern.test(val)) {
        if(provs[val]) {
            return true;
        }
    }
    return false;
}
// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证身份证号 End --------------------------------
// -----------------------------------------------------------------------------------------



// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证手机号码 Start ------------------------------
// -----------------------------------------------------------------------------------------
/**
 * 验证手机号
 * @param phoneNum
 * @returns {string}
 */
function checkPhone(phoneNum) {
    // 手机号正则
    var rePhone = /^1[3456789]\d{9}$/;
    if(!phoneNum) {
        return "请填写完整信息";
    } else if(!rePhone.test(phoneNum)) {
        return "手机号格式有误！"
    }
}
// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证手机号码 End --------------------------------
// -----------------------------------------------------------------------------------------


// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证数字 Start ------------------------------
// -----------------------------------------------------------------------------------------
/**
 * 验证是否是正整数
 * @param number
 * @param isZero        是否可以为0
 * @returns {string}
 */
function checkInt(number, isZero) {
    // 正整数正则
    var reNumber = /^[0-9]\d*$/;
    if(!isZero) {
        reNumber = /^[1-9]\d*$/;
    }
    if(!number) {
        return "请填写完整信息";
    } else if(!reNumber.test(number)) {
        return "请输入正确的正整数！"
    }
}

/**
 * 验证是否是正数(包括小数和0)
 * @param number
 * @param isZero        是否可以为0
 * @returns {string}
 */
function checkNumber(number, isZero) {
    // 正整数正则
    var reNumber = /^\d+(\.\d+)?$/;
    if(!number) {
        return "请填写完整信息";
    } else if(!reNumber.test(number)) {
        return "请输入正确的正数！"
    } else if(!isZero && number == 0) {
        return "不可以输入0哦！"
    }
}
// -----------------------------------------------------------------------------------------
// -------------------------------------- 正则验证数字 End --------------------------------
// -----------------------------------------------------------------------------------------

/**
 * [将文章里Base64图片转换为本地图片保存并返回文章内容]
 * @param  [pageContents] $pageContents [文章内容]
 * @param  [目录] $path [要保存的路径]
 */
function image_change(content, $path){
    $pageContents = str_replace('\"','"',$pageContents);
    $reg = '/<img (.*?)+src=[\'"](.*?)[\'"]/i';
    preg_match_all( $reg , $pageContents , $results );
    $imgArray=$results[2];
    //打印出来
    for ($i=0; $i < count($imgArray); $i++) {
        $isBase64=base64_image_content($imgArray[$i],$path,$i);
        if($isBase64){
            $pageContents=str_replace($imgArray[$i],$isBase64,$pageContents);
        }
    }
    return $pageContents;
}