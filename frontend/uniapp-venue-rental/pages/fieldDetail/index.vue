<template>
	<view>
		<up-swiper
		    :list="fieldImgs"
		    previousMargin="30"
		    nextMargin="30"
		    circular
		    :autoplay="true"
		    radius="5"
		    bgColor="#ffffff"
			height="220px"
		></up-swiper>
		<div class="field-title">
			<div>
				<h3>{{field.name}}</h3>
				<p style="margin-top: 6px;color: red;">￥{{field.price}}/天</p>
			</div>
			<div style="display: flex; align-items: center;" @click="addStar">
				<up-icon :name="starName" color="#7BB9F5" style="display: inline-block;" size="24"></up-icon>
				<span>收藏</span>
			</div>
		</div>
		<div class="field-info">
			<p style="border-bottom: 1px solid #ccc;padding: 4px;font-weight: bold">场地信息</p>
			<p class="field-info-item">
				<up-icon name="phone" color="#7BB9F5"></up-icon>
				<span>18229488888</span>
			</p>
			<p class="field-info-item" @click="clickMap(field.address,field.positionId)">
				<up-icon name="map" color="#7BB9F5"></up-icon>
				<span style="margin-left: 4px;">{{field.address}}</span>
			</p>
			<div class="Map" id="map" style="width: 100%; height: 200px"></div>
		</div>
		<div class="field-info" style="margin-top: 10px;">
			<p style="border-bottom: 1px solid #ccc;padding: 4px;font-weight: bold">可租用时间</p>
			<div class="dates" style="" ref="datesRef">
				<div 
					class="date" 
					v-for="(d,i) in canRentalDate" 
					:key="i" 
					:class="{'date-active': d,'disable':!d}"
					@click="handleDateClick(datesRef,d,i)"
				>
					{{d?timeCon(i):'不可租'}}
				</div>
			</div>
		</div>
		<div class="field-introduce">
			<p style="border-bottom: 1px solid #ccc;padding: 4px;"><b>场地介绍</b></p>
			<p style="text-indent: 2em;margin: 4px 0;">{{field.description}}</p>
		</div>
		<div class="evaluate">
			<p style="border-bottom: 1px solid #ccc;padding: 4px;">
				<b>评价</b>
				<span style="font-size: 12px;margin-left: 10px;">({{comments.length}}条)</span>
			</p>
			<div class="comment" v-for="comment in comments" :key="comment.id">
				<up-avatar class="avatar" style="margin-top: 4px;" :src="comment.headPortrait" size="30" shape="circle"></up-avatar>
				<div style="margin-left: 4px;">
					<p>
						{{comment.username}}
						<span style="margin-left: 4px; font-size: 10px;color:#a5a5a5;">
							发布于:{{timeconvert(comment.sendTime)}}
						</span>
					</p>
					<up-rate  v-model="comment.rate" size="14" active-color="#55aaff" readonly></up-rate>
					<p style="margin: 4px 0;overflow-wrap: anywhere;">{{comment.content
					}}</p>
				</div>
			</div>
			<up-empty
			    mode="list"
			    icon="http://cdn.uviewui.com/uview/empty/list.png"
				text="暂无评论"
				:show="emptyShow"
			>
			</up-empty>
		</div>
		<up-tabbar
			:value="value6"
			@change="name => value6 = name"
			:fixed="true"
			:placeholder="true"
			:safeAreaInsetBottom="true"
		>
			<up-tabbar-item text="首页" icon="home" @click="toHome"></up-tabbar-item>
			<!-- <up-button class="subscribe" style="height: 100%;" type="primary" :text="field.price == 0?'免费预约':'立即预约'" @click="toOrder"></up-button> -->
			<up-button class="subscribe" style="height: 100%;" v-if="publicStore.canRentalDates.length >= 2" type="primary" :text="field.price == 0?'免费预约':'立即预约'" @click="toOrder"></up-button>
			<up-button class="subscribe" style="height: 100%;" v-else disabled type="primary" text="请预约日期(至少一天)"></up-button>
		</up-tabbar>
		<up-toast ref="uToastRef"></up-toast>
	</view>
</template>

<script setup>
	import { onLoad ,onReady} from "@dcloudio/uni-app"
	import {ref,reactive} from "vue"
	import {
		requestFieldInfoById,
		requestImg,
		getCommentByFieldId,
		getUserById,
		addCollect,
		delCollectByUserId,
		checkIsCollected
	} from "/utils/api.js"
	import { usePublicStore } from "@/stores/public";
	import { showToast } from "../../utils/tools";
	import { getDate, getPosition } from "../../utils/api";
	
	const uToastRef = ref(null)
	
	const publicStore = usePublicStore()
	
	// 场地信息
	const field = ref()
	
	// 场地图片
	const fieldImgs = ref([])
	
	const comments = ref([])
	
	const emptyShow = ref(false)
	
	const timeconvert = (datetimeArr)=>{
		const [year, month, day, hours, minutes, milliseconds] = datetimeArr; 
		return `${year}-${month}-${day} ${hours}:${minutes>=10?minutes:'0'+minutes}`
	}
	
	const timeCon = (value) => {
		const today = new Date()
		today.setTime(today.getTime()+86400000*(value))
		return `${today.getUTCFullYear()}-${today.getMonth()+1}-${today.getDate()}`
	}
	
	const toHome = ()=>{
		// switchTab() 跳转到 tabBar 页面，并关闭其他所有非 tabBar 页面。
		uni.switchTab({
			url:"/pages/index/index"
		})
	}
	
	const datesRef = ref(null)
	
	const firstDate = ref(0) 
	const secondDate = ref(0) 
	const centerDate = ref(0) // 中心点
	
	let fd = 0;
	let sd = 0;
	let cd = 0;
	
	const handleDateClick = async(datesR,d,index)=>{
		
		if(firstDate.value == 0){
			firstDate.value = new Date(datesR.children[index].textContent).getTime()
			centerDate.value = firstDate.value
			if(canRentalDate.value[index]){
				datesR.children[index].className = "date date-active dbg"
			}
			fd = index
			cd = index
			return 
		}
		
		if(new Date(datesR.children[index].textContent).getTime() > centerDate.value){
			firstDate.value = centerDate.value
			fd = cd
			secondDate.value = new Date(datesR.children[index].textContent).getTime()
			sd = index
		}else {
			firstDate.value = new Date(datesR.children[index].textContent).getTime()
			fd = index
			sd = cd
			secondDate.value = centerDate.value
		}
		
		cd = index
		centerDate.value = new Date(datesR.children[index].textContent).getTime()
		
		for(var i =0;i<canRentalDate.value.length;i++) {
			if(canRentalDate.value[i]){
				datesR.children[i].className = "date date-active"
			}
		}
		
		publicStore.setCanRentalDates([])
		
		for(var i =fd ; i <= sd;i++){
			if(canRentalDate.value[i]){
				datesR.children[i].className = "date date-active dbg"
				publicStore.canRentalDates.push(datesR.children[i].textContent)
			}
			
		}

		// console.log(publicStore.canRentalDates)
	}
	
	const toOrder = async ()=>{
		await uni.navigateTo({
			url:`/pages/CreateOrder/index?fieldId=${field.value.id}`
		})
	}
	
	const clickMap = (address,positionId)=> {
		uni.navigateTo({
			url:`/pages/baiduMap/index?address=${address}&pi=${positionId}`
		})
	}
	
	const starName = ref('star') // star | star-fill
	
	const addStar = ()=>{
		const token = uni.getStorageSync("token")
		if(token == '') {
			uni.showToast({
				title: '请先登录!',
				type:'error',
				icon: 'none',
			})
			setTimeout(()=>{
				uni.navigateTo({
					url: "/pages/login/index"
				})
			},900)
			return 
		}
		
		if(starName.value === 'star'){
			// 收藏
			starName.value = 'star-fill'

			addCollect(uni.getStorageSync("userId"),field.value.id).then(res=>{
				if(res.data){
					showToast(uToastRef,{
						type:'success',
						message:'收藏成功~'
					})
				}
			})
			return 
		}
		// 取消收藏
		starName.value = 'star'
		delCollectByUserId(uni.getStorageSync("userId"),field.value.id).then(res=>{
			if(res.data){
				showToast(uToastRef,{
					message:'已取消收藏'
				})
			}
		})
	}
	
	const fieldId = ref('')
	
	const position = reactive({
		lat:0.0, // 纬度
		lng:0.0 // 经度
	})
	
	const canRentalDate = ref([]) // [true,false,true,false,true,true,true,true,true,true,true]
	
	onLoad(async(options)=>{
		fieldId.value = options.fieldId
	})
	
	onReady(async()=>{
		await requestFieldInfoById(fieldId.value).then(res=>{
			field.value = res.data
			field.value.imageId.forEach(id=>{
				requestImg(id).then(res=>{
					fieldImgs.value.push(res.data)
				}).catch(err=>console.log('请求图片失败!'))
			})
		}).catch(err => console.log('获取场地信息失败'))
		const token = uni.getStorageSync("token")
		if(token !== ''){
			await checkIsCollected(fieldId.value).then(res=>{
				if(!res.data){
					starName.value = 'star'
				}else starName.value = 'star-fill'
			})
		}
		await getCommentByFieldId(fieldId.value).then(res=>{
			comments.value = res.data
			if(comments.value.length <= 0) emptyShow.value = true
			comments.value.forEach(comment => {
				getUserById(comment.userId).then(res=>{
					requestImg(res.data.img).then(r => {
						comment.headPortrait = r.data
					})
					comment.username = res.data.username
				})
			})
		})
		
		await getPosition(field.value.positionId).then(res=>{
			Object.assign(position,res.data.position)
		})
		
		await getDate(fieldId.value).then(res => {
			canRentalDate.value = res.data
			
		}).catch(err => console.log('获取可用日期出错'))
		
		// let c = 0;
		// canRentalDate.value.forEach((e,i) => {
		// 	if(e){
		// 		if(c < 2){
		// 			datesRef.value.children[i].className = 'date date-active dbg'
		// 			publicStore.setCanRentalDates(datesRef.value.children[i].textContent)
		// 			c++
		// 		}
		// 	}
		// })
		
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
			title : field.value.name , // 信息窗口标题
			enableMessage:true,//设置允许信息窗发送短息
			message:"亲耐滴，快来吧~"
		}
		var infoWindow = new BMapGL.InfoWindow(`地址：${field.value.address}<br/>联系方式：18229488888`, opts);  // 创建信息窗口对象 
		marker.addEventListener("click", function(){          
			map.openInfoWindow(infoWindow,point); //开启信息窗口
		});
		
	})
</script>

<style scoped>
	.field-title{
		margin:0px 16px;
		margin-top: 12px;
		padding:4px 8px;
		border-radius: 5px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.field-info{
		margin:0px 16px;
		padding:4px 8px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}
	.field-info .field-info-item{
		display: flex;
		align-items: center;
		margin: 8px 0;
	}
	
	.date{
		display: inline-block;
		width: 111px;
		text-align: center;
		border-radius: 100px;
		border: 1px black solid;
		margin-top: 4px;
		margin-right: 8px;
	}
	.date-active{
		border: 1px rgb(123, 185, 245) solid;
		color: rgb(123, 185, 245);
		cursor: pointer;
	}
	.dbg{
		background-color: #e4fcfc;
	}
	.disable{
		background-color: #ccc;
		border:1px solid #ccc;
	}
	.dates:first-child{
		border: 1px rgb(123, 185, 245) solid;
		color: rgb(123, 185, 245);
	}
	.dates{
		height: 150px;
		overflow: auto;
	}
	.field-introduce{
		margin:10px 16px;
		padding:4px 8px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}
	.evaluate{
		margin:10px 16px;
		padding:4px 8px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}
	.evaluate .comment{
		display: flex;
		margin: 6px 0;
	}
	.subscribe{
		height: 100%; 
		border-top-left-radius: 100px;
		border-bottom-left-radius: 100px;
	}
	/*以下deep针对微信开发工具样式问题*/
	:deep(.u-button--square.data-v-461e713c){
		height: 100%;
		border-top-left-radius: 100px;
		border-bottom-left-radius: 100px;
	}
</style>