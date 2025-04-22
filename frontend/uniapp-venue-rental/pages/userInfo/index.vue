<template>
	<view style="overflow-y: hidden;"><!--禁止滚动-->
		<up-upload
			:fileList="file"
			@afterRead="afterRead"
			name="6"
			:maxCount="1"
			width="250"
			height="150"
		>
			<div style="margin: 50px auto;">
				<up-avatar class="avatar" style="" :src="publicStore.avatarSrc" size="120" shape="circle"></up-avatar>
				<p style="text-align: center;margin-top: 4px;">点击修改</p>
			</div>
		</up-upload>
		<div class="info">
			<div class="item">
				<span>昵称</span>
				<span class="text" @click="handleUsernameClick" v-if="!modifyUsernameCom">
					{{userInfo.username}}
					<up-icon style="display: inline-block;" name="arrow-right" size="14" color="black"></up-icon>
				</span>
				<up-input
					v-else
				    placeholder="请输入用户名"
					v-model="modifiedUsername"
					:focus="true"
					:maxlength="10"
					@blur="handleUsernameBlue"
				  >
					<template #suffix>
						<up-button
							@click="handleModifyUsername"
							text="点击修改"
							type="success"
							size="mini"
						></up-button>
					</template>
				</up-input>
			</div>
			<div class="item">
				<span>邮箱</span>
				<span class="text" @click="handleEmailClick" v-if="!modifyEmailCom">
					{{publicStore.email}}
					<up-icon style="display: inline-block;" name="arrow-right" size="14" color="black"></up-icon>
				</span>
				<up-input
					v-else
				    placeholder="请输入邮箱"
					v-model="modifiedEmail"
					:focus="true"
					@blur="handleEmailBlue"
				  >
					<template #suffix>
						<up-button
							@click="handleModifyEmail"
							text="点击修改"
							type="success"
							size="mini"
						></up-button>
					</template>
				</up-input>
			</div>
			<div class="item">
				<span>身份</span>
				<span class="text" style="margin-right: 18px;">
					{{userInfo.role == 'USER'?`尊贵的${userInfo.username}`:'管理员'}}
				</span>
			</div>
			<div class="item">
				<span>注册时间</span>
				<span class="text" style="margin-right: 18px;">{{userInfo.registerDate}}</span>
			</div>
		</div>
		<up-toast ref="uToastRef"></up-toast>
	</view>
</template>

<script setup>
	import { onLoad } from "@dcloudio/uni-app"
	import {getUser,modifyUserImg,requestImg,modifyUsername,modifyEmail} from "@/utils/api.js"
	import {ref} from 'vue'
	import { usePublicStore } from "@/stores/public.js";
	import { showToast } from "../../utils/tools";
	
	const uToastRef = ref(null)
	
	const publicStore = usePublicStore()
	
	const avatarUrl = ref('')
	
	const modifiedUsername = ref('')
	
	const modifyUsernameCom = ref(false)
	
	const modifiedEmail = ref('')
	
	const modifyEmailCom = ref(false)
	
	const file = ref()
	
	const afterRead = (file, lists, name)=>{
		modifyUserImg(file.file.url).then(res=>{
			publicStore.avatarSrc = URL.createObjectURL(file.file.file)
			showToast(uToastRef,{
				type:'success',
				message:'修改成功'
			})
		})
	}
	
	const handleUsernameClick = ()=>{
		modifiedUsername.value = userInfo.value.username
		modifyUsernameCom.value = true
	}
	
	const handleModifyUsername = () => {
		if(modifiedUsername.value.trim() == ''){
			return ;
		}
		modifyUsername(modifiedUsername.value).then(res => {
			if(res){
				uni.clearStorageSync()
				modifyUsernameCom.value = false
				showToast(uToastRef,{
					type:'success',
					message:'用户名修改成功,需重新登录'
				})
				setTimeout(()=>{
					// uni.switchTab({
					// 	url:"/pages/mine/index"
					// })
					uni.navigateTo({
						url:"/pages/login/index"
					})
				},1000)
			}
		})
	}
	
	const handleUsernameBlue = () => {
		if(modifiedUsername.value == userInfo.value.username) {
			modifyUsernameCom.value = false
		}
	}
	
	const handleEmailClick = ()=>{
		modifiedEmail.value = publicStore.email
		modifyEmailCom.value = true
	}
	
	const handleModifyEmail = () => {
		if (!uni.$u.test.email(modifiedEmail.value)) {
			showToast(uToastRef,{
				type:'error',
				message:'请输入正确的邮箱格式'
			})
			return;
		}
		modifyEmail(modifiedEmail.value).then(res=>{
			modifyEmailCom.value = false
			publicStore.setEmail(modifiedEmail.value)
			showToast(uToastRef,{
				type:'success',
				message:'修改成功'
			})
		})
	}
	
	const handleEmailBlue = () => {
		if(modifiedEmail.value == publicStore.email) modifyEmailCom.value = false
	}
	
	const userInfo = ref({
		id:'',
		username:'',
		email:'',
		role:'',
		registerDate:''
	})
	
	onLoad(()=>{
		getUser().then(res=>{
			if(res.data.img !== null) {
				requestImg(res.data.img).then(res => {
					publicStore.avatarSrc = res.data
				})
			}
			Object.assign(userInfo.value,res.data)
			publicStore.setEmail(userInfo.value.email)
		})
	})
</script>

<style scoped>
	.avatar{
		border: 2px solid #ccc;
	}
	.info{
		width: 80%;
		margin: 0 auto;
		border: 1px solid #ccc;
		border-radius: 16px;
		font-weight: bold;
		background-color:#f5f6f8;
	}
	.info .item{
		margin:30px 12px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.info>.item:first-child{
		margin-top: 16px;
	}
	.info>.item:last-child{
		margin-bottom: 16px;
	}
	.info .item .text{
		font-size: 14px;
	}
	
	:deep(.u-upload__wrap[data-v-cafe0b2a]){
		margin:0 auto;
	}
	
	:deep(.u-input){
		padding: 4px 9px;
		margin-left: 10px;
	}
	
	/*以下deep针对微信开发工具样式问题*/
	:deep(.u-avatar--circle.data-v-14a988f2){
		margin: 50px auto;
		border: 2px solid #ccc;
	}
	
	:deep(.u-icon--right.data-v-1c933a9a){
		display: inline-block;
	}
	
</style>