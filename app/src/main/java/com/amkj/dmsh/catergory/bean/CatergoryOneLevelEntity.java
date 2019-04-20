package com.amkj.dmsh.catergory.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxin on 2019/4/19 0019
 * Version:v4.0.0
 * ClassDescription :
 */
public class CatergoryOneLevelEntity extends BaseEntity {

    private List<CatergoryOneLevelBean> result;

    public List<CatergoryOneLevelBean> getResult() {
        return result;
    }

    public void setResult(List<CatergoryOneLevelBean> result) {
        this.result = result;
    }

    public static class CatergoryOneLevelBean implements Parcelable {
        /**
         * picUrl : http://image.domolife.cn/platform/8QT8aKyr7w1555657467316.jpeg
         * relateArticle : {"categoryName":"彩妆个护","articles":[{"goodsCategoryId":2,"articleCategoryId":69,"documentId":19295,"documentName":"阴冷天也不无聊，教你化少女日系妆","documentPicurl":"http://image.domolife.cn/platform/P3SajekAMd1546913413395.jpg"},{"goodsCategoryId":2,"articleCategoryId":69,"documentId":19172,"documentName":"刷屏的19年珊瑚橘，用在家里也是赏心悦目呐","documentPicurl":"http://image.domolife.cn/platform/eDEhyQW7861552035629102.jpg"}]}
         * name : 美妆护理
         * childCategoryList : [{"picUrl":"http://image.domolife.cn/platform/cRefQrJXkQ1523932687636.png","name":"美容仪器","pid":2,"id":54,"type":1},{"picUrl":"http://image.domolife.cn/platform/kk7KQmpX4i1523932712886.png","name":"护理用品","pid":2,"id":55,"type":1},{"picUrl":"http://image.domolife.cn/platform/aCNHkeGrTt1523932706461.png","name":"美容美妆","pid":2,"id":56,"type":1}]
         * pid : 0
         * id : 2
         * type : 1
         */

        private String picUrl;
        private RelateArticleBean relateArticle;
        private String name;
        private int pid;
        private int id;
        private int type;
        private List<ChildCategoryListBean> childCategoryList;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public RelateArticleBean getRelateArticle() {
            return relateArticle;
        }

        public void setRelateArticle(RelateArticleBean relateArticle) {
            this.relateArticle = relateArticle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<ChildCategoryListBean> getChildCategoryList() {
            return childCategoryList;
        }

        public void setChildCategoryList(List<ChildCategoryListBean> childCategoryList) {
            this.childCategoryList = childCategoryList;
        }

        public static class RelateArticleBean implements Parcelable {
            /**
             * categoryName : 彩妆个护
             * articles : [{"goodsCategoryId":2,"articleCategoryId":69,"documentId":19295,"documentName":"阴冷天也不无聊，教你化少女日系妆","documentPicurl":"http://image.domolife.cn/platform/P3SajekAMd1546913413395.jpg"},{"goodsCategoryId":2,"articleCategoryId":69,"documentId":19172,"documentName":"刷屏的19年珊瑚橘，用在家里也是赏心悦目呐","documentPicurl":"http://image.domolife.cn/platform/eDEhyQW7861552035629102.jpg"}]
             */

            private String categoryName;
            private List<ArticlesBean> articles;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<ArticlesBean> getArticles() {
                return articles;
            }

            public void setArticles(List<ArticlesBean> articles) {
                this.articles = articles;
            }

            public static class ArticlesBean {
                /**
                 * goodsCategoryId : 2
                 * articleCategoryId : 69
                 * documentId : 19295
                 * documentName : 阴冷天也不无聊，教你化少女日系妆
                 * documentPicurl : http://image.domolife.cn/platform/P3SajekAMd1546913413395.jpg
                 */

                private int goodsCategoryId;
                private int articleCategoryId;
                private int documentId;
                private String documentName;
                private String documentPicurl;

                public int getGoodsCategoryId() {
                    return goodsCategoryId;
                }

                public void setGoodsCategoryId(int goodsCategoryId) {
                    this.goodsCategoryId = goodsCategoryId;
                }

                public int getArticleCategoryId() {
                    return articleCategoryId;
                }

                public void setArticleCategoryId(int articleCategoryId) {
                    this.articleCategoryId = articleCategoryId;
                }

                public int getDocumentId() {
                    return documentId;
                }

                public void setDocumentId(int documentId) {
                    this.documentId = documentId;
                }

                public String getDocumentName() {
                    return documentName;
                }

                public void setDocumentName(String documentName) {
                    this.documentName = documentName;
                }

                public String getDocumentPicurl() {
                    return documentPicurl;
                }

                public void setDocumentPicurl(String documentPicurl) {
                    this.documentPicurl = documentPicurl;
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.categoryName);
                dest.writeList(this.articles);
            }

            public RelateArticleBean() {
            }

            protected RelateArticleBean(Parcel in) {
                this.categoryName = in.readString();
                this.articles = new ArrayList<ArticlesBean>();
                in.readList(this.articles, ArticlesBean.class.getClassLoader());
            }

            public static final Creator<RelateArticleBean> CREATOR = new Creator<RelateArticleBean>() {
                @Override
                public RelateArticleBean createFromParcel(Parcel source) {
                    return new RelateArticleBean(source);
                }

                @Override
                public RelateArticleBean[] newArray(int size) {
                    return new RelateArticleBean[size];
                }
            };
        }

        public static class ChildCategoryListBean implements Parcelable {
            /**
             * picUrl : http://image.domolife.cn/platform/cRefQrJXkQ1523932687636.png
             * name : 美容仪器
             * pid : 2
             * id : 54
             * type : 1
             */

            private String picUrl;
            private String name;
            private int pid;
            private int id;
            private int type;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.picUrl);
                dest.writeString(this.name);
                dest.writeInt(this.pid);
                dest.writeInt(this.id);
                dest.writeInt(this.type);
            }

            public ChildCategoryListBean() {
            }

            protected ChildCategoryListBean(Parcel in) {
                this.picUrl = in.readString();
                this.name = in.readString();
                this.pid = in.readInt();
                this.id = in.readInt();
                this.type = in.readInt();
            }

            public static final Creator<ChildCategoryListBean> CREATOR = new Creator<ChildCategoryListBean>() {
                @Override
                public ChildCategoryListBean createFromParcel(Parcel source) {
                    return new ChildCategoryListBean(source);
                }

                @Override
                public ChildCategoryListBean[] newArray(int size) {
                    return new ChildCategoryListBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.picUrl);
            dest.writeParcelable(this.relateArticle, flags);
            dest.writeString(this.name);
            dest.writeInt(this.pid);
            dest.writeInt(this.id);
            dest.writeInt(this.type);
            dest.writeList(this.childCategoryList);
        }

        public CatergoryOneLevelBean() {
        }

        protected CatergoryOneLevelBean(Parcel in) {
            this.picUrl = in.readString();
            this.relateArticle = in.readParcelable(RelateArticleBean.class.getClassLoader());
            this.name = in.readString();
            this.pid = in.readInt();
            this.id = in.readInt();
            this.type = in.readInt();
            this.childCategoryList = new ArrayList<ChildCategoryListBean>();
            in.readList(this.childCategoryList, ChildCategoryListBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<CatergoryOneLevelBean> CREATOR = new Parcelable.Creator<CatergoryOneLevelBean>() {
            @Override
            public CatergoryOneLevelBean createFromParcel(Parcel source) {
                return new CatergoryOneLevelBean(source);
            }

            @Override
            public CatergoryOneLevelBean[] newArray(int size) {
                return new CatergoryOneLevelBean[size];
            }
        };
    }
}
