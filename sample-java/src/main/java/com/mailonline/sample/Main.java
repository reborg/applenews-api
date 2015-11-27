package com.mailonline.sample;

import com.mailonline.AppleNews;

public class Main {

    public Object getArticle() {
        return AppleNews.getArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.getArticle();
    }


}
