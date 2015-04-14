br L1
L2:
	; factorial start
add SP #1 SP
mov D1 -4(SP)
sub SP #4 D1
	; activation end
push 1(D1)
push #0
cmpeqs
brfs L3
push #1
pop 7(D0)
br L4
L3:
push 1(D1)
add SP #1 SP
push 1(D1)
push #1
subs
call L2
sub SP #1 SP
sub SP #1 SP
push 7(D0)
muls
pop 7(D0)
L4:
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; factorial end
L5:
	; listnum start
add SP #1 SP
mov D1 -4(SP)
sub SP #4 D1
	; activation end
push #50
push #-1
muls
pop 3(D1)
L6:
push 1(D1)
push 3(D1)
cmpgts
brfs L7
push 1(D1)
wrts
push #" "
wrts
push 1(D1)
push #1
subs
pop 1(D1)
br L6
L7:
push #""
wrts
push #"\n"
wrts
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; listnum end
L8:
	; scopetest start
add SP #1 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
push #1
castsf
push #2.0
mulsf
push #4
castsf
divsf
push #2
castsf
addsf
pop 2(D1)
push #"From procedure otherthing is: "
wrts
push 2(D1)
wrts
push #"\n"
wrts
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #2 SP
ret
	; scopetest end
L12:
	; somemoreimbedding start
add SP #0 SP
mov D4 -3(SP)
sub SP #3 D4
	; activation end
push #"I am super imbedded and awesome. B is "
wrts
push 1(D4)
wrts
push #"\n"
wrts
	; deactivation start
mov D4 SP
mov 0(SP) D4
add SP #3 SP
ret
	; somemoreimbedding end
L11:
	; imbeddedagain start
add SP #1 SP
mov D3 -4(SP)
sub SP #4 D3
	; activation end
add SP #1 SP
push 1(D3)
call L12
sub SP #1 SP
sub SP #1 SP
	; deactivation start
mov D3 SP
mov 0(SP) D3
add SP #3 SP
ret
	; imbeddedagain end
L10:
	; imbedded start
add SP #1 SP
mov D2 -4(SP)
sub SP #4 D2
	; activation end
add SP #1 SP
push 1(D2)
call L11
sub SP #1 SP
sub SP #1 SP
	; deactivation start
mov D2 SP
mov 0(SP) D2
add SP #3 SP
ret
	; imbedded end
L9:
	; retstring start
add SP #2 SP
mov D1 -7(SP)
sub SP #7 D1
	; activation end
push #"trying the whole imbedded thing"
wrts
push 3(D1)
wrts
push #"\n"
wrts
add SP #1 SP
push 5(D1)
call L10
sub SP #1 SP
sub SP #1 SP
push #"This string was returned as a value"
pop 10(D0)
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #5 SP
ret
	; retstring end
L13:
	; refcheck start
add SP #0 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
push #5
push 1(D1)
adds
pop 1(D1)
push #"Variable x should be changed to "
wrts
push 1(D1)
wrts
push #"\n"
wrts
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; refcheck end
L15:
	; refcheck3 start
add SP #0 SP
mov D2 -3(SP)
sub SP #3 D2
	; activation end
push #"In refCheck 3 value is: "
wrts
push 1(D2)
wrts
push #"\n"
wrts
push #""
wrts
push #"\n"
wrts
	; deactivation start
mov D2 SP
mov 0(SP) D2
add SP #3 SP
ret
	; refcheck3 end
L14:
	; refcheck2 start
add SP #1 SP
mov D1 -6(SP)
sub SP #6 D1
	; activation end
push 2(D1)
push 3(D1)
addsf
pop 2(D1)
push #"X in refcheck2 is "
wrts
push 2(D1)
wrts
push #"\n"
wrts
add SP #1 SP
mov 3(D1) 0(SP)
add SP #1 SP
call L15
sub SP #1 SP
sub SP #1 SP
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #5 SP
ret
	; refcheck2 end
L1:
	; lab1 start
add SP #13 SP
sub SP #13 D0
	; activation end
push #"Welcome to my program (^_^ )"
wrts
push #"\n"
wrts
push #"Please enter an integer to find the factorial of:"
wrts
push #"\n"
wrts
rd 1(D0)
push #0
nots
brfs L16
add SP #1 SP
push 1(D0)
call L2
sub SP #1 SP
sub SP #1 SP
push 7(D0)
wrts
push #"\n"
wrts
br L17
L16:
push #"-1"
wrts
push #"\n"
wrts
L17:
add SP #1 SP
push #100
push #2
divs
call L5
sub SP #1 SP
sub SP #1 SP
add SP #1 SP
add SP #1 SP
push #9
push #3
divs
call L2
sub SP #1 SP
sub SP #1 SP
push 7(D0)
call L5
sub SP #1 SP
sub SP #1 SP
push #5
push #2
mods
pop 0(D0)
push 0(D0)
push #-1
muls
wrts
push #"\n"
wrts
push #"testing"
wrts
push #1
pop 5(D0)
push #0
pop 6(D0)
push 5(D0)
wrts
push 6(D0)
wrts
push #"\n"
wrts
push 6(D0)
push 5(D0)
ands
pop 6(D0)
push 6(D0)
wrts
push #"\n"
wrts
push 6(D0)
push 5(D0)
ors
pop 6(D0)
push 6(D0)
wrts
push #"\n"
wrts
push #1
push #2
adds
push #-1
muls
wrts
push #"\n"
wrts
push #15
push #15
muls
push #5
divs
push #1
adds
push #2
push #2
muls
subs
pop 0(D0)
push #"From *main* otherthing is: "
wrts
push 0(D0)
wrts
push #"\n"
wrts
add SP #1 SP
call L8
sub SP #0 SP
sub SP #1 SP
push #"From *main* otherthing still is: "
wrts
push 0(D0)
wrts
push #"\n"
wrts
push #7.0
push #2.1
mulsf
push #0.6
push #13
castsf
divsf
subsf
add SP #1 SP
push #13
push #11
subs
call L2
sub SP #1 SP
sub SP #1 SP
push 7(D0)
castsf
mulsf
pop 2(D0)
push 2(D0)
wrts
push #"\n"
wrts
add SP #1 SP
push #0.001
push #1.23
push #"String as input argument"
call L9
sub SP #3 SP
sub SP #1 SP
push 10(D0)
wrts
push #"\n"
wrts
push #"X starts at "
wrts
push 0(D0)
wrts
push #"\n"
wrts
add SP #1 SP
mov 0(D0) 0(SP)
add SP #1 SP
call L13
sub SP #1 SP
sub SP #1 SP
push #"X is now "
wrts
push 0(D0)
wrts
push #" in main"
wrts
push #"\n"
wrts
push #"floatMathTest start: "
wrts
push 2(D0)
wrts
push #"\n"
wrts
add SP #1 SP
push #10
push #3
divs
mov 2(D0) 0(SP)
add SP #1 SP
mov 2(D0) 0(SP)
add SP #1 SP
call L14
sub SP #3 SP
sub SP #1 SP
push #"floatMathTest is now "
wrts
push 2(D0)
wrts
push #" in main"
wrts
push #"\n"
wrts
push #5
pop 0(D0)
L18:
push 0(D0)
wrts
push #" "
wrts
push 0(D0)
push #3
subs
pop 0(D0)
push 0(D0)
push #0
cmplts
brfs L18
push #"For Loop!!!!"
wrts
	; For control statement start
push #5
push #-1
muls
	; Initialize for control variable
pop 0(D0)
push 0(D0)
push #20
adds
L19:
push 0(D0)
push 13(D0)
cmples
brfs L20
push #"otherthing is "
wrts
push 0(D0)
wrts
push #"\n"
wrts
add 0(D0) #1 0(D0)
br L19
L20:
push #1
pop 3(D0)
push #5
castsf
push #6.5
addsf
wrts
push #"\n"
wrts
push #"Thank you for using this!"
wrts
push #"\n"
wrts
add SP #1 SP
push 1(D0)
call L2
sub SP #1 SP
sub SP #1 SP
push 7(D0)
wrts
push #"\n"
wrts
	; deactivation start
mov D0 SP
	; lab1 end
hlt
