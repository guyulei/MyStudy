package com.guyulei.mystudy.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guyulei.mystudy.R;
import com.guyulei.mystudy.base.BaseActivity;
import com.guyulei.mystudy.view.SaleProgressView;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.lljjcoder.citylist.CityListSelectActivity;
import com.lljjcoder.citylist.bean.CityInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.guyulei.mystudy.R.id.choose_city_ios;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.choose_city)
    Button           mChooseCity;
    @BindView(R.id.choose_city_ios)
    Button           mChooseCityIos;
    @BindView(R.id.saleProgressView)
    SaleProgressView mSaleProgressView;
    @BindView(R.id.seek)
    SeekBar          mSeek;
    @BindView(R.id.animated_svg_view)
    AnimatedSvgView  mAnimatedSvgView;
    private static final String TAG = "MainActivity";
    @BindView(R.id.tofirstactivity)
    TextView    mTofirstactivity;
    @BindView(R.id.tosecondactivity)
    TextView    mTosecondactivity;
    @BindView(R.id.openView)
    TextView    mOpenView;
    @BindView(R.id.openTelView)
    TextView    mOpenTelView;
    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar1;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar2;
    @BindView(R.id.progressBar2button)
    Button      mProgressBar2button;
    @BindView(R.id.dialog)
    TextView    mDialog;
    @BindView(R.id.prodialog)
    TextView    mProdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: MainActivity");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            String savedata = savedInstanceState.getString("savedata");
            setTost(savedata);
        }
        initview();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        String savedata = "guyulei_MainActivity";
        outState.putString("savedata", savedata);
    }

    private void initview() {
        mChooseCity.setOnClickListener(this);
        mChooseCityIos.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSaleProgressView.setTotalAndCurrentCount(100, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mAnimatedSvgView.start();
        mTofirstactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstActivity.startActivity(MainActivity.this, "guyulei", "android");
            }
        });
    }

    @OnClick({R.id.tosecondactivity, R.id.openView, R.id.openTelView, R.id.progressBar2button, R.id.dialog, R.id.prodialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tosecondactivity:
                Intent intent = new Intent("com.guyulei.mystudy.Action_SecondActivity");
                intent.addCategory("com.guyulei.mystudy.Category_SecondActivity");
                startActivity(intent);
                break;
            case R.id.openView:
                Intent intentView = new Intent(Intent.ACTION_VIEW);
                intentView.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intentView);
                break;
            case R.id.openTelView:
                Intent intenttel = new Intent(Intent.ACTION_DIAL);
                intenttel.setData(Uri.parse("tel:15958121433"));
                startActivity(intenttel);
                break;
            case R.id.progressBar2button:
                int visibility = mProgressBar1.getVisibility();
                if (visibility == View.VISIBLE) {
                    mProgressBar1.setVisibility(View.INVISIBLE);
                } else {
                    mProgressBar1.setVisibility(View.VISIBLE);
                }
                int progress = mProgressBar2.getProgress();
                progress = progress + 10;
                mProgressBar2.setProgress(progress);
                break;
            case R.id.dialog:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setTitle("Dialog")
                        .setMessage("guyulei")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                break;
            case R.id.prodialog:
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("正在加载...");
                progressDialog.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu_one:
                setTost("home_menu_one");
                break;
            case R.id.home_menu_two:
                setTost("home_menu_two");
                break;
            case R.id.home_menu_three:
                setTost("home_menu_three");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTost(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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
                mChooseCityIos.setText(province.getName() + ":" + city.getName() + ":" + district.getName());
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
                mChooseCity.setText(cityName + ":" + id);
            }
        }
    }


}
