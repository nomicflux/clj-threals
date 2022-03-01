(ns clj-threals.operations
  (:require
   [schema.core :as s]
   [clj-threals.threals :as threals]))

(s/defn ++ :- threals/Threal
  [{x-reds :red x-greens :green x-blues :blue :as x} :- threals/Threal
   {y-reds :red y-greens :green y-blues :blue :as y} :- threals/Threal]
  {:red (into #{} (concat (map #(++ % y) x-reds)
                 (map #(++ x %) y-reds)))
   :green (into #{} (concat (map #(++ % y) x-greens)
                            (map #(++ x %) y-greens)))
   :blue (into #{} (concat (map #(++ % y) x-blues)
                           (map #(++ x %) y-blues)))})

(s/defn blueshift :- threals/Threal
  [{:keys [red green blue]} :- threals/Threal]
  {:red green
   :green blue
   :blue red})

(s/defn greenshift :- threals/Threal
  [{:keys [red green blue]} :- threals/Threal]
  {:red blue
   :green red
   :blue green})

(s/defn negation :- threals/Threal
  [x :- threals/Threal]
  (++ (greenshift x) (blueshift x)))

(s/defn gt_a :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_] (gt_a colour y x_)) (get x colour)))
         (empty? (filter (fn [y_] (gt_a c1 x y_)) (get y c1)))
         (empty? (filter (fn [y_] (gt_a c2 x y_)) (get y c2))))))

(s/defn gt_b :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (and (empty? (filter (fn [x_] (gt_b colour y x_)) (get x colour)))
       (empty? (filter (fn [y_] (gt_b colour y_ x)) (get y colour)))))

(s/defn gt_c :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
(let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_] (gt_c colour y x_)) (get x colour)))
         (empty? (filter (fn [y_] (gt_c colour y_ x)) (get y c1)))
         (empty? (filter (fn [y_] (gt_c colour y_ x)) (get y c2))))))
