(defproject clj-applenewsapi "0.0.8"
  :description "Clojure library to talk to Apple Publisher API services"
  :dependencies [[org.clojure/clojure "1.7.0-RC2"]
                 [clj-http "1.1.2"]
                 [environ "1.0.0"]
                 [commons-codec/commons-codec "1.9"]
                 [org.apache.commons/commons-imaging "1.0-SNAPSHOT"]
                 [bytebuffer  "0.2.0"]
                 [clj-time "0.9.0"]]
  :plugins [[lein-environ "1.0.0"]]
  :repositories [["apache" "https://repository.apache.org/content/repositories/snapshots/"]]
  :repl-options {:init (do (require 'midje.repl) (midje.repl/autotest))
                 :timeout 25000000000}
  :profiles {:dev {:dependencies [[midje "1.7.0-beta1"]]}}
  :jvm-opts ~(vec (map (fn [[p v]] (str "-D" (name p) "=" v))
                       {:java.awt.headless "true"}))
  ;; :jvm-opts ["-agentpath:/Applications/YourKit_Java_Profiler_2014_build_14112.app/Contents/Resources/bin/mac/libyjpagent.jnilib=tracing,onexit=snapshot,delay=0"])
  )
