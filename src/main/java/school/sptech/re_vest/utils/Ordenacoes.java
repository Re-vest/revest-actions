package school.sptech.re_vest.utils;

import lombok.experimental.UtilityClass;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Usuario;

import java.util.List;

@UtilityClass
public class Ordenacoes {

    public static void quickSortPreco(List<Produto> produtos, int indInicio, int indFim) {
        int i = indInicio; int j = indFim;
        Produto pivo = produtos.get((indInicio + indFim) / 2);

        while (i < j) {

            while(i < indFim && produtos.get(i).getPreco() < pivo.getPreco()) {
                i = i + 1;
            }

            while(j > indInicio && produtos.get(j).getPreco() > pivo.getPreco()) {
                j = j - 1;
            }

            if (i <= j) {
                Produto temp = produtos.get(i);
                produtos.set(i, produtos.get(j));
                produtos.set(j, temp);
                i = i + 1;
                j = j - 1;
            }

        }

        if (indInicio < j) quickSortPreco(produtos, indInicio, j);
        if (i < indFim) quickSortPreco(produtos, i, indFim);

    }

    public static void inverterOrdemLista(List<Produto> produtos) {
        if (produtos.size() > 1) {
            Produto valor = produtos.remove(0);
            inverterOrdemLista(produtos);
            produtos.add(valor);
        }
    }

    public static void ordenarPorId(List<Usuario> usuarios) {
        for (int i = 0; i < usuarios.size() - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < usuarios.size(); j++) {
                if (usuarios.get(j).getId() < usuarios.get(indiceMenor).getId()) {
                    indiceMenor = j;
                }
            }
            if (indiceMenor != i) {
                Usuario usuarioAuxiliar = usuarios.get(i);
                usuarios.set(i, usuarios.get(indiceMenor));
                usuarios.set(indiceMenor, usuarioAuxiliar);
            }
        }
    }
}
