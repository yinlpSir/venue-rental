/**
 * 导入pages.json的pages，通过pages里对象的needLogin属性判断需要登录的页面。
 * 不过我这里还是直接把需要登录的路径记录下来放到数组里
 */
import {pages} from "../pages.json"

// 需要登录的页面路径
export default [
  "/pages/order/index",
  "/pages/userInfo/index",
  "/pages/CreateOrder/index",
  "/pages/mineOrder/index",
  "/pages/mineCollect/index",
  "/pages/awaitOrder/index"
]