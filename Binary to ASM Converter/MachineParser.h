//Omair Alam oa6ci
#ifndef __MACHINEPARSER_H__
#define __MACHINEPARSER_H__

//using namespace std;

#include <iostream>
#include <fstream>
#include "Instruction.h"
#include "RegisterTable.h"
#include "Opcode.h"
#include <vector>
#include <sstream>
#include <stdlib.h>
#include <string>
#include <iomanip>

/* This class reads in a MIPS assembly file and checks its syntax.  If
 * the file is syntactically correct, this class will retain a list 
 * of Instructions (one for each instruction from the file).  This
 * list of Instructions can be iterated through.
 */
   

class MachineParser{
 public:
  // Specify a text file containing MIPS assembly instructions. Function
  // checks syntactic correctness of file and creates a list of Instructions.
  MachineParser(string filename);

  // Returns true if the file specified was syntactically correct.  Otherwise,
  // returns false.
  bool isFormatCorrect() { return myFormatCorrect; };
  bool isThirtyTwoBit() { return thirtyTwoBit;} ;
  int numberOfInstructions() { return noOfI; }; 

  // Iterator that returns the next Instruction in the list of Instructions.
  Instruction getNextInstruction();

 private:
  vector<Instruction> myInstructions;      // list of Instructions
  int myIndex;                             // iterator index
  bool myFormatCorrect;                    //checking whether 32 bit instructions are correct format or not 
  bool thirtyTwoBit;                       //checking whether all instructions are 32 bit or not 
  int noOfI;                               //number of instructions 

  RegisterTable registers;                 // encodings for registers
  OpcodeTable opcodes;                     // encodings of opcodes
  int myLabelAddress;   // Used to assign labels addresses
  int binaryToDecimal(string n); // used to conver from a binary value to a base 10 number.
  int immediateToDecimal(string n, bool isRegister); //used to convert twos complement immediate fields
  string decimalToHex(int n); //used to convert a decimal integer into a hexadecimal string.
  string decimalToString(int n); //used to convert a decimal integer into its string equivalent 
  
  // Decomposes a line of assembly code into strings for the opcode field and operands, 
  // checking for syntax errors and counting the number of operands.
  void getTokens(string line, string &opcode, string *operand, string &funct, InstType &Insty);

  // Given an Opcode, a string representing the operands, and the number of operands, 
  // breaks operands apart and stores fields into Instruction.
  bool getOperands(Instruction &i, Opcode o, string *operand);


  // Helper functions
  bool isWhitespace(char c)    { return (c == ' '|| c == '\t'); };
  bool isDigit(char c)         { return (c >= '0' && c <= '9'); };
  bool isAlphaUpper(char c)    { return (c >= 'A' && c <= 'Z'); };
  bool isAlphaLower(char c)    { return (c >= 'a' && c <= 'z'); };
  bool isSign(char c)          { return (c == '-' || c == '+'); };
  bool isAlpha(char c)         {return (isAlphaUpper(c) || isAlphaLower(c)); };
  
 // Given a valid instruction, returns a string representing the 32 bit MIPS binary encoding
  // of that instruction.
  string encode(Instruction i);

};

#endif
