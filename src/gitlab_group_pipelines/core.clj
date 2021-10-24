(ns gitlab-group-pipelines.core
  (:require [gitlab-group-pipelines.cli :as cli]
            [gitlab-group-pipelines.pipelines :as pipelines]
            [gitlab-group-pipelines.projects :as projects]))

(defn -main [& args] (let [gitlab-options (cli/parse-args args)]
                       (doall (pipelines/start-pipelines gitlab-options (projects/get-projects gitlab-options)))))
