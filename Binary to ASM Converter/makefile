#Omair Alam oa6ci
DEBUG_FLAG= -DDEBUG -g -Wall
CFLAGS=-DDEBUG -g -Wall

.SUFFIXES: .cpp .o

.cpp.o:
	g++ $(CFLAGS) -c $<


ASM: ASM.cpp Instruction.cpp Opcode.cpp RegisterTable.cpp MachineParser.cpp
	g++ -o ASM ASM.cpp Opcode.cpp MachineParser.cpp RegisterTable.cpp Instruction.cpp

clean:
	/bin/rm -f ASM *.o core


