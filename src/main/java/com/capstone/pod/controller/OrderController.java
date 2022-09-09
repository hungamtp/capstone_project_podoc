package com.capstone.pod.controller;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.common.ResponseDto;
import com.capstone.pod.dto.order.CancelOrderDto;
import com.capstone.pod.dto.order.OrderOwnDesignDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.dto.utils.Utils;
import com.capstone.pod.entities.Orders_;
import com.capstone.pod.momo.MomoCallbackRequestBody;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.shared.utils.LogUtils;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String key2 = "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz";
    private Mac HmacSHA256;

    @PostMapping("/{paymentMethod}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<ResponseDto> addOrder(@Validated @RequestBody ShippingInfoDto shippingInfoDto, @PathVariable int paymentMethod) throws Exception {
        LogUtils.init();
        ResponseDto responseDTO = new ResponseDto();
        PaymentResponse paymentResponse = ordersService.addOrder(shippingInfoDto, paymentMethod);
        responseDTO.setData(paymentResponse);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/shippinginfos")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<com.capstone.pod.dto.http.ResponseDto> getShippingInfos() {
        com.capstone.pod.dto.http.ResponseDto responseDto = new com.capstone.pod.dto.http.ResponseDto();
        List<ShippingInfoDto> shippingInfoDtos = ordersService.getMyShippingInfo();
        responseDto.setData(shippingInfoDtos);
        responseDto.setSuccessMessage(OrderSuccessMessage.GET_SHIPPING_INFO_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
//    @PutMapping("cancel-by-user")
//    @PreAuthorize(RolePreAuthorize.ROLE_USER)
//    public ResponseEntity<com.capstone.pod.dto.http.ResponseDto> cancelOrderByUser(@RequestBody CancelOrderByUserDto dto) throws IOException {
//        com.capstone.pod.dto.http.ResponseDto responseDto = new com.capstone.pod.dto.http.ResponseDto();
//        ordersService.cancelOrder(dto);
//        responseDto.setSuccessMessage(OrderSuccessMessage.DELETE_ORDER_SUCCESS);
//        return ResponseEntity.ok().body(responseDto);
//    }


    @GetMapping("/complete")
    public ResponseEntity completeOrder(@RequestParam String orderId , @RequestParam String appTransId) throws Exception {
        // response from momo

//        ?partnerCode=MOMOQOYK20220626
//            &orderId=1656439230514
//            &requestId=1656439230514
//            &amount=50000
//            &orderInfo=Pay+With+MoMo&orderType=momo_wallet
//            &transId=2694732419
//            &resultCode=0
//            &message=Successful.
//            &payType=qr
//            &responseTime=1656439248218
//            &extraData=
//    &signature=640a8e675597bbf0315f86426e07dee858df920bebc85a56e59db2cd1fe6a6d4

        //ZaloPay
        //amount=323123
        // &discountamount=0
        // &appid=2553&checksum=bb1ee529210b36f63221938bb50a4e1a9fa5566ef294ca4aa70731cd963de033
        // &apptransid=220713_695232
        // &pmcid=38
        // &bankcode=&status=1
        ordersService.completeOrder(orderId , appTransId);
        return ResponseEntity.ok().body("PAID_SUCCESS");
    }

    @GetMapping("/myorder")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity getAllMyOrder(HttpServletRequest request, @RequestParam int page, @RequestParam int size
        , @RequestParam(required = false) Boolean isPaid , @RequestParam(required = false) Boolean cancel) {
        String jwt = request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Orders_.CREATE_DATE).descending());
        PageDTO pageDTO = ordersService.getAllOrder(email, pageable , isPaid , cancel);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/orderdetail")
    public ResponseEntity getAllOrderDetail(@RequestParam int page, @RequestParam int size) {
        if (page < 0) {
            throw new IllegalStateException("PAGE >= 0");
        }

        return ResponseEntity.ok().body(ordersService.getAllMyOrderDetail(page, size));
    }

    @PutMapping
    public ResponseEntity payUnpaidOrder(@RequestParam int paymentMethod, @RequestParam String orderId) throws Exception {
        return ResponseEntity.ok().body(ordersService.payOrder(paymentMethod, orderId));
    }

    @PostMapping("/orderOwnDesign/{paymentMethod}")
    public ResponseEntity orderOwnDesign(@PathVariable int paymentMethod, @RequestBody OrderOwnDesignDto orderOwnDesignDto) throws Exception {
        return ResponseEntity.ok().body(ordersService.orderOwnDesign(orderOwnDesignDto, paymentMethod));
    }



    @PostMapping("/callback")
    public String callback(@RequestBody String jsonStr) throws InvalidKeyException, NoSuchAlgorithmException {
        HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(key2.getBytes(), "HmacSHA256"));
        JSONObject result = new JSONObject();

        try {
            JSONObject cbdata = new JSONObject(jsonStr);
            String dataStr = cbdata.getString("data");
            String reqMac = cbdata.getString("mac");

            byte[] hashBytes = HmacSHA256.doFinal(dataStr.getBytes());
            String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

            // kiểm tra callback hợp lệ (đến từ ZaloPay server)
            if (!reqMac.equals(mac)) {
                // callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                // thanh toán thành công
                // merchant cập nhật trạng thái cho đơn hàng
                JSONObject data = new JSONObject(dataStr);
                ordersService.completeOrder(data.get("app_trans_id").toString() , data.get("zp_trans_id").toString());
                logger.info("update order's status = success where app_trans_id = " + data.getString("app_trans_id"));

                result.put("return_code", 1);
                result.put("return_message", "success");
            }
        } catch (Exception ex) {
            result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
            result.put("return_message", ex.getMessage());
        }

        // thông báo kết quả cho ZaloPay server
        return result.toString();
    }

    @GetMapping("/dashboard")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity getDashBoard() {
        return ResponseEntity.ok().body(ordersService.getDesignerDashboard(LocalDateTime.now().minusYears(1), LocalDateTime.now()));
    }

    @GetMapping("/dashboard/admin")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity getDashBoardAdmin() {
        return ResponseEntity.ok().body(ordersService.getAdminDashboard(LocalDateTime.now().minusYears(1), LocalDateTime.now()));
    }


    @GetMapping("/dashboard/factory")
    @PreAuthorize(RolePreAuthorize.ROLE_FACTORY)
    public ResponseEntity getDashBoardFactory() {
        return ResponseEntity.ok().body(ordersService.getFactoryDashboard(LocalDateTime.now().minusYears(1), LocalDateTime.now()));
    }

    @PutMapping("update-order-status")
    @PreAuthorize(RolePreAuthorize.ROLE_FACTORY)
    public ResponseEntity updateOrderStatus(@RequestBody List<String> orderDetailIds, @RequestParam String orderStatus) {
        ResponseDto responseDTO = new ResponseDto();
        ordersService.updateOrderDetailsStatus(orderDetailIds, orderStatus);
        responseDTO.setSuccessMessage(OrderSuccessMessage.UPDATE_ORDER_DETAIL_STATUS_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity getAllOrderDetailByOrderId(@PathVariable String orderId ) {
        return ResponseEntity.ok().body(ordersService.getOderDetailByOrderId(orderId));
    }
    @PutMapping("/cancel")
    @PreAuthorize(RolePreAuthorize.ROLE_USER_AND_FACTORY)
    public ResponseEntity cancelOrderDetails(@RequestBody @Validated CancelOrderDto dto ) {
        com.capstone.pod.dto.http.ResponseDto responseDto = new com.capstone.pod.dto.http.ResponseDto<>();
        ordersService.cancelOrderDetail(dto);
        responseDto.setSuccessMessage(OrderSuccessMessage.CANCEL_ORDER_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/momo/callback")
    public ResponseEntity noti(@RequestBody MomoCallbackRequestBody request ) {
        if(request.getResultCode() == 0){
            ordersService.completeOrder(request.getOrderId() , request.getTransId().toString());
        }
        return ResponseEntity.ok().body("PAID_SUCCESS");
    }
}
