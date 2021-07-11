(ns gitlab-group-pipelines.core
  (:require [clj-http.client :as client])
  (:import (java.net URI)))

(def projects-url "%s/groups/%d/projects?include_subgroups=true&simple=true&archived=false&per_page=100&page=%d")
(def pipeline-url "%s/projects/%d/pipeline?ref=%s")

(defn auth [gitlab-token] {:private-token gitlab-token})

(defn get-projects
  ([options] (get-projects options 1))
  ([{:keys [:gitlab-url :gitlab-groupid :gitlab-token]} page]
   (let [resp (client/get (format projects-url gitlab-url gitlab-groupid page)
                          {:as      :json
                           :headers (auth gitlab-token)})
         body (:body resp)
         total_pages (Integer/parseInt (:x-total-pages (:headers resp)))]
     (if (> total_pages page) (println "OMG THERE ARE MORE PAGEZZZ")) ; TODO
     (println "Projects:" body)
     body)))

(defn pipeline-log [project]
  (str "Pipeline for " (:name project) "/" (:default_branch project) ":"))

(defn start-pipeline [{:keys [:gitlab-url :gitlab-token]} project]
  (try
    (let [resp (client/post (format pipeline-url gitlab-url (:id project) (:default_branch project))
                            {:as      :json
                             :headers (auth gitlab-token)})
          body (:body resp)
          pipeline (:web_url body)]
      (println (pipeline-log project) "CREATED @" pipeline)
      body)
    (catch Exception e (println (pipeline-log project) "FAILED" (.toString e)))))

(defn start-pipelines [gitlab-options projects]
  (map (fn [project] (start-pipeline gitlab-options project)) projects))

(defn exitWithUsage []
  (println "Usage: gitlab-group-pipelines <gitlab-url> <groupid> <gitlab-token>")
  (System/exit 1))

(defn parse-args [args]
  (if (= (count args) 3)
    (let [parsed {:gitlab-url     (.toString (.resolve (URI/create (nth args 0)) "/api/v4"))
                  :gitlab-groupid (Integer/parseInt (nth args 1))
                  :gitlab-token   (nth args 2)}]
      (println "CLI Arguments:" parsed)
      parsed)
    (exitWithUsage)))

(defn -main [& args] (let [gitlab-options (parse-args args)]
                     (start-pipelines gitlab-options (get-projects gitlab-options))))
