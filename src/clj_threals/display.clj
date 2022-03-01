(ns clj-threals.display
  (:require
   [schema.core :as s]
   [clj-threals.threals :as threals]
   [clojure.string :as str]))

(s/defn colorize :- s/Str
  [string :- s/Str
   color :- s/Keyword]
  (let [start (case color
                :red "[31m"
                :green "[32m"
                :blue "[34m"
                :yellow "[33m"
                :magenta "[35m"
                :cyan "[36m"
                :white "[37m"
                :black "[30m"
                :bg-red "[41m"
                :bg-green "[42m"
                :bg-blue "[44m")]
    (str \u001b start string \u001b "[0m")))

(def color-map
  {threals/zero ["0" :black]
   threals/red ["r" :red]
   threals/green ["g" :green]
   threals/blue ["b" :blue]
   threals/yellow ["^b^" :yellow]
   threals/cyan ["^r^" :cyan]
   threals/magenta ["^g^" :magenta]
   threals/star ["*" :white]
   threals/anti-red ["-r-" :bg-red]
   threals/anti-green ["-g-" :bg-green]
   threals/anti-blue ["-b-" :bg-blue]
   threals/all-star ["$" :white]
   threals/anti-star ["@" :white]
   threals/half-red-green ["r/g" :red]
   threals/half-red-blue ["r/b" :red]
   threals/half-green-blue ["g/b" :green]
   threals/half-green-red ["g/r" :green]
   threals/half-blue-red ["b/r" :blue]
   threals/half-blue-green ["b/g" :blue]
   threals/red-green-blue-tree ["rgb" :red]
   threals/red-blue-green-tree ["rbg" :red]
   threals/green-blue-red-tree ["gbr" :green]
   threals/green-red-blue-tree ["grb" :green]
   threals/blue-red-green-tree ["brg" :blue]
   threals/blue-green-red-tree ["bgr" :blue]})

(s/defn display* :- s/Str
  ([x]
   (display* 1 x))
  ([level :- s/Int
    {reds :red greens :green blues :blue :as x} :- threals/Threal]
   (str
        (if-let [[s colour] (get color-map x)]
          (colorize s colour)
          (str "\n"
               (str/join (repeat level "\t"))
               "( "
               (str/join " " (map (partial display* (inc level)) reds))
               " / "
               (str/join " " (map (partial display* (inc level)) greens))
               " \\ "
               (str/join " " (map (partial display* (inc level)) blues))
               " )"
               "\n"
               (str/join (repeat level "\t"))
               ))
        )))

(s/defn display :- (s/eq nil)
  [x :- threals/Threal]
  (println (display* x)))
