(ns clj-threals.threals
  (:require
   [schema.core :as s]
   [clojure.set :as set]))

(s/defschema Threal
  [#{Threal}
   #{Threal}
   #{Threal}])

(s/def colours #{:red :green :blue})

(s/defschema Colour
  (s/enum colours))

(s/defschema NatInt
  (s/constrained s/Int #(>= % 0)))

(s/defn other-colours
  [colour :- Colour]
  (remove #(= % colour) colours))

(s/def zero
  [#{}
   #{}
   #{}])

(s/def red
  [#{zero}
   #{}
   #{}])

(s/defn n-red
  [n :- NatInt]
  (case n
    0 zero
    1 red
    [#{(n-red (dec n))}
     #{}
     #{}]))

(s/def green
  [#{}
   #{zero}
   #{}])

(s/defn n-green
  [n :- NatInt]
  (case n
    0 zero
    1 green
    [#{}
     #{(n-green (dec n))}
     #{}]))

(s/def blue
  [#{}
   #{}
   #{zero}])

(s/defn n-blue
  [n :- NatInt]
  (case n
    0 zero
    1 blue
    [#{}
     #{}
     #{(n-blue (dec n))}]))

(s/def cyan
  [#{}
   #{zero}
   #{zero}])

(s/defn n-cyan
  [n :- NatInt]
  (case n
    0 zero
    1 cyan
    [#{}
     #{(n-cyan (dec n))}
     #{(n-cyan (dec n))}]))

(s/def magenta
  [#{zero}
   #{}
   #{zero}])

(s/defn n-magenta
  [n :- NatInt]
  (case n
    0 zero
    1 magenta
    [#{(n-magenta (dec n))}
     #{}
     #{(n-magenta (dec n))}]))

(s/def yellow
  [#{zero}
   #{zero}
   #{}])

(s/defn n-yellow
  [n :- NatInt]
  (case n
    0 zero
    1 yellow
    [#{(n-yellow (dec n))}
     #{(n-yellow (dec n))}
     #{}]))

(s/def star
  [#{zero}
   #{zero}
   #{zero}])

(s/defn n-star
  [n :- NatInt]
  (case n
    0 zero
    1 star
    [#{(n-star (dec n))}
     #{(n-star (dec n))}
     #{(n-star (dec n))}]))

(s/def anti-red
  [#{}
   #{blue}
   #{green}])

(s/def anti-green
  [#{blue}
   #{}
   #{red}])

(s/def anti-blue
  [#{green}
   #{red}
   #{}])

(s/def all-star
  [#{red}
   #{green}
   #{blue}])

(s/def white
  [#{anti-red}
   #{anti-green}
   #{anti-blue}])

(s/def half-red-blue
  [#{zero}
   #{}
   #{red}])

(s/def half-red-green
  [#{zero}
   #{red}
   #{}])

(s/def half-green-blue
  [#{}
   #{zero}
   #{green}])

(s/def half-green-red
  [#{green}
   #{zero}
   #{}])

(s/def half-blue-red
  [#{blue}
   #{}
   #{zero}])

(s/def half-blue-green
  [#{}
   #{green}
   #{zero}])

(s/def red-green-blue-tree
  [#{zero}
   #{red}
   #{half-red-green}])

(s/def red-blue-green-tree
  [#{zero}
   #{half-red-blue}
   #{red}])

(s/def green-blue-red-tree
  [#{half-green-blue}
   #{zero}
   #{green}])

(s/def green-red-blue-tree
  [#{green}
   #{zero}
   #{half-green-red}])

(s/def blue-red-green-tree
  [#{blue}
   #{half-blue-red}
   #{zero}])

(s/def blue-green-red-tree
  [#{half-blue-green}
   #{blue}
   #{zero}])

(s/defn all-t
  [threal :- Threal]
  [(into #{} [threal])
   (into #{} [threal])
   (into #{} [threal])])

(s/def red-arrow
  [#{star}
   #{zero}
   #{zero}])

(s/def green-arrow
  [#{zero}
   #{star}
   #{zero}])

(s/def blue-arrow
  [#{zero}
   #{zero}
   #{star}])

(s/def yellow-arrow
  [#{star}
   #{star}
   #{zero}])

(s/def cyan-arrow
  [#{zero}
   #{star}
   #{star}])

(s/def magenta-arrow
  [#{star}
   #{zero}
   #{star}])

(s/def half-yellow
  [#{zero}
   #{zero}
   #{yellow}])

(s/def half-magenta
  [#{zero}
   #{magenta}
   #{zero}])

(s/def half-cyan
  [#{cyan}
   #{zero}
   #{zero}])

(s/def half-red-cyan
  [#{zero}
   #{red}
   #{red}])

(s/def half-green-magenta
  [#{green}
   #{zero}
   #{green}])

(s/def half-blue-yellow
  [#{blue}
   #{blue}
   #{zero}])

(s/def anti-magenta
  [#{cyan}
   #{cyan yellow}
   #{yellow}])

(s/def anti-yellow
  [#{cyan}
   #{magenta}
   #{cyan magenta}])

(s/def anti-cyan
  [#{yellow magenta}
   #{magenta}
   #{yellow}])

(s/def black
  [#{anti-magenta
     anti-yellow}
   #{anti-cyan
     anti-yellow}
   #{anti-magenta
     anti-cyan}])

(s/def red-stalemate
  [#{cyan}
   #{red}
   #{red}])

(s/def green-stalemate
  [#{green}
   #{magenta}
   #{green}])

(s/def blue-stalemate
  [#{blue}
   #{blue}
   #{yellow}])

(s/def hot-yellow
  [#{yellow}
   #{yellow}
   #{blue}])

(s/def hot-magenta
  [#{magenta}
   #{green}
   #{magenta}])

(s/def hot-cyan
  [#{red}
   #{cyan}
   #{cyan}])

(s/defn timber-n
  [n :- s/Int]
  (case n
    0 zero
    1 star
    (let [timbers (into #{} (map timber-n (range 0 n)))]
      [timbers timbers timbers])))
