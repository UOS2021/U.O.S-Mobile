package com.uof.uof_mobile.dialog;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.MovieSeatAdapter;
import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.MovieItem;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class SelectSeatDialog extends Dialog {
    private final Context context;
    private final SelectSeatDialog.SelectSeatDialogListener selectSeatDialogListener;
    private MovieItem movieItem;
    private AppCompatImageButton ibtnDlgSelectSeatClose;
    private AppCompatTextView tvDlgSelectSeatMovie;
    private AppCompatTextView tvDlgSelectSeatTime;
    private RecyclerView rvDlgSelectSeatSeatList;
    private AppCompatTextView tvDlgSelectSeatTotalPrice;
    private LinearLayoutCompat llDlgSelectSeatSelect;
    private AppCompatTextView tvDlgSelectSeatSelect;
    private MovieSeatAdapter movieSeatAdapter;
    private int lastTotalPrice = 0;

    public SelectSeatDialog(@NonNull Context context, MovieItem movieItem, SelectSeatDialog.SelectSeatDialogListener selectSeatDialogListener) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        try {
            this.movieItem = movieItem.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selectSeatDialogListener = selectSeatDialogListener;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_selectseat);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgSelectSeatClose = findViewById(R.id.ibtn_dlgselectseat_close);
        tvDlgSelectSeatMovie = findViewById(R.id.tv_dlgselectseat_movie);
        tvDlgSelectSeatTime = findViewById(R.id.tv_dlgselectseat_time);
        rvDlgSelectSeatSeatList = findViewById(R.id.rv_dlgselectseat_seatlist);
        tvDlgSelectSeatTotalPrice = findViewById(R.id.tv_dlgselectseat_totalprice);
        llDlgSelectSeatSelect = findViewById(R.id.ll_dlgselectseat_select);
        tvDlgSelectSeatSelect = findViewById(R.id.tv_dlgselectseat_select);

        tvDlgSelectSeatMovie.setText(movieItem.getMovie());
        tvDlgSelectSeatTime.setText(movieItem.getTime());

        if (movieItem.getSelectSeatCount() > 0) {
            tvDlgSelectSeatSelect.setText(movieItem.getSelectSeatCount() + "자리 선택");
            llDlgSelectSeatSelect.setEnabled(true);
            llDlgSelectSeatSelect.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
            tvDlgSelectSeatTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(movieItem.getTotalPrice()));
        } else {
            tvDlgSelectSeatSelect.setText("선택");
            llDlgSelectSeatSelect.setEnabled(false);
            llDlgSelectSeatSelect.setBackgroundColor(context.getResources().getColor(R.color.gray));
            tvDlgSelectSeatTotalPrice.setText("0");
        }

        movieSeatAdapter = new MovieSeatAdapter(movieItem.getMovieSeatItemArrayList());
        rvDlgSelectSeatSeatList.setLayoutManager(new GridLayoutManager(context, movieItem.getWidth(), GridLayoutManager.VERTICAL, false));
        rvDlgSelectSeatSeatList.setAdapter(movieSeatAdapter);

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgSelectSeatClose.setOnClickListener(view -> {
            dismiss();
        });

        // 아이템 선택 시
        movieSeatAdapter.setOnItemClickListener((view, position) -> {
            movieItem.getMovieSeatItemArrayList().get(position).changeSelected();
            movieSeatAdapter.notifyItemChanged(position);

            if (movieItem.getSelectSeatCount() > 0) {
                tvDlgSelectSeatSelect.setText(movieItem.getSelectSeatCount() + "자리 선택");
                llDlgSelectSeatSelect.setEnabled(true);
                llDlgSelectSeatSelect.setBackgroundColor(context.getResources().getColor(R.color.color_primary));
            } else {
                tvDlgSelectSeatSelect.setText("선택");
                llDlgSelectSeatSelect.setEnabled(false);
                llDlgSelectSeatSelect.setBackgroundColor(context.getResources().getColor(R.color.gray));
            }

            ValueAnimator totalPriceValueAnimator = ValueAnimator.ofInt(lastTotalPrice, movieItem.getTotalPrice());
            totalPriceValueAnimator.setDuration(1000);
            totalPriceValueAnimator.addUpdateListener(valueAnimator -> tvDlgSelectSeatTotalPrice.setText(UsefulFuncManager.convertToCommaPattern((Integer) valueAnimator.getAnimatedValue())));
            totalPriceValueAnimator.start();

            lastTotalPrice = movieItem.getTotalPrice();
        });

        // 선택 버튼 클릭 시
        llDlgSelectSeatSelect.setOnClickListener(view -> {
            this.selectSeatDialogListener.onAddProductClicked(movieItem, new BasketItem(Global.ItemType.MOVIE_TICKET, movieItem.getMovie() + "&" + movieItem.getTime(), movieItem.getSelectSeatListToString(), movieItem.getMovieSeatItemArrayList().get(0).getPrice(), movieItem.getSelectSeatCount()));
            dismiss();
        });
    }

    public interface SelectSeatDialogListener {
        void onAddProductClicked(MovieItem updatedMovieItem, BasketItem basketItem);
    }
}
