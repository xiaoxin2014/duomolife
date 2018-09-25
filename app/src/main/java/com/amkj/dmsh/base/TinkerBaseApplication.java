package com.amkj.dmsh.base;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/25
 * version 3.1.7
 * class description:Tinker集成
 */
public class TinkerBaseApplication extends TinkerApplication {
    public TinkerBaseApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.amkj.dmsh.base.TinkerBaseApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
