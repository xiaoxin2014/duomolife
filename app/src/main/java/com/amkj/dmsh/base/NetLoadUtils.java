package com.amkj.dmsh.base;

import android.content.Context;

import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;

import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/31
 * version 3.1.6
 * class description:网络工具
 */
public class NetLoadUtils<T, E extends BaseEntity> {

    private static NetLoadUtils netLoadUtils;
    private Convertor<String> convertor;

    private NetLoadUtils() {
    }

    public static NetLoadUtils getQyInstance() {
        if (netLoadUtils == null) {
            synchronized (NetLoadUtils.class) {
                if (netLoadUtils == null) {
                    netLoadUtils = new NetLoadUtils();
                }
            }
        }
        return netLoadUtils;
    }

    /**
     * @param context
     * @param url
     * @param params
     * @param netLoadListener
     */
    public void loadNetData(Context context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (netLoadListener != null) {
                        netLoadListener.onSuccess(result);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (netLoadListener != null) {
                        netLoadListener.onError(ex);
                    }
                }
            });
        } else {
            if (netLoadListener != null) {
                netLoadListener.netClose();
            }
        }
    }

    public interface NetLoadListener {
        void onSuccess(String result);

        void netClose();

        void onError(Throwable throwable);
    }

    public void showLoadSir(LoadService loadService, List<T> list, E resultClass) {
        if(loadService!=null){
            if (list != null && list.size() > 0) {
                loadService.showWithConvertor(SUCCESS_CODE);
            } else if (resultClass != null) {
                loadService.showWithConvertor(resultClass.getCode());
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    /**
     * 配置数据 展示异常界面
     * @return
     */
    public Convertor<String> getLoadSirCover() {
        if (convertor == null) {
            convertor = new Convertor<String>() {
                @Override
                public Class<? extends Callback> map(String baseEntity) {
                    Class<? extends Callback> resultCode = NetErrorCallback.class;
                    switch (baseEntity) {
                        case SUCCESS_CODE:
                            resultCode = SuccessCallback.class;
                            break;
                        default:
                            break;
                    }
                    return resultCode;
                }
            };
        }
        return convertor;
    }
}
