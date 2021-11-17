(ns gitlab-group-actions.pipelines
  (:require [gitlab-group-actions.api :as api])
  (:require [gitlab-group-actions.projects :as projects]))

(def pipeline-endpoint "%s/projects/%d/pipeline?ref=%s")

(defn log-pipeline [project target-branch & status]
  (apply println (conj status (str "Pipeline for " (:name project) "/" target-branch ":"))))

(defn start-pipeline [{:keys [:access-token :api-url :dry-run]} project target-branch]
  (try
    (let [resp (api/post access-token pipeline-endpoint [api-url (:id project) target-branch] dry-run)
          body (:body resp)
          pipeline (:web_url [body])]
      (do (log-pipeline project "CREATED @" pipeline)
          {:id       (:id project),
           :pipeline pipeline}))
    (catch Exception e (log-pipeline project "FAILED" (.toString e) {:id    (:id project),
                                                                     :error e}))))
(defn start-pipelines [gitlab-options projects]
  (map (fn [project]
         (start-pipeline gitlab-options project (projects/get-target-branch project (:branch gitlab-options))))
       projects))
