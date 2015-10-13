//Omair Alam oa6ci
#include "Instruction.h"



Instruction::Instruction()
{
  myOpcode = UNDEFINED;
  myRS = myRT = myRD = NumRegisters;
}

Instruction::Instruction(Opcode op, string rs, string rt, string rd, int imm)
// You can specify all the fields to initialize the Instruction
{
  setValues(op, rs, rt, rd, imm);
}

void Instruction::setValues(Opcode op, string rs, string rt, string rd, int imm)
// You can specify all the fields to initialize the Instruction
{

  myOpcode = op;


  myRS = rs;


  myRT = rt;


  myRD = rd;

  myImmediate = imm;

  //  if(!( (imm & 0xFFFF0000) << 1))  // make sure it has nothing in upper 16 bits
  //    myImmediate = imm;  

}

string Instruction::getString()
// Returns a string which represents all of the fields 
{
  stringstream s ;
  s << "OP: \t" << myOpcode << "\t" << "RD: " << myRD << "\t" << 
    "RS: " << myRS << "\t" << "RT: " << "\t" << myRT << "\t" <<
    "Imm: " << myImmediate;
  
  return s.str();
  
}

