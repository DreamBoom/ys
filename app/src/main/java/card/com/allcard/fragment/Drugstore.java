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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import card.com.allcard.R;
import card.com.allcard.activity.BaseActivity;
import card.com.allcard.adapter.DrugstoreAdapter;
import card.com.allcard.bean.DrugstoreList;
import card.com.allcard.net.BaseHttpCallBack;
import card.com.allcard.net.HttpRequestPort;
import card.com.allcard.tools.Tool;

public class Drugstore extends Fragment{
    MMKV mk = BaseActivity.Companion.getMk();
    private DrugstoreAdapter adapter;
    private View noData;
    private View noWeb;
    private SwipeMenuListView listview;
    private  ArrayList<DrugstoreList.PharmacyBean> dataList = new ArrayList<>();
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
        adapter = new DrugstoreAdapter(getActivity(),dataList,R.layout.scrow_view_item1);
        listview.setAdapter(adapter);
        refresh.autoRefresh();
        return view;
    }

    public void getList() {
        String search = mk.decodeString(Tool.INSTANCE.getSearch(), "");
        String areaId = mk.decodeString(Tool.INSTANCE.getArea_ID(), "");
        HttpRequestPort.Companion.getInstance().drugstoreList("1",
                "100", areaId, search,new BaseHttpCallBack(getActivity()){
                    @Override
                    public void success(@NotNull String data) {
                        super.success(data);
                        DrugstoreList drugstoreList = JSONObject.parseObject(data,
                                new TypeReference<DrugstoreList>() {});
                        ArrayList<DrugstoreList.PharmacyBean> pharmacy = drugstoreList.getPharmacy();
                        dataList.clear();
                        if(pharmacy.size()>0){
                            if(listview.getHeaderViewsCount()>0){
                                listview.removeHeaderView(noData);
                            }
                            dataList.addAll(pharmacy);
                            adapter.notifyDataSetChanged();
                        }else {
                            if(listview.getHeaderViewsCount()==0){
                                listview.addHeaderView(noData);
                            }
                        }
                    }
                    @Override
                    public void onError(@NotNull Throwable throwable, boolean b) {
                        super.onError(throwable, b);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        if(listview.getHeaderViewsCount()>0){
                            listview.removeAllViews();
                        }
                        listview.addHeaderView(noWeb);
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Tool.INSTANCE.getCHOOSE() == 1) {
            getList();
        }
    }
}
