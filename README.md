# Project Stage 1 - J. POO Morgan Chase & Co.
## Name : Militaru Ionut-Marius 323CAb

### Project Description
* The main goal of this project was to implement a simplified banking system. 
The system simulates the basic functionalities of a bank, 
providing a user experience that is both intuitive and secure.


## Implemented Classes
* In the actionHandler package, I implemented a class called ActionHandler 
to manage the commands within the banking system. Additionally, I implemented two classes 
to print errors or commands to the output and an enum where I defined the different types 
of errors that can occur.

* In the bank package, I implemented various classes for accounts, cards, users, and the bank database. 
The Card and Account classes are abstract to allow for extension based on the type of card or account 
that will be implemented in the future.

* In the commands package, I implemented a Command interface, followed by various types of commands 
that can occur within the banking system. This design allows for new commands to be added to the banking 
system with ease, as they are independent of the other commands.

* In the transaction package, I implemented three classes: Transaction, TransactionBuilder, and Commerciant, 
along with an enum called TransactionDescription, which contains all types of description that can occur during a 
transaction. I implemented a TransactionBuilder to avoid creating separate classes for every type of 
transaction, where only the constructor would differ.

## DESIGN PATTERNS
#### In the project, I implemented three design patterns
* I implemented a Factory pattern for account creation to handle the type of account that should be implemented.
* In the transactions, I implemented a Builder pattern to handle the different types of transactions 
that can be carried out within the banking system.
* I implemented a Command pattern to manage the commands in the banking system, making it 
easier to extend the system by adding new commands.