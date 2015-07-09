import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.Map;
import java.util.Set;

public class AppleNews {

    private static IFn requireFn = Clojure.var("clojure.core", "require");
    private static IFn getArticleFn = Clojure.var("clj-applenewsapi.core", "get-article");

    static {
        requireFn.invoke(Clojure.read("clj-applenewsapi.core"));
    }

    public static Object getArticle(String id, String channelName) {
        return getArticleFn.invoke(id, channelName);
    }

    public static void main(String[] args) {
        Map article = (Map) AppleNews.getArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb", "sandbox");
        Set keys = article.keySet();
        for (Object o: keys) {
            System.out.println(o);
        }
    }

}
