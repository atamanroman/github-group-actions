(ns gitlab-group-pipelines.core
  (:require [gitlab-group-pipelines.cli :as cli]
            [gitlab-group-pipelines.pipelines :as pipelines]
            [gitlab-group-pipelines.projects :as projects]))

(defn -main [& raw] (let [opts (cli/parse-args raw)]
                      (cond
                        (= (:action opts) :exit) (System/exit (:exit-status opts))
                        (= (:action opts) :start-pipeline) (doall (pipelines/start-pipelines opts (projects/get-projects opts)))
                        :else (throw (new IllegalStateException "Not implemented yet!")))))
