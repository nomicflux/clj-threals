(ns clj-threals.operations-test
  (:require [clj-threals.operations :as sut]
            [clojure.test :refer [are is deftest]]
            [clj-threals.display :as display]
            [clj-threals.threals :as threals]))

(deftest gt_tests
  (doseq [gt_fn [sut/gt_a sut/gt_b sut/gt_c sut/gt_d]]
    (is (gt_fn :red threals/red threals/green))
    (is (gt_fn :red threals/red threals/blue))
    (is (gt_fn :red threals/red threals/red))
    (is (not (gt_fn :red threals/green threals/red)))
    (is (not (gt_fn :red threals/blue threals/red)))
    (is (gt_fn :green threals/green threals/blue))
    (is (gt_fn :green threals/green threals/red))
    (is (gt_fn :green threals/green threals/green))
    (is (not (gt_fn :green threals/blue threals/green)))
    (is (not (gt_fn :green threals/red threals/green)))
    (is (gt_fn :blue threals/blue threals/red))
    (is (gt_fn :blue threals/blue threals/green))
    (is (gt_fn :blue threals/blue threals/blue))
    (is (not (gt_fn :blue threals/red threals/blue)))
    (is (not (gt_fn :blue threals/green threals/blue)))))

(deftest domination_tests
  (is (sut/dominates? sut/gt_b :red threals/red threals/green))
  (is (sut/dominates? sut/gt_b :red threals/red threals/blue))
  (is (not (sut/dominates? sut/gt_b :red threals/green threals/red)))
  (is (not (sut/dominates? sut/gt_b :red threals/blue threals/red)))
  (is (sut/dominates? sut/gt_b :green threals/green threals/blue))
  (is (sut/dominates? sut/gt_b :green threals/green threals/red))
  (is (not (sut/dominates? sut/gt_b :green threals/blue threals/green)))
  (is (not (sut/dominates? sut/gt_b :green threals/red threals/green)))
  (is (sut/dominates? sut/gt_b :blue threals/blue threals/red))
  (is (sut/dominates? sut/gt_b :blue threals/blue threals/green))
  (is (not (sut/dominates? sut/gt_b :blue threals/green threals/blue)))
  (is (not (sut/dominates? sut/gt_b :blue threals/red threals/blue)))

  (is (sut/dominates? sut/gt_b :red threals/red-green-blue-tree threals/green))
  (is (sut/dominates? sut/gt_b :red threals/red-green-blue-tree threals/blue))
  (is (sut/dominates? sut/gt_b :red threals/red-blue-green-tree threals/green))
  (is (sut/dominates? sut/gt_b :red threals/red-blue-green-tree threals/blue))
  (is (sut/dominates? sut/gt_b :red threals/red threals/red-green-blue-tree))
  (is (sut/dominates? sut/gt_b :red threals/red threals/red-blue-green-tree))
  (is (not (sut/dominates? sut/gt_b :red threals/red-green-blue-tree threals/red-blue-green-tree)))
  (is (not (sut/dominates? sut/gt_b :red threals/red-blue-green-tree threals/red-green-blue-tree)))
  )

(deftest remove-dominated_tests
  (doseq [gt_fn [sut/gt_b]]
    (is (= #{threals/red}
           (sut/remove-dominated gt_fn :red #{threals/red threals/green threals/blue})))
    (is (= #{(threals/n-red 2)}
           (sut/remove-dominated gt_fn :red #{threals/red threals/green threals/blue (threals/n-red 2)})))
    (is (= #{threals/green}
           (sut/remove-dominated gt_fn :green #{threals/red threals/green threals/blue})))
    (is (= #{threals/blue}
           (sut/remove-dominated gt_fn :blue #{threals/red threals/green threals/blue})))
    (is (= #{(threals/n-red 3)}
           (sut/remove-dominated gt_fn :red #{threals/red (threals/n-red 3) (threals/n-red 2)})))))

(deftest simplification_tests
  (doseq [gt_fn [sut/gt_b]]
    (is (= {:red #{(threals/n-red 2)}
           :green #{(threals/n-green 2)}
           :blue #{(threals/n-blue 2)}}
           (sut/simplify gt_fn
                        {:red #{threals/red threals/green threals/blue (threals/n-red 2)}
                         :green #{threals/red threals/green threals/blue (threals/n-green 2)}
                         :blue #{threals/red threals/green threals/blue (threals/n-blue 2)}})))))

(def threal-list (keys display/colour-map))

(deftest full-eq_tests
  (doseq [gt_fn [sut/gt_b]
          x threal-list]
    (is (sut/full-eq? gt_fn x x))
    (is (sut/full-eq? gt_fn x (sut/++ x threals/zero)))))

(deftest rotate-eq_tests
  (doseq [gt_fn [sut/gt_b]
          x threal-list]
    (is (sut/rotate-eq? gt_fn x x))
    (is (sut/rotate-eq? gt_fn x (sut/++ x threals/zero)))))

(deftest addition-tests
  (doseq [x threal-list
          y threal-list
          gt_fn [sut/gt_b]]
    (is (sut/rotate-eq? gt_fn (sut/++ x y) (sut/sum-with-cache gt_fn x y)))))
