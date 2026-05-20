# Poker
A JavaFX-based single-player poker game where you play against a robot opponent.
 
## Features
 
- Texas Hold'em style gameplay with Flop, Turn, and River rounds
- Robot opponent with basic decision-making (fold, call, raise)
- Visual card display with suit and rank images
- Chip animations and pot tracking
- Play Again and Exit options at the end of each game
- Logic for deciding winner has been implemented according to Poker-rules.pdf
## How to Play
 
1. Launch the application
2. You are dealt two hole cards face up, the robot gets two face down
3. Choose to **Fold**, **Call (20)**, or **Raise** (select amount from dropdown)
4. Community cards are revealed across three rounds — Flop, Turn, River
5. After the River, the robot's cards are revealed and the winner is decided automatically
6. Click **Play Again** to start a fresh game or **End Game** to exit

## Areas for Improvement
 
- Implement proper 5-card best-hand selection from 7 cards using combinations
- Add kicker logic for tie-breaking (e.g. both have One Pair — compare remaining cards)
- Give the robot a smarter strategy based on its hand ranking and pot odds
- Add a chip/balance system so players can go bust and track wins across rounds
- Add sound effects for card dealing, chip stacking, and win/loss
- Show hand ranking label (e.g. "Full House") after the river is revealed
- Add a betting history or round summary panel
- Animate card dealing instead of instantly displaying all cards
