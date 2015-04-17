br L1
L2:
	; fred start
add SP #1 SP
mov D1 -5(SP)
sub SP #5 D1
	; activation end
push #5
pop 4(D1)
push #10
pop 1(D1)
push #20
pop @2(D1)
push 4(D1)
wrts
push 1(D1)
wrts
push @2(D1)
wrts
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #4 SP
ret
	; fred end
L1:
	; program2 start
add SP #3 SP
sub SP #3 D0
	; activation end
push #0
pop 0(D0)
push #1
pop 1(D0)
push 0(D0)
wrts
push 1(D0)
wrts
add SP #1 SP
push 0(D0)
push D0
push #1
adds
call L2
sub SP #2 SP
sub SP #1 SP
push 0(D0)
wrts
push 1(D0)
wrts
	; deactivation start
mov D0 SP
	; program2 end
hlt
