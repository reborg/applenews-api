(ns clj-applenewsapi.parallel)

(defn ppmap
  "Like pmap with a configurable chunk size."
  [f coll n]
  (let [rets (map #(future (f %)) coll)
        step (fn step [[x & xs :as vs] fs]
               (lazy-seq
                 (if-let [s (seq fs)]
                   (cons (deref x) (step xs (rest s)))
                   (map deref vs))))]
    (step rets (drop n rets))))
