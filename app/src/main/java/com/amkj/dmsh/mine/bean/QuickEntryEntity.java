package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/4/22
 * Version:v4.5.0
 * ClassDescription :七鱼客服快捷入口实体类
 */
public class QuickEntryEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-04-17 10:01:44
     * list : [{"id":2299,"title":"订单详情","link":"2"},{"id":2302,"title":"测试修改","link":"www.baidu.com/123"},{"id":2300,"title":"订单详情","link":"2"},{"id":2303,"title":"第一位","link":"2"},{"id":2304,"title":"第一位","link":"2"}]
     */

    private List<QuickEntryBean> list;

    public List<QuickEntryBean> getList() {
        return list;
    }

    public void setList(List<QuickEntryBean> list) {
        this.list = list;
    }

    public static class QuickEntryBean {
        /**
         * id : 2299
         * title : 订单详情
         * link : 2
         */

        private String id;
        private String title;
        private String link;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
