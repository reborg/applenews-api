(ns clj-applenewsapi.bundle
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn load-edn [s]
  (edn/read-string (slurp (io/resource (str (name s) ".edn")))))
