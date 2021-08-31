package com.uof.uof_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.HttpManager;
import com.uof.uof_mobile.manager.PatternManager;
import com.uof.uof_mobile.other.Card;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;

public class CardDialog extends AppCompatDialog {
    private final Context context;
    private AppCompatImageButton ibtnAddCardClose;
    private TextInputLayout tilDlgCardNum;
    private TextInputLayout tilDlgCardMonth;
    private TextInputLayout tilDlgCardYear;
    private TextInputLayout tilDlgCardCvc;
    private TextInputLayout tilDlgCardPw;
    private AppCompatTextView tvDlgCardSave;
    private final Card card;

    public CardDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable, Card card) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.card = card;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_card);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnAddCardClose = findViewById(R.id.ibtn_dlgcard_close);
        tilDlgCardNum = findViewById(R.id.til_dlgcard_num);
        tilDlgCardMonth = findViewById(R.id.til_dlgcard_month);
        tilDlgCardYear = findViewById(R.id.til_dlgcard_year);
        tilDlgCardCvc = findViewById(R.id.til_dlgcard_cvc);
        tilDlgCardPw = findViewById(R.id.til_dlgcard_pw);
        tvDlgCardSave = findViewById(R.id.tv_dlgcard_save);

        tvDlgCardSave.setTextColor(context.getResources().getColor(R.color.color_light));

        if (card.getNum().length() > 0) {
            tilDlgCardNum.getEditText().setText(card.getNum());
            tilDlgCardMonth.getEditText().setText(card.getDueDate().substring(0, 2));
            tilDlgCardYear.getEditText().setText(card.getDueDate().substring(3, 5));
            tilDlgCardCvc.getEditText().setText(card.getCvc());
        }

        checkSaveButtonEnable();

        // 닫기 버튼이 눌렸을 경우
        ibtnAddCardClose.setOnClickListener(view -> {
            dismiss();
        });

        // 카드번호 영역 변경 시
        tilDlgCardNum.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkCardNumber(editable.toString());

                if (result == Global.Pattern.LENGTH_SHORT) {
                    tilDlgCardNum.setError("카드번호는 16자리 숫자입니다");
                    tilDlgCardNum.setErrorEnabled(true);
                } else if (result == Global.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilDlgCardNum.setError("숫자만 입력가능합니다");
                    tilDlgCardNum.setErrorEnabled(true);
                } else {
                    tilDlgCardNum.setError(null);
                    tilDlgCardNum.setErrorEnabled(false);
                }

                checkSaveButtonEnable();
            }
        });

        // 유효기간 월 영역 변경 시
        tilDlgCardMonth.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("00")) {
                    tilDlgCardMonth.getEditText().setText("01");
                } else if (editable.toString().length() != 0 && Integer.valueOf(editable.toString()) > 12) {
                    tilDlgCardMonth.getEditText().setText("12");
                }

                checkSaveButtonEnable();
            }
        });

        // 유효기간 월 영역 포커스 해제 시
        tilDlgCardMonth.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                int result = PatternManager.checkCardDueDateMonth(tilDlgCardMonth.getEditText().getText().toString());

                if (result == Global.Pattern.OK) {
                    int month = Integer.valueOf(tilDlgCardMonth.getEditText().getText().toString());

                    if (tilDlgCardMonth.getEditText().getText().toString().length() == 1) {
                        tilDlgCardMonth.getEditText().setText("0" + month);
                    }

                    if (month < 1) {
                        tilDlgCardMonth.getEditText().setText("01");
                    } else if (month > 12) {
                        tilDlgCardMonth.getEditText().setText("12");
                    }
                }

                checkSaveButtonEnable();
            }
        });

        // 유효기간 년 영역 변경 시
        tilDlgCardYear.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkSaveButtonEnable();
            }
        });

        // 유효기간 년 포커스 해제 시
        tilDlgCardYear.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                int result = PatternManager.checkCardDueDateYear(tilDlgCardYear.getEditText().getText().toString());

                if (result == Global.Pattern.OK && tilDlgCardYear.getEditText().getText().toString().length() == 1) {
                    tilDlgCardYear.getEditText().setText("0" + tilDlgCardYear.getEditText().getText().toString());
                }

                checkSaveButtonEnable();
            }
        });

        // CVC 영역 변경 시
        tilDlgCardCvc.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkCardCvc(editable.toString());

                if (result == Global.Pattern.LENGTH_SHORT) {
                    tilDlgCardCvc.setError("CVC는 세 자리 숫자입니다");
                    tilDlgCardCvc.setErrorEnabled(true);
                } else if (result == Global.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilDlgCardCvc.setError("숫자만 입력가능합니다");
                    tilDlgCardCvc.setErrorEnabled(true);
                } else {
                    tilDlgCardCvc.setError(null);
                    tilDlgCardCvc.setErrorEnabled(false);
                }

                checkSaveButtonEnable();
            }
        });

        // 비밀번호 영역 변경 시
        tilDlgCardPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int result = PatternManager.checkCardPw(editable.toString());

                if (result == Global.Pattern.LENGTH_SHORT) {
                    tilDlgCardPw.setError("카드 비밀번호는 네 자리 숫자입니다");
                    tilDlgCardPw.setErrorEnabled(true);
                } else if (result == Global.Pattern.NOT_ALLOWED_CHARACTER) {
                    tilDlgCardPw.setError("숫자만 입력가능합니다");
                    tilDlgCardPw.setErrorEnabled(true);
                } else {
                    tilDlgCardPw.setError(null);
                    tilDlgCardPw.setErrorEnabled(false);
                }

                checkSaveButtonEnable();
            }
        });

        // 등록 버튼이 눌렸을 경우
        tvDlgCardSave.setOnClickListener(view -> {
            if (checkSaveButtonEnable()) {
                try {
                    JSONObject sendData = new JSONObject();
                    sendData.put("request_code", Global.Network.Request.CARD_ADD);

                    JSONObject message = new JSONObject();
                    message.accumulate("id", Global.User.id);

                    JSONObject card = new JSONObject();

                    if (tilDlgCardMonth.getEditText().getText().toString().length() == 1) {
                        tilDlgCardMonth.getEditText().setText("0" + tilDlgCardMonth.getEditText().getText().toString());
                    }

                    if (tilDlgCardYear.getEditText().getText().toString().length() == 1) {
                        tilDlgCardYear.getEditText().setText("0" + tilDlgCardMonth.getEditText().getText().toString());
                    }

                    card.accumulate("num", tilDlgCardNum.getEditText().getText().toString());
                    card.accumulate("due_date", tilDlgCardMonth.getEditText().getText().toString() + "/" + tilDlgCardYear.getEditText().getText().toString());
                    card.accumulate("cvc", tilDlgCardCvc.getEditText().getText().toString());
                    card.accumulate("pw", tilDlgCardPw.getEditText().getText().toString());

                    message.accumulate("card", card);

                    sendData.accumulate("message", message);

                    JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                    String responseCode = recvData.getString("response_code");

                    if (responseCode.equals(Global.Network.Response.CARD_ADD_SUCCESS)) {
                        // 카드등록 성공
                        //Toast.makeText(context, "카드가 등록되었습니다", Toast.LENGTH_SHORT).show();
                    } else if (responseCode.equals(Global.Network.Response.CARD_ADD_FAILED)) {
                        // 카드등록 실패
                        Toast.makeText(context, "카드등록 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                        // 서버 연결 실패
                        Toast.makeText(context, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        // 카드등록 실패 - 기타 오류
                        Toast.makeText(context, "카드등록 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    private void saveButtonEnable(boolean enable) {
        if (enable) {
            tvDlgCardSave.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            tvDlgCardSave.setTextColor(context.getResources().getColor(R.color.color_light));
        }
        tvDlgCardSave.setEnabled(enable);
    }

    private boolean checkSaveButtonEnable() {
        boolean result = (PatternManager.checkCardNumber(tilDlgCardNum.getEditText().getText().toString()) == Global.Pattern.OK
                && PatternManager.checkCardCvc(tilDlgCardCvc.getEditText().getText().toString()) == Global.Pattern.OK
                && PatternManager.checkCardDueDateMonth(tilDlgCardMonth.getEditText().getText().toString()) == Global.Pattern.OK
                && PatternManager.checkCardDueDateYear(tilDlgCardYear.getEditText().getText().toString()) == Global.Pattern.OK
                && PatternManager.checkCardPw(tilDlgCardPw.getEditText().getText().toString()) == Global.Pattern.OK);

        saveButtonEnable(result);

        return result;
    }
}
