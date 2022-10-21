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

Partial ordering is the most important element, as equality and simplification rely on it. As of now, a system which
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

In addition, it is not clear what a three-player misère version would be - would two players be considered as winners
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

#### Possible Impact of Order of Play

Edit: I have found a case where order of play appears to matter.

To start, for a contrasting base case , `red` + `green` + `blue`, or `( 0 / \ ) + ( / 0 \ ) + ( / \ 0 )`, appears to be a third player win, or 0.
If Red goes first (without loss of generality), then the situation will be `( / 0 \ ) + ( / \ 0)` as red has only one move. After Green, we have
`( / \ 0 )`, and after Blue, nothing, If Red is followed by Blue instead of Green, the outcome is the same: both players
have a move, and Red ends up with no moves (i.e. at `0`).

But, if we have `( red / green \ )` + `( / green \ blue )` + `( red / \ blue )`, then we have two possible outcomes:
1. Outcome #1
    1. Red reduces `( red / green \ )` -> `red`, leaving `red` + `( / green \ blue )` + `( red / \ blue )`
    2. Green has only one choice, to reduce `( / green \ blue )` -> `green`, leaving `red` + `green` + `( red / \ blue )`.
    3. Blue similarly has only one choice, `( red / \ blue )` -> `blue`, leaving `red` + `green` + `blue`.
2. Outcome #2
    1. But, if Red reduces `( red / \ blue )` -> `red`, we have `( red / green \ )` + `( / green \ blue )` + `red`.
    2. Green can either reduce `( red / green \ )` -> `green` or `( / green \ blue )` -> `green`. 
        1. The first leaves `green` + `( / green \ blue )` + `red`. This becomes then `green` + `blue` after `red` after
           Blue plays, another 0 game.
        2. The second leaves `( red / green \ )` + `blue` + `red`. Blue picks their option, leaving `( red / green \ )` +
           `red`. Red can then win by picking the first option, leaving `red` + `red`.
        3. Generalizing, since the game is symmetrical, we can claim a first person win. *But*, the order of the second
           player was important - nothing in the original game distinguished `green` or `blue`, but Red removing a Blue
           option led to a Red win, while Red removing a Green option led to a Red loss and a Blue win.

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
;;;; #b (coloured yellow) as it is in a sense an anti-blue (red and green can move to zero,
;;;;    blue has no moves), but not a strict negation
;;;; * to represent that the same move is repeated for all players

;; Show the "characteristic" between two threals (their comparison along all three primary 
;; and all three secondary axes 
(% white zero)
;; #{:red} - #{:green} : ==
;; #{:green} - #{:blue} : ==
;; #{:blue} - #{:red} : ==
;; #{:green :red} - #{:blue} : <-
;; #{:green :blue} - #{:red} : <-
;; #{:red :blue} - #{:green} : <-
;;;; Example of two values which are equal along the primary axes, but not the secondary

(% black star)
;; #{:red} - #{:green} : ==
;; #{:green} - #{:blue} : ==
;; #{:blue} - #{:red} : ==
;; #{:green :red} - #{:blue} : <-
;; #{:green :blue} - #{:red} : <-
;; #{:red :blue} - #{:green} : <-
;;;; Another example of two values which are equal along the primary axes, 
;;;; but not the secondary

(% white black)
;; #{:red} - #{:green} : ||
;; #{:green} - #{:blue} : ||
;; #{:blue} - #{:red} : ||
;; #{:green :red} - #{:blue} : ==
;; #{:green :blue} - #{:red} : ==
;; #{:red :blue} - #{:green} : ==
;;;; The inverse is also possible, where values are equal along the secondary axes, 
;;;; but not the primary
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
