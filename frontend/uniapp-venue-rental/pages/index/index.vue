<template>
	<view class="home">
		<up-sticky offset-top="6">
			<view class="search">
				<up-search v-model="searchContent" placeholder="请输入场地相关关键字" shape="round" :showAction="false" searchIconColor="#7BB9F5"  @search="handleSearch" @clear="handleSearchClear"></up-search>
			</view>
		</up-sticky>
		<view class="carousel" v-if="carouselRender">
			<up-swiper
			    :list="carouselList"
			    indicator
			    indicatorMode="line"
			    circular
				height="155"
				@click="handleSwiperClick"
			></up-swiper>
		</view>
		<view class="content">
			<div class="field" v-for="field in fields" :key="field.title">
				<up-image :show-loading="true" radius="10" :src="field.src" width="170px" height="110px" @click="clickField(field.id)"></up-image>
				<span class="fieldName" @click="clickField(field.id)">{{field.name}}</span>
				<p class="filedAddress" @click="clickMap(field.address,field.positionId)">
					<up-icon name="map"></up-icon>
					<span class="text">{{field.address}}</span>
				</p>
			</div>
			<up-empty
				style="width:100%;margin: 0 auto;"
			    mode="search"
			    icon="http://cdn.uviewui.com/uview/empty/search.png"
				:show="empty"
			>
			</up-empty>
		</view>
		<up-loadmore v-show="loadmoreShow" :status="loadmoreStatus" loading-text="努力加载中..." loadmore-text="轻轻上拉加载~" nomore-text="小主实在没有啦~"  />
	</view>
</template>

<script setup>
	import {ref} from "vue";
	import {requestCarousel,requestFieldInfo,requestImg,requestSearch} from "/utils/api.js"
	import { onLoad,onReachBottom } from "@dcloudio/uni-app"
		
	//轮播数据
	const carouselList = ref([]); 
	
	// 场地信息
	const fields = ref([])
	
	// 轮播图是否显示
	const carouselRender = ref(true)
	
	// 内容为空 组件是否显示
	const empty = ref(false)
	
	// 加载更多组件 是否显示
	const loadmoreShow = ref(false)
	
	// 搜索内容
	const searchContent = ref('')
	
	// 主页分页信息
	const pagingInfo = {
		page:0,
		size:8
	}
	
	const swiperLinks = [
		'https://re.jd.com/search?keyword=%e7%bb%bf%e8%89%b2%e6%9c%89%e6%9c%ba%e9%a3%9f%e5%93%81&keywordid=907891232601&re_dcp=202m0QjIIg==&traffic_source=1004&enc=utf8&cu=true&utm_source=baidu-search&utm_medium=cpc&utm_campaign=t_262767352_baidusearch&utm_term=907891232601_0_c67aefe067064a7397c1238f685f4f73',
		'https://www.tmall.com/',
		'https://www.cshelong.com/'
	]
	
	const handleSwiperClick = (index)=>{
		window.open(swiperLinks[index],'_blank')
	}
	
	// 加载更多组件的状态
	const loadmoreStatus = ref('loadmore')
	
	/**
	 * 处理 搜索里的 数据懒加载
	 */
	const handleScrollSearch = async (sc) => {
		if(sc.trim() == '') return ;
		let temp = fields.value.length // 记录获取新数据之前的数据长度
		
		loadmoreStatus.value = 'loading'
		await requestSearch(sc,pagingInfo).then(res=>{
			fields.value.push(...res.data)
			
			if(res.data.length >= pagingInfo.size) loadmoreStatus.value = 'loadmore'
			else loadmoreStatus.value = 'nomore'
			
			fields.value.forEach(field =>{
				requestImg(field.imageId[0]).then(res=>{
					field.src = res.data
				}).catch(err=>console.log('请求图片失败!'))
			})
		}).catch(err=>console.log('搜索失败!'))
	}
	
	/**
	 * 处理 按了回车键的搜索，差不多就是第一次搜索吧
	 */
	const handleSearch = async (sc) => {
		if(sc.trim() == '') return ;
		
		/**
		 *  做搜索的前置操作
		 */
		carouselRender.value = false
		fields.value = [] // 清空数据
		pagingInfo.page = 0
		
		loadmoreStatus.value = 'loading'
		await requestSearch(sc,pagingInfo).then(res=>{
			fields.value.push(...res.data)
			console.log(fields.value.length)
			if(fields.value.length <= 0) {
				// 无数据时显示empty组件 隐藏 加载更多组件
				empty.value = true 
				loadmoreShow.value = false
				// return
			}else {
				empty.value = false
				loadmoreShow.value = true
			}
			
			if(res.data.length >= pagingInfo.size) loadmoreStatus.value = 'loadmore'
			else loadmoreStatus.value = 'nomore'
			
			fields.value.forEach(field =>{
				requestImg(field.imageId[0]).then(res=>{
					field.src = res.data
				}).catch(err=>console.log('请求图片失败!'))
			})
		}).catch(err=>console.log('搜索失败!'))
	}
	
	const handleSearchClear = ()=>{
		carouselRender.value = true
		empty.value = false
		pagingInfo.page = 0
		fields.value = [] // 清空数据
		getFieldInfo()
	}
	
	const getFieldInfo = async()=>{
		loadmoreStatus.value = 'loading'
		await requestFieldInfo(pagingInfo).then(res=>{
			fields.value.push(...res.data) // 方便数据懒加载

			if(fields.value.length <= 0) {
				empty.value = true
				loadmoreShow.value = false
			}else {
				empty.value = false
				loadmoreShow.value = true
			}
			
			if(res.data.length >= pagingInfo.size) loadmoreStatus.value = 'loadmore'
			else loadmoreStatus.value = 'nomore'
			
			// 加载图片
			fields.value.forEach(field =>{
				requestImg(field.imageId[0]).then(res=>{
					field.src = res.data
				}).catch(err=>console.log('请求图片失败!'))
			})
		}).catch(err=>console.log('请求场地信息失败!'))
	}
	
	onLoad(()=>{
		requestCarousel().then(res=>{
			carouselList.value = res.data
		}).catch(err=>{
			console.log('获取轮播图错误!')
		})
			
		getFieldInfo()
	})
	
	onReachBottom(()=>{
		if(loadmoreStatus.value == 'nomore') return
		pagingInfo.page++
		// 由 searchContent 来决定执行获取主页的数据还是搜素数据
		if(searchContent.value.trim() == '') getFieldInfo()
		else handleScrollSearch(searchContent.value)
	})

	const clickField = (fieldId) => {
	  // 点击事件处理逻辑
	  uni.navigateTo({
	  	url:`/pages/fieldDetail/index?fieldId=${fieldId}`
	  })
	};
	
	const clickMap = (address,positionId)=> {
		uni.navigateTo({
			url:`/pages/baiduMap/index?address=${address}&pi=${positionId}`
		})
	}
</script>

<style scoped>
	h1,h2,h3,h4,h5,h6{
		margin: 0;
	}
	.home{
		padding: 6px 12px;
	}
	.home .search{
		margin-bottom: 10px;
	}
	.home .content{
		display: flex;
		flex-wrap: wrap;
		justify-content:space-between;
		margin: 12px 6px;
	}
	.home .content .field{
		margin-bottom: 12px;
		min-width: 170px;
		max-width: 170px;
	}
	.home .content .field .fieldName{
		display: inline-block;
		margin-top: 6px;
		margin-left: 2px;
		font-weight: bold;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.home .content .field .filedAddress{
		display: flex;
		align-items: center;
		margin-left: 2px;
		margin-top: 0px; 
		font-size: 10px;
		color: rgb(144, 147, 153);
	}
	.home .content .field .filedAddress .text{
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
	:deep(.u-empty.data-v-bd84101d){
		margin: 0 auto;
	}
</style>
