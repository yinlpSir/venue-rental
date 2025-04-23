# venue-rental
🚀基于Spring Boot 3 + Spring Security 6 + Vue3 &amp; Vite + Element Plus + uni-app构建的前后端分离场地租赁小程序。集成了百度地图API实现智能选址，支付宝沙箱支付保障交易流程。采用MongoDB存储图片资源，基于redis实现数据缓存。

### 项目介绍
这是一个简单的运动场地租赁小程序，旨在为用户提供便利快速的场馆租赁服务。通过友好的界面，使用户能够轻松浏览场馆信息（包括搜索场馆名字或者通过地图查询附近的场馆）、查看场馆可用时段并快速完成预订。

### 主要技术栈
- **后端**：Spring Boot 3 + Spring Security 6 + Spring JPA + Redis + MongoDB
- **前端**：Vue3 & Vite + Element Plus + Axios + Pinia + uni-app
- **第三方接口**：支付宝沙箱支付、百度地图API

### 功能特性
- **地图查找**：集成百度地图API，支持查找附近的场馆。
- **沙箱支付**：调用支付宝的支付接口来预定需要付费的场馆。
- **订单管理**：可对订单进行CRUD。
- **场馆管理**：用户可收藏、评价场馆；管理员可CRUD场馆。
- **用户管理**：管理员可对用户进行一系列的操作。
  
### 演示图
<table>
    <tr>
        <td><img alt="1" src="https://github.com/user-attachments/assets/263f11fb-d7be-459e-b862-9c4d1dd94117" /></td>
        <td><img alt="2" src="https://github.com/user-attachments/assets/facd68c9-57ba-4809-a99d-f61b8ab74c8d" /></td>
    </tr>
    <tr>
        <td><img width="982" alt="3" src="https://github.com/user-attachments/assets/d6013346-3dd6-4062-a271-ec61dca604d2" /></td>
        <td><img alt="4" src="https://github.com/user-attachments/assets/816b3077-0e2e-42f5-874d-be2f9e1f762c"/></td>
    </tr>
    <tr>
        <td><img width="982" alt="5" src="https://github.com/user-attachments/assets/96fa71fe-3ca6-4b02-a269-83094cb00c92" /></td>
        <td><img width="982" alt="6" src="https://github.com/user-attachments/assets/78790c99-91f2-4949-9fc7-e6a3d4f04dbb"/></td>
    </tr>
	  <tr>
        <td><img width="970" alt="7" src="https://github.com/user-attachments/assets/35941e07-53fd-4bfb-8186-c68a11848243" /></td>
        <td><img width="970" alt="8" src="https://github.com/user-attachments/assets/34cb0f72-d81b-41e4-b5db-d2e731a8f652" /></td>
    </tr>
    <tr>
        <td><img width="970" alt="9" src="https://github.com/user-attachments/assets/c7473056-4395-4e98-a30c-12cc362fd306" /></td>
        <td><img width="970" alt="10" src="https://github.com/user-attachments/assets/11e17345-d510-4532-963c-bb557ebf1440" /></td>
    </tr>
    <tr>
        <td><img width="970" alt="11" src="https://github.com/user-attachments/assets/63f0eace-a81c-4b99-a5f0-1416709e9f63" /></td>
        <td><img width="970" alt="12" src="https://github.com/user-attachments/assets/8f132a9b-c53f-446d-8734-d6ab089a1480" /></td>
    </tr>
    <tr>
        <td><img width="970" alt="13" src="https://github.com/user-attachments/assets/04a6c666-fa0a-4d61-9b17-02fb8dfb5f4a" /></td>
        <td><img width="970" alt="14" src="https://github.com/user-attachments/assets/a42198e2-572f-4d5f-b731-1c98f5d9d9fe" /></td>
    </tr>
    <tr>
        <td><img width="970" alt="15" src="https://github.com/user-attachments/assets/de7baebb-91fa-4c21-a853-3926a463de40" /></td>
        <td><img width="970" alt="16" src="https://github.com/user-attachments/assets/0cdc7bb0-8d1d-49a4-98e4-88480935e73b" /></td>
    </tr>
    <tr>
        <td><img width="970" alt="17" src="https://github.com/user-attachments/assets/7ffc3acb-f095-4fdd-92c6-2efa18f3a258" /></td>
        <td><img width="970" alt="18" src="https://github.com/user-attachments/assets/0fb76c32-3046-480f-b9b6-c3eb37e2c99e" /></td>
    </tr>
</table>
