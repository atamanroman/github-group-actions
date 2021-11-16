(ns gitlab-group-actions.cli-test
  (:require [clojure.test :refer :all]
            [gitlab-group-actions.cli :refer :all]))

(deftest version
  (testing "-v"
    (let [args (parse-args ["-v"])]
      (is (= (:action args) :exit))
      (is (= (:exit-status args) 0))))
  (testing "--version"
    (let [args (parse-args ["--version"])]
      (is (= (:action args) :exit))
      (is (= (:exit-status args) 0)))))

(deftest help
  (testing "-h"
    (let [args (parse-args ["-h"])]
      (is (= (:action args) :exit))
      (is (= (:exit-status args) 0))))

  (testing "--help"
    (let [args (parse-args ["-h"])]
      (is (= (:action args) :exit))
      (is (= (:exit-status args) 0)))))

(deftest bad-input
  (let [args (parse-args ["foo"])]
    (is (= (:action args) :exit))
    (is (= (:exit-status args) 1))))

(deftest start-pipeline
  (testing "--dry-run"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "--dry-run"])]
      (is (= (:dry-run args) true))
      (is (= (:action args) :start-pipeline))
      (is (= (:recursive args) false))
      (is (= (:api-url args) "a"))
      (is (= (:group-id args) "b"))
      (is (= (:access-token args) "c"))
      (is (= (:branch args) ":default"))
      (is (nil? (:exit-status args)))))

  (testing "-r"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "-r"])]
      (is (= (:dry-run args) false))
      (is (= (:action args) :start-pipeline))
      (is (= (:recursive args) true))
      (is (= (:api-url args) "a"))
      (is (= (:group-id args) "b"))
      (is (= (:access-token args) "c"))
      (is (= (:branch args) ":default"))
      (is (nil? (:exit-status args)))))

  (testing "create-tag args are not set"
    (let [args (parse-args ["start-pipeline" "a" "b" "c"])]
      (is (nil? (:tag-name args)))
      (is (nil? (:tag-message args))))))

(deftest create-tag
  (testing "create-tag"
    (let [args (parse-args ["create-tag" "1.2.3" "Release 1.2.3" "a" "b" "c"])]
      (is (= (:action args) :create-tag))
      (is (= (:tag-name args) "1.2.3"))
      (is (= (:tag-message args) "Release 1.2.3"))
      (is (= (:recursive args) false))
      (is (= (:dry-run args) false))
      (is (= (:api-url args) "a"))
      (is (= (:group-id args) "b"))
      (is (= (:access-token args) "c"))
      (is (= (:branch args) ":default"))
      (is (nil? (:exit-status args))))))
