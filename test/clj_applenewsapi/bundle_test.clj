(ns clj-applenewsapi.bundle-test
  (:require [midje.sweet :refer :all]
            [clj-applenewsapi.bundle :refer :all]))

(facts "updating revisions"
       (fact "it swap current with given if already there"
             (revision (update-revision (load-edn :bundle) "some-revision-no")) => "some-revision-no")
       (fact "it add the revision if non existent"
             (revision (update-revision (update-metadata (load-edn :bundle) "{\"data\":{\"revision\":\"AAAAAAAAAAAAAAAAAAAAAA==\"}}") "rev2")) => "rev2"))
