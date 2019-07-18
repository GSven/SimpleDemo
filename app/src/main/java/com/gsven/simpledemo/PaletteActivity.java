package com.gsven.simpledemo;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_PALETTE)
public class PaletteActivity extends BaseActivity {
    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public int getContentView() {
        return R.layout.activity_palette;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    //    private int index = 0;
//    private int max = 4;
    @Override
    protected void initViews() {
//        setSupportActionBar(toolbar);
//        fab.setOnClickListener(view -> {
//            index++;
//            if (index > max) {
//                index = 0;
//            }
//
//            int id = images[index];
//            mImageView.setImageResource(id);
//            magic(id);
//        });

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {

        });
        magic(R.mipmap.test);
    }

    private void magic(int id) {
        Palette.Builder builder = new Palette.Builder(BitmapFactory.decodeResource(getResources(), id));
        Palette palette = builder.generate();
        if(palette !=null){
            Palette.Swatch swatch = getSwatch(palette);
            if (swatch != null) {
                mContent.setTextColor(swatch.getRgb());

                mToolbarLayout.setExpandedTitleColor(Color.WHITE);
                mToolbarLayout.setCollapsedTitleTextColor(swatch.getTitleTextColor());
                mToolbarLayout.setBackgroundColor(swatch.getRgb());
                mToolbarLayout.setContentScrimColor(swatch.getRgb());
                mToolbarLayout.setTitle(String.valueOf(swatch.getRgb()));
                fab.setBackgroundColor(swatch.getRgb());
                fab.setRippleColor(swatch.getRgb());
                StatusBarUtil.setStatusBarColor(PaletteActivity.this, swatch.getRgb());
            }
        }
    }

    private Palette.Swatch getSwatch(Palette palette) {
        return palette.getVibrantSwatch();
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void setListeners() {

    }
}
