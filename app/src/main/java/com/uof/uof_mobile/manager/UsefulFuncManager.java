package com.uof.uof_mobile.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

public class UsefulFuncManager {
    // 문자열을 이미지로 변경
    public static Bitmap convertStringToBitmap(String encodeData) {
        byte[] decodedString = Base64.decode(encodeData, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    // 이미지를 문자열로 변경
    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
    }

    // 1000단위 구분기호 추가
    public static String convertToCommaPattern(String value) {
        return new DecimalFormat("###,###").format(Integer.valueOf(value));
    }

    // 1000단위 구분기호 추가
    public static String convertToCommaPattern(int value) {
        return new DecimalFormat("###,###").format(value);
    }
}
