package com.capstone.pod.zalo;

import com.capstone.pod.momo.models.PaymentResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZaloService {

    private static Map<String, String> config = new HashMap<String, String>(){{
        put("app_id", "2553");
        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public PaymentResponse createZaloPayOrder(Long amount , String description , String transactionId) throws IOException {
        Random rand = new Random();
        final Map embed_data = new HashMap(){{}};
        embed_data.put("redirecturl" ,"http://localhost:3000/thankyou");
        Map<String, Object> order = new HashMap<String, Object>(){{
            put("app_id", config.get("app_id"));
            put("app_trans_id", transactionId); // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
            put("app_time", System.currentTimeMillis()); // miliseconds
            put("app_user", "user123");
            put("amount", amount);
            put("description", description);
            put("bank_code", "zalopayapp");
            put("item", "[]");
            put("embed_data", new JSONObject(embed_data).toString());
        }};

        // app_id +???|???+ app_trans_id +???|???+ appuser +???|???+ amount +"|" + app_time +???|???+ embed_data +"|" +item
        String data = order.get("app_id") +"|"+ order.get("app_trans_id") +"|"+ order.get("app_user") +"|"+ order.get("amount")
            +"|"+ order.get("app_time") +"|"+ order.get("embed_data") +"|"+ order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(config.get("endpoint"));

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }

        // Content-Type: application/x-www-form-urlencoded
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        JSONObject result = new JSONObject(resultJsonStr.toString());

        for (String key : result.keySet()) {
            System.out.format("%s = %s\n", key, result.get(key));
        }
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayUrl(result.get("order_url").toString());
        paymentResponse.setOrderId(result.get("zp_trans_token").toString());
        paymentResponse.setMessage(result.get("return_message").toString());
        return paymentResponse;
    }
}
