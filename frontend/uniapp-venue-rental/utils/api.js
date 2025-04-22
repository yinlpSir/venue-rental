import http from "./request.js";

export const requestLogin = async (data) => await http.post("/login",data)

export const requestRegister = (data) => http.post("/register",data)

// 获取轮播图
export const requestCarousel = (data) => http.get("/v3/causal")

// 分页获取场地信息
export const requestFieldInfo = (data) => http.post("/field/all",data)

// 通过场地Id获取场地信息
export const requestFieldInfoById = async (fieldId) => {
	await checkField(fieldId) // 每次查询场地都判断一下场地(主要是场地的status)
	return await http.post(`/field/get/${fieldId}`)
}

// 获取场地图片
export const requestImg = (imageId) => http.get(`/image/${imageId}`)

// 搜索
export const requestSearch = (searchContent,data) => {
	return http.post(`/field/search/${searchContent}`,data)
}

// 获取当前登录的用户信息
export const getUser = async() => {
	return await http.get(`/self`)
}

// 创建订单
export const createOrder = (data) => {
	return http.post(`/order/create`,data)
}

// 支付宝支付接口
export const alipay = (data) => {
	return http.post(`/alipay/pay`,data)
}

// 判断场地是否可以租用
export const checkField = async (fieldId) => {
	return await http.post(`/field/is/${fieldId}`)
}

// 获取某用户所有订单
export const getOrdersByUserId = async (userId) => {
	return await http.get(`/order/byUserId/${userId}`)
}

// 获取某场地的评论
export const getCommentByFieldId = async (fieldId) => {
	return await http.get(`/comment/fieldId/${fieldId}`)
}

// 对某个场地进行评论
export const addComment = async (data) => {
	return await http.post(`/comment/create`,data)
}

// 更新某个订单
export const updateOrder = async (data) => {
	return await http.post(`/order/update`,data)
}

// 删除某个订单
export const delOrder = async (orderId) => {
	return await http.post(`/order/cancel/${orderId}`)
}

// 根据用户id查询用户信息
export const getUserById = async (id) => {
	return await http.get(`/user/${id}`)
}

// 将场地加入收藏
export const addCollect = async (userId,fieldId) => {
	return await http.post(`/collect/add/${userId}/${fieldId}`)
}

// 获取某用户的收藏
export const getCollectByUserId = async (userId) => {
	return await http.get(`/collect/${userId}`)
}

// 删除某用户的收藏
export const delCollectByUserId = async (userId,fieldId) => {
	return await http.post(`/collect/del/${userId}/${fieldId}`)
}

// 免费订单
export const freeField = async (data) => {
	return await http.get(`/rentalFree`)
}

// 修改头像
export const modifyUserImg = async (fileUrl) => {
	return await http.upload(`/image/uploadUserImg`,{
		filePath:fileUrl, // filePath就是上传的文件
		name:'file'
	})
}

/**
 * 修改用户名
 * username 是修改的用户名
 */
export const modifyUsername = async (username) => {
	return await http.get(`/u/${username}`)
}

/**
 * 修改邮箱
 * email 是修改的email
 */
export const modifyEmail = async (email) => {
	return await http.get(`/e/${email}`)
}

// 判断用户是否已收藏某个场地
export const checkIsCollected = async (fieldId) => {
	return await http.post(`/collect/check/${fieldId}`)
}

// 获取经纬度坐标
export const getPosition = async (positionId) => {
	return await http.post(`/field/getPosition/${positionId}`)
}

// 场地不可租赁时间
export const getDate = async (id) => {
	return await http.post(`/field/date/${id}`)
}