# MIPS Assembly Code Generated
# Data Segment
	.data
_static_x:	.word 0	# static int x

# Text Segment
	.text
	.globl main
# Program: MultiMethodTest
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
	li $a0, 3	# Load integer literal 3
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
	lw $a0, _static_x	# Load static x to accumulator
	addiu $v0, $zero, 1	# Syscall for print_int
	syscall	# Print integer
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
	
_method_helper:
# Method: helper
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -40	# Allocate frame (40 bytes)
	sw $ra, 36($sp)	# Save return address
	sw $fp, 32($sp)	# Save frame pointer
	addiu $fp, $sp, 40	# FP points to old SP
	
# Method body
# Local variable temp at offset -4
# Assignment: temp
	li $a0, 42	# Load integer literal 42
	sw $a0, -4($fp)	# Store to local temp
# Assignment: x
	lw $a0, -4($fp)	# Load local temp to accumulator
	sw $a0, _static_x	# Store to static x
# Epilogue
_method_helper_exit:
	lw $ra, 36($sp)	# Restore return address
	lw $fp, 32($sp)	# Restore frame pointer
	addiu $sp, $sp, 40	# Deallocate frame
	jr $ra	# Return
	
