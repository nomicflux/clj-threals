# clj-threals

A Clojure implementation of the earlier https://github.com/nomicflux/threals library. A testing ground for developing
concepts in three-player combiatorial game theory https://en.wikipedia.org/wiki/Combinatorial_game_theory.

Expect tests to be broken and functions to be oddly named and proliferating - basic concepts are still being created,
the three most important being coherent and sensible notions of partial ordering, equality, and simplification of games.
After these, the next concept to be explored is negation, if it even properly exists in general.

## Background & Problematics

### Colours & Players

Players are referred to as red, green, and blue. Combinations of red & green vs blue are "yellow", green & blue vs red
"cyan", and blue & red vs green "magenta".

### Partial Ordering Problems

Partial ordering is the most important element, as equality and simplification rely on it. As of now, system which
empirically appears to be consistent (i.e. for which any game can be replaced with its simplification in a sum, and the
result is equal to the original) can be built off of orderings which treat each game as three separate two player
games - that is, there is a partial ordering along the red-green axis, the green-blue axis, and the blue-red axis.

However, this does not appear to completely describe the landscape:
1. Games can be equal along all three of these axes, but still greater than / less than / confused along the yellow-blue
   axis, the cyan-red axis, and the magenta-green axis. That is to say, an analysis that ignores the third player gives
   different results than one which allows for the possibility of (even temporary) coalitions.
2. Under this ordering, `* + * = 0`, as in the two player case. However, playing three-player Nim would show that `* + *` is
   a second player win, while `0` is generally treated as a third player win; `* + * + *` should be the zero game instead.

A consistent ordering using the three secondary axes has not yet been found.

### Three-player Nim

Some preliminary analysis of three-player Nim (see `timber-n`) has begun. It is unclear so far as to what patterns to
search for; in normal two-player Nim, one can analyze games in terms of winning strategies for the first or second
player, but most three-player Nim games do not have a guaranteed winning strategy (in general, either the second or
third player will reach a position where they have no possible win, at which point they can often throw the game in
favour of a different player than the one under analysis as a perfectly rational move.) 

### Three- (and N-)player Misère

In addition, it is not clear what a three-player misère version would be - would two players be considering as winners
and only the player who took the last move as the loser? If so, than this would suggest an interesting interpretation of
misère vs normal play - in normal play, one is a winner if all other players lose, and in misère play, one is
a winner if there exists a losing player. Could this duality be utilised? Does it make sense to say that in a one-person
game, under normal play the player always wins, and under misère play the player always loses (if we want to extend the
game to any number of players and preserve the universal-existential duality)?

### Order of Play

It is also not yet clear whether the order of play of red-green-blue(-red-...) should be enforced in the theory or not. Given
the combinatorial nature of games, a player could always choose a different game, which would mean that the ordering in
any given component could be upset. However, it also seems possible that a situation exists where:

1. Blue has a move which gives themselves a large advantage, and some moves for Green
2. Green has a move which gives them and Blue only a small advantage
3. Red has a move which would give Green no moves, amongst others

Given ordering, Red might want to force Green out this turn, even though it would give Blue a bigger advantage. Without
ordering, Blue has a move which can restore moves to Green, so Red would not have this option; there would be no sense
of Green losing only because they go next.

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
;; ( r / r g \ b )

;; Add threals together without any simplification
(++ yellow star)
;; => [#{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]
;;   [#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{[#{} #{} #{}]}]}
;; #{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]
;;   [#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{[#{} #{} #{}]}]}
;; #{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]}]

;; Add threals together with caching and simplification
(+++ yellow star)
;; => [#{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]}
;; #{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]}
;; #{[#{[#{} #{} #{}]} #{[#{} #{} #{}]} #{}]}]

;; Add threals together with caching / simplification and display the result
(++% yellow star)
;; #b* 
;;;; A representation of the `yellow` ( 0 / 0 \ ) move for all three players
;;;; #b (coloured yellow) as it is in a sense an anti-blue (red and green can move to zero, blue has no moves), but not
;;;;    a strict negation
;;;; * to represent that the same move is repeated for all players

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
