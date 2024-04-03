package com.mindnote;

public class firebasemodel {
    private String title;
    private String content;
    private String Subtitle;
    public firebasemodel(){

    }
    public firebasemodel(String title,String content,String subtitle){
        this.title=title;
        this.content=content;
        this.Subtitle=subtitle;
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
}
