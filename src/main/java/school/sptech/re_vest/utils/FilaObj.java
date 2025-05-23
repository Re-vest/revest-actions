package school.sptech.re_vest.utils;

public class FilaObj<T> {
    private T[] fila;
    private int tamanho;

    @SuppressWarnings("unchecked")
    public FilaObj(int capacidade) {
        fila = (T[]) new Object[capacidade];
        tamanho = 0;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException("Fila cheia");
        }
        fila[tamanho] = info;
        tamanho++;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return fila[0];
    }

    public T poll() {
        if (isEmpty()) {
            return null;
        }

        T elementoRemovido = fila[0];

        for (int i = 0; i < tamanho - 1; i++) {
            fila[i] = fila[i + 1];
        }

        fila[tamanho - 1] = null;
        tamanho--;

        return elementoRemovido;
    }

    public void exibe() {
        for (int i = 0; i < tamanho; i++) {
            System.out.print(" " + fila[i]);
        }
    }

    public T[] getFila() {
        return fila;
    }

    public void setFila(T[] fila) {
        this.fila = fila;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
}
