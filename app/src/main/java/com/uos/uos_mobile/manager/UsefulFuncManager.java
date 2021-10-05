package com.uos.uos_mobile.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * U.O.F-Mobile 내에서 자주 사용되는 함수들을 모아둔 클래스
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class UsefulFuncManager {
    /**
     * Base64 인코딩 되어있는 문자열을 비트맵으로 변환해주는 함수
     * @param encodeData Base64 인코딩 되어있는 문자열
     * @return Bitmap 문자열에서 Base64 디코딩된 비트맵
     */
    public static Bitmap convertStringToBitmap(String encodeData) {
        byte[] decodedString = Base64.decode(encodeData, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /**
     * 비트맵을 Base64 인코딩 되어있는 문자열로 변환해주는 함수
     * @param bitmap Base64 인코딩할 비트맵
     * @return String 비트맵에서 Base64 인코딩된 문자열
     */
    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String result = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);

        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 정수 또는 문자열을 천단위 구분기호를 넣은 문자열로 변환해주는 함수
     * @param value 천단위 구분기호를 넣을 값
     * @return 천단위 구분기호가 추가된 문자열
     */
    public static String convertToCommaPattern(String value) {
        return new DecimalFormat("###,###").format(Integer.valueOf(value));
    }
    public static String convertToCommaPattern(int value) {
        return new DecimalFormat("###,###").format(value);
    }


    /**
     * yyyy-MM-dd로 구성된 날짜에서 요일을 구하는 함수
     * @param date 요일을 추출할 날짜
     * @return String 추출된 요일 / 예외 발생시 null 반환
     */
    public static String getWeekDayFromDate(String date) {
        try {
            return new SimpleDateFormat("EE", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
