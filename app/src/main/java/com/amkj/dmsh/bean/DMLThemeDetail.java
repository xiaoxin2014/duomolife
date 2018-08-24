package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/12.
 */
public class DMLThemeDetail {

    /**
     * result : {"picUrl":"http://img.domolife.cn/platform/mMMFF2zkHQ1494230713616.jpg","discription":"这个夏季喂辅小专场，就是为了宝妈量身定制的哈！像日常一些辅食工具，这次的专场内都涵盖了，有K-MOM动物家族抗菌存储袋，可以用来收纳宝宝的衣物，抗细菌效果持续力强，可重复使用。还有宝贝餐桌上必备的辅食餐具套装，babycare的\u201c控温\u201d餐具，不仅颜高还好用，套装内种类也很齐全，基本需要的都具备了。美国BOON家的多功能软硬双头勺、米糊喂养勺和吸盘碗口碑也很好，性价比很高。为了防止宝宝吃饭时弄到到处都是备上个MARCUS&MARCUS 卡通硅胶围兜也很不错哈。另外还有美国Nuby研磨碗和三个奶爸辅食剪，有了这款神器，日常制作辅食都轻松多了。夏天孩子们不爱喝水怎么办？准备一个加拿大Precidio儿童吸管果汁杯，像饮料盒一样，外观很有创意，孩子们只要捧着喝就可以了。本专场全场包邮，小主们看着入哟！","subtitle":"这个夏季喂辅小专场，就是为了宝妈量身定制的哈！像日常一些辅食工具，这次的专场内都涵盖了，有K-MOM动物家族抗菌存储袋，可以用来收纳宝宝的衣物，抗细菌效果持续力强，可重复使用。还有宝贝餐桌上必备的辅食餐具套装，babycare的\u201c控温\u201d餐具，不仅颜高还好用，套装内种类也很齐全，基本需要的都具备了。美国BOON家的多功能软硬双头勺、米糊喂养勺和吸盘碗口碑也很好，性价比很高。为了防止宝宝吃饭时弄到到处都是备上个MARCUS&MARCUS 卡通硅胶围兜也很不错哈。另外还有美国Nuby研磨碗和三个奶爸辅食剪，有了这款神器，日常制作辅食都轻松多了。夏天孩子们不爱喝水怎么办？准备一个加拿大Precidio儿童吸管果汁杯，像饮料盒一样，外观很有创意，孩子们只要捧着喝就可以了。本专场全场包邮，小主们看着入哟！","startTime":"2017-05-09 10:00:00","id":283,"endTime":"2017-05-14 23:59:59","title":"宝妈最爱夏季喂辅小专场","indexTitle":"","picUrlIndex":""}
     * msg : 请求成功
     * currentTime : 2017-05-11 15:01:38
     * code : 01
     */

    @SerializedName("result")
    private ThemeDataBean themeDataBean;
    private String msg;
    private String currentTime;
    private String code;
    private long addSecond;

    public ThemeDataBean getThemeDataBean() {
        return themeDataBean;
    }

    public void setThemeDataBean(ThemeDataBean themeDataBean) {
        this.themeDataBean = themeDataBean;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getAddSecond() {
        return addSecond;
    }

    public void setAddSecond(long addSecond) {
        this.addSecond = addSecond;
    }

    public static class ThemeDataBean {
        /**
         * picUrl : http://img.domolife.cn/platform/mMMFF2zkHQ1494230713616.jpg
         * discription : 这个夏季喂辅小专场，就是为了宝妈量身定制的哈！像日常一些辅食工具，这次的专场内都涵盖了，有K-MOM动物家族抗菌存储袋，可以用来收纳宝宝的衣物，抗细菌效果持续力强，可重复使用。还有宝贝餐桌上必备的辅食餐具套装，babycare的“控温”餐具，不仅颜高还好用，套装内种类也很齐全，基本需要的都具备了。美国BOON家的多功能软硬双头勺、米糊喂养勺和吸盘碗口碑也很好，性价比很高。为了防止宝宝吃饭时弄到到处都是备上个MARCUS&MARCUS 卡通硅胶围兜也很不错哈。另外还有美国Nuby研磨碗和三个奶爸辅食剪，有了这款神器，日常制作辅食都轻松多了。夏天孩子们不爱喝水怎么办？准备一个加拿大Precidio儿童吸管果汁杯，像饮料盒一样，外观很有创意，孩子们只要捧着喝就可以了。本专场全场包邮，小主们看着入哟！
         * subtitle : 这个夏季喂辅小专场，就是为了宝妈量身定制的哈！像日常一些辅食工具，这次的专场内都涵盖了，有K-MOM动物家族抗菌存储袋，可以用来收纳宝宝的衣物，抗细菌效果持续力强，可重复使用。还有宝贝餐桌上必备的辅食餐具套装，babycare的“控温”餐具，不仅颜高还好用，套装内种类也很齐全，基本需要的都具备了。美国BOON家的多功能软硬双头勺、米糊喂养勺和吸盘碗口碑也很好，性价比很高。为了防止宝宝吃饭时弄到到处都是备上个MARCUS&MARCUS 卡通硅胶围兜也很不错哈。另外还有美国Nuby研磨碗和三个奶爸辅食剪，有了这款神器，日常制作辅食都轻松多了。夏天孩子们不爱喝水怎么办？准备一个加拿大Precidio儿童吸管果汁杯，像饮料盒一样，外观很有创意，孩子们只要捧着喝就可以了。本专场全场包邮，小主们看着入哟！
         * startTime : 2017-05-09 10:00:00
         * id : 283
         * endTime : 2017-05-14 23:59:59
         * title : 宝妈最爱夏季喂辅小专场
         * indexTitle :
         * picUrlIndex :
         */

        private String picUrl;
        private String discription;
        private String subtitle;
        private String startTime;
        private int id;
        private String endTime;
        private String title;
        private String indexTitle;
        private String picUrlIndex;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIndexTitle() {
            return indexTitle;
        }

        public void setIndexTitle(String indexTitle) {
            this.indexTitle = indexTitle;
        }

        public String getPicUrlIndex() {
            return picUrlIndex;
        }

        public void setPicUrlIndex(String picUrlIndex) {
            this.picUrlIndex = picUrlIndex;
        }
    }
}
