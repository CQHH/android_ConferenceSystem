package com.hhxk.app.ui.query;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.refresh.PtrInitHelper;
import com.em.refresh.PtrDefaultHandler;
import com.em.refresh.PtrFrameLayout;
import com.hhxk.app.R;
import com.hhxk.app.adapter.AllConferenceAdapter;
import com.hhxk.app.adapter.NotConferenceAdapter;
import com.hhxk.app.base.BaseLazyFgt;
import com.hhxk.app.event.ConferenceEvent;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.AllConferencePojo;
import com.hhxk.app.pojo.NotConferencePojo;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  所有会议-未开始会议fragemnt
 * @date   2019/02/21
 * @author enmaoFu
 */
public class NotConferenceFgt extends BaseLazyFgt {

    @BindView(R.id.refresh)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private NotConferenceAdapter notConferenceAdapter;

    /**
     * 数据源
     */
    private List<NotConferencePojo> notConferencePojos;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_query_conference_not;
    }

    @Override
    protected void initData() {

        kv = MMKV.defaultMMKV();

        /**
         * 下拉刷新
         */
        PtrInitHelper.initPtr(getActivity(), ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                doHttp(RetrofitUtils.createApi(Http.class).notStartMeeting(),1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        //实例化适配器
        notConferenceAdapter = new NotConferenceAdapter(R.layout.item_query_conference_not, new ArrayList<NotConferencePojo>());
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        recyclerView.setHasFixedSize(true);
        //设置间隔样式
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //设置空数据页面
        setEmptyView(notConferenceAdapter);
        //设置adapter
        recyclerView.setAdapter(notConferenceAdapter);
        notConferenceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<NotConferencePojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<NotConferencePojo, ? extends BaseViewHolder> adapter, View view, int position) {
                EventBus.getDefault().post(new ConferenceEvent("open"));
                kv.encode("meeting_id_details",notConferenceAdapter.getItem(position).getMeeting_id());
                kv.encode("record","n");
            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        doHttp(RetrofitUtils.createApi(Http.class).notStartMeeting(),1);
    }

    @Override
    protected boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).notStartMeeting(),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                ptrFrameLayout.refreshComplete();
                notConferencePojos = AppJsonUtil.getArrayList(result,NotConferencePojo.class);
                notConferenceAdapter.setNewData(notConferencePojos);
                break;
        }
    }

    /**
     * 设置RecyclerView空数据页面
     * @param quickAdapter
     */
    public void setEmptyView(BaseQuickAdapter quickAdapter) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_assets_null, null, false);
        quickAdapter.setEmptyView(view);
    }

}
