//Omair Alam oa6ci
#ifndef __OPCODE_H__
#define __OPCODE_H__

using namespace std;

#include <iostream>
#include <string>


// Listing of all supported MIPS instructions
enum Opcode {
  ADD, 
  ADDI, 
  MULT,
  MFLO,
  SRA,
  SLT,
  LB,
  BEQ,
  J, 
  UNDEFINED
};

// Different types of MIPS encodings
enum InstType{
  RTYPE,
  ITYPE,
  JTYPE
};


/* This class represents templates for supported MIPS instructions.  For every supported
 * MIPS instruction, the OpcodeTable includes information about the opcode, expected
 * operands, and other fields.  
 */
class OpcodeTable {
 public:
  OpcodeTable();

  // Given a valid MIPS assembly mnemonic, returns an Opcode which represents a 
  // template for that instruction.
  Opcode getOpcode(string op);
  //Given a function field and an opcode retrun the proper Opcode from the RegisterTables:For RTYPE
  Opcode getOpcode(string op, string func);

  // Given an Opcode, returns number of expected operands.
  int numOperands(Opcode o);

  // Given an Opcode, returns the position of RS/RT/RD/IMM field.  If field is not
  // appropriate for this Opcode, returns -1.
  int RSposition(Opcode o);
  int RTposition(Opcode o);
  int RDposition(Opcode o);
  int IMMposition(Opcode o);

  // Given an Opcode, returns true if instruction expects a label in the instruction.
  // See "J".
  bool isIMMLabel(Opcode o);

  //Given an immediate for an opcode, shift the immediate by two bits to align it properly
  bool addressShiftNeeded(Opcode o);


  //Given an Opcode, returns true if instruction's immediate field is being used as an address.
  bool isAddress(Opcode o);

  // Given an Opcode, returns instruction type.
  InstType getInstType(Opcode o);

  // Given an Opcode, returns a string representing the binary encoding of the opcode
  // field.
  string getOpcodeField(Opcode o);

  // Given an Opcode, returns a string representing the binary encoding of the function
  // field.
  string getFunctField(Opcode o);
  //Given an Opcode, return a string representing the name of the function
  string getName(Opcode o);


 private:
  struct OpcodeTableEntry{
    string name;
    int numOps;
    int rdPos;
    int rsPos;
    int rtPos;
    int immPos;
    bool immLabel;
    bool isAddress;
    bool addressShift;
 /* these position values relect the order in which the 
     * various register specifiers appear in the instruction mnemonic.
     * So, for example, the add instruction mnemonic is
     * add rd, rs, rt
     * Thus rdPos is 0, rsPos is 1, and rtPos is 2
     * As seen below, if some field is missing from a given type of instruction,
     * that field position is given as -1.  (See the ADD example in Opcode.cpp, where
     * there is not immediate field in the add instruction, so .immPos is -1.)
     */
    InstType instType;
    string op_field;
    string funct_field;

    OpcodeTableEntry(){
      numOps = 0; 
      rdPos = rsPos = rtPos = immPos = -1;
      immLabel = false;
    };
  };

  OpcodeTableEntry myArray[UNDEFINED];


};


#endif
