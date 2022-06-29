package com.capstone.pod.momo.config;

public class MoMoEndpoint {
    private String endpoint;
    private String create;
    private String refund;
    private String query;
    private String confirm;
    private String tokenPay;
    private String tokenBind;
    private String tokenCbInquiry;
    private String tokenDelete;
    private String redirectUrl;
    private String notiUrl;

    public MoMoEndpoint(String endpoint, String create, String refund, String query, String confirm, String tokenPay, String tokenBind, String tokenQueryCb, String tokenDelete , String returnUrl , String notiUrl) {
        this.endpoint = endpoint;
        this.create = create;
        this.confirm = confirm;
        this.refund = refund;
        this.query = query;
        this.tokenPay = tokenPay;
        this.tokenBind = tokenBind;
        this.tokenCbInquiry = tokenQueryCb;
        this.tokenDelete = tokenDelete;
        this.redirectUrl = returnUrl;
        this.notiUrl = notiUrl;
    }

    public String getCreateUrl() {
        return endpoint + create;
    }

    public String getRefundUrl() {
        return endpoint + refund;
    }

    public String getQueryUrl() {
        return endpoint + query;
    }

    public String getConfirmUrl() {
        return endpoint + confirm;
    }

    public String getTokenPayUrl() {
        return endpoint + tokenPay;
    }

    public String getTokenBindUrl() {
        return endpoint + tokenBind;
    }

    public String getCbTokenInquiryUrl() {
        return endpoint + tokenCbInquiry;
    }

    public String getTokenDeleteUrl() {
        return endpoint + tokenDelete;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getNotiUrl() {
        return notiUrl;
    }

    public void setNotiUrl(String notiUrl) {
        this.notiUrl = notiUrl;
    }
}
