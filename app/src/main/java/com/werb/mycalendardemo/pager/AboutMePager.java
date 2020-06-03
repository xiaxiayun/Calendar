package com.werb.mycalendardemo.pager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.Events;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by abc-pc on 2020/4/14.
 */
public class AboutMePager extends BasePager implements View.OnClickListener {

    @Bind(R.id.about_bg)
    LinearLayout about_bg;
    @Bind(R.id.github)
    TextView github;
    @Bind(R.id.CSDN)
    TextView CSDN;

    public AboutMePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.aboutme_pager, null);  //载入aboutme_pager界面

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        about_bg.setOnClickListener(v -> BusProvider.getInstance().send(new Events.AgendaListViewTouchedEvent()));

        github.setOnClickListener(this);
        CSDN.setOnClickListener(this);
        CSDN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.github:
                Intent it1 = new Intent(Intent.ACTION_VIEW, Uri.parse(github.getText().toString()));
                it1.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                mActivity.startActivity(it1);
                break;
            case R.id.CSDN:
                Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse(CSDN.getText().toString()));
                it2.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                mActivity.startActivity(it2);
                break;
        }
    }
}
