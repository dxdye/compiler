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
	addiu $sp, $sp, -40	# Allocate frame (40 bytes)
	sw $ra, 36($sp)	# Save return address
	sw $fp, 32($sp)	# Save frame pointer
	addiu $fp, $sp, 40	# FP points to old SP
	
# Method body
# Local variable x at offset -4
# Local variable y at offset -8
# Local variable z at offset -12
# Local variable test at offset -16
# Local variable flag2 at offset -20
# Assignment: x
	li $a0, 10	# Load integer literal 10
	sw $a0, -4($fp)	# Store to local x
# Assignment: y
	li $a0, 20	# Load integer literal 20
	sw $a0, -8($fp)	# Store to local y
# Assignment: z
	lw $a0, -4($fp)	# Load local x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local y to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	add $a0, $t1, $a0	# Add: $t1 + $a0
	sw $a0, -12($fp)	# Store to local z
# Assignment: test
	lw $a0, -4($fp)	# Load local x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local y to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	slt $a0, $t1, $a0	# Less than: $t1 < $a0
	sw $a0, -16($fp)	# Store to local test
# Assignment: flag2
	lw $a0, -4($fp)	# Load local x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 10	# Load integer literal 10
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	xor $a0, $t1, $a0	# XOR for equality
	sltiu $a0, $a0, 1	# Set if equal (result is 0)
	sw $a0, -20($fp)	# Store to local flag2
	lw $a0, -16($fp)	# Load local test to accumulator
	beq $a0, $zero, if_end_0	# If condition false, skip body
# Assignment: z
	lw $a0, -12($fp)	# Load local z to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 2	# Load integer literal 2
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	mul $a0, $t1, $a0	# Multiply: $t1 * $a0
	sw $a0, -12($fp)	# Store to local z
	lw $a0, -12($fp)	# Load local z to accumulator
	addiu $v0, $zero, 1	# Syscall for print_int
	syscall	# Print integer
	addiu $a0, $zero, 10	# Newline character
	addiu $v0, $zero, 11	# Syscall for print_char
	syscall	# Print newline
	syscall	# Print newline
if_end_0:
# Assignment: globalX
	lw $a0, -4($fp)	# Load local x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local y to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	add $a0, $t1, $a0	# Add: $t1 + $a0
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -12($fp)	# Load local z to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	add $a0, $t1, $a0	# Add: $t1 + $a0
	sw $a0, _static_globalX	# Store to static globalX
# Assignment: globalFlag
	lw $a0, -4($fp)	# Load local x to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 10	# Load integer literal 10
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	xor $a0, $t1, $a0	# XOR for equality
	sltiu $a0, $a0, 1	# Set if equal (result is 0)
	beq $a0, $zero, and_false_0	# AND: short-circuit if left is false
	lw $a0, -8($fp)	# Load local y to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 15	# Load integer literal 15
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	xor $a0, $t1, $a0	# XOR for inequality
	sltu $a0, $zero, $a0	# Set if not equal (result != 0)
	move $t0, $a0	# AND: result from right
	j and_end_0	# Jump to end
and_false_0:
	addiu $t0, $zero, 0	# AND: result is false
and_end_0:
	sw $t0, _static_globalFlag	# Store to static globalFlag
	lw $a0, _static_globalX	# Load static globalX to accumulator
	addiu $v0, $zero, 1	# Syscall for print_int
	syscall	# Print integer
	addiu $a0, $zero, 10	# Newline character
	addiu $v0, $zero, 11	# Syscall for print_char
	syscall	# Print newline
	syscall	# Print newline
	lw $a0, _static_globalFlag	# Load static globalFlag to accumulator
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
	
