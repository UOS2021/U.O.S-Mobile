package com.uos.uos_mobile.dialog;

import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

import java.util.ArrayList;

/**
 * U.O.S-Mobile 내 모든 Dialog가 상속받아야 하는 클래스.<br><br>
 * 중복된 Dialog 호출 방지를 위해 구현된 클래스입니다. U.O.S-Mobile
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class UosDialog extends AppCompatDialog {

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
}
