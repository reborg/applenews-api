(defproject net.reborg/applenews-api "1.0.2-SNAPSHOT"
  :description "Java/Clojure client for the Apple News REST api (https://developer.apple.com/go/?id=news-api-ref)"
  :url "https://github.com/reborg/applenews-api"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :scm {:name "git"
        :url "https://github.com/reborg/applenews-api"}
  :pom-addition [:developers [:developer
                              [:name "reborg"]
                              [:email "reborg@reborg.net"]
                              [:url "http://reborg.net"]]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "1.1.2"]
                 [environ "1.0.0"]
                 [bytebuffer "0.2.0"]
                 [cheshire "5.5.0"]
                 [org.imgscalr/imgscalr-lib "4.2"]
                 [clj-time "0.9.0"]]
  :plugins [[lein-environ "1.0.0"]
            [lein-javadoc "0.3.0"]]
  :javadoc-opts {:package-names ["net.reborg"]}
  :java-source-paths ["java"]
  :repl-options {:init (do (require 'midje.repl) (midje.repl/autotest))
                 :timeout 250000}
  :profiles {:uberjar {:source-paths ["src" "java"]
                       :aot :all}
             :dev {:dependencies [[midje "1.7.0"]]}}
  :jvm-opts ~(vec (map (fn [[p v]] (str "-D" (name p) "=" v))
                       {:java.awt.headless "true"}))
  :deploy-repositories [["releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                                     :creds :gpg}
                         "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]]
  ;; :jvm-opts ["-agentpath:/Applications/YourKit_Java_Profiler_2014_build_14112.app/Contents/Resources/bin/mac/libyjpagent.jnilib=tracing,onexit=snapshot,delay=0"])
  )
