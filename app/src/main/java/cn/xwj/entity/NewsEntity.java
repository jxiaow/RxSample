package cn.xwj.entity;

/**
 * Created by xw on 2018/1/11.
 */

public class NewsEntity {
    private String type;
    private String item;

    public NewsEntity(String type, String item) {
        this.type = type;
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
