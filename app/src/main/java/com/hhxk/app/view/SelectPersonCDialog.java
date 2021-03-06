package com.hhxk.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.hhxk.app.R;
import com.hhxk.app.adapter.SelectChoicePersonCAdapter;
import com.hhxk.app.adapter.SelectChoicePersonCYAdapter;
import com.hhxk.app.pojo.SelectChoicePersonCPojo;
import com.hhxk.app.pojo.SelectChoicePersonCYPojo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title  选择参与人员view
 * @date   2019/02/25
 * @author enmaoFu
 */
public class SelectPersonCDialog extends Dialog {

    private RecyclerView rvTwo;
    private Spinner mSpinner;
    private RecyclerView mRv;
    private TextView clear;
    private TextView btn;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManagerTwo;

    /**
     * 适配器
     */
    private SelectChoicePersonCAdapter selectChoicePersonCAdapter;

    /**
     * 数据源
     */
    private List<SelectChoicePersonCPojo> selectChoicePersonPojos;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager   mLayoutManager;

    /**
     * 适配器
     */
    private SelectChoicePersonCYAdapter selectChoicePersonCYAdapter;

    /**
     * 数据源
     */
    private List<SelectChoicePersonCYPojo> selectChoicePersonCYPojos;

    private List<String> stringList;

    /**
     * 从下往上滑动动画
     */
    public static final int DIALOG_ANIM_SLID_BOTTOM = com.em.baseframe.R.style.DialogAnimationSlidBottom;
    /**
     * 对话框宽度所占屏幕宽度的比例
     */
    public static final float WIDTHFACTOR = 0.6f;

    /**
     * 对话框
     */
    private Window window;

    public SelectPersonCDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        setContentView(R.layout.dialog_select_person_c);

        window = this.getWindow();
        //是否系统级弹框
        if (false) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        // 获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenwidth = metrics.widthPixels;
        int width = 0;
        if (WIDTHFACTOR > 0) {
            width = (int) (screenwidth * WIDTHFACTOR);
        } else {
            width = (int) (screenwidth * WIDTHFACTOR);
        }
        // 设置对话框宽度
        window.getAttributes().width = width;
        // 设置对话框位置
        window.setGravity(Gravity.CENTER);

        init();
    }

    private void init(){
        rvTwo = (RecyclerView)findViewById(R.id.rv_two);
        mSpinner = (Spinner)findViewById(R.id.spinner);
        mRv = (RecyclerView)findViewById(R.id.rv);
        clear = (TextView)findViewById(R.id.clear);
        btn = (TextView)findViewById(R.id.btn);
    }

    public void setRv(Context mContext, List<SelectChoicePersonCPojo> selectChoicePersonPojos){
        //实例化布局管理器
        mLayoutManagerTwo = new LinearLayoutManager(mContext);
        //实例化适配器
        selectChoicePersonCAdapter = new SelectChoicePersonCAdapter(R.layout.item_select_choice_person, selectChoicePersonPojos);
        //设置布局管理器
        rvTwo.setLayoutManager(mLayoutManagerTwo);
        //大小不受适配器影响
        rvTwo.setHasFixedSize(true);
        //分割线
        rvTwo.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mContext)
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //设置adapter
        rvTwo.setAdapter(selectChoicePersonCAdapter);
    }

    public void setRvY(Context mContext,List<SelectChoicePersonCYPojo> selectChoicePersonCYPojos){

        //实例化布局管理器
        mLayoutManager = new GridLayoutManager(mContext,4);
        //实例化适配器
        selectChoicePersonCYAdapter = new SelectChoicePersonCYAdapter(R.layout.item_select_y_person, selectChoicePersonCYPojos);
        //设置布局管理器
        mRv.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        mRv.setHasFixedSize(true);
        //分割线
        mRv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mContext)
                        .color(Color.parseColor("#ffffff"))
                        .sizeResId(R.dimen.recyclerview_item_hr_12)
                        .build());
        //设置adapter
        mRv.setAdapter(selectChoicePersonCYAdapter);
    }

    public List<SelectChoicePersonCYPojo> setData(){
        selectChoicePersonCYPojos = new ArrayList<>();
        SelectChoicePersonCYPojo selectChoicePersonCYPojo = new SelectChoicePersonCYPojo();
        selectChoicePersonCYPojo.setUser_name("ss");
        selectChoicePersonCYPojo.setUser_id(0);
        selectChoicePersonCYPojo.setDepartment_id("0");
        selectChoicePersonCYPojo.setDepartment_name("sss");
        selectChoicePersonCYPojo.setPosition_id("0");
        selectChoicePersonCYPojo.setPosition_name("ssss");
        selectChoicePersonCYPojo.setUser_account("sssss");
        selectChoicePersonCYPojos.add(selectChoicePersonCYPojo);
        return selectChoicePersonCYPojos;
    }

    public void addRvData(List<SelectChoicePersonCPojo> selectChoicePersonPojos){
        selectChoicePersonCAdapter.addData(selectChoicePersonPojos);
    }

    public void getOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener){
        selectChoicePersonCAdapter.setOnLoadMoreListener(requestLoadMoreListener,rvTwo);
    }

    public SelectChoicePersonCAdapter getAdapter(){
        return selectChoicePersonCAdapter;
    }

    public SelectChoicePersonCYAdapter getCYAdapter(){
        return selectChoicePersonCYAdapter;
    }

    public void setSpinner(Context context,Map<String,String> map){
        stringList = new ArrayList<>();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            stringList.add(entry.getValue());
        }
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.item_select_person, R.id.text,stringList);
        mSpinner.setAdapter(adapter);
    }

    public void setSpinnerOnItemClick(AdapterView.OnItemSelectedListener onItemSelectedListener){
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void BtnOnClick(View.OnClickListener clickListener){
        btn.setOnClickListener(clickListener);
    }

    public void ClearOnClick(View.OnClickListener clickListener){
        clear.setOnClickListener(clickListener);
    }

    /**
     * 给对话框设置动画
     */
    public void setDialogAnimation() {
        this.getWindow().setWindowAnimations(DIALOG_ANIM_SLID_BOTTOM);
    }

}
