package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.listitem.MovieItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieOrderingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<MovieItem> movieItemArrayList = new ArrayList<>();  // 예매가능 영화 데이터 목록
    private MovieOrderingAdapter.OnItemClickListener onItemClickListener = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new MovieOrderingAdapter.MovieViewHolder(layoutInflater.inflate(R.layout.item_movieordering, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((MovieViewHolder) viewHolder).tvMovieOrderingMovie.setText(movieItemArrayList.get(position).getMovie());
        ((MovieViewHolder) viewHolder).tvMovieOrderingTime.setText(movieItemArrayList.get(position).getTime());
        ((MovieViewHolder) viewHolder).tvMovieOrderingTheater.setText(movieItemArrayList.get(position).getTheater());
    }

    public void setOnItemClickListener(MovieOrderingAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList.size();
    }

    public MovieItem getItem(int position) {
        return movieItemArrayList.get(position);
    }

    public void setItem(int position, MovieItem movieItem) {
        movieItemArrayList.set(position, movieItem);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 영화 뷰 관리자
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clMovieOrdering;
        public AppCompatTextView tvMovieOrderingMovie;
        public AppCompatTextView tvMovieOrderingTime;
        public AppCompatTextView tvMovieOrderingTheater;

        public MovieViewHolder(View view) {
            super(view);
            clMovieOrdering = view.findViewById(R.id.cl_movieordering);
            tvMovieOrderingMovie = view.findViewById(R.id.tv_movieordering_movie);
            tvMovieOrderingTime = view.findViewById(R.id.tv_movieordering_time);
            tvMovieOrderingTheater = view.findViewById(R.id.tv_movieordering_theater);

            clMovieOrdering.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    public void setJson(JSONArray jsonArray) {
        movieItemArrayList.clear();

        for (int loop = 0; loop < jsonArray.length(); loop++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(loop);

                MovieItem movieItem = new MovieItem();
                movieItem.setSeatListFromJson(jsonObject);

                movieItemArrayList.add(movieItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
