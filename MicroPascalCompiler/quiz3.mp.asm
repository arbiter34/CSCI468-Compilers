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
push @1(D1)
muls
adds
pop 1(D0)
push 1(D0)
castsf
push @2(D1)
