<template>
	<view class="logo">
		<image src="/static/logo.png" mode="" class="img"></image>
	</view>
	<view class="form">
		<up-toast ref="uToastRef"></up-toast>
		<up-form
			labelPosition="top"
			:model="userInfo"
			:rules="rules"
			ref="formRef"
		>
			<up-form-item borderBottom prop="username">
				<up-input
				    placeholder="请输入用户名"
				    border="bottom"
				    clearable
					v-model="userInfo.username"
					prefixIcon="account-fill"
					prefixIconStyle="font-size: 22px;color: #909399"
				></up-input> 
			</up-form-item>
			<up-form-item borderBottom prop="password">
				<up-input
				    placeholder="请输入密码"
					:password="true"
					:autoBlur="true"
				    border="bottom"
				    clearable
					v-model="userInfo.password"
					prefixIcon="lock-fill"
					prefixIconStyle="font-size: 22px;color: #909399"
				></up-input> 
			</up-form-item>	
		</up-form>
		<div style="margin-top: 35px;">
			<up-button type="primary" @click="submit" >
				<span style="font-size: 18px;">登 录</span>
			</up-button>
			<div class="part">
				<span style="color:#7BB9F5 ;">
					<navigator url="/pages/register/index">注册</navigator>
				</span>
				<span style="color:#7BB9F5 ;">忘记密码?</span>
			</div>
		</div>
	</view>
	<view class="other">
		<up-divider text="其他登录方式"></up-divider>
		<div class="items">
			<div class="item">QQ</div>
			<div class="item">微信</div>
			<div class="item">微博</div>
		</div>
	</view>
</template>

<script setup>
	import {ref,reactive,toRaw} from "vue"
	import {requestLogin,getUser} from "/utils/api.js"
	import { showToast } from "../../utils/tools"
	
	// 表单响应式引用
	const formRef = ref(null)

	// 登录信息
	const userInfo = reactive({
		username:"",
		password:""
	})
	
	const uToastRef = ref(null)
	
	const submit = async ()=>{
		await formRef.value.validate().then(valid => {  
		    if (valid) {  
				showToast(uToastRef,{
					type:'loading',
					message:'登录中'
				})
				requestLogin(toRaw(userInfo)).then(res => {
					getUser().then(res=>{
						if(res.data.role == null){
							showToast(uToastRef,{
								type:'error',
								message:'您已被系统拉黑,请联系管理!'
							})
							return ;
						}
						uni.setStorageSync('userId',res.data.id)
					})
					
					showToast(uToastRef,{
						type:'success',
						message:'登录成功!'
					})
					/**
					 * uni.setStorage(OBJECT) 
					 * 将数据存储在本地缓存中指定的 key 中，会覆盖掉原来该 key 对应的内容，这是一个异步接口。
					 * uni.setStorage({
							key:'token',
							data:'sdfjsldfsldjflsjdf'
						})
						uni.getStorage({
							key:"token",
							success(res) {
								console.log(res.data)
							}
						})
					 * 
					 * uni.setStorageSync(KEY,DATA)
					 * 将 data 存储在本地缓存中指定的 key 中，会覆盖掉原来该 key 对应的内容，这是一个同步接口。
					 * uni.getStorageInfo(OBJECT)
					 * uni.getStorageSync(KEY)
					 * uni.removeStorageSync(KEY)
					 * uni.clearStorageSync()
					 */
					uni.setStorageSync('token',res.data.jwttoken)
					
					setTimeout(function(){
						uni.$emit('refresh') // 解决 navigateBack() 返回上一页不能刷新页面问题（从而不能更新数据)
						uni.navigateBack({
							delta: 1,
							animationType: 'pop-out',
							animationDuration: 200
						});
					},1000)
				}).catch(err=>{
					showToast(uToastRef,{
						type:'error',
						message:'用户名或密码错误!',
					})
				})
		    } else {  
				// uni.$u.toast('校验失败')
		    }  
		}).catch(() => {  
			// 处理验证错误  
			// uni.$u.toast('校验失败')
		});  
	}
	
	const rules = ref({
		username:[
			{
				required: true,
				message: '请输入用户名',
				trigger: ['blur', 'change']
			},
			// 3-8个字符之间的判断
			{
				min: 3,
				max: 8,
				message: '请输入有效的用户名'
			},
		],
		password:[
			{
				required: true,
				message: '请输入密码',
				trigger: ['blur', 'change']
			},
			// 3-8个字符之间的判断
			{
				min: 3,
				max: 8,
				message: '请输入有效的密码'
			},
		],
	})
	
</script>

<style scoped>
	.logo{
		width: 100%;
		display: flex;
		justify-content: center;
		margin-top: 100px;
		margin-bottom: 35px;
	}
	.logo .img{
		width: 50%;
		height: 100px;
	}
	.form{
		margin:4px 8px;
		padding: 2px 30px;
	}
	.form .part{
		display: flex;
		justify-content: space-between;
		font-size: 15px;
		margin: 4px 5px;
	}
	.other{
		margin:4px 8px;
		margin-top: 40px;
		padding: 2px 30px;
	}
	.other .items{
		display: flex;
		justify-content: space-around;
		margin-top: 30px;
	}
	.other .items .item{
		width: 50px;
		height: 50px;
		border-radius: 50%;
		border: 1px solid rgb(144, 147, 153);
		text-align: center;
		line-height: 50px;
		color: rgb(144, 147, 153);
	}
</style>
