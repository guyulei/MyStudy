package com.guyulei.mystudy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.guyulei.mystudy.view.SaleProgressView;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.lljjcoder.citylist.CityListSelectActivity;
import com.lljjcoder.citylist.bean.CityInfoBean;

import butterknife.ButterKnife;

import static com.guyulei.mystudy.R.id.choose_city_ios;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChoosecity;
    private Button mChoosecityios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initview();

    }

    private void initview() {
        mChoosecity = (Button) findViewById(R.id.choose_city);
        mChoosecity.setOnClickListener(this);
        mChoosecityios = (Button) findViewById(choose_city_ios);
        mChoosecityios.setOnClickListener(this);

        final SaleProgressView saleProgressView = (SaleProgressView) findViewById(R.id.saleProgressView);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                saleProgressView.setTotalAndCurrentCount(100, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AnimatedSvgView animatedsvgview = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        animatedsvgview.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_city:
                Intent intent = new Intent(MainActivity.this, CityListSelectActivity.class);
                startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);
                break;
            case choose_city_ios:
                choose_city_ios();
                break;
        }
    }

    private void choose_city_ios() {
        CityPickerView cityPicker = new CityPickerView.Builder(MainActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#234Dfa")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //返回结果
                //ProvinceBean 省份信息
                //CityBean     城市信息
                //DistrictBean 区县信息
                mChoosecityios.setText(province.getName() + ":" + city.getName() + ":" + district.getName());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                Bundle bundle = data.getExtras();
                CityInfoBean cityInfoBean = (CityInfoBean) bundle.getParcelable("cityinfo");
                if (null == cityInfoBean) {
                    return;
                }
                //城市名称
                String cityName = cityInfoBean.getName();
                int id = cityInfoBean.getId();
                mChoosecity.setText(cityName + ":" + id);
            }
        }
    }
}
