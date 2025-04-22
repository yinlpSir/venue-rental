<template>
	<view style="
		background-color: #7BB9F5;
		background-image: url(/static/mine/bg.jpg);
		background-size: contain;
	">
		<view class="mine-header">
			<div class="avatar" @click="toLogin" v-if="!isLogin">
				<div class="left">
					<up-avatar :src="xxx" size="65" shape="circle"></up-avatar>
					<div style="margin-left: 14px; min-width: 95px; max-width: 190px;">
						<h4 style="margin-bottom: 6px;">
							请登录/注册您的账号
						</h4>
						<up-button type="primary"  size="mini" style="width: 65px;margin-left: 0;border-radius: 6px;" >点击登录</up-button>
					</div>
				</div>
				<up-icon name="arrow-right" size="20" color="black"></up-icon>
			</div>
			
			<div class="avatar" v-else>
				<div class="left">
					<up-avatar :src="publicStore.avatarSrc" size="65" shape="circle"></up-avatar>
					<div style="margin-left: 14px; min-width: 95px; max-width: 190px;">
						<h4 style="margin-bottom: 6px;font-weight: bold;font-size:20px;">
							{{userInfo.username}}
						</h4>
						<span>{{publicStore.email}}</span>
					</div>
				</div>
				<up-icon name="arrow-right" size="20" color="black" @click="toUserInfo"></up-icon>
			</div>
			<div style="display: flex; justify-content: space-between;margin: 0 40px; padding: 15px 0;">
				<div class="order" @click="handleMineOrder">
					<image src="/static/mine/order.png" mode="" style="width: 30px; height: 30px;"></image>
					<p style="width: 65px;"><center>我的订单</center></p>
				</div>
				<div class="order" @click="handlePendingOrder">
					<image src="/static/mine/purse.png" mode="" style="width: 30px; height: 30px;"></image>
					<p style="width: 51px;"><center>待付款</center></p>
				</div>
				<div class="order" @click="handleStar">
					<image src="/static/mine/star.png" mode="" style="width: 30px; height: 30px;"></image>
					<p style="width: 65px;"><center>我的收藏</center></p>
				</div>
			</div>
		</view>
		
		<view class="mine-body">
			<div class="item" @click="toUserInfo">
				<div class="left">
					<image src="/static/mine/personal_info.png" mode="" style="width: 35px; height: 35px;"></image>
					<span style="margin-left: 8px;">个人信息</span>
				</div>
				<up-icon name="arrow-right" size="20" style="margin-right: 10px;"></up-icon>
			</div>
			<div class="item">
				<div class="left">
					<image src="/static/mine/problem.png" mode="" style="width: 35px; height: 35px;"></image>
					<span style="margin-left: 8px;">常见问题</span>
				</div>
				<up-icon name="arrow-right" size="20" style="margin-right: 10px;"></up-icon>
			</div>
			<div class="item">
				<div class="left">
					<image src="/static/mine/idea.png" mode="" style="width: 35px; height: 35px;"></image>
					<span style="margin-left: 8px;">意见反馈</span>
				</div>
				<up-icon name="arrow-right" size="20" style="margin-right: 10px;"></up-icon>
			</div>
			<div class="item">
				<div class="left">
					<image src="/static/mine/contact_us.png" mode="" style="width: 35px; height: 35px;"></image>
					<span style="margin-left: 8px;">联系我们</span>
				</div>
				<up-icon name="arrow-right" size="20" style="margin-right: 10px;"></up-icon>
			</div>
			
			<div class="logout" v-if="isLogin" @click="logout">
				<image src="/static/mine/logout.png" style="width: 40px; height: 40px;" mode="" ></image>
			</div>
		</view>
	</view>
</template>

<script setup>
	import {ref} from "vue"
	
	import { onLoad } from "@dcloudio/uni-app"
	
	import {getUser,requestImg} from "@/utils/api.js"
	
	import { usePublicStore } from "@/stores/public.js";
	
	// ✨
	const publicStore = usePublicStore()
	
	// 头像
	const avatarSrc = ref("")
	
	const isLogin = ref(false) // 是否登录
	
	const userInfo = ref({
		id:'',
		username:'',
		email:'',
		role:''
	})
	
	// 是否登录了
	const checkLogin = ()=>{
		// 如果token不存在则 uni.getStorageSync('token') == '' 为 true
		if(uni.getStorageSync('token') !== '') {
			// 已登录
			isLogin.value = true
			
			getUser().then(res=>{
				if(res.data.img !== null) {
					requestImg(res.data.img).then(res => {
						publicStore.avatarSrc = res.data
					})
				}
				userInfo.value.id = res.data.id
				userInfo.value.username = res.data.username
				publicStore.setEmail(res.data.email)
				userInfo.value.role = res.data.role
			}).catch(err=>console.log('用户信息获取失败!'))
		}
	}
	
	onLoad(()=>{
		checkLogin()
	})
	
	uni.$on('refresh',()=>{
		checkLogin()
	})
	
	const toUserInfo = ()=>{
		uni.navigateTo({
			url:`/pages/userInfo/index`,
			animationType: 'pop-in',
			animationDuration: 200
		})
	}
	
	const toLogin = ()=>{
		// uniapp 路由
		uni.navigateTo({
			url:"/pages/login/index",
			animationType: 'pop-in',
			animationDuration: 200
		})
	}
	
	const handleMineOrder=()=>{
		uni.navigateTo({
			url:"/pages/mineOrder/index",
		})
	}
	
	const handlePendingOrder=()=>{
		uni.navigateTo({
			url:"/pages/awaitOrder/index",
		})
	}
	
	const handleStar=()=>{
		uni.navigateTo({
			url:"/pages/mineCollect/index",
		})
	}
	
	const logout = ()=>{
		avatarSrc.value = ''
		isLogin.value = false
		uni.clearStorageSync()
		uni.navigateTo({
			url:"/pages/login/index"
		})
	}
</script>

<style scoped>
	.mine-header{
		width: 100%;
		height: 200px;
	}
	.mine-header .avatar{
		margin:0 40px;
		margin-bottom: 10px;
		padding-top: 40px;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}
	.mine-header .avatar .left{
		display: flex;
		align-items: center;
		flex-wrap: wrap;
	}
	.mine-header .order{
		min-width: 30px;
		max-width: 80px;
		display: flex;
		justify-content: center;
		flex-wrap: wrap; /* 允许换行 */
	}
	.mine-body{
		width: 100%;
		border-top-right-radius: 20px;
		border-top-left-radius: 20px;
		background-color: white;
		padding: 6px 0;
		border-bottom: none;
	}
	.mine-body .item{
		display: flex;
		justify-content: space-between;
		margin: 6px 0;
	}
	.mine-body .item .left{
		display: flex;
		align-items: center;
		padding: 6px 10px;
	}
	.mine-body .logout{
		margin: 0 auto;
		margin-top: 50px;
		margin-bottom: 30px;
		width: 40%;
		height: 50px;
		border-radius: 50px;
		background-color: #ccc;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>
