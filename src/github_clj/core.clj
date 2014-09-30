(ns github-clj.core
  (:require [clj-http.client :as client]))


(def gh_data (client/get "https://api.github.com/search/repositories?q=language:clojure&sort=stars&order=desc" {:accept :json :as :json}))

(defn get-project [fullname]
  (try
   (:body (client/get (str "https://raw.githubusercontent.com/" fullname "/master/project.clj") {:as :clojure}))
    (catch Exception e (println "bqd project" fullname))
    ))

(get-project "quil/quil")

(defn get-version [project]
  (try
  (let [deps (->> (drop 3 project)
                  (apply hash-map)
                  (:dependencies)
                  (map #(take 2 %))
                  (into {}))]
    (deps 'org.clojure/clojure))
  (catch Exception e (println (second project)))))

(get-version (get-project "quil/quil"))

(let [items (get-in gh_data [:body :items])]
   (map (comp get-version get-project :full_name) items))
