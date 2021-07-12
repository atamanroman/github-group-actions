(ns gitlab-group-pipelines.core
  (:require [clj-http.client :as client])
  (:import (java.net URI)))

(def projects-url "%s/groups/%d/projects?include_subgroups=true&simple=true&archived=false&per_page=100&page=%d")
(def pipeline-url "%s/projects/%d/pipeline?ref=%s")

(defn auth-header [gitlab-token] {:private-token gitlab-token})

(defn get-projects
  ([gitlab-options] (get-projects gitlab-options 1))
  ([{:keys [:gitlab-url :gitlab-groupid :gitlab-token]} page]
   (let [resp (client/get (format projects-url gitlab-url gitlab-groupid page)
                          {:as      :json
                           :headers (auth-header gitlab-token)})
         body (:body resp)
         total_pages (Integer/parseInt (:x-total-pages (:headers resp)))]
     (if (> total_pages page) (println "OMG THERE ARE MORE PAGEZZZ")) ; TODO
     (println "Projects:" body)
     body)))

(defn log-pipeline [project & status]
  (apply println (conj status (str "Pipeline for " (:name project) "/" (:default_branch project) ":"))))

(defn create-pipeline-url [gitlab-url project]
  (format pipeline-url gitlab-url (:id project) (:default_branch project)))

(defn dry-start-pipeline [gitlab-url project]
  (do (log-pipeline project "WOULD CREATE @" (create-pipeline-url gitlab-url project) "(DRY-RUN!)")
      {:id       (:id project)
       :pipeline "https://example.com"}))

(defn start-pipeline [{:keys [:gitlab-url :gitlab-token :dry-run]} project]
  (if dry-run (dry-start-pipeline gitlab-url project)
              (try
                (let [resp (client/post (create-pipeline-url gitlab-url project)
                                        {:as      :json
                                         :headers (auth-header gitlab-token)})
                      body (:body resp)
                      pipeline (:web_url body)]
                  (do (log-pipeline project "CREATED @" pipeline)
                      {:id       (:id project),
                       :pipeline pipeline}))
                (catch Exception e (log-pipeline project "FAILED" (.toString e) {:id    (:id project),
                                                                                 :error e})))))
(defn start-pipelines [gitlab-options projects]
  (map (fn [project] (start-pipeline gitlab-options project)) projects))

(defn exit-with-usage []
  (do (println "Usage: gitlab-group-pipelines [--dry-run] <gitlab-url> <groupid> <gitlab-token>")
      (System/exit 1)))

(defn parse-args [args]
  (let [dry-run (= (first args) "--dry-run")
        rest-args (if dry-run (rest args) args)]
    (if (= (count rest-args) 3)
      (let [parsed {:gitlab-url     (.toString (.resolve (URI/create (nth rest-args 0)) "/api/v4"))
                    :gitlab-groupid (Integer/parseInt (nth rest-args 1))
                    :gitlab-token   (nth rest-args 2)
                    :dry-run        dry-run}]
        (do (println "CLI Arguments:" parsed)
            parsed))
      (exit-with-usage))))

(defn -main [& args] (let [gitlab-options (parse-args args)]
                       (start-pipelines gitlab-options (get-projects gitlab-options))))
