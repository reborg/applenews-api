import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;
import clojure.lang.PersistentVector;

import java.util.Map;

public class AppleNews {

    private static IFn requireFn = Clojure.var("clojure.core", "require");
    static {
        requireFn.invoke(Clojure.read("clj-applenewsapi.core"));
        requireFn.invoke(Clojure.read("clj-applenewsapi.bundle"));
    }

    private static IFn loadEdnFn = Clojure.var("clj-applenewsapi.bundle", "load-edn");

    private static IFn getArticleFn = Clojure.var("clj-applenewsapi.core", "get-article");
    private static IFn deleteArticleFn = Clojure.var("clj-applenewsapi.core", "delete-article");
    private static IFn getChannelFn = Clojure.var("clj-applenewsapi.core", "get-channel");
    private static IFn getSectionFn = Clojure.var("clj-applenewsapi.core", "get-section");
    private static IFn getSectionsFn = Clojure.var("clj-applenewsapi.core", "get-sections");
    private static IFn createArticleFn = Clojure.var("clj-applenewsapi.core", "create-article");

    public static Map<Keyword, Object> getArticle(String id) {
        return (Map<Keyword, Object>) getArticleFn.invoke(id);
    }

    public static Map<Keyword, Object> getArticle(String id, String channelName) {
        return (Map<Keyword, Object>) getArticleFn.invoke(id, channelName);
    }

    public static Map<Keyword, Object> deleteArticle(String id) {
        return (Map<Keyword, Object>) deleteArticleFn.invoke(id);
    }

    public static Map<Keyword, Object> deleteArticle(String id, String channelName) {
        return (Map<Keyword, Object>) deleteArticleFn.invoke(id, channelName);
    }

    public static Map<Keyword, Object> getChannel() {
        return (Map<Keyword, Object>) getChannelFn.invoke();
    }

    public static Map<Keyword, Object> getChannel(String id) {
        return (Map<Keyword, Object>) getChannelFn.invoke(id);
    }

    public static Map<Keyword, Object> getSection(String id) {
        return (Map<Keyword, Object>) getSectionFn.invoke(id);
    }

    public static Map<Keyword, Object> getSection(String id, String channelName) {
        return (Map<Keyword, Object>) getSectionFn.invoke(id, channelName);
    }

    public static Map<Keyword, Object> getSections() {
        return (Map<Keyword, Object>) getSectionsFn.invoke();
    }

    public static Map<Keyword, Object> getSections(String channelName) {
        return (Map<Keyword, Object>) getSectionsFn.invoke(channelName);
    }

    public static Map<Keyword, Object> createArticle(PersistentVector bundle) {
        return (Map<Keyword, Object>) createArticleFn.invoke(bundle);
    }

    public static Map<Keyword, Object> createArticle(PersistentVector bundle, String channelName) {
        return (Map<Keyword, Object>) createArticleFn.invoke(bundle, channelName);
    }

    public static void main(String[] args) {

        Map<Keyword, Object> resp = AppleNews.getArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb");
        printResponse("getArticle 1-arg", resp);

        resp = AppleNews.getArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb", "sandbox");
        printResponse("getArticle 2-arg", resp);

        resp = AppleNews.deleteArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb");
        printResponse("deleteArticle 1-arg", resp);

        resp = AppleNews.deleteArticle("0710ca90-4b11-4abc-d5d6-4ae6de567ebb", "sandbox");
        printResponse("deleteArticle 2-arg", resp);

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

        PersistentVector testBundle = (PersistentVector) loadEdnFn.invoke(Keyword.intern("bundle"));
        resp = AppleNews.createArticle(testBundle);
        printResponse("createArticle 1-arg", resp);

        resp = AppleNews.createArticle(testBundle, "sandbox");
        printResponse("createArticle 2-arg", resp);

    }

    private static void printResponse(String msg, Map<Keyword, Object> resp) {
        System.out.println(msg + " response status: " + resp.get(Keyword.intern("status")));
        System.out.println(msg + " response body: " + resp.get(Keyword.intern("body")));
    }

}
