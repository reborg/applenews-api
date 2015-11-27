package com.mailonline.sample;

import clojure.lang.Keyword;
import com.mailonline.AppleNews;

import java.util.Map;

import static com.mailonline.AppleNews.*;

public class Main {

    private static Map<Keyword, Object> resp;

    public static void main(String[] args) {

        resp = AppleNews.createArticle(loadSampleBundle());
        printResponse("createArticle 1-arg", resp);

        String id = getId(resp);

        resp = getArticle(id);
        printResponse("getArticle 1-arg", resp);

        resp = getArticle(id, "sandbox");
        printResponse("getArticle 2-arg", resp);

        resp = AppleNews.getChannel();
        printResponse("getChannel 0-arg", resp);

        resp = AppleNews.getChannel("sandbox");
        printResponse("getChannel 1-arg", resp);

        resp = AppleNews.getSection("4697c7f6-d091-41fd-e568-aae0ba23f8d2");
        printResponse("getSection 1-arg", resp);

        resp = AppleNews.getSection("4697c7f6-d091-41fd-e568-aae0ba23f8d2", "sandbox");
        printResponse("getSection 2-arg", resp);

        resp = AppleNews.getSections();
        printResponse("getSections 0-arg", resp);

        resp = AppleNews.getSections("sandbox");
        printResponse("getSections 1-arg", resp);

        resp = AppleNews.deleteArticle(id);
        printResponse("deleteArticle 1-arg", resp);

    }


}
