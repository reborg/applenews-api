(ns clj-applenewsapi.multipart
  (:require [clojure.java.io :as io]
            [clj-http.multipart :as mp]
            [clojure.string :refer [split]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream]
           [javax.imageio ImageIO]
           [java.net URL]
           [org.apache.commons.imaging.formats.jpeg.exif ExifRewriter]
           [java.util UUID]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn random []
  (.replaceAll (str (UUID/randomUUID)) "-" ""))

(defn part [boundary c-type c-name txt]
  (str (format "--%s\r\n" boundary)
       (format "Content-Type: %s\r\n" c-type)
       (format "Content-Disposition: form-data; name=%s; filename=%s; size=%s\r\n" c-name c-name (alength (.getBytes txt)))
       "\r\n"
       txt
       "\r\n"))

(defn mime-type [url]
  (let [ext (last (split url #"\."))]
    (cond
      (= "jpg" ext)  "image/jpeg"
      (= "jpeg" ext) "image/jpeg"
      (= "png" ext)  "image/png"
      (= "bmp" ext)  "image/bmp"
      (= "gif" ext)  "image/gif"
      (= "tif" ext)  "image/tiff"
      (= "tiff" ext) "image/tiff"
      (= "ico" ext)  "image/x-icon"
      :else "application/octet-stream")))

(defn file-name [url]
  (last (split url #"/")))

(defn strip-headers [url]
  (let [is (.openStream (URL. url))
        os (ByteArrayOutputStream.)]
    (.removeExifMetadata (ExifRewriter.) is os)
    (ByteArrayInputStream. (.toByteArray os))))

(defn create-parts-for-files [boundary urls]
  (letfn [(f [url]
            (part boundary (mime-type url) (file-name url) (slurp (strip-headers url) :encoding "UTF-8")))]
  (apply str (map f urls))))

(defn payload [boundary bundle]
  (let [article-json (:content (first (filter #(= "article.json" (:filename %)) bundle)))
        metadata (:content (first bundle))
        urls (remove nil? (mapv :url bundle))]
    (str (part boundary  "application/json" "metadata" metadata)
         (part boundary  "application/json" "article.json" article-json)
         (create-parts-for-files boundary urls)
         (format "--%s--" boundary))))
