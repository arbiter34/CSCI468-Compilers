br L1
L2:
	; tryit start
add SP #1 SP
mov D1 -5(SP)
sub SP #5 D1
	; activation end
rd 4(D1)
push 4(D1)
push 1(D0)
push 1(D1)
muls
adds
pop 1(D0)
push 1(D0)
castsf
push 2(D1)
mulsf
pop 2(D1)
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #4 SP
ret
	; tryit end
L1:
	; test start
add SP #4 SP
sub SP #4 D0
	; activation end
rd 0(D0)
rd 1(D0)
rdf 2(D0)
add SP #1 SP
push 0(D0)
push 2(D0)
call L2
sub SP #2 SP
sub SP #1 SP
push 0(D0)
wrts
push 1(D0)
wrts
push 2(D0)
wrts
	; deactivation start
mov D0 SP
	; test end
hlt
