package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.MovieSeatItem;
import com.uos.uos_mobile.other.Global;

import java.util.ArrayList;

public class MovieSeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MovieSeatItem> movieSeatItemArrayList = new ArrayList<>();  // 예매가능 영화 데이터 목록
    private MovieSeatAdapter.OnItemClickListener onItemClickListener = null;

    public MovieSeatAdapter(ArrayList<MovieSeatItem> movieSeatItemArrayList) {
        this.movieSeatItemArrayList = movieSeatItemArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        if (viewType == Global.MovieSeat.RESERVATION_AVAILABLE) {
            return new MovieSeatAdapter.ReservationAvailableViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_reservationavailable, parent, false));
        } else if (viewType == Global.MovieSeat.ALREADY_RESERVED) {
            return new MovieSeatAdapter.AlreadyReservedViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_alreadyreserved, parent, false));
        } else if (viewType == Global.MovieSeat.UNRESERVED_SEAT) {
            return new MovieSeatAdapter.UnreservedSeatViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_unreservedseat, parent, false));
        } else if (viewType == Global.MovieSeat.SELECTED_SEAT) {
            return new MovieSeatAdapter.SelectedSeatViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_selectedseat, parent, false));
        } else if (viewType == Global.MovieSeat.NONE) {
            return new MovieSeatAdapter.NoneSeatViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_none, parent, false));
        } else {
            return new MovieSeatAdapter.NoneSeatViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_seat_none, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ReservationAvailableViewHolder) {
            ((ReservationAvailableViewHolder) viewHolder).tvReservationAvailableCode.setText(movieSeatItemArrayList.get(position).getCode());
        } else if (viewHolder instanceof AlreadyReservedViewHolder) {
            ((AlreadyReservedViewHolder) viewHolder).tvAlreadyReservedCode.setText(movieSeatItemArrayList.get(position).getCode());
        } else if (viewHolder instanceof UnreservedSeatViewHolder) {
            ((UnreservedSeatViewHolder) viewHolder).tvUnreservedSeatCode.setText(movieSeatItemArrayList.get(position).getCode());
        } else if (viewHolder instanceof SelectedSeatViewHolder) {
            ((SelectedSeatViewHolder) viewHolder).tvSelectedSeatCode.setText(movieSeatItemArrayList.get(position).getCode());
        }
    }

    public void setOnItemClickListener(MovieSeatAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return movieSeatItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (movieSeatItemArrayList.get(position).getSelected()) {
            return Global.MovieSeat.SELECTED_SEAT;
        } else {
            return movieSeatItemArrayList.get(position).getState();
        }
    }

    public void setMovieSeatItemArrayList(ArrayList<MovieSeatItem> movieSeatItemArrayList) {
        this.movieSeatItemArrayList = movieSeatItemArrayList;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 예약가능 자리 (예약 가능)
    public class ReservationAvailableViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clReservationAvailable;
        public AppCompatTextView tvReservationAvailableCode;

        public ReservationAvailableViewHolder(View view) {
            super(view);
            clReservationAvailable = view.findViewById(com.uos.uos_mobile.R.id.cl_reservationavailable);
            tvReservationAvailableCode = view.findViewById(com.uos.uos_mobile.R.id.tv_reservationavailable_code);

            clReservationAvailable.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    // 예약가능 자리(예약됨)
    public class AlreadyReservedViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clAlreadyReserved;
        public AppCompatTextView tvAlreadyReservedCode;

        public AlreadyReservedViewHolder(View view) {
            super(view);
            clAlreadyReserved = view.findViewById(com.uos.uos_mobile.R.id.cl_alreadyreserved);
            tvAlreadyReservedCode = view.findViewById(com.uos.uos_mobile.R.id.tv_alreadyreserved_code);
        }
    }

    // 예약불가 자리
    public class UnreservedSeatViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clUnreservedSeat;
        public AppCompatTextView tvUnreservedSeatCode;

        public UnreservedSeatViewHolder(View view) {
            super(view);
            clUnreservedSeat = view.findViewById(com.uos.uos_mobile.R.id.cl_unreservedseat);
            tvUnreservedSeatCode = view.findViewById(com.uos.uos_mobile.R.id.tv_unreservedseat_code);
        }
    }

    // 선택된 자리
    public class SelectedSeatViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clSelectedSeat;
        public AppCompatTextView tvSelectedSeatCode;

        public SelectedSeatViewHolder(View view) {
            super(view);
            clSelectedSeat = view.findViewById(com.uos.uos_mobile.R.id.cl_selectedseat);
            tvSelectedSeatCode = view.findViewById(com.uos.uos_mobile.R.id.tv_selectedseat_code);

            clSelectedSeat.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    // 빈공간
    public class NoneSeatViewHolder extends RecyclerView.ViewHolder {
        public NoneSeatViewHolder(View view) {
            super(view);
        }
    }
}
