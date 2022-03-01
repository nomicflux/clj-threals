(ns clj-threals.operations-test
  (:require [clj-threals.operations :as sut]
            [clojure.test :refer [is deftest]]
            [clj-threals.display :as display]
            [clj-threals.threals :as threals]))

(defn gt_a_spy
  [colour x y]
  (println "testing" colour)
  (display/display x)
  (display/display y)
  (println "*********")
  (let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_]
                           (println colour)
                           (display/display x_)
                           (display/display y)
                           (println (sut/gt_a colour y x_))
                           (println "---")
                           (gt_a_spy colour y x_)) (get x colour)))
         (empty? (filter (fn [y_]
                           (println c1)
                           (display/display x)
                           (display/display y_)
                           (println (sut/gt_a c1 x y_))
                           (println "---")
                           (gt_a_spy c1 x y_)) (get y c1)))
         (empty? (filter (fn [y_]
                           (println colour)
                           (display/display x)
                           (display/display y_)
                           (println (sut/gt_a c2 x y_))
                           (println "---")
                           (gt_a_spy c2 x y_)) (get y c2))))))

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

(deftest gt_c_tests
  (is (sut/gt_c :red threals/red threals/green))
  (is (sut/gt_c :red threals/red threals/blue))
  (is (sut/gt_c :red threals/red threals/red))
  (is (not (sut/gt_c :red threals/green threals/red)))
  (is (not (sut/gt_c :red threals/blue threals/red)))
  (is (sut/gt_c :green threals/green threals/blue))
  (is (sut/gt_c :green threals/green threals/red))
  (is (sut/gt_c :green threals/green threals/green))
  (is (not (sut/gt_c :green threals/blue threals/green)))
  (is (not (sut/gt_c :green threals/red threals/green)))
  (is (sut/gt_c :blue threals/blue threals/red))
  (is (sut/gt_c :blue threals/blue threals/green))
  (is (sut/gt_c :blue threals/blue threals/blue))
  (is (not (sut/gt_c :blue threals/red threals/blue)))
  (is (not (sut/gt_c :blue threals/green threals/blue))))
