(ns clj-threals.operations
  (:require
   [schema.core :as s]
   [clj-threals.threals :as threals]
   [clj-threals.display :as display]
   [clojure.set :as set]))

(s/defn ++ :- threals/Threal
  [[x-reds x-greens x-blues :as x] :- threals/Threal
   [y-reds y-greens y-blues :as y] :- threals/Threal]
  [(set/union (reduce #(conj %1 (++ %2 y)) #{} x-reds)
              (reduce #(conj %1 (++ x %2)) #{} y-reds))
   (set/union (reduce #(conj %1 (++ %2 y)) #{} x-greens)
              (reduce #(conj %1 (++ x %2)) #{} y-greens))
   (set/union (reduce #(conj %1 (++ %2 y)) #{} x-blues)
              (reduce #(conj %1 (++ x %2)) #{} y-blues))])

(s/defn sum :- threals/Threal
  [& threals]
  (reduce ++ threals/zero threals))

(s/defn blueshift :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (map blueshift green))
   (into #{} (map blueshift blue))
   (into #{} (map blueshift red))])

(s/defn greenshift :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (map greenshift blue))
   (into #{} (map greenshift red))
   (into #{} (map greenshift green))])

(s/defn magentablur :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (concat (map magentablur red)
                     (map magentablur green)))
   (into #{} (concat (map magentablur green)
                     (map magentablur blue)))
   (into #{} (concat (map magentablur blue)
                     (map magentablur red)))])

(s/defn cyanblur :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (concat (map cyanblur blue)
                     (map cyanblur green)))
   (into #{} (concat (map cyanblur red)
                     (map cyanblur blue)))
   (into #{} (concat (map cyanblur green)
                     (map cyanblur red)))])

(s/defn yellowblur :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (concat (map yellowblur red)
                     (map yellowblur blue)))
   (into #{} (concat (map yellowblur green)
                     (map yellowblur red)))
   (into #{} (concat (map yellowblur blue)
                     (map yellowblur green)))])

(s/defn magentaswap :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (map magentaswap blue))
   (into #{} (map magentaswap green))
   (into #{} (map magentaswap red))])

(s/defn cyanswap :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (map cyanswap red))
   (into #{} (map cyanswap blue))
   (into #{} (map cyanswap green))])

(s/defn yellowswap :- threals/Threal
  [[red green blue] :- threals/Threal]
  [(into #{} (map yellowswap green))
   (into #{} (map yellowswap red))
   (into #{} (map yellowswap blue))])

(s/defn negation :- threals/Threal
  [x :- threals/Threal]
  (++ (greenshift x) (blueshift x)))

(s/defn getc :- #{threals/Threal}
  [[r g b] :- threals/Threal
   colour :- threals/Colour]
  (case colour
    :red r
    :green g
    :blue b))

(s/defn gt_a :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_] (gt_a c1 x_ y)) (getc x c1)))
         (empty? (filter (fn [x_] (gt_a c2 x_ y)) (getc x c2)))
         (empty? (filter (fn [y_] (gt_a colour y_ x)) (getc y colour))))))

(s/defn gt_b :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_] (gt_b colour y x_)) (getc x c1)))
         (empty? (filter (fn [x_] (gt_b colour y x_)) (getc x c2)))
         (empty? (filter (fn [y_] (gt_b colour y_ x)) (getc y colour))))))

(s/defn gt_c :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (let [[c1 c2] (threals/other-colours colour)]
    (and (empty? (filter (fn [x_] (gt_c colour y x_)) (getc x c1)))
         (empty? (filter (fn [x_] (gt_c c1 x_ y)) (getc x c1)))
         (empty? (filter (fn [x_] (gt_c c2 x_ y)) (getc x c1)))
         (empty? (filter (fn [x_] (gt_c colour y x_)) (getc x c2)))
         (empty? (filter (fn [x_] (gt_c c1 x_ y)) (getc x c2)))
         (empty? (filter (fn [x_] (gt_c c2 x_ y)) (getc x c2)))
         (empty? (filter (fn [y_] (gt_c colour y_ x)) (getc y colour)))
         (empty? (filter (fn [y_] (gt_c c1 x y_)) (getc y colour)))
         (empty? (filter (fn [y_] (gt_c c2 x y_)) (getc y colour)))
         )))

(s/defn full-eq? :- s/Bool
  [gt_fn
   x :- threals/Threal
   y :- threals/Threal]
  (and (gt_fn :red x y)
       (gt_fn :red y x)
       (gt_fn :green x y)
       (gt_fn :green y x)
       (gt_fn :blue x y)
       (gt_fn :blue y x)))

(s/defn rotate-eq? :- s/Bool
  [gt_fn
   x :- threals/Threal
   y :- threals/Threal]
  (and (gt_fn :red x y)
       (gt_fn :green x y)
       (gt_fn :blue x y)))

(s/defn dominates? :- s/Bool
  [gt_fn
   colour
   x :- threals/Threal
   y :- threals/Threal]
  (and (gt_fn colour x y)
       (not (gt_fn colour y x))))

(s/defn remove-dominated :- #{threals/Threal}
  [gt_fn
   colour :- threals/Colour
   threals :- #{threals/Threal}]
  (let [d? (memoize (partial dominates? gt_fn colour))]
      (loop [curr (first threals)
             to-check (rest threals)
             to-keep #{}]
        (if (nil? curr)
          to-keep
          (let [greater-than (filter #(d? % curr) to-check)
                not-less-than (remove #(d? curr %) to-check)]
            (if (empty? greater-than)
              (recur (first not-less-than) (rest not-less-than) (conj to-keep curr))
              (recur (first not-less-than) (rest not-less-than) to-keep)))))))

(s/defn simplify :- #{threals/Threal}
  [gt_fn
   [red green blue] :- threals/Threal]
  [(remove-dominated gt_fn :red (map (partial simplify gt_fn) red))
   (remove-dominated gt_fn :green (map (partial simplify gt_fn) green))
   (remove-dominated gt_fn :blue (map (partial simplify gt_fn) blue))])

(s/defschema ThrealCache
  {[threals/Threal threals/Threal] threals/Threal})

(s/defn add-with-cache :- {:result threals/Threal :cache ThrealCache}
  ([gt_fn x y]
   (add-with-cache gt_fn x y {}))
  ([gt_fn
    [x-reds x-greens x-blues :as x] :- threals/Threal
    [y-reds y-greens y-blues :as y] :- threals/Threal
    cache :- ThrealCache]
   (let [sum-reducer (fn [other
                          starting-cache
                          coll]
                       (reduce (fn [{:keys [results cache]} next]
                                 (let [res (add-with-cache gt_fn other next cache)]
                                   {:results (conj results (:result res))
                                    :cache (:cache res)}))
                               {:results []
                                :cache starting-cache}
                               coll))]
     (if-let [sum (or (get cache [x y]) (get cache [y x]))]
       {:result sum :cache cache}
       (let [{res_xr :results cache_xr :cache} (sum-reducer y cache x-reds)
             {res_yr :results cache_yr :cache} (sum-reducer x cache_xr y-reds)
             {res_xg :results cache_xg :cache} (sum-reducer y cache_yr x-greens)
             {res_yg :results cache_yg :cache} (sum-reducer x cache_xg y-greens)
             {res_xb :results cache_xb :cache} (sum-reducer y cache_yg x-blues)
             {res_yb :results cache_yb :cache} (sum-reducer x cache_xb y-blues)
             result (simplify gt_fn  [(into #{} (concat res_xr res_yr))
                                      (into #{} (concat res_xg res_yg))
                                      (into #{} (concat res_xb res_yb))])]
         {:result result
          :cache (assoc cache_yb [x y] result)})))))

(s/defn sum-with-cache :- {:result threals/Threal
                           :cache ThrealCache}
  [gt_fn
   cache :- ThrealCache
   & threals]
  (->> threals
       (reduce (fn [{:keys [result cache]} next]
                 (add-with-cache gt_fn result next cache))
               {:result threals/zero
                :cache cache})))

(s/defn neg
  [[red green blue] :- threals/Threal]
  [(into #{} (concat (map neg green) (map neg blue)))
   (into #{} (concat (map neg blue) (map neg red)))
   (into #{} (concat (map neg red) (map neg green)))])

(s/defn neg-simpl
  [gt_fn
   [red green blue] :- threals/Threal]
  (simplify gt_fn
            [(into #{} (concat (map (partial neg-simpl gt_fn) green)
                               (map (partial neg-simpl gt_fn) blue)))
             (into #{} (concat (map (partial neg-simpl gt_fn) blue)
                               (map (partial neg-simpl gt_fn) red)))
             (into #{} (concat (map (partial neg-simpl gt_fn) red)
                               (map (partial neg-simpl gt_fn) green)))]))

(s/defn generic-gt
  [for-colours :- #{threals/Colour}
   against-colours :- #{threals/Colour}
   x :- threals/Threal
   y :- threals/Threal]
  (let [for-fn (fn [c] (empty? (filter (fn [y_] (generic-gt for-colours against-colours y_ x)) (getc y c))))
        against-fn (fn [c] (empty? (filter (fn [x_] (generic-gt against-colours for-colours x_ y)) (getc x c))))]
    (and (every? for-fn for-colours)
         (every? against-fn against-colours))))

(defn power [s]
  (set (loop [[f & r] (seq s) p '(#{})]
         (if f (recur r (concat p (map #(conj % f) p)))
             p))))

(s/defn characteristic
  [x :- threals/Threal
   y :- threals/Threal]
  (let [all-colour-sets (power threals/colours)]
    (into {}
          (for [for-c all-colour-sets
                against-c all-colour-sets]
            [[for-c against-c] (generic-gt for-c against-c x y)]))))

(s/defn to-category
  [for-c :- #{threals/Colour}
   against-c :- #{threals/Colour}
   chars]
  (let [a-b (get chars [for-c against-c])
        b-a (get chars [against-c for-c])]
    (case [a-b b-a]
      [true true] "=="
      [true false] "<-"
      [false true] "->"
      [false false] "||")))

(s/def key-axes [[#{:red} #{:green}]
                 [#{:green} #{:blue}]
                 [#{:blue} #{:red}]
                 [#{:red :green} #{:blue}]
                 [#{:green :blue} #{:red}]
                 [#{:blue :red} #{:green}]])

(s/def opposing-axes (concat key-axes
                             [[#{} #{}]
                              [#{:red} #{}]
                              [#{:green} #{}]
                              [#{:blue} #{}]
                              [#{:red :green} #{}]
                              [#{:green :blue} #{}]
                              [#{:blue :red} #{}]
                              [#{:red :green :blue} #{}]]))

(s/def all-axes (concat opposing-axes
                        [[#{:red} #{:red}]
                         [#{:green} #{:green}]
                         [#{:blue} #{:blue}]
                         [#{:red} #{:red :green}]
                         [#{:red} #{:blue :red}]
                         [#{:green} #{:blue :green}]
                         [#{:green} #{:green :red}]
                         [#{:blue} #{:blue :red}]
                         [#{:blue} #{:green :blue}]
                         [#{:red} #{:red :green :blue}]
                         [#{:green} #{:red :green :blue}]
                         [#{:blue} #{:red :green :blue}]
                         [#{:red :green} #{:red :green :blue}]
                         [#{:green :blue} #{:red :green :blue}]
                         [#{:blue :red} #{:red :green :blue}]
                         [#{:red :green :blue} #{:red :green :blue}]]))

(s/defn show-characteristics
  ([x y]
   (show-characteristics x y key-axes))
  ([x :- threals/Threal
    y :- threals/Threal
    axes]
   (let [chars (characteristic x y)]
     (doseq [[axis-a axis-b] axes]
       (println axis-a "-" axis-b ":" (to-category axis-a axis-b chars))))))

(s/def gt-r-g (memoize (partial generic-gt #{:red} #{:green})))
(s/def gt-g-b (memoize (partial generic-gt #{:green} #{:blue})))
(s/def gt-b-r (memoize (partial generic-gt #{:blue} #{:red})))
(s/defn gt_d :- s/Bool
  [colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (case colour
    :green (and (gt-g-b x y) (gt-r-g y x))
    :red (and (gt-r-g x y) (gt-b-r y x))
    :blue (and (gt-b-r x y) (gt-g-b y x))))

(s/def gt-cyan (memoize (partial generic-gt #{:green :blue} #{:red})))
(s/def gt-magenta (memoize (partial generic-gt #{:blue :red} #{:green})))
(s/def gt-yellow (memoize (partial generic-gt #{:red :green} #{:blue})))
(s/defn gt_e :- s/Bool
  [_colour :- threals/Colour
   x :- threals/Threal
   y :- threals/Threal]
  (and (gt-yellow x y)
       (gt-magenta x y)
       (gt-cyan x y)))

(s/defn for-birthday
  [gt_fn
   visitor_fn
   n :- s/Int]
  (loop [prev #{}
         day 0
         seen #{}
         acc {}]
    (let [prev-power (into [] (power prev))
          [today new-seen]
          (loop [r-list prev-power
                 g-list prev-power
                 b-list prev-power
                 seen seen
                 acc prev]
            (cond
              (empty? b-list) [acc seen]
              (empty? g-list) (recur (rest b-list) (rest b-list) (rest b-list) seen acc)
              (empty? r-list) (recur (rest g-list) (rest g-list) b-list seen acc)
              :else (let [r (first r-list)
                          g (first g-list)
                          b (first b-list)
                          triple [r g b]]
                      (if (contains? seen triple)
                        (recur (rest r-list) g-list b-list seen acc)
                        (let [t1 (simplify gt_fn [r g b])
                              t2 (simplify gt_fn [g b r])
                              t3 (simplify gt_fn [b r g])
                              t4 (simplify gt_fn [r b g])
                              t5 (simplify gt_fn [b g r])
                              t6 (simplify gt_fn [g r b])
                              ts (into #{} [t1 t2 t3 t4 t5 t6])
                              new (set/difference ts acc)]
                          (when visitor_fn
                            (when (seq new)
                              (visitor_fn day new)))
                          (recur (rest r-list) g-list b-list
                                 (set/union seen (into #{} [[r g b]
                                                   [g b r]
                                                   [b r g]
                                                   [r b g]
                                                   [b g r]
                                                   [g r b]]))
                                 (set/union acc new)))))))]
      (if (= day n)
        acc
        (recur today (inc day) new-seen (assoc acc day today))))))

(defn show-bd [n]
  (let [gt (memoize gt_d)]
    (for-birthday gt
                  (fn [day ts] (println day ": ") (doseq [t ts] (display/display t)))
                  n))
  nil)
