(ns clj-applenewsapi.crypto-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.config :refer [*env*]]
            [clj-applenewsapi.crypto :refer :all]))

(def rest-sample-config
  {:clj-applenewsapi {:host "https://testhost"
               :channels {:ch1 {:channel-id "ch1-id"
                                :api-key-id "ch1-key"
                                :api-key-secret "ch1-secret"}
                          :ch2 {:channel-id "ch2-id"
                                :api-key-id "ch2-key"
                                :api-key-secret "ch2-secret"}
                          :ch3 {:channel-id "ch3-id"
                                :api-key-id "ch3-key"
                                :api-key-secret "ch3-secret"}}}})

(def expected "YmI0NDQ2NDllOTJkNThhNTc3M2U5YmFjNTE4NDA0ZWQxODBlOTFiMjNlNzBjYzNjNDNkYjkwZmYyZDczOTAyNQ==")

(facts "crypto facts"
       (fact "sanity check"
             (binding [*env* rest-sample-config]
               (signature (canonical
                            "GET"
                            "http://localhost:8080/channels/"
                            "2015-06-17T09:53:40Z") "ch1-secret")) => expected))

; (def api-key-secret  "HgyfMPjFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF=")
; (def api-key-id  "240ab880-FFFF-FFFF-FFFF-FFFFFFFFFFFF")
; (def channel-id  "cdb737aa-FFFF-FFFF-FFFF-FFFFFFFFFFFF")

; (def url (format "http://localhost:8080/channels/%s" channel-id))
; (def date "2015-06-17T09:53:40Z")
; (def canonical-request (str "GET" url date))
; (def key (b64/decode (.getBytes api-key-secret)))
; (def hashed (sha256-hmac canonical-request key))
; (def signature (String. (b64/encode (.getBytes hashed))))
; (def authorization (format "HHMAC; key=%s; signature=%s; date=%s" api-key-id signature date))
; (def headers {"Authorization" authorization})

; or the python version!

; import requests
; import base64
; from hashlib import sha256
; import hmac
; from datetime import datetime

; channel_id = '7e577d53-a79b-4431-9b43-6935a7ac7579'
; api_key_id = '45f1a203-80d8-47c9-ae12-17bd0189b0f9'
; api_key_secret = 'CFlfe+PhvhiWZynv+2P38AwmBHaHsGqKat2xNmYInMI='
; url = 'https://u48r14.digitalhub.com:443/channels/%s' % channel_id
; date = datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%SZ")
; canonical_request = 'GET' + url + str(date)
; key = base64.b64decode(api_key_secret)
; hashed = hmac.new(key, canonical_request, sha256)
; signature = hashed.digest().encode("base64").rstrip('\n')
; authorization = 'HHMAC; key=%s; signature=%s; date=%s' % (api_key_id, str(signature), date)
; headers = {'Authorization': authorization}
; response = requests.get(url, headers=headers)
; print response.text
