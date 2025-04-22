<template>
	<view class="order-list">
		
		<view class="order" v-for="order in orders" :key="order.id">
			<view class="order-header">
				<p style="font-weight: bold;font-size: 18px;">{{order.fieldInfo.name}}</p>
				<span v-if="order.payStatus" style="color: green;">已支付</span>
				<span v-else style="color: #FFA500;">未支付</span>
			</view>
			
			<view class="order-body">
				<div style="display: flex;align-items: center;">
					<up-image :show-loading="true" radius="10" :src="order.img" width="100" height="80"></up-image>
					<div style="font-size: 14px;margin-left: 8px;">
						<p style="margin: 4px 0;">开始时间: {{timeconvert(order.startTime)}}</p>
						<p style="margin: 4px 0;">结束时间: {{timeconvert(order.endTime)}}</p>
						<p>单价: ￥{{order.price}}/天</p>
					</div>
				</div>
				<div style="font-weight: bold;font-size: 16px;">
					￥{{order.total}}
				</div>
			</view>
			
			<view class="order-footer">
				<!--之所以给每个button添加一个view，是为了解决微信小程序上flex布局的justify-content布局不生效。-->
				
				<view class="">
					<up-button :plain="true" style="width: 15%;margin-left: 8px;" :hairline="true" shape="circle" size="small" color="#ffa85c" text="删除订单" @click="deleteOrder(order.id)"></up-button>
				</view>
				
				<view class="">
					<up-button v-if="checkOrderEndtime(order.endTime)" :plain="true" style="width: 15%;margin-left: 8px;" :hairline="true" shape="circle" size="small" color="black" :disabled="order.evaluateStatus" @click="popupOpen(order.fieldInfo.id,order)">{{order.evaluateStatus?'已评价':'评价'}}</up-button>
				</view>
				
				<view class="">
					<up-button v-if="!checkOrderEndtime(order.endTime)" :plain="true" style="width: 15%;margin-left: 8px;" :hairline="true" shape="circle" size="small" color="green" text="进行中..."></up-button>
				</view>
				
				<view class="">
					<up-button v-if="checkOrderEndtime(order.endTime)" :plain="true" style="width: 15%;margin-left: 8px;" :hairline="true" shape="circle" size="small" text="已结束" disabled></up-button>
				</view>
			</view>
		</view>
		
		<up-popup :show="popupShow" mode="center" :closeable="true" :round="10" @close="popupClose" :customStyle="{width:'70%',padding:'40px 20px'}">
			<view>
				<div style="display: flex;margin-bottom: 10px;">
					<span>评分：</span>
					<up-rate v-model="rate"></up-rate>
				</div>
				<div style="display: flex;flex-wrap: wrap;">
					<span>评价：</span>
					<up-textarea v-model="evaluateContent" placeholder="请输入您的评价~" count></up-textarea>
				</div>
				<div style="display: flex; margin-top: 20px;">
					<span style="width: 50px;"></span>
					<up-button type="primary" shape="circle" style=" width: 40%;" text="提交" @click="addEvaluate" ></up-button>
				</div>
			</view>
		</up-popup>
		
		<up-toast ref="uToastRef"></up-toast>
		
		<!--模态框-->
		<up-modal :show="modalShow" title="确认要删除此订单吗?" confirm-text="确认" cancel-text="取消" showCancelButton @confirm="confirmDelOrder" @cancel="modalShow=false"></up-modal>
		
		<up-empty
		        mode="data"
		        icon="http://cdn.uviewui.com/uview/empty/data.png"
				text="暂无订单"
				:show="emptyShow"
		/>
		
	</view>
</template>

<script setup>
	import { onLoad } from "@dcloudio/uni-app"
	import {ref, toRaw} from "vue"
	import {showToast} from "@/utils/tools.js"
	import {
		getOrdersByUserId,
		requestFieldInfoById,
		requestImg,
		addComment,
		updateOrder,
		delOrder
	} from "@/utils/api.js"
	
	const uToastRef = ref(null)
	
	const rate = ref(0)
	
	const evaluateContent = ref('')
	
	let currentOrder = {}
	
	const currentFieldId = ref('')
	
	const currentDelOrderId = ref('')
	
	const popupShow = ref(false)
	
	const modalShow = ref(false)
	
	const emptyShow = ref(false)
	
	const popupClose = async ()=> {
		rate.value = 0
		evaluateContent.value = ''
		currentFieldId.value = ''
		currentOrder = {}
		popupShow.value = false
	}
	
	const popupOpen = (fieldId,order)=> {
		popupShow.value = true
		currentFieldId.value = fieldId
		currentOrder = JSON.parse(JSON.stringify(toRaw(order))) // 解决浅拷贝问题
		currentOrder.evaluateStatus = true
		delete currentOrder.fieldInfo
		delete currentOrder.img
	}
	
	const deleteOrder = (orderId)=>{
		modalShow.value = true
		currentDelOrderId.value = orderId
	}
	
	const confirmDelOrder = async() => {
		await delOrder(currentDelOrderId.value).then(res=>{
			currentDelOrderId.value = ''
			loadData()
			showToast(uToastRef,{
				type:'success',
				message:'删除成功!'
			})
		})
		modalShow.value = false
	}
	
	const addEvaluate = async()=>{
		if(rate.value > 0 && evaluateContent.value !== '') {
			await addComment({
				content:evaluateContent.value,
				rate:rate.value,
				fieldId:currentFieldId.value,
				userId:uni.getStorageSync("userId")
			}).then(res=>{
				updateOrder(currentOrder).then(res=>{
					popupClose()
					showToast(uToastRef,{
						type:'success',
						message:'感谢评价!'
					})
					loadData()
				})
			})
		}	
	}
	
	const timeconvert = (datetimeArr)=>{
		const [year, month, day, hours, minutes, milliseconds] = datetimeArr; 
		// hours+8 由于后端没对时差进行处理,前端代处理一下(东八区)
		return `${year}-${month}-${day} ${hours+8}:${minutes>=10?minutes:'0'+minutes}`
	}
	
	const checkOrderEndtime = (endtimeArr)=>{
		let now = Date.now()
		let endtimeTimeStamp = new Date(timeconvert(endtimeArr)).getTime()
		if(endtimeTimeStamp > now) return false // 订单还没结束
		return true // 订单已结束
	}
	
	const orders = ref([])
	
	const loadData = async()=>{
		await getOrdersByUserId(uni.getStorageSync("userId")).then(res=>{
			orders.value = res.data
			orders.value.forEach(order=>{
				requestFieldInfoById(order.fieldId).then(res=>{
					order.fieldInfo = res.data
					requestImg(res.data.imageId[0]).then(res=>{
						order.img = res.data
					})
				})
			})
		}).catch(err => console.log(err))
		if(orders.value.length == 0) emptyShow.value = true
	}
	
	onLoad(()=>{
		loadData()
	})
</script>

<style scoped>
	.order-list{
		padding: 8px 8px;
	}
	.order{
		border: 1px solid #ccc;
		margin: 8px 0;
		padding: 8px 8px;
		border-radius: 15px;
		background-color: #f5f5f5;
	}
	.order .order-header{
		margin-bottom: 8px;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.order .order-body{
		display: flex;
		margin-bottom: 8px;
		align-items: center;
		justify-content: space-between;
	}
	.order .order-footer{
		display: flex;
		justify-content: flex-end;
		margin-top: 14px;
	}
	:deep(uni-button){
		margin-left: 0;
		margin-right: 0;
	}
	:deep(.order .order-footer.data-v-071fab2e){
		display: flex;
		justify-content: flex-end;
	}
	:deep(.u-button){
		width: 10%;
		margin-left: 15px;
	}
	:deep(.u-button--hairline.data-v-461e713c){
		width: 15%;
	}
</style>