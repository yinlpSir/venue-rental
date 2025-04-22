package com.example.fieldrental.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliPayConfig {
    @Value("${spring.alipay.url}")
    private String serverUrl ;
    @Value("${spring.alipay.appId}")
    private String appId ;
    @Value("${spring.alipay.privateKey}")
    private String privateKey ;
    @Value("${spring.alipay.publicKey}")
    private String publicKey ;

    @Value("${spring.alipay.notifyUrl}")
    private String notifyUrl ;

    public AlipayClient alipayClient ()
    {
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl,appId,privateKey,"json","utf-8",publicKey,"RSA2");
        return alipayClient;
    }

    public AlipayTradeWapPayRequest request(){
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(notifyUrl+"/alipay/notify");
        return request;
    }

    //AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
    ////异步接收地址，仅支持http/https，公网可访问
    //request.setNotifyUrl("");
    ////同步跳转地址，仅支持http/https
    //request.setReturnUrl("");
    ///******必传参数******/
    //JSONObject bizContent = new JSONObject();
    ////商户订单号，商家自定义，保持唯一性
    //bizContent.put("out_trade_no", "20210817010101004");
    ////支付金额，最小值0.01元
    //bizContent.put("total_amount", 0.01);
    ////订单标题，不可使用特殊符号
    //bizContent.put("subject", "测试商品");
    //
    ///******可选参数******/
    ////手机网站支付默认传值QUICK_WAP_WAY
    //bizContent.put("product_code", "QUICK_WAP_WAY");
    ////bizContent.put("time_expire", "2022-08-01 22:00:00");
    //
    ////// 商品明细信息，按需传入
    ////JSONArray goodsDetail = new JSONArray();
    ////JSONObject goods1 = new JSONObject();
    ////goods1.put("goods_id", "goodsNo1");
    ////goods1.put("goods_name", "子商品1");
    ////goods1.put("quantity", 1);
    ////goods1.put("price", 0.01);
    ////goodsDetail.add(goods1);
    ////bizContent.put("goods_detail", goodsDetail);
    //
    ////// 扩展信息，按需传入
    ////JSONObject extendParams = new JSONObject();
    ////extendParams.put("sys_service_provider_id", "2088511833207846");
    ////bizContent.put("extend_params", extendParams);
    //
    //request.setBizContent(bizContent.toString());
    //AlipayTradeWapPayResponse response = alipayClient.pageExecute(request,"POST");
    //// 如果需要返回GET请求，请使用
    //// AlipayTradeWapPayResponse response = alipayClient.pageExecute(request,"GET");
    //String pageRedirectionData = response.getBody();
    //System.out.println(pageRedirectionData);
    //
    //if(response.isSuccess()){
    //System.out.println("调用成功");
    //} else {
    //System.out.println("调用失败");

}
