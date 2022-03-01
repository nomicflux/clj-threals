(ns clj-threals.operations-test
  (:require [clj-threals.operations :as sut]
            [clojure.test :refer [is deftest]]
            [clj-threals.threals :as threals]))

(deftest gt_a_tests
  (is (sut/gt_a :red threals/red threals/green))
  (is (sut/gt_a :red threals/red threals/blue))
  (is (sut/gt_a :red threals/red threals/red))
  (is (not (sut/gt_a :red threals/green threals/red)))
  (is (not (sut/gt_a :red threals/blue threals/red)))
  (is (sut/gt_a :green threals/green threals/blue))
  (is (sut/gt_a :green threals/green threals/red))
  (is (sut/gt_a :green threals/green threals/green))
  (is (not (sut/gt_a :green threals/blue threals/green)))
  (is (not (sut/gt_a :green threals/red threals/green)))
  (is (sut/gt_a :blue threals/blue threals/red))
  (is (sut/gt_a :blue threals/blue threals/green))
  (is (sut/gt_a :blue threals/blue threals/blue))
  (is (not (sut/gt_a :blue threals/red threals/blue)))
  (is (not (sut/gt_a :blue threals/green threals/blue))))

(deftest gt_b_tests
  (is (sut/gt_b :red threals/red threals/green))
  (is (sut/gt_b :red threals/red threals/blue))
  (is (sut/gt_b :red threals/red threals/red))
  (is (not (sut/gt_b :red threals/green threals/red)))
  (is (not (sut/gt_b :red threals/blue threals/red)))
  (is (sut/gt_b :green threals/green threals/blue))
  (is (sut/gt_b :green threals/green threals/red))
  (is (sut/gt_b :green threals/green threals/green))
  (is (not (sut/gt_b :green threals/blue threals/green)))
  (is (not (sut/gt_b :green threals/red threals/green)))
  (is (sut/gt_b :blue threals/blue threals/red))
  (is (sut/gt_b :blue threals/blue threals/green))
  (is (sut/gt_b :blue threals/blue threals/blue))
  (is (not (sut/gt_b :blue threals/red threals/blue)))
  (is (not (sut/gt_b :blue threals/green threals/blue))))
