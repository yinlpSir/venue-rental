<template>
	<view class="order-list">
		
		<view class="order" v-for="order in orders" :key="order.id">
			<view class="order-header">
				<p style="font-weight: bold;font-size: 18px;">{{order.fieldInfo.name}}</p>
				<span style="color: #FFA500;">未支付</span>
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
					<up-button v-if="!checkOrderEndtime(order.endTime)" :plain="true" style="width: 15%;margin-left: 8px;" :hairline="true" shape="circle" size="small" color="green" text="点击支付" @click="handlePay(order)"></up-button>
				</view>
				
			</view>
		</view>
		
		<up-toast ref="uToastRef"></up-toast>
		
		<!--模态框-->
		<up-modal :show="modalShow" title="确认要删除此订单吗?" confirm-text="确认" cancel-text="取消" showCancelButton @confirm="confirmDelOrder" @cancel="modalShow=false"></up-modal>
		
		<up-empty
		        mode="data"
		        icon="http://cdn.uviewui.com/uview/empty/data.png"
				text="暂无订单"
				:show="emptyShow"
		/>
		
		<!--加载页-->
		<up-loading-page :loading="loadingPageShow"></up-loading-page>
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
		delOrder,
		createOrder,
		alipay,
		freeField
	} from "@/utils/api.js"
	
	const uToastRef = ref(null)
	
	let currentOrder = {}
	
	const loadingPageShow = ref(false)
	
	const currentFieldId = ref('')
	
	const currentDelOrderId = ref('')
		
	const modalShow = ref(false)
	
	const emptyShow = ref(false)
	
	const orders = ref([])
	
	const handlePay = async(order)=>{
		console.log(order)
		let st =''
		order.startTime.forEach(e=>{
			if(e!=0){
				st+=e+'-'
			}
		})
		st = st.slice(0, -1)
		
		let et =''
		order.startTime.forEach(e=>{
			if(e!=0){
				st+=e+'-'
			}
		})
		et = et.slice(0, -1)

		loadingPageShow.value = true
		
		alipay({
			traceNo:order.id,
			totalAmount:order.total,
			subject: order.fieldInfo.name,
			fieldId:order.fieldId
		}).then(res=>{
			
			document.querySelector('body').innerHTML = res.data;
			document.forms[0].submit()
		}).catch(err => console.log(err))
	}
	
	const deleteOrder = (orderId)=>{
		modalShow.value = true
		currentDelOrderId.value = orderId
	}
	
	const confirmDelOrder = async() => {
		await delOrder(currentDelOrderId.value).then(res=>{
			currentDelOrderId.value = ''
			// loadData()
			showToast(uToastRef,{
				type:'success',
				message:'删除成功!'
			})
		})
		modalShow.value = false
		location.reload()
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
	
	const loadData = async()=>{
		const os = await getOrdersByUserId(uni.getStorageSync("userId"))
		const fs = ref('')
		os.data.forEach(o =>{
			if(!o.payStatus){
				fs.value = requestFieldInfoById(o.fieldId);
					// o.fieldInfo = fs.data
					// console.log()
					fs.value.then(res => {
						o.fieldInfo = res.data
						requestImg(res.data.imageId[0]).then(res=>{
							o.img = res.data
							orders.value.push(o)
						})
					})
			}
		})
		
		// fs.value.then(await res => {
		// 	o.fieldInfo = res.data
		// 	requestImg(res.data.imageId[0]).then(res=>{
		// 		o.img = res.data
		// 		orders.value.push(o)
		// 	})
		// })
		
		// await getOrdersByUserId(uni.getStorageSync("userId")).then(res=>{
			
		// 	res.data.forEach(order=>{
		// 		if(!order.payStatus){
		// 			requestFieldInfoById(order.fieldId).then(res=>{
		// 				order.fieldInfo = res.data
		// 				requestImg(res.data.imageId[0]).then(res=>{
		// 					order.img = res.data
		// 					orders.value.push(order)
		// 				})
		// 			})
					
		// 		}
		// 	})
			
		// }).catch(err => console.log(err))
		
		// if(orders.value <= 0){
		// 	emptyShow.value = true
		// 	return ;
		// }else emptyShow.value = false
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