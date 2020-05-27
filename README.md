# Breakout

This is the classic game of breaking bricks by bouncing ball with a controllable paddle (implemented by Java). You have 3 chances to eliminate all bricks in order to win. 

## How to use

Simply download the repository and you could execute the "run()" method defined in the "Breakout.java" source file with your own IDE like Visual Studio with the Java extension installed. Note that you should open the whole project folder "Breakout" to correctly set up the project dependency, as the source file imports the ACM Java Task Force for its graphics, util and program packages. Thus, if you use Eclipse, you need to add the acm.jar as the external library after import the project, which could be found from https://cs.stanford.edu/people/eroberts/jtf/index.html. 

## Improvements:

* It was 2015 summer when I first creates this game from scratch. There are 7 instance variables, accessed by several different methods of my program. Although this facilitates the decomposition and readability of the code but there are risks when using these changeable instance variables. It's almost always preferable to use parameter passing and return values to move data between different parts of the program, but I am too lazy.

* Turning the source code into a Java applet that I could put on my personal website sounds pretty cool!

* Introducing some physics laws into the game: the ball will spin and move in a curve if the player slide the paddle. This sounds like a cool physics simulation idea as well.

