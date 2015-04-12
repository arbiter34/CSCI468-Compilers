program Program3;
  var Sum: Integer;
       I:   Integer;
       N:   Integer;
  begin
     Read(N);
     I   := 1;
     Sum := 0;
     while (I <= N) or (I > 100) do begin
       Sum := Sum + I;
       I := I + 1;
       Write(I, Sum)
     end
   end.

   MOV SP D0
   ADD SP #3 SP
   RD 2(D0)
   MOV #1 1(D0)
   MOV #0 0(D0)
L1:
   PUSH 1(D0)
   PUSH 2(D0)
   CMPLES
   PUSH 1(D0)
   PUSH #100
   CMPGTS
   ORS
   BRFS L2
   PUSH 0(D0)
   PUSH 1(D0)
   ADDS
   POP 0(D0)
   PUSH 1(D0)
   PUSH #1
   ADDS
   POP 1(D0)
   PUSH 1(D0)
   WRTLNS
   PUSH 0(D0)
   WRTLNS
   BR L1
L2:
  MOV D0 SP
  HLT