package taller

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConjuntosDifusosTest extends AnyFunSuite {
  val obj = new ConjuntosDifusos()
  val epsilon = 0.0001

  // Tests para pertenece
  test("pertenece - evalua funcion caracteristica correctamente") {
    val conj = obj.grande(1, 2)
    val grado = obj.pertenece(10, conj)
    assert(grado >= 0.0 && grado <= 1.0)
  }

  test("pertenece - retorna el mismo valor que aplicar la funcion directamente") {
    val conj = obj.grande(2, 3)
    assert(obj.pertenece(50, conj) == conj(50))
  }

  test("pertenece - funciona con diferentes conjuntos difusos") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(3, 4)
    assert(obj.pertenece(100, g1) != obj.pertenece(100, g2))
  }

  test("pertenece - evalua valores en el limite inferior") {
    val conj = obj.grande(5, 2)
    assert(obj.pertenece(0, conj) == 0.0)
  }

  test("pertenece - evalua valores grandes con alta pertenencia") {
    val conj = obj.grande(1, 3)
    assert(obj.pertenece(500, conj) > 0.95)
  }

  // Tests para grande
  test("grande - numeros negativos tienen grado 0") {
    val g = obj.grande(1, 2)
    assert(g(-5) == 0.0)
    assert(g(-100) == 0.0)
  }

  test("grande - cero tiene grado 0") {
    val g = obj.grande(2, 3)
    assert(g(0) == 0.0)
  }

  test("grande - numeros muy grandes tienden a 1") {
    val g = obj.grande(1, 2)
    assert(g(1000) > 0.999)
  }

  test("grande - con d=1 y e=2, el grado crece adecuadamente") {
    val g = obj.grande(1, 2)
    val g10 = g(10)
    val g50 = g(50)
    val g100 = g(100)
    assert(g10 < g50 && g50 < g100)
  }

  test("grande - diferentes parametros d y e producen conjuntos diferentes") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(5, 3)
    val diferencia = if (g1(20) > g2(20)) g1(20) - g2(20) else g2(20) - g1(20)
    assert(diferencia > 0.01)
  }

  test("grande - con exponente mayor, crece mas rapido") {
    val g2 = obj.grande(1, 2)
    val g5 = obj.grande(1, 5)
    assert(g5(10) > g2(10))
  }

  // Tests para complemento
  test("complemento - suma con original es 1") {
    val g = obj.grande(1, 2)
    val noG = obj.complemento(g)
    val suma = g(25) + noG(25)
    val diferencia = if (suma > 1.0) suma - 1.0 else 1.0 - suma
    assert(diferencia < epsilon)
  }

  test("complemento - grado 0 se convierte en 1") {
    val g = obj.grande(1, 2)
    val noG = obj.complemento(g)
    assert(noG(0) == 1.0)
  }

  test("complemento - doble complemento es el original") {
    val g = obj.grande(2, 3)
    val noG = obj.complemento(g)
    val noNoG = obj.complemento(noG)
    val diferencia = if (g(42) > noNoG(42)) g(42) - noNoG(42) else noNoG(42) - g(42)
    assert(diferencia < epsilon)
  }

  test("complemento - invierte correctamente valores intermedios") {
    val g = obj.grande(10, 2)
    val noG = obj.complemento(g)
    val valorOriginal = g(15)
    val valorComplemento = noG(15)
    val suma = valorOriginal + valorComplemento
    val diferencia = if (suma > 1.0) suma - 1.0 else 1.0 - suma
    assert(diferencia < epsilon)
  }

  test("complemento - multiples valores cumplen la propiedad") {
    val g = obj.grande(3, 4)
    val noG = obj.complemento(g)
    val valores = List(5, 20, 50, 100, 200)
    valores.foreach { v =>
      val suma = g(v) + noG(v)
      val diferencia = if (suma > 1.0) suma - 1.0 else 1.0 - suma
      assert(diferencia < epsilon)
    }
  }

  // Tests para union
  test("union - resultado es al menos tan grande como cada conjunto") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(3, 2)
    val u = obj.union(g1, g2)
    val valor = 30
    assert(u(valor) >= g1(valor) && u(valor) >= g2(valor))
  }

  test("union - toma el maximo de los grados") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(5, 3)
    val u = obj.union(g1, g2)
    val maximo = if (g1(15) > g2(15)) g1(15) else g2(15)
    val diferencia = if (u(15) > maximo) u(15) - maximo else maximo - u(15)
    assert(diferencia < epsilon)
  }

  test("union - es conmutativa") {
    val g1 = obj.grande(2, 2)
    val g2 = obj.grande(4, 3)
    val u1 = obj.union(g1, g2)
    val u2 = obj.union(g2, g1)
    val diferencia = if (u1(35) > u2(35)) u1(35) - u2(35) else u2(35) - u1(35)
    assert(diferencia < epsilon)
  }

  test("union - con conjunto y su complemento da siempre 1") {
    val g = obj.grande(1, 2)
    val noG = obj.complemento(g)
    val u = obj.union(g, noG)
    val valores = List(1, 10, 50, 100, 500)
    valores.foreach { v =>
      val diferencia = if (u(v) > 1.0) u(v) - 1.0 else 1.0 - u(v)
      assert(diferencia < epsilon)
    }
  }

  test("union - es idempotente") {
    val g = obj.grande(2, 3)
    val u = obj.union(g, g)
    val diferencia = if (u(27) > g(27)) u(27) - g(27) else g(27) - u(27)
    assert(diferencia < epsilon)
  }

  // Tests para interseccion
  test("interseccion - resultado es menor o igual que cada conjunto") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(2, 3)
    val i = obj.interseccion(g1, g2)
    val valor = 40
    assert(i(valor) <= g1(valor) && i(valor) <= g2(valor))
  }


  test("interseccion - toma el minimo de los grados") {
    val g1 = obj.grande(1, 3)
    val g2 = obj.grande(4, 2)
    val i = obj.interseccion(g1, g2)
    val minimo = if (g1(22) < g2(22)) g1(22) else g2(22)
    val diferencia = if (i(22) > minimo) i(22) - minimo else minimo - i(22)
    assert(diferencia < epsilon)
  }


  test("interseccion - es conmutativa") {
    val g1 = obj.grande(3, 2)
    val g2 = obj.grande(1, 4)
    val i1 = obj.interseccion(g1, g2)
    val i2 = obj.interseccion(g2, g1)
    val diferencia = if (i1(45) > i2(45)) i1(45) - i2(45) else i2(45) - i1(45)
    assert(diferencia < epsilon)
  }


  test("interseccion - con conjunto y su complemento tiende a 0") {
    val g = obj.grande(2, 2)
    val noG = obj.complemento(g)
    val i = obj.interseccion(g, noG)
    val valores = List(1, 15, 60, 120, 800)
    valores.foreach { v =>
      assert(i(v) < 0.5)
    }
  }


  test("interseccion - es idempotente") {
    val g = obj.grande(3, 3)
    val i = obj.interseccion(g, g)
    val diferencia = if (i(33) > g(33)) i(33) - g(33) else g(33) - i(33)
    assert(diferencia < epsilon)
  }


  // Tests para inclusion
  test("inclusion - todo conjunto esta incluido en si mismo") {
    val g = obj.grande(1, 2)
    assert(obj.inclusion(g, g))
  }


  test("inclusion - conjunto esta incluido en su union con otro") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(3, 3)
    val u = obj.union(g1, g2)
    assert(obj.inclusion(g1, u))
    assert(obj.inclusion(g2, u))
  }


  test("inclusion - interseccion esta incluida en cada conjunto") {
    val g1 = obj.grande(2, 2)
    val g2 = obj.grande(1, 3)
    val i = obj.interseccion(g1, g2)
    assert(obj.inclusion(i, g1))
    assert(obj.inclusion(i, g2))
  }


  test("inclusion - conjunto no esta incluido en su complemento") {
    val g = obj.grande(1, 2)
    val noG = obj.complemento(g)
    assert(!obj.inclusion(g, noG))
  }


  test("inclusion - detecta correctamente no inclusion") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(5, 2)
    assert(!obj.inclusion(g1, g2))
  }


  test("inclusion - conjunto con grados menores esta incluido") {
    val g1 = obj.grande(10, 2)
    val g2 = obj.grande(1, 2)
    assert(obj.inclusion(g1, g2))
  }


  // Tests para igualdad
  test("igualdad - todo conjunto es igual a si mismo") {
    val g = obj.grande(2, 3)
    assert(obj.igualdad(g, g))
  }


  test("igualdad - doble complemento es igual al original") {
    val g = obj.grande(1, 2)
    val noG = obj.complemento(g)
    val noNoG = obj.complemento(noG)
    assert(obj.igualdad(g, noNoG))
  }


  test("igualdad - conjuntos diferentes no son iguales") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(5, 3)
    assert(!obj.igualdad(g1, g2))
  }


  test("igualdad - es simetrica") {
    val g1 = obj.grande(2, 2)
    val g2 = obj.grande(2, 2)
    assert(obj.igualdad(g1, g2) == obj.igualdad(g2, g1))
  }


  test("igualdad - union e interseccion con mismo conjunto son iguales al original") {
    val g = obj.grande(3, 2)
    val u = obj.union(g, g)
    val i = obj.interseccion(g, g)
    assert(obj.igualdad(g, u))
    assert(obj.igualdad(g, i))
  }


  test("igualdad - leyes de De Morgan primera ley") {
    val g1 = obj.grande(1, 2)
    val g2 = obj.grande(3, 2)
    val u = obj.union(g1, g2)
    val compU = obj.complemento(u)
    val compG1 = obj.complemento(g1)
    val compG2 = obj.complemento(g2)
    val interComp = obj.interseccion(compG1, compG2)
    assert(obj.igualdad(compU, interComp))
  }

}