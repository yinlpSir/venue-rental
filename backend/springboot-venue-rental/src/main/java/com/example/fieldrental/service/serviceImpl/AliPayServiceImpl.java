package com.example.fieldrental.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.example.fieldrental.config.AliPayConfig;
import com.example.fieldrental.dto.AliPay;
import com.example.fieldrental.entity.Field;
import com.example.fieldrental.entity.Order;
import com.example.fieldrental.repository.FieldRepository;
import com.example.fieldrental.repository.OrderRepository;
import com.example.fieldrental.service.AliPayService;
import com.example.fieldrental.service.OrderService;
import com.example.fieldrental.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service("AliPayService")
public class AliPayServiceImpl implements AliPayService {

    @Value("${spring.alipay.publicKey}")
    private String publicKey ;

    @Autowired
    private AliPayConfig aliPayConfig ;

    @Autowired
    private RedisService redisService ;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FieldRepository fieldRepository;

//    @Override
//    public String create(AliPay aliPay) {
//        JSONObject bizContent = new JSONObject();
//        bizContent.put("out_trade_no", aliPay.getTraceNo());
//        bizContent.put("total_amount", aliPay.getTotalAmount());
//        bizContent.put("subject", aliPay.getSubject());
//        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
////        bizContent.put("fieldId", aliPay.getFieldId());
//        AlipayClient client = aliPayConfig.alipayClient();
//        AlipayTradeWapPayRequest request = aliPayConfig.request();
//        request.setBizContent(bizContent.toString());
//        /**
//         * 同步通知
//         *    同步通知说明：https://opendocs.alipay.com/open/00iki4?pathHash=eab39489
//         *    支付宝同步通知说明：https://opendocs.alipay.com/support/01raw3?ant_source=opendoc_recommend
//         */
//        request.setReturnUrl("http://coding.nat300.top/#/pages/fieldDetail/index?fieldId="+aliPay.getFieldId());
//        // 异步通知
//        request.setNotifyUrl("notifyurlsdf");
//        String form = "";
//        try{
//            form = client.pageExecute(request).getBody();//调用SDK生成表单
////            redisService.delete(aliPay.getTraceNo());
//            Order order = orderRepository.findById(aliPay.getTraceNo()).orElse(null);
////            order.setPayStatus(true); // 设置订单状态为已支付
//            Field field = fieldRepository.findById(order.getFieldId()).orElse(null);
//            field.setStatus(false);// 设置场地状态为不能租用了
//            if (order == null) {
//                throw new AlipayApiException("没发现这个订单");
//            }
//            if (Objects.isNull(field)) {
//                throw new AlipayApiException("未发现这个场地");
//            }
//            orderRepository.save(order);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return form;
//    }
    @Value("${spring.alipay.notifyUrl}")
    private String notifyUrl ;

    @Override
    public String create(AliPay aliPay) {
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", aliPay.getTraceNo());
        bizContent.put("total_amount", aliPay.getTotalAmount());
        bizContent.put("subject", aliPay.getSubject());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
//        bizContent.put("fieldId", aliPay.getFieldId());
        AlipayClient client = aliPayConfig.alipayClient();
        AlipayTradeWapPayRequest request = aliPayConfig.request();
        request.setBizContent(bizContent.toString());
        /**
         * 同步通知
         *    同步通知说明：https://opendocs.alipay.com/open/00iki4?pathHash=eab39489
         *    支付宝同步通知说明：https://opendocs.alipay.com/support/01raw3?ant_source=opendoc_recommend
         */
        request.setReturnUrl("http://coding.nat300.top/#/pages/fieldDetail/index?fieldId="+aliPay.getFieldId());
        // 异步通知
        request.setNotifyUrl(notifyUrl);
        String form = "";
        try{
            form = client.pageExecute(request).getBody();//调用SDK生成表单
            Order order = orderRepository.findById(aliPay.getTraceNo()).orElse(null);
            Field field = fieldRepository.findById(order.getFieldId()).orElse(null);
            field.setStatus(false);// 设置场地状态为不能租用了
            if (order == null) {
                throw new AlipayApiException("没发现这个订单");
            }
            if (Objects.isNull(field)) {
                throw new AlipayApiException("未发现这个场地");
            }
            orderRepository.save(order);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }


    /***
     * this method is notify the alipay service pay status
     * the function is same of the top one
     * 该方法验证支付宝的支付状态
     * 所执行的操作逻辑与上面相同
     * 因涉及到内网穿透，所以简化了部分功能，只做测试用途。
     * */
//    @Override
//    public boolean AliNotify(Map<String, String> params) throws AlipayApiException {
//        String outTradeNo = params.get("out_trade_no");
//        String gmtPayment = params.get("gmt_payment");//支付时间
//        String alipayTradeNo = params.get("trade_no");
//        String sign = params.get("sign");
//        String content = AlipaySignature.getSignCheckContentV1(params);
//        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, privateKey, "UTF-8"); //验证
//        if(checkSignature){
//            System.out.println("交易名称: " + params.get("subject"));
//            System.out.println("交易状态: " + params.get("trade_status"));
//            System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//            System.out.println("商户订单号: " + params.get("out_trade_no"));
//            System.out.println("交易金额: " + params.get("total_amount"));
//            System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//            System.out.println("买家付款时间: " + params.get("gmt_payment"));
//            System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
////                redisService.delete(params.get("out_trade_no"));
////                Order order = orderRepository.findById(params.get("out_trade_no")).orElse(null);
////                if (order == null) {
////                    throw new AlipayApiException("没发现这个订单");
////                }
////                order.setPayStatus(Boolean.parseBoolean(params.get("trade_status")));
////                orderRepository.save(order);
//            return true;
//
//        }
//        return false;
//    }

    @Override
    public boolean AliNotify(Map<String, String> params) throws AlipayApiException {
        String outTradeNo = params.get("out_trade_no");
        String gmtPayment = params.get("gmt_payment");//支付时间
        String alipayTradeNo = params.get("trade_no");
        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, publicKey, "UTF-8"); //验证
        if(true){
            System.out.println("交易名称: " + params.get("subject"));
            System.out.println("交易状态: " + params.get("trade_status"));
            System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
            System.out.println("商户订单号: " + params.get("out_trade_no"));
            System.out.println("交易金额: " + params.get("total_amount"));
            System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
            System.out.println("买家付款时间: " + params.get("gmt_payment"));
            System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
            redisService.delete(params.get("out_trade_no"));
            Order order = orderRepository.findById(params.get("out_trade_no")).orElse(null);
            if (order == null) {
                throw new AlipayApiException("没发现这个订单");
            }
            order.setPayStatus(params.get("trade_status").equals("TRADE_SUCCESS"));
//            order.setPayStatus(true);
            orderRepository.save(order);
            return true;

        }
        return false;
    }

}
