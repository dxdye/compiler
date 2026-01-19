# Test Files Organization

This directory contains all test files for the Simple language compiler.

## Test Files Structure

### Correct Programs (Should Pass All Checks)
- **`simple_correct.sim`** - Basic correct program
  - Tests: variable declarations, assignments, arithmetic, comparisons, print statements
  - Expected: ✅ Parse → ✅ Name Check → ✅ Type Check
  
- **`comprehensive_test.sim`** - Comprehensive correct program
  - Tests: More complex valid constructs
  - Expected: ✅ Parse → ✅ Name Check → ✅ Type Check

- **`test_correct.sim`** - Legacy correct test
- **`test_correct2.sim`** - Legacy correct test 2

### Error Detection Tests (Should Fail Checks)

#### Name Errors
- **`test_name_error.sim`** - Name checking errors
  - Contains: Undeclared variables, duplicate declarations
  - Expected: ✅ Parse → ❌ Name Check

- **`test_name_errors.sim`** - Legacy name errors test

#### Type Errors
- **`test_type_error.sim`** - Type checking errors
  - Contains: Type mismatches, invalid operations
  - Expected: ✅ Parse → ✅ Name Check → ❌ Type Check

- **`test_type_errors.sim`** - Legacy type errors test

- **`type_example.sim`** - Type error examples
  - Contains: Various type violations
  - Expected: ✅ Parse → ✅ Name Check → ❌ Type Check

- **`type_example2.sim`** - Additional type examples
- **`type_example_err.sim`** - Type error examples

### General Test
- **`test.sim`** - General test file

## Output Files
All `.out` files are generated during testing and contain the decompiled AST output.

## Running Tests

### Individual Tests
```bash
make test-simple           # Test simple_correct.sim
make test-comprehensive    # Test comprehensive_test.sim
make test-name-errors      # Test test_name_error.sim
make test-type-errors      # Test test_type_error.sim
make test-type-example     # Test type_example.sim
```

### Test Suites
```bash
make test-correct          # Run all correct program tests
make test-errors           # Run all error detection tests
make test-all              # Run ALL tests
```

### Clean Up
```bash
make clean-tests           # Remove all .out files
```

## Expected Test Results

| Test File | Parsing | Name Check | Type Check |
|-----------|---------|------------|------------|
| simple_correct.sim | ✅ Pass | ✅ Pass | ✅ Pass |
| comprehensive_test.sim | ✅ Pass | ✅ Pass | ✅ Pass |
| test_name_error.sim | ✅ Pass | ❌ Fail | N/A |
| test_type_error.sim | ✅ Pass | ✅ Pass | ❌ Fail |
| type_example.sim | ✅ Pass | ✅ Pass | ❌ Fail |

## Adding New Tests

1. Create a new `.sim` file in this directory
2. Add a corresponding test target in `Makefile`
3. Run the test to verify expected behavior
