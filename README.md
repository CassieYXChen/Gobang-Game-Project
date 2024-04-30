# Gobang Game Application README
Project Title: Gobang  
Group ID: F2  
Group Members:  
Yang Zhe, 1155174022  
Chen Meifang, 1155173961  
Chen Yingxin, 1155173751  
Ding Yuzhou, 1155173825  
Ye Jianqiu, 1155124313  
Department: Department of Computer Science and Engineering  
University: The Chinese University of Hong Kong  

## Introduction
Welcome to the Gobang Game Application repository, a strategic two-player board game implemented in Java that allows players to compete against each other from different computers within the same network or over the internet.  

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
2. Set the database username and password to `root`.
3. Install Navicat to connect to your database.
4. Create a database named `gobang`.
5. Import the `gobang.sql` file located in `gobang/sql`.

### Networking Setup

1. Modify the `db.properties` file in the `src` folder to match your machine's IP address and MySQL port number:

    ```
    url=jdbc:mysql://localhost:3306/gobang?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
    username=root
    password=<your_mysql_password>
    driver=com.mysql.jdbc.Driver
    ```

2. Configure ports in the `Global` class under `com.groupF2.common`:
  - Set `myPort` and `oppoPort` (e.g., 8088 for a single instance on one machine or different values like 8088 and 8089 for two instances).

### Security Adjustments

Execute the following SQL commands to enable remote MySQL access:

```sql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
FLUSH PRIVILEGES;
Final Steps
Replace the mysql-connector-java.jar in gobang/lib with the latest jar file suitable for MySQL 8.
Update your Java environment path (for macOS users):
bash
Copy code
nano ~/.bash_profile
export JAVA_HOME=<your_jdk1.8_path>
source ~/.bash_profile

## Game Instructions

1. To register for a new account, click "Register", then input username and password.  
2. To log in the game, enter username and password. If "Remember password" is ticked, next time username and password will be automatically filled. And password will be encrypted.  
3. In the main page, start time, elapsed time, and move time limit are shown above. Friend List is shown on right hand side, 19x19 gameboard is shown on left hand side, chatbox is shown on right hand side. Other buttons are shown below.  
4. Friend List shows all online players with their status(available or busy). Click on names to invite them starting a game.  
5. Write something in the chatbox and click on "Send", you could communicate with your opponent.  
6. To save a finished game board, do not click "New game", just click "save the game board".  
7. To open a previous game board, click "open the game board" and save the file with the same name and location as it was saved.  
8. To retract a move, use "Undo move". Based on the condition that opponent agrees to do so, you can  undo this move.  
9. To exit program, do not click red "x" or just shut down the program. Use "Exit" below. After a secondary confirmation, you could exit successfully.  
10. To view records, use "My record" and "Opponent's record". Then you could see total number of games, win number, defeat number, and other information.  
11. In additional, there are several buttons to help change the settings, like change or stop BGM, start slides show.  
12. To play with yourself or AI, click on "Human VS AI". There are several modes to choose. Click on "AI Game" and place a stone, you will battle with a strong AI. Click on "Random Play" ,you will battle with a random AI. "New Game" allows you to paly with yourself. "Start Slide Show" can change background image, and click on "Main Page", you can quit Human VS AI page.

## Additional Rule
  
"Forbidden Move" is a special button in "Human VS AI". Click on this button, then you will be restrict by rules according to the "Chinese Backgammon Competition Rules". Specification:  
   Three-three forbidden (black's one piece falls to form two live threes at the same time, and this piece must be a common component of the two living threes).  
   four-four forbidden (black's one piece falls to form two or more living fours at the same time).  
   long chain ban (black piece falls to form one or more long chain).  
