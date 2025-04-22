<template>
	<view class="create-order">
		<div class="header">
			<up-image :show-loading="true" radius="10" :src="field.cover" width="170px" height="110px"></up-image>
			<div style="width: auto; margin-left: 10px;">
				<span class="fieldName">{{field.name}}</span>
				<span class="fieldPhone">
					<up-icon name="phone" color="#7BB9F5"></up-icon>
					<span>18229488888</span>
				</span>
				<span class="fieldAddress">
					<up-icon name="map" color="#7BB9F5"></up-icon>
					<span>{{field.address}}</span>
				</span>
			</div>
		</div>
		
		<div class="body">
			<div class="rent-day">
				<span>租用天数</span>
				<up-number-box v-model="days" name="day" style="background-color: #f4f9fd;border-radius: 20px;" @change="handleChangeDay">
					<template #minus>
						<view class="minus">
							<up-icon name="minus" size="12" @click="minusDay"></up-icon>
						</view>
					</template>
					<template #input>
						<text style="margin:0 8px;text-align: center;" class="input">{{days}}</text>
					</template>
					<template #plus>
						<view class="plus">
							<up-icon name="plus" color="skyblue" size="12" @click="plusDay"></up-icon>
						</view>
					</template>
				</up-number-box>
			</div>
			
			<div class="rent-amount">
				<span>场地租金</span>
				<span>￥{{field.price}}/天</span>
			</div>
			<div class="rent-date">
				<span>租赁日期</span>
				<div>
					<div style="color:skyblue; display: flex;align-items: center; justify-content: flex-end; margin-bottom: 6px;" @click="startDatetimePickerShow = true">
						<p>
							{{ startDatetime == ''?'开始日期':dayjs(startDatetime).format('YYYY-MM-DD HH:mm') }}
						</p>
						<up-icon name="arrow-down-fill" color="skyblue" size="4"></up-icon>
					</div>
					<div style=" color:skyblue; text-align: right; display: flex;align-items: center; justify-content: flex-end; margin-top: 6px;" @click="endDatetimePickerShow = true">
						<p>
							{{ endDatetime == ''?'结束日期':dayjs(endDatetime).format('YYYY-MM-DD HH:mm') }}
						</p>
						<up-icon name="arrow-down-fill" color="skyblue" size="4"></up-icon>
					</div>
				</div>
			</div>
		</div>
		
		<up-datetime-picker
			ref="startDatetimePickerRef"
		    :show="startDatetimePickerShow"
			title="开始日期"
		    v-model="startDatetime"
		    mode="datetime"
			:closeOnClickOverlay="true"
			:formatter="formatter"
			:minDate="Date.now()"
			@close="confirmStartDate"
			@cancel="confirmStartDate"
			@confirm="confirmStartDate"
		></up-datetime-picker>
		<up-datetime-picker
			ref="endDatetimePickerRef"
		    :show="endDatetimePickerShow"
			title="结束日期"
		    v-model="endDatetime"
		    mode="datetime"
			:closeOnClickOverlay="true"
			:formatter="formatter"
			:minDate="Date.now()+oneDayInMs"
			@close="confirmEndDate"
			@cancel="confirmEndDate"
			@confirm="confirmEndDate"
		></up-datetime-picker>
		<up-toast ref="uToastRef" style="z-index: 66666;!important"></up-toast>
		
		<!--加载页-->
		<up-loading-page :loading="loadingPageShow"></up-loading-page>
		
		<div class="rule">
			<h5 style="text-align: center;">场地租赁事项协议条款</h5>
			一、租赁期限
			<p class="pp">租赁期限自[具体起始日期]起至[具体结束日期]止。
			如需续租，承租方应在租赁期满前[具体时间，如30天]书面通知出租方，并重新签订租赁合同。</p>
			二、租金及支付方式
			<p class="pp">租金总额为人民币[具体金额]元，支付方式为[一次性支付/分期支付，分期支付需明确每期支付金额及时间]。
			租金支付时间为[每月/每季度/每年]的第[具体日期]之前，支付至出租方指定账户。</p>
			三、场地使用
			<p class="pp">承租方应合理使用并妥善保管场地及其附属设施，不得擅自改变场地结构或用途。
			承租方在租赁期间内，不得转租、转借或以其他方式让渡场地使用权给第三方，除非事先得到出租方书面同意。
			承租方应遵守国家法律法规及当地社区规定，不得在场地内进行违法活动或产生噪音、污染等影响他人生活的行为。</p>
		</div>
		
		<up-tabbar
			:fixed="true"
			:placeholder="true"
			:safeAreaInsetBottom="true"
		>
			<div class="btabbar">
				<div style="width: 50%;">
					<span style="color: gray;">在线支付</span>
					<span style="color: red;margin: 0 4px;font-size: 18px;">￥{{(field.price*days).toFixed(2)}}</span>
					<span style="color: skyblue;">
						<span>明细</span>
						<up-icon style="display: inline-block;" color="skyblue" name="arrow-up"></up-icon>
					</span>
				</div>
				<up-button style="height:80%;border-radius:10px;width: 180px;margin: 0;" type="primary" text="立即支付" @click="pay" />
			</div>
		</up-tabbar>
	</view>
</template>

<script setup>
	import {onLoad,onReady} from "@dcloudio/uni-app"
	import dayjs from "dayjs" // 一个日期处理的js库
	import {showToast} from "@/utils/tools.js"
	import {ref} from "vue"
	import {
		requestFieldInfoById,
		requestImg,
		createOrder,
		alipay,
		freeField
	} from "/utils/api.js"
	import { usePublicStore } from "../../stores/public"
	
	const publicStore = usePublicStore()
	
	const range = (start, end) => {
	  const result = [];
	  for (let i = start; i < end; i++) {
	    result.push(i);
	  }
	  return result;
	};

	const field = ref({})
	
	const days = ref(publicStore.canRentalDates.length-1 <= 0?1:publicStore.canRentalDates.length-1)
	
	const uToastRef = ref(null)
	
	const loadingPageShow = ref(false)
	
	// 天数 +1
	const plusDay = () => {
		endDatetime.value += oneDayInMs
	}
	
	// 天数 -1
	const minusDay = () => {
		if(days.value <= 1) return 
		endDatetime.value -= oneDayInMs
	}
	
	const pay = async ()=>{
		loadingPageShow.value = true
		if(field.value.price == 0){
			// 免费场地预约
			freeField({
				id: "",
				fieldId:field.value.id,
				price:field.value.price,
				total:field.value.price*days.value,
				startTime:new Date(startDatetime.value),
				endTime:new Date(endDatetime.value),
				payStatus:true,
				addressIP:'',
				userId:uni.getStorageSync("userId"),
				coachId: null,
				deviceRental: true
			}).then(res=>{
				uni.navigateTo({
					url:`/pages/fieldDetail/index?fieldId=${field.value.id}`
				})
			}).catch(err=>console.log('订单创建失败!'))
			return ;
		}
		
		// 付费的场地预约
		await createOrder({
			id: "",
			fieldId:field.value.id,
			price:field.value.price,
			// total:(field.value.price*days.value).toFixed(2),
			total:field.value.price*days.value,
			startTime:new Date(startDatetime.value),
			endTime:new Date(endDatetime.value),
			payStatus:true,
			addressIP:'',
			userId:uni.getStorageSync("userId"),
			coachId: null,
			deviceRental: true
		}).then(res=>{
			// res.data 返回订单的id
			alipay({
				traceNo:res.data,
				totalAmount:(field.value.price*days.value).toFixed(2),
				subject: field.value.name,
				fieldId:field.value.id
			}).then(res=>{
				
				document.querySelector('body').innerHTML = res.data;
				document.forms[0].submit()
			}).catch(err => console.log(err))
		}).catch(err => console.log('创建失败!'))
	}

	// #region 日期相关
	/**
	 * 规则：
	 * 1、开始时间不能是过去时间
	 * 2、按一天起租，不够一天按一天算
	 * 3、结束时间不能小于开始时间且必须大一天
	 */
	
	// 一天的毫秒数
	const oneDayInMs = 24 * 60 * 60 * 1000;
	
	const startDatetimePickerRef = ref(null)
	const endDatetimePickerRef = ref(null)
	
	const startDatetimePickerShow = ref(false)
	const endDatetimePickerShow = ref(false)
	
	// const startDatetime = ref(Date.now()) 
	// const endDatetime = ref(Date.now()+oneDayInMs)

	const startDatetime = ref(Number.isNaN(new Date(publicStore.canRentalDates[0]).getTime())?Date.now():new Date(publicStore.canRentalDates[0]).getTime())
	const endDatetime = ref(Number.isNaN(new Date(publicStore.canRentalDates[publicStore.canRentalDates.length-1]).getTime())?Date.now()+oneDayInMs:new Date(publicStore.canRentalDates[publicStore.canRentalDates.length-1]).getTime())
	
	/**
	 * dateToDays 通过毫秒数计算出距离指定日期有多少天
	 * startTime 指定日期的毫秒数
	 * endTime 
	 * 
	 * Math.floor() 是js的向下取整函数
	 * Math.ceil() 是js的向上取整函数
	 */
	const dateToDays = (startTime,endTime)=> {
		let timeDiff = endTime - startTime // 时间差
		let day = Math.floor(timeDiff / oneDayInMs) // 将时间差转换为天数
		let hour = Math.floor(timeDiff / (1000 * 60 * 60) % 24) // 将时间差转换为小时
		let minutes = Math.floor((timeDiff / (1000 * 60)) % 60)
		const seconds = Math.floor((timeDiff / 1000) % 60)
		
		if(hour >= 23 && minutes >=59) days.value = day+1
		else days.value = day
	}
	
	const confirmStartDate = (val)=>{
		// 这里加 (10*1000) => 10秒误差
		if((endDatetime.value - startDatetime.value) <= 0 || (endDatetime.value - startDatetime.value)+(10*1000) < oneDayInMs || startDatetime.value > endDatetime.value) {
			showToast(uToastRef,{
				type:'error',
				message:'至少租一天~'
			})
			return 
		}
		dateToDays(startDatetime.value,endDatetime.value)
		startDatetimePickerShow.value = false
	}
	
	const confirmEndDate = (val)=>{
		if((endDatetime.value - startDatetime.value) <= 0 || (endDatetime.value - startDatetime.value)+(10*1000) < oneDayInMs || endDatetime.value <= startDatetime.value) {
			showToast(uToastRef,{
				type:'error',
				message:'至少租一天~'
			})
			return 
		}
		dateToDays(startDatetime.value,endDatetime.value)
		endDatetimePickerShow.value = false
	}
	
	const formatter = (type, value) => {
		if (type === 'year') {
			return `${value}年`;
		}
		if (type === 'month') {
			return `${value}月`;
		}
		if (type === 'day') {
			return `${value}日`;
		}
		if (type === 'hour') {
			return `${value}点`;
		}
		if (type === 'minute') {
			return `${value}分`;
		}
		return value;
	};
	
	onReady(() => {
	  // 微信小程序需要用此写法
	  startDatetimePickerRef.value.setFormatter(formatter);
	  endDatetimePickerRef.value.setFormatter(formatter);
	});
	
	// #endregion

	onLoad((options) => {
		requestFieldInfoById(options.fieldId).then(res => {
			field.value = res.data
			requestImg(field.value.imageId[0]).then(res => {
				field.value.cover = res.data
			}).catch(err => console.log('请求图片失败!'))
		}).catch(err => console.log('获取场地信息失败'))
	})
</script>

<style scoped>
	.create-order {
		padding: 10px 12px;
	}

	.header {
		display: flex;
		height: 110px;
		align-items: center;
	}

	.header .fieldName {
		font-weight: bold;
		font-size: 20px;
		margin-top: 6px;
		display: block;
		margin-bottom: 20px;
	}

	.header .fieldPhone {
		display: flex;
		align-items: center;
		margin: 8px 0;
		font-size: 14px;
	}

	.header .fieldAddress {
		display: flex;
		align-items: center;
		margin: 8px 0;
		font-size: 14px;
	}

	.body {
		margin-top: 10px;
		border: 1px solid #dde2eb;
		border-radius: 10px;
	}
	.body .rent-day{
		display: flex;
		justify-content: space-between;
		padding: 8px 8px;
		font-weight: bold;
	}
	.body .rent-amount{
		display: flex;
		justify-content: space-between;
		padding: 8px 8px;
		font-weight: bold;
	}
	.body .rent-date{
		padding: 8px 8px;
		font-weight: bold;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	
	.rule{
		margin-top: 10px;
		border: 1px solid #dde2eb;
		border-radius: 10px;
		padding: 8px 8px;
	}
	.rule .pp{
		text-indent: 2em;
		line-height: 20px;
		font-size: 14px;
		margin: 6px 0;
	}
	
	.btabbar{
		display: flex;
		height: 100%;
		width: 100%;
		align-items: center;
		justify-content:space-between;
		flex-wrap: nowrap;
		padding: 0 12px;
	}
	
	/** 自定义的数字滚动样式*/
	.minus {
		width: 22px;
		height: 22px;
		border: 1px solid #E6E6E6;
		border-radius: 50%;
		display:  flex;
		justify-content: center;
		align-items: center;
	}

	.plus {
		width: 22px;
		height: 22px;
		border: 1px solid skyblue;
		border-radius: 50%;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		justify-content: center;
		align-items: center;
	}
	
	/*以下deep针对微信开发工具样式问题*/
	:deep(.u-button--square.data-v-461e713c){
		height:80%;
		border-radius:10px;
		width: 180px;
		margin: 0;
	}
	:deep(.u-icon--right.data-v-1c933a9a){
		display: inline-block;
	}
	:deep(.u-icon text){
		font-size: 12px!important;
	}
</style>