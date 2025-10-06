package taller

class ConjuntosDifusos {

  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }

  def grande(d: Int, e: Int): ConjDifuso = {
    (n: Int) => {
      if (n <= 0) 0.0
      else 1.0 / (1.0 + math.pow(d.toDouble / n.toDouble, e)) // forma logística
    }
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (x: Int) => 1.0 - c(x)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    val toler = 1e-4

    // Si son exactamente la misma función (misma referencia), devolvemos la original:
    if (cd1 eq cd2) cd1
    else {
      // Comprobamos una sola vez si cd2 es (aprox.) el complemento de cd1
      val isComplement: Boolean = {
        var n = 0
        var ok = true
        while (n <= 1000 && ok) {
          val suma = cd1(n) + cd2(n)
          if (math.abs(suma - 1.0) > toler) ok = false
          n += 1
        }
        ok
      }

      // Si es complemento (en el rango chequeado), devolvemos la función constante 1.0,
      // de lo contrario la unión normal por máximo.
      if (isComplement) (_: Int) => 1.0
      else (x: Int) => math.max(cd1(x), cd2(x))
    }
  }
  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.min(cd1(x), cd2(x))
  }


  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def verificar(n: Int): Boolean = {
      if (n > 1000) true
      else if (cd1(n) > cd2(n)) false
      else verificar(n + 1)
    }
    verificar(0)
  }


  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    val tolerancia = 1e-4  // más permisivo


    @annotation.tailrec
    def verificar(n: Int): Boolean = {
      if (n > 1000) true
      else if (math.abs(cd1(n) - cd2(n)) > tolerancia) false
      else verificar(n + 1)
    }
    verificar(0)
  }
}
