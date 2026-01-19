# MIPS Assembly Code Generated
# Data Segment
	.data
_static_globalX:	.word 0	# static int globalX
_static_globalY:	.word 0	# static int globalY
_static_globalFlag:	.word 0	# static boolean globalFlag

# Text Segment
	.text
	.globl main
# Program: ComprehensiveTest
main:
# Method: main
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -8	# Allocate frame (8 bytes)
	sw $ra, 4($sp)	# Save return address
	sw $fp, 0($sp)	# Save frame pointer
	addiu $fp, $sp, 8	# FP points to old SP
	
# Method body
# Local variable x at offset -4
# Local variable y at offset -8
# Local variable z at offset -12
# Local variable test at offset -16
# Local variable flag2 at offset -20
# Assignment: x
	li $t0, 10	# Load integer literal 10
	sw $t0, -4($fp)	# Store to local x
# Assignment: y
	li $t0, 20	# Load integer literal 20
	sw $t0, -8($fp)	# Store to local y
# Assignment: z
	lw $t0, -4($fp)	# Load local x
	lw $t1, -8($fp)	# Load local y
	add $t0, $t0, $t1	# Add
	sw $t0, -12($fp)	# Store to local z
# Assignment: test
	lw $t0, -4($fp)	# Load local x
	lw $t1, -8($fp)	# Load local y
	slt $t0, $t0, $t1	# Less than
	sw $t0, -16($fp)	# Store to local test
# Assignment: flag2
	lw $t0, -4($fp)	# Load local x
	li $t1, 10	# Load integer literal 10
	xor $t0, $t0, $t1	# XOR for equality
	sltiu $t0, $t0, 1	# Set if equal (result is 0)
	sw $t0, -20($fp)	# Store to local flag2
	lw $t0, -16($fp)	# Load local test
	beq $t0, $zero, if_end_0	# If condition false, skip body
# Assignment: z
	lw $t0, -12($fp)	# Load local z
	li $t1, 2	# Load integer literal 2
	mul $t0, $t0, $t1	# Multiply
	sw $t0, -12($fp)	# Store to local z
	lw $t0, -12($fp)	# Load local z
	move $a0, $t0	# Prepare for print_int
	li $v0, 1	# Syscall for print_int
	syscall	# Print integer
	li $a0, 10	# Newline character
	li $v0, 11	# Syscall for print_char
	syscall	# Print newline
if_end_0:
# Assignment: globalX
	lw $t0, -4($fp)	# Load local x
	lw $t1, -8($fp)	# Load local y
	add $t0, $t0, $t1	# Add
	lw $t1, -12($fp)	# Load local z
	add $t0, $t0, $t1	# Add
	sw $t0, _static_globalX	# Store to static globalX
# Assignment: globalFlag
	beq null, $zero, and_false_0	# AND: short-circuit if left is false
	move $t0, null	# AND: result from right
	j and_end_0	# Jump to end
and_false_0:
	li $t0, 0	# AND: result is false
and_end_0:
	sw $t0, _static_globalFlag	# Store to static globalFlag
	lw $t0, _static_globalX	# Load static globalX
	move $a0, $t0	# Prepare for print_int
	li $v0, 1	# Syscall for print_int
	syscall	# Print integer
	li $a0, 10	# Newline character
	li $v0, 11	# Syscall for print_char
	syscall	# Print newline
	lw $t0, _static_globalFlag	# Load static globalFlag
	move $a0, $t0	# Prepare for print_int
	li $v0, 1	# Syscall for print_int
	syscall	# Print boolean
	li $a0, 10	# Newline character
	li $v0, 11	# Syscall for print_char
	syscall	# Print newline
# Epilogue
main_exit:
	lw $ra, 4($sp)	# Restore return address
	lw $fp, 0($sp)	# Restore frame pointer
	addiu $sp, $sp, 8	# Deallocate frame
	jr $ra	# Return
	
