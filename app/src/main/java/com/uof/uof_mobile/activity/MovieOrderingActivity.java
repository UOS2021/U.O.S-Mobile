package com.uof.uof_mobile.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.MovieOrderingAdapter;
import com.uof.uof_mobile.adapter.OrderingAdapter;
import com.uof.uof_mobile.dialog.BasketDialog;
import com.uof.uof_mobile.dialog.SelectProductDialog;
import com.uof.uof_mobile.dialog.SelectSeatDialog;
import com.uof.uof_mobile.dialog.SelectSetDialog;
import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.MovieItem;
import com.uof.uof_mobile.item.OrderingProductItem;
import com.uof.uof_mobile.item.OrderingSetItem;
import com.uof.uof_mobile.manager.BasketManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieOrderingActivity extends AppCompatActivity {
    private final String tempJson = "{\n" +
            "    \"response_code\": \"0007\"\n" +
            "    , \"message\":\n" +
            "    {\n" +
            "        \"company\":\n" +
            "        {\n" +
            "            \"name\": \"CGV\"\n" +
            "            , \"type\": \"movie\"\n" +
            "        }\n" +
            "        , \"movie_list\":\n" +
            "        [\n" +
            "            {\n" +
            "                \"movie\": \"트랜스포머3\"\n" +
            "                , \"time\": \"10:00\"\n" +
            "                , \"theater\": \"1관\"\n" +
            "                , \"width\": 4\n" +
            "                , \"height\": 4\n" +
            "                , \"seat_list\":\n" +
            "                [\n" +
            "                    { \"code\": \"A1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E4\", \"state\": 0, \"price\": 1000 }\n" +
            "                ]\n" +
            "            }, {\n" +
            "                \"movie\": \"인타임\"\n" +
            "                , \"time\": \"12:00\"\n" +
            "                , \"theater\": \"3관\"\n" +
            "                , \"width\": 3\n" +
            "                , \"height\": 3\n" +
            "                , \"seat_list\":\n" +
            "                [\n" +
            "                    { \"code\": \"A1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C3\", \"state\": 0, \"price\": 1000 }\n" +
            "                ]\n" +
            "            }, {\n" +
            "                \"movie\": \"분노의질주: 더 얼티메이트\"\n" +
            "                , \"time\": \"15:00\"\n" +
            "                , \"theater\": \"2관\"\n" +
            "                , \"width\": 2\n" +
            "                , \"height\": 2\n" +
            "                , \"seat_list\":\n" +
            "                [\n" +
            "                    { \"code\": \"A1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B2\", \"state\": 0, \"price\": 1000 }\n" +
            "                ]\n" +
            "            }, {\n" +
            "                \"movie\": \"인셉션\"\n" +
            "                , \"time\": \"23:00\"\n" +
            "                , \"theater\": \"9관\"\n" +
            "                , \"width\": 10\n" +
            "                , \"height\": 10\n" +
            "                , \"seat_list\":\n" +
            "                [\n" +
            "                    { \"code\": \"A1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A4\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A5\", \"state\": 2, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A8\", \"state\": 2, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A9\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"A10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B7\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B8\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B9\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"B10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C2\", \"state\": 2, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C4\", \"state\": 2, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"C10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"D10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E4\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E5\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E6\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E7\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E8\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"E10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"F10\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G1\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G2\", \"state\": 1, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"G10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"H10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"I10\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J1\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J2\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J3\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J4\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J5\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J6\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J7\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J8\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J9\", \"state\": 0, \"price\": 1000 }\n" +
            "                    , { \"code\": \"J10\", \"state\": 0, \"price\": 1000 }\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "        , \"category_list\":\n" +
            "        [\n" +
            "            {\n" +
            "                \"category\": \"세트\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"창녕 갈릭 버거 세트\"\n" +
            "                        , \"price\": 8500\n" +
            "                        , \"conf\": \"버거 + 후렌치후라이 (M) + 콜라 (M)\"\n" +
            "                        , \"desc\": \"100% 국내산 창녕 햇마늘로 만든 토핑과 마늘 아이올리 30g의 풍미가 육즙을 꽉 가둬낸 순쇠고기 패티 2장과 만나 조화를 이뤄 풍부한 맛\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                        , \"category_list\":\n" +
            "                        [\n" +
            "                            {\n" +
            "                                 \"category\" : \"사이드\"\n" +
            "                                , \"required\" : true\n" +
            "                                , \"product_list\":\n" +
            "                                [\n" +
            "                                    {\n" +
            "                                        \"name\": \"맥너겟 4조각 (소스1종 랜덤증정)\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"골든 모짜렐라 치즈스틱 2조각\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                ] \n" +
            "                            }, {\n" +
            "                                 \"category\" : \"음료\"\n" +
            "                                , \"required\" : true\n" +
            "                                , \"product_list\":\n" +
            "                                [\n" +
            "                                    {\n" +
            "                                        \"name\": \"코카 콜라 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"환타 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"스프라이트 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"코카 콜라 제로 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"커피 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"바닐라 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"딸기 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"초코 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"드립 커피 미디엄\"\n" +
            "                                        , \"price\": 100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                ] \n" +
            "                            }\n" +
            "                        ]\n" +
            "                    }, {\n" +
            "                        \"name\": \"트리플 치즈버거 세트\"\n" +
            "                        , \"price\": 8500\n" +
            "                        , \"conf\": \"트리플 치즈버거 + 후렌치후라이 (M) + 콜라 (M)\"\n" +
            "                        , \"desc\": \"부드러운 치즈와 풍부한 육즙의 패티를 세배 더 진하게 즐길 수 있는 트리플 치즈버거\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                        , \"category_list\":\n" +
            "                        [\n" +
            "                            {\n" +
            "                                 \"category\" : \"사이드\"\n" +
            "                                , \"required\" : true\n" +
            "                                , \"product_list\":\n" +
            "                                [\n" +
            "                                    {\n" +
            "                                        \"name\": \"맥너겟 4조각 (소스1종 랜덤증정)\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"골든 모짜렐라 치즈스틱 2조각\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                ] \n" +
            "                            }, {\n" +
            "                                 \"category\" : \"음료\"\n" +
            "                                , \"required\" : true\n" +
            "                                , \"product_list\":\n" +
            "                                [\n" +
            "                                    {\n" +
            "                                        \"name\": \"코카 콜라 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"환타 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"스프라이트 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"코카 콜라 제로 미디엄\"\n" +
            "                                        , \"price\": 0\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"커피 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"바닐라 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"딸기 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"초코 쉐이크 미디엄\"\n" +
            "                                        , \"price\": 1100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                    , {\n" +
            "                                        \"name\": \"드립 커피 미디엄\"\n" +
            "                                        , \"price\": 100\n" +
            "                                        , \"desc\": \"추가비용 없음\"\n" +
            "                                        , \"image\": \"imagedata\"\n" +
            "                                    }\n" +
            "                                ] \n" +
            "                            }\n" +
            "                        ]\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "            , {\n" +
            "                \"category\": \"X\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"몬스터X\"\n" +
            "                        , \"price\": 8800\n" +
            "                        , \"desc\": \"\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"통새우X\"\n" +
            "                        , \"price\": 8800\n" +
            "                        , \"desc\": \"\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"콰트로치즈X\"\n" +
            "                        , \"price\": 8800\n" +
            "                        , \"desc\": \"\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }\n" +
            "            , {\n" +
            "                \"category\": \"와퍼\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"불고기와퍼\"\n" +
            "                        , \"price\": 7000\n" +
            "                        , \"desc\": \"불에 직접 구운 순 쇠고기 패티가 들어간 와퍼에 달콤한 불고기 소스까지\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"새우버거\"\n" +
            "                        , \"price\": 2500\n" +
            "                        , \"desc\": \"다진 새우 패티가 들어간 버거\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"콰트로치즈와퍼\"\n" +
            "                        , \"price\": 7800\n" +
            "                        , \"desc\": \"진짜 불맛을 즐겨라, 4가지 고품격 치즈와 불에 직접 구운 와퍼 패티의 만남\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"통새우와퍼\"\n" +
            "                        , \"price\": 7800\n" +
            "                        , \"desc\": \"직화 방식으로 구운 100% 순쇠고기 패티에 갈릭페퍼 통새우와 스파이시 토마토소스가 더해진 프리미엄 버거\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"베이컨치즈와퍼\"\n" +
            "                        , \"price\": 8800\n" +
            "                        , \"desc\": \"풍미 가득한 아메리칸 클래식의 완벽한 조화\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"리얼와퍼\"\n" +
            "                        , \"price\": 7000\n" +
            "                        , \"desc\": \"100% 직화의 원칙, 신선한 재료를 준비하는 원칙, 믿고 먹을 수 있는 와퍼를 지금 버거킹에서 만나보세요\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"몬스터와퍼\"\n" +
            "                        , \"price\": 9400\n" +
            "                        , \"desc\": \"불맛 가득 순쇠고기, 치킨, 베이컨에 화끈한 디아블로 소스의 압도적인 맛\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }\n" +
            "            , {\n" +
            "                \"category\": \"비프&치킨버거\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"아기상어 새우버거\"\n" +
            "                        , \"price\": 4800\n" +
            "                        , \"desc\": \"더 통통한 새우로 돌아왔다\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"엄마상어 새우버거\"\n" +
            "                        , \"price\": 5800\n" +
            "                        , \"desc\": \"더 통통한 새우로 돌아왔다\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"아빠상어 새우버거\"\n" +
            "                        , \"price\": 6800\n" +
            "                        , \"desc\": \"더 통통한 새우로 돌아왔다\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"고추장소불고기버거\"\n" +
            "                        , \"price\": 5200\n" +
            "                        , \"desc\": \"부드러운 소불고기에 매콤함을 더하다\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"직화소불고기버거\"\n" +
            "                        , \"price\": 4800\n" +
            "                        , \"desc\": \"패티까지 진짜 불고기\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"바비큐킹치킨버거\"\n" +
            "                        , \"price\": 3800\n" +
            "                        , \"desc\": \"진한 바비큐 소스가 듬뿍\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"롱치킨버거\"\n" +
            "                        , \"price\": 5300\n" +
            "                        , \"desc\": \"담백한 치킨 패티에 부드러운 마요네즈 소스와 싱싱한 야채가 듬뿍~\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }\n" +
            "            , {\n" +
            "                \"category\": \"디저트\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"오레오 맥플러리\"\n" +
            "                        , \"price\": 3200\n" +
            "                        , \"desc\": \"우유 듬뿍 신선한 아이스크림에 아삭아삭 오레오 쿠키가 가득\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"애플파이\"\n" +
            "                        , \"price\": 1900\n" +
            "                        , \"desc\": \"바각한 파이 속 사과 과육이 아삭, 애플파이\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"딸기 선데이 아이스크림\"\n" +
            "                        , \"price\": 2200\n" +
            "                        , \"desc\": \"신선한 우유와 딸기가 아이스크림으로 만났다\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }\n" +
            "            , {\n" +
            "                \"category\": \"사이드\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"쉐이킹프라이 구운갈릭\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"구운 마늘의 깊은 풍미\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"쉐이킹프라이 매콤치즈\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"고소한 치즈맛에 매콤함까지\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"쉐이킹프라이 스윗어니언\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"달콤 고소한 단짠의 정석\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"바삭킹 8조각, 스위트칠리소스\"\n" +
            "                        , \"price\": 12100\n" +
            "                        , \"desc\": \"매콤하게 바삭하게 튀긴 치킨윙\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"바삭킹 8조각, 까망베르치즈소스\"\n" +
            "                        , \"price\": 12100\n" +
            "                        , \"desc\": \"매콤하게 바삭하게 튀긴 치킨윙\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"바삭킹 8조각, 디아블로소스\"\n" +
            "                        , \"price\": 12100\n" +
            "                        , \"desc\": \"매콤하게 바삭하게 튀긴 치킨윙\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"바삭킹 2조각\"\n" +
            "                        , \"price\": 3500\n" +
            "                        , \"desc\": \"매콤하게 바삭하게 튀긴 치킨윙\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }, {\n" +
            "                \"category\": \"음료\"\n" +
            "                , \"product_list\":\n" +
            "                [\n" +
            "                    {\n" +
            "                        \"name\": \"제로톡톡 청포도\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"제로 칼로리 100% 천연 과일향 드링크\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                    , {\n" +
            "                        \"name\": \"아이스 아메리카노\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"RA인증을 받은 자연을 담은 아메리카노\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"아이스초코\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"시원하게 즐기는 진~한 초코\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"씨그램\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"코카콜라 L\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"코카콜라로 더 짜릿하게\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"코카콜라 제로 L\"\n" +
            "                        , \"price\": 2700\n" +
            "                        , \"desc\": \"100% 짜릿함, 칼로리는 제로\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }, {\n" +
            "                        \"name\": \"코카콜라 제로 R\"\n" +
            "                        , \"price\": 2500\n" +
            "                        , \"desc\": \"100% 짜릿함, 칼로리는 제로\"\n" +
            "                        , \"image\": \"imagedata\"\n" +
            "                    }\n" +
            "                ]\n" +
            "                , \"set_list\":\n" +
            "                [\n" +
            "                ]\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";
    private AppCompatImageButton ibtnMovieOrderingBack;
    private AppCompatTextView tvMovieOrderingCompanyName;
    private AppCompatTextView tvMovieOrderingShowMovie;
    private AppCompatTextView tvMovieOrderingShowFood;
    private LinearLayoutCompat llMovieOrderingMovie;
    private RecyclerView rvMovieOrderingMovieList;
    private LinearLayoutCompat llMovieOrderingFood;
    private ChipGroup cgMovieOrderingCategoryList;
    private RecyclerView rvMovieOrderingProductList;
    private LinearLayoutCompat llMovieOrderingSelected;
    private AppCompatTextView tvMovieOrderingTotalPrice;
    private AppCompatTextView tvMovieOrderingProductCount;
    private LinearLayoutCompat llMovieOrderingPay;
    private JSONObject companyData;
    private JSONArray movieData;
    private JSONArray productData;
    private MovieOrderingAdapter movieOrderingAdapter;
    private OrderingAdapter orderingAdapter;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieordering);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        Global.basketManager.getOrderingItemArrayList().clear();
        if (Global.socketManager != null && Global.socketManager.isSocketConnected()) {
            Global.socketManager.disconnect();
        }
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);
        ibtnMovieOrderingBack = findViewById(R.id.ibtn_movieordering_back);
        tvMovieOrderingCompanyName = findViewById(R.id.tv_movieordering_companyname);
        tvMovieOrderingShowMovie = findViewById(R.id.tv_movieordering_showmovie);
        tvMovieOrderingShowFood = findViewById(R.id.tv_movieordering_showfood);
        llMovieOrderingMovie = findViewById(R.id.ll_movieordering_movie);
        rvMovieOrderingMovieList = findViewById(R.id.rv_movieordering_movielist);
        llMovieOrderingFood = findViewById(R.id.ll_movieordering_food);
        cgMovieOrderingCategoryList = findViewById(R.id.cg_movieordering_categorylist);
        rvMovieOrderingProductList = findViewById(R.id.rv_movieordering_productlist);
        llMovieOrderingSelected = findViewById(R.id.ll_movieordering_selected);
        llMovieOrderingSelected = findViewById(R.id.ll_movieordering_selected);
        tvMovieOrderingTotalPrice = findViewById(R.id.tv_movieordering_totalprice);
        tvMovieOrderingProductCount = findViewById(R.id.tv_movieordering_productcount);
        llMovieOrderingPay = findViewById(R.id.ll_movieordering_order);

        // UI 초기 상태 설정
        tvMovieOrderingShowMovie.setEnabled(false);
        tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(R.color.recyclerview_background));
        llMovieOrderingMovie.setVisibility(View.VISIBLE);
        tvMovieOrderingShowFood.setEnabled(true);
        tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(R.color.gray));
        llMovieOrderingFood.setVisibility(View.GONE);
        Intent loadData = getIntent();

        try {
            //companyData = new JSONObject(loadData.getStringExtra("companyData"));
            //productData = new JSONArray(loadData.getStringExtra("productData"));
            companyData = new JSONObject(tempJson).getJSONObject("message").getJSONObject("company");
            movieData = new JSONObject(tempJson).getJSONObject("message").getJSONArray("movie_list");
            productData = new JSONObject(tempJson).getJSONObject("message").getJSONArray("category_list");
            tvMovieOrderingCompanyName.setText(companyData.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MovieOrderingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 영화 목록 Adapter 설정
        movieOrderingAdapter = new MovieOrderingAdapter();
        movieOrderingAdapter.setJson(movieData);
        rvMovieOrderingMovieList.setLayoutManager(new LinearLayoutManager(MovieOrderingActivity.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MovieOrderingActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvMovieOrderingMovieList.addItemDecoration(dividerItemDecoration);
        rvMovieOrderingMovieList.setAdapter(movieOrderingAdapter);

        // 상품 목록 Adapter 설정
        orderingAdapter = new OrderingAdapter();
        orderingAdapter.setJson(productData);
        rvMovieOrderingProductList.setLayoutManager(new GridLayoutManager(MovieOrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvMovieOrderingProductList.setAdapter(orderingAdapter);
        Global.basketManager = new BasketManager(tvMovieOrderingCompanyName.getText().toString());

        updatePriceInfo();

        // 카테고리를 chipgroup에 추가
        for (int loop = 0; loop < productData.length(); loop++) {
            Chip chip = (Chip) MovieOrderingActivity.this.getLayoutInflater().inflate(R.layout.chip_category, cgMovieOrderingCategoryList, false);
            try {
                chip.setText(productData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    orderingAdapter.setSelectedCategory(selectedCategory);
                    rvMovieOrderingProductList.setAdapter(orderingAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (loop == 0) {
                selectedCategory = chip.getText().toString();
                orderingAdapter.setSelectedCategory(selectedCategory);
                rvMovieOrderingProductList.setAdapter(orderingAdapter);
                chip.setChecked(true);
            }
            cgMovieOrderingCategoryList.addView(chip);
        }

        // 뒤로가기 버튼이 눌렸을 경우
        ibtnMovieOrderingBack.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(MovieOrderingActivity.this, R.style.AlertDialogTheme)
                    .setTitle("주문 취소")
                    .setMessage("주문창에서 나가시겠습니까?")
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                        finish();
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {
                    }).create();

            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            });
            alertDialog.show();
        });

        // 영화 표시 버튼이 눌렸을 때
        tvMovieOrderingShowMovie.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(false);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(R.color.recyclerview_background));
            llMovieOrderingMovie.setVisibility(View.VISIBLE);

            tvMovieOrderingShowFood.setEnabled(true);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(R.color.gray));
            llMovieOrderingFood.setVisibility(View.GONE);
        });

        // 음식 표시 버튼이 눌렸을 때
        tvMovieOrderingShowFood.setOnClickListener(view -> {
            tvMovieOrderingShowMovie.setEnabled(true);
            tvMovieOrderingShowMovie.setBackgroundColor(getResources().getColor(R.color.gray));
            llMovieOrderingMovie.setVisibility(View.GONE);

            tvMovieOrderingShowFood.setEnabled(false);
            tvMovieOrderingShowFood.setBackgroundColor(getResources().getColor(R.color.recyclerview_background));
            llMovieOrderingFood.setVisibility(View.VISIBLE);
        });

        // 리스트 아이템이 눌렸을 경우
        orderingAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = orderingAdapter.getItem(position);

            if (orderingProductItem.getType() == Global.ItemType.PRODUCT) {
                // 선택된 아이템이 단일상품일 경우
                new SelectProductDialog(MovieOrderingActivity.this, orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            } else {
                // 선택된 아이템이 세트상품일 경우
                new SelectSetDialog(MovieOrderingActivity.this, (OrderingSetItem) orderingProductItem, (orderingItem) -> {
                    if (orderingItem.getCount() >= 1) {
                        Global.basketManager.addItem(orderingItem);
                        updatePriceInfo();
                    }
                }).show();
            }
        });

        // 선택정보창 버튼이 눌렸을 경우
        llMovieOrderingSelected.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(MovieOrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                BasketDialog basketDialog = new BasketDialog(MovieOrderingActivity.this);
                basketDialog.setOnDismissListener(dialogInterface -> {
                    updatePriceInfo();
                });
                basketDialog.show();
            }
        });

        // 결제 버튼이 눌렸을 경우
        llMovieOrderingPay.setOnClickListener(view -> {
            if (Global.basketManager.getOrderCount() == 0) {
                Toast.makeText(MovieOrderingActivity.this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MovieOrderingActivity.this, PayActivity.class));
            }
        });

        // 영화 선택 시
        movieOrderingAdapter.setOnItemClickListener((view, position) -> new SelectSeatDialog(MovieOrderingActivity.this, movieOrderingAdapter.getItem(position), new SelectSeatDialog.SelectSeatDialogListener() {
            @Override
            public void onAddProductClicked(MovieItem updatedMovieItem, BasketItem basketItem) {
                movieOrderingAdapter.setItem(position, updatedMovieItem.clone());
                basketItem.setMovieItem(movieOrderingAdapter.getItem(position));

                for (BasketItem basketItem1 : Global.basketManager.getOrderingItemArrayList()) {
                    if (basketItem1.getMenu().equals(basketItem.getMenu())) {
                        Global.basketManager.getOrderingItemArrayList().remove(basketItem1);
                    }
                }
                Global.basketManager.getOrderingItemArrayList().add(basketItem);
                updatePriceInfo();
            }
        }).show());
    }

    private void updatePriceInfo() {
        tvMovieOrderingTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderPrice()));
        tvMovieOrderingProductCount.setText(String.valueOf(Global.basketManager.getOrderCount()));

        if (Global.basketManager.getOrderCount() == 0) {
            llMovieOrderingSelected.setEnabled(false);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(R.color.gray));
            llMovieOrderingPay.setEnabled(false);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            llMovieOrderingSelected.setEnabled(true);
            llMovieOrderingSelected.setBackgroundColor(getResources().getColor(R.color.color_primary));
            llMovieOrderingPay.setEnabled(true);
            llMovieOrderingPay.setBackgroundColor(getResources().getColor(R.color.color_primary));
        }
    }
}