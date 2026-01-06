## Buttons
* Enter numbers using `0–9` and `.`
* Perform calculations with `+`, `−`, `×`, `÷`
* Press `=` to show the result
* Press `=` again to repeat the last calculation using the last operator and operand
* `%` converts the current value to a percentage
* `+/-` changes the sign of the current value
* `SQRT` returns the square root of the current value (negative -> `0`)
* `CE` **C**lears the current **E**ntry (the number being entered)
* `C` **C**lears the entire calculation and resets the calculator
* `M+` (**M**emory add) adds the current display to the stored value
* `M-` (**M**emory subtract) subtracts the current display from the stored value
* `MRC` (**M**emory **R**ecall/**C**lear) shows the stored value; press it twice in a row to **C**lear the memory and show `0`

## Calculation rules

* Calculations are done step by step and can be chained (e.g. `7 + 3 + 2 = 12`)
* Unary actions (`%`, `+/-`) always affect the number currently shown
* Memory operations (`M+`, `M-`) always use the number currently shown

## Display behavior

* The screen shows the current input or the last result
* Selecting a binary operator clears the display until the next digit or unary action
* Typing a number after a result starts a new calculation
* Recalling memory displays the stored memory value without clearing the calculation state
* Only one decimal point is allowed per number

## Error handling

* Invalid actions are ignored (e.g. `CE` when no entry is active, operators in the cleared state)
* Division by zero is handled safely
* The calculator stays responsive in all cases
