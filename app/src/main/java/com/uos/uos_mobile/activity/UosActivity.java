package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * U.O.S-Mobile 내 모든 Activity가 상속받아야 하는 클래스.<br><br>
 * 중복된 Activity 호출 방지 및 편리한 Activity 이동을 위해 구현된 클래스입니다. UosActivity를 상속받은 모든 클래스는
 * onCreate() 실행 시 UosActivity.activities에 추가되며 onDestroy() 생성시 UosActivity.activities에서 제
 * 거됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class UosActivity extends AppCompatActivity {

    /**
     * 현재 생성되어있는 UosActivity 클래스를 상속받은 Activity의 목록.
     */
    private static ArrayList<UosActivity> activities = new ArrayList<>();

    /**
     * onDestroy 호출 시 activities 목록에서 현재 UosActivity 제거.
     */
    @Override
    protected void onDestroy() {
        activities.remove(this);

        super.onDestroy();
    }

    /**
     * onCreate 호출 시 UosActivity.activities에 현재 클래스와 동일한 클래스가 존재할 경우 해당 UosActivity를
     * 종료 및 UosActivity.activities에서 제거한 후 현재 UosActivity를 UosActivity.activities에 추가.
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
    }

    /**
     * 현재 activities 내에 있는 UosActivity 중 매개변수로 넘어온 클래스의 UosActivity까지 되돌리는 함수.
     * activities의 마지막 UosActivity부터 매개변수로 넘어온 Activity 사이의 모든 Activity는 종료.
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
     * 현재 activities에 생성되어있는 모든 UosActivity를 종료하고 매개변수로 전달된 Intent를 이용하여 새로운 A
     * ctivity를 실행.
     *
     * @param activityIntent UosActivity 실행 시 사용될 Intent
     */
    public static void startFromActivity(Intent activityIntent){
        for(int loop = 0; loop < activities.size() - 1; loop++){
            activities.get(loop).finish();
            activities.remove(loop);
        }

        activities.get(0).startActivity(activityIntent);
        activities.get(0).finish();
    }

    /**
     * 현재 UosActivity.activities에 생성되어있는 모든 UosActivity를 종료.
     */
    public static void clear(){
        for(UosActivity uosActivity : activities){
            uosActivity.finish();
            activities.remove(uosActivity);
        }
    }

    /**
     * 현재 UosActivity.activities에 추가되어있는 UosActivity 중 매개변수로 전달된 클래스와 동일한 클래스를 가
     * 진 UosActivity를 반환. 만약 동일한 클래스를 가진 UosActivity가 없을 경우 Null 반환
     * 
     * @param targetUosActivity 가져올 UosActivity의 클래스.
     * @return UosActivity activities에 있는 UosActivity 객체.
     */
    public static UosActivity get(Class targetUosActivity){
        for(UosActivity uosActivity : activities){
            if(uosActivity.getClass().equals(targetUosActivity)){
                return uosActivity;
            }
        }
        return null;
    }
}
