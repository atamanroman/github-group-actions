(ns gitlab-group-actions.excludes
  (:require [clojure.tools.logging :as log]
            [clojure.string :as str])
  (:import (java.util.regex Pattern)))

(defn- parse-regex [str] (if-not (str/blank? str)
                           (do (try
                                 (let [re (Pattern/compile str)]
                                   (log/debugf "Parsed RegEx=%s" str)
                                   re)
                                 (catch Exception e
                                   (log/warnf "Skip broken RegEx=%s" str e))))))

; doall: https://stackoverflow.com/questions/4118123/read-a-very-large-text-file-into-a-list-in-clojure
(defn from-file [path] (with-open [rdr
                                   (try (clojure.java.io/reader path)
                                        (catch Exception e
                                          (throw (RuntimeException. (str "Could not open excludes-file=" path) e))))]
                         (let [exlusions (doall (remove nil? (map #(parse-regex %) (line-seq rdr))))]
                           (println (str "Exclusions from " path ":") exlusions)
                           exlusions)))
