package card.com.allcard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.adapter.ServiceAdapter;
import card.com.allcard.bean.ServiceListBean;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;

public class Service5 extends Fragment{
    MMKV mk = BaseActivity.Companion.getMk();
    private ServiceAdapter adapter;
    private  ArrayList<ServiceListBean.ListBean> dataList = new ArrayList<>();
    int i = 0;
    private View noData;
    private View noWeb;
    private SwipeMenuListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drugstore_fragment, container, false);
        SmartRefreshLayout refresh = view.findViewById(R.id.refresh);
        listview = view.findViewById(R.id.listView);
        noData = inflater.inflate(R.layout.no_data, container, false);
        noWeb = inflater.inflate(R.layout.view_no_web, container, false);
        refresh.setEnableOverScrollDrag(false);
        refresh.setOnRefreshListener(refreshLayout -> {
            refresh.finishRefresh();
            getList();
        });

        adapter = new ServiceAdapter(getActivity(),dataList,R.layout.news_list_item);
        listview.setAdapter(adapter);
        if( i == 0){
            i = 1;
            refresh.autoRefresh();
        }
        return view;
    }

    public void getList() {
        HttpRequestPort.Companion.getInstance().serviceList("4", new BaseHttpCallBack(getActivity()) {
                    @Override
                    public void success(String data) {
                        super.success(data);
                        ServiceListBean bean = JSONObject.parseObject(data,
                                new TypeReference<ServiceListBean>() {});
                        List<ServiceListBean.ListBean> service = bean.getList();
                        dataList.clear();
                        if(service.size()>0){
                            if(listview.getHeaderViewsCount()>0){
                                listview.removeHeaderView(noData);
                            }
                            dataList.addAll(service);
                            adapter.notifyDataSetChanged();
                        }else {
                            if(listview.getHeaderViewsCount()==0){
                                listview.addHeaderView(noData);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        super.onError(throwable, b);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        if(listview.getHeaderViewsCount()>0){
                            listview.removeAllViews();
                        }
                        listview.addHeaderView(noWeb);
                    }
                });
    }
}
