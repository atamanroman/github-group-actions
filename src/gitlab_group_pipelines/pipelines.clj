(ns gitlab-group-pipelines.pipelines
  (:require [gitlab-group-pipelines.api :as api]))

(def pipeline-url "%s/projects/%d/pipeline?ref=%s")

(defn log-pipeline [project & status]
  (apply println (conj status (str "Pipeline for " (:name project) "/" (:default_branch project) ":"))))

(defn start-pipeline [{:keys [:access-token :api-url :dry-run]} project]
  (try
    (let [resp (api/post access-token pipeline-url [api-url (:id project) (:default_branch project)] dry-run)
          body (:body resp)
          pipeline (:web_url [body])]
      (do (log-pipeline project "CREATED @" pipeline)
          {:id       (:id project),
           :pipeline pipeline}))
    (catch Exception e (log-pipeline project "FAILED" (.toString e) {:id    (:id project),
                                                                     :error e}))))
(defn start-pipelines [gitlab-options projects]
  (map (fn [project] (start-pipeline gitlab-options project)) projects))
