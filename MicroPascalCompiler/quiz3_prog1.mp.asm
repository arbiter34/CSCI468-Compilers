br L1
L1:
	; prog1 start
add SP #3 SP
sub SP #3 D0
	; activation end
push #5
castsf
push 1(D0)
castsf
push 2(D0)
castsf
mulsf
addsf
pop 0(D0)
