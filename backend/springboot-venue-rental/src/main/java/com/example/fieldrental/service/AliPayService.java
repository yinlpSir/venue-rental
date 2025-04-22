package com.example.fieldrental.service;

import com.alipay.api.AlipayApiException;
import com.example.fieldrental.dto.AliPay;

import java.util.Map;

public interface AliPayService {

    String create(AliPay aliPay);

    boolean AliNotify(Map<String, String> params) throws AlipayApiException;
}
