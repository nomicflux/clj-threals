(ns clj-threals.display
  (:require
   [schema.core :as s]
   [clj-threals.threals :as threals]
   [clojure.string :as str]))

(s/defn colourize :- s/Str
  [string :- s/Str
   colour :- s/Keyword]
  (let [start (case colour
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

(def colour-map
  {threals/zero ["0" :black]
   threals/red ["r" :red]
   (threals/n-red 2) ["2r" :red]
   (threals/n-red 3) ["3r" :red]
   threals/red-arrow ["r^" :red]
   (threals/all-t threals/red) ["r*" :red]
   threals/green ["g" :green]
   (threals/n-green 2) ["2g" :green]
   (threals/n-green 3) ["3g" :green]
   threals/green-arrow ["g^" :green]
   (threals/all-t threals/green) ["g*" :green]
   threals/blue ["b" :blue]
   (threals/n-blue 2) ["2b" :blue]
   (threals/n-blue 3) ["3b" :blue]
   threals/blue-arrow ["b^" :blue]
   (threals/all-t threals/blue) ["b*" :blue]
   threals/yellow ["#b" :yellow]
   (threals/n-yellow 2) ["2#b" :yellow]
   (threals/n-yellow 3) ["3#b" :yellow]
   threals/yellow-arrow ["#b^" :yellow]
   (threals/all-t threals/yellow) ["#b*" :yellow]
   threals/cyan ["#r" :cyan]
   (threals/n-cyan 2) ["2#r" :cyan]
   (threals/n-cyan 3) ["3#r" :cyan]
   threals/cyan-arrow ["#r^" :cyan]
   (threals/all-t threals/cyan) ["#r*" :cyan]
   threals/magenta ["#g" :magenta]
   (threals/n-magenta 2) ["2#g" :magenta]
   (threals/n-magenta 3) ["3#g" :magenta]
   threals/magenta-arrow ["#g^" :magenta]
   (threals/all-t threals/magenta) ["#g*" :magenta]
   threals/star ["*" :white]
   (threals/n-star 2) ["2*" :white]
   (threals/n-star 3) ["3*" :white]
   threals/anti-red ["-r-" :cyan]
   threals/anti-green ["-g-" :magenta]
   threals/anti-blue ["-b-" :yellow]
   threals/all-star ["$" :white]
   threals/white ["@" :white]
   threals/half-red-green ["r/g" :red]
   threals/half-red-blue ["r/b" :red]
   threals/half-green-blue ["g/b" :green]
   threals/half-green-red ["g/r" :green]
   threals/half-blue-red ["b/r" :blue]
   threals/half-blue-green ["b/g" :blue]
   threals/red-green-blue-tree ["r/g/b" :red]
   threals/red-blue-green-tree ["r/b/g" :red]
   threals/green-blue-red-tree ["g/b/r" :green]
   threals/green-red-blue-tree ["g/r/b" :green]
   threals/blue-red-green-tree ["b/r/g" :blue]
   threals/blue-green-red-tree ["b/g/r" :blue]
   threals/half-yellow ["rg/b" :yellow]
   threals/half-magenta ["br/g" :magenta]
   threals/half-cyan ["gb/r" :cyan]
   threals/half-red-cyan ["r/gb" :red]
   threals/half-green-magenta ["g/br" :green]
   threals/half-blue-yellow ["b/rg" :blue]
   threals/anti-yellow ["-rg-" :blue]
   threals/anti-magenta ["-br-" :green]
   threals/anti-cyan ["-gb-" :red]
   threals/black ["-rgb-" :black]
   threals/red-stalemate ["!r!" :bg-red]
   threals/green-stalemate ["!g!" :bg-green]
   threals/blue-stalemate ["!b!" :bg-blue]
   threals/hot-yellow ["rg-b" :yellow]
   threals/hot-magenta ["br-g" :magenta]
   threals/hot-cyan ["gb-r" :cyan]
   (threals/timber-n 2) ["*2" :white]
   (threals/timber-n 3) ["*3" :white]})

(s/defn display* :- s/Str
  ([x]
   (display* 1 x))
  ([level :- s/Int
    [reds greens blues :as x] :- threals/Threal]
   (str
        (if-let [[s colour] (get colour-map x)]
          (colourize s colour)
          (if (= reds greens blues)
            (str " { " (str/join " " (map (partial display* level) reds)) " }")
            (str "\n"
                (str/join (repeat level "\t"))
                " ( "
                (str/join " " (map (partial display* (inc level)) reds))
                " / "
                (str/join " " (map (partial display* (inc level)) greens))
                " \\ "
                (str/join " " (map (partial display* (inc level)) blues))
                " )"
                "\n"
                (str/join (repeat (dec level) "\t"))
                )))
        )))

(s/defn display :- (s/eq nil)
  [x :- threals/Threal]
  (println (display* x)))

