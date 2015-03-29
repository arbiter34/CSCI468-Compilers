	; do_things start
add SP #1 SP
mov D1 -3(SP)
sub SP #3 D1
	; activation end
	; deactivation start
mov -3(SP) D1
sub SP #1 SP
ret
	; do_things end
	; stuff start
add SP #2 SP
mov D1 -6(SP)
sub SP #6 D1
	; activation end
	; deactivation start
mov -6(SP) D1
sub SP #2 SP
ret
	; stuff end
	; do_stuff start
add SP #0 SP
mov D0 -2(SP)
sub SP #2 D0
	; activation end
	; deactivation start
mov -2(SP) D0
sub SP #0 SP
ret
	; do_stuff end
