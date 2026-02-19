# MIPS Assembly Code Generated
# Data Segment
	.data
_static_x:	.word 0	# static int x

# Text Segment
	.text
	.globl main
# Program: SimpleIfOnlyTest
main:
# Method: main
# Prologue (MIPS ABI compliant)
	addiu $sp, $sp, -8	# Allocate frame (8 bytes)
	sw $ra, 4($sp)	# Save return address
	sw $fp, 0($sp)	# Save frame pointer
	addiu $fp, $sp, 8	# FP points to old SP
	
# Method body
# Local variable a at offset -4
# Assignment: a
	li $a0, 5	# Load integer literal 5
	sw $a0, -4($fp)	# Store to local a
	lw $a0, -4($fp)	# Load local a to accumulator
	li $a0, 0	# Load integer literal 0
	slt $a0, $a0, $a0	# Greater than
	beq $a0, $zero, if_end_0	# If condition false, skip body
# Assignment: x
	lw $a0, -4($fp)	# Load local a to accumulator
	addi $sp, $sp, -4	# Allocate stack space
	sw $a0, 0($sp)	# Push left operand
	li $a0, 2	# Load integer literal 2
	lw $t1, 0($sp)	# Pop previous result to temp
	addi $sp, $sp, 4	# Deallocate stack space
	mul $a0, $t1, $a0	# Multiply: $t1 * $a0
	sw $a0, _static_x	# Store to static x
if_end_0:
# Epilogue
main_exit:
	lw $ra, 4($sp)	# Restore return address
	lw $fp, 0($sp)	# Restore frame pointer
	addiu $sp, $sp, 8	# Deallocate frame
	jr $ra	# Return
	
