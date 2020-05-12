package com.amkj.dmsh.shopdetails.dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.amkj.dmsh.R;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.amkj.dmsh.utils.alertdialog.BaseAlertDialog;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2020/4/9
 * Version:v4.5.0
 * ClassDescription :滚轮弹窗
 */
public class AlertDialogWheel extends BaseAlertDialog {

    @BindView(R.id.wv_communal_one)
    WheelView mWvCommunalOne;
    private ClickConfirmListener mClickConfirmListener;
    private Map<String, String> mMap = new LinkedHashMap<>();

    public AlertDialogWheel(@NonNull AppCompatActivity context) {
        super(context, R.style.NobackDialog);
    }


    @Override
    protected int getContentView() {
        return R.layout.alter_dialog_wheel;
    }

    @Override
    protected void initViews() {

    }

    public void updateView(Map<String, String> map) {
        mMap.clear();
        List<String> datas = new ArrayList<>();
        if (map != null && map.size() > 0) {
            mMap.putAll(map);
            for (Map.Entry<String, String> entry : mMap.entrySet()) {
                datas.add(entry.getValue());
            }
        }
        mWvCommunalOne.setAdapter(new ArrayWheelAdapter<>(datas));
        mWvCommunalOne.setCyclic(false);
        mWvCommunalOne.setCurrentItem(0);
    }

    public void updateView(List<String> datas) {
        mMap = null;
        mWvCommunalOne.setAdapter(new ArrayWheelAdapter<>(datas));
        mWvCommunalOne.setCyclic(false);
        mWvCommunalOne.setCurrentItem(0);
    }

    public void setConfirmListener(ClickConfirmListener clickConfirmListener) {
        mClickConfirmListener = clickConfirmListener;
    }

    public interface ClickConfirmListener {
        void confirm(String key, String value);
    }

    @OnClick({R.id.tv_one_click_cancel, R.id.tv_one_click_confirmed})
    public void onViewClicked(View view) {
        dismiss();
        if (view.getId() == R.id.tv_one_click_confirmed) {
            try {
                int itemsCount = mWvCommunalOne.getItemsCount();
                if (itemsCount > 0) {
                    int currentItem = mWvCommunalOne.getCurrentItem();
                    if (mClickConfirmListener != null) {
                        if (mMap != null) {
                            int i = 0;
                            for (Map.Entry<String, String> entry : mMap.entrySet()) {
                                if (i == currentItem) {
                                    mClickConfirmListener.confirm(entry.getKey(), entry.getValue());
                                    break;
                                }
                                i++;
                            }
                        } else {
                            mClickConfirmListener.confirm("", (String) (mWvCommunalOne.getAdapter()).getItem(currentItem));
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
