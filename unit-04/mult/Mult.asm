// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

@R2
M = 0

@R1
D = M

// IF R1 = 0 stop
@END
D; JEQ

// IF R1  = 1 SET R2 = R0, NORMAL FLOW
@R0
D = M

// IF R0 = stop
@END
D; JEQ

// IF R0 = 1 SET R2 = R1
@SETR2TOR1
D - 1; JEQ


(LOOP)


    @R0
    D = M

    @R2
    M = D + M

    @R1
    D = M - 1

    @R1
    M = D

    // IF R1 GREATER THAN 0, LOOP
    @LOOP
    D; JGT



(END)
    @END
    0; JMP


(SETR2TOR1)
@R1
D = M
@R2
M = D
@END
0; JEQ

