(ns clj-threals.threals
  (:require
   [schema.core :as s]))

(s/defschema Threal
  {:red #{Threal}
   :green #{Threal}
   :blue #{Threal}})

(s/def colours #{:red :green :blue})

(s/defschema Colour
  (s/enum colours))

(s/defschema NatInt
  (s/constrained s/Int #(>= % 0)))

(s/defn other-colours
  [colour :- Colour]
  (remove #(= % colour) colours))

(s/def zero
  {:red #{}
   :green #{}
   :blue #{}})

(s/def red
  {:red #{zero}
   :green #{}
   :blue #{}})

(s/defn n-red
  [n :- NatInt]
  (case n
    0 zero
    1 red
    {:red #{(n-red (dec n))}
     :green #{}
     :blue #{}}))

(s/def green
  {:red #{}
   :green #{zero}
   :blue #{}})

(s/defn n-green
  [n :- NatInt]
  (case n
    0 zero
    1 green
    {:red #{}
     :green #{(n-green (dec n))}
     :blue #{}}))

(s/def blue
  {:red #{}
   :green #{}
   :blue #{zero}})

(s/defn n-blue
  [n :- NatInt]
  (case n
    0 zero
    1 blue
    {:red #{}
     :green #{}
     :blue #{(n-blue (dec n))}}))

(s/def cyan
  {:red #{}
   :green #{zero}
   :blue #{zero}})

(s/defn n-cyan
  [n :- NatInt]
  (case n
    0 zero
    1 cyan
    {:red #{}
     :green #{(n-cyan (dec n))}
     :blue #{(n-cyan (dec n))}}))

(s/def magenta
  {:red #{zero}
   :green #{}
   :blue #{zero}})

(s/defn n-magenta
  [n :- NatInt]
  (case n
    0 zero
    1 magenta
    {:red #{(n-magenta (dec n))}
     :green #{}
     :blue #{(n-magenta (dec n))}}))

(s/def yellow
  {:red #{zero}
   :green #{zero}
   :blue #{}})

(s/defn n-yellow
  [n :- NatInt]
  (case n
    0 zero
    1 yellow
    {:red #{(n-yellow (dec n))}
     :green #{(n-yellow (dec n))}
     :blue #{}}))

(s/def star
  {:red #{zero}
   :green #{zero}
   :blue #{zero}})

(s/defn n-star
  [n :- NatInt]
  (case n
    0 zero
    1 star
    {:red #{(n-star (dec n))}
     :green #{(n-star (dec n))}
     :blue #{(n-star (dec n))}}))

(s/def anti-red
  {:red #{}
   :green #{blue}
   :blue #{green}})

(s/def anti-green
  {:red #{blue}
   :green #{}
   :blue #{red}})

(s/def anti-blue
  {:red #{green}
   :green #{red}
   :blue #{}})

(s/def all-star
  {:red #{red}
   :green #{green}
   :blue #{blue}})

(s/def anti-star
  {:red #{anti-red}
   :green #{anti-green}
   :blue #{anti-blue}})

(s/def half-red-blue
  {:red #{zero}
   :green #{}
   :blue #{red}})

(s/def half-red-green
  {:red #{zero}
   :green #{red}
   :blue #{}})

(s/def half-green-blue
  {:red #{}
   :green #{zero}
   :blue #{green}})

(s/def half-green-red
  {:red #{green}
   :green #{zero}
   :blue #{}})

(s/def half-blue-red
  {:red #{blue}
   :green #{}
   :blue #{zero}})

(s/def half-blue-green
  {:red #{}
   :green #{green}
   :blue #{zero}})

(s/def red-green-blue-tree
  {:red #{zero}
   :green #{red}
   :blue #{half-red-green}})

(s/def red-blue-green-tree
  {:red #{zero}
   :green #{half-red-blue}
   :blue #{red}})

(s/def green-blue-red-tree
  {:red #{half-green-blue}
   :green #{zero}
   :blue #{green}})

(s/def green-red-blue-tree
  {:red #{green}
   :green #{zero}
   :blue #{half-green-red}})

(s/def blue-red-green-tree
  {:red #{blue}
   :green #{half-blue-red}
   :blue #{zero}})

(s/def blue-green-red-tree
  {:red #{half-blue-green}
   :green #{blue}
   :blue #{zero}})
