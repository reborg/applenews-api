package com.mailonline;

import clojure.java.api.Clojure;
import clojure.lang.*;

import java.util.Map;

public class AppleNews {

    private static IFn requireFn = Clojure.var("clojure.core", "require");
    static {
        requireFn.invoke(Clojure.read("com.mailonline.applenews-api.core"));
        requireFn.invoke(Clojure.read("com.mailonline.applenews-api.bundle"));
        requireFn.invoke(Clojure.read("com.mailonline.applenews-api.config"));
    }

    private static IFn loadEdnFn = Clojure.var("com.mailonline.applenews-api.bundle", "load-edn");
    private static IFn idFromJsonFn = Clojure.var("com.mailonline.applenews-api.bundle", "id-from-json");

    private static IFn getArticleFn = Clojure.var("com.mailonline.applenews-api.core", "get-article");
    private static IFn deleteArticleFn = Clojure.var("com.mailonline.applenews-api.core", "delete-article");
    private static IFn getChannelFn = Clojure.var("com.mailonline.applenews-api.core", "get-channel");
    private static IFn getSectionFn = Clojure.var("com.mailonline.applenews-api.core", "get-section");
    private static IFn getSectionsFn = Clojure.var("com.mailonline.applenews-api.core", "get-sections");
    private static IFn createArticleFn = Clojure.var("com.mailonline.applenews-api.core", "create-article");

    private static final IFn withBindingsFn = Clojure.var("clojure.core", "with-bindings*");
    private static final IFn listFn = Clojure.var("clojure.core", "list*");
    private static final IFn env = Clojure.var("com.mailonline.applenews-api.config", "*env*");

    public static Object withConfig(IFn f, Object... args) {
        Object config = loadEdnFn.invoke(Keyword.intern("config"));
        Object res = listFn.invoke(RT.map(env, config), f, args);
        return withBindingsFn.applyTo((ISeq) res);
    }

    public static Map<Keyword, Object> getArticle(String id) {
        return (Map<Keyword, Object>) withConfig(getArticleFn, id);
    }

    public static Map<Keyword, Object> getArticle(String id, String channelName) {
        return (Map<Keyword, Object>) withConfig(getArticleFn, id, channelName);
    }

    public static Map<Keyword, Object> deleteArticle(String id) {
        return (Map<Keyword, Object>) withConfig(deleteArticleFn, id);
    }

    public static Map<Keyword, Object> deleteArticle(String id, String channelName) {
        return (Map<Keyword, Object>) withConfig(deleteArticleFn, id, channelName);
    }

    public static Map<Keyword, Object> getChannel() {
        return (Map<Keyword, Object>) withConfig(getChannelFn);
    }

    public static Map<Keyword, Object> getChannel(String id) {
        return (Map<Keyword, Object>) withConfig(getChannelFn, id);
    }

    public static Map<Keyword, Object> getSection(String id) {
        return (Map<Keyword, Object>) withConfig(getSectionFn, id);
    }

    public static Map<Keyword, Object> getSection(String id, String channelName) {
        return (Map<Keyword, Object>) withConfig(getSectionFn, id, channelName);
    }

    public static Map<Keyword, Object> getSections() {
        return (Map<Keyword, Object>) withConfig(getSectionsFn);
    }

    public static Map<Keyword, Object> getSections(String channelName) {
        return (Map<Keyword, Object>) withConfig(getSectionsFn, channelName);
    }

    public static Map<Keyword, Object> createArticle(PersistentVector bundle) {
        return (Map<Keyword, Object>) withConfig(createArticleFn, bundle);
    }

    public static Map<Keyword, Object> createArticle(PersistentVector bundle, String channelName) {
        return (Map<Keyword, Object>) withConfig(createArticleFn, bundle, channelName);
    }

    public static PersistentVector loadSampleBundle() {
        return (PersistentVector) loadEdnFn.invoke(Keyword.intern("bundle"));
    }

    public static void printResponse(String msg, Map<Keyword, Object> resp) {
        System.out.println(msg + " response status: " + resp.get(Keyword.intern("status")));
        System.out.println(msg + " response body: " + resp.get(Keyword.intern("body")));
    }

    public static String getId(Map<Keyword, Object> resp) {
        String body = (String) resp.get(Keyword.intern("body"));
        return (String) idFromJsonFn.invoke(body);
    }
}
