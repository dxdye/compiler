# MIPS Assembly Code Generated
# Data Segment
	.data
_static_x:	.word 0	# static int x
_static_y:	.word 0	# static int y
_static_flag:	.word 0	# static boolean flag

# Text Segment
	.text
	.globl main
# Program: SimpleTest
main:
# Method: main
	sw $ra, 0($sp)	# Save return address
	sw $fp, -4($sp)	# Save old frame pointer
	move $fp, $sp	# Set up frame pointer
	addiu $sp, $sp, -8	# Allocate space for RA and FP
# Local variable a at offset -4
# Local variable b at offset -8
# Assignment: a
	li $t0, 5	# Load integer literal 5
	sw $t0, -4($fp)	# Store to local a
# Assignment: b
	li $t0, 10	# Load integer literal 10
	sw $t0, -8($fp)	# Store to local b
# Assignment: x
	lw $t0, -4($fp)	# Load local a
	lw $t1, -8($fp)	# Load local b
	add $t0, $t0, $t1	# Add
	sw $t0, _static_x	# Store to static x
# Assignment: y
	lw $t0, _static_x	# Load static x
	li $t1, 2	# Load integer literal 2
	mul $t0, $t0, $t1	# Multiply
	sw $t0, _static_y	# Store to static y
# Assignment: flag
	lw $t0, -4($fp)	# Load local a
	lw $t1, -8($fp)	# Load local b
	slt $t0, $t0, $t1	# Less than
	sw $t0, _static_flag	# Store to static flag
	lw $t0, _static_x	# Load static x
	move $a0, $t0	# Prepare for print_int
	li $v0, 1	# Syscall for print_int
	syscall	# Print integer
	li $a0, 10	# Newline character
	li $v0, 11	# Syscall for print_char
	syscall	# Print newline
	lw $t0, _static_flag	# Load static flag
	move $a0, $t0	# Prepare for print_int
	li $v0, 1	# Syscall for print_int
	syscall	# Print boolean
	li $a0, 10	# Newline character
	li $v0, 11	# Syscall for print_char
	syscall	# Print newline
main_exit:
	move $sp, $fp	# Restore stack pointer
	lw $fp, -4($sp)	# Restore frame pointer
	lw $ra, 0($sp)	# Restore return address
	addiu $sp, $sp, 16	# Deallocate frame
	jr $ra	# Return from method
	
