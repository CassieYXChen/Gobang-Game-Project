# Gobang Game Application README
Project Title: Gobang  
Group ID: F2  
Group Members:  
Yang Zhe, 1155174022  
Chen Meifang, 1155173961  
Chen Yingxin, 1155173751  
Ding Yuzhou, 1155173825  
Department: Department of Computer Science and Engineering  
University: The Chinese University of Hong Kong  

## Introduction
Gobang, also known as Five in a Row, is a classic strategy board game. This project aims to provide a comprehensive Gobang game application, developed as part of the CSCI3100 course requirements. The application facilitates real-time gameplay between two players either over a local network or via the internet, leveraging JavaFX for the graphical user interface, and utilizing sockets for network communications, JDBC for database interactions, and MySQL for data management.

## Key Features and Implementation
1. User Registration and Login: The application supports user registration and login functionality, essential for personalized gaming experiences. This is achieved through JDBC, which facilitates robust interaction with a MySQL database, thereby ensuring secure storage and retrieval of user credentials.
2. Opponent Selection and Game Initiation: Players can select their opponents through a user-friendly interface. Real-time gameplay is enabled via socket connections, ensuring seamless and synchronous game interactions.
3. Graphical User Interface: The GUI, constructed using JavaFX, features an intuitive layout that includes a dynamic chessboard and interactive buttons for actions such as initiating a new game or exiting the current session.
4. Move Suggestions and Time Management: To enhance the competitive aspect of the game, visual cues are provided for possible moves. The application also tracks and displays time constraints for each move (step time) and total game duration (session time).
5. Game Records and Undo Functionality: The platform records detailed game statistics in the database, allowing players to review past outcomes. An undo feature is available, enhancing strategic depth and providing players with the opportunity to rectify mistakes.
6. Chat and Interaction: An integrated chat system allows players to communicate during the game, adding a social element to the platform.
7. Concession, Save/Load Game: Players can concede games, save ongoing games for future replay, or load previously saved games, adding flexibility to the gaming experience.
8. Audio and Visual Effects: The application includes sound effects for moves and a background music playlist, along with a rotating background image feature, creating an immersive gaming environment.
9. AI and Random Player Modes: The application includes two modes for playing against the computer—AI Player and Random Player—catering to different skill levels and providing usability for single-player scenarios.
10. Professional Rules Implementation: Advanced Gobang rules, such as restrictions on certain moves (forbidden moves), are implemented to align the game closer to competitive standards.

## Development Tools

To set up and run the application, you will need the following tools:

- **IntelliJ IDEA**: Our preferred IDE for Java development. [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download)
- **Navicat for MySQL**: A powerful database administration tool. [Download Navicat](https://navicat.com/en/download/navicat-for-mysql)

## Development Environment

Ensure your system is equipped with:

- **JDK 1.8 by Oracle** (includes JavaFX which is essential for our application): [Download JDK 1.8](https://www.oracle.com/java/technologies/downloads/#java8-mac)
- **MySQL 8.0**: [Download MySQL 8.0](https://downloads.mysql.com/archives/installer/)

## Setup Steps

### Windows Environment Deployment

#### IntelliJ IDEA

Follow this detailed [IDEA tutorial](https://github.com/judasn/IntelliJ-IDEA-Tutorial) for installation and setup instructions. Afterwards, download the project and open it directly in IntelliJ IDEA.

#### MySQL

1. Download and install MySQL version 8.0.
2. Set the database username and password.
3. Install Navicat to connect to your database.
4. Create a database named `gobang`.
5. Import the `gobang.sql` file located in `gobang/sql`.

### Setup

1. Modify the database configuration
First, you need to modify the db.properties file in the src directory to reflect your host IP address and MySQL port number. This involves changing the connection string (URL) of the database. Assuming your native IP address is 192.168.1.100 and your MySQL port number is 3306 (the default port), you should modify the file like this:

    ```
    url=jdbc:mysql://192.168.1.100:3306/gobang?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
    username=root
    password=mengmin521
    driver=com.mysql.cj.jdbc.Driver
   ```
2. Configure ports in the `Information` class under `gobang.src.GroupF2.gobang.base`:
    - Set `myPort` and `oppoPort` (e.g., 6666 for a single instance on one machine or different values like 6666 and 6667 for two instances).
    - First example: myPort = 6666, oppoPort = 6667
    - Second example: myPort = 6667, oppoPort = 6666
3. MySQL Remote Access
If the game is to be played against different computers that are not on the same LAN, you need to make sure that the MySQL database allows remote access. The following SQL statements can be executed to enable remote access:
   ```
    GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
    FLUSH PRIVILEGES;
   ```
   This assumes that you are using both the user name and password root. Note: Replace the user name and password as required.
4. Firewall and port access
Make sure your firewall Settings do not block access to desired ports (such as 6666 and 6667) or require remote permissions to be turned on for these ports in your firewall.
5. LAN restriction:
Because the ip address used for the Internet is basically the LAN ip address provided by the router or the operator, this ip address cannot be directly accessed from the Internet, that is, the battle between different computers can only be in the same LAN
6. Launch the game
Finally, run the main method located in ‘gobang.src.GroupF2.gobang.main.GobangMain’ to start the game.

### Security Adjustments

Execute the following SQL commands to enable remote MySQL access:
```
sql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
FLUSH PRIVILEGES;
Final Steps
Replace the mysql-connector-java.jar in gobang/lib with the latest jar file suitable for MySQL 8.  
```
Update your Java environment path (for macOS users):  
```
bash
Copy code
nano ~/.bash_profile
export JAVA_HOME=<your_jdk1.8_path>
source ~/.bash_profile
```
## Game Instructions
### Register & Log in
1. To register for a new account, click "Register", then input username and password.  
2. To log in the game, enter username and password. If "Remember password" is ticked, next time username and password will be automatically filled. And password will be encrypted.
### Main Page & Functions
1. In the main page, start time, elapsed time, and move time limit are shown above. Friend List is shown on the right-hand side, 19x19 gameboard is shown on the left-hand side, and chatbox is shown on the right-hand side. Other buttons are shown below.  
2. Friend List shows all online players with their status(available or busy). Click on names to invite them starting a game.  
3. Write something in the chatbox and click on "Send", you could communicate with your opponent.  
4. To save a finished game board, do not click "New game", just click "save the game board".  
5. To open a previous game board, click "open the game board" and save the file with the same name and location as it was saved.  
6. To retract a move, use "Undo move". Based on the condition that opponent agrees to do so, you can  undo this move.  
7. To exit program, do not click red "x" or just shut down the program. Use "Exit" below. After a secondary confirmation, you could exit successfully.  
8. To view records, use "My record" and "Opponent's record". Then you could see total number of games, win number, defeat number, and other information.  
9. In additional, there are several buttons to help change the settings, like change or stop BGM, start slides show.  
10. To play with yourself or AI, click on "Human VS AI". There are several modes to choose. Click on "AI Game" and place a stone, you will battle with a strong AI. Click on "Random Play" ,you will battle with a random AI. "New Game" allows you to paly with yourself. "Start Slide Show" can change background image, and click on "Main Page", you can quit Human VS AI page.

## Additional Rule
  
"Forbidden Move" is a special button in "Human VS AI". Click on this button, then you will be restrict by rules according to the "Chinese Backgammon Competition Rules".  
Specification:  
1. Three-three forbidden (black's one piece falls to form two live threes at the same time, and this piece must be a common component of the two living threes).  
2. Four-four forbidden (black's one piece falls to form two or more living fours at the same time).  
3. Long chain ban (black piece falls to form one or more long chain).  
