br L1
L2:
	; fred start
add SP #0 SP
mov D1 -2(SP)
sub SP #2 D1
	; activation end
push 0(D0)
push #1
adds
pop 0(D0)
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #2 SP
ret
	; fred end
L1:
	; program1 start
add SP #2 SP
sub SP #2 D0
	; activation end
rd 0(D0)
push 0(D0)
wrts
push #"\n"
wrts
add SP #1 SP
call L2
sub SP #0 SP
sub SP #1 SP
push 0(D0)
wrts
	; deactivation start
mov D0 SP
	; program1 end
hlt
