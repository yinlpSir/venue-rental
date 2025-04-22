package com.example.fieldrental.controller;

import com.alipay.api.AlipayApiException;
import com.example.fieldrental.dto.AliPay;
import com.example.fieldrental.handler.HasAnyRole;
import com.example.fieldrental.service.AliPayService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "支付宝api")
@Controller
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private AliPayService aliPayService;

    @ApiOperation(value = "支付宝返回状态获取")
    @PostMapping("/notify")
    public void notification (HttpServletRequest request) throws AlipayApiException {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap(); // request中拿到param
            for(String name : requestParams.keySet()){
                params.put(name, request.getParameter(name));//装配到params中
            }
            aliPayService.AliNotify(params);
        }
    }

    @HasAnyRole(roles = {"USER","ADMIN"})
    @PostMapping("/pay")
    @Operation(summary  = "支付接口")
    @Parameters({
            @Parameter(name = "subject",//参数名字
                    description  = "项目名称",//参数的描述
                    required = true,//是否必须传入
                    //in定义参数传递类型：有path,query,body,form,header
                    in  = ParameterIn.PATH
            )
            ,
            @Parameter(name = "traceNo",//参数名字
                    description  = "订单号",//参数的描述
                    required = true,//是否必须传入
                    in  = ParameterIn.PATH
            ) ,
            @Parameter(name = "totalAmount",//参数名字
                    description  = "订单总数",//参数的描述
                    required = true,//是否必须传入
                    in  = ParameterIn.PATH
            )
    })
    public void pay(HttpServletResponse response, @RequestBody AliPay aliPay) throws IOException {
        String s = aliPayService.create(aliPay);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(s);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }



}
