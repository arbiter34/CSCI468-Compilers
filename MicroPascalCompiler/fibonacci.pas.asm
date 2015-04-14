br L1
L2:
	; fib start
add SP #0 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
push 1(D1)
push #0
cmpeqs
push 1(D1)
push #1
cmpeqs
ors
brfs L3
push 1(D1)
pop 0(D0)
br L4
L3:
add SP #1 SP
push 1(D1)
push #1
subs
call L2
sub SP #1 SP
sub SP #1 SP
push 0(D0)
add SP #1 SP
push 1(D1)
push #2
subs
call L2
sub SP #1 SP
sub SP #1 SP
push 0(D0)
adds
pop 0(D0)
L4:
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; fib end
L1:
	; fibonacci start
add SP #1 SP
sub SP #1 D0
	; activation end
add SP #1 SP
push #10
call L2
sub SP #1 SP
sub SP #1 SP
push 0(D0)
wrts
	; deactivation start
mov D0 SP
	; fibonacci end
hlt
