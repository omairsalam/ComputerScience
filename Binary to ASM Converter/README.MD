# Binary to Assembly Language Converter 

<h2> High Level Design </h2> 
<p> This program converts a machine level binary instruction in 32 bit MIPS architecture into an assembly language instruction in MIPS </p> 
<p> To run this program, you have to run make which will in turn run the makefile specified. This should produce an executible which takes in one parameter i.e. the file that containts the 32 bit instructions </po
<p> One example to run the program would be would therefore be, <pre> make </pre> <pre> ASM test1.mach </pre> Please note that asm means assembly and mach means machine. In this program we are converting mach to asm. </p> 

<p> Please note that sometimes copying over 32 bit instructions from one text editor into an already made .mach file does not work and the error received says that the instruction is not 32 bits while it obviously it. To resolve this error, please write the 32 bits manually instead of copying, OR, copy the instruction without any prior formatting. (There is an option in sublime for this) </p> <p> Cheers! </p> 
