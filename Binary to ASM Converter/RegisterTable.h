//Omair Alam oa6ci
#ifndef _REGISTERTABLE_H
#define _REGISTERTABLE_H

#include <string>

using namespace std;

/* This class stores information about the valid register names for MIPS.
 */

typedef int Register;
const int NumRegisters = 32;

struct RegisterEntry{
  string name;
  Register number;
};
/* This class stores information about the valid register names for MIPS.
 */
class RegisterTable{
 public:
 	 // Constructs and initializes the valid register names and numbers
  RegisterTable();

  //Given a string representing a Mips number, return the Mips register operand associated with that number.
  string getName(int num);


 private:
  RegisterEntry myRegisters[64];

};


#endif
