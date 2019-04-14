package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :
 */
public class HomeNewUserEntity extends BaseEntity {
    String cover;
    String title;
    String desc;
    List<HomeNewUserBean> HomeNewUserGoods;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<HomeNewUserBean> getHomeNewUserGoods() {
        return HomeNewUserGoods;
    }

    public void setHomeNewUserGoods(List<HomeNewUserBean> homeNewUserGoods) {
        HomeNewUserGoods = homeNewUserGoods;
    }

    public class HomeNewUserBean {
        String pic;
        String price;
        int id;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
