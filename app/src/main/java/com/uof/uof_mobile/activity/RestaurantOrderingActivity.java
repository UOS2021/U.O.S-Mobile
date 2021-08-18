package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.SelectProductDialog;
import com.uof.uof_mobile.manager.RestaurantBascketManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingAdapter;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestaurantOrderingActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnRestaurantOrderingBack;
    private AppCompatTextView tvRestaurantOrderingCompanyName;
    private ChipGroup cgRestaurantOrderingCategoryList;
    private RecyclerView rvRestaurantOrderingProductList;
    private LinearLayoutCompat llRestaurantOrderingSelected;
    private AppCompatTextView tvRestaurantOrderingPayAmount;
    private AppCompatTextView tvRestaurantOrderingSelectedCount;
    private LinearLayoutCompat llRestaurantOrderingPay;
    private JSONObject companyData;
    private JSONArray productData;

    private RestaurantOrderingAdapter restaurantOrderingAdapter;
    private RestaurantBascketManager restaurantBascketManager;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantordering);

        init();
    }

    private void init() {
        ibtnRestaurantOrderingBack = findViewById(R.id.ibtn_restaurantordering_back);
        tvRestaurantOrderingCompanyName = findViewById(R.id.tv_restaurantordering_companyname);
        cgRestaurantOrderingCategoryList = findViewById(R.id.cg_restaurantordering_categorylist);
        rvRestaurantOrderingProductList = findViewById(R.id.rv_restaurantordering_productlist);
        llRestaurantOrderingSelected = findViewById(R.id.ll_restaurantordering_selected);
        tvRestaurantOrderingPayAmount = findViewById(R.id.tv_restaurantordering_payamount);
        tvRestaurantOrderingSelectedCount = findViewById(R.id.tv_restaurantordering_selectedcount);
        llRestaurantOrderingPay = findViewById(R.id.ll_restaurantordering_pay);

        tvRestaurantOrderingPayAmount.setText("0");
        tvRestaurantOrderingSelectedCount.setText("0");

        Intent loadData = getIntent();

        try {
            //companyData = new JSONObject(loadData.getStringExtra("companyData"));
            //productData = new JSONArray(loadData.getStringExtra("productData"));
            companyData = new JSONObject(tempJson).getJSONObject("message").getJSONObject("company");
            productData = new JSONObject(tempJson).getJSONObject("message").getJSONArray("category_list");
            tvRestaurantOrderingCompanyName.setText(companyData.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RestaurantOrderingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 상품 목록 Adapter 설정
        restaurantOrderingAdapter = new RestaurantOrderingAdapter();
        restaurantOrderingAdapter.setJson(productData);
        rvRestaurantOrderingProductList.setLayoutManager(new GridLayoutManager(RestaurantOrderingActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvRestaurantOrderingProductList.setAdapter(restaurantOrderingAdapter);
        restaurantBascketManager = new RestaurantBascketManager();

        // 카테고리를 chipgroup에 추가
        for (int loop = 0; loop < productData.length(); loop++) {
            Chip chip = (Chip) RestaurantOrderingActivity.this.getLayoutInflater().inflate(R.layout.chip_category, cgRestaurantOrderingCategoryList, false);
            try {
                chip.setText(productData.getJSONObject(loop).getString("category"));
                chip.setOnClickListener(view -> {
                    selectedCategory = chip.getText().toString();
                    restaurantOrderingAdapter.setSelectedCategory(selectedCategory);
                    rvRestaurantOrderingProductList.setAdapter(restaurantOrderingAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            cgRestaurantOrderingCategoryList.addView(chip);
            if (loop == 0) {
                selectedCategory = chip.getText().toString();
                restaurantOrderingAdapter.setSelectedCategory(selectedCategory);
                rvRestaurantOrderingProductList.setAdapter(restaurantOrderingAdapter);
                chip.setChecked(true);
            }
        }

        // 뒤로가기 버튼이 눌렸을 경우
        ibtnRestaurantOrderingBack.setOnClickListener(view -> {
            finish();
        });

        // 리스트 아이템이 눌렸을 경우
        restaurantOrderingAdapter.setOnItemClickListener((view, position) -> {
            RestaurantOrderingProductItem restaurantOrderingItem = restaurantOrderingAdapter.getItem(position);

            if (restaurantOrderingItem.getType() == Constants.ItemType.PRODUCT) {
                new SelectProductDialog(RestaurantOrderingActivity.this, restaurantOrderingItem, (count) -> {
                    int currentPayAmount = Integer.parseInt(tvRestaurantOrderingPayAmount.getText().toString().replace(",", ""));
                    int currentSelectedCount = Integer.parseInt(tvRestaurantOrderingSelectedCount.getText().toString());

                    restaurantBascketManager.addItem(restaurantOrderingItem, count);
                    tvRestaurantOrderingPayAmount.setText(UsefulFuncManager.convertToCommaPattern(currentPayAmount + restaurantOrderingItem.getPrice() * count));
                    tvRestaurantOrderingSelectedCount.setText(String.valueOf(currentSelectedCount + count));
                }).show();
            }
        });

        // 선택정보창 버튼이 눌렸을 경우
        llRestaurantOrderingSelected.setOnClickListener(view -> {

        });

        // 결제 버튼이 눌렸을 경우
        llRestaurantOrderingPay.setOnClickListener(view -> {
            Intent intent = new Intent(RestaurantOrderingActivity.this, PayActivity.class);
            startActivity(intent);
        });
    }

    //private String tempJson = "{\"message\":{\"company\":{\"name\":\"맥도날드\"},\"category_list\":[{\"category\":\"테스트카테고리\",\"product_list\":[{\"name\":\"상품1\",\"price\":500,\"desc\":\"상품1입니다\",\"image\":\"imgdata\"},{\"name\":\"상품2\",\"price\":700,\"desc\":\"상품2입니다\",\"image\":\"imgdata\"},{\"name\":\"상품3\",\"price\":900,\"desc\":\"상품3입니다\",\"image\":\"imgdata\"},{\"name\":\"상품4\",\"price\":1000,\"desc\":\"상품4입니다\",\"image\":\"imgdata\"}],\"set_list\":[{\"name\":\"세트1\",\"price\":3000,\"desc\":\"세트1입니다\",\"image\":\"imgdata\",\"product_list\":[{\"name\":\"서브상품1\",\"price\":500,\"desc\":\"서브상품1입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품2\",\"price\":500,\"desc\":\"서브상품2입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품3\",\"price\":500,\"desc\":\"서브상품3입니다\",\"image\":\"imgdata\"},{\"name\":\"서브상품4\",\"price\":500,\"desc\":\"서브상품4입니다\",\"image\":\"imgdata\"}]}]}]}}";
    private String tempJson = "{\n" +
            "    \"response_code\": \"0007\"\n" +
            "    , \"message\":\n" +
            "    {\n" +
            "        \"company\":\n" +
            "        {\n" +
            "            \"name\": \"버거킹\"\n" +
            "            , \"type\": \"restaurant\"\n" +
            "        }\n" +
            "        , \"category_list\":\n" +
            "        [\n" +
            "            {\n" +
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
}