(defproject gitlab-group-actions "0.1.0-SNAPSHOT"
  :description "Do Things With all Repos in a Group"
  :url "https://github.com/atamanroman/gitlab-group-actions"
  :license {:name "MIT License"
            :url  "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.1"]
                 [dev.nubank/docopt "0.6.1-fix7"]]
  :repl-options {:init-ns gitlab-group-actions.core}
  :plugins [[lein-cljfmt "0.8.0"]]
  :main gitlab-group-actions.core
  :aot [gitlab-group-actions.core])
