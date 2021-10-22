package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * U.O.S-Mobile 내 모든 Activity가 상속받아야 하는 클래스.<br><br>
 * 중복된 Activity 호출 방지 및 편리한 Activity 이동을 위해 구현된 클래스입니다. UosActivity를 상속받은 모든
 * 클래스는 onCreate() 실행 시 실행중인 Activity 목록에 추가되며 onDestroy() 생성시 실행중인 Activity 목록에서
 * 제거됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public abstract class UosActivity extends AppCompatActivity {

    /**
     * 현재 생성되어있는 UosActivity 클래스를 상속받은 Activity의 목록.
     */
    public static ArrayList<UosActivity> activities = new ArrayList<>();

    /**
     * onDestroy() 호출 시 activities 목록에서 현재 Activity를 제거합니다.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        activities.remove(this);
    }

    /**
     * onCreate() 호출 시 UosActivity.activities에 현재 클래스와 동일한 클래스가 존재할 경우 해당 Activity를
     * 종료하고 실행중인 Activity 목록에서 제거합니다.그리고 현재 Activity를 실행중인 Activity 목록에 추가합니다.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (UosActivity uosActivity : activities) {
            if (uosActivity.getClass().equals(this.getClass())) {

                /* 현재 UosActivity와 동일한 클래스의 UosActivity가 존재할 경우 */

                uosActivity.finish();
            }
        }
        activities.add(this);

        super.onCreate(savedInstanceState);

        init();
    }

    /**
     * 현재 activities 내에 있는 Activity 중 매개변수로 넘어온 클래스의 Activity까지 되돌리는 함수입니다.
     * 실행중인 Activity 목록의 마지막 Activity(가장 최근에 실행된 UosActivity)부터 매개변수로 넘어온 Activity
     * 사이의 모든 Activity는 종료되어 매개변수로 넘어온 Activity가 화면에 표시되도록 합니다.
     *
     * @param targetUosActivity 되돌아갈 UosActivity.
     * @return boolean targetActivity가 activities에 존재할 경우 true, 존재하지 않을 경우 false 반환.
     */
    public static boolean revertToActivity(Class targetUosActivity) {
        for (UosActivity uosActivity : activities) {
            if (uosActivity.getClass().equals(targetUosActivity)) {

                /* 만약 activities에 돌아가고자 하는 UosActivity가 존재할 경우 */

                for (int loop = activities.size() - 1; loop > activities.indexOf(uosActivity); loop--) {
                    activities.get(loop).finish();
                    activities.remove(loop);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 실행중인 Activity 목록에 있는 모든 Activity를 종료하고 매개변수로 전달된 Intent를 이용하여 새로운
     * Activity를 실행합니다.
     *
     * @param activityIntent UosActivity 실행 시 사용될 Intent
     */
    public static void startFromActivity(Intent activityIntent) {
        for (int loop = 0; loop < activities.size() - 1; loop++) {
            activities.get(loop).finish();
            activities.remove(loop);
        }

        activities.get(0).startActivity(activityIntent);
        activities.get(0).finish();
    }

    /**
     * 실행중인 Activity 목록에 있는 모든 Activity를 종료합니다. 이는 앱을 종료하는 것과 동일한 동작을 합니다.
     */
    public static void clear() {
        for (UosActivity uosActivity : activities) {
            uosActivity.finish();
            activities.remove(uosActivity);
        }
    }

    /**
     * 실행중인 Activity 목록에 추가되어있는 Activity 중 매개변수로 전달된 클래스와 동일한 클래스를 가진
     * Activity를 반환합니다. 만약 동일한 클래스를 가진 UosActivity가 없을 경우 Null 반환합니다.
     *
     * @param targetUosActivity 가져올 UosActivity의 클래스.
     * @return UosActivity activities에 있는 UosActivity 객체.
     */
    public static UosActivity get(Class targetUosActivity) {
        for (UosActivity uosActivity : activities) {
            if (uosActivity.getClass().equals(targetUosActivity)) {

                /* 매개변수로 전달된 클래스와 동일한 클래스인 객체가 있을 경우 */

                return uosActivity;
            }
        }
        return null;
    }

    /**
     * 실행중인 Activity 목록에 있는 Activity 중 매개변수로 전달된 index에 해당하는 Activity를 반환합니다.
     * 인덱스가 -1일 경우 가장 마지막에 있는 Activity 객체를 반환하고 잘못된 인덱스 입력 시 null을 반환합니다.
     *
     * @param index 가져올 Activity의 인덱스.
     * @return UosActivity activities에 있는 UosActivity 객체.
     */
    public static UosActivity get(int index) {
        if (index == -1) {
            
            /* 인덱스 값이 -1일 경우 - 가장 마지막에 추가된 Activity 객체를 반환 */

            return activities.get(activities.size() - 1);
        } else if (index > 0 && index < activities.size()) {

            /* 인덱스 값이 유효한 값일 경우 */
            
            return activities.get(index);
        }
        return null;
    }

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수입니다. onCreate() 함수의 가장
     * 마지막에서 실행됩니다.
     */
    protected abstract void init();
}
