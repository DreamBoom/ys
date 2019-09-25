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
import card.com.allcard.tools.Tool;

public class Service1 extends Fragment {
    MMKV mk = BaseActivity.Companion.getMk();
    private ServiceAdapter adapter;
    int i = 0;
    private View noData;
    private View noWeb;
    private SwipeMenuListView listview;
    private String type = "";
    private ArrayList<ServiceListBean.ListBean> dataList = new ArrayList<>();
    private SmartRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drugstore_fragment, container, false);
        refresh = view.findViewById(R.id.refresh);
        listview = view.findViewById(R.id.listView);
        noData = inflater.inflate(R.layout.no_data, container, false);
        noWeb = inflater.inflate(R.layout.view_no_web, container, false);
        refresh.setEnableOverScrollDrag(false);
        refresh.setOnRefreshListener(refreshLayout -> {
            getList();
        });
        adapter = new ServiceAdapter(getActivity(), dataList, R.layout.news_list_item);
        listview.setAdapter(adapter);
        return view;
    }

    public void getList() {
        HttpRequestPort.Companion.getInstance().serviceList(type, new BaseHttpCallBack(getActivity()) {
            @Override
            public void success(String data) {
                super.success(data);
                ServiceListBean bean = JSONObject.parseObject(data, new TypeReference<ServiceListBean>() {});
                List<ServiceListBean.ListBean> service = bean.getList();
                dataList.clear();
                if (service.size() > 0) {
                    if (listview.getHeaderViewsCount() > 0) {
                        listview.removeHeaderView(noData);
                    }
                    dataList.addAll(service);
                    adapter.notifyDataSetChanged();
                } else {
                    if (listview.getHeaderViewsCount() == 0) {
                        listview.addHeaderView(noData);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                dataList.clear();
                adapter.notifyDataSetChanged();
                if (listview.getHeaderViewsCount() > 0) {
                    listview.removeAllViews();
                }
                listview.addHeaderView(noWeb);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                refresh.finishRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        type = mk.decodeString(Tool.INSTANCE.getTab(), "");
        refresh.autoRefresh();
    }
}
