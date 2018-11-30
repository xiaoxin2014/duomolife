package com.amkj.dmsh.network;

import io.reactivex.Flowable;
import retrofit2.http.POST;

import static com.amkj.dmsh.constant.Url.MINE_BOTTOM_DATA;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/29
 * version 3.1.9
 * class description:retrofit网络请求
 */
public interface NetApiService {
    /**
     * 我-底部宫格
     * @return
     */
    @POST(MINE_BOTTOM_DATA)
    Flowable<String> getMineTypeBottom();

}
