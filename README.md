# Gale

Gale is a simple language for building time series arrays.
It defines numerical functions and if–else if–else expressions of the number of milliseconds, `$t`.
This package provides a multilayered API and a command-line parser.

**Sine wave from 0–255.**

```
127+sin($t)/2
```

**255 every second, 0 everywhere else.**
```
if $t%1000=0: 255
```

**255 every second, lasting 100ms.**
```
if $t%1000=0: 255  @ 100
```
Here the _evaluation interval_ of `100` causes the expression to be re-evaluated only every 100ms.


**Weird Brownian motion.**
```
if $t<10000: 50 elif $t<2*pow(10,4): 100 else: $[$t-1] + norm(0, 1)  @ 100
```
This takes advantage of _array indexing_ and _random distributions_.
The result is 50 for the first 10s, 100 for the next 10s, and follows Brownian motion after that. The value only gets updated every 100 steps.


#### statements
- `if`, `elif`, `else`
- `@` (evaluate every _n_ milliseconds)

#### functions


- `sqrt`, `√`, `∛`
- `sin`, `cos`, `tan`, `asin`, `acos`, `atan`
- `exp`, `pow`, `exp`, `ln`, `sinh`, `cosh`
- `abs`, `round`, `ceil`, `floor`, `min`, `max`
- `sgn`, `bool` (0 if 0, 1 otherwise)
- `constrain` (between two values)
- `unifR`, `normR`, `betaR`, `gammaR` (random sampling)

#### operators
- `==` or `=`
- `!=` or `≠`
- `<=` or `≤`
- `\>=` or `≥`
- `and` or `∧`
- `or` or `∨`
- `nand` or `⊼`
- `nor` or `⊽`
- `xor` or `⊻`



## formal description

The syntax used here is a slightly flawed EBNF.

```
<series> ::= <modifresult> [('@' <int>]
```

Where the *evaluation interval (i)* is set to 1 by default, and `modifresult` is `ifresult` with these substitutions:

- `$t` → time (index)
- `$[<int>]` → value at previous time `int` (from the integer grammar)

This time-series gets built from index 0 to n, with each interval `ki...(k+1)i` for each integer `k = 1 ... n/i` set to the expression evalutated at `t = ki`.


#### symbols

Whitespace is always ignored, and these operator long forms are converted to what the grammar uses:

- == → =
- != → ≠
- <= → ≤
- \>= → ≥
- and → ∧
- or → ∨
- nand → ⊼
- nor → ⊽
- xor → ⊻


#### real number math

```
<digit>  ::= '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'
<number> ::= <digit>{<digit>} ['.'{<digit>}]
<params> ::= <expr> {',' <expr>}
<fn>     ::= <function> '(' <params> ')'
<factor> ::= <parens>|<fn>|<number>
<parens> ::= '(' <terms> ')'
<opF>    ::= '*'|'×'|'/'|'%'
<opT>    ::= '+'|'-'|'−'
<term>   ::= <factor> {<opF> <factor>}
<real>   ::= <term> {<opT> <term>}
```

#### functions

- sqrt, √, ∛
- sin, cos, tan, asin, acos, atan
- exp, pow, exp, ln, sinh, cosh
- abs, round, ceil, floor, min, max
- sgn, bool (0 if 0, 1 otherwise)
- random sampling: unifR, normR, betaR, gammaR


#### integer math

This is identical to the real number grammar, except that division (`/`) and functions that can yield real numbers are excluded, and `pow` does not allow negative exponents.

#### boolean expressions of real numbers

This requires a *tolerance* for approximately equal (`≈`) and not approximately equal (`≉`).

```
condition   ::= ('='|'≠'|'≈'|≉'|'<'|'>'|'≥'|'≤') <real>
junction    ::= <expr> <condition> {<condition>}
wrapped     ::= '('<bool>')' | <junction>
boolean     ::= <wrapped> {('∧'|'∨'|'⊽'|'⊼'|'⊻') <wrapped>}
```

#### boolean expressions of integers

This is identical to the grammar for boolean expressions of real numbers, except substituting `<int>` for `<real>`.

#### if–elif–else expressions of real numbers

```
<rule>      ::= <bool> ':' <real>
<ifelse>    ::= 'if' <boolean>':' <real> [{'elif' <boolean>':' <real>} 'else:' <real>]
<ifresult>  ::= <real> | <ifelse>
```

#### if–elif–else expressions of integers

This is the same as if–elif–else expressions of real numbers, except substituting `<int>` for `<real>`.

#### if–elif–else expressions of strings

This is the same as if–elif–else expressions of real numbers, except substituting arbitrary strings for `<real>`.
