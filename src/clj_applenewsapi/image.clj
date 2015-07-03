(ns clj-applenewsapi.image
  (:require [clojure.string :refer [split]]
            [clj-applenewsapi.bytes :as bbytes]
            [clojure.java.io :refer [copy]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileOutputStream]
           [javax.imageio ImageIO]
           [org.imgscalr Scalr]
           [java.awt.image BufferedImageOp]))

(set! *warn-on-reflection* true)

(defn extension [url]
  (last (split url #"\.")))

(defn mime-type [url]
  (let [ext (extension url)]
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
  (and (is-image? thumbnail-url)
       (= (file-name thumbnail-url) (file-name part-url))))

(defn ^bytes adjust-size [url]
  (letfn [(f [is os]
            (-> (Scalr/resize
                  (ImageIO/read is)
                  org.imgscalr.Scalr$Mode/AUTOMATIC
                  700
                  420
                  (make-array BufferedImageOp 0))
                (ImageIO/write (extension url) os)))]
    (bbytes/with-url url f)))
