br L1
L2:
	; factorial start
add SP #0 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
	; deactivation start
mov D1 SP
mov 0(SP) D1
add SP #3 SP
ret
	; factorial end
L1:
	; do_stuff start
add SP #3 SP
sub SP #3 D0
	; activation end
push #1
castsf
pop 0(D0)
	; For control statement start
push #5
push #-1
muls
	; Initialize for control variable
pop 1(D0)
push 1(D0)
push #20
adds
L3:
push 1(D0)
push 3(D0)
cmples
brfs L4
push #"otherthing is "
wrts
push 1(D0)
wrts
push #"\n"
wrts
add 1(D0) #1 1(D0)
br L3
L4:
	; deactivation start
mov D0 SP
	; do_stuff end
hlt
