(ns clj-applenewsapi.multipart
  (:require [clojure.java.io :as io]
            [clj-http.multipart :as mp]
            [clojure.java.io :refer [copy]]
            [clojure.string :refer [split]])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream FileInputStream]
           [java.net URL URLConnection]
           [org.apache.commons.imaging.formats.jpeg.exif ExifRewriter]
           [java.util UUID]))

(set! *warn-on-reflection* true)

(defn to-bytes [^String s]
  (.getBytes s "UTF-8"))

(defn random []
  (.replaceAll (str (UUID/randomUUID)) "-" ""))

(defn part
  "Create the part composing all the necessary pieces
  together into a byte-array. Payload is expected to be [B"
  [boundary c-type c-name ^bytes payload out]
  (let [header (str (format "--%s\r\n" boundary)
                    (format "Content-Type: %s\r\n" c-type)
                    (format "Content-Disposition: form-data; name=%s; filename=%s; size=%s\r\n" c-name c-name (alength payload)))
        return "\r\n"]
    (copy (to-bytes header) out)
    (copy (to-bytes return) out)
    (copy payload out)
    (copy (to-bytes return) out)))

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

(defn as-bytes
  "Slurp the bytes from the url into a ByteArrayOuputStream and returns it."
  [url]
  (with-open [out (java.io.ByteArrayOutputStream.)]
          (let [^URLConnection conn (doto
                                      (.openConnection (URL. url))
                                      (.setConnectTimeout 30000)
                                      (.setReadTimeout 90000))]
            (copy (.getInputStream conn) out)
            (.toByteArray out))))

(defn create-parts-for-files [boundary urls out]
  (doseq [url urls]
    (part boundary
          (mime-type url)
          (file-name url)
          (as-bytes url) out)))

(defn payload [boundary bundle]
  (let [article-json (:content (first (filter #(= "article.json" (:filename %)) bundle)))
        metadata (:content (first bundle))
        urls (remove nil? (mapv :url bundle))]
    (with-open [out (ByteArrayOutputStream.)]
      (part boundary "application/json" "metadata" (to-bytes metadata) out)
      (part boundary "application/json" "article.json" (to-bytes article-json) out)
      (create-parts-for-files boundary urls out)
      (copy (to-bytes (format "--%s--" boundary)) out)
      (.toByteArray out))))
