(ns clj-applenewsapi.core
  (:require [clj-http.client :as client]
            [clj-applenewsapi.crypto :refer [signature now canonical]]
            [clj-applenewsapi.multipart :refer [multipart]]
            [clj-applenewsapi.config :as cfg]))

(def default-opts
  {
   :content-type "application/json"
   :accept :json
   :socket-timeout 10000
   :conn-timeout 10000
   :headers {}})

(defn post-opts [mpart]
  (-> default-opts
      (dissoc :content-type)
      (assoc :multipart mpart)))

(defn authorize [opts method url channel-name]
  (let [t (now)
        secret (cfg/api-key-secret channel-name)
        keyid (cfg/api-key-id channel-name)
        digest (signature (canonical method url t) secret)
        auth (format "HHMAC; key=%s; signature=%s; date=%s" keyid digest t)]
    (update-in opts [:headers] #(assoc % "Authorization" auth))))

(defn get-article [id channel-name]
  (let [url (str (cfg/host) "/articles/" id)]
    (client/get url (authorize default-opts "GET" url channel-name))))

(defn get-channel [channel-name]
  (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name))]
    (client/get url (authorize default-opts "GET" url channel-name))))

(defn create-article [channel-name bundle]
  (let [url (str (cfg/host) "/channels/" (cfg/channel-id channel-name) "/articles")
        opts (authorize (post-opts (multipart bundle)) "POST" url channel-name)]
    (client/post url opts)))
