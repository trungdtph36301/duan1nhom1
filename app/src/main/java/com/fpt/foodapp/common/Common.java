package com.fpt.foodapp.common;

import com.fpt.foodapp.dto.User;

public class Common {
    //Lấy tên đăng nhập;
    public static User user;
    public static final String DELETE ="Delete";
    public static String coverStatus(String status) {
        if (status.equals("0")) {
            return "Chờ xác nhận";
        } else if (status.equals("1")) {
            return "Đã nhận hàng";
        } else {
            return "Đã huỷ";
        }


    }
}
