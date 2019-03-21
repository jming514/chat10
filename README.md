# CPS888 Project: Chat10

## Current functions

A chat server must be first set up before clients can talk. The clients choose an IP and port # to connect to. Multiple clients can connect to a chat server and message each other.

Login window requires user to enter an username and password. 
login button: prompts the program to check the database for matching username and password and returns a message correspondingly.
registration button: opens the registration window. Registration fields are inserted into database if unique username constraint is met. 


## Information

Client.java and Server.java are the back end files.

ClientGUI2.java and ServerGUI.java are handle all GUI.

loginDB.java basic login authenication + registration GUI working

jdbc library:sqlite-jdbc-3.23.1.jar retreived from https://bitbucket.org/xerial/sqlite-jdbc/downloads/

login.db is the sqlite database file to store the login information

## Changes to make

ClientGUI2.java needs to be connected to Client.java

ServerGUI.java needs to be redesigned.

User database linked to login database (friend/block list, chat logs...)

Initialize main window after sucessful login

Improve GUI for login/registration

## Features to add

- [ ] Show connected users

- [ ] Stored chat history

- [ ] File transfer

- [ ] Account database

- [ ] 
