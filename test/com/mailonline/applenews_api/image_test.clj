(ns com.mailonline.applenews-api.image-test
  (:require [midje.sweet :refer :all]
            [clojure.java.io :refer [writer]]
            [com.mailonline.applenews-api.config :refer [*env*]]
            [com.mailonline.applenews-api.image :refer :all])
  (:import [java.io File ByteArrayOutputStream ByteArrayInputStream FileOutputStream]
           [javax.imageio ImageIO]
           [org.imgscalr Scalr]
           [java.util UUID]
           [java.awt.image BufferedImageOp]))

(defn cfg [resize?]
  {:applenews-api {:thumbnail-resize-enable resize?
                   :thumbnail-resize-height 666
                   :thumbnail-resize-width 888}})

(defn width-and-height [image-file]
  (let [image (ImageIO/read image-file)]
    [(.getWidth image) (.getHeight image)]))

(defn to-image [^bytes bbytes]
  (let [file (File/createTempFile (str (UUID/randomUUID)) ".jpg")
        _ (.deleteOnExit file)]
    (with-open [fos (FileOutputStream. file)]
      (ImageIO/write (ImageIO/read (ByteArrayInputStream. bbytes)) "jpg" fos)
      file)))

(facts "image resizing"
       (fact "should not happen if switch is off"
             (binding [*env* (cfg false)]
               (width-and-height (to-image (adjust-size "file:./test/resize-me.jpg"))) => [636 382]))
       (fact "but it resizes otherwise"
             (binding [*env* (cfg true)]
               (width-and-height (to-image (adjust-size "file:./test/resize-me.jpg"))) => [888 533])) )
