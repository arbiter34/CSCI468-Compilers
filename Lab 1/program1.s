	.file "program1.pas"
# Begin asmlist al_begin
# End asmlist al_begin
# Begin asmlist al_stabs
# End asmlist al_stabs
# Begin asmlist al_procedures

.section .text
	.balign 8,0x90
.globl	PASCALMAIN
	.type	PASCALMAIN,@function
PASCALMAIN:
.globl	main
	.type	main,@function
main:
.Lc1:
	pushq	%rbp
.Lc3:
.Lc4:
	movq	%rsp,%rbp
.Lc5:
	subq	$16,%rsp
	movq	%rbx,-16(%rbp)
	call	FPC_INITIALIZEUNITS
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rdi
	call	fpc_writeln_end
	call	FPC_IOCHECK
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rdi
	call	fpc_writeln_end
	call	FPC_IOCHECK
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rsi
	movq	$_$TESTER$_Ld1,%rdx
	movl	$0,%edi
	call	fpc_write_text_shortstr
	call	FPC_IOCHECK
	movq	%rbx,%rdi
	call	fpc_write_end
	call	FPC_IOCHECK
	call	fpc_get_input
	movq	%rax,%rbx
	leaq	-8(%rbp),%rsi
	movq	%rbx,%rdi
	call	fpc_read_text_sint
	call	FPC_IOCHECK
	movw	-8(%rbp),%ax
	movw	%ax,U_P$TESTER_I
	movq	%rbx,%rdi
	call	fpc_read_end
	call	FPC_IOCHECK
	movswq	U_P$TESTER_I,%rax
	incq	%rax
	movw	%ax,U_P$TESTER_I
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rsi
	movq	$_$TESTER$_Ld2,%rdx
	movl	$0,%edi
	call	fpc_write_text_shortstr
	call	FPC_IOCHECK
	movswq	U_P$TESTER_I,%rdx
	movq	%rbx,%rsi
	movl	$0,%edi
	call	fpc_write_text_sint
	call	FPC_IOCHECK
	movq	%rbx,%rdi
	call	fpc_writeln_end
	call	FPC_IOCHECK
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rdi
	call	fpc_writeln_end
	call	FPC_IOCHECK
	call	fpc_get_output
	movq	%rax,%rbx
	movq	%rbx,%rdi
	call	fpc_writeln_end
	call	FPC_IOCHECK
	call	FPC_DO_EXIT
	movq	-16(%rbp),%rbx
	leave
	ret
.Lc2:
.Le0:
	.size	main, .Le0 - main

.section .text
# End asmlist al_procedures
# Begin asmlist al_globals

.section .bss
	.balign 2
	.type U_P$TESTER_I,@object
	.size U_P$TESTER_I,2
U_P$TESTER_I:
	.zero 2

.section .data
	.balign 8
.globl	THREADVARLIST_P$TESTER
	.type	THREADVARLIST_P$TESTER,@object
THREADVARLIST_P$TESTER:
	.quad	0
.Le1:
	.size	THREADVARLIST_P$TESTER, .Le1 - THREADVARLIST_P$TESTER

.section .data
	.balign 8
.globl	INITFINAL
	.type	INITFINAL,@object
INITFINAL:
	.long	1,0
	.quad	INIT$_SYSTEM
	.quad	0
.Le2:
	.size	INITFINAL, .Le2 - INITFINAL

.section .data
	.balign 8
.globl	FPC_THREADVARTABLES
	.type	FPC_THREADVARTABLES,@object
FPC_THREADVARTABLES:
	.long	2
	.quad	THREADVARLIST_SYSTEM
	.quad	THREADVARLIST_P$TESTER
.Le3:
	.size	FPC_THREADVARTABLES, .Le3 - FPC_THREADVARTABLES

.section .data
	.balign 8
.globl	FPC_RESOURCESTRINGTABLES
	.type	FPC_RESOURCESTRINGTABLES,@object
FPC_RESOURCESTRINGTABLES:
	.quad	0
.Le4:
	.size	FPC_RESOURCESTRINGTABLES, .Le4 - FPC_RESOURCESTRINGTABLES

.section .data
	.balign 8
.globl	FPC_WIDEINITTABLES
	.type	FPC_WIDEINITTABLES,@object
FPC_WIDEINITTABLES:
	.long	0
.Le5:
	.size	FPC_WIDEINITTABLES, .Le5 - FPC_WIDEINITTABLES

.section .fpc
	.balign 8
	.ascii	"FPC 2.6.2-8 [2014/01/22] for x86_64 - Linux"

.section .data
	.balign 8
.globl	__stklen
	.type	__stklen,@object
__stklen:
	.quad	8388608

.section .data
	.balign 8
.globl	__heapsize
	.type	__heapsize,@object
__heapsize:
	.quad	0

.section .data
.globl	__fpc_valgrind
	.type	__fpc_valgrind,@object
__fpc_valgrind:
	.byte	0

.section .data
	.balign 8
.globl	FPC_RESLOCATION
	.type	FPC_RESLOCATION,@object
FPC_RESLOCATION:
	.quad	0
# End asmlist al_globals
# Begin asmlist al_const
# End asmlist al_const
# Begin asmlist al_typedconsts

.section .rodata
	.balign 8
.globl	_$TESTER$_Ld1
_$TESTER$_Ld1:
	.ascii	"%Please enter an integer value for I: \000"

.section .rodata
	.balign 8
.globl	_$TESTER$_Ld2
_$TESTER$_Ld2:
	.ascii	"\032The current value of I is \000"
# End asmlist al_typedconsts
# Begin asmlist al_rotypedconsts
# End asmlist al_rotypedconsts
# Begin asmlist al_threadvars
# End asmlist al_threadvars
# Begin asmlist al_imports
# End asmlist al_imports
# Begin asmlist al_exports
# End asmlist al_exports
# Begin asmlist al_resources
# End asmlist al_resources
# Begin asmlist al_rtti
# End asmlist al_rtti
# Begin asmlist al_dwarf_frame

.section .debug_frame
.Lc6:
	.long	.Lc8-.Lc7
.Lc7:
	.long	-1
	.byte	1
	.byte	0
	.uleb128	1
	.sleb128	-4
	.byte	16
	.byte	12
	.uleb128	7
	.uleb128	8
	.byte	5
	.uleb128	16
	.uleb128	2
	.balign 4,0
.Lc8:
	.long	.Lc10-.Lc9
.Lc9:
	.quad	.Lc6
	.quad	.Lc1
	.quad	.Lc2-.Lc1
	.byte	4
	.long	.Lc3-.Lc1
	.byte	14
	.uleb128	16
	.byte	4
	.long	.Lc4-.Lc3
	.byte	5
	.uleb128	6
	.uleb128	4
	.byte	4
	.long	.Lc5-.Lc4
	.byte	13
	.uleb128	6
	.balign 4,0
.Lc10:
# End asmlist al_dwarf_frame
# Begin asmlist al_dwarf_info
# End asmlist al_dwarf_info
# Begin asmlist al_dwarf_abbrev
# End asmlist al_dwarf_abbrev
# Begin asmlist al_dwarf_line
# End asmlist al_dwarf_line
# Begin asmlist al_picdata
# End asmlist al_picdata
# Begin asmlist al_resourcestrings
# End asmlist al_resourcestrings
# Begin asmlist al_objc_data
# End asmlist al_objc_data
# Begin asmlist al_objc_pools
# End asmlist al_objc_pools
# Begin asmlist al_end
# End asmlist al_end
.section .note.GNU-stack,"",%progbits

