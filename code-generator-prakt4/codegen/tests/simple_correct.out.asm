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
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -40	# Allocate frame (40 bytes)
	sw $ra, 36($sp)	# Save return address
	sw $fp, 32($sp)	# Save frame pointer
	addiu $fp, $sp, 40	# FP points to old SP
	
# Method body
# Local variable a at offset -4
# Local variable b at offset -8
# Assignment: a
	li $a0, 5	# Load integer literal 5
	sw $a0, -4($fp)	# Store to local a
# Assignment: b
	li $a0, 10	# Load integer literal 10
	sw $a0, -8($fp)	# Store to local b
# Assignment: x
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local b to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	add $a0, $t1, $a0	# Add: $t1 + $a0
	sw $a0, _static_x	# Store to static x
# Assignment: y
	lw $a0, _static_x	# Load static x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 2	# Load integer literal 2
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	mul $a0, $t1, $a0	# Multiply: $t1 * $a0
	sw $a0, _static_y	# Store to static y
# Assignment: flag
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local b to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	slt $a0, $t1, $a0	# Less than: $t1 < $a0
	sw $a0, _static_flag	# Store to static flag
	lw $a0, _static_x	# Load static x to accumulator
	addiu $v0, $zero, 1	# Syscall for print_int
	syscall	# Print integer
	addiu $a0, $zero, 10	# Newline character
	addiu $v0, $zero, 11	# Syscall for print_char
	syscall	# Print newline
	syscall	# Print newline
	lw $a0, _static_flag	# Load static flag to accumulator
	addiu $v0, $zero, 1	# Syscall for print_int
	syscall	# Print boolean
	addiu $a0, $zero, 10	# Newline character
	addiu $v0, $zero, 11	# Syscall for print_char
	syscall	# Print newline
	syscall	# Print newline
# Epilogue
main_exit:
	lw $ra, 36($sp)	# Restore return address
	lw $fp, 32($sp)	# Restore frame pointer
	addiu $sp, $sp, 40	# Deallocate frame
	jr $ra	# Return
	
