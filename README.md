# clj-threals

A Clojure implementation of the earlier [https://github.com/nomicflux/threals] library. A testing ground for developing
concepts in three-player combiatorial game theory [https://en.wikipedia.org/wiki/Combinatorial_game_theory].

Expect tests to be broken and functions to be oddly named and proliferating - basic concepts are still being created,
the three most important being coherent and sensible notions of partial ordering, equality, and simplification of games.
After these, the next concept to be explored is negation, if it even properly exists in general.

## Background & Problematic

Players are referred to as red, green, and blue. Combinations of red & green vs blue are "yellow", green & blue vs red
"cyan", and blue & red vs green "magenta".

Partial ordering is the most important element, as equality and simplification rely on it. As of now, system which
empirically appears to be consistent (i.e. for which any game can be replaced with its simplification in a sum, and the
result is equal to the original) can be built off of orderings which treat each game as three separate two player
games - that is, there is a partial ordering along the red-green axis, the green-blue axis, and the blue-red axis.

However, this does not appear to completely describe the landscape:
1. Games can be equal along all three of these axes, but still greater than / less than / confused along the yellow-blue
   axis, the cyan-red axis, and the magenta-green axis. That is to say, an analysis that ignores the third player gives
   different results than one which allows for the possibility of (even temporary) coalitions.
2. Under this ordering, * + * = 0, as in the two player case. However, playing three-player Nim would show that * + * is
   a second player win, while 0 is generally treated as a third player win; * + * + * should be the zero game instead.

A consistent ordering using the three secondary axes has not yet been found.

## Usage

Threals are represented as triples of sets:

```clojure
[#{red-values} #{green-values} #{blue-values}]
```

Again, this library is in active development and does not even properly constitute an alpha anything. However, the
following commands have come up often in research:

```clojure
;; A (more) user-friendly way of viewing threals
(display [#{red} #{red green} #{blue}])
;; => ( r / r g \ b )

;; Add threals together without any simplification
(++ red blue)

;; Add threals together with caching and simplification
(+++ yellow star)

;; Add threals together with caching / simplification and display the result
(++% red-green-blue-tree (timber-n 3))

;; Show the "characteristic" between two threals (their comparison along all three primary and all three secondary axes
(% white black)
;; #{:red} - #{:green} : ||
;; #{:green} - #{:blue} : ||
;; #{:blue} - #{:red} : ||
;; #{:green :red} - #{:blue} : ==
;; #{:green :blue} - #{:red} : ==
;; #{:red :blue} - #{:green} : ==
```

## License

Copyright © 2022 Michael Anderson

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
