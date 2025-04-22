<template>
    <div class="Map" id="map" style="width: 600px; height: 250px;"></div>
</template>

<script setup>
    import { onMounted } from 'vue';

    onMounted(()=>{
        const BMapGL = window.BMapGL
        // console.log(BMapGL)
        var map =new BMapGL.Map("map")
        var point = new BMapGL.Point(116.404,39.915) // 经度 | 纬度
        map.centerAndZoom(point,15)
        map.enableScrollWheelZoom(true)

        // 创建定位控件
        var locationControl = new BMapGL.LocationControl({
            // 控件的停靠位置（可选，默认左上角）
            anchor: BMAP_ANCHOR_TOP_RIGHT,
            // 控件基于停靠位置的偏移量（可选）
            offset: new BMapGL.Size(20, 20)
        });
        // 将定位控件添加到地图上
        map.addControl(locationControl);

        var zoomControl = new BMapGL.ZoomControl();  // 添加缩放控件
        map.addControl(zoomControl); // 将缩放控件添加到地图上

    })
</script>

<style scoped>

.Map{
    width:500px;
    height: 200px;
}

#map{
    width:500px;
    height: 200px;
}

</style>