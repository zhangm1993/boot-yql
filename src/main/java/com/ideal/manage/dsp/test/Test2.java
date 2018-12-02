package com.ideal.manage.dsp.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideal.manage.dsp.util.HttpUtils;

public class Test2 {
    public static void main(String[] args) throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Cell_02_Volt", "22.2");
        jsonObject.put("port", "2222");
        jsonObject.put("ip", "2.2.2.2");
        jsonObject.put("macid", "id222");
        jsonObject.put("Cell_03_Volt", "22.3");
        jsonObject.put("messageId", "0x186101F4");
        jsonObject.put("time", "1526031927931");
        jsonArray.add(jsonObject);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("Cell_02_Volt", "33.3");
        jsonObject1.put("port", "3333");
        jsonObject1.put("ip", "3.3.3.3");
        jsonObject1.put("macid", "id333");
        jsonObject1.put("Cell_03_Volt", "33.4");
        jsonObject1.put("messageId", "0x186101F3");
        jsonObject1.put("time", "1526031927931");
        jsonArray.add(jsonObject1);


        JSONObject result = new JSONObject();
        result.put("type", 2);
        result.put("data", jsonArray.toJSONString());
        System.out.println(result.toJSONString());

        String result1 = HttpUtils.post("http://localhost:10801/esms/canbs", result.toJSONString(), "application/json;charset=UTF-8");
        System.out.println(result1);
    }
}
