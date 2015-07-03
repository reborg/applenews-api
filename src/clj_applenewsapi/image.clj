(ns clj-applenewsapi.image
  (:require [clojure.string :refer [split]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream]
           [java.net URL URLConnection]
           [java.util UUID]))

(set! *warn-on-reflection* true)

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

(defn is-image? [url]
  (not= "application/octet-stream" (mime-type url)))

(defn thumbnail? [thumbnail-url part-url]
  (and (is-image? part-url)
       (= (file-name thumbnail-url) (file-name part-url))))

(defn ^bytes adjust-size [url]
  (let [is (.openStream (URL. url))
        os (ByteArrayOutputStream.)]
    ; (.removeExifMetadata (ExifRewriter.) is os)
    (ByteArrayInputStream. (.toByteArray os))))
