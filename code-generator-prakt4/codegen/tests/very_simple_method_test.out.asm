# MIPS Assembly Code Generated
# Data Segment
	.data
_static_x:	.word 0	# static int x

# Text Segment
	.text
	.globl main
# Program: VerySimpleMethodTest
main:
# Method: main
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -40	# Allocate frame (40 bytes)
	sw $ra, 36($sp)	# Save return address
	sw $fp, 32($sp)	# Save frame pointer
	addiu $fp, $sp, 40	# FP points to old SP
	
# Method body
# Prepare method call to helper
	jal _method_helper	# Call method helper
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
# Assignment: x
	li $a0, 42	# Load integer literal 42
	sw $a0, _static_x	# Store to static x
# Epilogue
_method_helper_exit:
	lw $ra, 36($sp)	# Restore return address
	lw $fp, 32($sp)	# Restore frame pointer
	addiu $sp, $sp, 40	# Deallocate frame
	jr $ra	# Return
	
