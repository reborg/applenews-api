(ns net.reborg.applenews-api.image
  (:require [clojure.string :refer [split]]
            [net.reborg.applenews-api.bytes :as bbytes]
            [net.reborg.applenews-api.config :refer [resize-thumbnail? thumbnail-resize-height thumbnail-resize-width]]
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
            (try (let [image (ImageIO/read is)
                       width (.getWidth image)
                       height (.getHeight image)]
                   (-> (if (and (resize-thumbnail?) (or (< width 600) (< height 400)))
                         (Scalr/resize image org.imgscalr.Scalr$Mode/AUTOMATIC (thumbnail-resize-width) (thumbnail-resize-height) (make-array BufferedImageOp 0))
                         image)
                       (ImageIO/write (extension url) os)
                       ; (ImageIO/write (extension url) (FileOutputStream. "resized-700-420.jpg"))
                       ))
                 (catch Exception e
                   (throw (ex-info "Failed adjusting size"
                                   {:url url :in ::adjust-size}
                                   e)))))]
    (bbytes/with-url url f)))
