//Omair Alam oa6ci
#include "MachineParser.h"
#include <string>
#include <iostream>
#include <sstream>
#include <vector>
#include <iomanip>

MachineParser::MachineParser(string filename)
  // Specify a text file containing MIPS 32 bit instructions
  // checks syntactic correctness of file and creates a list of instructions in MIPS formatting.
{
  Instruction i;
  myFormatCorrect = true;
  thirtyTwoBit = true; 
  noOfI=0; 

  myLabelAddress = 0x400000;
  //In order to read from the input file, create an ifstream 
  ifstream in;
  in.open(filename.c_str());
  if(in.bad()){
    myFormatCorrect = false;
  }
  else{
    string line;
    //Using while loop for the purpose of parsing each line of the input file to break it down into correct MIPS components 
    while( getline(in, line)){
       noOfI++;
      if(thirtyTwoBit == false){
          continue;} 
      // if the length of a line is not 32 bits, it is not a proper MIPS instruction 
      if(line.size() != 32)
    {
    //  cout << "enters this thirtytwobit if statement "<< endl; 
      thirtyTwoBit = false; 
      continue;
    }
    else
    {
      string opcode(""); //string opcode is going to contain the opcode after the get token method is called 
      string operand[80]; //an array containing the binary representations of different fields in the MIPS 32 bit instruction 
      string funct("");
      InstType InsTy;
      //for parsing purposes, breaks the 32 bit instruction into tokens to find out the opcode, the operands(it is an array), the function field as well as the type 
      getTokens(line, opcode, operand, funct, InsTy);
      //builds an instance of the opcode using the function field as well as the data of the opcode itself 
      Opcode o = opcodes.getOpcode(opcode, funct);
       
        
      //if o is undefined, the getOpcode method could not return a proper opcode meaning the opcode of the instruction was incorrect thus the instruction was incorrect 
      if(o == UNDEFINED){
        if(thirtyTwoBit == false){
          break; 
        }
        // invalid opcode specified
      //  cout << "32 bit string is not a valid mips instruction" << endl; 
        //myFormatCorrect = false;
        //cout << "break occuring here" <<endl; 
        i.setEncoding("-1");      //we push the encoding as -1 so that when ASM.cpp tries to find whether its undefined or not, it can easily just compare it to -1 to know 
        myInstructions.push_back(i);

        continue;
    //    cout << "lol" <<endl; 
      }
      bool success = getOperands(i, o, operand); //based on the opcode, the operands, and the insstruction, success is a boolean which determines whether the MIPS insturction is a correct one in the correct format 
      if(!success){
        if(thirtyTwoBit == false){
          break; 
        }
     //   cout << "32 bit string is not a valid mips instruction" << endl; 
        myFormatCorrect = false;
    //    cout << "second break occuring here" <<endl; 
      // break;
      }
      string encoding = encode(i); //we take the instruction and use the encoding method to output the MIPS instruction representing these 32 bits 
      i.setEncoding(encoding);//based on this encoding string, we can create an instruction 
      myInstructions.push_back(i); //the instructions are then pushed onto the vector that is holding all these instructions waiting to be printed by the program at the end 

    }
  }

  myIndex = 0;
}
}
void MachineParser::getTokens(string line,
             string &opcode, string *operand, string &funct, InstType &InsTy
             )
  // Decomposes a line of assembly code into strings for the opcode field and operands, 
  // checking for syntax errors and counting the number of operands.
{
    opcode = line.substr(0,6);
    Opcode myOpcode = opcodes.getOpcode(opcode);


    //I TYPE INSTRUCTIONS 
    if(opcodes.getInstType(myOpcode) == ITYPE)
    {
      if (opcodes.addressShiftNeeded(myOpcode) ==true)
      //if the immidiate field is an address, we have to shift left to align it 
      {      
        operand[0] = line.substr(6,5); //rs
        operand[1] = line.substr(11, 5); //rt
        operand[2] = line.substr(16, 16) + "00"; //immediate
      }    

      else
      {
        operand[0] = line.substr(6,5); //rs
        operand[1] = line.substr(11, 5); //rt
        operand[2] = line.substr(16, 16); //immediate
    //    cout << "operand[2] is" << operand[2] << endl;
      }
    }
    //R TYPE INSTRUCTIONS
    else if(opcodes.getInstType(myOpcode) == RTYPE)   //dealing with R type instructions 
    {
      operand[1] = line.substr(6,5); //rs
      operand[2] = line.substr(11,5); //rt
      operand[0] = line.substr(16,5); //rd
      operand[3] = line.substr(21,5); //shammt
      operand[4] = line.substr(26,6); //funct
      funct = operand[4]; //only to check format of funct AGAIN
    }
    //JTYPE INSTRUCTIONS 
    else if(opcodes.getInstType(myOpcode) == JTYPE)
    {
      operand[0] = ("000000" + line.substr(6, 26) + "00"); //we add 0's on the left to align it properly by making it 26 bits 
    }
    else
    {
     // cerr << "Invalid Instruction: Does not fit any type" << endl;
    }
}

  bool MachineParser::getOperands(Instruction &i, Opcode o, 
          string *operand)
  // Given an Opcode, an array of strings representing the operands
  // breaks operands apart and stores fields into Instruction.
{
  OpcodeTable* myTable = new OpcodeTable;
  string rs, rt, rd;
  int imm;
  imm = 0;
  //These methods use the Opcode class in order to gain information on the current instruction in to determine how each instructions data fields should be saved
  
  int rsPosition = opcodes.RSposition(o);
  //obtain positions of rs,rd,rt and imm from opcode table using the opcode as a parameter 
  int rtPosition = opcodes.RTposition(o);
  int rdPosition = opcodes.RDposition(o);
  int immediatePosition = opcodes.IMMposition(o);
  //Get instruction type
  InstType insTyp = opcodes.getInstType(o); //having the type of the instruction i.e. RTYPE, JTYPE, ITYPE makes work easier 
   
  //FOR ITYPE INSTRUCTIONS 
  if(insTyp == ITYPE)
  {
    //BRANCH OR EQUAL TO INSTRUCTIONS
    if((myTable->getName(o).find("beq")!=std::string::npos)||(immediatePosition == 1))
    {
      rsPosition = 0;
      rtPosition = 1;
    }

    if(immediatePosition == 1)
    {
      rsPosition = 0;
      rtPosition = 1;
    }   
    
  }
//RS =, RD AND RT FOR R TYPE INSTRUCTIONS 
  if(insTyp == RTYPE)
  {
    rdPosition = 0;
    rsPosition = 1;
    rtPosition = 2;
  }
  if(insTyp == ITYPE)
  {
    if(immediatePosition == 1)
    {
      immediatePosition = 2;
    }
  }
   //the following statments check whether the register position is -1 which means it is not being used.
    //if the position is not -1 (meaning it is being used), we get the binary string representation of the register
    //from the array of operands that we have, and then we use the method registerBinaryToDecimal to obtain the correct 
    //number of the register that we are going to use 
    //the boolean isRegister is true for all these, because they are all registers 

  if(rsPosition != -1){
    rs = registers.getName(immediateToDecimal(operand[rsPosition],true));
  }

  if(rtPosition != -1){
    rt = registers.getName(immediateToDecimal(operand[rtPosition],true));
  }
  
  if(rdPosition != -1){
    rd = registers.getName(immediateToDecimal(operand[rdPosition],true));

  }
  //IMMEDIATES FOR R TYPE, J TYPE and I TYPE 

 //if instruction is I type and has an immmediate, the immediate has to be address in memory thus we won't treat it as a register 
  if(immediatePosition != -1 && insTyp == ITYPE)
  {

    imm = immediateToDecimal(operand[immediatePosition],false);
  //  cout << "chutiya" << immediatePosition << endl; 
  }



  //this means there is an immediate for an RTYPE, so immediate is s shift amount for instructions like SRA and SLL, so the immedaite is register type  
   if(immediatePosition != -1 && insTyp == RTYPE)
  {
    immediatePosition = 3;
    imm = immediateToDecimal(operand[immediatePosition],true);

  }  

  //this means the instruction is a jump and the immediate is the only operand for it. Furthermore, its an address so the immediate is not of type register 
  if(insTyp == JTYPE && immediatePosition != -1)
  {
    immediatePosition = 0;

    imm = immediateToDecimal(operand[immediatePosition],true);
  }
//set the values of the instruction using all the values for o, rs, rt, rd and imm that we have found 
  i.setValues(o, rs, rt, rd, imm);

  return true;

}

Instruction MachineParser::getNextInstruction()
//Iterator to get next instrution
{
  if(myIndex < (int)(myInstructions.size())){
    myIndex++;
    return myInstructions[myIndex-1];
  }
  
  Instruction i;
  return i;

}

string MachineParser::encode(Instruction i)
  // Given a valid instruction, returns a string representing the 32 bit MIPS binary encoding
  // of that instruction.
{
  OpcodeTable* myTable = new OpcodeTable;
  Opcode myOpcode = i.getOpcode();
  string ASMString = "";

   //adds the opcode(alphabetical representation)  of the instruction to the string 
   ASMString += (myTable-> getName(myOpcode) + " ");  // we must find the name for each instruction

  //FOR ITYPE INSTRUCTIONS 
  if(myTable -> getInstType(myOpcode) == ITYPE)
  {


  if(myTable -> IMMposition(myOpcode) == 1)  //symbolizes a load store instruction since immediate is
    //is at position 1 
    {
      if( myTable -> RTposition(myOpcode) != -1)      //if the RT register is being used 
      {
        ASMString += i.getRT();
        ASMString += ", "; 
      }
      //convert the immediate to a string, the method deals with the two's complement conversion
      ASMString+= decimalToString(i.getImmediate());

      if( myTable -> RSposition(myOpcode) != -1)
      {
        ASMString += "("+i.getRS()+")"; //paranthesis for load instruction $register(offset)
      }
    }
  else
    //THIS IS FOR INSTRUCTIONS THAT ARE NOT LOAD OR STORE 
    {

    if( myTable -> RSposition(myOpcode) >= 0)
      {
        ASMString += (i.getRS());
        ASMString += ", "; 
      }
      if( myTable -> RTposition(myOpcode) >= 0)
      {
        ASMString += i.getRT() ;
        ASMString += ", "; 
      }
      
    if((ASMString.find("beq")!=std::string::npos)) //meaning instruction is of type branch if equal to thus immediate is an addresss
    {
      ASMString += decimalToHex(i.getImmediate());  //converts the address to which we have to branch from decimal to hexadecimal
    }
    
    else  //if its not a beq instruction then the immedaite is not an address so we simply parse it to a string 
          //instead of treating it as an address 
    {
      ASMString += decimalToString(i.getImmediate());
    }
  }
}

  //FOR RTYPE INSTRUCTIONS 
else if(myTable -> getInstType(myOpcode) == RTYPE)
  {
  
//I've put a series of conditional statements which using the opcode type to determine the fields that need to be added to 
// ASM String 
//we iterate through the number of elements in an instruction of type (myOpcode) and 
//we find the RD, RS, RT as well as the immediate  
      for(int position = 0; position < myTable -> numOperands(myOpcode); position++)
    {
      if((myTable->RDposition(myOpcode)) == position)
      {
        ASMString += (i.getRD());
      }
      if(myTable-> IMMposition(myOpcode) == position)
      {
        ASMString += decimalToString(i.getImmediate());
      }
      if(myTable->RSposition(myOpcode) == position)
      {
        ASMString+=i.getRS();
      }
      if((myTable->RTposition(myOpcode)) == position)
      {
        ASMString += (i.getRT());
      }
      if(position < (myTable -> numOperands(myOpcode)-1)) // comma after every register
      {
        ASMString += ", ";
      }

    }  
  }
//FOR JTYPE INSTRUCTIONS 
else
  {

    if(myTable -> isIMMLabel(myOpcode) == true)     //J type instructions have immediate labels 
    {
      ASMString += decimalToHex(i.getImmediate());  //convert the immedaite to its hexadeciaml representation 

    }

  } 
 // cout <<"My decoded instructions are " <<ASMString << endl; 

  return ASMString;
}
//DECIMAL TO HEXADECIAML CONVERTER 
string MachineParser::decimalToHex(int n)
{
  stringstream ss;  //uses a stringsteam to convert the hexadecimal to binary 
  ss<<"0x"<<hex<<n;
  string result(ss.str());
  return result;
}

//IMMEDIATE TO DECIMAL CONVERTER 
int MachineParser::immediateToDecimal(string n, bool isRegister)
{
 // cout << "immToDec" << n << endl; 
  int output = 0;
  int strLength = n.length()-1;
      int AmtSub = 0;
      if(isRegister==false){
      if(((n.at(0)-48)*1)==1)
      {
          if(n.length()==16)
            {
              AmtSub-= 65536;
            }
          else if (n.length()==32)
            {
              AmtSub-=2147483648;
            }
      }
   } 
      int multiplicationFactor=1;
      for(int j = strLength; j>=0; j--)
      {
          output += ((n.at(j)-48)*multiplicationFactor); 
          multiplicationFactor *= 2;
      }
   
    return output+AmtSub ; //returns the two's complement representatation of number contained in the string 
}

//BINARY TO DECIMAL CONVERTER 
int MachineParser::binaryToDecimal(string n)
{
  //cout << "binTOoDec " << n << endl; 
  int output = 0;
  int strLength = n.length();
  
       
     int multiplicationFactor =1;
    //iterates through the string and converts it 
      for(int k = strLength-1; k >=0; k--)
      {
          output = output +( (n.at(k)-48) * multiplicationFactor); 
           multiplicationFactor = multiplicationFactor * 2;
      }
    return output;
}

//DECIMAL TO STRING PARSER 
string MachineParser::decimalToString(int n)
{
 // cout << "value of n is " << n << endl; 
  stringstream myStream;
  myStream<<n;
  string output(myStream.str());
    return (output);
}
