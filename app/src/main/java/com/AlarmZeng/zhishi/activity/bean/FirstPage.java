package com.AlarmZeng.zhishi.activity.bean;

import java.util.List;

/**
 * Created by hunter_zeng on 2016/5/18.
 */
public class FirstPage {


    /**
     * date : 20160518
     * stories : [{"title":"完全不输三杯鸡的素食美味，饱腹感十足","ga_prefix":"051815","images":["http://pic1.zhimg.com/f1040654c39c67e1280469ce7f3650e8.jpg"],"multipic":true,"type":0,"id":8318212},{"images":["http://pic2.zhimg.com/9583629790afed79277fb311170f2569.jpg"],"type":0,"id":8318520,"ga_prefix":"051814","title":"信用卡之于理财，用「神器」来形容都不过分"},{"images":["http://pic4.zhimg.com/2edc084dad539018659991a2cfb65c83.jpg"],"type":0,"id":8318441,"ga_prefix":"051813","title":"韩剧《太阳的后裔》不仅在国内火了，还圈了一群伊朗粉丝"},{"images":["http://pic1.zhimg.com/196ca6c937b81d96195cfc0634635f20.jpg"],"type":0,"id":8318349,"ga_prefix":"051812","title":"大误 · 他将我抱了起来，我顿时肋下发烫，心跳密集"},{"images":["http://pic2.zhimg.com/81c83a72a19c92c9c22cd9794291f15d.jpg"],"type":0,"id":8313882,"ga_prefix":"051811","title":"一片小小的避孕药，提高了女性的地位和工资"},{"images":["http://pic1.zhimg.com/a7964b64e07579682fb4d1d022112250.jpg"],"type":0,"id":8316828,"ga_prefix":"051810","title":"聊天里吸烟的表情，会让你更认可吸烟行为吗？"},{"images":["http://pic3.zhimg.com/6a38b084ada019b0470b28fb7bb26d52.jpg"],"type":0,"id":8301534,"ga_prefix":"051809","title":"能搭配曲子的歌词才是好歌词，比如周杰伦遇到方文山"},{"title":"花了六百多，我在传说中让人「哭晕」的上海迪士尼玩一天","ga_prefix":"051808","images":["http://pic4.zhimg.com/1461f1c0cdaa600b4e11ce4f69882def.jpg"],"multipic":true,"type":0,"id":8314974},{"images":["http://pic4.zhimg.com/24629dbe27716007d1a37d7f528dd53b.jpg"],"type":0,"id":8315711,"ga_prefix":"051807","title":"「孩子跑来跑去的，连我都累了，他还不累吗？」"},{"images":["http://pic2.zhimg.com/56fce1bc039a1e552ab7f7c9eb356721.jpg"],"type":0,"id":8313389,"ga_prefix":"051807","title":"工行宣布停止个人账户综合理财业务了？莫慌，都怪名字没取好"},{"images":["http://pic2.zhimg.com/fd999032675e3a3c3bcb081aad2544d1.jpg"],"type":0,"id":8314387,"ga_prefix":"051807","title":"每天能赚几千万美元，Facebook 的首页厉害在哪里？"},{"images":["http://pic1.zhimg.com/a7ead46ed3bb0531dd179065b48bf9a4.jpg"],"type":0,"id":8316810,"ga_prefix":"051807","title":"读读日报 24 小时热门 TOP 5 · 和几位在上学期间生孩子的年轻人聊了聊"},{"images":["http://pic3.zhimg.com/46068b32d101bce5106eb4fbb3966386.jpg"],"type":0,"id":8310843,"ga_prefix":"051806","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic4.zhimg.com/38f41b78eb534f1a917bc6f81adb6bcf.jpg","type":0,"id":8318520,"ga_prefix":"051814","title":"信用卡之于理财，用「神器」来形容都不过分"},{"image":"http://pic2.zhimg.com/8b2a34ec6fb1feae87a97c8eaaa17c79.jpg","type":0,"id":8314974,"ga_prefix":"051808","title":"花了六百多，我在传说中让人「哭晕」的上海迪士尼玩一天"},{"image":"http://pic3.zhimg.com/e8811608a0ced328017989b2ecf1e6a6.jpg","type":0,"id":8316810,"ga_prefix":"051807","title":"读读日报 24 小时热门 TOP 5 · 和几位在上学期间生孩子的年轻人聊了聊"},{"image":"http://pic4.zhimg.com/03e256caf53db442805c0a3a5e673ddb.jpg","type":0,"id":8301534,"ga_prefix":"051809","title":"能搭配曲子的歌词才是好歌词，比如周杰伦遇到方文山"},{"image":"http://pic4.zhimg.com/8f209bcfb5b6e0625ca808e43c0a0a73.jpg","type":0,"id":8314043,"ga_prefix":"051717","title":"怎样才能找到自己的兴趣所在，发自内心地去工作？"}]
     */

    private String date;
    /**
     * title : 完全不输三杯鸡的素食美味，饱腹感十足
     * ga_prefix : 051815
     * images : ["http://pic1.zhimg.com/f1040654c39c67e1280469ce7f3650e8.jpg"]
     * multipic : true
     * type : 0
     * id : 8318212
     */

    private List<Stories> stories;
    /**
     * image : http://pic4.zhimg.com/38f41b78eb534f1a917bc6f81adb6bcf.jpg
     * type : 0
     * id : 8318520
     * ga_prefix : 051814
     * title : 信用卡之于理财，用「神器」来形容都不过分
     */

    private List<TopStories> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public List<TopStories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

    public static class Stories {
        private String title;
        private String ga_prefix;
        private boolean multipic;
        private int type;
        private String id;
        private List<String> images;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStories {
        private String image;
        private int type;
        private String id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
