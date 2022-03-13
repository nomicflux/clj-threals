(ns clj-threals.core
  (:require
   [clojure.string :as str]
   [clj-threals.threals :refer :all]
   [clj-threals.operations :refer :all]
   [clj-threals.display :refer [display colour-map]]
   [clojure.set :as set]))

(def gt (memoize gt_d))

(def small-list [zero red green blue yellow cyan magenta star])
(def threal-list (keys colour-map))
(def mega-threal-list
  (into #{} (:acc (reduce (fn [{:keys [acc cache]} [x y]]
                            (let [{:keys [result cache]} (add-with-cache gt x y cache)]
                              {:acc (conj acc result)
                               :cache cache}))
                          {:acc []
                           :cache {}}
                          (for [x threal-list
                                y threal-list]
                            [x y])))))

(defn find-eq-mismatches
  [gt_s gt_eq eq list]
  (loop [xs list
         ys list
         cache= {}
         cache_s {}
         mismatches []
         noncommutative []]
    (if (empty? ys)
      [mismatches noncommutative]
      (if (empty? xs)
        (recur (rest ys) (rest ys) cache= cache_s mismatches noncommutative)
        (let [x (first xs)
              y (first ys)
              {xy :result cache= :cache} (sum-with-cache = cache= x y)
              {sxy :result cache_s :cache} (sum-with-cache gt_s cache_s x y)
              {syx :result cache_s :cache} (sum-with-cache gt_s cache_s y x)
              new-mismatches (cond-> mismatches
                               (not (eq gt_eq xy sxy))
                               (conj [x y]))
              new-noncommutative (cond-> noncommutative
                                   (not (eq gt_eq sxy syx))
                                   (conj [x y]))]
          (recur (rest xs) ys cache= cache_s new-mismatches new-noncommutative))))))

(defn find-3-eq-mismatches
  [gt_s gt_eq eq list]
  (loop [xs list
         ys list
         zs list
         cache {}
         mismatches []]
    (cond
      (empty? zs) mismatches
      (empty? ys) (recur (rest zs) (rest zs) (rest zs) cache mismatches)
      (empty? xs) (recur (rest ys) (rest ys) zs cache mismatches)
      :else (let [x (first xs)
                  y (first ys)
                  z (first zs)
                  {sxy-z :result
                   cache :cache} (let [{:keys [result cache]}
                                        (add-with-cache gt_s x y cache)]
                                    (add-with-cache gt_s result z cache))
                  {sx-yz :result
                   cache :cache} (let [{:keys [result cache]}
                                       (add-with-cache gt_s y z cache)]
                                   (add-with-cache gt_s x result cache))
                  new-mismatches (cond-> mismatches
                                   (not (eq gt_eq sxy-z sx-yz))
                                   (conj [x y z]))]
              (recur (rest xs) ys zs cache new-mismatches)))))

(defn run-eq-mismatches
  ([]
   (run-eq-mismatches {}))
  ([{:keys [list gt_fn eq]
     :or {list threal-list
          gt_fn gt
          eq rotate-eq?}}]
   (let [[m n] (find-eq-mismatches gt_fn gt_fn eq list)]
     (println (count m) (count n)))))

(defn run-3-eq-mismatches
  ([]
   (run-3-eq-mismatches {}))
  ([{:keys [list gt_fn eq]
     :or {list threal-list
          gt_fn gt
          eq rotate-eq?}}]
   (let [m (find-3-eq-mismatches gt_fn gt_fn eq list)]
     (println (count m)))))

(defn find-mega-eq-mismatches
  [gt_s gt_eq eq]
  (loop [xs mega-threal-list
         ys mega-threal-list
         mismatches []]
    (if (empty? ys)
      mismatches
      (if (empty? xs)
        (recur (rest ys) (rest ys) mismatches)
        (let [x (first xs)
              y (first ys)
              xy (++ x y)
              sxy (simplify gt_s xy)
              new-mismatches (cond-> mismatches
                               (not (eq gt_eq xy sxy))
                               (do (display x)
                                   (display y)
                                   (println "---")
                                   (conj [x y xy sxy])))]
          (recur (rest xs) ys new-mismatches))))))

(defn find-negation-mismatches
  [gt_s gt_eq eq cache list]
  (loop [xs list
         neg-mismatches []
         cache cache]
    (if (empty? xs)
      [neg-mismatches cache]
      (let [x (first xs)
            {z :result cache :cache} (sum-with-cache gt_s cache x (greenshift x) (blueshift x))
            new-neg-mismatches (cond-> neg-mismatches
                                 (not (eq gt_eq z zero))
                                 (conj [x z]))]
        (recur (rest xs) new-neg-mismatches cache)))))

(defn run-negation-mismatches
  ([] (run-negation-mismatches {}))
  ([{:keys [gt_s gt_eq eq list cache]
     :or {gt_s gt
          gt_eq gt
          eq rotate-eq?
          list small-list
          cache {}}}]
   (let [[m cache] (find-negation-mismatches gt_s gt_eq eq cache list)]
     (println (count m))
     (println "Cache size: " (count cache))
     (when (< (count m) 15)
       (doseq [[x z] m]
         (display x)
         (display z)
         (println "---"))))))

(defn find-cache-mismatches
  [gt_s gt_eq eq]
  (loop [xs threal-list
         ys threal-list
         mismatches []
         non-identities []]
    (if (empty? ys)
      [mismatches non-identities]
      (if (empty? xs)
        (recur threal-list (rest ys) mismatches non-identities)
        (let [x (first xs)
              y (first ys)
              xy (++ x y)
              sxy (sum-with-cache gt_s x y)
              new-mismatches (cond-> mismatches
                               (not (eq gt_eq xy sxy))
                               (conj [x y xy sxy]))
              new-non-identities (cond-> non-identities
                                   (not (= xy sxy))
                                   (conj [x y xy sxy]))]
          (recur (rest xs) ys new-mismatches new-non-identities))))))

(defn +++
  [& args]
  (display (:result (apply sum-with-cache gt {} args))))

(defn %
  [x y]
  (show-characteristics x y))
