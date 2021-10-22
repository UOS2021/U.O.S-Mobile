package com.uos.uos_mobile.dialog;

import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

import com.uos.uos_mobile.activity.UosActivity;

import java.util.ArrayList;

/**
 * U.O.S-Mobile 내 모든 Dialog가 상속받아야 하는 클래스.<br><br>
 * 중복된 Dialog 호출 방지를 위해 구현된 클래스입니다. U.O.S-Mobile
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public abstract class UosDialog extends AppCompatDialog {

    /**
     * 현재 생성되어있는 UosDialog 클래스를 상속받은 Dialog의 목록.
     */
    private static final ArrayList<UosDialog> dialogs = new ArrayList<>();

    public UosDialog(Context context) {
        super(context);

        addDialog();
    }

    public UosDialog(Context context, int theme) {
        super(context, theme);

        addDialog();
    }

    /**
     * dismiss() 호출 시 dialogs 목록에서 현재 UosDialog 제거.
     */
    @Override
    public void dismiss() {
        dialogs.remove(this);
        super.dismiss();
    }

    /**
     * UosDialog.dialogs에 동일한 클래스가 있을 경우 해당 UosDialog를 종료 및 UosDialog.dialogs에서 제거하고
     * 현재 UosDialog를 UosDialog.dialogs에 추가.
     */
    protected void addDialog() {
        for (UosDialog dialog : dialogs) {
            if (dialog.getClass().equals(this.getClass())) {

                /* 현재 UosDialog와 동일한 클래스의 UosDialog가 존재할 경우 */

                dialog.dismiss();
            }
        }
        dialogs.add(this);
    }

    /**
     * 현재 UosDialog.dialogs에 추가되어있는 UosDialog 중 매개변수로 전달된 클래스와 동일한 클래스를 가
     * 진 UosDialog를 반환. 만약 동일한 클래스를 가진 UosDialog가 없을 경우 Null 반환
     *
     * @param targetUosDialog 가져올 UosDialog의 클래스.
     * @return UosDialog dialogs에 있는 UosDialog 객체.
     */
    public static UosDialog get(Class targetUosDialog){
        for(UosDialog uosDialog : dialogs){
            if(uosDialog.getClass().equals(targetUosDialog)){
                return uosDialog;
            }
        }
        return null;
    }
}
