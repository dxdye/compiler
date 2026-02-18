# MIPS Assembly Code Generated
# Data Segment
	.data
_static_x:	.word 0	# static int x

# Text Segment
	.text
	.globl main
# Program: SimpleIfTest
main:
# Method: main
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -8	# Allocate frame (8 bytes)
	sw $ra, 4($sp)	# Save return address
	sw $fp, 0($sp)	# Save frame pointer
	addiu $fp, $sp, 8	# FP points to old SP
	
# Method body
# Local variable a at offset -4
# Local variable b at offset -8
# Assignment: a
	li $a0, 5	# Load integer literal 5
	sw $a0, -4($fp)	# Store to local a
# Assignment: b
	li $a0, 10	# Load integer literal 10
	sw $a0, -8($fp)	# Store to local b
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local b to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	slt $a0, $t1, $a0	# Less than: $t1 < $a0
	beq $a0, $zero, if_else_0	# If condition false, go to else
# Assignment: x
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local b to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	add $a0, $t1, $a0	# Add: $t1 + $a0
	sw $a0, _static_x	# Store to static x
	j if_end_1	# Skip else part
if_else_0:
# Assignment: x
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	lw $a0, -8($fp)	# Load local b to accumulator
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	sub $a0, $t1, $a0	# Subtract: $t1 - $a0
	sw $a0, _static_x	# Store to static x
if_end_1:
# Epilogue
main_exit:
	lw $ra, 4($sp)	# Restore return address
	lw $fp, 0($sp)	# Restore frame pointer
	addiu $sp, $sp, 8	# Deallocate frame
	jr $ra	# Return
	
