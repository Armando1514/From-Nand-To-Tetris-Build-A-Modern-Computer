// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.


(LOOP)
    @SCREEN //RAM[16384]
    D = A
    @addr
    M = D
    @8191
    D = A // 16384 + 8191 IS THE END OF THE SCREEN MEMORY MAP
    @i
    M = D

    // white screen
    (WHITE)
        @addr
        A = M
        M = 0 //RAM[addr] = 00000000000000..
        @1
        D = A
        @addr
        M = M + D
        A = M

        @i
        D = M - 1
        M = D

        @WHITE
        D  ; JGE
    

    @KBD //RAM[24576]
    D = M

    // if no value in keyboard memory map go back
    @LOOP
    D; JEQ

    // else do the screen black
    @SCREEN //RAM[16384]
    D = A
    @addr
    M = D 
    @8191
    D = A // 16384 + 8191 IS THE END OF THE SCREEN MEMORY MAP
    @i
    M = D

    // black
    // white screen
    (BLACK)
        @addr
        A = M
        M = -1 //RAM[addr] = 11111..
        @1
        D = A
        @addr
        M = M + D
        A = M

        @i
        D = M - 1
        M = D

        @BLACK
        D  ; JGE

    (CHECK)
    @KBD //RAM[24576]
    D = M

    // if the keyboard is still pressed stay in black
    @CHECK
    D; JNE

    @LOOP
    0; JEQ