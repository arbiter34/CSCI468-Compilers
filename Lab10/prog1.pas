Program 1

program Program1;
  var Apples: Integer;
       Cost:   Integer;
  begin
     Read(Apples);
     if Apples > 0
       then begin
         Cost := Apples * 5;
         Write(Cost)
       end
       else begin
         Cost := Apples * 7;
         Write(Cost)
       end
   end.


  MOV SP D0
  ADD SP #2 SP
  RD 0(D0)
  PUSH 0(D0)
  PUSH #0
  CMPGTS
  BRFS L1
  PUSH 0(D0)
  PUSH #5
  MULS
  POP 1(D1)
  PUSH 1(D1)
  WRTS
  BR L2
L1:
  PUSH 0(D0)
  PUSH #7
  MULS
  POP 1(D0)
  PUSH 1(D0)
  WRTS
L2:
  MOV D0 SP
  HLT

