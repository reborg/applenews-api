(ns clj-applenewsapi.response-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.bundle :refer [load-edn]]
            [clj-applenewsapi.response :refer :all]))

(facts "enriching the api response"
       (fact "get article"
             (let [r (load-edn :get-response)]
               (:body (enrich r)) => truthy
               (:id (enrich r)) => "69148703-cc53-4c5a-b87c-d93a48dc82ee"
               (:http-response-date (enrich r)) => "Thu, 03 Sep 2015 09:25:56 GMT"
               (:request-time (enrich r)) => 1999
               (:channel-id (enrich r)) => "7e577d53-a79b-4431-9b43-6935a7ac7579"
               (:section-ids (enrich r)) => ["02be22cb-cf88-42c2-eee1-537a2d27d899"]
               (:revision (enrich r)) => "AAAAAAAAAAAAAAAAAAAAAQ=="
               (:share-url (enrich r)) => "https://news.apple.com/AaRSHA8xTTFq4fNk6SNyC7g"
               (:is-candidate-to-be-featured (enrich r)) => false
               (:is-sponsored (enrich r)) => false
               (:created-at (enrich r)) => "2015-09-02T16:21:08Z"
               (:modified-at (enrich r)) => "2015-09-02T16:21:23Z"
               (:type (enrich r)) => "article"
               (:status (enrich r)) => 200
               (:article-id (enrich r)) => "3184898"))
       (fact "create article"
             (let [r (load-edn :create-response)]
               (:body (enrich r)) => truthy
               (:id (enrich r)) => "c39b616f-7a10-4caf-9909-38c40e5b3dc8"
               (:http-response-date (enrich r)) => "Thu, 03 Sep 2015 09:26:51 GMT"
               (:request-time (enrich r)) => 6436
               (:channel-id (enrich r)) => "7e577d53-a79b-4431-9b43-6935a7ac7579"
               (:section-ids (enrich r)) => ["2d9ce476-cfdc-3430-8920-e6e2afb2f39b"]
               (:revision (enrich r)) => "AAAAAAAAAAAAAAAAAAAAAA=="
               (:share-url (enrich r)) => "https://news.apple.com/Aw5thb3oQTK-ZCTjEDls9yA"
               (:is-candidate-to-be-featured (enrich r)) => false
               (:is-sponsored (enrich r)) => false
               (:created-at (enrich r)) => "2015-09-03T09:26:56Z"
               (:modified-at (enrich r)) => "2015-09-03T09:26:56Z"
               (:type (enrich r)) => "article"
               (:status (enrich r)) => 201
               (:article-id (enrich r)) => "3184843"))
       (fact "update article"
             (let [r (load-edn :update-response)]
               (:body (enrich r)) => truthy
               (:id (enrich r)) => "c39b616f-7a10-4caf-9909-38c40e5b3dc8"
               (:http-response-date (enrich r)) => "Thu, 03 Sep 2015 09:28:27 GMT"
               (:request-time (enrich r)) => 5878
               (:channel-id (enrich r)) => "7e577d53-a79b-4431-9b43-6935a7ac7579"
               (:section-ids (enrich r)) => ["2d9ce476-cfdc-3430-8920-e6e2afb2f39b"]
               (:revision (enrich r)) => "AAAAAAAAAAAAAAAAAAAAAg=="
               (:share-url (enrich r)) => "https://news.apple.com/Aw5thb3oQTK-ZCTjEDls9yA"
               (:is-candidate-to-be-featured (enrich r)) => false
               (:is-sponsored (enrich r)) => false
               (:created-at (enrich r)) => "2015-09-03T09:26:56Z"
               (:modified-at (enrich r)) => "2015-09-03T09:28:32Z"
               (:type (enrich r)) => "article"
               (:status (enrich r)) => 200
               (:article-id (enrich r)) => "3184843"))
       (fact "update article with errors"
             (let [r (load-edn :update-409-response)]
               (:body (enrich r)) => truthy
               (:http-response-date (enrich r)) => "Thu, 03 Sep 2015 09:28:53 GMT"
               (:request-time (enrich r)) => 5616
               (:status (enrich r)) => 409)))
