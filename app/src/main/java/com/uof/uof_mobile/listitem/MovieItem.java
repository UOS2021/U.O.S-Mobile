package com.uof.uof_mobile.listitem;

import com.uof.uof_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieItem implements Cloneable {
    private String movie;
    private String time;
    private String theater;
    private int width;
    private int height;
    private ArrayList<MovieSeatItem> movieSeatItemArrayList = new ArrayList<>();

    public MovieItem clone() {
        MovieItem movieItem = new MovieItem();

        movieItem.setMovie(this.movie);
        movieItem.setTheater(this.theater);
        movieItem.setTime(this.time);
        movieItem.setWidth(this.width);
        movieItem.setHeight(this.height);

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
            width = recvSeatData.getInt("width");
            height = recvSeatData.getInt("height");

            movieSeatItemArrayList.clear();

            JSONArray recvSeatList = recvSeatData.getJSONArray("seat_list");

            int seatCount = 0;
            for (int loop1 = 0; loop1 < height; loop1++) {
                for (int loop2 = 0; loop2 < width; loop2++) {
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
