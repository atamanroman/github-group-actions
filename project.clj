(defproject gitlab-group-pipelines "0.1.0-SNAPSHOT"
  :description "Start a Pipeline for all Repos in a Group"
  :url "https://github.com/atamanroman/gitlab-group-actions"
  :license {:name "MIT License"
            :url  "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.0"]]
  :repl-options {:init-ns gitlab-group-pipelines.core}
  :plugins [[lein-cljfmt "0.8.0"]]
  :main gitlab-group-pipelines.core)
