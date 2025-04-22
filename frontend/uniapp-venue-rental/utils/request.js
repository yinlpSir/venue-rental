
import BASE_URL from "./env"; // 引入公共请求接口

const requestConfig = () => {
	
	/**
	 * setConfig()对http进行全局配置
	 */
	uni.$u.http.setConfig((config)=>{
		/* config 为默认全局配置*/
		config.baseURL = BASE_URL; /* 根域名 */
		return config
	})
	
	/**
	 * 需要携带token的url
	 * 	1、像这种路径 /order/byUserId/${id} 统一写成 /order/byUserId/
	 */
	let needAuthorizationPaths = [
		"/self",
		"/order/create",
		"/alipay/pay",
		"/order/byUserId/",
		"/order/update",
		"/user/",
		"/order/cancel/",
		"/collect/add/",
		"/collect/",
		"/collect/del/",
		"/image/uploadUserImg",
		"/u/",
		"/e/",
		// "/field/getPosition/",
		// "/field/date/"
	]
	
	const matchesPath = (path)=>{
		if(needAuthorizationPaths.includes(path)) return true
		return needAuthorizationPaths.some(p => path.startsWith(p))
	}
	
	/**
	 * 请求拦截
	 */
	uni.$u.http.interceptors.request.use((config)=>{
		// console.log(config.url)
		// console.log(matchesPath(config.url))
		if(matchesPath(config.url)){
			const token = uni.getStorageSync('token')
			config.header.Authorization = `Bearer ${token}`
		}
		
		return config;
	},(config)=>{
		return Promise.reject(config) // return Promise.reject(config) 会取消本次请求
	})
	
	/**
	 * 响应拦截
	*/
   uni.$u.http.interceptors.response.use((res)=>{
	   // 响应处理
	   
	   return res;
   },(err)=>{
	   if(err.statusCode == 500){
		   uni.showToast({
		   		title: '服务器异常,请重新登录!',
		   		type:'error',
		   		icon: 'none',
		   	})
			uni.clearStorageSync()
		   	setTimeout(()=>{
		   		uni.navigateTo({
		   			url:"/pages/login/index"
		   		})
		   	},500)
	   }
	   
	   if(err.statusCode == 401){
	   		   uni.showToast({
	   				title: '身份验证异常,请重新登录!',
	   				type:'error',
	   				icon: 'none',
	   			})
				uni.clearStorageSync()
	   			setTimeout(()=>{
	   				uni.switchTab({
	   					url:"/pages/mine/index"
	   				})
	   			},900)
	   }
	   return Promise.reject(err)
   })
}

requestConfig();

const http = uni.$u.http ;

export default http ;