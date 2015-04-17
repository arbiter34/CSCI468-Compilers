br L1
L2:
	; factorial start
add SP #0 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
push 1(D1)
push #0
cmpeqs
brfs L3
push #1
pop 1(D0)
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
push 1(D0)
muls
pop 1(D0)
L4:
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; factorial end
L1:
	; program3 start
add SP #2 SP
sub SP #2 D0
	; activation end
rd 0(D0)
push 0(D0)
push #0
cmpges
brfs L5
add SP #1 SP
push 0(D0)
call L2
sub SP #1 SP
sub SP #1 SP
push 1(D0)
wrts
br L6
L5:
push #1
push #-1
muls
wrts
L6:
	; deactivation start
mov D0 SP
	; program3 end
hlt
