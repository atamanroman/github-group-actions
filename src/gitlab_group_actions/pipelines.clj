(ns gitlab-group-actions.pipelines
  (:require [gitlab-group-actions.api :as api])
  (:require [gitlab-group-actions.projects :as projects]))

(defn log-pipeline [project target-branch & status]
  (println (format "Pipeline for %s on branch %s: %s" (:name project) target-branch (apply str status))))

(defn start-pipeline [{:keys [:access-token :api-url :dry-run]} project target-branch]
  (try
    (let [resp (api/post access-token "%s/projects/%d/pipeline?ref=%s" [api-url (:id project) target-branch] dry-run)
          body (:body resp)
          pipeline (:web_url [body])]
      (do (log-pipeline project target-branch "CREATED @ " pipeline)
          {:id       (:id project),
           :pipeline pipeline}))
    (catch Exception e (log-pipeline project target-branch "FAILED" (.toString e) {:id    (:id project),
                                                                                   :error e}))))
(defn start-pipelines [gitlab-options projects]
  (map (fn [project]
         (start-pipeline gitlab-options project (projects/get-target-branch project (:branch gitlab-options))))
       projects))
