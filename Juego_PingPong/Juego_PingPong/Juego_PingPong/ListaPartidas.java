public class ListaPartidas {
    Nodo cabeza;
    int totalPartidas = 0;

    public void insertar(Partida partida) {
        Nodo nuevo = new Nodo(partida);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        totalPartidas++;
    }

    public void listar() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual.partida.mostrar());
            actual = actual.siguiente;
        }
    }

    public int contarVictorias(String jugador) {
        return contarVictoriasRecursivo(cabeza, jugador);
    }

    private int contarVictoriasRecursivo(Nodo actual, String jugador) {
      if (actual == null)
        return 0;
      return (actual.partida.ganador.equals(jugador) ? 1 : 0) +
          contarVictoriasRecursivo(actual.siguiente, jugador);
    }

    public void buscarPorJugador(String nombre) {
      Nodo actual = cabeza;
      boolean encontrado = false;
      while (actual != null) {
        if (actual.partida.ganador.equalsIgnoreCase(nombre)) {
          System.out.println(actual.partida.mostrar());
          encontrado = true;
        }
        actual = actual.siguiente;
      }
      if (!encontrado) {
        System.out.println("No se encontraron partidas ganadas por: " + nombre);
      }
    }
    public void ordenarPorNumeroPartida() {
    if (cabeza == null || cabeza.siguiente == null) return;

    boolean huboCambio;
    do {
        Nodo actual = cabeza;
        Nodo anterior = null;
        Nodo siguiente = cabeza.siguiente;
        huboCambio = false;

        while (siguiente != null) {
            if (actual.partida.numero > siguiente.partida.numero) {
                // Intercambio de nodos
                huboCambio = true;
                if (anterior != null) {
                    Nodo temp = siguiente.siguiente;
                    anterior.siguiente = siguiente;
                    siguiente.siguiente = actual;
                    actual.siguiente = temp;
                } else {
                    Nodo temp = siguiente.siguiente;
                    cabeza = siguiente;
                    siguiente.siguiente = actual;
                    actual.siguiente = temp;
                }
                anterior = siguiente;
                siguiente = actual.siguiente;
            } else {
                anterior = actual;
                actual = siguiente;
                siguiente = siguiente.siguiente;
            }
        }
    } while (huboCambio);
}


}
