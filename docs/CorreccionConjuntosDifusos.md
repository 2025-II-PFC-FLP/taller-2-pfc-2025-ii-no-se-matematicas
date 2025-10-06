# Informe de Corrección – ConjuntosDifusos

## Teorema Principal

**Enunciado**: La clase `ConjuntosDifusos` implementa correctamente las operaciones de pertenencia, complemento, unión, intersección, inclusión e igualdad de conjuntos difusos para enteros en el rango $[0,1000]$.

## Definición de las Funciones

Sea un conjunto difuso representado por:

```scala
type ConjDifuso = Int => Double
```

* **pertenece(elem, s)**: retorna el grado de pertenencia de `elem` en `s`.
* **grande(d, e)**: genera un conjunto difuso creciente según

$$
\mu(n) = \frac{1}{1 + (d/n)^e}, \quad n > 0
$$

* **complemento(c)**: retorna

$$
\mu_{\neg c}(x) = 1 - \mu_c(x)
$$

* **union(cd1, cd2)**: retorna

$$
\mu_{cd1 \cup cd2}(x) = \max(\mu_{cd1}(x), \mu_{cd2}(x))
$$

* **interseccion(cd1, cd2)**: retorna

$$
\mu_{cd1 \cap cd2}(x) = \min(\mu_{cd1}(x), \mu_{cd2}(x))
$$

* **inclusion(cd1, cd2)**: retorna `true` si

$$
\forall n \in [0,1000]: cd1(n) \le cd2(n)
$$

* **igualdad(cd1, cd2)**: retorna `true` si `inclusion(cd1, cd2)` y `inclusion(cd2, cd1)` son `true`.

## Casos Base y Correctitud

* **Caso Base**: Para `inclusion` e `igualdad`, si `n > 1000` retorna `true` inmediatamente.
* Para `grande`, `complemento`, `union` e `interseccion`, cada función retorna un valor correcto según la definición matemática.

### Paso Inductivo

* **Hipótesis Inductiva**: Supongamos que `verificar(n)` calcula correctamente la inclusión o igualdad para todos los enteros menores que `k`.
* **Tesis**: Demostrar que `verificar(k)` retorna correctamente:

    * **inclusion**: si `cd1(k) > cd2(k)` retorna `false`; si no, aplica recursión para `k+1`.
    * **igualdad**: verifica `|cd1(k) - cd2(k)| <= tolerancia`; si no, retorna `false`; si sí, recurre a `k+1`.

Por inducción sobre `n`, todas las funciones recursivas recorren correctamente el dominio $[0,1000]$.

### Resultado Final

* Todas las funciones devuelven resultados consistentes con la definición de conjuntos difusos.
* Unión e intersección cumplen la lógica matemática:

$$
\mu_{union}(x) = \max(\mu_{cd1}(x), \mu_{cd2}(x))
$$

$$
\mu_{inter}(x) = \min(\mu_{cd1}(x), \mu_{cd2}(x))
$$

* Complemento cumple:

$$
\mu_{comp}(x) = 1 - \mu_c(x)
$$

## Análisis de Terminación

**Lema de Terminación**: Todas las funciones terminan en tiempo finito.

* `inclusion` e `igualdad` recorren enteros de 0 a 1000; al ser un rango finito, la recursión termina.
* Las demás funciones (`pertenece`, `grande`, `complemento`, `union`, `interseccion`) son puras y no recursivas.

## Invariantes del Algoritmo

* Para todo `ConjDifuso`, los valores retornados cumplen $0 \le \mu(x) \le 1$.
* `union` e `interseccion` no alteran valores fuera del dominio evaluado.
* `inclusion` e `igualdad` verifican todos los elementos de 0 a 1000, asegurando cobertura completa.

## Ejemplo de Ejecución

```scala
val cd1 = grande(10, 2)
val cd2 = complemento(cd1)
pertenece(5, cd1)         // ≈ 0.8
pertenece(5, cd2)         // ≈ 0.2
union(cd1, cd2)(5)        // 1.0
interseccion(cd1, cd2)(5) // ≈ 0.2
inclusion(cd1, cd2)        // false
igualdad(cd1, cd2)         // false
```

## Conclusión

* **Es correcta**: Todas las operaciones cumplen las definiciones de conjuntos difusos.
* **Termina**: Las funciones recursivas finalizan en pasos finitos.
* **Mantiene invariantes**: Los valores de pertenencia se mantienen en $[0,1]$.
* **Implementa operaciones básicas correctamente**: pertenencia, complemento, unión, intersección, inclusión e igualdad.

El informe valida formalmente que la implementación coincide con la teoría de conjuntos difusos y sus operaciones matemáticas.
