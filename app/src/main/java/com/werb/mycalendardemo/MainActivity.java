package com.werb.mycalendardemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.werb.mycalendardemo.alarmremind.SendAlarmBroadcast;
import com.werb.mycalendardemo.customview.calendar.CalendarView;
import com.werb.mycalendardemo.database.SinInDao;
import com.werb.mycalendardemo.fragment.ContentFragment;
import com.werb.mycalendardemo.utils.BusProvider;
import com.werb.mycalendardemo.utils.CalendarManager;
import com.werb.mycalendardemo.utils.Events;
import com.werb.mycalendardemo.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;     //事件监听器

public class MainActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    //侧边栏布局 导航抽屉
    @Bind(R.id.main_draw_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.left_draw)
    NavigationView mNavigationView;
    //悬浮按钮
    @Bind(R.id.fab_layout)
    RapidFloatingActionLayout fab_layout;
    @Bind(R.id.fab_button_group)
    RapidFloatingActionButton fab_button_group;
    private RapidFloatingActionHelper rfabHelper;
    //日历View
    @Bind(R.id.calendar_view)
    public CalendarView calendar_view;
    //CalendarView自定义控件的属性
    private int mCalendarHeaderColor, mCalendarDayTextColor, mCalendarPastDayTextColor, mCalendarCurrentDayColor;
    //月份
    @Bind(R.id.title_day)
    TextView title_day;
    //日程 ListView
    @Bind(R.id.main_frame)
    FrameLayout main_frame;
    @Bind(R.id.signed_button)
    ImageButton signButton;

    private SinInDao mSinInDao;

    //left_menu点击打开关闭侧边栏  事件监听器
    @OnClick(R.id.left_menu)
    void openLeftDrawe() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    //旋转箭头 收起或打开CalendarView
    @Bind(R.id.title_arrow)
    ImageView title_arrow;
    //打开关闭CalendarView
    private boolean isOpen = true;//日历视图是否显示

    @OnClick(R.id.calendar_open_close)
    void openAndCloseCalendarView() {
        if (!isOpen) {
            calendar_view.setVisibility(View.VISIBLE);
            arrowRotateAnim(isOpen);
            isOpen = true;
        } else {
            calendar_view.setVisibility(View.GONE);
            arrowRotateAnim(isOpen);
            isOpen = false;
        }
    }

    //回到今天
    @OnClick(R.id.go_today)
    void goToday() {
//        homePager.agenda_view.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
        BusProvider.getInstance().send(new Events.GoBackToDay());
        calendar_view.scrollToDate(CalendarManager.getInstance().getToday(), CalendarManager.getInstance().getWeeks());
    }

    private boolean isAllowAlert = false;

    //OnCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSinInDao = new SinInDao(this);
        mSinInDao.open();
        ButterKnife.bind(this);

        initCalendarInfo();
        initLayoutView();
        setMonthTitle();
        initFab();



        //弹窗权限验证
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isAllowAlert = PrefUtils.getBoolean(this,"isAllowAlert",false);
            if(!isAllowAlert){
                showPermissionDialog();
            }
        }else {
            SendAlarmBroadcast.startAlarmService(this);
        }

    }

    //权限申请相关方法
    private static final int REQUEST_CODE = 1;
    @TargetApi(Build.VERSION_CODES.M)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));     //Intent隐式传递方法,传递data。
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 权限申请弹窗
     */
    private void showPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("弹窗需要开启权限哦~")
                .setPositiveButton("开启", (dialog, which) -> {
                    requestAlertWindowPermission();
                })
                .setNegativeButton("取消", (dialog, which) -> {

                });
        builder.create().show();
    }


    /**
     * 初始化布局
     */
    private void initLayoutView() {
        //初始化 CalendarView,设置颜色
        mCalendarHeaderColor = R.color.calendar_header_day_background;
        mCalendarDayTextColor = R.color.calendar_text_day;
        mCalendarCurrentDayColor = R.color.calendar_text_current_day;
        mCalendarPastDayTextColor = R.color.calendar_text_past_day;

        calendar_view.findViewById(R.id.cal_day_names).setBackgroundColor(getResources().getColor(mCalendarHeaderColor));

        // CalendarView 初始化,完成布局的初始化，数据的绑定,日期的显示
        calendar_view.init(CalendarManager.getInstance(this), getResources().getColor(mCalendarDayTextColor), getResources().getColor(mCalendarCurrentDayColor), getResources().getColor(mCalendarPastDayTextColor));

        //填充 碎片Fragment
        FragmentManager manager =getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        ContentFragment fragment = new ContentFragment();

        transaction.replace(R.id.main_frame, fragment, "CONTENT_FRAGMENT");

        transaction.commit();

    }

    /**
     * title_arrow 旋转动画
     */
    private void arrowRotateAnim(Boolean isOpen) {
        RotateAnimation anim;
        //旋转动画效果   参数值 旋转的开始角度  旋转的结束角度  pivotX x轴伸缩值
        if (isOpen) {   //isOpen为true则向上
            anim = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            anim = new RotateAnimation(180f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        anim.setDuration(150);   //该方法用于设置动画的持续时间，以毫秒为单位
        anim.setFillAfter(true);    //动画终止时停留在最后一帧
        title_arrow.startAnimation(anim);   //启动动画
    }

    /**
     * 设置月份
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setMonthTitle() {      //导航栏显示月份
        BusProvider.getInstance().toObserverable().subscribe(event -> {
            if (event instanceof Events.CalendarMonthEvent) {
                Events.CalendarMonthEvent monthEvent = (Events.CalendarMonthEvent) event;
                title_day.setText(monthEvent.getMonth());
            } else if (event instanceof Events.CalendarSignButtonEvent) {
                Events.CalendarSignButtonEvent signButtonEvent = (Events.CalendarSignButtonEvent) event;
                Log.i("TESTTEST","事件传过来了");
                if (signButtonEvent.isSigned) {
                    //已签到
                    Log.i("TESTTEST","已签到");
                    signButton.setImageDrawable(getDrawable(R.drawable.signed));
                    signButton.setClickable(false);
                } else {
                    Log.i("TESTTEST","未签到");
                    //未签到
                    signButton.setImageDrawable(getDrawable(R.drawable.sign));
                    signButton.setClickable(true);
                    signButton.setOnClickListener(view -> {
                        if (signButtonEvent.isToday) {
                            //是当天则可以签到
                            mSinInDao.insertSinInfo(signButtonEvent.nowDate);
                            BusProvider.getInstance().send(new Events.SignedEvent(signButtonEvent.dayItem));
                            Toast.makeText(this,"签到成功",Toast.LENGTH_SHORT).show();
                        } else {
                            //不是当天不可以签到
                            Toast.makeText(this,"不是当天无法签到",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化 Fab(FloatingActionButton) 悬浮按钮
     */
    private void initFab() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("活动")
                        .setResId(R.drawable.ic_access_alarms_white_24dp)
                        .setIconNormalColor(0xffd84315)
                        .setIconPressedColor(0xffbf360c)
                        .setLabelSizeSp(14)
                        .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                fab_layout,
                fab_button_group,
                rfaContent
        ).build();

    }


    /**
     * 添加日程活动信息
     */
    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        Toast.makeText(this, "Click Label !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if (i == 0) {
            Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
            intent.putExtra("type","mainToAdd");
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("---onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        support.deactivate();
    }
    //首次添加活动，提醒打开 弹窗权限
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this,"弹窗权限开启！",Toast.LENGTH_SHORT).show();
                PrefUtils.setBoolean(MainActivity.this, "isAllowAlert", true);
            }else {
                PrefUtils.setBoolean(MainActivity.this, "isAllowAlert", false);
            }
        }
    }


    /**
     * 初始化日历信息
     */
    private void initCalendarInfo() {
        // 设置日历显示的时间，最大为当前时间+1年，最小为当前时间-1年
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.YEAR, -1);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);
        //根据你传入的开始结束值，构建生成Calendar数据（各种Item，JavaBean）
        CalendarManager.getInstance(this).buildCal(minDate, maxDate, Locale.getDefault());
    }

    /**
     * 得到侧滑菜单
     */
    public NavigationView getNavigationView(){
        return mNavigationView;
    }
    /**
     * 得到DrawerLayout
     */
    public DrawerLayout getDrawerLayout(){
        return mDrawerLayout;
    }

}
