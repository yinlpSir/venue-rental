<template>
	<view class="collects" v-for="(collect,index) in collects" :key="index">
		<div class="collect" @click="toFieldDetail(collect.id)">
			<div style="display: flex;align-items: flex-start;">
				<up-image :show-loading="true" radius="10" :src="collect.img" width="100" height="80"></up-image>
				<div style="font-size: 14px;margin-left: 8px;">
					<h3 style="margin-bottom: 6px;">{{collect.name}}</h3>
					<p style="color: #959595;margin-bottom: 2px;margin-left: 3px;">{{collect.commentCount}}条评论</p>
					<p style="display: flex;font-size: 12px;">
						<up-icon name="map" color="#7BB9F5"></up-icon>
						<span style="color: #959595;">{{collect.address}}</span>
					</p>
				</div>
			</div>
			<div style="display: flex;align-items: center;">
				<span style="font-weight: bold;font-size: 16px;">￥{{collect.price}}</span>/天
			</div>
		</div>
	</view>
	<up-empty
	        mode="data"
	        icon="http://cdn.uviewui.com/uview/empty/data.png"
			text="暂无收藏"
			:show="emptyShow"
	/>
</template>

<script setup>
	import { onLoad,onShow } from "@dcloudio/uni-app"
	import {getCollectByUserId,requestFieldInfoById,requestImg,getCommentByFieldId} from "@/utils/api.js"
	import {ref} from "vue"
	
	const emptyShow = ref(false)
	
	const collects = ref([])
	
	const toFieldDetail = (fieldId)=>{
		uni.navigateTo({
			url:`/pages/fieldDetail/index?fieldId=${fieldId}`
		})
	}
	
	/**
	 * 里面的写法都是异步写法，如果不异步，图片可能得第二次才能加载出来。
	 * 如果要捕获错误，可以用try.catch()
	 */
	onLoad(async ()=>{
		
		const cs = await getCollectByUserId(uni.getStorageSync("userId")).catch(err=>console.log('无法获取收藏!'))
		if(cs.data.collection.length == 0) {
			emptyShow.value = true
			return 
		}
		
		for(const fieldId of cs.data.collection){
			const fieldInfo = await requestFieldInfoById(fieldId);
			collects.value.push(fieldInfo.data)
			if(fieldInfo.data.imageId && fieldInfo.data.imageId.length > 0){
				const img = await requestImg(fieldInfo.data.imageId[0]);
				collects.value.slice(-1)[0].img = img.data; // 获取collects数组里的最后一个元素
			}
			await getCommentByFieldId(fieldId).then(res=>{
				collects.value.slice(-1)[0].commentCount = res.data.length;
			})
		}

	})
	
</script>

<style scoped>
	.collects{
		margin: 8px 2px;
		padding: 8px 8px;
		border-radius: 15px;
	}
	.collects .collect{
		display: flex;
		justify-content: space-between;
	}
</style>