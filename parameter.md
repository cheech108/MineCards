# Parameters
This file contains all the parameters that a pack could have, if it has a star in front of it, it's required. This is case-sensitive.

## Main Pack
- *DisplayName: How the pack is displayed.
- CardsGiven: How many cards the pack gives, defaults to 3.
- *rarities: A sub dictionary written as with as many rarities as needed below.

## Rarities
This is where the cards go, a pack can hold infinitely many of these. It's really hard to make this make sense so check out some of the example packs.
- *color: The color this will show up in game as.
- *weight: The weight of the rarity, think of the weight as tickets to a raffle, all the tickets get added together and one is pulled. (EX common: 3, rare: 1, there is a 3/4 chance common is picked and a 1/4 chance rare is picked.).
- *drops: A string list of every card in this rarity. When this rarity gets selected one of these will be picked at random.