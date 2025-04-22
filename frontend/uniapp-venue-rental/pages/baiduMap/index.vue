<template>
	<view>
		<div class="Map" id="map" style="width: 100%; height: 100vh"></div>
	</view>
</template>

<script setup>
	import { ref,reactive } from 'vue';
	import { onLoad,onReady,onInit } from "@dcloudio/uni-app"
	import { getPosition } from '../../utils/api';
	
	const pi = ref('')
	
	const address = ref('')
	
	const fieldInfo = reactive({
		id:'',
		fieldName:'',
		des:''
	})
	
	const position = reactive({
		lat:0.0, // 纬度
		lng:0.0 // 经度
	})
	
	/**
	 * zoom 设置地图级别,级别越高，地图的显示就越大
	 * scroll-wheel-zoom 允许鼠标滚轮缩放地图
	 */
	
	onLoad(async(options)=>{
		if(options.pi !== 'null'){
			pi.value = options.pi
		}
		address.value = options.address
	})
	
	onReady(async()=>{
		// 这里涉及同步和异步问题，所以在这里请求
		await getPosition(pi.value).then(res => {
			fieldInfo.id = res.data.id
			fieldInfo.fieldName = res.data.fieldName
			fieldInfo.des = res.data.description
			Object.assign(position,res.data.position)
		})
		
		const BMapGL = window.BMapGL
		var map =new BMapGL.Map("map")
		var point = new BMapGL.Point(position.lng,position.lat) // 经度 | 纬度 
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
		
		// 添加缩放控件
		var zoomControl = new BMapGL.ZoomControl({
		    // 控件的停靠位置（可选，默认左上角）
		    anchor: BMAP_ANCHOR_TOP_RIGHT,
		    // 控件基于停靠位置的偏移量（可选）
		    offset: new BMapGL.Size(20, 80)
		});  
		map.addControl(zoomControl); // 将缩放控件添加到地图上
		
		// 创建点标记
		var marker = new BMapGL.Marker(point);
		map.addOverlay(marker)
		var opts = {
			width : 200,     // 信息窗口宽度
			height: 100,     // 信息窗口高度
			title : fieldInfo.fieldName , // 信息窗口标题
			enableMessage:true,//设置允许信息窗发送短息
			message:"亲耐滴，快来吧~"
		}
		var infoWindow = new BMapGL.InfoWindow(`地址：${address.value}<br/>联系方式：18229488888`, opts);  // 创建信息窗口对象 
		marker.addEventListener("click", function(){          
			map.openInfoWindow(infoWindow,point); //开启信息窗口
		});
	})

</script>

<style scoped>
/* .baidu-map{
    width: 300px;
    height: 300px;
} */
</style>
