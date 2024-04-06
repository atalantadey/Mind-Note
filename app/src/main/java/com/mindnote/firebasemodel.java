package com.mindnote;

public class firebasemodel {
    private String title;
    private String content;
    private String Subtitle;
    private String Image;
    private String Date;
    private String Color;
    private String Url;
    public firebasemodel(){

    }
    public firebasemodel(String title,String content,String subtitle,String Image,String Date, String Color,String Url){
        this.title=title;
        this.content=content;
        this.Subtitle=subtitle;
        this.Date=Date;
        this.Image=Image;
        this.Color=Color;
        this.Url=Url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSubtitle() {
        return Subtitle;
    }
    public void setSubtitle(String Subtitle) {
        this.Subtitle = Subtitle;
    }
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getColor() {
        return Color;
    }
    public void setColor(String color) {
        Color = color;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}

