/**
 * 发送请求
 * @param url           请求地址
 * @param method        请求方式
 * @param param       请求参数
 * @param isToken       是否需要传Token
 * @param isJson        是否将请求参数转换为JSON
 * @param rollFunc      回调函数(可以为空)
 */
function sendAjax(url, method, param, isToken, isJson, rollFunc) {
    let sendObj = {
        url: url,
        type: method,
        async: false,
        dataType: "json",
        success: function (res) {
            if(rollFunc) {
                rollFunc(res)
            } else {
                layer.msg(res.msg, {
                    timer: 1000
                })
            }
        },
        error: function (res) {
            layer.confirm('啊哦！访问出问题了！快找开发狗算账！', {
                btn: ['确定']  //按钮
                , icon: 5
                , anim: 6
            }, function (index) {
                layer.closeAll();
                layer.close(index);
            });
        }
    }
    // 是否携带了请求参数，同时请求是否需要将参数转为JSON
    if(param && isJson) {
        sendObj.data = JSON.stringify(param);
        sendObj.contentType = 'application/json;charset=utf-8'; //设置请求头信息
    } else if(param && !isJson) {
        sendObj.data = param;
        sendObj.contentType = 'application/x-www-form-urlencoded;charset:UTF-8'; //设置请求头信息
    }
    // 是否携带Token请求
    if(isToken) {
        sendObj.beforeSend = function(XMLHttpRequest) {
            let local = window.localStorage;
            if(!local) {
                layer.msg("该浏览器不支持localStorage，获取Token失败！", {
                    timer: 1500
                });
                return;
            }
            let token = local.token;
            if(!token) {
                layer.msg("未获取到Token！请先登录获取Token！", {
                    timer: 1500
                });
                return;
            }
            XMLHttpRequest.setRequestHeader("X-Token", localStorage.token)
            console.log(localStorage.token)
        }
    }
    $.ajax(sendObj)
}