Program 4

program Program4;
  var Sum, I, N : Integer;
  begin
     Read(N);
     I   := 0;
     Sum := 0;
     repeat
       Sum := Sum + I;
       I   := I + 1;
       Write(I, Sum);
     until (I > N) and (I <= 100)
   end.
 
  MOV SP D0
  ADD SP #3 SP
  RD 2(D0)
  MOV #0 1(D0)
  MOV #0 0(D0)
L1:
  PUSH 0(D0)
  PUSH 1(D0)
  ADDS
  POP 0(D0)
  PUSH 1(D0)
  PUSH #1
  ADDS
  POP 1(D0)
  PUSH 1(D0)
  WRTS
  PUSH 0(D0)
  WRTS
  PUSH 1(D0)
  PUSH 2(D0)
  CMPGTS
  BRFS L1
  PUSH 1(D0)
  PUSH #100
  CMPLES
  BRFS L1
  MOV D0 SP
  HLT