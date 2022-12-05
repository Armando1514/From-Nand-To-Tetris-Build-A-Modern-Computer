# ASSEMBLY LANGUAGES AND ASSEMBLERS

 ## Basic Assembler Logic

Repeat: 

- Read the next Assembly language command.
- Break into the different fields it is composed of.
- Lookup the binary code for each field.
- Combine these codes into a single machine language command.
- Output this machine language command

Until end-of-file-reached.

In the specific, **for each instruction**:

- Parse the instruction: break it into its underlying fields.

- A-instruction: translate the decimal value into a binary value.

- C-instruction: for each field in the instruction, generate the corresponding binary code.

  Assemble the translated binary codes into a complete 16-bit machine instruction.

- Write the 16-bit instruction to the output file.

