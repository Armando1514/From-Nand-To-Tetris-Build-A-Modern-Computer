@17
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@17
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
D=M-D
@SP
M=M-1
M=M-1
@BOOLTRUE.0
D;JEQ
@SP
A=M
M=0
@SP
M=M+1
@ENDBOOL.0
0;JMP
(BOOLTRUE.0)
@SP
A=M
M=-1
@SP
M=M+1
(ENDBOOL.0)
@892
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@891
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
D=M-D
@SP
M=M-1
M=M-1
@BOOLTRUE.1
D;JLT
@SP
A=M
M=0
@SP
M=M+1
@ENDBOOL.1
0;JMP
(BOOLTRUE.1)
@SP
A=M
M=-1
@SP
M=M+1
(ENDBOOL.1)
@32767
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@32766
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
D=M-D
@SP
M=M-1
M=M-1
@BOOLTRUE.2
D;JGT
@SP
A=M
M=0
@SP
M=M+1
@ENDBOOL.2
0;JMP
(BOOLTRUE.2)
@SP
A=M
M=-1
@SP
M=M+1
(ENDBOOL.2)
@56
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@31
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@53
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
M=D+M
D=A+1
@SP
M=D
@112
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
M=M-D
D=A+1
@SP
M=D
@SP
A=M-1
M=-M
D=M
@SP
A=M-1
M=D
@SP
A=M-1
D=M
A=A-1
M=M&D
D=A+1
@SP
M=D
@82
D=A
@SP
A=M
M=D
D=A+1
@SP
M=D
@SP
A=M-1
D=M
A=A-1
M=M|D
D=A+1
@SP
M=D