/**
 * 这里面主要做登录拦截
 */

/**
 * 导入pages.json的pages，通过pages里对象的needLogin属性判断需要登录的页面。
 * 不过我这里还是直接把需要登录的路径记录下来放到数组里
 */
import {pages} from "../pages.json"

// 需要登录的路径
import needLogin from "./needLoginPagePath"

// 需要添加拦截的列表
let interceptList = ["navigateTo","switchTab"]

// 用遍历的方式分别为uni.navigateTo uni.switchTab 这两个路由方法添加拦截
interceptList.forEach(item => {
	// 添加拦截
	uni.addInterceptor(item,{
		invoke(args) {
			// 拦截前触发的函数
			
			// args.url ==> /pages/fieldDetail/index?fieldId=6713a9d964d2b358715c2af2
			const token = uni.getStorageSync("token")
			const url = args.url.split('?')[0] // 防止类似于这种 /pages/fieldDetail/index?fieldId=6713a9d 的url
			
			if(needLogin.includes(url) && token == ''){
				uni.showToast({
					title: '请先登录',
					type:'error',
					icon: 'none',
				})
				setTimeout(()=>{
					uni.navigateTo({
						url: "/pages/login/index"
					})
				},900)
				return false
			}
			return true
		},
		success(args){
			// 拦截成功回调
			console.log('intercept success')
		},
		fail() {
			console.log('intercept fail')
		}
	})
})

// uni.removeInterceptor(STRING)
