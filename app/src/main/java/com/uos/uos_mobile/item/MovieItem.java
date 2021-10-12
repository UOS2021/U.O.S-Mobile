package com.uos.uos_mobile.item;

import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 영화 정보를 담고 있는 클래스.<br><br>
 * 영화관 자리 예약에 맞춤 설계된 클래스로 영호관 내 자리 상태에 대한 정보를 가지고 있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class MovieItem implements Cloneable {
    /**
     * 영화 제목.
     */
    private String movie;

    /**
     * 상영 시간.
     */
    private String time;

    /**
     * 상영관.
     */
    private String theater;

    /**
     * 상영관 내 좌석의 열 개수(빈 공간 포함).
     */
    private int row;

    /**
     * 상영관 내 좌석의 행 개수(빈 공간 포함).
     */
    private int col;

    /**
     * 상영관 내 모든 좌석정보에 대한 리스트.
     */
    private ArrayList<MovieSeatItem> movieSeatItemArrayList = new ArrayList<>();

    /**
     * 장바구니에 MovieItem 저장 시 좌석정보까지 함께 저장하기 위해 Cloneable로부터 오버라이드한 함수.
     * 
     * @return MovieItem 현재 객체와 동일한 정보를 가지고 있는 새로운 MovieItem 객체.
     */
    @Override
    public MovieItem clone() {
        MovieItem movieItem = new MovieItem();

        movieItem.setMovie(this.movie);
        movieItem.setTheater(this.theater);
        movieItem.setTime(this.time);
        movieItem.setRow(this.row);
        movieItem.setCol(this.col);

        ArrayList<MovieSeatItem> movieSeatItemArrayList = new ArrayList<>();

        for (MovieSeatItem movieSeatItem : this.movieSeatItemArrayList) {
            try {
                movieSeatItemArrayList.add(movieSeatItem.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        movieItem.setMovieSeatItemArrayList(movieSeatItemArrayList);

        return movieItem;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ArrayList<MovieSeatItem> getMovieSeatItemArrayList() {
        return movieSeatItemArrayList;
    }

    public void setMovieSeatItemArrayList(ArrayList<MovieSeatItem> movieSeatItemArrayList) {
        this.movieSeatItemArrayList = movieSeatItemArrayList;
    }

    public void setSeatListFromJson(JSONObject recvSeatData) {
        try {
            movie = recvSeatData.getString("movie");
            time = recvSeatData.getString("time");
            theater = recvSeatData.getString("theater");
            row = recvSeatData.getInt("width");
            col = recvSeatData.getInt("height");

            movieSeatItemArrayList.clear();

            JSONArray recvSeatList = recvSeatData.getJSONArray("seat_list");

            int seatCount = 0;
            for (int loop1 = 0; loop1 < col; loop1++) {
                for (int loop2 = 0; loop2 < row; loop2++) {
                    String code = Global.MovieSeat.ROW_ARR[loop1] + (loop2 + 1);

                    JSONObject seatData = recvSeatList.getJSONObject(seatCount);

                    MovieSeatItem movieSeatItem = new MovieSeatItem();
                    if (code.equals(seatData.getString("code"))) {
                        movieSeatItem.setMovieSeatItemFromJson(seatData);
                        seatCount++;
                    } else {
                        movieSeatItem.setState(-1);
                        movieSeatItem.setCode(code);
                        movieSeatItem.setPrice(0);
                    }
                    movieSeatItemArrayList.add(movieSeatItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSelectSeatCount() {
        int selectedCount = 0;

        for (MovieSeatItem movieSeatItem : movieSeatItemArrayList) {
            if (movieSeatItem.getSelected()) {
                selectedCount++;
            }
        }

        return selectedCount;
    }

    public String getSelectSeatListToString() {
        String result = "";

        for (MovieSeatItem movieSeatItem : movieSeatItemArrayList) {
            if (movieSeatItem.getSelected()) {
                result += movieSeatItem.getCode() + "&";
            }
        }

        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public int getTotalPrice() {
        int price = 0;

        for (MovieSeatItem movieSeatItem : movieSeatItemArrayList) {
            if (movieSeatItem.getSelected()) {
                price += movieSeatItem.getPrice();
            }
        }

        return price;
    }

    public void setAllSeatSelected(boolean selected) {
        for (MovieSeatItem movieSeatItem : movieSeatItemArrayList) {
            if (movieSeatItem.getState() == Global.MovieSeat.RESERVATION_AVAILABLE || movieSeatItem.getState() == Global.MovieSeat.SELECTED_SEAT) {
                movieSeatItem.setSelected(selected);
            }
        }
    }
}
