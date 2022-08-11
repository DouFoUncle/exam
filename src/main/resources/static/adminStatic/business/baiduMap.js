$(function () {
    var map = new BMap.Map("container");
    var point = new BMap.Point(longitude ? longitude : 116.331398, latitude ? latitude : 39.897445);
    map.centerAndZoom(point, 16);
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    map.addOverlay(new BMap.Marker(point));//标出所在地
    var geolocation = new BMap.Geolocation();

    // 初始化地图 获取当前位置
    if(!longitude && !latitude) {
        geolocation.getCurrentPosition(function(r){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                var mk = new BMap.Marker(r.point);
                map.addOverlay(mk);//标出所在地
                map.panTo(r.point);//地图中心移动
                var point = new BMap.Point(r.point.lng,r.point.lat);//用所定位的经纬度查找所在地省市街道等信息
                var gc = new BMap.Geocoder();
                // 设置经纬度
                longitude = r.point.lng;
                latitude = r.point.lat;
                gc.getLocation(point, function(rs){
                    submitAddress = rs.address
                });
            }else {
                alert('failed'+this.getStatus());
            }
        },{enableHighAccuracy: true})
    }

    // 给地图添加点击事件，点击地图重新绘制标记点
    map.addEventListener("click",function(e){
        map.clearOverlays();
        var lng=e.point.lng;
        var lat=e.point.lat;
        var geoc = new BMap.Geocoder();
        // 设置经纬度
        longitude = e.point.lng;
        latitude = e.point.lat;
        //创建标注位置
        var pt = new BMap.Point(lng, lat);
        //var myIcon = new BMap.Icon("./img/icon_address.png", new BMap.Size(100,100));
        map.addOverlay(new BMap.Marker(pt));              // 将标注添加到地图中
        geoc.getLocation(pt, function(rs){
            submitAddress = rs.address
        });
    });

    // 搜索后重新构建标注点
    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        var geoc = new BMap.Geocoder();
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            // 设置经纬度
            longitude = pp.lng;
            latitude = pp.lat;
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
            geoc.getLocation(pp, function(rs){
                submitAddress = rs.address
            });
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }

    function G(id) {
        return document.getElementById(id);
    }

    // 建立一个自动完成的对象
    var ac = new BMap.Autocomplete(
        {
            "input" : "suggestId"   // 绑定input框的ID
            , "location" : map
        }
    );

    // 鼠标放在搜索框中的下拉列表上的事件
    ac.addEventListener("onhighlight", function(e) {
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    // 鼠标点击搜索框中的下拉列表后的事件
    var myValue;
    ac.addEventListener("onconfirm", function(e) {
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        setPlace();
    });

    $("#getMap").click(function() {
        console.log("纬度" + latitude)
        console.log("经度" + longitude)
    })

    $("#resetBtn").click(function() {
        $("#suggestId").val('')
    })
})

