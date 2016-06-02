package com.xyh.easywashcar.model;

/**
 * Created by 向阳湖 on 2016/5/28.
 */
public class NewsContent {
    /**
     * {
     "showapi_res_code": 0,
     "showapi_res_error": "",
     "showapi_res_body": {
     "pagebean": {
     "allNum": 11,
     "allPages": 1,
     "contentlist": [
     {
     "channelId": "5572a109b3cdc86cf39001e5",
     "channelName": "汽车最新",
     "desc": "网易汽车5月27日报道据相关人士透露，2016款揽胜极光将于6月1日正式上市，前期仅推出4款常规车型，其官方指导价已于此前发布，与现款保持一致39.8万55.8万元。而限量版的倾橙版将随后于7月...",
     "imageurls": [
     {
     "height": 413,
     "url": "http://www.gs.xinhuanet.com/qiche/2016-05/27/1118946056_14643411130571n.JPG",
     "width": 550
     },
     {
     "height": 413,
     "url": "http://www.gs.xinhuanet.com/qiche/2016-05/27/1118946056_14643411130891n.JPG",
     "width": 550
     }
     ],
     "link": "http://www.gs.xinhuanet.com/qiche/2016-05/27/c_1118946056.htm",
     "pubDate": "2016-05-27 10:00:18",
     "source": "新华网",
     "title": "提供越野新玩法 2016款极光6月1日上市"
     },
     *
     * */

    private String title;    //标题
    private String desc;      //简要
    private String pubDate;   //发布时间
    private String sourceLink;   //原文链接
    private String resource;     //原文来自
    private String imgUrl;         //图片链接
    private String html;        //html文本

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }
}
