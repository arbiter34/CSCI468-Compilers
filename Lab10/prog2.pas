program Program2;
    var I: Integer;
       N: Integer;
  begin
     Read(N);
     for I := 1 to N do
       Write(I);
   end.


    MOV SP D0
    ADD SP #2 SP
    RD 1(D0)
	MOV #1 0(D0)
L1:
	PUSH 0(D0)
	PUSH 1(D0)
	CMPLES
	BRFS L2
	PUSH 0(D0)
	WRTS
	ADD 0(D0) #1 0(D0)
	BR L1
L2:
	MOV D0 SP
	HLT