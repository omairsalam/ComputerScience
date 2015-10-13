//Omair Alam oa6ci
#include "MachineParser.h"
#include <iostream>

using namespace std;

/* This file reads in a MIPS assembly file specified at the command line.
 * If the file is correct syntactically, each instruction in the file
 * will be translated into its 32 bit MIPS binary encoding and printed
 * to stdout, one per line.
 *
 */

int main(int argc, char *argv[])
{
  MachineParser *parser;
  
  if(argc < 2){
    cerr << "Need to specify an assembly file to translate."<<endl;
    exit(1);
  }

  parser = new MachineParser(argv[1]);

  if(parser->isThirtyTwoBit() == false){
    cerr << "Some instruction(s) is more than 32 bits " << endl;
    exit(1);
  }
  
/*
if(parser->isFormatCorrect() == false){
    cerr << "Format of some part of the input file is incorrect " << endl;
  }
    i = parser->getNextInstruction();

  while( i.getOpcode() != UNDEFINED){
    // cout << i.getString() << endl;
    cout << i.getEncoding() << endl;
    i = parser->getNextInstruction();
  }
  */

  Instruction i;

  //Iterate through instructions, printing each encoding.
  //cout << " got to parser" << endl; 
  i = parser->getNextInstruction();
 // cout << "wapis "<<endl;
  for (int k=0; k< (parser -> numberOfInstructions()); k++){
    if(i.getEncoding().find("-1")!= std::string::npos){         
    cerr << "Format this instruction is incorrect" << endl;
    i = parser->getNextInstruction();
    continue; 
      }
    // cout << i.getString() << endl;
    cout << i.getEncoding() << endl;
    i = parser->getNextInstruction();
  }

/*
if(parser->isFormatCorrect() == false){
    cerr << "Format of some part of the input file is incorrect " << endl;
  }
    i = parser->getNextInstruction();

  while( i.getOpcode() != UNDEFINED){
    // cout << i.getString() << endl;
    cout << i.getEncoding() << endl;
    i = parser->getNextInstruction();
  }
  */
  delete parser;
}
