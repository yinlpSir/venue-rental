/**
 * 开发环境选择
 */

let BASE_URL;

if(process.env.NODE_ENV === 'development'){
	BASE_URL = "http://localhost:8080/" // 开发环境基本请求地址
	// BASE_URL="http://anitsunat.natapp1.cc/"
}else{
	BASE_URL = "" // 生产环境基本请求地址
}

export default BASE_URL;