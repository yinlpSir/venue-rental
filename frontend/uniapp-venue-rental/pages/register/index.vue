<template>
	<view class="logo">
		<image src="/static/logo.png" mode="" class="img"></image>
	</view>
	<view class="form">
		<up-toast ref="uToastRef"></up-toast>
		<up-form labelPosition="top" :model="userInfo" :rules="rules" ref="registerFormRef">
			<up-form-item borderBottom prop="username">
				<up-input placeholder="请输入用户名" border="bottom" clearable v-model="userInfo.username"
					prefixIcon="account-fill" prefixIconStyle="font-size: 22px;color: #909399"></up-input>
			</up-form-item>
			<up-form-item borderBottom prop="password">
				<up-input placeholder="请输入密码" :password="true" :autoBlur="true" border="bottom" clearable
					v-model="userInfo.password" prefixIcon="lock-fill"
					prefixIconStyle="font-size: 22px;color: #909399"></up-input>
			</up-form-item>
			<up-form-item borderBottom prop="confirmPassword">
				<up-input placeholder="请输入确认密码" :password="true" :autoBlur="true" border="bottom" clearable
					v-model="userInfo.confirmPassword" prefixIcon="lock-fill"
					prefixIconStyle="font-size: 22px;color: #909399"></up-input>
			</up-form-item>
			<up-form-item borderBottom prop="email">
				<up-input placeholder="请输入您的邮箱" :autoBlur="true" border="bottom" clearable v-model="userInfo.email"
					prefixIcon="lock-fill" prefixIconStyle="font-size: 22px;color: #909399"></up-input>
			</up-form-item>
		</up-form>
		<div style="margin-top: 35px;">
			<up-button type="primary" @click="register">
				<span style="font-size: 18px;">注 册</span>
			</up-button>
			<div class="part">
				<span style="color:#7BB9F5 ;">
					<navigator url="/pages/login/index">登录</navigator>
				</span>
				<!-- <span style="color:#7BB9F5 ;">
						<navigator url="/pages/mine/index">返回主页</navigator>
					</span> -->
			</div>
		</div>
	</view>
</template>

<script setup>
	import {ref,reactive,toRaw} from "vue"
	import {requestRegister} from "/utils/api.js"
	import { showToast } from "../../utils/tools"

	// 表单响应式引用
	const registerFormRef = ref(null)

	// 注册信息
	const userInfo = reactive({
		username: "",
		password: "",
		confirmPassword: "",
		email: ""
	})
	
	const uToastRef = ref(null)
	
	const register = async () => {
		await registerFormRef.value.validate().then(valid => {
		    if (valid) {  
				showToast(uToastRef,{
					type:'loading',
					message:'注册中'
				})
				requestRegister(toRaw(userInfo)).then(res =>{
					showToast(uToastRef,{
						type:'success',
						message:'注册成功',
						url:"/pages/login/index"
					})
				}).catch(err => {
					showToast(uToastRef,{
						type:'error',
						message:err.data.message
					})
				})
		    } else {  
				// uni.$u.toast('校验失败')
		    }  
		}).catch(() => {  
			// 处理验证错误  
			// uni.$u.toast('处理验证错误')
		});  
	}
	
	const rules = ref({
		username: [
			{
				required: true,
				message: '请输入用户名',
				trigger: ['blur', 'change']
			},
			// 正则判断为字母或数字
			{
				pattern: /^[0-9a-zA-Z]*$/g,
				// 正则检验前先将值转为字符串
				transform(value) {
					return String(value);
				},
				message: '用户名只能包含字母或数字',
				trigger: ['blur', 'change']
			},
			// 3-8个字符之间的判断
			{
				min: 3,
				max: 8,
				message: '用户名长度在3-8个字符之间',
				trigger: ['blur', 'change']
			},
			// 校验用户是否已存在
			{
				asyncValidator: (rule, value, callback) => {
					// console.log("校验用户是否已存在")
					callback()
					// uni.$u.http.post('/xxx/xxx', {name: value}).then(res => {
					// 	// 如果验证不通过，需要在callback()抛出new Error('错误提示信息')
					// 	if(res.error) {
					// 		callback(new Error('姓名重复'));
					// 	} else {
					// 		// 如果校验通过，也要执行callback()回调
					// 		callback();
					// 	}
					// })
				},
				// 如果是异步校验，无需写message属性，错误的信息通过Error抛出即可
				// message: 'xxx'
			}
		],
		password: [
			{
				required: true,
				message: '请输入密码',
				trigger: ['blur', 'change']
			},
			// 正则判断为字母或数字
			{
				pattern: /^[0-9a-zA-Z]*$/g,
				// 正则检验前先将值转为字符串
				transform(value) {
					return String(value);
				},
				message: '密码中只能包含字母或数字'
			},
			// 3-8个字符之间的判断
			{
				min: 3,
				max: 8,
				message: '密码长度在3-8个字符之间'
			},
		],
		confirmPassword: [
			{
				required: true,
				message: '请输入确认密码',
				trigger: ['blur', 'change']
			},
			{
				asyncValidator: (rule, value, callback) => {
					if (userInfo.password !== value) callback(new Error("两次输入密码不一致"))
					else callback()
				},
				trigger: ['blur']
			}
		],
		email: [
			{
				required: true,
				message: '请输入您的邮箱',
				trigger: ['blur', 'change']
			},
			{
				asyncValidator: (rule, value, callback) => {
					/**
					 * 老式写法似乎要有个引入test方法操作 import { test } from '@/uni_modules/uview-plus';
					 * 可参考：https://uiadmin.net/uview-plus/js/test.html#google_vignette
					 */
					if (!uni.$u.test.email(value)) callback(new Error("请输入格式正确的邮箱"))
					else callback()
				},
				trigger: ['blur']
			}
		]
	})
</script>

<style scoped>
	.logo {
		width: 100%;
		display: flex;
		justify-content: center;
		margin-top: 100px;
		margin-bottom: 35px;
	}

	.logo .img {
		width: 50%;
		height: 100px;
	}

	.form {
		margin: 4px 8px;
		padding: 2px 30px;
	}

	.form .part {
		display: flex;
		justify-content: space-between;
		font-size: 15px;
		margin: 4px 5px;
	}
</style>