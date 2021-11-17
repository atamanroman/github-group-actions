(ns gitlab-group-actions.cli-test
  (:require [clojure.test :refer :all]
            [gitlab-group-actions.cli :refer :all]))

(deftest version
  (testing "-v"
    (let [args (parse-args ["-v"])]
      (is (= :exit (:action args)))
      (is (= 0 (:exit-status args)))))
  (testing "--version"
    (let [args (parse-args ["--version"])]
      (is (= :exit (:action args)))
      (is (= 0 (:exit-status args))))))

(deftest help
  (testing "-h"
    (let [args (parse-args ["-h"])]
      (is (= :exit (:action args)))
      (is (= 0 (:exit-status args)))))

  (testing "--help"
    (let [args (parse-args ["-h"])]
      (is (= :exit (:action args)))
      (is (= 0 (:exit-status args))))))

(deftest bad-input
  (let [args (parse-args ["foo"])]
    (is (= :exit (:action args)))
    (is (= 1 (:exit-status args)))))

(deftest start-pipeline
  (testing "--dry-run"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "--dry-run"])]
      (is (= true (:dry-run args)))
      (is (= :start-pipeline (:action args)))
      (is (= false (:recursive args)))
      (is (= "a" (:api-url args)))
      (is (= "b" (:group-id args)))
      (is (= "c" (:access-token args)))
      (is (= :default (:branch args)))
      (is (nil? (:exit-status args)))))

  (testing "-r"
    (let [args (parse-args ["start-pipeline" "a" "b" "c" "-r"])]
      (is (= false (:dry-run args)))
      (is (= :start-pipeline (:action args)))
      (is (= true (:recursive args)))
      (is (= "a" (:api-url args)))
      (is (= "b" (:group-id args)))
      (is (= "c" (:access-token args)))
      (is (= :default (:branch args)))
      (is (nil? (:exit-status args)))))

  (testing "create-tag args are not set"
    (let [args (parse-args ["start-pipeline" "a" "b" "c"])]
      (is (nil? (:tag-name args)))
      (is (nil? (:tag-message args))))))

(deftest create-tag
  (testing "create-tag"
    (let [args (parse-args ["create-tag" "1.2.3" "Release 1.2.3" "a" "b" "c"])]
      (is (= :create-tag (:action args)))
      (is (= "1.2.3" (:tag-name args)))
      (is (= "Release 1.2.3" (:tag-message args)))
      (is (= false (:recursive args)))
      (is (= false (:dry-run args)))
      (is (= "a" (:api-url args)))
      (is (= "b" (:group-id args)))
      (is (= "c" (:access-token args)))
      (is (= :default (:branch args)))
      (is (nil? (:exit-status args))))))
