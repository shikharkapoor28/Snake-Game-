package SnakeGame;

import bridges.base.NamedColor;
import bridges.base.NamedSymbol;
import bridges.games.NonBlockingGame;

class Snake extends NonBlockingGame {
	// Game settings
	java.util.Random random = new java.util.Random();

	// Set dimensions of the game board
	static int gridColumns = 30;
	static int gridRows = 30;

	// Refresh rate
	final long FRAMERATE = 1000000000 / 15;
	long frameTime;
	long nextFrameTime;

	// Initial snake settings
	int startX = gridColumns / 3;
	int startY = gridRows / 2;
	int startLength = 3;

	// Similar to a chessboard, the background will alternate in color
	NamedColor bc1 = NamedColor.forestgreen; // board color
	NamedColor bc2 = NamedColor.green; // board color

	// Setting up other game colors
	NamedColor sc = NamedColor.silver; // snake color
	NamedColor hc = NamedColor.white; // head color
	NamedColor ac = NamedColor.red; // apple color

	// TODO: Create an element to represent the snake's head (call it head)
	// and tail (call it tail) - is there a class that could help us where
	// the location of the head and tail could be encapsulated?
	Block head;
	Block tail;
	// TODO: Create an element to represent the apple
	Block apple;

	// TODO: Keep track of the snake's current and last direction
	Direction dir;
	Direction lastDir;

	// Constructor - set bridges credentials, grid size
	public Snake(int assid, String login, String apiKey, int c, int r) {
		super(assid, login, apiKey, c, r);
	}

	// Handle user input
	public void handleInput() {
		// Based on which of the 4 arrow keys is pressed
		// move the snake in the requested particular direction
		// Note: if the snake is moving North, it cannot move South
		// meaning that it cannot turn on itself

		// handling input for left key : change direction to WEST if not currently facing to east and last direction east
		if (keyLeftStillPressed() && dir != Direction.EAST && lastDir != Direction.EAST) {
			dir = Direction.WEST;
		} 
		// handling input for left right :change direction to EAST if not currently facing to west and last direction west
		else if (keyRightStillPressed() && dir != Direction.WEST && lastDir != Direction.WEST) {
			dir = Direction.EAST;
		}
		// handling input for left up :change direction to NORTH if not currently facing to south and last direction south
		else if (keyUpStillPressed() && dir != Direction.SOUTH && lastDir != Direction.SOUTH) {
			dir = Direction.NORTH;
		} 
		// handling input for left down :change direction to SOUTH if not currently facing to north and last direction north
		else if (keyDownStillPressed() && dir != Direction.NORTH && lastDir != Direction.NORTH) {
			dir = Direction.SOUTH;
		}

		// TODO: Construct an if statement that tests which key is pressed and
		// if the current and last directions do not equal the opposite direction
		// then set the current direction to the direction requested
	}

	// Update snake position
	public void updatePosition() {
		// Move the snake one position, based on its direction and update
		// the linked list
		Block current = head.next; // assumes you created a Block called head at the top of your class
		int nextX = head.x;
		int nextY = head.y;
		Block nextPos = head;

		// Loops through the whole snake to move all of the blocks one space
		while (current != null) {
			int tempX = current.x;
			int tempY = current.y;
			current.x = nextX;
			current.y = nextY;
			nextX = tempX;
			nextY = tempY;
			current = current.next;
		}

		// TODO: handle edge cases - check to make sure the snake
		// doesnt go off the edge of the board; can do a wrap around
		// in X or Y to handle it. Must handle all 4 directions the snake
		// might be traveling..

		if (dir == Direction.EAST) {
			head.x++;
			if (head.x >= gridColumns) 
				head.x = 0; // wrap around to left if head.x value is more than or equal to gridColumns(value:30)
		} else if (dir == Direction.WEST) {
			head.x--;
			if (head.x < 0)
				head.x = gridColumns - 1; // wrap around to right if head.x value is less than 0
		} else if (dir == Direction.SOUTH) {
			head.y++;
			if (head.y >= gridRows)
				head.y = 0; // Wrap around to top if head.y value is greater than equal to gridRows(value:30)
		} else if (dir == Direction.NORTH) {
			head.y--;
			if (head.y < 0)
				head.y = gridRows - 1; // Wrap around to bottom if head.y value is less than 0
		}
	}

	// Create a new apple (position)
	public void plantApple() {
		// Randomly position the apple, taking care to ensure that it doesn't
		// intersect with the snake position.

		// TODO: Create an x and y (ints). You will want to get a random
		// number between 0 and 29 for x and for y. You will want to keep
		// getting random numbers for x and y WHILE there is a collision
		// between the x or y and a block on the snake. You'll have to loop
		// through the snake much like in updatePosition() verifying whether
		// or not the x/y of a block is the same as the random x/y.
		// Once you find a valid x/y, you can set the apple's x/y to x/y.

		int x = 0, y = 0;

		Boolean collision = true;
		do {

			x = random.nextInt(29);
			y = random.nextInt(29);

			collision = false;
			Block current = head;
			while (current != null) {
				if (current.x == x && current.y == y) {
					collision = true;
					break;
				}
				current = current.next;
			}
		}
		while (collision);
		{
			apple.x = x;
			apple.y = y;
		}

	}

	// Check if snake has found the apple
	public void detectApple() {
		// If apple is found, snake consumes it and update the board and plant
		// a new apple on the board.

		// TODO: If the head's x/y equals the apple's x/y, add a new tail
		// and plant an apple

		if (head.x == apple.x && head.y == apple.y) {
			Block newTail = new Block(tail.x, tail.y);
			drawSymbol(apple.y, apple.x, NamedSymbol.none, ac);
			tail.next = newTail;
			tail = newTail;
			
			drawSymbol(tail.y, tail.x, NamedSymbol.none, sc);
			
			plantApple();
		}

	}

	// Check if snake ate itself! Yuk!
	public void detectDeath() {
		// TODO: Loop through the snake's body and determine if the head's x/y
		// equals any of the current's x/y throughout the loop. If it does,
		// Sytem.exit(0)
		Block current = head.next;

		while (current != null) {
			if (head.x == current.x && head.y == current.y) 
			{
				System.exit(0);
			}
			current = current.next;
		}
	}

	// Redraw
	public void paint() {
		// TODO: Draw the board, the apple and the snake
		// make sure to choose colors so that snake and apple are clearly visible.

		for (int i = 0; i < gridRows; ++i) 
		{
			for (int j = 0; j < gridColumns; ++j) 
			{
				setBGColor(i, j, (i + j) % 2 == 0 ? bc1 : bc2); 
			}
		}
		setBGColor(head.y, head.x, hc); // coloring snakes head white
		Block current = head.next;
		while (current != null) {
			setBGColor(current.y, current.x, sc); // coloring snakes body silver
			current = current.next;
		}
		drawSymbol(apple.y, apple.x, NamedSymbol.apple, ac); // making symbol apple

	}

	// Set up the first state of the game grid
	public void initialize() {
		// TODO: Create the snake of some number of elements,
		// perform all initializations, place the apple
		int startX = 5;
		int startY = 5;

		// TODO: Draw the background of the board exactly as was done in repaint()
		for (int i = 0; i < gridColumns; ++i) {
			for (int j = 0; j < gridRows; ++j) {
				if (i % 2 == j % 2)
					setBGColor(j, i, bc1);
				else
					setBGColor(j, i, bc2);
			}
		}

		// TODO: Set head to a new block passing it the start x/y
		head = new Block(startX, startY);

		// loops through the snake based on the start length and colors
		// the board accordingly
		Block current = head;

		for (int i = 0; i < startLength; ++i) {
			setBGColor(startY, startX - i, sc);
			if (i > 0) {
				current.next = new Block(startX - i, startY);
				current = current.next;
			}
		}
		tail = current;

		frameTime = System.nanoTime();
		nextFrameTime = frameTime + FRAMERATE + 10000;
		dir = Direction.EAST;
		lastDir = dir;
		apple = new Block();

		// TODO: plant an apple
		
		plantApple();
	}

	// Game loop will run many times per second.
	// handle input, check if apple was detected, update position, redraw,
	// detect if snake ate itself
	public void gameLoop() {
		// TODO: handle the input
		handleInput();

		if (System.nanoTime() > nextFrameTime) {
			frameTime = System.nanoTime();
			nextFrameTime = frameTime + FRAMERATE;

			// TODO: set the last direction equal to direction
			lastDir = dir;
			// TODO: detect an apple
			detectApple();
			// TODO: update the position
			updatePosition();
			// TODO: paint the screen
			paint();
			// TODO: detect death
			detectDeath();
		}
	}

	public static void main(String args[]) {
		Snake game = new Snake(3, "shikhar_kapoor_", "869815336302", gridColumns, gridRows);
		game.setTitle("Snake");

		game.start();
	}
}

enum Direction {
	NORTH, SOUTH, EAST, WEST
}

// helper class to hold snake and apple objects; snake grows as it
// eats and hence a linked list of blocks
class Block {
	public Block next;
	public int x;
	public int y;

	public Block() {
		this(-1, -1, null);
	}

	public Block(int x, int y) {
		this(x, y, null);
	}

	public Block(int x, int y, Block next) {
		this.x = x;
		this.y = y;
		this.next = next;
	}
}
